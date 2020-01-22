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
import com.zhiyu.quanzhu.ui.fragment.FragmentKaQuanShiYongJiLu;
import com.zhiyu.quanzhu.ui.fragment.FragmentKaQuanWeiShiYong;
import com.zhiyu.quanzhu.ui.fragment.FragmentKaQuanYiGuoQi;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的卡券
 */
public class WoDeKaQuanActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private NoScrollViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private LinearLayout weishiyonglayout, yiguoqilayout, shiyongjilulayout;
    private TextView weishiyongtextview, yiguoqitextview, shiyongjilutextview;
    private View weishiyongview, yiguoqiview, shiyongjiluview;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodekaquan);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的卡券");
        weishiyonglayout = findViewById(R.id.weishiyonglayout);
        weishiyonglayout.setOnClickListener(this);
        yiguoqilayout = findViewById(R.id.yiguoqilayout);
        yiguoqilayout.setOnClickListener(this);
        shiyongjilulayout = findViewById(R.id.shiyongjilulayout);
        shiyongjilulayout.setOnClickListener(this);
        weishiyongtextview = findViewById(R.id.weishiyongtextview);
        yiguoqitextview = findViewById(R.id.yiguoqitextview);
        shiyongjilutextview = findViewById(R.id.shiyongjilutextview);
        weishiyongview = findViewById(R.id.weishiyongview);
        yiguoqiview = findViewById(R.id.yiguoqiview);
        shiyongjiluview = findViewById(R.id.shiyongjiluview);

        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentKaQuanWeiShiYong());
        fragmentList.add(new FragmentKaQuanYiGuoQi());
        fragmentList.add(new FragmentKaQuanShiYongJiLu());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.weishiyonglayout:
                barChange(0);
                break;
            case R.id.yiguoqilayout:
                barChange(1);
                break;
            case R.id.shiyongjilulayout:
                barChange(2);
                break;
        }
    }

    private void barChange(int position) {
        weishiyongtextview.setTextColor(getResources().getColor(R.color.text_color_black));
        yiguoqitextview.setTextColor(getResources().getColor(R.color.text_color_black));
        shiyongjilutextview.setTextColor(getResources().getColor(R.color.text_color_black));
        weishiyongview.setVisibility(View.INVISIBLE);
        yiguoqiview.setVisibility(View.INVISIBLE);
        shiyongjiluview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                weishiyongtextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                weishiyongview.setVisibility(View.VISIBLE);
                break;
            case 1:
                yiguoqitextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                yiguoqiview.setVisibility(View.VISIBLE);
                break;
            case 2:
                shiyongjilutextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shiyongjiluview.setVisibility(View.VISIBLE);
                break;
        }
        mViewPager.setCurrentItem(position);
    }
}
