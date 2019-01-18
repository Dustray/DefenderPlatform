package cn.dustray.utils;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.dustray.webfilter.KeywordEntity;

public class BmobUtil {
    public final static String APPLICATION_ID = "1a67de83b1b060162bbad1b79bf1bd37";
    public Context context;

    public BmobUtil(Context context) {
        this.context = context;
    }

    public static void initialize(Activity activity) {
        Bmob.initialize(activity, APPLICATION_ID);
        //自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

}
