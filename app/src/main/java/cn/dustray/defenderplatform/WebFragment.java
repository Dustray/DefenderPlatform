package cn.dustray.defenderplatform;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.popupwindow.WebGroupPopup;
import cn.dustray.popupwindow.WebMenuPopup;


public class WebFragment extends Fragment implements View.OnClickListener, WebItemFragment.OnWebViewCreatedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ImageButton btnBack, btnGo, btnMenu, btnGroup, btnShare;
    private LinearLayout webToolBar;
    List<WebItemFragment> webFragArray = new ArrayList<>();

    private WebItemFragment webFrag;
    private FragmentManager manager;
    private WebGroupPopup webGroupPopup;

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
        initFragment();
        initWebToolBar();
        //btnBack.setImageBitmap(mainWebView.getCapture());
    }

    private void initFragment() {
        manager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_top_enter, R.animator.fragment_slide_bottom_exit);
        webFrag = WebItemFragment.newInstance(this);
        transaction.add(R.id.web_main_frag, webFrag);
        transaction.commit();
        webFragArray.add(webFrag);
    }

    public boolean canGoBack() {
        return webFrag.canGoBack();
    }

    public void goBack() {
        webFrag.goBack();
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

    public void createNewFragment() {
        initFragment();
    }

    public void loadFragment(WebItemFragment fragment) {
//        webFrag = fragment;
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.web_main_frag, fragment);
//        transaction.commit();
        if (webGroupPopup != null) {
            webGroupPopup.dismiss();
        }
        switchContent(webFrag, fragment);
        judgeWebState();
    }

    public void switchContent(WebItemFragment from, WebItemFragment to) {
        if (webFrag != to) {
            webFrag = to;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.animator.fragment_slide_top_enter, R.animator.fragment_slide_bottom_exit);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.web_main_frag, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }

        }
    }

    public void search(String searchStr) {
        webFrag.loadUrl("https://m.baidu.com/s?from=1012852p&word=" + searchStr);
    }

    private void judgeWebState() {
        if (webFrag.canGoBack()) {
            btnBack.setImageResource(R.drawable.ic_btn_back_black);
        } else {
            btnBack.setImageResource(R.drawable.ic_btn_back_gray);
        }
        if (webFrag.canGoForward()) {
            btnGo.setImageResource(R.drawable.ic_btn_go_black);
        } else {
            btnGo.setImageResource(R.drawable.ic_btn_go_gray);
        }
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
                if (webFrag.canGoBack())
                    webFrag.goBack();
                judgeWebState();
                break;
            case R.id.btn_web_tool_go:
                if (webFrag.canGoForward())
                    webFrag.goForward();
                judgeWebState();
                break;
            case R.id.btn_web_tool_menu:
                new WebMenuPopup(getActivity()).showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_group:
                //webGroupArray.add(mainWebView);
                webGroupPopup = new WebGroupPopup(getActivity(), webFragArray);
                webGroupPopup.showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_share:

                break;
        }
    }

    @Override
    public void onWebViewCreateFinished() {

        judgeWebState();
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
