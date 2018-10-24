package cn.dustray.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.view.View;
import android.widget.TextView;

import cn.dustray.control.xWebPopupWindow;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.defenderplatform.WebFragment;

public class WebMenuPopup extends xWebPopupWindow implements View.OnClickListener {

    private Context context;
    private ImageButton btnFullScreen, btnNoPicture, btnRotate, btnScreenshot, btnNightMode;
    private ImageButton btnHome, btnRefresh, btnRollLock, btnExit, btnClean;
    private TextView textFullScreen, textNoPicure, textRotate, textNightMode;
    private WebFragment webFragment;

    public WebMenuPopup(Context context) {
        super(context);
        this.context = context;
        webFragment = ((MainActivity) context).webFragment;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_web_menu, null);
        setContentView(view);
        initView();
        changeButtonSettingState();
    }

    private void initView() {
        //第一组
        btnFullScreen = getContentView().findViewById(R.id.btn_web_menu_fullscreen);
        btnNoPicture = getContentView().findViewById(R.id.btn_web_menu_nopic);
        btnRotate = getContentView().findViewById(R.id.btn_web_menu_rotate);
        btnScreenshot = getContentView().findViewById(R.id.btn_web_menu_capture);
        btnNightMode = getContentView().findViewById(R.id.btn_web_menu_night);

        btnFullScreen.setOnClickListener(this);
        btnNoPicture.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnScreenshot.setOnClickListener(this);
        btnNightMode.setOnClickListener(this);

        textFullScreen = getContentView().findViewById(R.id.text_web_menu_fullscreen);
        textNoPicure = getContentView().findViewById(R.id.text_web_menu_nopic);
        textRotate = getContentView().findViewById(R.id.text_web_menu_rotate);
        textNightMode = getContentView().findViewById(R.id.text_web_menu_night);
        //第二组
        btnHome = getContentView().findViewById(R.id.btn_web_menu_home);
        btnHome.setOnClickListener(this);
        btnRefresh = getContentView().findViewById(R.id.btn_web_menu_refresh);
        btnRefresh.setOnClickListener(this);
        btnRollLock = getContentView().findViewById(R.id.btn_web_menu_rolllock);
        btnRollLock.setOnClickListener(this);

        btnExit = getContentView().findViewById(R.id.btn_web_menu_down);
        btnExit.setOnClickListener(this);
        btnClean = getContentView().findViewById(R.id.btn_web_menu_clean);
        btnClean.setOnClickListener(this);


    }

    public void showAtBottom(View view) {        //弹窗位置设置
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void changeButtonSettingState() {
        //ViewPager可滑动
        if (((MainActivity) context).getPageScrollState()) {
            btnRollLock.setImageResource(R.drawable.ic_btn_roll_unlock_black);
        } else {
            btnRollLock.setImageResource(R.drawable.ic_btn_roll_lock_black);
        }
        //全屏模式
        if (webFragment.isFullScreen()) {//已打开
            btnFullScreen.setImageResource(R.drawable.ic_btn_fullscreenoff_gray);
            textFullScreen.setText("全屏/开");
        } else {//已关闭
            btnFullScreen.setImageResource(R.drawable.ic_btn_fullscreenon_black);
            textFullScreen.setText("全屏/关");
        }

        //无图模式
        if (webFragment.isNoPicMode()) {//已打开
            btnNoPicture.setImageResource(R.drawable.ic_btn_picturemodeoff_gray);
            textNoPicure.setText("无图/开");
        } else {//已关闭
            btnNoPicture.setImageResource(R.drawable.ic_btn_picturemodeon_black);
            textNoPicure.setText("无图/关");
        }

        //旋转模式
        if (webFragment.isRatote()) {//已打开
            btnRotate.setImageResource(R.drawable.ic_btn_rotateoff_gray);
            textRotate.setText("旋转/开");
        } else {//已关闭
            btnRotate.setImageResource(R.drawable.ic_btn_rotateon_black);
            textRotate.setText("旋转/关");
        }

        //夜间模式
        if (webFragment.isNightMode()) {//已打开
            btnNightMode.setImageResource(R.drawable.ic_btn_nightmodeoff_gray);
            textNightMode.setText("日间模式");
        } else {//已关闭
            btnNightMode.setImageResource(R.drawable.ic_btn_nightmodeon_black);
            textNightMode.setText("夜间模式");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //第一组
            case R.id.btn_web_menu_fullscreen:
                webFragment.changeFullScreen();
                break;
            case R.id.btn_web_menu_nopic:
                webFragment.changeNoPicMode();
                break;
            case R.id.btn_web_menu_rotate:
                webFragment.changeRatote();
                break;
            case R.id.btn_web_menu_capture:
                break;
            case R.id.btn_web_menu_night:
                webFragment.changeNightMode();
                break;
            //第二组
            case R.id.btn_web_menu_home:
                webFragment.goHome();
                break;
            case R.id.btn_web_menu_refresh:
                webFragment.refresh();
                break;
            case R.id.btn_web_menu_rolllock:
                ((MainActivity) context).changePageScroll();
                break;
            case R.id.btn_web_menu_down:
                break;
            case R.id.btn_web_menu_clean:
                ((MainActivity) context).webFragment.cleanCache();
                break;
        }
        dismiss();
    }

}
