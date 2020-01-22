package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ShopProfileRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;

/**
 * 聊天-店铺资料
 */
public class ShopProfileActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLaout;
    private RecyclerView mRecyclerView;
    private ShopProfileRecyclerAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();
    private View headerView;
    private int totalDy;
    private LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initData();
        initViews();
    }

    private void initData() {
        list.add("https://c-ssl.duitang.com/uploads/item/201812/28/20181228142701_AjmR5.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201903/06/20190306190318_LeVFA.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201308/01/20130801210238_Fhhau.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201504/06/20150406H2612_iVhWv.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201704/21/20170421134815_GnvHa.jpeg");
        list.add("http://c-ssl.duitang.com/uploads/item/201812/16/20181216090355_8jvuX.jpeg");
    }

    private void initViews() {
        headerLayout = findViewById(R.id.headerLayout);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_shop_profile_recyclerview, null);
        backLaout = findViewById(R.id.backLayout);
        backLaout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ShopProfileRecyclerAdapter(this);
        adapter.addDatas(list);
        adapter.setHeaderView(headerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                headerLayoutChange();
            }
        });

    }

    private void headerLayoutChange() {
        if (Math.abs(totalDy) > 0) {
            float alpha = (float) Math.abs(totalDy) / (float) 100;
            if (alpha > 1.0f) {
                alpha = 1.0f;
            }
            headerLayout.getBackground().mutate().setAlpha((int) (alpha * 255));
        } else if (totalDy == 0) {
            headerLayout.getBackground().mutate().setAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
        }
    }
}
