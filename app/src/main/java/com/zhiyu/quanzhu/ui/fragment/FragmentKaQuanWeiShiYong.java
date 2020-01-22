package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class FragmentKaQuanWeiShiYong extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout quanbulayout, shixiaolayout;
    private TextView quanbutextview, shixiaotextview;
    private View quanbuview, shixiaoview;
    private NoScrollViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kaquan_weishiyong, null);
        initViews();
        return view;
    }

    private void initViews() {
        quanbulayout = view.findViewById(R.id.quanbulayout);
        quanbulayout.setOnClickListener(this);
        quanbutextview = view.findViewById(R.id.quanbutextview);
        quanbuview = view.findViewById(R.id.quanbuview);
        shixiaolayout = view.findViewById(R.id.shixiaolayout);
        shixiaolayout.setOnClickListener(this);
        shixiaotextview = view.findViewById(R.id.shixiaotextview);
        shixiaoview = view.findViewById(R.id.shixiaoview);

        mViewPager = view.findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentKaQuanWeiShiYongQuanBu());
        fragmentList.add(new FragmentKaQuanWeiShiYongShiXiao());
        adapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanbulayout:
                barChange(0);
                break;
            case R.id.shixiaolayout:
                barChange(1);
                break;
        }
    }

    private void barChange(int position) {
        quanbutextview.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        shixiaotextview.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        quanbuview.setVisibility(View.INVISIBLE);
        shixiaoview.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                quanbutextview.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                quanbuview.setVisibility(View.VISIBLE);
                break;
            case 1:
                shixiaotextview.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                shixiaoview.setVisibility(View.VISIBLE);
                break;
        }
        mViewPager.setCurrentItem(position);
    }
}
