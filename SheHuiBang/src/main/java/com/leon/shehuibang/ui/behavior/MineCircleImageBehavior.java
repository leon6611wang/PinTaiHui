package com.leon.shehuibang.ui.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class MineCircleImageBehavior extends CoordinatorLayout.Behavior<View> {
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public MineCircleImageBehavior() {
    }

    public MineCircleImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0; //判定只接受垂直方向的滚动
//    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }

        float dy = dependency.getY() - child.getHeight();
        dy = dy < 0 ? 0 : dy;
//        float y = -(dy / deltaY) * child.getHeight();
//        child.setTranslationY(-y/2);

        float scale = dy / deltaY;
        scale = Math.abs(scale);
        child.setScaleX(scale);
        child.setScaleY(scale);
        return true;
    }
}
