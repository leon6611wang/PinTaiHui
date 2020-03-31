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
 * 我的发布-文章操作
 */
public class ArticleOperatDialog extends Dialog implements View.OnClickListener {
    private TextView upTextView, downTextView, deleteTextView, cancelTextView;
    private int position;

    public ArticleOperatDialog(@NonNull Context context, int themeResId, OnArticleOperatListener listener) {
        super(context, themeResId);
        this.onArticleOperatListener = listener;
    }

    public void setPosition(int p) {
        this.position = p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_article_operat);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        upTextView = findViewById(R.id.upTextView);
        upTextView.setOnClickListener(this);
        downTextView = findViewById(R.id.downTextView);
        downTextView.setOnClickListener(this);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upTextView:
                if (null != onArticleOperatListener) {
                    onArticleOperatListener.onArticleOperat(1, position, "文章上架");
                }
                dismiss();
                break;
            case R.id.downTextView:
                if (null != onArticleOperatListener) {
                    onArticleOperatListener.onArticleOperat(2, position, "文章下架");
                }
                dismiss();
                break;
            case R.id.deleteTextView:
                if (null != onArticleOperatListener) {
                    onArticleOperatListener.onArticleOperat(3, position, "文章删除");
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private OnArticleOperatListener onArticleOperatListener;

    public interface OnArticleOperatListener {
        void onArticleOperat(int index, int position, String desc);
    }

}
