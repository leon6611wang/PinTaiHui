package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MoveImageView extends AppCompatImageView {
    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLocation(int x, int y) {
        this.setFrame(x, y - this.getHeight(), x + this.getWidth(), y);
    }

    // 移动
    public boolean autoMouse(MotionEvent event) {
        boolean rb = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.setLocation((int) event.getX(), (int) event.getY());
                rb = true;
                break;
        }
        return rb;
    }
}
