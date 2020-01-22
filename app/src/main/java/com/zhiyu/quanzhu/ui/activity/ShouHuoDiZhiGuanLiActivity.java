package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.ShouHuoDiZhiRecyclerAdapter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 管理收货地址
 */
public class ShouHuoDiZhiGuanLiActivity extends BaseActivity implements View.OnClickListener, ShouHuoDiZhiRecyclerAdapter.OnDiZhiDeleteListener {
    private LinearLayout backLayout, rightLayout;
    private RecyclerView mRecyclerView;
    private ShouHuoDiZhiRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShouHuoDiZhiGuanLiActivity> shouHuoDiZhiGuanLiActivityWeakReference;

        public MyHandler(ShouHuoDiZhiGuanLiActivity activity) {
            shouHuoDiZhiGuanLiActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShouHuoDiZhiGuanLiActivity activity = shouHuoDiZhiGuanLiActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouhuodizhiguanli);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);

        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ShouHuoDiZhiRecyclerAdapter(this);
        adapter.setOnDiZhiDeleteListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent addeditIntent = new Intent(this, AddEditShouHuoDiZhiActivity.class);
                startActivity(addeditIntent);
                break;
        }
    }

    @Override
    public void onDiZhiDelete(int position) {
        Log.i("dizhi", "position: " + position);
    }
}
