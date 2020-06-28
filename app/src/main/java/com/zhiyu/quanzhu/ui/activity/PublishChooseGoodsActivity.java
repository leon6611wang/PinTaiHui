package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentPublishChooseGoodsSearch;
import com.zhiyu.quanzhu.ui.fragment.FragmentPublishChooseGoodsSelected;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 发布文章/视频 商品选择
 */
public class PublishChooseGoodsActivity extends BaseActivity implements View.OnClickListener, PublishChooseGoodsRelationActivity.OnRelationGoodsListener {
    private LinearLayout backLayout, searchLayout, selectedLayout;
    private TextView searchTextView, selectedTextView;
    private View searchLine, selectedLine;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FragmentPublishChooseGoodsSearch searchFragment;
    private FragmentPublishChooseGoodsSelected selectedFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int feeds_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_choose_goods);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        PublishChooseGoodsRelationActivity.setOnRelationGoodsListener(this);
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
//        System.out.println("chooseGoodsAty feeds_id: "+feeds_id);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        searchLayout = findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(this);
        selectedLayout = findViewById(R.id.selectedLayout);
        selectedLayout.setOnClickListener(this);
        searchLine = findViewById(R.id.searchLine);
        selectedLine = findViewById(R.id.selectedLine);
        searchTextView = findViewById(R.id.searchTextView);
        selectedTextView = findViewById(R.id.selectedTextView);

        viewPager = findViewById(R.id.viewPager);
        searchFragment = new FragmentPublishChooseGoodsSearch();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("feeds_id", feeds_id);
        searchFragment.setArguments(bundle1);
        selectedFragment = new FragmentPublishChooseGoodsSelected();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("feeds_id", feeds_id);
        selectedFragment.setArguments(bundle2);
        fragmentList.add(searchFragment);
        fragmentList.add(selectedFragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            case R.id.searchLayout:
                changeTitle(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.selectedLayout:
                changeTitle(1);
                viewPager.setCurrentItem(1);
                break;

        }
    }

    private void changeTitle(int index) {
        searchTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        selectedTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        searchLine.setVisibility(View.INVISIBLE);
        selectedLine.setVisibility(View.INVISIBLE);
        switch (index) {
            case 0:
                searchTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                searchLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                selectedTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                selectedLine.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private Set<Integer> idSet = new HashSet<>();

    @Override
    public void onRelationGoods(Set<Integer> set) {
        idSet.addAll(set);
    }


    @Override
    public void finish() {
        setResultIntent();
//        if (null != onChooseGoodsListener) {
//            onChooseGoodsListener.onChooseGoods(idSet);
//        }
        super.finish();
    }

    private void setResultIntent() {
        int c1 = searchFragment.getSelectedGoodsCount();
        int c2 = selectedFragment.getSelectedGoodsCount();
        System.out.println("c1: " + c1 + " , c2: " + c2);
        int count = (c1 > c2) ? c1 : c2;
        Intent intent = new Intent();
        intent.putExtra("count", count);
        setResult(1033, intent);
    }

    public static void setOnChooseGoodsListener(OnChooseGoodsListener listener) {
        onChooseGoodsListener = listener;
    }

    private static OnChooseGoodsListener onChooseGoodsListener;

    public interface OnChooseGoodsListener {
        void onChooseGoods(Set<Integer> idSet);
    }
}
