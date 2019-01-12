package cn.dustray.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

import cn.dustray.entity.AppSettingEntity;

public class SettingUtil {
    //todo 检测并刷新加载
    public static void detectAndRefresh(Activity activity) {
        FullScreen.changeToFullScreen(activity);//刷新全屏设置
    }

    public static void refreshFullScreen(Activity activity) {
        FullScreen.changeToFullScreen(activity);//刷新全屏设置
    }

    public static class FullScreen {
        /**
         * 判断是否全屏
         *
         * @param context
         * @return
         */
        public static boolean isFullScreen(Context context) {
            return AppSettingEntity.isFullScreenFlag(context);
        }

        /**
         * 改变全屏状态
         *
         * @param activity
         * @return
         */
        public static boolean changeFullScreen(Activity activity) {
            boolean isFullScreen = !isFullScreen(activity);
            AppSettingEntity.setFullScreenFlag(activity, isFullScreen);//修改设置
            changeToFullScreen(activity);
            return AppSettingEntity.getFullScreenFlag();
        }

        private static void changeToFullScreen(Activity activity) {
            if (!isFullScreen(activity)) {//设置为非全屏
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(lp);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {//设置为全屏
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                activity.getWindow().setAttributes(lp);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }

    }

    public static class Scroll {
        /**
         * 切换是否能滑动换页
         */
        public static boolean changeScrollFlag(Context context) {
            if (getScrollFlag(context)) {
                setScrollFlag(context, false);
                xToast.toast(context, "滑动切换已关闭");
            } else {
                setScrollFlag(context, true);
                xToast.toast(context, "滑动切换已开启");
            }
            return AppSettingEntity.getScrollFlag();
        }

        public static boolean getScrollFlag(Context context) {
            return AppSettingEntity.isScrollFlag(context);
        }

        public static void setScrollFlag(Context context, boolean s) {
            AppSettingEntity.setScrollFlag(context, s);
        }
    }
}
