package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.ui.activity.FullSearchActivity;
import com.zhiyu.quanzhu.ui.adapter.MyFragmentPagerAdapter;
import com.zhiyu.quanzhu.ui.adapter.MyFragmentStatePagerAdapter;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.dialog.PublishDialog;
import com.zhiyu.quanzhu.ui.widget.NoScrollHorizontallyViewPager;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 首页-圈子
 */
public class FragmentHomeQuanZi extends Fragment implements View.OnClickListener {
    private View view;
    private NoScrollViewPager viewPager;
    private MyFragmentStatePagerAdapter adapter;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private TextView guanzhutextview, tuijiantextview, souquantextview;
    private ImageView publishMenuImageView, searchImageView;
    private FrameLayout topBarLayout;
    private int topBarWidth, topBarHeight;
    private int bottomBarHeight;
    private int contentHeight;
    private PublishDialog publishDialog;
    private LinearLayout cityLayout;
    private TextView cityTextView;
    private ProvinceCityDialog cityDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_quanzi, container, false);
        Bundle bundle = getArguments();
        bottomBarHeight = bundle.getInt("bottomBarHeight");
//        System.out.println("FragmentHomeQuanZi bottomBarHeight: " + bottomBarHeight);
        topBarLayout = view.findViewById(R.id.topBarLayout);
        topBarLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        topBarLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        topBarWidth = topBarLayout.getWidth(); // 获取宽度
                        topBarHeight = topBarLayout.getHeight(); // 获取高度
                        initDatas();
                        initViews();
                        initDialogs();
//                        System.out.println("topBarWidth: " + topBarWidth + " , topBarHeight: " + topBarHeight);
                        return true;
                    }
                });

        return view;
    }

    private void initDialogs() {
        publishDialog = new PublishDialog(getContext(), R.style.dialog);
        cityDialog = new ProvinceCityDialog(getContext(), R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                cityTextView.setText(city.getName());
                fragmentQuanZiTuiJian.setCity(city.getName());
            }
        });
    }

    private FragmentQuanZiTuiJian fragmentQuanZiTuiJian;

    private void initDatas() {
        fragmentQuanZiTuiJian = new FragmentQuanZiTuiJian();
        Bundle bundle = new Bundle();
        int screenHeight = ScreentUtils.getInstance().getScreenHeight(getContext());
        contentHeight = screenHeight - topBarHeight - bottomBarHeight;
        bundle.putInt("contentHeight", contentHeight);
        fragmentQuanZiTuiJian.setArguments(bundle);
//        if (null == fragmentArrayList && fragmentArrayList.size() == 0) {
        fragmentArrayList.add(new FragmentQuanZiGuanZhu());
        fragmentArrayList.add(fragmentQuanZiTuiJian);
        fragmentArrayList.add(new FragmentQuanZiSouQuan());
//        }
    }

    private void initViews() {
        cityLayout = view.findViewById(R.id.cityLayout);
        cityLayout.setOnClickListener(this);
        cityTextView = view.findViewById(R.id.cityTextView);
        cityTextView.setText(SharedPreferencesUtils.getInstance(getContext()).getLocationCity());
        guanzhutextview = view.findViewById(R.id.guanzhutextview);
        tuijiantextview = view.findViewById(R.id.tuijiantextview);
        souquantextview = view.findViewById(R.id.souquantextview);
        publishMenuImageView = view.findViewById(R.id.publishMenuImageView);
        searchImageView = view.findViewById(R.id.searchImageView);
        searchImageView.setOnClickListener(this);
        guanzhutextview.setOnClickListener(this);
        tuijiantextview.setOnClickListener(this);
        souquantextview.setOnClickListener(this);
        publishMenuImageView.setOnClickListener(this);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setScroll(false);
        adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fragmentArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guanzhutextview:
                barChange(0);
                break;
            case R.id.tuijiantextview:
                barChange(1);
                break;
            case R.id.souquantextview:
                barChange(2);
                break;
            case R.id.publishMenuImageView:
                publishDialog.show();
                break;
            case R.id.searchImageView:
                Intent fullSearchIntent = new Intent(getActivity(), FullSearchActivity.class);
                fullSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(fullSearchIntent);
                break;
            case R.id.cityLayout:
                cityDialog.show();
                break;
        }
    }

    private void barChange(int position) {
        viewPager.setCurrentItem(position);
        guanzhutextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tuijiantextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        souquantextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        guanzhutextview.setTextSize(14);
        tuijiantextview.setTextSize(14);
        souquantextview.setTextSize(14);

        switch (position) {
            case 0:
                guanzhutextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                guanzhutextview.setTextSize(16);
                break;
            case 1:
                tuijiantextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tuijiantextview.setTextSize(16);
                break;
            case 2:
                souquantextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                souquantextview.setTextSize(16);
                break;
        }
    }
}
