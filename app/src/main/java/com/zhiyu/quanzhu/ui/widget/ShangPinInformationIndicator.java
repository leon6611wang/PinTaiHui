package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

public class ShangPinInformationIndicator extends LinearLayout {
    private int count=1, current=1;

    public ShangPinInformationIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setBackground(getContext().getDrawable(R.drawable.shape_shangpin_info_indicator_bg));
        this.getBackground().setAlpha(77);
    }

    public void setCount(int c){
        this.count=c;
        TextView textView = new TextView(getContext());
        textView.setText(current + "/" + count);
        textView.setTextSize(16);
        textView.setTextColor(getContext().getResources().getColor(R.color.white));
        this.addView(textView);
    }

    public void setCurrent(int c){
        this.current=c;
        TextView textView=(TextView)this.getChildAt(0);
        textView.setText(current + "/" + count);
    }
}
