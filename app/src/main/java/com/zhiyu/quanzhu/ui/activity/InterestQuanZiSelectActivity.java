package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 感兴趣的圈子选择
 */
public class InterestQuanZiSelectActivity extends BaseActivity implements View.OnClickListener{

    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout backLayout,rightLayout;
    private TextView titleTextView , rightTextView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<InterestQuanZiSelectActivity> activityWeakReference;

        public MyHandler(InterestQuanZiSelectActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            InterestQuanZiSelectActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_quanzi_select);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();

    }

    private void initPtr() {
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

    }

    private void initViews() {
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView=findViewById(R.id.titleTextView);
        titleTextView.setText("选择你感兴趣的圈子");
        rightLayout=findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView=findViewById(R.id.rightTextView);
        rightTextView.setText("跳过");
        rightTextView.setTextColor(getResources().getColor(R.color.text_color_yilingqu));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                finish();
                break;
        }
    }
}
