package com.leon.shehuibang.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.leon.shehuibang.R;

public class HiddenAnimUtils {
    private int mHeight;//伸展高度
    private View hideView;//需要展开隐藏的布局，开关控件
    private RotateAnimation animation;//旋转动画

    /**
     * 构造器(可根据自己需要修改传参)
     *
     * @param context  上下文
     * @param hideView 需要隐藏或显示的布局view
     */
    public static HiddenAnimUtils newInstance(Context context, View hideView) {
        return new HiddenAnimUtils(context, hideView);
    }

    private HiddenAnimUtils(Context context, View hideView) {
        this.hideView = hideView;
        mHeight = (int) context.getResources().getDimension(R.dimen.dp_50);
    }

    /**
     * 开关
     */
    public void toggle() {
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }


    private void openAnim(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.start();
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, mHeight, 0);
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.GONE);
//            }
//        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator arg0) {
//                int value = (int) arg0.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
//                layoutParams.height = value;
//                v.setLayoutParams(layoutParams);
//            }
//        });
        return animator;
    }
}
