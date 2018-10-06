package cn.dustray.defenderplatform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.dustray.control.xWebView;


public class WebItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public xWebView mainWebView;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;
    private OnWebViewCreatedListener webListener;

    @SuppressLint("ValidFragment")
    public WebItemFragment(Fragment frag) {
        // Required empty public constructor
        webListener = (OnWebViewCreatedListener) frag;
    }

    public static WebItemFragment newInstance(Fragment frag) {
        WebItemFragment fragment = new WebItemFragment(frag);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWebView();
        progressBar = getActivity().findViewById(R.id.web_progressbar);
        //btnBack.setImageBitmap(mainWebView.getCapture());
    }

    private void initWebView() {
        mainWebView = getView().findViewById(R.id.main_webview);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.getSettings().setDomStorageEnabled(true);
        mainWebView.getSettings().setLoadsImagesAutomatically(true); // 加载图片
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
                if (webListener != null) {
                    webListener.onWebViewCreateFinished();
                }

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
        if (webListener != null) {
            webListener.onWebViewCreateFinished();
        }
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

    public void loadUrl(String str) {
        mainWebView.loadUrl(str);
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
        return inflater.inflate(R.layout.fragment_web_item, container, false);
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
        webListener = null;
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
    }
}
