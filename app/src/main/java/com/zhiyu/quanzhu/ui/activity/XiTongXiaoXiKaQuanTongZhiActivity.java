package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.XiTongXiaoXiKaQuanTongZhiRecyclerAdapter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 系统消息-卡券通知
 */
public class XiTongXiaoXiKaQuanTongZhiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private XiTongXiaoXiKaQuanTongZhiRecyclerAdapter adapter;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<XiTongXiaoXiKaQuanTongZhiActivity> activityWeakReference;
        public MyHandler(XiTongXiaoXiKaQuanTongZhiActivity activity){
            activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            XiTongXiaoXiKaQuanTongZhiActivity activity=activityWeakReference.get();
            switch (msg.what){
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xitongxiaoxi_inner_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("卡券通知");
        mRecyclerView=findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new XiTongXiaoXiKaQuanTongZhiRecyclerAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initPtr(){
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
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
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
