package com.zhiyu.quanzhu.ui.dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class WaitDialog extends Dialog {
    private Context mContext;
    private ImageView waitImageView;
    private TextView mTextView;

    public WaitDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    public WaitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    public void setNotice(String notice){
        mTextView.setText(notice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext) / 5*2 ;
        params.height = ScreentUtils.getInstance().getScreenWidth(mContext) / 5*2 ;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);
    }

    private void initViews() {
        waitImageView = findViewById(R.id.waitImageView);
        mTextView=findViewById(R.id.mTextView);
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
