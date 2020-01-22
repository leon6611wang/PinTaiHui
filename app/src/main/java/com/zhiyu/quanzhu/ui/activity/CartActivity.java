package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.CartAvailableFragment;
import com.zhiyu.quanzhu.ui.fragment.CartInvalidFragment;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */
public class CartActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout, availableLayout, invalidLayout;
    private TextView invalidTextView, availabelTextView, rightTextView;
    private View availableLineView, invalidLineView;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        availableLayout = findViewById(R.id.availableLayout);
        availableLayout.setOnClickListener(this);
        invalidLayout = findViewById(R.id.invalidLayout);
        invalidLayout.setOnClickListener(this);
        invalidTextView = findViewById(R.id.invalidTextView);
        availabelTextView = findViewById(R.id.availabelTextView);
        availableLineView = findViewById(R.id.availableLineView);
        invalidLineView = findViewById(R.id.invalidLineView);
        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new CartAvailableFragment());
        fragmentList.add(new CartInvalidFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                barChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:

                break;
            case R.id.availableLayout:
                barChange(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.invalidLayout:
                barChange(1);
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    private void barChange(int position) {
        availabelTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        availableLineView.setBackgroundColor(getResources().getColor(R.color.white));
        invalidTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        invalidLineView.setBackgroundColor(getResources().getColor(R.color.white));
        switch (position) {
            case 0:
                availabelTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                availableLineView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
                break;
            case 1:
                invalidTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                invalidLineView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
                break;
        }
    }



}
