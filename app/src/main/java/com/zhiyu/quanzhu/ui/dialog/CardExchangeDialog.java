package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 圈子详情编辑框
 * 公告/简介
 */
public class CardExchangeDialog extends Dialog implements View.OnClickListener {
    private TextView cancelTextView, confirmTextView;
    private EditText contentEditText;
    private int position;
    public void setPosition(int p){
        this.position=p;
    }


    public CardExchangeDialog(@NonNull Context context, int themeResId, OnCardExhangeListener listener) {
        super(context, themeResId);
        this.onCardExhangeListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_exchange);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext()) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    @Override
    public void show() {
        super.show();
        if(null!=contentEditText){
            contentEditText.setText(null);
        }
    }

    private void initViews() {
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
                if (!StringUtils.isNullOrEmpty(content)) {
                    if (null != onCardExhangeListener) {
                        onCardExhangeListener.onExchange(content,position);
                    }
                    dismiss();
                } else {
                    MessageToast.getInstance(getContext()).show("请输入申请加圈友消息");
                }
                break;
        }
    }

    private OnCardExhangeListener onCardExhangeListener;

    public interface OnCardExhangeListener {
        void onExchange(String content,int position);
    }
}
