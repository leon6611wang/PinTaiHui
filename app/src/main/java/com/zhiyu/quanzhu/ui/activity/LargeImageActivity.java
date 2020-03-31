package com.zhiyu.quanzhu.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 大图查看器
 */
public class LargeImageActivity extends BaseActivity {

    private LargeImageView largeImageView;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        imgUrl = getIntent().getStringExtra("imgUrl");
        initViews();
    }

    private void initViews() {
        largeImageView = findViewById(R.id.largeImageView);
        largeImageView.setEnabled(true);
        largeImageView.setHorizontalScrollBarEnabled(false);
        largeImageView.setVerticalScrollBarEnabled(false);
        largeImageView.setBackgroundColor(getResources().getColor(R.color.text_color_black));
        Glide.with(this).asBitmap().load(imgUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        largeImageView.setImage(resource);
                    }
                });
        largeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
