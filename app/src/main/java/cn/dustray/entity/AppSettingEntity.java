package cn.dustray.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.dustray.utils.PreferenceUtil;

public class AppSettingEntity {
    public final static String SCROLL = "pref_scroll_change";
    public final static String FULL_SCREEN = "pref_full_screen";
    public final static String NIGHT_MODE = "pref_night_mode";
    public final static String RATOTE = "pref_ratote";
    private static boolean scrollFlag = true;
    private static boolean fullScreenFlag = false;
    private static boolean nightModeFlag = false;
    private static boolean ratoteFlag = false;
    private static SharedPreferences mSharedPreferences;

    public void refreshAll(Context context) {
        isFullScreenFlag(context);
        isNightModeFlag(context);
        isRatoteFlag(context);
        isScrollFlag(context);
    }

    /**
     * 获取滑动锁定状态(不安全)
     *
     * @return
     */
    public static boolean getScrollFlag() {
        return scrollFlag;
    }

    /**
     * 获取滑动锁定状态
     *
     * @param context
     * @return
     */
    public static boolean isScrollFlag(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return scrollFlag = mSharedPreferences.getBoolean(SCROLL, true);
    }

    /**
     * 设置滑动锁定状态
     *
     * @param context
     * @param _scrollFlag
     */
    public static void setScrollFlag(Context context, boolean _scrollFlag) {
        scrollFlag = _scrollFlag;
        PreferenceUtil.writeSharedPrefs(context, SCROLL, _scrollFlag);
    }


    /**
     * 获取全屏状态(不安全)
     *
     * @return
     */
    public static boolean getFullScreenFlag() {
        return fullScreenFlag;
    }

    /**
     * 获取全屏状态
     *
     * @param context
     * @return
     */
    public static boolean isFullScreenFlag(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return fullScreenFlag = mSharedPreferences.getBoolean(FULL_SCREEN, false);
    }

    /**
     * 设置全屏状态
     *
     * @param context
     * @param _fullScreenFlag
     */
    public static void setFullScreenFlag(Context context, boolean _fullScreenFlag) {
        fullScreenFlag = _fullScreenFlag;
        PreferenceUtil.writeSharedPrefs(context, FULL_SCREEN, _fullScreenFlag);
    }

    /**
     * 获取夜间模式状态
     *
     * @param context
     * @return
     */
    public static boolean isNightModeFlag(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return nightModeFlag = mSharedPreferences.getBoolean(NIGHT_MODE, false);
    }

    /**
     * 设置夜间模式状态
     *
     * @param context
     * @param _nightModeFlag
     */
    public static void setNightModeFlag(Context context, boolean _nightModeFlag) {
        nightModeFlag = _nightModeFlag;
        PreferenceUtil.writeSharedPrefs(context, NIGHT_MODE, _nightModeFlag);
    }

    /**
     * 获取旋转锁定状态
     *
     * @param context
     * @return
     */
    public static boolean isRatoteFlag(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return ratoteFlag = mSharedPreferences.getBoolean(RATOTE, false);
    }

    /**
     * 设置旋转锁定状态
     *
     * @param context
     * @param _ratoteFlag
     */
    public static void setRatoteFlag(Context context, boolean _ratoteFlag) {
        ratoteFlag = _ratoteFlag;
        PreferenceUtil.writeSharedPrefs(context, RATOTE, _ratoteFlag);
    }

    public class WebSetting {
        private boolean noPicModeFlag;

        public boolean isNoPicModeFlag() {
            return noPicModeFlag;
        }

        public void setNoPicModeFlag(boolean noPicModeFlag) {
            this.noPicModeFlag = noPicModeFlag;
        }
    }

    public class ChatSetting {

    }
}
