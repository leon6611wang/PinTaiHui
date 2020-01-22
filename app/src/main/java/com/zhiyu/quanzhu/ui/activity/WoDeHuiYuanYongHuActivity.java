package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentJiFenDuiHaoLi;
import com.zhiyu.quanzhu.ui.fragment.FragmentJiFenJiLu;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的会员-用户
 */
public class WoDeHuiYuanYongHuActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TextView jifenduihaoliTextView, jifenjiluTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodehuiyuan_yonghu);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentJiFenDuiHaoLi());
        fragmentList.add(new FragmentJiFenJiLu());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        jifenduihaoliTextView = findViewById(R.id.jifenduihaoliTextView);
        jifenduihaoliTextView.setOnClickListener(this);
        jifenjiluTextView = findViewById(R.id.jifenjiluTextView);
        jifenjiluTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent jiluIntent=new Intent(this,HuiYuanDuiHuanJiLuActivity.class);
                startActivity(jiluIntent);
                break;
            case R.id.jifenduihaoliTextView:
                barChange(0);
                break;
            case R.id.jifenjiluTextView:
                barChange(1);
                break;
        }
    }

    private void barChange(int position) {
        jifenduihaoliTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_gray_bg));
        jifenjiluTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_gray_bg));
        jifenduihaoliTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        jifenjiluTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        switch (position) {
            case 0:
                jifenduihaoliTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_grown_bg));
                jifenduihaoliTextView.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                jifenjiluTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_grown_bg));
                jifenjiluTextView.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        mViewPager.setCurrentItem(position);
    }
}
