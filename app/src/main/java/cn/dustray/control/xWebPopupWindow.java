package cn.dustray.control;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.dustray.defenderplatform.R;
import cn.dustray.utils.AnimUtil;

public class xWebPopupWindow extends PopupWindow {

    private Context context;
    private AnimUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;

    public xWebPopupWindow(Context context) {
        this.context = context;
        animUtil = new AnimUtil();
        initWindow();
    }


    private void initWindow() {
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setElevation(1);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        toggleBright();
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        this.setAnimationStyle(R.style.pop_animation);
    }

    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(0.5f, 1f, 200);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (1.5f - progress);//三目运算，应该挺好懂的。
                backgroundAlpha((Activity) context, bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }
}
