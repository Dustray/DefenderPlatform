package cn.dustray.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.dustray.defenderplatform.R;

public class xWebView extends WebView {

    Context context;

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
        CookieManager.getInstance().setAcceptThirdPartyCookies(this,true);
    }

    public Bitmap getCapture() {
        this.destroyDrawingCache();
        this.setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createScaledBitmap(this.getDrawingCache(), 450, 800, true);

        //this.setDrawingCacheEnabled(false);
        return bmp;
    }

}
