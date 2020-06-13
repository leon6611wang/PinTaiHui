package com.zhiyu.quanzhu.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.GuideFragment;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private View indicator1, indicator2, indicator3, indicator4;
    private ImageView tiaoguoImageView;
    private TextView okTextView;
    private LinearLayout indicatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        SPUtils.getInstance().saveGuide(this);
        initDatas();
        initViews();
    }

    private void initDatas() {
        GuideFragment guideFragment1 = new GuideFragment();
        Bundle b1 = new Bundle();
        b1.putInt("index", 1);
        guideFragment1.setArguments(b1);
        fragmentList.add(guideFragment1);

        GuideFragment guideFragment2 = new GuideFragment();
        Bundle b2 = new Bundle();
        b2.putInt("index", 2);
        guideFragment2.setArguments(b2);
        fragmentList.add(guideFragment2);

        GuideFragment guideFragment3 = new GuideFragment();
        Bundle b3 = new Bundle();
        b3.putInt("index", 3);
        guideFragment3.setArguments(b3);
        fragmentList.add(guideFragment3);

        GuideFragment guideFragment4 = new GuideFragment();
        Bundle b4 = new Bundle();
        b4.putInt("index", 4);
        guideFragment4.setArguments(b4);
        fragmentList.add(guideFragment4);
    }

    private void initViews() {
        tiaoguoImageView = findViewById(R.id.tiaoguoImageView);
        tiaoguoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mViewPager = findViewById(R.id.mViewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    okTextView.setVisibility(View.VISIBLE);
                    indicatorLayout.setVisibility(View.GONE);
                } else if (position < 3) {
                    okTextView.setVisibility(View.GONE);
                    indicatorLayout.setVisibility(View.VISIBLE);
                }
                indicatorChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicatorLayout = findViewById(R.id.indicatorLayout);
        indicator1 = findViewById(R.id.indicator1);
        indicator2 = findViewById(R.id.indicator2);
        indicator3 = findViewById(R.id.indicator3);
        indicator4 = findViewById(R.id.indicator4);
        okTextView = findViewById(R.id.okTextView);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initIndicatorLayout();
    }

    private LinearLayout.LayoutParams layoutParamsCurrent, layoutParamsNormal;

    private void initIndicatorLayout() {
        int dp_18, dp_10;
        dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
        dp_18 = (int) getResources().getDimension(R.dimen.dp_18);
        layoutParamsCurrent = new LinearLayout.LayoutParams(dp_18, dp_10);
        layoutParamsNormal = new LinearLayout.LayoutParams(dp_10, dp_10);
    }

    private void indicatorChange(int position) {
        indicator1.setLayoutParams(layoutParamsNormal);
        indicator1.setBackground(getResources().getDrawable(R.drawable.shape_yuandian_ededed));
        indicator2.setLayoutParams(layoutParamsNormal);
        indicator2.setBackground(getResources().getDrawable(R.drawable.shape_yuandian_ededed));
        indicator3.setLayoutParams(layoutParamsNormal);
        indicator3.setBackground(getResources().getDrawable(R.drawable.shape_yuandian_ededed));
        indicator4.setLayoutParams(layoutParamsNormal);
        indicator4.setBackground(getResources().getDrawable(R.drawable.shape_yuandian_ededed));
        switch (position) {
            case 0:
                indicator1.setLayoutParams(layoutParamsCurrent);
                indicator1.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 1:
                indicator2.setLayoutParams(layoutParamsCurrent);
                indicator2.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 2:
                indicator3.setLayoutParams(layoutParamsCurrent);
                indicator3.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 3:
                indicator4.setLayoutParams(layoutParamsCurrent);
                indicator4.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
        }

    }


}
