package cn.dustray.browser;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Set;

import cn.dustray.control.xWebView;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.WebTabFragment;
import cn.dustray.utils.FileUtils;
import io.reactivex.Scheduler;

public class WebViewManager {

    Application application;
    Scheduler diskScheduler;

    List<WebTabFragment> list;
    Context context;

    public WebViewManager(Application application, List<WebTabFragment> list,Context context) {
        this.application = application;
        this.list = list;
        this.context=context;
    }

    public void saveToFile() {
        Bundle outState = new Bundle(ClassLoader.getSystemClassLoader());

        for (int i = 0; i < list.size(); i++) {
            WebTabFragment tabFragment = list.get(i);

            Bundle state = new Bundle(ClassLoader.getSystemClassLoader());
            xWebView webView = tabFragment.mainWebView;
            if (webView != null) {
                webView.saveState(state);
                Log.i("def", "putin:--WEBVIEW_" + i+";    size:"+state.toString());
                outState.putBundle("WEBVIEW_" + i, state);
            }
        }
        FileUtils.writeBundleToStorage(application, outState, "SAVED_TABS.parcel")
                .subscribe();
    }


    public List<WebTabFragment> readSavedStateFromDisk() {
        Bundle bundle = FileUtils.readBundleFromStorage(application, "SAVED_TABS.parcel");
        if (bundle != null) {
            Set<String> keySet = bundle.keySet();  //获取所有的Key,
            int i = 0;
            for (String key : keySet) {  //bundle.get(key);来获取对应的value
                if (key.startsWith("WEBVIEW_")) {
                    Bundle state = bundle.getBundle("WEBVIEW_" + i);
                    Log.i("def", "putout:-------->WEBVIEW_" + i+";    size:"+state.toString());
                    WebTabFragment webFrag = WebTabFragment.newInstance();
                     webFrag.addXWebView(new xWebView(context.getApplicationContext()));
                    // webFragment.addXWebView(web);
                    //webFrag.mainWebView.restoreState(state);//.onSaveInstanceState(state);
                    webFrag.webState=state;
                    list.add(webFrag);
                    i++;
                }
            }
        }
        return list;
    }
}
