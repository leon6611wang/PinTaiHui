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
 * 是否保存为草稿
 */
public class DrafDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView titleTextView, cancelTextView, confirmTextView;

    public DrafDialog(@NonNull Context context, int themeResId, OnDrafListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onDrafListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_draf);
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
                if (null != onDrafListener) {
                    onDrafListener.onCancel();
                }
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onDrafListener) {
                    onDrafListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    private OnDrafListener onDrafListener;

    public interface OnDrafListener {
        void onConfirm();

        void onCancel();
    }
}
