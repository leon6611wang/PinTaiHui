package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.PublishArticleActivity;
import com.zhiyu.quanzhu.ui.activity.PublishFeedActivity;
import com.zhiyu.quanzhu.ui.activity.PublishVideoActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 发布(文章，视频，动态)
 */
public class PublishDialog extends Dialog implements View.OnClickListener {
    private TextView publishArticleTextView, publishVideoTextView, publishFeedTextView, cancelTextView;

    public PublishDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_publish);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
//        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initViews() {
        publishArticleTextView = findViewById(R.id.publishArticleTextView);
        publishArticleTextView.setOnClickListener(this);
        publishVideoTextView = findViewById(R.id.publishVideoTextView);
        publishVideoTextView.setOnClickListener(this);
        publishFeedTextView = findViewById(R.id.publishFeedTextView);
        publishFeedTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publishArticleTextView:
                Intent articleIntent = new Intent(getContext(), PublishArticleActivity.class);
                articleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(articleIntent);
                dismiss();
                break;
            case R.id.publishVideoTextView:
                Intent videoIntent = new Intent(getContext(), PublishVideoActivity.class);
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(videoIntent);
                dismiss();
                break;
            case R.id.publishFeedTextView:
                Intent feedIntent = new Intent(getContext(), PublishFeedActivity.class);
                feedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(feedIntent);
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }
}
