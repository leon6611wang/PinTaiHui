package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class CircleBannerSortMenuDialog extends Dialog implements View.OnClickListener {
    private TextView updateTextView, deleteTextView, cancelTextView;
    private int position;

    public void setPosition(int p){
        this.position=p;
    }
    public CircleBannerSortMenuDialog(@NonNull Context context, int themeResId, OnCircleBannerSortMenuListener listener) {
        super(context, themeResId);
        this.onCircleBannerSortMenuListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_banner_sort_menu);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        updateTextView = findViewById(R.id.updateTextView);
        updateTextView.setOnClickListener(this);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateTextView:
                if (null != onCircleBannerSortMenuListener) {
                    onCircleBannerSortMenuListener.onCircleBannerSortMenu(1,position);
                }
                dismiss();
                break;
            case R.id.deleteTextView:
                if (null != onCircleBannerSortMenuListener) {
                    onCircleBannerSortMenuListener.onCircleBannerSortMenu(2,position);
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnCircleBannerSortMenuListener onCircleBannerSortMenuListener;

    public interface OnCircleBannerSortMenuListener {
        void onCircleBannerSortMenu(int index,int position);
    }
}
