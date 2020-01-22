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
import com.zhiyu.quanzhu.ui.fragment.FragmentOrderAll;
import com.zhiyu.quanzhu.ui.fragment.FragmentOrderDaiFaHuo;
import com.zhiyu.quanzhu.ui.fragment.FragmentOrderDaiFuKuan;
import com.zhiyu.quanzhu.ui.fragment.FragmentOrderDaiPingJia;
import com.zhiyu.quanzhu.ui.fragment.FragmentOrderDaiShouHuo;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int position;
    private LinearLayout alllayout, daifukuanlayout, daifahuolayout, daishouhuolayout, daipingjialayout;
    private TextView alltextview, daifukuantextview, daishouhuotextview, daifahuotextview, daipingjiatextview;
    private View allview, daifukuanview, daifahuoview, daishouhuoview, daipingjiaview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        position = (Integer) getIntent().getExtras().get("position");
        initViews();
    }

    private void initViews() {
        alllayout = findViewById(R.id.alllayout);
        daifukuanlayout = findViewById(R.id.daifukuanlayout);
        daifahuolayout = findViewById(R.id.daifahuolayout);
        daishouhuolayout = findViewById(R.id.daishouhuolayout);
        daipingjialayout = findViewById(R.id.daipingjialayout);
        alllayout.setOnClickListener(this);
        daifukuanlayout.setOnClickListener(this);
        daifahuolayout.setOnClickListener(this);
        daishouhuolayout.setOnClickListener(this);
        daipingjialayout.setOnClickListener(this);
        alltextview = findViewById(R.id.alltextview);
        daifukuantextview = findViewById(R.id.daifukuantextview);
        daifahuotextview = findViewById(R.id.daifahuotextview);
        daishouhuotextview = findViewById(R.id.daishouhuotextview);
        daipingjiatextview = findViewById(R.id.daipingjiatextview);
        allview = findViewById(R.id.allview);
        daifukuanview = findViewById(R.id.daifukuanview);
        daifahuoview = findViewById(R.id.daifahuoview);
        daishouhuoview = findViewById(R.id.daishouhuoview);
        daipingjiaview = findViewById(R.id.daipingjiaview);
        fragmentList.add(new FragmentOrderAll());
        fragmentList.add(new FragmentOrderDaiFuKuan());
        fragmentList.add(new FragmentOrderDaiFaHuo());
        fragmentList.add(new FragmentOrderDaiShouHuo());
        fragmentList.add(new FragmentOrderDaiPingJia());
        mViewPager = findViewById(R.id.mViewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alllayout:
                barChange(0);
                break;
            case R.id.daifukuanlayout:
                barChange(1);
                break;
            case R.id.daifahuolayout:
                barChange(2);
                break;
            case R.id.daishouhuolayout:
                barChange(3);
                break;
            case R.id.daipingjialayout:
                barChange(4);
                break;

        }
    }

    private void barChange(int position) {
        alltextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        daifukuantextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        daifahuotextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        daishouhuotextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        daipingjiatextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        allview.setVisibility(View.INVISIBLE);
        daifukuanview.setVisibility(View.INVISIBLE);
        daifahuoview.setVisibility(View.INVISIBLE);
        daishouhuoview.setVisibility(View.INVISIBLE);
        daipingjiaview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                alltextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                allview.setVisibility(View.VISIBLE);
                break;
            case 1:
                daifukuantextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                daifukuanview.setVisibility(View.VISIBLE);
                break;
            case 2:
                daifahuotextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                daifahuoview.setVisibility(View.VISIBLE);
                break;
            case 3:
                daishouhuotextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                daishouhuoview.setVisibility(View.VISIBLE);
                break;
            case 4:
                daipingjiatextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                daipingjiaview.setVisibility(View.VISIBLE);
                break;
        }
        mViewPager.setCurrentItem(position);
    }
}
