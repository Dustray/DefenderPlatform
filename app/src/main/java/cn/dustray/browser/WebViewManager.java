package cn.dustray.browser;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.dustray.control.xWebView;
import cn.dustray.utils.FileUtils;
import cn.dustray.utils.xToast;
import io.reactivex.Scheduler;

import static android.support.constraint.Constraints.TAG;
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
    public WebViewManager(Context context) {
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
                Log.i("def", "putin:--WEBVIEW_" + i + ";    " + state.toString() + "(size" + state.size() + ")");
                outState.putBundle("WEBVIEW_" + i, state);
                tabFragment.generateSnapshot();
                saveBitmap(tabFragment.getSnapshot(), i);
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
                    Log.i("def", "putout:-------->WEBVIEW_" + i + ";    " + state.toString() + "(size" + state.size() + ")");
                    WebTabFragment webFrag = WebTabFragment.newInstance();
                    xWebView web = new xWebView(context.getApplicationContext());
                    web.setId(generateViewId());
                    webFrag.setWebView(web);
                    webFrag.setSnapshotBmp(openBitmapFromFile(i));
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

    public void saveBitmap(Bitmap bitmap, int index) {
        // 首先保存图片
        File appDir = new File(application.getFilesDir(), "web_capture_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "browser_tab_capture_" + index + ".jpg";
        File file = new File(appDir, fileName);
        Log.i(TAG, "write_filepath" + file.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to write capture to storage" + e.toString(), e);
        }

    }

    public void saveSnapshot(Bitmap bitmap) {
        String path = "snapshot";
        String fileName = new Date().getTime()+".jpg";
        saveBitmapToDisk(bitmap, path, fileName);
    }

    public void saveBitmapToDisk(Bitmap bitmap, String path, String fileName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "DefenderPlatform/"+path );
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        //og.i(TAG, "write_filepath" + file.getPath());
       // xToast.toast(context,file.getAbsolutePath()+"bitmap:"+bitmap.getHeight());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to write capture to storage" + e.toString(), e);
        }

    }

    public Bitmap openBitmapFromFile(int index) {
        File inputFile = new File(application.getFilesDir(), "web_capture_image");
        Log.i(TAG, "filepath" + application.getFilesDir());
        FileInputStream inputStream = null;
        try {
            //noinspection IOResourceOpenedButNotSafelyClosed
            String fileName = "browser_tab_capture_" + index + ".jpg";
            File file = new File(inputFile, fileName);
            inputStream = new FileInputStream(file);


            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(450, 800, Bitmap.Config.RGB_565);
            }
            inputStream.close();
            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 450, 800, true);
            return bmp;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Unable to read capture from storage" + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Unable to read capture from storage" + e.toString(), e);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            // inputFile.delete();
        }
        return null;
    }
}
