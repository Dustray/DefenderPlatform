package cn.dustray.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.dustray.defenderplatform.R;

public class xWebView extends WebView {


    public xWebView(Context context) {
        super(context);
        init();
    }

    public xWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public xWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    public xWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //init();
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
        this.getSettings().setTextZoom(100);
        if (Build.VERSION.SDK_INT >= 19) {
            this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }

    public Bitmap getCapture() {
        this.destroyDrawingCache();
        this.setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createScaledBitmap(this.getDrawingCache(),450,800,true);

        //this.setDrawingCacheEnabled(false);
        return bmp;
    }
}
