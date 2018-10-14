package cn.dustray.defenderplatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import cn.dustray.browser.WebViewManager;
import cn.dustray.control.xWebView;
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

    public xWebView mainWebView;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;
    private OnWebViewCreatedListener webListener;

    private Bitmap snapshotBmp;
    private FrameLayout frameLayout;
    private Bundle webState;
    private boolean showPicture = true;
    public boolean isCaptureChanged = false;

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

        initWebView();
    }


    private void initWebView() {
        if (mainWebView == null) {
            mainWebView = new xWebView(getActivity().getApplicationContext());
            mainWebView.setId(generateViewId());
        }
        frameLayout = getView().findViewById(R.id.web_frame);
        frameLayout.addView(mainWebView);
        //xToast.toast(getActivity(), "addnew");
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        frameLayout.addView(progressBar);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 5;
        progressBar.setLayoutParams(params);

        mainWebView.getSettings().setLoadsImagesAutomatically(showPicture); // 加载图片
        mainWebView.setWebViewClient(new WebViewClient() {
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

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mainWebView.loadUrl(homeUrl);
            }

        });

        mainWebView.setWebChromeClient(new WebChromeClient() {

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
        });
        WindowManager windowManagers = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManagers.getDefaultDisplay().getMetrics(outMetrics);
        //int width = outMetrics.widthPixels;
        final int screenHeight = outMetrics.heightPixels;
        mainWebView.setOnTouchListener(new View.OnTouchListener() {
            float touchDownPositionY = 0, touchMovePositionY = 0, touchUpPositionY = 0;
            float touchDownPositionX = 0, touchMovePositionX = 0, touchUpPositionX = 0;
            boolean moveFlag = false, isToastEdge = false;
            int height = screenHeight;


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isCaptureChanged = true;
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    //chatListView.performClick();
                    touchDownPositionY = motionEvent.getY();
                    touchDownPositionX = motionEvent.getX();
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
                    if (touchDownPositionY - touchMovePositionY > 50) {
                        //往上滑 隐藏toolbar和web tool bar
                        mainAppBar.setExpanded(false, true);
                        moveFlag = false;
                    } else if (touchMovePositionY - touchDownPositionY > 50) {
                        //往下滑 显示lbar和web tool bar
                        mainAppBar.setExpanded(true, true);
                        moveFlag = false;

                    }

                }
                if (height - touchDownPositionY < 450) {
                    if (touchDownPositionX - touchMovePositionX > 300 && Math.abs(touchMovePositionY - touchDownPositionY) < 100) {
                        //从右往左,前进
                        xToast.toast(getContext(), "前进");

                            isToastEdge = true;

                    } else if (touchMovePositionX - touchDownPositionX > 300 && Math.abs(touchMovePositionY - touchDownPositionY) < 100) {
                        xToast.toast(getContext(), "后退");

                    } 
                    //抬起
                    if (touchDownPositionX - touchUpPositionX > 300 && Math.abs(touchUpPositionY - touchDownPositionY) < 100) {
                        //从右往左,前进
                        goForward();
                    } else if (touchUpPositionX - touchDownPositionX > 300 && Math.abs(touchUpPositionY - touchDownPositionY) < 100) {
                        goBack();
                    }

                }
                // }
                return false;
            }
        });

        if (webState != null) {
            mainWebView.restoreState(webState);

        } else {
            loadUrl(homeUrl);
        }
        homeUrl = "file:///android_asset/html/HomePage.html";
        if (webListener != null) {
            webListener.onWebViewCreateFinished();
        }
    }

    public void setWebView(xWebView web) {
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
        mainWebView.loadUrl(homeUrl);
    }

    public void refresh() {
        mainWebView.reload();
    }

    public boolean canGoBack() {
        return mainWebView.canGoBack();
    }

    public void goBack() {
        if (mainWebView.canGoBack())
            mainWebView.goBack();
    }

    public boolean canGoForward() {
        return mainWebView.canGoForward();
    }

    public void goForward() {
        if (mainWebView.canGoForward())
            mainWebView.goForward();
    }

    public void setShowPicture(boolean showPicture) {
        this.showPicture = showPicture;
        if (mainWebView != null)
            mainWebView.getSettings().setLoadsImagesAutomatically(showPicture); // 加载图片
    }

    public void loadUrl(String str) {
        mainWebView.loadUrl(str);
    }

    public void generateSnapshot() {
        if (snapshotBmp == null || isCaptureChanged) {//如果是空或者改变过就获取新截图，否则不做处理
            Bitmap bmp = mainWebView.getCapture();
            if (bmp == null) {
                snapshotBmp = Bitmap.createBitmap(450, 800, Bitmap.Config.RGB_565);
            } else {
                snapshotBmp = bmp;
            }
            isCaptureChanged = false;
        }


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
        List<WebTabFragment> array = ((MainActivity) getActivity()).webFragment.webFragArray;
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
