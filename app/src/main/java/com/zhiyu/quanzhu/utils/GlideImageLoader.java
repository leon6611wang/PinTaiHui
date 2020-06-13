package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.zhiyu.quanzhu.R;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path)
                //异常时候显示的图片
                .error(R.drawable.image_error)
                //加载成功前显示的图片
                .placeholder(R.drawable.image_error)
                //url为空的时候,显示的图片
                .fallback(R.drawable.image_error)
                .into(imageView);
    }
}
