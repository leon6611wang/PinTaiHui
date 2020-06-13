package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class OrderConfirmUseCouponDialog extends Dialog implements View.OnClickListener {
    private LinearLayout useLayout, unuseLayout;
    private ImageView useImageView, unuseImageView;
    private TextView moneyTextView;
    private int index;

    public OrderConfirmUseCouponDialog(@NonNull Context context, int themeResId, OnSelectCouponListener listener) {
        super(context, themeResId);
        this.onSelectCouponListener = listener;
    }

    public void setIndex(int i, int couponMoney) {
        this.index = i;
        moneyTextView.setText(PriceParseUtils.getInstance().parsePrice(couponMoney));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_confirm_use_coupon);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        useLayout = findViewById(R.id.useLayout);
        useLayout.setOnClickListener(this);
        unuseLayout = findViewById(R.id.unuseLayout);
        unuseLayout.setOnClickListener(this);
        useImageView = findViewById(R.id.useImageView);
        unuseImageView = findViewById(R.id.unuseImageView);
        moneyTextView = findViewById(R.id.moneyTextView);
    }

    public void setUseCoupon(boolean use) {
        layoutChange(use ? 1 : 2);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.useLayout:
                layoutChange(1);
                if (null != onSelectCouponListener) {
                    onSelectCouponListener.onSelectCoupon(index, true);
                }
                dismiss();
                break;
            case R.id.unuseLayout:
                layoutChange(2);
                if (null != onSelectCouponListener) {
                    onSelectCouponListener.onSelectCoupon(index, false);
                }
                dismiss();
                break;
        }
    }

    private void layoutChange(int position) {
        useImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.gouwuche_unselect));
        unuseImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.gouwuche_unselect));
        switch (position) {
            case 1:
                useImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.gouwuche_selected));
                break;
            case 2:
                unuseImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.gouwuche_selected));
                break;
        }
    }

    private OnSelectCouponListener onSelectCouponListener;

    public interface OnSelectCouponListener {
        void onSelectCoupon(int index, boolean isUseCoupon);
    }
}
