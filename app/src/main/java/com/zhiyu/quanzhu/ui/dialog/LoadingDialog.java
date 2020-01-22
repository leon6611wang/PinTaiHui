package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class LoadingDialog extends Dialog {
    private Context mContext;
    private ImageView waitImageView;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext) / 5 * 2;
        params.height = ScreentUtils.getInstance().getScreenWidth(mContext) / 5 * 2;
        params.dimAmount = 0.0f;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initViews() {
        waitImageView = findViewById(R.id.waitImageView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.wait_dialog_anim);
            waitImageView.startAnimation(animation);
        }
    }
}
