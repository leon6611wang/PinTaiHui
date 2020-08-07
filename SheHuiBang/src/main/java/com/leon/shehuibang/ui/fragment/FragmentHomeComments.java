package com.leon.shehuibang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.shehuibang.R;
import com.leon.shehuibang.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeComments extends Fragment implements View.OnClickListener {
    private View view;
    private TextView titleTextView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentHomeCommentsComments commentsComments;
    private FragmentHomeCommentsShop commentsShop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_comments, null);
        initViews();
        return view;
    }

    private void initViews() {
        titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setOnClickListener(this);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("左邻右舍"), true);
        tabLayout.addTab(tabLayout.newTab().setText("铺子"), false);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager = view.findViewById(R.id.viewPager);
        commentsComments = new FragmentHomeCommentsComments();
        fragmentList.add(commentsComments);
        commentsShop = new FragmentHomeCommentsShop();
        fragmentList.add(commentsShop);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleTextView:
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        commentsComments.scrollToTop();
                        break;
                }
                break;
        }
    }
}
