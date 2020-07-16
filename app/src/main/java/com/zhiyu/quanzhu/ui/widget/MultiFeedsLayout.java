package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.bean.FeedImage;

import java.util.List;

public class MultiFeedsLayout extends LinearLayout {
    private List<FeedImage> list;
    private int imageCount;

    public MultiFeedsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<FeedImage> imageList) {
        this.list = imageList;
        if (null != list && list.size() > 0) {
            imageCount = list.size();
        }
    }

    private void initViews() {
        switch (imageCount) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
        }
    }


}
