package cn.dustray.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    public  static void Location(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        200);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        201);
            }
        }
    }
}
