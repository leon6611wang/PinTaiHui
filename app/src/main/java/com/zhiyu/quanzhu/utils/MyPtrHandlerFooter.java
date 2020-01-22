package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.util.Log;
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

public class MyPtrHandlerFooter implements PtrUIHandler {
    private Context context;
    private ImageView img;
    private TextView tip;

    public MyPtrHandlerFooter(Context context, ViewGroup parent) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.footer_ptr, parent);
        this.img = view.findViewById(R.id.id_footer_iv_img);
        this.tip = view.findViewById(R.id.id_footer_tv_tip);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tip.setText("上拉加载更多");
    }


    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tip.setText("正在加载...");
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        img.startAnimation(animation);//開始动画
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        tip.setText("加载完成");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
    }
}
