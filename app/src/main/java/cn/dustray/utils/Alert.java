package cn.dustray.utils;

import android.content.Context;

import cn.dustray.popupwindow.AlertPopup;


public class Alert {
    private static boolean lastClickTime = true;//标志字
    private Alert.OnPopupAlertListener mLisenter;
    Context context;
    private boolean isShowing = false;

    public Alert(Context context) {
        this.context = context;
    }

    public void setOnPopupAlertListener(Alert.OnPopupAlertListener mLisenter) {
        this.mLisenter = mLisenter;
    }

    public boolean popupAlert(String alertContext) {
        return popupAlert(alertContext, null);
    }

    ;

    public boolean popupAlert(String alertContext, String button1Text) {
        return popupAlert(alertContext, button1Text, null);
    }

    ;

    public boolean popupAlert(String alertContext, String button1Text, String button2Text) {
        if (lastClickTime && !isShowing) {//标志字为true可调用Toast
            isShowing = true;
            lastClickTime = false;//标志字置false
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            AlertPopup alert;
            if (button1Text == null) {//no button
                alert = new AlertPopup(context, alertContext);
            } else if (button2Text == null) {//ok
                alert = new AlertPopup(context, alertContext, button1Text);
            } else {//ok cancle
                alert = new AlertPopup(context, alertContext, button1Text, button2Text);
            }
            alert.setOnPopupAlertListener(new AlertPopup.OnPopupAlertListener() {
                @Override
                public void onClickOk() {
                    mLisenter.onClickOk();
                }

                @Override
                public void onClickCancel() {
                    mLisenter.onClickCancel();

                }

                @Override
                public void onDismiss() {
                    isShowing = false;
                }
            });
            alert.showAtTop();
            new Thread() {//使用Thread
                public void run() {
                    try {
                        Thread.sleep(3000);//延迟1000毫秒
                        lastClickTime = true;//标志字置true
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return false;
    }


    public interface OnPopupAlertListener {
        void onClickOk();

        void onClickCancel();
    }
}
