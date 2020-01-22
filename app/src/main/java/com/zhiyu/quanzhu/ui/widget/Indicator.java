package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zhiyu.quanzhu.R;

/**
 * banner图片指示器
 */
public class Indicator extends LinearLayout {
    private LinearLayout linearLayout;
    private Context context;
    private LinearLayout.LayoutParams ll;
    private int dp_2;
    private int default_dot_selected_img = R.mipmap.dot_sel;
    private int default_dot_img = R.mipmap.dot;

    public Indicator(Context context) {
        super(context);
        init(context);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context ctxt) {
        this.context = ctxt;
        linearLayout = this;
        dp_2 = (int) context.getResources().getDimension(R.dimen.dp_2);
        ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ll.leftMargin= dp_2;
        ll.rightMargin= dp_2;
    }

    public void addParams(int count, int dot_selected_img, int dot_img) {
        if (dot_selected_img > 0) {
            default_dot_selected_img= dot_selected_img ;
        }
        if (dot_img > 0) {
            default_dot_img=dot_img;
        }
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            if (i == 0) {
                imageView.setImageDrawable(context.getDrawable(default_dot_selected_img));
            } else {
                imageView.setImageDrawable(context.getDrawable(default_dot_img));
            }
            imageView.setLayoutParams(ll);
            linearLayout.addView(imageView);
        }
    }

    public void setCurrent(int current){
        int count=this.getChildCount();
        for(int i=0;i<count;i++){
            ImageView imageView=(ImageView)linearLayout.getChildAt(i);
            if(i==current){
                imageView.setImageDrawable(context.getDrawable(default_dot_selected_img));
            }else{
                imageView.setImageDrawable(context.getDrawable(default_dot_img));
            }
        }
    }
}
