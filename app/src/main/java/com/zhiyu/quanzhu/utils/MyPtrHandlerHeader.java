package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class MyPtrHandlerHeader implements PtrUIHandler {
    private Context context;

    private ImageView img;
    private TextView tip;

    public MyPtrHandlerHeader(Context context, ViewGroup parent) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.header_ptr, parent);
        this.img = view.findViewById(R.id.id_header_iv_img);
        this.tip = view.findViewById(R.id.id_header_tv_tip);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tip.setText("下拉刷新数据");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tip.setText("正在加载...");
//        RotateAnimation animation = new RotateAnimation(0, 360, 0.5f, 0.5f);
//        animation.setFillAfter(false);
//        animation.setDuration(1000);
//        animation.setRepeatMode(Animation.RESTART);
//        img.startAnimation(animation);

//        Animation animation = new RotateAnimation(0, 359);
//        animation.setDuration(500);
//        animation.setRepeatMode(Animation.RESTART);
//        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
//        img.startAnimation(animation);//開始动画

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        img.startAnimation(animation);//開始动画
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        tip.setText("刷新完成");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
    }
}
