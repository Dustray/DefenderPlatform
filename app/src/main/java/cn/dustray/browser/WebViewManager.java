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

import static android.view.View.generateViewId;

public class WebViewManager {

    Application application;
    Scheduler diskScheduler;

    List<WebTabFragment> list;
    Context context;

    public WebViewManager(Application application, List<WebTabFragment> list, Context context) {
        this.application = application;
        this.list = list;
        this.context = context;
    }

    public void saveToFile() {
        Bundle outState = new Bundle(ClassLoader.getSystemClassLoader());
        Log.i("def", "saveToFile:------------------IN-------------------------------WEBVIEW");
        for (int i = 0; i < list.size(); i++) {
            WebTabFragment tabFragment = list.get(i);

            Bundle state = new Bundle(ClassLoader.getSystemClassLoader());
            xWebView webView = tabFragment.mainWebView;
            if (webView != null) {
                if (tabFragment.getWebState() != null) {
                    state = tabFragment.getWebState();
                } else
                    webView.saveState(state);
                Log.i("def", "putin:--WEBVIEW_" + i + ";    " + state.toString()+"(size"+state.size()+")");
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
            Log.i("def", "readSavedStateFromDisk:------------------------OUT------------WEBVIEW");
            for (String key : keySet) {  //bundle.get(key);来获取对应的value
                if (key.startsWith("WEBVIEW_")) {
                    Bundle state = bundle.getBundle("WEBVIEW_" + i);
                    Log.i("def", "putout:-------->WEBVIEW_" + i + ";    " + state.toString()+"(size"+state.size()+")");
                    WebTabFragment webFrag = WebTabFragment.newInstance();
                    xWebView web=new xWebView(context.getApplicationContext());
                    web.setId(generateViewId());
                    webFrag.setWebView(web);
                    // webFragment.addXWebView(web);
                    //webFrag.mainWebView.restoreState(state);//.onSaveInstanceState(state);
                    if (state.size() != 0) {
                        webFrag.setWebState(state);
                    }
                    list.add(webFrag);
                    i++;
                }
            }
        }
        return list;
    }
}
