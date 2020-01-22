package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhiyu.quanzhu.R;

public class RootLayout extends FrameLayout {
    private View noDataView,noNetView;
    public RootLayout(@NonNull Context context) {
        super(context);
    }

    public RootLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        noDataView= LayoutInflater.from(context).inflate(R.layout.rootlayout_no_data,null);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }
}
