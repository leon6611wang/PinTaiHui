package com.leon.shehuibang.ui.widget.template;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.leon.shehuibang.R;

import java.util.List;

public class CommentsTemplate_2_0 extends LinearLayout {
    private View view;
    private ImageView imageView0, imageView1;

    public CommentsTemplate_2_0(Context context) {
        super(context);
        view = inflate(context, R.layout.template_comments_2_0, this);
        imageView0 = view.findViewById(R.id.imageView0);
        imageView1 = view.findViewById(R.id.imageView1);
    }

    public void setImageUrl(List<String> urlList) {
        if (null != urlList && urlList.size() == 2) {
            Glide.with(getContext()).load(urlList.get(0)).error(R.drawable.image_bg).into(imageView0);
            Glide.with(getContext()).load(urlList.get(1)).error(R.drawable.image_bg).into(imageView1);
        }

    }


}
