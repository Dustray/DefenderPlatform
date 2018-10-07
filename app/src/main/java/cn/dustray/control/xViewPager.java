package cn.dustray.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class xViewPager extends ViewPager {
    private boolean lockRollFlag = true;

    public xViewPager(@NonNull Context context) {
        super(context);
    }

    public xViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 切换是否能滑动换页
     */
    public boolean changeScrollFlag() {
        if(lockRollFlag){
            lockRollFlag=false;
        }else{
            lockRollFlag=true;
        }


        return lockRollFlag;
    }
    public boolean getScrollFlag() {

        return lockRollFlag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return lockRollFlag && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return lockRollFlag && super.onTouchEvent(ev);

    }

}
