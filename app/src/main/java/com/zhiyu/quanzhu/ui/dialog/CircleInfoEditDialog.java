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
 * 圈子详情编辑框
 * 公告/简介
 */
public class CircleInfoEditDialog extends Dialog implements View.OnClickListener {
    private TextView titleTextView, cancelTextView, confirmTextView;
    private EditText contentEditText;
    private int editType = 1;//1:公告，2:简介,3:申请入圈
    private String title, hint;


    public CircleInfoEditDialog(@NonNull Context context, int themeResId, OnEditListener listener) {
        super(context, themeResId);
        this.onEditListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_info_edit);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext()) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    public void setEditType(int type) {
        this.editType = type;
        switch (editType) {
            case 1:
                title = "编辑公告";
                hint = "请输入您要发表的圈公告...";
                break;
            case 2:
                title = "编辑圈子简介";
                hint = "请输入您的圈子简介信息...";
                break;
            case 3:
                title="申请信息";
                hint="请输入您的入圈申请信息...";
                break;
        }
        titleTextView.setText(title);
        contentEditText.setText(null);
        contentEditText.setHint(hint);
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
                if (null != onEditListener) {
                    onEditListener.onEdit(editType, content);
                }
                dismiss();
                break;
        }
    }

    private OnEditListener onEditListener;

    public interface OnEditListener {
        void onEdit(int editType, String content);
    }
}
