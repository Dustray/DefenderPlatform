package cn.dustray.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dustray.control.xButtonLayout;
import cn.dustray.control.xWebPopupWindow;
import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.browser.BrowserFragment;
import cn.dustray.utils.FilterPreferenceHelper;
import cn.dustray.utils.SettingUtil;

public class WebMenuPopup extends xWebPopupWindow implements View.OnClickListener {

    private Context context;
    private xButtonLayout btnFullScreen, btnNoPicture, btnRotate, btnScreenshot, btnNoFilterMode;
    private xButtonLayout btnBookmark, btnHome, btnRefresh, btnRollLock, btnClean;
    private ImageButton btnExit;
    //private TextView textFullScreen, textNoPicure, textRotate, textNightMode;
    private BrowserFragment browserFragment;
    private OnPopupInteractionListener mListener;
    private OnWebMenuBtnClick btnClickListener;
    private FilterPreferenceHelper spHelper;

    public WebMenuPopup(Context context) {
        super(context);
        this.context = context;
        mListener = (OnPopupInteractionListener) context;

        browserFragment = ((MainActivity) context).browserFragment;
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
        btnNoFilterMode = getContentView().findViewById(R.id.btn_web_menu_nofilter);

        btnFullScreen.setOnClickListener(this);
        btnNoPicture.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnScreenshot.setOnClickListener(this);
        btnNoFilterMode.setOnClickListener(this);

        //textFullScreen = getContentView().findViewById(R.id.btn_web_menu_fullscreen);
        //  textNoPicure = getContentView().findViewById(R.id.text_web_menu_nopic);
        // textRotate = getContentView().findViewById(R.id.text_web_menu_rotate);
        // textNightMode = getContentView().findViewById(R.id.text_web_menu_night);
        //第二组
        btnBookmark = getContentView().findViewById(R.id.btn_web_menu_bookmark);
        btnBookmark.setOnClickListener(this);
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

        if (spHelper == null)
            spHelper = new FilterPreferenceHelper(((MainActivity) context));


    }

    public void setOnWebMenuBtnClick(OnWebMenuBtnClick btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    @Override
    public void showAtBottom(View view) {        //弹窗位置设置
        super.showAtBottom(view);
        changeButtonSettingState();
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void changeButtonSettingState() {
        //ViewPager可滑动
        if (((MainActivity) context).getPageScrollState()) {
//            btnRollLock.setImageResource(R.drawable.ic_btn_roll_unlock_black);
            btnRollLock.close();
        } else {
//            btnRollLock.setImageResource(R.drawable.ic_btn_roll_lock_black);
            btnRollLock.open();
        }
        //全屏模式
        if (SettingUtil.FullScreen.isFullScreen(context)) {//已打开
            //btnFullScreen.setImageResource(R.drawable.ic_btn_fullscreenoff_gray);
            btnFullScreen.close();
            //textFullScreen.setText("全屏/开");
        } else {//已关闭
            //btnFullScreen.setImageResource(R.drawable.ic_btn_fullscreenon_black);
            btnFullScreen.open();
            //textFullScreen.setText("全屏/关");
        }

        //无图模式
        if (SettingUtil.NoPicMode.getNoPicMode(context)) {//已打开
//            btnNoPicture.setImageResource(R.drawable.ic_btn_picturemodeoff_gray);
//            textNoPicure.setText("无图/开");
            btnNoPicture.close();
        } else {//已关闭
//            btnNoPicture.setImageResource(R.drawable.ic_btn_picturemodeon_black);
//            textNoPicure.setText("无图/关");
            btnNoPicture.open();
        }

        //旋转模式
        if (SettingUtil.Ratote.getRatoteFlag(context)) {//已打开
//            btnRotate.setImageResource(R.drawable.ic_btn_rotateoff_gray);
//            textRotate.setText("旋转/开");
            btnRotate.close();
        } else {//已关闭
//            btnRotate.setImageResource(R.drawable.ic_btn_rotateon_black);
//            textRotate.setText("旋转/关");
            btnRotate.open();
        }

        //屏蔽模式
        if (spHelper.getIsNoFilter()) {//已打开
//            btnNoFilterMode.setImageResource(R.drawable.ic_btn_nightmodeoff_gray);
//            textNightMode.setText("日间模式");
            btnNoFilterMode.close();
        } else {//已关闭
//            btnNoFilterMode.setImageResource(R.drawable.ic_btn_nightmodeon_black);
//            textNightMode.setText("夜间模式");
            btnNoFilterMode.open();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //第一组
            case R.id.btn_web_menu_fullscreen:
                //    browserFragment.changeFullScreen();
                SettingUtil.FullScreen.changeFullScreen((MainActivity) context);
                break;
            case R.id.btn_web_menu_nopic:
                browserFragment.changeNoPicMode();
                break;
            case R.id.btn_web_menu_rotate:
                //    browserFragment.changeRatote();
                SettingUtil.Ratote.changeRatoteFlag((MainActivity) context);
                break;
            case R.id.btn_web_menu_capture:
                btnClickListener.onScreenshotClick();
                break;
            case R.id.btn_web_menu_night:
                browserFragment.changeNightMode();
                break;
            case R.id.btn_web_menu_nofilter:
                mListener.onChangeNoFilter();
                break;
            //第二组
            case R.id.btn_web_menu_home:
                browserFragment.goHome();
                break;
            case R.id.btn_web_menu_refresh:
                browserFragment.refresh();
                break;
            case R.id.btn_web_menu_rolllock:
                //((MainActivity) context).changePageScroll();
                mListener.onChangePageScroll();
                break;
            case R.id.btn_web_menu_down:
                break;
            case R.id.btn_web_menu_clean:
                //((MainActivity) context).browserFragment.cleanCache();
                mListener.onCleanCache();
                break;
            case R.id.btn_web_menu_bookmark:
                btnClickListener.onAddBookmarkClick();
                break;
        }
        dismiss();
    }

    public interface OnPopupInteractionListener {
        void onCleanCache();

        void onChangePageScroll();

        void onChangeNoFilter();
    }

    public interface OnWebMenuBtnClick {
        void onScreenshotClick();

        void onAddBookmarkClick();
    }
}
