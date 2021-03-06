package cn.dustray.browser;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.dustray.control.xWebView;
import cn.dustray.defenderplatform.R;
import cn.dustray.entity.AppSettingEntity;
import cn.dustray.popupwindow.WebGroupPopup;
import cn.dustray.popupwindow.WebMenuPopup;
import cn.dustray.popupwindow.WebSharePopup;
import cn.dustray.utils.PermissionUtil;
import cn.dustray.utils.SettingUtil;
import cn.dustray.utils.SqliteUtil;
import cn.dustray.utils.WebUtil;
import cn.dustray.utils.xToast;
import cn.dustray.webfilter.FilterUtil;


public class BrowserFragment extends Fragment implements View.OnClickListener, WebTabFragment.OnWebViewCreatedListener
        , View.OnLongClickListener, WebMenuPopup.OnWebMenuBtnClick {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String newTabUrl;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ImageButton btnBack, btnGo, btnMenu, btnGroup, btnShare;
    private LinearLayout webToolBar;
    public List<WebTabFragment> webFragArray = new ArrayList<WebTabFragment>();

    private WebTabFragment webFrag;
    private FragmentManager manager;
    private WebGroupPopup webGroupPopup;
    private WebSharePopup webSharePopup;
    private WebMenuPopup webMenuPopup;
    private FilterUtil sf;
    private SqliteUtil su;
    //浏览器配置
    private boolean isNightMode = false;
    public int currentItem = 0;

    public BrowserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BrowserFragment newInstance() {
        BrowserFragment fragment = new BrowserFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取保存成文件的webview 状态（state）
        WebViewManager webManager = new WebViewManager(getActivity().getApplication(), webFragArray, getActivity());
        webManager.readSavedStateFromDisk();
        //FragmentManager
        manager = getActivity().getSupportFragmentManager();
        //初始化webtoolbar
        initWebToolBar();
        //初始化屏蔽引擎
        sf = new FilterUtil(getActivity());
        su = new SqliteUtil(getActivity());
        //xToast.toast(getActivity(), "s" + webFragArray.size());
        if (webFragArray.size() == 0) {//未从文件中获取缓存的页面
            initFragment();
        } else {
            initFragment(webFragArray);
        }
        //btnBack.setImageBitmap(mainWebView.getCapture());
        if (newTabUrl != null) {
            initFragment(newTabUrl);
            newTabUrl = null;
        }
    }

    private void initFragment() {
        String Url = "";
        initFragment(Url);
    }

    private void initFragment(String Url) {
//TODO 此处应用后台重新加载activity， getSupportFragmentManager()返回空指针

        // if (manager == null) manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit);

        webFrag = WebTabFragment.newInstance();
        webFrag.setFatherFrag(this);
        if (!Url.equals("")) {
            webFrag.setHomeUrl(Url);
        }
        webFrag.setShowPicture(!SettingUtil.NoPicMode.getNoPicMode(getContext()));
        transaction.add(R.id.web_main_frag, webFrag);
        transaction.commit();
        webFragArray.add(webFrag);
        refreshGroupIcon();
    }

    public boolean isWebFragNull() {
        return webFrag == null;
    }

    private void initFragment(List<WebTabFragment> array) {
        webFrag = array.get(array.size() - 1);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit);

        webFrag.setFatherFrag(this);

        transaction.add(R.id.web_main_frag, webFrag);
        transaction.commit();

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

    public void createNewFragment(FragmentManager manager, String Url) {
        if (this.manager == null)
            this.manager = manager;
        initFragment(Url);
    }

    public void loadFragment(WebTabFragment fragment, int currentItem, boolean closePopup) {
        this.currentItem = currentItem;
        if (closePopup && webGroupPopup != null) {
            webGroupPopup.dismiss();
        }
        switchContent(webFrag, fragment);
        judgeWebState();
    }

    public void refreshGroupIcon() {

        switch (webFragArray.size()) {
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

    public void search(String searchStr, int from) {
        String test = searchStr;
        String processResult = "";
        // test = test.replace("/", "");
//        if (test.endsWith(".cn") || test.endsWith(".com") || test.endsWith(".org") || test.endsWith(".net") || test.endsWith(".xin") ||
//                test.endsWith(".edu") || test.endsWith(".top") || test.endsWith(".cc") || test.endsWith(".tv") || test.endsWith(".co") ||
//                test.endsWith(".site") || test.endsWith(".biz") || test.endsWith(".vip") || test.endsWith(".wang") || test.endsWith(".ltd")) {
        if (WebUtil.isURL(test)) {
            //如果是网址
            if (!test.startsWith("http://") && !test.startsWith("https://")) {
                //如果不以http开头
                processResult = "http://" + searchStr;
            } else {
                //如果以http开头
                processResult = searchStr;
            }
        } else {
            //如果不是网址
            processResult = "https://m.baidu.com/s?word=" + searchStr;
        }
        if (from == 0) {//从chat还是web来的
            createNewFragment(processResult);
        } else {
            webFrag.loadUrl(processResult);
        }
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

    public void cleanCache() {
        webFrag.cleanCache();
    }


    //设置添加屏幕的背景透明度//不起作用
    public void changeToNightMode() {
        if (isNightMode) {//设置为夜间模式
        } else {//设置为日间模式
        }
    }

    public boolean changeNoPicMode() {
        boolean s = SettingUtil.NoPicMode.changeNoPicMode(getContext());
        //webFrag.setShowPicture(!this.isNoPicMode);
        for (WebTabFragment frag : webFragArray)
            frag.setShowPicture(!s);
        return SettingUtil.NoPicMode.getNoPicModeOnly();
    }

    public boolean changeNightMode() {
        this.isNightMode = !isNightMode;
        changeToNightMode();
        return isNightMode;
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public void refleshFilterData() {
        sf.refleshData();
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
        sf.close();
        su.close();
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
                if (webMenuPopup == null) {
                    webMenuPopup = new WebMenuPopup(getActivity());
                    webMenuPopup.setOnWebMenuBtnClick(this);
                }
                webMenuPopup.showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_group:
                //webGroupArray.add(mainWebView);
                if (webGroupPopup == null) {
                    webGroupPopup = new WebGroupPopup(getActivity(), webFragArray);
                }
                webGroupPopup.showAtBottom(webToolBar);
                break;
            case R.id.btn_web_tool_share:
                if (webSharePopup == null) {
                    webSharePopup = new WebSharePopup(getActivity());
                }
                java.net.URL url = null;
                try {
                    url = new java.net.URL(webFrag.mainWebView.getUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String host = "链接";
                if (url != null)
                    host = url.getHost();// 获取主机名
                webSharePopup.showAtBottom(webToolBar, webFrag.mainWebView.getTitle(), webFrag.mainWebView.getUrl(), host);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {

            case R.id.btn_web_tool_menu:
                goHome();
                xToast.toast(getActivity(), "回到首页");
                //xToast.popupAlert(getActivity(),"回到首页","好的");
                //new AlertPopup(getActivity(), "回到首页","好的").showAtTop();
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

    @Override
    public boolean onWebUrlCanLoad(String url) {
        return sf.filterWebsite(url);
    }

    @Override
    public String showWebUrlFilterKeyword() {
        return sf.getFilterKey();
    }

    @Override
    public void addWebHistory(String title, String url) {
        su.recordHistoryToSqlite(title, url);
    }

    @Override
    public void onScreenshotClick() {
        if (currentItem <= webFragArray.size() - 1) {
            PermissionUtil.Storage(getActivity());
            Bitmap bit = webFragArray.get(currentItem).createSnapshot();
            new WebViewManager(getContext()).saveSnapshot(bit);
            xToast.toast(getContext(), "截图已保存");
        } else {
            currentItem = 0;
        }
    }

    @Override
    public void onAddBookmarkClick() {
        if (currentItem <= webFragArray.size() - 1) {
            xWebView webview = webFragArray.get(currentItem).mainWebView;

            su.recordBookmarkToSqlite(webview.getTitle(), webview.getUrl());
            xToast.toast(getContext(), "书签已添加");
        } else {
            currentItem = 0;
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
