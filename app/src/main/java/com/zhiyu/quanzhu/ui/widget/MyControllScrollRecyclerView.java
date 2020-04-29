package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyControllScrollRecyclerView extends RecyclerView {


    public MyControllScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    private boolean canScroll = true;

    public void controllScroll(boolean isCanScroll) {
        this.canScroll = isCanScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.dispatchTouchEvent(ev);
        } else {
            return canScroll;
        }
    }

}
