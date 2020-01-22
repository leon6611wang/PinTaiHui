package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class InputDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView titleTextView, cancelTextView, confirmTextView;
    private EditText contentEditText;

    public InputDialog(@NonNull Context context, int themeResId, OnInputCallback callback) {
        super(context, themeResId);
        this.context = context;
        this.onInputCallback = callback;
    }

    public void setTitleAndHint(String title,String hint){
        if(null!=titleTextView){
            titleTextView.setText(title);
        }
        if(null!=contentEditText){
            contentEditText.setHint(hint);
            contentEditText.setText(null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        contentEditText = findViewById(R.id.contentEditText);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                String content = contentEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(content) && null != onInputCallback) {
                    onInputCallback.onInput(content);
                }
                dismiss();
                break;
        }
    }

    private OnInputCallback onInputCallback;

    public interface OnInputCallback {
        void onInput(String content);
    }
}
