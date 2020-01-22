package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class ChoosePhotoDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView xiangceTextView, paizhaoTextView, quxiaoTextView;

    public ChoosePhotoDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ChoosePhotoDialog(@NonNull Context context, int themeResId, OnChoosePhotoListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onChoosePhotoListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choosephoto);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initViews() {
        xiangceTextView = findViewById(R.id.xiangceTextView);
        xiangceTextView.setOnClickListener(this);
        paizhaoTextView = findViewById(R.id.paizhaoTextView);
        paizhaoTextView.setOnClickListener(this);
        quxiaoTextView = findViewById(R.id.quxiaoTextView);
        quxiaoTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiangceTextView:
                if (null != onChoosePhotoListener) {
                    this.onChoosePhotoListener.xiangce();
                    dismiss();
                }
                break;
            case R.id.paizhaoTextView:
                if (null != onChoosePhotoListener) {
                    this.onChoosePhotoListener.paizhao();
                    dismiss();
                }
                break;
            case R.id.quxiaoTextView:
                dismiss();
                break;
        }
    }

    private OnChoosePhotoListener onChoosePhotoListener;

    public interface OnChoosePhotoListener {
        void xiangce();

        void paizhao();
    }
}
