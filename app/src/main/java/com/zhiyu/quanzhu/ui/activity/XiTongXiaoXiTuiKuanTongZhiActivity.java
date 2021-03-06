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
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 系统消息-退款通知
 */
public class XiTongXiaoXiTuiKuanTongZhiActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private RecyclerView mRecyclerView;

    private static class MyHandler extends Handler {
        WeakReference<XiTongXiaoXiTuiKuanTongZhiActivity> activityWeakReference;

        public MyHandler(XiTongXiaoXiTuiKuanTongZhiActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            XiTongXiaoXiTuiKuanTongZhiActivity activity = activityWeakReference.get();
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
        setContentView(R.layout.activity_xitongxiaoxi_inner_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("退款通知");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration((int) getResources().getDimension(R.dimen.dp_10));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
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
                isRefresh=false;
                list();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page=1;
                isRefresh=true;
                list();
            }
        });
        ptrFrameLayout.autoRefresh();
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

    private int page=1;
    private boolean isRefresh=true;

    private void list(){
        RequestParams params= MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.XI_TONG_XIAO_XI_ZHI_FU_TONG_ZHI);
        params.addBodyParameter("page",String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("关注店铺"+result);
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();
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
