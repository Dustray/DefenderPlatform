package cn.dustray.browser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.just.agentweb.AgentWeb;

import java.util.List;

import cn.dustray.utils.PreferenceHelper;
import cn.dustray.control.xWebView;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.utils.Alert;
import cn.dustray.webfilter.FilterUtil;
import cn.dustray.utils.PermissionUtil;
import cn.dustray.utils.xToast;

import static android.view.View.generateViewId;


public class WebTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String homeUrl = "file:///android_asset/html/HomePage.html";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgentWeb mainWebView;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;
    private OnWebViewCreatedListener webListener;

    private Bitmap snapshotBmp;
    private FrameLayout frameLayout;
    private Bundle webState;
    private boolean showPicture = true;
    public boolean isCaptureChanged = false;
    private PreferenceHelper spHelper;

    public WebTabFragment() {
        // Required empty public constructor

    }

    public static WebTabFragment newInstance() {
        WebTabFragment fragment = new WebTabFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spHelper = new PreferenceHelper(getActivity());
        initWebView();
    }

    private void initWebView() {
        if (mainWebView == null) {
            mainWebView = AgentWeb.with(this)
                    .setAgentWebParent(frameLayout, new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()//进度条
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .createAgentWeb()
                    .ready()
                    .go(homeUrl);
        }
        frameLayout = getView().findViewById(R.id.web_frame);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        frameLayout.addView(progressBar);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 5;
        progressBar.setLayoutParams(params);



        WindowManager windowManagers = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManagers.getDefaultDisplay().getMetrics(outMetrics);
        //int width = outMetrics.widthPixels;
        final int screenHeight = outMetrics.heightPixels;
        mainWebView.getWebCreator().getWebView().setOnTouchListener(new View.OnTouchListener() {
            float touchDownPositionY = 0, touchMovePositionY = 0, touchUpPositionY = 0;
            float touchDownPositionX = 0, touchMovePositionX = 0, touchUpPositionX = 0;
            //float touchDownScrollY = 0;//WebView位置
            boolean moveFlag = false;
            int height = screenHeight;
            private final static int GO_BACK_FORWARD_LENGTH = 200;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isCaptureChanged = true;
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    //chatListView.performClick();
                    touchDownPositionY = motionEvent.getY();
                    touchDownPositionX = motionEvent.getX();
                    //touchDownScrollY = mainWebView.getScrollY();//WebView位置
                    moveFlag = true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    //chatListView.performClick();
                    touchUpPositionY = motionEvent.getY();
                    touchUpPositionX = motionEvent.getX();
                }
                //if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (moveFlag) {
                    touchMovePositionY = motionEvent.getY();
                    touchMovePositionX = motionEvent.getX();

                    AppBarLayout mainAppBar = getActivity().findViewById(R.id.main_appbar);
                    // xToast.toast(getActivity(), "" + mainWebView.getScrollY());
                    if (touchDownPositionY - touchMovePositionY > 100) {
                        //往上滑 隐藏toolbar和web tool bar
                        //if (touchDownScrollY != mainWebView.getScrollY())//webview本身位置没变（问题解决）
                        mainAppBar.setExpanded(false, true);
                        moveFlag = false;
                    } else if (touchMovePositionY - touchDownPositionY > 100) {
                        //往下滑 显示lbar和web tool bar
                        //if (touchDownScrollY != mainWebView.getScrollY())//webview本身位置没变
                        mainAppBar.setExpanded(true, true);
                        moveFlag = false;
                    }

                }
                if (height - touchDownPositionY < 450) {
                    if (touchDownPositionX - touchMovePositionX > GO_BACK_FORWARD_LENGTH && Math.abs(touchMovePositionY - touchDownPositionY) < 100) {
                        //从右往左,前进
                        if (canGoForward())
                            xToast.toast(getContext(), "前进");
                        else
                            xToast.toast(getContext(), "别搓了，到头了");
                    } else if (touchMovePositionX - touchDownPositionX > GO_BACK_FORWARD_LENGTH && Math.abs(touchMovePositionY - touchDownPositionY) < 100) {
                        if (canGoBack())
                            xToast.toast(getContext(), "后退");
                        else
                            xToast.toast(getContext(), "别搓了，到头了");
                    }
                    //抬起
                    if (touchDownPositionX - touchUpPositionX > GO_BACK_FORWARD_LENGTH && Math.abs(touchUpPositionY - touchDownPositionY) < 100) {
                        //从右往左,前进
                        goForward();
                    } else if (touchUpPositionX - touchDownPositionX > GO_BACK_FORWARD_LENGTH && Math.abs(touchUpPositionY - touchDownPositionY) < 100) {
                        goBack();
                    }

                }
                // }
                return false;
            }
        });

        if (webState != null) {
            mainWebView.getWebCreator().getWebView().restoreState(webState);

        } else {
            loadUrl(homeUrl);
        }
        homeUrl = "file:///android_asset/html/HomePage.html";
        if (webListener != null) {
            webListener.onWebViewCreateFinished();
        }
    }
    private WebViewClient mWebViewClient=new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //xToast.toast(getActivity(), "111");
            //过滤
            FilterUtil sf = new FilterUtil(getActivity());
            if (!spHelper.getIsNoFilter() && !sf.filterWebsite(request.getUrl().toString())) {
                xToast.toast(getActivity(), "已拦截，关键字：" + sf.getFilterKey());
                mainWebView.getUrlLoader().stopLoading();
                mainWebView.back();
            }

            //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)}
            boolean flag = openApp(request.getUrl().toString(), getActivity());
