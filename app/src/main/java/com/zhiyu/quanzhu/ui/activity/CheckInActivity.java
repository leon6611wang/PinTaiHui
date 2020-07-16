package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CheckIn;
import com.zhiyu.quanzhu.model.result.CheckInResult;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.adapter.CheckInRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CheckInSuccessDialog;
import com.zhiyu.quanzhu.ui.dialog.RegTokenDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ShareUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 签到
 */
public class CheckInActivity extends BaseActivity implements View.OnClickListener, CheckInRecyclerAdapter.OnCheckInShareListener {
    private RecyclerView mRecyclerView;
    private CheckInRecyclerAdapter adapter;
    private ArrayList<CheckIn> list = new ArrayList<>();
    private View headerView;
    private View titleView, headerTitleView;
    private LinearLayout backLayout, headerBackLayout, rightLayout, headerRightLayout;
    private RegTokenDialog regTokenDialog;
    private CheckInSuccessDialog checkInSuccessDialog;
    private ShareDialog shareDialog;
    private TextView qiandaoTextView, daysTextView;
    private String regToken, shareText, avatar;
    private int uid;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CheckInActivity> activityWeakReference;

        public MyHandler(CheckInActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CheckInActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    activity.daysTextView.setText(String.valueOf(activity.checkInResult.getData().getDays()));
                    if (activity.checkInResult.getData().isIs_sgin()) {
                        activity.qiandaoTextView.setText("已签到");
                    }
                    activity.adapter.clearDatas();
                    activity.adapter.addDatas(activity.checkInResult.getData().getList());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.qiandaoTextView.setText("已签到");
                        activity.daysTextView.setText(String.valueOf(activity.checkInResult.getData().getDays() + 1));
                        activity.checkInSuccessDialog.show();
                        activity.checkInSuccessDialog.setDays(activity.checkInResult.getData().getWeekdays() + 1);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        regToken = getIntent().getStringExtra("regToken");
        shareText = getIntent().getStringExtra("shareText");
        avatar = getIntent().getStringExtra("avatar");
        uid = getIntent().getIntExtra("uid", 0);
        initViews();
        initDialogs();
        appShareConfig();
        inviteShareConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInInformation();
    }

    private void initDialogs() {
        checkInSuccessDialog = new CheckInSuccessDialog(this, R.style.dialog);
        regTokenDialog = new RegTokenDialog(this, R.style.dialog, new RegTokenDialog.OnRegTokenShareListener() {
            @Override
            public void onRegToakenShare() {
                shareDialog.show();
                inviteShareResult.getData().getShare().setImage_url(avatar);
                shareDialog.setShare(inviteShareResult.getData().getShare(), uid);
                shareDialog.hideInnerShare();
            }
        });
        shareDialog = new ShareDialog(this, this, R.style.dialog);
    }


    private int totalDy = 0;

    private void initViews() {
        titleView = findViewById(R.id.titleView);
        titleView.setBackground(getResources().getDrawable(R.color.grown));
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CheckInRecyclerAdapter(this);
        adapter.setOnCheckInShareListener(this);
        adapter.addDatas(list);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_qiandao_recyclerview, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(layoutParams);
        qiandaoTextView = headerView.findViewById(R.id.qiandaoTextView);
        qiandaoTextView.setOnClickListener(this);
        daysTextView = headerView.findViewById(R.id.daysTextView);
        headerTitleView = headerView.findViewById(R.id.headerTitleView);
        headerBackLayout = headerView.findViewById(R.id.backLayout);
        headerBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRightLayout = headerView.findViewById(R.id.rightLayout);
        headerRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guizeIntent = new Intent(CheckInActivity.this, H5PageActivity.class);
                guizeIntent.putExtra("url", checkInResult.getData().getUrl());
                startActivity(guizeIntent);
            }
        });
        adapter.setHeaderView(headerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                changeTitleView();
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
                Intent guizeIntent = new Intent(CheckInActivity.this, H5PageActivity.class);
                guizeIntent.putExtra("url", checkInResult.getData().getUrl());
                startActivity(guizeIntent);
                break;
            case R.id.qiandaoTextView:
                if (!checkInResult.getData().isIs_sgin()) {
                    checkIn();
                }
                break;
        }
    }

    private void changeTitleView() {
        if (Math.abs(totalDy) > 0) {
            titleView.setVisibility(View.VISIBLE);
            headerTitleView.setVisibility(View.INVISIBLE);
            float alpha = (float) Math.abs(totalDy) / (float) 300;
//            if(alpha<0.4f){
//                alpha=0.4f;
//            }
            titleView.setAlpha(alpha);
        } else {
            titleView.setVisibility(View.GONE);
            headerTitleView.setVisibility(View.VISIBLE);
            titleView.setAlpha(0);
        }
    }

    private CheckInResult checkInResult;

    /**
     * 签到详情
     */
    private void checkInInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CHECK_IN_INFORMATION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("签到详情: " + result);
                checkInResult = GsonUtils.GsonToBean(result, CheckInResult.class);
                Message message = myHandler.obtainMessage(1);
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

    private BaseResult baseResult;

    private void checkIn() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CHECK_IN);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("签到: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    @Override
    public void onCheckInShare(int type, String type_desc) {
        switch (type) {
            case 1:
                regTokenDialog.show();
                regTokenDialog.setRegTokenData(regToken, shareText, null);
                break;
            case 2:
                shareDialog.show();
                appShareResult.getData().getShare().setImage_url(avatar);
                shareDialog.setShare(appShareResult.getData().getShare(), uid);
                shareDialog.hideInnerShare();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }

    private ShareResult appShareResult;

    private void appShareConfig() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_INVITE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                appShareResult = GsonUtils.GsonToBean(result, ShareResult.class);
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

    private ShareResult inviteShareResult;

    private void inviteShareConfig() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_INVITE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                inviteShareResult = GsonUtils.GsonToBean(result, ShareResult.class);
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
