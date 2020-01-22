package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollHorizontallyViewPager extends ViewPager {
    public NoScrollHorizontallyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollHorizontallyViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //不拦截,否则子孩子都无法收到事件,一般这个自定义的时候都不作处理
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
