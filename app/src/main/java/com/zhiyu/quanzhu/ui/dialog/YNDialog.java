package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * yes or no dialog
 */
public class YNDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView titleTextView, cancelTextView, confirmTextView;

    public YNDialog(@NonNull Context context, int themeResId, OnYNListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onYNListener = listener;
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yn);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onYNListener) {
                    onYNListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    private OnYNListener onYNListener;

    public interface OnYNListener {
        void onConfirm();
    }
}
