package com.leon.shehuibang.ui.widget.template;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.leon.shehuibang.R;

import java.util.List;

public class CommentsTemplate_1_1 extends LinearLayout {
    private View view;
    private ImageView imageView0;

    public CommentsTemplate_1_1(Context context) {
        super(context);
        view = inflate(context, R.layout.template_comments_1_1, this);
        imageView0 = view.findViewById(R.id.imageView0);
    }

    public void setImageUrl(List<String> urlList) {
        if (null != urlList && urlList.size() == 1)
            Glide.with(getContext()).load(urlList.get(0)).error(R.drawable.image_bg).into(imageView0);
    }


}
