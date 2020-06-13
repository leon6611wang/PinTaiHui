package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.ui.adapter.DraftsAdapter;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class DraftsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout, bottomLayout, allSelectLayout;
    private TextView titleTextView, rightTextView, deleteTextView;
    private ImageView allSelectImageView;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private DraftsAdapter adapter;
    private YNDialog ynDialog;
    private String ids = "";

    private boolean isDeleteStatus;//删除状态
    private boolean isAllSelect;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<DraftsActivity> activityWeakReference;

        public MyHandler(DraftsActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DraftsActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.feedList);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.adapter.onDeleteSuccess();

                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initPtr();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setVideoStop(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.setVideoStop(true);
    }

    private void initDialogs() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                deleteDrafts();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("草稿箱");
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText("管理");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DraftsAdapter(this, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        bottomLayout = findViewById(R.id.bottomLayout);
        allSelectLayout = findViewById(R.id.allSelectLayout);
        allSelectLayout.setOnClickListener(this);
        allSelectImageView = findViewById(R.id.allSelectImageView);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
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
                drafts();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                drafts();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private int page = 1;
    private boolean isRefresh = true;
    private FeedResult feedResult;
    private List<Feed> feedList;

    private void drafts() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_DRAFT_LIST);
        params.addBodyParameter("type", "2");
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("我的草稿箱: " + result);
                feedResult = GsonUtils.GsonToBean(result, FeedResult.class);
                if (isRefresh) {
                    feedList = feedResult.getData().getList();
                } else {
                    feedList.addAll(feedResult.getData().getList());
                }
                System.out.println("我的草稿箱: " + feedList.size());
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("我的草稿箱: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                isDeleteStatus = !isDeleteStatus;
                if (isDeleteStatus) {
                    rightTextView.setText("取消");
                    bottomLayout.setVisibility(View.VISIBLE);
                    if (null != feedList && feedList.size() > 0) {
                        for (Feed feed : feedList) {
                            feed.setSelected(false);
                        }
                    }
                    adapter.setList(feedList);
                    allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.draft_unselect));
                } else {
                    rightTextView.setText("管理");
                    bottomLayout.setVisibility(View.GONE);
                }
                adapter.setDeleteStatus(isDeleteStatus);
                break;
            case R.id.allSelectLayout:
                isAllSelect = !isAllSelect;
                if (null != feedList && feedList.size() > 0) {
                    for (Feed feed : feedList) {
                        feed.setSelected(isAllSelect);
                    }
                }
                adapter.setList(feedList);
                if (isAllSelect) {
                    allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.draft_selected));
                } else {
                    allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.draft_unselect));
                }

                break;
            case R.id.deleteTextView:
                List<Feed> list = adapter.getList();
                if (null != list && list.size() > 0) {
                    for (Feed feed : list) {
                        if (feed.isSelected())
                            ids += feed.getContent().getId() + ",";
                    }
                }
                ynDialog.show();
                ynDialog.setTitle("确定删除草稿？");
                break;
        }
    }

    private BaseResult baseResult;

    private void deleteDrafts() {
        System.out.println("ids: " + ids);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_DRAFT);
        params.addBodyParameter("ids", ids);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.setQQShareResult(requestCode,resultCode,data);
    }
}
