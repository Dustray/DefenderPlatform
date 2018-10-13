package cn.dustray.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.utils.DataCacheUtil;
import cn.dustray.utils.xToast;

public class xWebView extends WebView {

    Context context;
    private HistoryStack historyStack = new HistoryStack();
    private boolean isFromDatabase = false;

    public xWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public xWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public xWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public xWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        //支持javascript
        this.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        this.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        this.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        this.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setLoadsImagesAutomatically(true); // 加载图片
        this.getSettings().setAllowFileAccess(true);
        this.getSettings().setAppCacheEnabled(true);
        //启用地理定位
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setGeolocationEnabled(true);//启用地理定位
        this.getSettings().setDatabaseEnabled(true);//启用数据库
        String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        this.getSettings().setGeolocationDatabasePath(dir);//设置定位的数据库路径

        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用网络缓存，开启的话容易导致app膨胀导致卡顿
        this.getSettings().setTextZoom(100);
        //  this.getSettings().setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 19) {
            this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反

                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);//获取滚动位置
                //webview.getScrollY()/newScale*oldScale;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();//接受证书
            }
        });
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
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

    public void cleanCache() {
        clearCache(true);
        String dataSize = "0KB";
        try {
            dataSize = DataCacheUtil.getTotalCacheSize(context.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String size = dataSize;
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean aBoolean) {
                if (aBoolean) {
                    xToast.toast(context, "浏览器缓存已清除:" + size );
                } else {
                    xToast.toast(context, "当前无可清理缓存" + size);
                }
            }
        });

    }

    class XWebViewHistoryEntity {
        private String Url;
        private int position;// webview.getScrollY()/newScale*oldScale

        public XWebViewHistoryEntity(String url) {
            Url = url;
            this.position = 0;
        }

        public XWebViewHistoryEntity(String url, int position) {
            Url = url;
            this.position = position;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }


    }

    class HistoryStack {
        private List<XWebViewHistoryEntity> list = new ArrayList<>();
        private int StackPosition = 0;//从0开始为第一页
        private Bitmap capture;
        private static final int BACK_PAGE = -1;
        private static final int NOW_PAGE = 0;
        private static final int FOREWARD_PAGE = 1;

        public boolean isStackBottom() {
            if (StackPosition == 0) return true;
            else return false;
        }

        public boolean isStackTop() {
            if (StackPosition == size() - 1) return true;
            else return false;
        }

        /**
         * 获取历史栈长度（页面数）
         *
         * @return
         */
        public int size() {
            return list.size();
        }

        /**
         * 入栈
         *
         * @param url
         */
        public void push(String url) {
            clearForward();
            XWebViewHistoryEntity entity = new XWebViewHistoryEntity(url);
            list.add(entity);
            StackPosition = size() - 1;
        }

        /**
         * 获取上一页Url
         *
         * @return
         * @throws NullPointerException
         */
        public String getBackUrl() throws NullPointerException {
            if (StackPosition == 0) throw new NullPointerException("栈已到达底部");
            StackPosition--;
            return list.get(StackPosition).getUrl();
        }

        /**
         * 获取下一页Url
         *
         * @return
         * @throws NullPointerException
         */
        public String getForwardUrl() throws NullPointerException {
            if (StackPosition == size() - 1) throw new NullPointerException("栈已到达顶部");
            StackPosition++;
            return list.get(StackPosition).getUrl();
        }

        /**
         * 清除当前页面以后的页面
         */
        private void clearForward() {
            if (StackPosition == size() - 1) return;//栈顶
            for (int i = StackPosition + 1; i < size(); i++) {
                list.remove(i);
            }
        }

        /**
         * 更新当前页面滚动位置
         *
         * @param PagePosition
         */
        public void updatePagePosition(int PagePosition) {
            list.get(StackPosition).position = PagePosition;
        }

        /**
         * 获取当前页面滚动位置
         */
        public int getPagePosition() {
            return list.get(StackPosition).position;
        }

        /**
         * 获取当前页面在栈中的位置
         *
         * @return
         */
        public int getStackPosition() {
            return StackPosition;
        }

        /**
         * 设置当前页面在栈中的位置
         *
         * @param stackPosition
         */
        public void setStackPosition(int stackPosition) {
            StackPosition = stackPosition;
        }

        /**
         * 判断网址是否相同
         *
         * @param nowUrl
         * @param whichPage
         * @return
         */
        public boolean isUrlSameAsHistory(String nowUrl, int whichPage) {

            if (list.get(StackPosition + whichPage).getUrl().equals(nowUrl)) return true;
            else return false;
        }

        /**
         * 获取快照
         *
         * @return
         */
        public Bitmap getCapture() {
            return capture;
        }

        /**
         * 升级快照
         *
         * @param capture
         */
        public void updateCapture(Bitmap capture) {
            this.capture = capture;
        }

        /**
         * 销毁栈
         */
        public void destroy() {
            list.clear();
            list = null;
        }
    }
}
