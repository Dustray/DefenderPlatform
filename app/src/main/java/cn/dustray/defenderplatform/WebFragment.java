package cn.dustray.defenderplatform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.control.xWebView;
import cn.dustray.popupwindow.WebGroupPopup;
import cn.dustray.popupwindow.WebMenuPopup;


public class WebFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private xWebView mainWebView;

    private ImageButton btnBack, btnGo, btnMenu, btnGroup, btnShare;
    private ProgressBar progressBar;
    private LinearLayout webToolBar;
    List<xWebView> webGroupArray = new ArrayList<>();

    public WebFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WebFragment newInstance() {
        WebFragment fragment = new WebFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initWebView();

        initWebToolBar();
        progressBar = getActivity().findViewById(R.id.web_progressbar);
        //btnBack.setImageBitmap(mainWebView.getCapture());
    }

    private void initWebView() {
        mainWebView = getView().findViewById(R.id.main_webview);
        mainWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                if (url == null) return false;
//
//                try {
//                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        view.loadUrl(url);
//                        return true;
//                    } else {
//                        Toast.makeText(getActivity(), "交流,角楼" , Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return false;
//                    }
//                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
//                    return false;
//                }
                return false;
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
                judgeWebState();
                progressBar.setVisibility(View.INVISIBLE);

            }

        });

        mainWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);

            }

        });

        mainWebView.setOnTouchListener(new View.OnTouchListener() {
            float touchDownPosition = 0, touchUpPosition = 0;
            boolean moveFlag = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    //chatListView.performClick();
                    touchDownPosition = motionEvent.getY();
                    moveFlag = true;
                }
                //if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (moveFlag) {
                    touchUpPosition = motionEvent.getY();
                    AppBarLayout mainAppBar = getActivity().findViewById(R.id.main_appbar);
                    if (touchDownPosition - touchUpPosition > 50) {
                        //往上滑 隐藏toolbar和web tool bar
                        // Toast.makeText(getActivity(), "弹键盘", Toast.LENGTH_LONG).show();
                        mainAppBar.setExpanded(false, true);
                        moveFlag = false;
                    } else if (touchUpPosition - touchDownPosition > 50) {
                        //往下滑 显示lbar和web tool bar
                        mainAppBar.setExpanded(true, true);
                        moveFlag = false;

                    }
                }
                // }
                return false;
            }
        });
        mainWebView.loadUrl("https://www.bing.com/");
        webGroupArray.add(mainWebView);//入列
    }

    private xWebView initWebView(xWebView web) {
        web = getView().findViewById(R.id.main_webview);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                if (url == null) return false;
//
//                try {
//                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        view.loadUrl(url);
//                        return true;
//                    } else {
//                        Toast.makeText(getActivity(), "交流,角楼" , Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return false;
//                    }
//                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
//                    return false;
//                }
                return false;
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
                judgeWebState();
                progressBar.setVisibility(View.INVISIBLE);

            }

        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);

            }

        });

        web.setOnTouchListener(new View.OnTouchListener() {
            float touchDownPosition = 0, touchUpPosition = 0;
            boolean moveFlag = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    //chatListView.performClick();
                    touchDownPosition = motionEvent.getY();
                    moveFlag = true;
                }
                //if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (moveFlag) {
                    touchUpPosition = motionEvent.getY();
                    AppBarLayout mainAppBar = getActivity().findViewById(R.id.main_appbar);
                    if (touchDownPosition - touchUpPosition > 50) {
                        //往上滑 隐藏toolbar和web tool bar
                        // Toast.makeText(getActivity(), "弹键盘", Toast.LENGTH_LONG).show();
                        mainAppBar.setExpanded(false, true);
                        moveFlag = false;
                    } else if (touchUpPosition - touchDownPosition > 50) {
                        //往下滑 显示lbar和web tool bar
                        mainAppBar.setExpanded(true, true);
                        moveFlag = false;

                    }
                }
                // }
                return false;
            }
        });
        web.loadUrl("https://www.bing.com/");
        return web;
    }

    private void initWebToolBar() {
        webToolBar = getActivity().findViewById(R.id.web_tool_bar);

        btnBack = getActivity().findViewById(R.id.btn_web_tool_back);
        btnGo = getActivity().findViewById(R.id.btn_web_tool_go);
        btnMenu = getActivity().findViewById(R.id.btn_web_tool_menu);
        btnGroup = getActivity().findViewById(R.id.btn_web_tool_group);
        btnShare = getActivity().findViewById(R.id.btn_web_tool_share);

        btnBack.setOnClickListener(this);
        btnGo.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnGroup.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    public boolean canGoBack() {
        return mainWebView.canGoBack();
    }

    public void goBack() {
        if (mainWebView.canGoBack())
            mainWebView.goBack();
        judgeWebState();
    }

    private void judgeWebState() {
        if (mainWebView.canGoBack()) {
            btnBack.setImageResource(R.drawable.ic_btn_back_black);
        } else {
            btnBack.setImageResource(R.drawable.ic_btn_back_gray);
        }
        if (mainWebView.canGoForward()) {
            btnGo.setImageResource(R.drawable.ic_btn_go_black);
        } else {
            btnGo.setImageResource(R.drawable.ic_btn_go_gray);
        }
    }

    public void createNewWeb() {
        xWebView web = new xWebView(getActivity());
        //mainWebView = web;
        web = initWebView(web);
        webGroupArray.add(web);//入列
        mainWebView = web;
    }

    public void loadWeb(xWebView web) {
        mainWebView = web;
    }

    public void search(String searchStr) {
        mainWebView.loadUrl("https://m.baidu.com/s?from=1012852p&word=" + searchStr);
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
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_web_tool_back:
                goBack();

                break;
            case R.id.btn_web_tool_go:
                if (mainWebView.canGoForward())
                    mainWebView.goForward();
                judgeWebState();
                break;
            case R.id.btn_web_tool_menu:
                new WebMenuPopup(getActivity()).showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_group:
                //webGroupArray.add(mainWebView);
                new WebGroupPopup(getActivity(), webGroupArray).showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_share:

                break;
        }
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
}
