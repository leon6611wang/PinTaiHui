package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
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
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.ui.adapter.MyShangQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.MyShangQuanMenuDialog;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的圈子
 */
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
                    activity.adapter.setList(activity.list);
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
                switch (position) {
                    case 1:
                        Intent scanIntent = new Intent(MyShangQuanActivity.this, ScanActivity.class);
                        startActivity(scanIntent);
                        break;
                    case 2:
                        Intent createShangQuanIntent = new Intent(MyShangQuanActivity.this, CreateCircleActivity.class);
                        startActivity(createShangQuanIntent);
                        break;
                }
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
                isRefresh = false;

                circleList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                if (null != list) {
                    list.clear();
                }
                circleList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        ptrFrameLayout.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        if (null != list) {
            list.clear();
        }
        circleList();
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

    private CircleResult circleResult;
    private List<Circle> list;
    private boolean isRefresh = true;

    private void circleList() {
//        System.out.println("uid: " + SharedPreferencesUtils.getInstance(this).getUserId());
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_LIST);
//        params.addBodyParameter("uid", SharedPreferencesUtils.getInstance(this).getUserId());
        params.addBodyParameter("uid","82");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
                if (isRefresh) {
                    list = circleResult.getData().getList();
                } else {
                    list.addAll(circleResult.getData().getList());
                }

                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("circle list: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
