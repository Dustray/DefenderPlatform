package cn.dustray.control;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.utils.Alert;
import cn.dustray.utils.DataCacheUtil;
import cn.dustray.utils.xToast;

public class xWebView extends WebView {

    static Context context;
    private boolean isFromDatabase = false;
    private  static xWebView webView ;

    public xWebView(Context context) {
        super(context);
        this.context = context;
        init();
        webView=this;
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
        this.setBackgroundColor(Color.rgb(250,250,250)); // 设置背景色
        //this.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
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
                    xToast.toast(context, "浏览器缓存已清除:" + size);
                } else {
                    xToast.toast(context, "当前无可清理缓存");
                }
            }
        });
    }

    //判断app是否安装
    private static boolean isInstall(Intent intent) {
        //Log.i("browser", "非https--->");
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //打开app
    public static boolean openApp(String url, final Context activityContext) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {

                // Log.i("browser", "非https--->"+url);
                Uri uri = Uri.parse(url);
                String host = uri.getHost();
                String scheme = uri.getScheme();
                //host 和 scheme 都不能为null
                if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(scheme)) {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (isInstall(intent)) {
                        //Log.i("browser", "已安装");
                        Alert alert = new Alert(activityContext);
                        alert.setOnPopupAlertListener(new Alert.OnPopupAlertListener() {
                            @Override
                            public void onClickOk() {
                                context.startActivity(intent);
                                xToast.toast(context, "正在跳转，请稍后");
                            }

                            @Override
                            public void onClickCancle() {
                            }
                        });
                        String appName = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).get(0).loadLabel(context.getPackageManager()).toString();
                        alert.popupAlert("网站请求打开“" + appName + "”，是否同意？", "同意");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static class xWebViewCilent extends WebViewClient {

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);//获取滚动位置
            //webview.getScrollY()/newScale*oldScale;
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webView.loadUrl("file:///android_asset/html/HomePage.html");
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();//接受证书
        }
    }

}
