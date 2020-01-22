package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeQuanShang;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeXiaoXi;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeRenMai;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeTuiJian;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeWoDe;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity2 extends BaseActivity implements ViewPager.OnPageChangeListener{
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Log.i("HomeActivity2","onCreate");
        initDatas();
        initViews();
    }

    private void initDatas(){
        fragmentList=new ArrayList<>();
        FragmentHomeTuiJian fragmentHomeTuiJian=new FragmentHomeTuiJian();
        FragmentHomeRenMai fragmentHomeRenMai =new FragmentHomeRenMai();
        FragmentHomeXiaoXi fragmentHomeXiaoXi =new FragmentHomeXiaoXi();
        FragmentHomeQuanShang fragmentHomeQuanShang =new FragmentHomeQuanShang();
        FragmentHomeWoDe fragmentHomeWoDe=new FragmentHomeWoDe();
        fragmentList.add(fragmentHomeTuiJian);
        fragmentList.add(fragmentHomeRenMai);
        fragmentList.add(fragmentHomeXiaoXi);
        fragmentList.add(fragmentHomeQuanShang);
        fragmentList.add(fragmentHomeWoDe);
    }

    private void initViews(){
        Log.i("HomeActivity2","initviews");
        System.out.println("activity count: "+(null==fragmentList?0:fragmentList.size()));
        viewPager=findViewById(R.id.viewPager);
        Log.i("HomeActivity2","activity count: "+(null==fragmentList?0:fragmentList.size()));
        adapter=new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
