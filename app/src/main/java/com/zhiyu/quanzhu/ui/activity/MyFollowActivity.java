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
import com.zhiyu.quanzhu.ui.fragment.FragmentMyFollowMember;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyFollowShop;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注
 */
public class MyFollowActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private LinearLayout memberLayout,shopLayout;
    private TextView memberTextView,shopTextView;
    private View memberLine,shopLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);

        initViews();
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的关注");
        memberLayout=findViewById(R.id.memberLayout);
        memberLayout.setOnClickListener(this);
        memberTextView=findViewById(R.id.memberTextView);
        memberLine=findViewById(R.id.memberLine);
        shopLayout=findViewById(R.id.shopLayout);
        shopLayout.setOnClickListener(this);
        shopTextView=findViewById(R.id.shopTextView);
        shopLine=findViewById(R.id.shopLine);
        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentMyFollowMember());
        fragmentList.add(new FragmentMyFollowShop());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.memberLayout:
                changeTitle(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.shopLayout:
                changeTitle(1);
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    private void changeTitle(int index){
        memberTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        shopTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        memberLine.setVisibility(View.INVISIBLE);
        shopLine.setVisibility(View.INVISIBLE);
        switch (index){
            case 0:
                memberTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                memberLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                shopTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shopLine.setVisibility(View.VISIBLE);
                break;
        }
    }
}
