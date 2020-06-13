package com.zhiyu.quanzhu.model.bean;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class MessageImage {
    private String url;
    private String path;
    private int width;
    private int height;

    private LinearLayout.LayoutParams imageParams;
    private float ratio = 0.64f;
    private int screenWidth;
    private int max;

    public ViewGroup.LayoutParams getImageParams(Context context) {
        if (null == imageParams) {
            if (screenWidth == 0) {
                screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
                max = Math.round(screenWidth * ratio);
            }
            if (!StringUtils.isNullOrEmpty(url)) {
                if (width == 0 || height == 0) {
                    int[] wh = ImageUtils.getInstance().getLocalImageWidthHeight(url);
                    width = wh[0];
                    height = wh[1];
                }
                if (width >= height) {
                    int _height = Math.round(max * ((float) height / (float) width));
                    imageParams = new LinearLayout.LayoutParams(max, _height);
                } else if (height >= width) {
                    int _width = Math.round(max * ((float) width / (float) height));
                    imageParams = new LinearLayout.LayoutParams(_width, max);
                }
            }
        }
        return imageParams;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
