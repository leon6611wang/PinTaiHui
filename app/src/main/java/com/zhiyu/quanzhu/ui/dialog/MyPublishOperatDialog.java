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
 * 我的发布-文章操作
 */
public class MyPublishOperatDialog extends Dialog implements View.OnClickListener {
    private LinearLayout upLayout, downLayout, deleteLayout;
    private TextView upTextView, downTextView, deleteTextView, cancelTextView;
    private int position;
    private int type = 0;//1:article,2:video
    private boolean isPublish;

    public MyPublishOperatDialog(@NonNull Context context, int themeResId, OnArticleOperatListener listener) {
        super(context, themeResId);
        this.onArticleOperatListener = listener;
    }

    public void setPosition(int p, int t, boolean is_publish) {
        this.position = p;
        this.type = t;
        this.isPublish = is_publish;
        if (isPublish) {
            upLayout.setVisibility(View.GONE);
            downLayout.setVisibility(View.VISIBLE);
        } else {
            upLayout.setVisibility(View.VISIBLE);
            downLayout.setVisibility(View.GONE);
        }
        switch (type) {
            case 1:
                upTextView.setText("文章上架");
                downTextView.setText("文章下架");
                deleteTextView.setText("文章删除");
                break;
            case 2:
                upTextView.setText("视频上架");
                downTextView.setText("视频下架");
                deleteTextView.setText("视频删除");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mypublish_operat);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        upLayout = findViewById(R.id.upLayout);
        downLayout = findViewById(R.id.downLayout);
        deleteLayout = findViewById(R.id.deleteLayout);
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
                    onArticleOperatListener.onArticleOperat(1, position,type, "文章上架");
                }
                dismiss();
                break;
            case R.id.downTextView:
                if (null != onArticleOperatListener) {
                    onArticleOperatListener.onArticleOperat(2, position,type, "文章下架");
                }
                dismiss();
                break;
            case R.id.deleteTextView:
                if (null != onArticleOperatListener) {
                    onArticleOperatListener.onArticleOperat(3, position,type, "文章删除");
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
        void onArticleOperat(int index, int position,int type, String desc);
    }

}
