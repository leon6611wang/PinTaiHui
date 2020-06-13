package com.zhiyu.quanzhu.model.bean;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class GoodsImg {
    private String url;
    private int width;
    private int height;
    private boolean isVideo=false;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams videoParams;
    private LinearLayout.LayoutParams imageParams;

    public LinearLayout.LayoutParams getLayoutParams(int screenWidth) {
        if (width >= height) {
            int _height = Math.round(screenWidth * ((float) height / (float) width));
            layoutParams = new LinearLayout.LayoutParams(screenWidth, _height);
        } else {
            int _width = Math.round(screenWidth * ((float) width / (float) height));
            layoutParams = new LinearLayout.LayoutParams(_width, screenWidth);
        }
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    public LinearLayout.LayoutParams getVideoParams(Context context) {
        if (null == videoParams) {
            int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
            if (width == 0 || height == 0) {
                videoParams = new LinearLayout.LayoutParams(screenWidth, screenWidth);
            } else {
                if (width > height) {
                    int _height = Math.round(screenWidth * ((float) height / (float) width));
                    videoParams = new LinearLayout.LayoutParams(screenWidth, _height);
                } else {
                    int _width = Math.round(screenWidth * ((float) width / (float) height));
                    videoParams = new LinearLayout.LayoutParams(_width, screenWidth);
                }
            }
            videoParams.gravity = Gravity.CENTER;
        }
        return videoParams;
    }

    public LinearLayout.LayoutParams getImageParams(Context context) {
        if (null == imageParams) {
            int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
            int dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
            int width_ = screenWidth - dp_30;
            int height_ = Math.round(width_ * ((float) height / (float) width));
            imageParams = new LinearLayout.LayoutParams(width_, height_);
            imageParams.gravity = Gravity.CENTER;
        }
        return imageParams;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
