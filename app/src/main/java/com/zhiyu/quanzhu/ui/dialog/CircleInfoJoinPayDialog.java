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
 * 圈子详情
 * 入圈付费
 */
public class CircleInfoJoinPayDialog extends Dialog implements View.OnClickListener {
    private TextView cancelTextView, confirmTextView;
    private TextView contentTextView;

    public void setPrice(String price) {
        contentTextView.setText("该商圈需要支付￥" + price + "元入圈费用，并等待圈主审核通过后加入。若圈主24小时内未审核或审核不通过，则费用将原路退还。");
    }

    public CircleInfoJoinPayDialog(@NonNull Context context, int themeResId, OnPayListener listener) {
        super(context, themeResId);
        this.onPayListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_info_join_pay);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext()) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }


    private void initViews() {
        contentTextView = findViewById(R.id.contentTextView);
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
                if(null!=onPayListener){
                    onPayListener.onPay();
                }
                dismiss();
                break;
        }
    }

    private OnPayListener onPayListener;

    public interface OnPayListener {
        void onPay();
    }
}
