package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
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
    private LinearLayout backLayout;
    private TextView titleTextView, cancelTextView;
    private EditText searchEditText;
    private FrameLayout rightLayout;
    private ImageView searchImageView;
    private boolean isSearchModel;//是否搜索模式
    private FragmentOrderAll orderAll;
    private FragmentOrderDaiFuKuan orderDaiFuKuan;
    private FragmentOrderDaiFaHuo orderDaiFaHuo;
    private FragmentOrderDaiShouHuo orderDaiShouHuo;
    private FragmentOrderDaiPingJia orderDaiPingJia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        position = getIntent().getIntExtra("position", 0);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
                    orderAll.searchOrder(search);
                    orderDaiFuKuan.searchOrder(search);
                    orderDaiFaHuo.searchOrder(search);
                    orderDaiFaHuo.searchOrder(search);
                    orderDaiShouHuo.searchOrder(search);
                    orderDaiPingJia.searchOrder(search);

                }
                return false;
            }
        });
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        searchImageView = findViewById(R.id.searchImageView);
        cancelTextView = findViewById(R.id.cancelTextView);
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
        orderAll = new FragmentOrderAll();
        orderDaiFuKuan = new FragmentOrderDaiFuKuan();
        orderDaiFaHuo = new FragmentOrderDaiFaHuo();
        orderDaiShouHuo = new FragmentOrderDaiShouHuo();
        orderDaiPingJia = new FragmentOrderDaiPingJia();
        fragmentList.add(orderAll);
        fragmentList.add(orderDaiFuKuan);
        fragmentList.add(orderDaiFaHuo);
        fragmentList.add(orderDaiShouHuo);
        fragmentList.add(orderDaiPingJia);
        mViewPager = findViewById(R.id.mViewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);
        barChange(position);
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
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                searchModelChange();
                break;
        }
    }

    //搜索模式切换
    private void searchModelChange() {
        isSearchModel = !isSearchModel;
        if (isSearchModel) {
            titleTextView.setVisibility(View.INVISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
            searchImageView.setVisibility(View.GONE);
            cancelTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.INVISIBLE);
            searchImageView.setVisibility(View.VISIBLE);
            cancelTextView.setVisibility(View.GONE);
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
