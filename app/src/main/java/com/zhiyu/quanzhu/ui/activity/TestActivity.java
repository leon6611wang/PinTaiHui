package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianAdapter2;
import com.zhiyu.quanzhu.utils.GalleryLayoutManager;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;

public class TestActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private QuanZiTuiJianAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new QuanZiTuiJianAdapter2();
//        LinearLayoutManager ms = new LinearLayoutManager(this);
//        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRecyclerView.setLayoutManager(ms);
        final GalleryLayoutManager gm = new GalleryLayoutManager();
        mRecyclerView.setLayoutManager(gm);

        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter.setDatas(list);
//        adapter.setHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,null));
        adapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.item_foot, null));
        mRecyclerView.setAdapter(adapter);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);

    }


}
