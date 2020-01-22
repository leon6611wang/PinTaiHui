package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyLocationRecyclerView extends RecyclerView {
    public MyLocationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private int downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                System.out.println("手指坐标 " + downX + " , " + downY);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }

    public int[] getLocation() {
        int[] location = new int[2];
        location[0] = downX;
        location[1] = downY;
        return location;
    }
}
