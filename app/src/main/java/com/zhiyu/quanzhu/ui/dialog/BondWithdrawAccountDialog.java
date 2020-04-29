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
 * 绑定提现账户
 */
public class BondWithdrawAccountDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView cancelTextView, confirmTextView;

    public BondWithdrawAccountDialog(@NonNull Context context, int themeResId, OnBondWithdrawAccountListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onBondListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bond_withdraw_acount);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
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
                if (null != onBondListener) {
                    onBondListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    private OnBondWithdrawAccountListener onBondListener;

    public interface OnBondWithdrawAccountListener {
        void onConfirm();
    }
}
