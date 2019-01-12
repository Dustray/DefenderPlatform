package cn.dustray.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import cn.dustray.defenderplatform.R;

public class PreferenceUtil {
    //跨进程使用SharedPreferences的使用，需要使用MODE_MULTI_PROCESS模式。
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void writeSharedPrefs(Context context, String key, String value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void writeSharedPrefs(Context context, String key, int value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void writeSharedPrefs(Context context, String key, boolean value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
