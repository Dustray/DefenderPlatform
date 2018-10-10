package cn.dustray.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.popupwindow.AlertNoBtnPopup;

/**
 * Created by Dustray on 2016/11/27 0027.
 */

public class xToast {
    public static final int ALERT_RESULT_OK = 1;
    public static final int ALERT_RESULT_CANCLE = 2;
    public static final int ALERT_RESULT_DONOTHING = 3;
    private static boolean lastClickTime = true;//标志字
    private static OnPopupAlertListener mLisenter;

    public static void toast(Context context, String msg) {
        if (lastClickTime) {//标志字为true可调用Toast
            lastClickTime = false;//标志字置false
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            new Thread() {//使用Thread
                public void run() {
                    try {
                        Thread.sleep(1000);//延迟1000毫秒
                        lastClickTime = true;//标志字置true
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public static boolean popupAlert(Context context, String alertContext) {
        return popupAlert(context, alertContext, null);
    }

    ;

    public static boolean popupAlert(Context context, String alertContext, String button1Text) {
        return popupAlert(context, alertContext, button1Text, null);
    }

    ;

    public static boolean popupAlert(Context context, String alertContext, String button1Text, String button2Text) {
        if (lastClickTime) {//标志字为true可调用Toast
            mLisenter = (MainActivity) context;
            lastClickTime = false;//标志字置false
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            AlertNoBtnPopup alert;
            if (button1Text == null) {//no button
                alert = new AlertNoBtnPopup(context, alertContext);
            } else if (button2Text == null) {//ok
                alert = new AlertNoBtnPopup(context, alertContext, button1Text);
            } else {//ok cancle
                alert = new AlertNoBtnPopup(context, alertContext, button1Text, button2Text);
            }
            alert.setOnPopupAlertListener(new AlertNoBtnPopup.OnPopupAlertListener() {
                @Override
                public void onClickOk() {
                    mLisenter.onClickOk();
                }

                @Override
                public void onClickCancle() {
                    mLisenter.onClickCancle();

                }
            });
            alert.showAtTop();
            new Thread() {//使用Thread
                public void run() {
                    try {
                        Thread.sleep(1000);//延迟1000毫秒
                        lastClickTime = true;//标志字置true
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return false;
    }

    ;

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public static void exitBy2Click(Activity context) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            xToast.toast(context, "再按一次返回桌面");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
//            context.finish();
//            System.exit(0);
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(backHome);
        }
    }

    public interface OnPopupAlertListener {
        void onClickOk();

        void onClickCancle();
    }
}
