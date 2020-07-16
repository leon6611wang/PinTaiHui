package com.zhiyu.quanzhu.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zhiyu.quanzhu.R;

/**
 * 抽屉菜单布局
 * 作者:leon
 */
public class LeonDrawerMenuLayout extends FrameLayout implements View.OnClickListener {
    private View contentView, menuView, handleView;
    private int menuHeight, handleHeight;
    private boolean isShow;

    public LeonDrawerMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initViews();
    }

    public void setHeight(int height) {
        menuHeight = height;
        initAnima();
    }

    private void initViews() {
        contentView = this.getChildAt(0);
        menuView = this.getChildAt(1);
        handleView = menuView.findViewById(R.id.handleView);
        handleView.setOnClickListener(this);
        if (null != menuView && menuHeight == 0) {
            menuHeight = menuView.getHeight();
            handleHeight = handleView.getHeight();
        }
    }

    private void initAnima() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(menuView, "translationY", 0.0f, -(menuHeight - handleHeight), -(menuHeight - handleHeight));
        objectAnimator.setDuration(0);
        objectAnimator.start();
    }

    public void hideAnima() {
        isShow = false;
        if (null != onMenuOperationListener) {
            onMenuOperationListener.onMenuOpen(isShow);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(menuView, "translationY", 0.0f, -(menuHeight - handleHeight), -(menuHeight - handleHeight));
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    private void showAnima() {
        isShow = true;
        if (null != onMenuOperationListener) {
            onMenuOperationListener.onMenuOpen(isShow);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(menuView, "translationY", -(menuHeight - handleHeight), 0.0f, 0.0f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.handleView:
                if (isShow) {
                    hideAnima();
                } else {
                    showAnima();
                }
                break;
        }
    }

    private OnMenuOperationListener onMenuOperationListener;

    public void setOnMenuOperationListener(OnMenuOperationListener listener) {
        this.onMenuOperationListener = listener;
    }

    public interface OnMenuOperationListener {
        void onMenuOpen(boolean isShow);
    }
}
