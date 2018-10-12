package cn.dustray.popupwindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.defenderplatform.R;
import cn.dustray.utils.PixelConvert;

public class TextMenuPopup extends PopupWindow implements View.OnClickListener {

    private Context context;
    private Button btnTextCopy, btnTextShare;
    private String text;

    public TextMenuPopup(Context context, String text) {
        this.context = context;
        this.text = text;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_text_menu, null);

        setContentView(view);
        initWindow();
        initButton();
    }

    private void initButton() {
        btnTextCopy = getContentView().findViewById(R.id.text_menu_copy);
        btnTextCopy.setOnClickListener(this);
        btnTextShare = getContentView().findViewById(R.id.text_menu_share);
        btnTextShare.setOnClickListener(this);
    }


    private void initWindow() {
        this.setWidth(PixelConvert.dip2px(context, 140));
        this.setHeight(PixelConvert.dip2px(context, 35));

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setElevation(1);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha((Activity) context, 1f);
//            }
//        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {        //弹窗位置设置
        //setAnimationStyle(R.style.pop_animation);
        // showAtLocation(view, Gravity.BOTTOM, 0, PixelConvert.dip2px(context, 45));
        showAsDropDown(view, view.getWidth() / 2 - getWidth() / 2, -view.getHeight()-getHeight()-5);
     }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_menu_copy:  // 将文本内容放到系统剪贴板里。
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("DefenderPlatform", text);
                cm.setPrimaryClip(clipData);
                break;
            case R.id.text_menu_share:
                ((MainActivity) context).chatFragment.share("转发",text);
                break;
        }
        dismiss();
    }
}
