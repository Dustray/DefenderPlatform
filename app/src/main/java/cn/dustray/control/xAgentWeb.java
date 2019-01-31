package cn.dustray.control;

import android.support.v4.app.Fragment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebView;

public class xAgentWeb {
    private AgentWeb agentWeb;
    private String homeUrl = "";

    /**
     * 初始化
     *
     * @param fragment         父容器
     * @param frameLayout      父容器
     * @param mWebViewClient   客户端
     * @param mWebChromeClient 客户端
     * @param homeUrl          默认链接
     */
    public xAgentWeb(Fragment fragment, FrameLayout frameLayout, WebViewClient mWebViewClient, WebChromeClient mWebChromeClient, String homeUrl) {
        this.homeUrl = homeUrl;
        agentWeb = AgentWeb.with(fragment)
                .setAgentWebParent(frameLayout, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(homeUrl);
    }

    /**
     * 获取WebView
     *
     * @return
     */
    private WebView getWebView() {
        return agentWeb.getWebCreator().getWebView();

    }

    /**
     * 获取AgentWeb
     * @return
     */
    public AgentWeb getAgentWeb() {
        return agentWeb;
    }

    /**
     * 回首页
     */
    public void goHome() {
        agentWeb.getUrlLoader().loadUrl(homeUrl);
    }

    /**
     * 刷新
     */
    public void refresh() {
        agentWeb.getUrlLoader().reload();
    }

    /**
     * 判断是否可后退
     *
     * @return
     */
    public boolean canGoBack() {
        return getWebView().canGoBack();
    }

    /**
     * 后退
     *
     * @return
     */
    public boolean goBack() {
        return agentWeb.back();
    }

    /**
     * 判断是否可前进
     *
     * @return
     */
    public boolean canGoForward() {
        return getWebView().canGoForward();
    }

    /**
     * 前进
     *
     * @return
     */
    public boolean goForward() {
        if (getWebView().canGoForward()) {
            getWebView().goForward();
            return true;
        }
        return false;
    }

    /**
     * 获取title
     *
     * @return
     */
    public String getTitle() {
        return getWebView().getTitle();
    }

    /**
     * 获取Url
     *
     * @return
     */
    public String getUrl() {
        return getWebView().getUrl();
    }


}
