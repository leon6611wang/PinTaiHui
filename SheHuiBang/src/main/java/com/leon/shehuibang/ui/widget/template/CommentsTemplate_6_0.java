package com.leon.shehuibang.ui.widget.template;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.leon.shehuibang.R;

import java.util.List;

public class CommentsTemplate_6_0 extends LinearLayout {
    private View view;
    private ImageView imageView0, imageView1, imageView2, imageView3, imageView4, imageView5;

    public CommentsTemplate_6_0(Context context) {
        super(context);
        view = inflate(context, R.layout.template_comments_6_0, this);
        imageView0 = view.findViewById(R.id.imageView0);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageView5 = view.findViewById(R.id.imageView5);
    }

    public void setImageUrl(List<String> urlList) {
        if (null != urlList && urlList.size() == 6) {
            Glide.with(getContext()).load(urlList.get(0)).error(R.drawable.image_bg).into(imageView0);
            Glide.with(getContext()).load(urlList.get(1)).error(R.drawable.image_bg).into(imageView1);
            Glide.with(getContext()).load(urlList.get(2)).error(R.drawable.image_bg).into(imageView2);
            Glide.with(getContext()).load(urlList.get(3)).error(R.drawable.image_bg).into(imageView3);
            Glide.with(getContext()).load(urlList.get(4)).error(R.drawable.image_bg).into(imageView4);
            Glide.with(getContext()).load(urlList.get(5)).error(R.drawable.image_bg).into(imageView5);
        }

    }

}
