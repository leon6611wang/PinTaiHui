package com.leon.myvideoplaerlibrary.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * TinyHung@Outlook.com
 * 2019/4/9
 * Video Cover Controller Base
 * 封面控制器基类
 */

public abstract class BaseCoverController extends FrameLayout {

    public BaseCoverController( Context context) {
        this(context,null);
    }

    public BaseCoverController( Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseCoverController( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mOnStartListener){
                    mOnStartListener.onStartPlay();
                }
            }
        });
    }

    public interface OnStartListener{
        void onStartPlay();
    }

    protected OnStartListener mOnStartListener;

    public void setOnStartListener(OnStartListener onStartListener) {
        mOnStartListener = onStartListener;
    }

    public void onDestroy(){
        mOnStartListener=null;
    }
}