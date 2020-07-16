package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.leon.chic.dao.PageDao;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.model.result.QuanZiGuanZhuResult;
import com.zhiyu.quanzhu.ui.adapter.MultiFeedsAdapter;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MultiFeedsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MultiFeedsAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MultiFeedsActivity> weakReference;

        public MyHandler(MultiFeedsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MultiFeedsActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.feedList);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_feeds);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MultiFeedsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
                page++;
                feedsList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                feedsList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }


    private int page = 1;
    private boolean isRefresh = true;
    private FeedResult feedResult;
    private List<Feed> feedList;

    private void feedsList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_GUANZHU_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                feedResult = GsonUtils.GsonToBean(result, FeedResult.class);
                if (isRefresh) {
                    feedList = feedResult.getData().getMycircles();
                } else {
                    feedList.addAll(feedResult.getData().getMycircles());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
