package cn.dustray.defenderplatform;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.popupwindow.WebGroupPopup;
import cn.dustray.popupwindow.WebMenuPopup;
import cn.dustray.popupwindow.WebSharePopup;
import cn.dustray.tool.xToast;


public class WebFragment extends Fragment implements View.OnClickListener, WebTabFragment.OnWebViewCreatedListener
        , View.OnLongClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ImageButton btnBack, btnGo, btnMenu, btnGroup, btnShare;
    private LinearLayout webToolBar;
    List<WebTabFragment> webFragArray = new ArrayList<WebTabFragment>();

    private WebTabFragment webFrag;
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

        initWebToolBar();
        initFragment();
        //btnBack.setImageBitmap(mainWebView.getCapture());
    }

    private void initFragment() {
        String Url = "";
        initFragment(Url);
    }

    private void initFragment(String Url) {

        manager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit);
        if (Url.equals("")) {
            webFrag = WebTabFragment.newInstance(this);
        } else {
            webFrag = WebTabFragment.newInstance(this, Url);
        }
        transaction.add(R.id.web_main_frag, webFrag);
        transaction.commit();
        webFragArray.add(webFrag);
        refreshGroupIcon();
    }

    public void goHome() {
        webFrag.goHome();
    }

    public void refresh() {
        webFrag.refresh();
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
        btnMenu.setOnLongClickListener(this);
        btnGroup.setOnClickListener(this);
        btnGroup.setOnLongClickListener(this);
        btnShare.setOnClickListener(this);

    }

    public void createNewFragment() {
        initFragment();
    }

    public void createNewFragment(String Url) {
        initFragment(Url);
    }

    public void loadFragment(WebTabFragment fragment) {
        if (webGroupPopup != null) {
            webGroupPopup.dismiss();
        }
        switchContent(webFrag, fragment);
        judgeWebState();
    }

    public void refreshGroupIcon() {

        switch ( webFragArray.size()) {
            case 1:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_1);
                break;
            case 2:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_2);
                break;
            case 3:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_3);
                break;
            case 4:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_4);
                break;
            case 5:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_5);
                break;
            case 6:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_6);
                break;
            case 7:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_7);
                break;
            case 8:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_8);
                break;
            case 9:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_9);
                break;
            default:
                btnGroup.setImageResource(R.drawable.ic_btn_group_black_9_);
                break;
        }
    }

    public void switchContent(WebTabFragment from, WebTabFragment to) {
        if (webFrag != to) {
            webFrag = to;
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.web_main_frag, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }

        }
    }

    public void search(String searchStr) {
        createNewFragment("https://m.baidu.com/s?word=" + searchStr);

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

        AppBarLayout mainAppBar = getActivity().findViewById(R.id.main_appbar);
        mainAppBar.setExpanded(false, true);
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
                new WebSharePopup(getActivity(), webFrag.mainWebView.getTitle(), webFrag.mainWebView.getUrl()).showAtBottom(webToolBar);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {

            case R.id.btn_web_tool_menu:
                goHome();
                xToast.toast(getActivity(), "回到首页");
                break;
            case R.id.btn_web_tool_group:
                createNewFragment();
                xToast.toast(getActivity(), "新建标签页");
                break;

        }
        return true;
    }

    @Override
    public void onWebViewCreateFinished() {

        judgeWebState();
    }

    @Override
    public void onOpenNewWebTab(String Url) {
        createNewFragment(Url);
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
