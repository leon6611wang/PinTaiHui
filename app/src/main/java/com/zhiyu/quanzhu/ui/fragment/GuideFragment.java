package com.zhiyu.quanzhu.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.LoginGetVertifyCodeActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class GuideFragment extends Fragment {
    private ImageView mImageView;
    private TextView titleTextView;
    private View view;
    private int index;
    private int screenWidth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide, null);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(getContext());
        Bundle bundle = getArguments();
        index = bundle.getInt("index");
        initViews();
        if (index == 1) {
            rightEndter();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (null != mImageView)
            if (isVisibleToUser) {
                rightEndter();
            } else {
                leftLeave();
            }
    }

    private void initViews() {
        mImageView = view.findViewById(R.id.mImageView);
        titleTextView = view.findViewById(R.id.titleTextView);
        String titleContent = null;
        int guide_image = 0;
        switch (index) {
            case 1:
                guide_image = R.mipmap.guide1;
                titleContent = "智慧商圈 助力你我";
                break;
            case 2:
                guide_image = R.mipmap.guide2;
                titleContent = "私域电商 舍我其谁";
                break;
            case 3:
                guide_image = R.mipmap.guide3;
                titleContent = "六度人脉 八面来财";
                break;
            case 4:
                guide_image = R.mipmap.guide4;
                titleContent = "我的社区 我的商界";
                break;
        }
        titleTextView.setText(titleContent);
        mImageView.setImageDrawable(getResources().getDrawable(guide_image));

    }

    public void rightEndter() {
        float[] x = {screenWidth, (screenWidth / 4) * 3, (screenWidth / 4) * 2, screenWidth / 4, 0};
        ObjectAnimator startAnim = ObjectAnimator.ofFloat(mImageView, "translationX", x);
        mImageView.setVisibility(View.VISIBLE);

        //创建透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(titleTextView, "alpha", 0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(startAnim).with(alpha);
        set.setDuration(1000);
        set.start();
    }

    public void leftLeave() {
        float[] x = {0f, -screenWidth};
        ObjectAnimator endAnim = ObjectAnimator.ofFloat(mImageView, "translationX", x);
        //创建透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(titleTextView, "alpha", 1.0f, 0f);
        AnimatorSet set = new AnimatorSet();
        set.play(endAnim).with(alpha);
        set.setDuration(1000);
        set.start();
    }

}
