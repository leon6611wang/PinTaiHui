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

/**
 * 性别
 */
public class GenderDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView femaleTextView, maleTextView, cancelTextView;

    public GenderDialog(@NonNull Context context, int themeResId, OnGenderSelectListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onGenderSelectListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gender);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        femaleTextView = findViewById(R.id.femaleTextView);
        femaleTextView.setOnClickListener(this);
        maleTextView = findViewById(R.id.maleTextView);
        maleTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.femaleTextView:
                if (null != onGenderSelectListener) {
                    onGenderSelectListener.onGenderSelect(2, "女");
                }
                dismiss();
                break;
            case R.id.maleTextView:
                if (null != onGenderSelectListener) {
                    onGenderSelectListener.onGenderSelect(1, "男");
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnGenderSelectListener onGenderSelectListener;

    public interface OnGenderSelectListener {
        void onGenderSelect(int index, String gender);
    }
}