//                if (Build.VERSION.SDK_INT < 26||Build.VERSION.SDK_INT==28) {
//                    return flag;
//                }
            return flag;
            //return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


            progressBar.setVisibility(View.VISIBLE);
            //隐藏toolbar
            AppBarLayout mainAppBar = getActivity().findViewById(R.id.main_appbar);
            mainAppBar.setExpanded(false, true);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if (webListener != null) {
                webListener.onWebViewCreateFinished();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    };
    private WebChromeClient mWebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

            if (webListener != null) {
                webListener.onOpenNewWebTab(view.getUrl());
            }
            return true; //super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        //定位回调函数
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            // super.onGeolocationPermissionsShowPrompt(origin, callback);
            PermissionUtil.Location(getActivity());//权限申请

            final boolean remember = true;
            Alert alert = new Alert(getActivity());
            alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                @Override
                public void onClickOk() {
                    callback.invoke(origin, true, remember);
                }

                @Override
                public void onClickCancel() {
                    callback.invoke(origin, false, remember);
                }
            });
            alert.popupAlert(origin + "想使用你的位置信息。", "允许", "拒绝");
        }
    };
    //判断app是否安装
    private boolean isInstall(Intent intent) {
        //Log.i("browser", "非https--->");
        return getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
    //打开app
    public boolean openApp(String url, final Context activityContext) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {

                // Log.i("browser", "非https--->"+url);
                Uri uri = Uri.parse(url);
                String host = uri.getHost();
                String scheme = uri.getScheme();
                //host 和 scheme 都不能为null
                if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(scheme)) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (isInstall(intent)) {
                        //Log.i("browser", "已安装");
                        Alert alert = new Alert(activityContext);
                        alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                            @Override
                            public void onClickOk() {
                                getActivity().startActivity(intent);
                                xToast.toast(getContext(), "正在跳转，请稍后");
                            }

                            @Override
                            public void onClickCancel() {
                            }
                        });
                        String appName = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).get(0).loadLabel(getContext().getPackageManager()).toString();
                        alert.popupAlert("网站请求打开“" + appName + "”，是否同意？", "同意");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    public void setWebView(AgentWeb web) {
        mainWebView = web;
    }

    public void setWebState(Bundle state) {
        this.webState = state;
    }

    public Bundle getWebState() {
        return this.webState;
    }

    public void setHomeUrl(String url) {
        homeUrl = url;
    }

    public void setFatherFrag(Fragment frag) {
        webListener = (OnWebViewCreatedListener) frag;
    }

    public void goHome() {
        mainWebView.getUrlLoader().loadUrl(homeUrl);
    }

    public void refresh() {
        mainWebView.getUrlLoader().reload();
    }

    public boolean canGoBack() {
        return mainWebView.getWebCreator().getWebView().canGoBack();
    }

    public boolean goBack() {
          return  mainWebView.back();
    }

    public boolean canGoForward() {
        return mainWebView.getWebCreator().getWebView().canGoForward();
    }

    public void goForward() {
        if (mainWebView.getWebCreator().getWebView().canGoForward())
            mainWebView.getWebCreator().getWebView().goForward();
    }

    public void setShowPicture(boolean showPicture) {
        this.showPicture = showPicture;
        if (mainWebView != null)
            mainWebView.getAgentWebSettings().getWebSettings().setLoadsImagesAutomatically(showPicture); // 加载图片
    }

    public void loadUrl(String str) {
        mainWebView.getUrlLoader().loadUrl(str);
    }

    public void generateSnapshot() {
        if (snapshotBmp == null || isCaptureChanged) {//如果是空或者改变过就获取新截图，否则不做处理
            Bitmap bmp =getCapture();
            if (bmp == null) {
                snapshotBmp = Bitmap.createBitmap(450, 800, Bitmap.Config.RGB_565);
            } else {
                snapshotBmp = bmp;
            }
            isCaptureChanged = false;
        }


    }
    public Bitmap getCapture() {
        this.destroyDrawingCache();
        this.setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
        this.buildDrawingCache();
        Bitmap temp = this.getDrawingCache();
        Bitmap bmp;
//        if (temp == null) {
//            bmp = Bitmap.createBitmap(450, 800, Bitmap.Config.RGB_565);
//        } else {
        if (temp == null) return null;
        bmp = Bitmap.createScaledBitmap(temp, 450, 800, true);
        //}
        //this.setDrawingCacheEnabled(false);
        return bmp;
    }

    public void setSnapshotBmp(Bitmap snapshotBmp) {
        this.snapshotBmp = snapshotBmp;
    }

    public Bitmap getSnapshot() {
        if (snapshotBmp == null) {
            snapshotBmp = Bitmap.createBitmap(450, 800, Bitmap.Config.RGB_565);
        }
        return snapshotBmp;
    }

    public void cleanCache() {
        mainWebView.cleanCache();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_tab, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存页面状态
//                if (webState == null)
//                    webState = new Bundle(ClassLoader.getSystemClassLoader());
//                mainWebView.saveState(webState);
        List<WebTabFragment> array = ((MainActivity) getActivity()).browserFragment.webFragArray;
        WebViewManager manager = new WebViewManager(getActivity().getApplication(), array, getActivity());
        manager.saveToFile();
    }

    @Override
    public void onResume() {
        super.onResume();

        mainWebView.getSettings().setLoadsImagesAutomatically(showPicture); // 加载图片
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDestroy() {
        if (mainWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mainWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mainWebView);
            }

            mainWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mainWebView.getSettings().setJavaScriptEnabled(false);
            mainWebView.clearHistory();
            mainWebView.removeAllViews();
            mainWebView.destroy();
        }
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        webListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mainWebView.saveState(outState);
        this.webState = outState;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnWebViewCreatedListener {
        void onWebViewCreateFinished();

        void onOpenNewWebTab(String Url);
    }


}
