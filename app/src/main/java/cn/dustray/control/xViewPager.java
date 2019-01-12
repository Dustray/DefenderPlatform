package cn.dustray.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;

import cn.dustray.defenderplatform.MainActivity;
import cn.dustray.utils.SettingUtil;
import cn.dustray.utils.xToast;

public class xViewPager extends ViewPager {
    int height=0;
    private Context context;
    public xViewPager(@NonNull Context context) {
        super(context);
        this.context=context;
        WindowManager manager = ((MainActivity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        //int width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
    }

    public xViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        WindowManager manager = ((MainActivity)context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        //int width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(getCurrentItem()==1&&height-ev.getY()<450){
            //xToast.toast(context,"ss"+height+"d"+ev.getY());
            return false;
        }
        return SettingUtil.Scroll.getScrollFlagOnly() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return SettingUtil.Scroll.getScrollFlagOnly() && super.onTouchEvent(ev);

    }

}
