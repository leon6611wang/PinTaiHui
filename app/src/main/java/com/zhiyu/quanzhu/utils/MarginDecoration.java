package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

//    public MarginDecoration(Context context) {
//        margin = ScreentUtils.getInstance().dip2px(context, 10);
//    }

    public void setMargin(int m){
        margin=m;
    }

    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
//        if (parent.getChildLayoutPosition(view) % 2 == 0) {
//            outRect.set(margin, margin, 0, 0);
//        } else {
//            outRect.set(margin, margin, 0, 0);
//        }
        outRect.left = margin;
        outRect.right = 0;
        outRect.bottom = margin;
        if (parent.getChildPosition(view) == 0)
            outRect.top = margin;

    }
}
