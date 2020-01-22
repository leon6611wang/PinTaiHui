package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.MyShangQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.MyShangQuanMenuDialog;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyShangQuanActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout backLayout;
    private LinearLayout rightLayout;
    private ImageView menuImageView;
    private MyShangQuanMenuDialog menuDialog;
    private int menuY, dp_15;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private MyShangQuanRecyclerAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        WeakReference<MyShangQuanActivity> activityWeakReference;

        public MyHandler(MyShangQuanActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyShangQuanActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shangquan);
        dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        initPtr();
        initViews();
        initDialogs();
    }

    private void initDialogs() {
        menuDialog = new MyShangQuanMenuDialog(this, R.style.dialog, new MyShangQuanMenuDialog.OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int position, String desc) {

            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        menuImageView = findViewById(R.id.menuImageView);
        menuImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        menuImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] locations = new int[2];
                        menuImageView.getLocationOnScreen(locations);
                        int menu_y = locations[1];
                        int menu_height = menuImageView.getHeight(); // 获取高度
                        menuY = menu_y - menu_height + 20;
                        return true;
                    }
                });

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new MyShangQuanRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_15);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
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
            case R.id.rightLayout:
                menuDialog.show();
                menuDialog.setY(menuY);
                break;
        }
    }
}
