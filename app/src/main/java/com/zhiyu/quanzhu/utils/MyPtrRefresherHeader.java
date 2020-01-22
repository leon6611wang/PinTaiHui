package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;

public class MyPtrRefresherHeader extends LinearLayout {
    public MyPtrRefresherHeader(Context context) {
        super(context, null);
        LayoutInflater.from(context).inflate(R.layout.header_ptr, this);
    }

    public MyPtrRefresherHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.header_ptr, this);
    }

    public MyPtrRefresherHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.header_ptr, this);
    }

}
