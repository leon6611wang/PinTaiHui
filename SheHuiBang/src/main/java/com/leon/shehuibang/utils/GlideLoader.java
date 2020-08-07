package com.leon.shehuibang.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.lcw.library.imagepicker.utils.ImageLoader;
import com.leon.shehuibang.R;

/**
 * 实现自定义图片加载
 * Create by: chenWei.li
 * Date: 2018/8/30
 * Time: 下午11:10
 * Email: lichenwei.me@foxmail.com
 */
public class GlideLoader implements ImageLoader {

    private RequestOptions mOptions = new RequestOptions()
            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(R.drawable.image_bg)
            .error(R.drawable.image_bg);

    private RequestOptions mPreOptions = new RequestOptions()
            .skipMemoryCache(true)
            .error(R.drawable.image_bg);

    @Override
    public void loadImage(ImageView imageView, String imagePath) {
        //小图加载
        Glide.with(imageView.getContext()).load(imagePath).apply(mOptions).into(imageView);
    }

    @Override
    public void loadPreImage(ImageView imageView, String imagePath) {
        //大图加载
        Glide.with(imageView.getContext()).load(imagePath).apply(mPreOptions).into(imageView);

    }

    @Override
    public void clearMemoryCache() {
        //清理缓存
//        Glide.get(BaseApplication.applicationContext).clearMemory();
    }
}
