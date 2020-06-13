package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * yes or no dialog
 */
public class ShareInnerDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView nameTextView, contentTextView, cancelTextView, confirmTextView;
    private NiceImageView avatarImageView;

    public ShareInnerDialog(@NonNull Context context, int themeResId, OnShareInnerListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onShareInnerListener = listener;
    }

    public void setShareContent(String avatar,String name,String content){
        if(!StringUtils.isNullOrEmpty(avatar)){
            avatarImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(avatar).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(avatarImageView);
        }else{
            avatarImageView.setVisibility(View.GONE);
        }
        nameTextView.setText(name);
        contentTextView.setText(content);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_innner);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        contentTextView = findViewById(R.id.contentTextView);
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
                if (null != onShareInnerListener) {
                    onShareInnerListener.onShareInner();
                }
                dismiss();
                break;
        }
    }

    private OnShareInnerListener onShareInnerListener;

    public interface OnShareInnerListener {
        void onShareInner();
    }
}
