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
import com.zhiyu.quanzhu.ui.fragment.FragmentDianZan;
import com.zhiyu.quanzhu.ui.fragment.FragmentFangKe;
import com.zhiyu.quanzhu.ui.fragment.FragmentLiShi;
import com.zhiyu.quanzhu.ui.fragment.FragmentPingLun;

import java.util.ArrayList;
import java.util.List;

public class ZuJiActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int position;
    private LinearLayout fangkelayout, pinglunlayout, dianzanlayout, lishilayout;
    private TextView fangketextview, pingluntextview, dianzantextview, lishitextview;
    private View fangkeview, pinglunview, dianzanview, lishiview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zuji);
        position = getIntent().getExtras().getInt("position");
        initViews();
    }

    private void initViews() {
        fangkelayout = findViewById(R.id.fangkelayout);
        fangkelayout.setOnClickListener(this);
        pinglunlayout = findViewById(R.id.pinglunlayout);
        pinglunlayout.setOnClickListener(this);
        dianzanlayout = findViewById(R.id.dianzanlayout);
        dianzanlayout.setOnClickListener(this);
        lishilayout = findViewById(R.id.lishilayout);
        lishilayout.setOnClickListener(this);
        fangketextview = findViewById(R.id.fangketextview);
        pingluntextview = findViewById(R.id.pingluntextview);
        dianzantextview = findViewById(R.id.dianzantextview);
        lishitextview = findViewById(R.id.lishitextview);
        fangkeview = findViewById(R.id.fangkeview);
        pinglunview = findViewById(R.id.pinglunview);
        dianzanview = findViewById(R.id.dianzanview);
        lishiview = findViewById(R.id.lishiview);

        fragmentList.add(new FragmentFangKe());
        fragmentList.add(new FragmentPingLun());
        fragmentList.add(new FragmentDianZan());
        fragmentList.add(new FragmentLiShi());
        mViewPager = findViewById(R.id.mViewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        barChange(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fangkelayout:
                barChange(0);
                break;
            case R.id.pinglunlayout:
                barChange(1);
                break;
            case R.id.dianzanlayout:
                barChange(2);
                break;
            case R.id.lishilayout:
                barChange(3);
                break;
        }
    }

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

    private void barChange(int position) {
        fangketextview.setTextColor(getResources().getColor(R.color.black));
        pingluntextview.setTextColor(getResources().getColor(R.color.black));
        dianzantextview.setTextColor(getResources().getColor(R.color.black));
        lishitextview.setTextColor(getResources().getColor(R.color.black));
        fangkeview.setVisibility(View.INVISIBLE);
        pinglunview.setVisibility(View.INVISIBLE);
        dianzanview.setVisibility(View.INVISIBLE);
        lishiview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                fangketextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                fangkeview.setVisibility(View.VISIBLE);
                break;
            case 1:
                pingluntextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                pinglunview.setVisibility(View.VISIBLE);
                break;
            case 2:
                dianzantextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                dianzanview.setVisibility(View.VISIBLE);
                break;
            case 3:
                lishitextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                lishiview.setVisibility(View.VISIBLE);

                break;
        }
        mViewPager.setCurrentItem(position);
    }
}
