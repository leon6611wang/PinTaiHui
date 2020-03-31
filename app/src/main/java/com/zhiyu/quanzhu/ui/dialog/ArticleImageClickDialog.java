package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 文章图片点击
 */
public class ArticleImageClickDialog extends Dialog implements View.OnClickListener {
    private TextView deleteImage, topInsertText, bottomInsertText, cancelTextView;
    private int index;
    private LinearLayout topLayout, bottomLayout;

    public void setIndex(int i) {
        this.index = i;
    }

    public void showTop(boolean isShow) {
        topLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void showBottom(boolean isShow) {
        bottomLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public ArticleImageClickDialog(@NonNull Context context, int themeResId, OnArticleImageClickListener listener) {
        super(context, themeResId);
        this.onArticleImageClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_article_image_click);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        deleteImage = findViewById(R.id.deleteImage);
        deleteImage.setOnClickListener(this);
        topInsertText = findViewById(R.id.topInsertText);
        topInsertText.setOnClickListener(this);
        bottomInsertText = findViewById(R.id.bottomInsertText);
        bottomInsertText.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        topLayout = findViewById(R.id.topLayout);
        bottomLayout = findViewById(R.id.bottomLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteImage:
                if (null != onArticleImageClickListener) {
                    onArticleImageClickListener.onArticleImageClick(1, index);
                }
                dismiss();
                break;
            case R.id.topInsertText:
                if (null != onArticleImageClickListener) {
                    onArticleImageClickListener.onArticleImageClick(2, index);
                }
                dismiss();
                break;
            case R.id.bottomInsertText:
                if (null != onArticleImageClickListener) {
                    onArticleImageClickListener.onArticleImageClick(3, index);
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnArticleImageClickListener onArticleImageClickListener;

    public interface OnArticleImageClickListener {
        void onArticleImageClick(int position, int index);
    }

}
