package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class LargeImageDialog extends Dialog {
    private LargeImageView largeImageView;
    private Context mContext;
    public LargeImageDialog(@NonNull Context context) {
        super(context);
        this.mContext=context;

    }

    public LargeImageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_large_image);
        initParams();
        initViews();
    }

    private void initParams(){
        Window window = this.getWindow() ;
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        window.setAttributes(p);
        window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    public void setImageUrl(String imageUrl){
        Glide.with(getContext()).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                largeImageView.setImage(resource);
            }
        });
    }

    private void initViews(){
        largeImageView =findViewById(R.id.imageView);
        largeImageView.setEnabled(true);
        largeImageView.setHorizontalScrollBarEnabled(false);
        largeImageView.setVerticalScrollBarEnabled(false);
        largeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
