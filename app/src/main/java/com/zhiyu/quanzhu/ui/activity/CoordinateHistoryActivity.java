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
import com.zhiyu.quanzhu.model.bean.CoordinateHistory;
import com.zhiyu.quanzhu.model.result.CoordinateHistoryResult;
import com.zhiyu.quanzhu.ui.adapter.CoordinateHistoryAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 客服介入历史
 * 1.订单信息
 * 2.商家态度(同意，拒绝)
 * 3.用户取消退款
 * 4.用户上传凭证，客服介入
 * 5.客服裁决结果
 * 6.商品已寄回
 * 7.商家退款完成
 * 8.用户取消客服介入
 * 9.介入完成
 */
public class CoordinateHistoryActivity extends BaseActivity implements View.OnClickListener {
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private CoordinateHistoryAdapter adapter;
    private TextView tousuTextView, titleTextView;
    private LinearLayout backLayout;
    private int oid, itemid;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CoordinateHistoryActivity> activityWeakReference;

        public MyHandler(CoordinateHistoryActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CoordinateHistoryActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    activity.ptrFrameLayout.refreshComplete();
                    break;
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
        setContentView(R.layout.activity_coordinate_hisotry);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        oid = getIntent().getIntExtra("oid", 0);
        itemid = getIntent().getIntExtra("itemid", 0);
//        System.out.println("oid: " + oid + " , itemid: " + itemid);
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
                page++;
                isRefresh = false;
                coordinateHistory();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                coordinateHistory();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("协商历史");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CoordinateHistoryAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        tousuTextView = findViewById(R.id.tousuTextView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.tousuTextView:
                MessageToast.getInstance(this).show("拨打电话");
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh;
    private CoordinateHistoryResult historyResult;
    private List<CoordinateHistory> list;

    private void coordinateHistory() {
        Message message = myHandler.obtainMessage(1);
        message.sendToTarget();
//        MessageToast.getInstance(this).show(isRefresh ? "刷新成功" : "加载更多成功");
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COORDINATE_HISTORY);
        params.addBodyParameter("oid", String.valueOf(oid));
        params.addBodyParameter("itemid", String.valueOf(itemid));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("协商历史: " + result);
                historyResult = GsonUtils.GsonToBean(result, CoordinateHistoryResult.class);
                if (isRefresh) {
                    list = historyResult.getData().getList();
                } else {
                    list.addAll(historyResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("协商历史: " + ex.toString());
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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
