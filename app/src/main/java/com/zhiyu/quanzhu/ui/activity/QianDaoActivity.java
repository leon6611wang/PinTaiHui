package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.QianDao;
import com.zhiyu.quanzhu.ui.adapter.QianDaoRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.QianDaoSuccessDialog;

import java.util.ArrayList;

public class QianDaoActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private QianDaoRecyclerAdapter adapter;
    private ArrayList<QianDao> list = new ArrayList<>();
    private View headerView;
    private View titleView,headerTitleView;
    private LinearLayout backLayout,headerBackLayout,rightLayout,headerRightLayout;
    private QianDaoSuccessDialog qianDaoSuccessDialog;
    private TextView qiandaoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiandao);
        initData();
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        qianDaoSuccessDialog=new QianDaoSuccessDialog(this,R.style.dialog);
    }

    private void initData() {
        list.add(new QianDao("每日签到", 10));
        list.add(new QianDao("点赞文章", 20));
        list.add(new QianDao("点赞动态", 20));
        list.add(new QianDao("评论动态", 20));
        list.add(new QianDao("评论文章", 20));
        list.add(new QianDao("收藏文章", 20));
        list.add(new QianDao("每日签到", 10));
        list.add(new QianDao("点赞文章", 20));
        list.add(new QianDao("点赞动态", 20));
        list.add(new QianDao("评论动态", 20));
        list.add(new QianDao("评论文章", 20));
        list.add(new QianDao("收藏文章", 20));
        list.add(new QianDao("每日签到", 10));
        list.add(new QianDao("点赞文章", 20));
        list.add(new QianDao("点赞动态", 20));
        list.add(new QianDao("评论动态", 20));
        list.add(new QianDao("评论文章", 20));
        list.add(new QianDao("收藏文章", 20));
    }

    private int totalDy = 0;
    private void initViews() {
        titleView=findViewById(R.id.titleView);
        titleView.setBackground(getResources().getDrawable(R.color.grown));
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightLayout=findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guizeIntent=new Intent(QianDaoActivity.this,H5PageActivity.class);
                startActivity(guizeIntent);
            }
        });
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new QianDaoRecyclerAdapter();
        adapter.addDatas(list);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_qiandao_recyclerview, null);
        qiandaoTextView=headerView.findViewById(R.id.qiandaoTextView);
        qiandaoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qianDaoSuccessDialog.show();
                qianDaoSuccessDialog.setDays(3);
            }
        });
        headerTitleView=headerView.findViewById(R.id.headerTitleView);
        headerBackLayout=headerView.findViewById(R.id.backLayout);
        headerBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRightLayout=headerView.findViewById(R.id.rightLayout);
        headerRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guizeIntent=new Intent(QianDaoActivity.this,H5PageActivity.class);
                startActivity(guizeIntent);
            }
        });
        adapter.setHeaderView(headerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
                changeTitleView();
                Log.i("qiandaorecycler","totalDy: "+totalDy);
            }
        });
    }

    private void changeTitleView(){
        if(Math.abs(totalDy)>0){
            titleView.setVisibility(View.VISIBLE);
            headerTitleView.setVisibility(View.INVISIBLE);
            float alpha=(float) Math.abs(totalDy)/(float) 300;
//            if(alpha<0.4f){
//                alpha=0.4f;
//            }
            titleView.setAlpha(alpha);
        }else{
            titleView.setVisibility(View.GONE);
            headerTitleView.setVisibility(View.VISIBLE);
            titleView.setAlpha(0);
        }
    }
}
