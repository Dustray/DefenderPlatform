package cn.dustray.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.view.View;

import cn.dustray.defenderplatform.R;
import cn.dustray.tool.PixelConvert;

public class WebMenuPopup extends PopupWindow implements View.OnClickListener {

    private Context context;

    public WebMenuPopup(Context context) {
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_web_menu, null);
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(PixelConvert.dip2px(context,100));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setElevation(1);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
       ColorDrawable dw = new ColorDrawable(Color.WHITE);        //设置SelectPicPopupWindow弹出窗体的背景
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
        showAsDropDown(view, 0, 0);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差


    }

    @Override
    public void onClick(View view) {

    }
}
