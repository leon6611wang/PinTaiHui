package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * yes or no dialog
 */
public class RefuseReasonDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView titleTextView, cancelTextView, confirmTextView;
    private EditText mEditText;

    public RefuseReasonDialog(@NonNull Context context, int themeResId, OnRefuseJoinCircleListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onRefuseJoinCircleListener=listener;
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_refuse_join_circle);
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
        mEditText=findViewById(R.id.mEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if(null!=onRefuseJoinCircleListener){
                    String content=mEditText.getText().toString().trim();
                    onRefuseJoinCircleListener.onRefuseJoinCircle(content);
                }
                dismiss();
                break;
        }
    }
    private OnRefuseJoinCircleListener onRefuseJoinCircleListener;
    public interface  OnRefuseJoinCircleListener{
        void onRefuseJoinCircle(String refuse_content);
    }

}
