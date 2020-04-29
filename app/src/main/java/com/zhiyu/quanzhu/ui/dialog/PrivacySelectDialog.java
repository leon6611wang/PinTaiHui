package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.CalendarUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 隐私
 */
public class PrivacySelectDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LoopView mLoopView;
    private String privacy;
    private int privacyIndex;
    private List<String> list = new ArrayList<>();
    private TextView cancelTextView, confirmTextView;

    public PrivacySelectDialog(@NonNull Context context, int themeResId, OnPrivacySelectListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onPrivacySelectListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_privacy_select);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        list.add("不公开");
        list.add("公开（任何人可见）");
        list.add("仅好友可见");
        list.add("仅实名认证用户可见");
        list.add("保密（任何人不可见）");
        privacy = list.get(0);
    }


    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mLoopView = findViewById(R.id.mLoopView);
        mLoopView.setItems(list);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                privacyIndex = index;
                privacy = list.get(index);
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
                if (null != onPrivacySelectListener) {
                    onPrivacySelectListener.onPrivacySelect(privacy, privacyIndex);
                }
                dismiss();
                break;
        }
    }

    private OnPrivacySelectListener onPrivacySelectListener;

    public interface OnPrivacySelectListener {
        void onPrivacySelect(String privacy, int index);
    }
}
