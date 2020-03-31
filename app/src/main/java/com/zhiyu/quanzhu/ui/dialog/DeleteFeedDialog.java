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

/**
 * 首页-圈子-关注-删除
 */
public class DeleteFeedDialog extends Dialog implements View.OnClickListener {
    private TextView unfollowTextView, complaintTextView, cancelTextView;
    private int position;

    public DeleteFeedDialog(@NonNull Context context, int themeResId, OnDeleteFeedListener listener) {
        super(context, themeResId);
        this.onDeleteFeedListener = listener;
    }

    public void setPosition(int p) {
        this.position = p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_feed);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        unfollowTextView = findViewById(R.id.unfollowTextView);
        unfollowTextView.setOnClickListener(this);
        complaintTextView = findViewById(R.id.complaintTextView);
        complaintTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unfollowTextView:
                if (null != onDeleteFeedListener) {
                    onDeleteFeedListener.onDeleteFeed(1, position, "取消关注");
                }
                dismiss();
                break;
            case R.id.complaintTextView:
                if (null != onDeleteFeedListener) {
                    onDeleteFeedListener.onDeleteFeed(2, position, "投诉该条内容");
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnDeleteFeedListener onDeleteFeedListener;

    public interface OnDeleteFeedListener {
        void onDeleteFeed(int index, int position, String desc);
    }

}
