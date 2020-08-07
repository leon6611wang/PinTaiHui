package com.leon.shehuibang.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leon.shehuibang.R;

public class TabHomeBottom extends LinearLayout implements View.OnClickListener {
    private LinearLayout layout0, layout1, layout2, layout3,layout_search;
    private ImageView image0, image1, image2, image3,image_search;
    private View view;
    private ViewPager viewPager;

    public TabHomeBottom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.tab_home_bottom, null);
        int height=(int) context.getResources().getDimension(R.dimen.home_bottom_tab_height);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,height);
        view.setLayoutParams(params);
        this.addView(view);
        initViews();
    }

    private void initViews() {
        layout0 = view.findViewById(R.id.layout0);
        layout0.setOnClickListener(this);
        layout1 = view.findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
        layout2 = view.findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        layout3 = view.findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
        layout_search=view.findViewById(R.id.layout_search);
        layout_search.setOnClickListener(this);

        image0 = view.findViewById(R.id.image0);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image_search=view.findViewById(R.id.image_search);

    }

    public void setViewPager(ViewPager vp) {
        this.viewPager = vp;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                change(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout0:
                change(0);
                if (null != viewPager) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.layout1:
                change(1);
                if (null != viewPager) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.layout_search:
                change(2);
                if (null != viewPager) {
                    viewPager.setCurrentItem(2);
                }
                break;
            case R.id.layout2:
                change(3);
                if (null != viewPager) {
                    viewPager.setCurrentItem(3);
                }
                break;
            case R.id.layout3:
                change(4);
                if (null != viewPager) {
                    viewPager.setCurrentItem(4);
                }
                break;
        }
    }

    private void change(int index) {
        image0.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.comments_black));
        image1.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.garden_black));
        image_search.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.search_black));
        image2.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.message_black));
        image3.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.mine_black));
        switch (index) {
            case 0:
                image0.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.comments_blue));
                break;
            case 1:
                image1.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.garden_blue));
                break;
            case 2:
                image_search.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.search_blue));
                break;
            case 3:
                image2.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.message_blue));
                break;
            case 4:
                image3.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.mine_blue));
                break;
        }
    }
}
