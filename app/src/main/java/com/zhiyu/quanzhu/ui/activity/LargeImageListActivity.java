package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.LargeImageListAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class LargeImageListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LargeImageListAdapter adapter;
    private int position = -1;
    private ArrayList<String> imgList;
    private TextView indicatorTextView;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_list);
        position = getIntent().getIntExtra("position", -1);
        imgList = getIntent().getStringArrayListExtra("imgList");
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        indicatorTextView = findViewById(R.id.indicatorTextView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
        adapter = new LargeImageListAdapter(this);
        adapter.setList(imgList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(lm);
        if (position > -1) {
            mRecyclerView.scrollToPosition(position);
            indicatorTextView.setText((position + 1) + "/" + imgList.size());
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {//停止滑动
                    currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                    indicatorTextView.setText((currentPosition + 1) + "/" + imgList.size());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
