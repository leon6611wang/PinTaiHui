package com.leon.shehuibang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.leon.shehuibang.R;
import com.leon.shehuibang.base.BaseActivity;
import com.leon.shehuibang.ui.adapter.ViewPagerAdapter;
import com.leon.shehuibang.ui.fragment.FragmentHomeComments;
import com.leon.shehuibang.ui.fragment.FragmentHomeGarden;
import com.leon.shehuibang.ui.fragment.FragmentHomeMessage;
import com.leon.shehuibang.ui.fragment.FragmentHomeMine;
import com.leon.shehuibang.ui.fragment.FragmentHomeSearch;
import com.leon.shehuibang.ui.widget.TabHomeBottom;
import com.leon.shehuibang.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabHomeBottom tabHomeBottom;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private CardView publishCardView;
    public static HomeActivity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity=this;
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        fragmentList.add(new FragmentHomeComments());
        fragmentList.add(new FragmentHomeGarden());
        fragmentList.add(new FragmentHomeSearch());
        fragmentList.add(new FragmentHomeMessage());
        fragmentList.add(new FragmentHomeMine());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabHomeBottom = findViewById(R.id.tabHomeBottom);
        tabHomeBottom.setViewPager(viewPager);
        publishCardView = findViewById(R.id.publishCardView);
        publishCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publishCardView:
                Intent intent = new Intent(this, PublishCommetsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
