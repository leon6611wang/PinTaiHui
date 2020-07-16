package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentLargeImage;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class LargeImageList2Activity extends BaseActivity {
    private int position = 0;
    private ArrayList<String> imgList;
    private TextView indicatorTextView;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_list_2);
        position = getIntent().getIntExtra("position", 0);
        imgList = getIntent().getStringArrayListExtra("imgList");
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }

    private void initViews() {
        indicatorTextView = findViewById(R.id.indicatorTextView);
        indicatorTextView.setText((position + 1) + "/" + imgList.size());
        mViewPager = findViewById(R.id.mViewPager);
        for (int i = 0; i < imgList.size(); i++) {
            FragmentLargeImage fragment = new FragmentLargeImage();
            Bundle bundle = new Bundle();
            bundle.putString("url", imgList.get(i));
            fragment.setArguments(bundle);
            list.add(fragment);
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorTextView.setText((position + 1) + "/" + imgList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
