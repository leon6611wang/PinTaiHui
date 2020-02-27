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

public class DeleteImageDialog extends Dialog implements View.OnClickListener {
    private TextView deleteTextView, cancelTextView;

    public DeleteImageDialog(@NonNull Context context, int themeResId, OnDeleteImageClickListener listener) {
        super(context, themeResId);
        this.onDeleteImageClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_image);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteTextView:
                if (null != onDeleteImageClickListener) {
                    onDeleteImageClickListener.onDeleteImage();
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnDeleteImageClickListener onDeleteImageClickListener;

    public interface OnDeleteImageClickListener {
        void onDeleteImage();
    }
}
