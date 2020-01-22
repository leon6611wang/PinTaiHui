package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;

public class MyPtrRefresherFooter extends LinearLayout {
    public MyPtrRefresherFooter(Context context) {
        super(context, null);
        LayoutInflater.from(context).inflate(R.layout.footer_ptr, this);
    }

    public MyPtrRefresherFooter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.footer_ptr, this);
    }

    public MyPtrRefresherFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.footer_ptr, this);
    }

}
