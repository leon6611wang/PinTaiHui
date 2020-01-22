package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.GouWuCheJieSuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 购物车结算
 */
public class GouWuCheJieSuanActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private RecyclerView mRecyclerView;
    private GouWuCheJieSuanRecyclerAdapter adapter;
    private TextView zhifuTextView;
    private PayWayDialog payWayDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gouwuche_jiesuan);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        payWayDialog=new PayWayDialog(this, R.style.dialog, new PayWayDialog.OnPayWayListener() {
            @Override
            public void onPayWay(int payWay, String payWayStr) {

            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("购物车结算");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new GouWuCheJieSuanRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        zhifuTextView=findViewById(R.id.zhifuTextView);
        zhifuTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhifuTextView:
                payWayDialog.show();
                break;
        }
    }
}
