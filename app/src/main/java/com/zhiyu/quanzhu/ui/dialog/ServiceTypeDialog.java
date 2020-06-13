package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeDialog extends Dialog implements View.OnClickListener {
    private LoopView mLoopView;
    private List<String> list = new ArrayList<>();
    private String serviceType;
    private int serviceTypeIndex = 1;
    private TextView cancelTextView, confirmTextView;

    public ServiceTypeDialog(@NonNull Context context, int themeResId, OnServiceTypeListener listener) {
        super(context, themeResId);
        this.onServiceTypeListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_type);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        list.add("仅退款");
        list.add("退货退款");
        list.add("换货");
        serviceType = list.get(0);
    }

    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mLoopView = findViewById(R.id.mLoopView);
        mLoopView.setItems(list);
        mLoopView.setInitPosition(0);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                serviceTypeIndex = index + 1;
                serviceType = list.get(index);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onServiceTypeListener) {
                    onServiceTypeListener.onServiceType(serviceTypeIndex, serviceType);
                }
                dismiss();
                break;
        }
    }

    private OnServiceTypeListener onServiceTypeListener;

    public interface OnServiceTypeListener {
        void onServiceType(int typeIndex, String servieType);
    }
}
