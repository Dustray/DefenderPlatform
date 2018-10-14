package cn.dustray.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import cn.dustray.control.xViewPager;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;

public class AlertPopup extends PopupWindow implements View.OnClickListener {

    private Context context;
    private Button btnOk, btnCancle;
    private ImageButton btnExit;
    private String alertContext, buttonText_1, buttonText_2;
    private TextView textalertContext;
    private LinearLayout alertFrame;
    private OnPopupAlertListener mListener;
    private xViewPager parentView;
    private boolean isTouching = false;
    private static int ALERT_TYPE = 0;

    public AlertPopup(Context context, String alertContext) {
        this.context = context;
        this.alertContext = alertContext;
        ALERT_TYPE = 1;
        // mListener = (OnPopupAlertListener)context;
        init();
    }

    public AlertPopup(Context context, String alertContext, String buttonText) {
        this.context = context;
        this.alertContext = alertContext;
        this.buttonText_1 = buttonText;
        ALERT_TYPE = 2;
        // mListener = (OnPopupAlertListener)context;
        init();
    }

    public AlertPopup(Context context, String alertContext, String buttonText_1, String buttonText_2) {
        this.context = context;
        this.alertContext = alertContext;
        this.buttonText_1 = buttonText_1;
        this.buttonText_2 = buttonText_2;
        ALERT_TYPE = 3;
        // mListener = (OnPopupAlertListener)context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_alert_non_btn, null);
        switch (ALERT_TYPE) {
            case 2:
                view = inflater.inflate(R.layout.popup_alert_ok_btn, null);
                break;
            case 3:
                view = inflater.inflate(R.layout.popup_alert_okcancle_btn, null);
                break;
        }
        setContentView(view);
        alertFrame = view.findViewById(R.id.alert_popup_frame);
        initButton();
        initWindow();
    }

    private void initButton() {
        textalertContext = getContentView().findViewById(R.id.alert_popup_title);
        textalertContext.setText(alertContext);

        switch (ALERT_TYPE) {
            case 3:
                btnCancle = getContentView().findViewById(R.id.btn_alert_cancle);
                btnCancle.setOnClickListener(this);
                btnCancle.setText(buttonText_2);
            case 2:
                btnOk = getContentView().findViewById(R.id.btn_alert_ok);
                btnOk.setOnClickListener(this);
                btnOk.setText(buttonText_1);
        }
        btnExit = getContentView().findViewById(R.id.btn_alert_popup_up);
        btnExit.setOnClickListener(this);

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            float downY;
            boolean canTouch = true;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (canTouch) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        downY = motionEvent.getY();
                        isTouching = true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        if (downY - motionEvent.getY() > 100) {//消失
                            btnExit.setImageResource(R.drawable.ic_btn_menu_up_white);
                            dismiss();
                            canTouch = false;
                        } else if (downY < motionEvent.getY()) {//锁定
                            btnExit.setImageResource(R.drawable.ic_btn_lock_white);
                        }
                    }
                }
                return false;
            }
        });
    }


    private void initWindow() {
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.setTouchable(true);
        this.setElevation(15);
        this.update();
        ColorDrawable dw = new ColorDrawable(Color.WHITE);        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);


        parentView = ((MainActivity) context).findViewById(R.id.main_page);
        new Thread() {//使用Thread
            public void run() {
                try {
                    Thread.sleep(3000);//延迟1000毫秒
                    if (!isTouching) {
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public void setOnPopupAlertListener(OnPopupAlertListener mListener) {
        this.mListener = mListener;
    }

    public void showAtTop() {        //弹窗位置设置
        setAnimationStyle(R.style.pop_animation_2);
        showAtLocation(parentView, Gravity.TOP, 0, 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.onDismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alert_ok:
                mListener.onClickOk();
                break;
            case R.id.btn_alert_cancle:
                mListener.onClickCancle();
                break;
            case R.id.btn_alert_popup_up:
                break;
        }
        btnExit.setImageResource(R.drawable.ic_btn_menu_up_white);
        dismiss();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //btnRegisterGoStep.setText("确定");
            if (msg.what == 0) {
                dismiss();
            }
        }
    };

    public interface OnPopupAlertListener {
        void onClickOk();

        void onClickCancle();

        void onDismiss();
    }
}
