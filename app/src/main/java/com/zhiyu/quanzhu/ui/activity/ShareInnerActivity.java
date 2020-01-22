package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerQuanLiao;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerQuanYou;
import com.zhiyu.quanzhu.ui.fragment.FragmentShareInnerZuiJin;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ShareInnerActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TextView titleTextView;
    private LinearLayout headerlayout,rightLayout, zuijinlayout, quanyoulayout, quanliaolayout, titlelayout, searchInnerLayout;
    private TextView zuijintextview, quanyoutextview, quanliaotextview;
    private View zuijinlineview, quanyoulineview, quanliaolineview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_inner);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        headerlayout=findViewById(R.id.headerlayout);
        titlelayout = findViewById(R.id.titlelayout);
        titleTextView = findViewById(R.id.titleTextView);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        zuijinlayout = findViewById(R.id.zuijinlayout);
        zuijinlayout.setOnClickListener(this);
        quanyoulayout = findViewById(R.id.quanyoulayout);
        quanyoulayout.setOnClickListener(this);
        quanliaolayout = findViewById(R.id.quanliaolayout);
        quanliaolayout.setOnClickListener(this);
        zuijintextview = findViewById(R.id.zuijintextview);
        quanyoutextview = findViewById(R.id.quanyoutextview);
        quanliaotextview = findViewById(R.id.quanliaotextview);
        zuijinlineview = findViewById(R.id.zuijinlineview);
        quanyoulineview = findViewById(R.id.quanyoulineview);
        quanliaolineview = findViewById(R.id.quanliaolineview);

        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentShareInnerZuiJin());
        fragmentList.add(new FragmentShareInnerQuanYou());
        fragmentList.add(new FragmentShareInnerQuanLiao());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zuijinlayout:
                barChange(0);
                break;
            case R.id.quanyoulayout:
                barChange(1);
                break;
            case R.id.quanliaolayout:
                barChange(2);
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
        zuijintextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        quanyoutextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        quanliaotextview.setTextColor(getResources().getColor(R.color.text_color_shareinner_gray));
        zuijinlineview.setVisibility(View.INVISIBLE);
        quanyoulineview.setVisibility(View.INVISIBLE);
        quanliaolineview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                zuijintextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zuijinlineview.setVisibility(View.VISIBLE);
                break;
            case 1:
                quanyoutextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanyoulineview.setVisibility(View.VISIBLE);
                break;
            case 2:
                quanliaotextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanliaolineview.setVisibility(View.VISIBLE);
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_silent, R.anim.dialog_dimiss);
    }
}
