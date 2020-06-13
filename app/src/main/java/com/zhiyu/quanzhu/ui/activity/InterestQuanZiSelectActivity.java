package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
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
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.InterestCircle;
import com.zhiyu.quanzhu.model.result.InterestCircleResult;
import com.zhiyu.quanzhu.ui.adapter.InterestQuanZiRecyclerAdapter;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 感兴趣的圈子选择
 */
public class InterestQuanZiSelectActivity extends BaseActivity implements View.OnClickListener {
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView, enterHomeTextView;
    private InterestQuanZiRecyclerAdapter adapter;
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
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    Intent intent1 = new Intent(activity, HomeActivity.class);
                    activity.startActivity(intent1);
                    activity.finish();
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
                page++;
                isRefresh = false;
                circleList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                circleList();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("选择你感兴趣的圈子");
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText("跳过");
        enterHomeTextView = findViewById(R.id.enterHomeTextView);
        enterHomeTextView.setOnClickListener(this);
        rightTextView.setTextColor(getResources().getColor(R.color.text_color_yilingqu));
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new InterestQuanZiRecyclerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                Intent intent1 = new Intent(this, HomeActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.rightLayout:
                Intent intent2 = new Intent(this, HomeActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.enterHomeTextView:
                likeCircle();
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private InterestCircleResult interestCircleResult;
    private List<InterestCircle> list;

    private void circleList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHANG_QUAN_TUI_JIAN_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("推荐的圈子: " + result);
                interestCircleResult = GsonUtils.GsonToBean(result, InterestCircleResult.class);
                if (isRefresh) {
                    list = interestCircleResult.getData().getCirclelist();
                } else {
                    list.addAll(interestCircleResult.getData().getCirclelist());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("推荐的圈子: " + ex.toString());
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

    private void likeCircle() {
        List<Integer> ids = new ArrayList<>();
        for (InterestCircle circle : adapter.getList()) {
            if (circle.isSelect()) {
                ids.add(circle.getId());
            }
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.LIKE_CIRCLE);
        params.addBodyParameter("circles", GsonUtils.GsonString(ids));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("like circle: " + result);
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
}
