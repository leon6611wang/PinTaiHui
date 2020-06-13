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
import com.zhiyu.quanzhu.model.result.GoodsFenLeiResult;
import com.zhiyu.quanzhu.ui.adapter.QuanShangFenLeiRecyclerViewLeftAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanShangFenLeiRecyclerViewRightAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 商城-商品分类
 */
public class MallGoodsTypeActivity extends BaseActivity implements View.OnClickListener,QuanShangFenLeiRecyclerViewLeftAdapter.OnLeftClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private RecyclerView leftRecyclerView, rightRecyclerView;
    private QuanShangFenLeiRecyclerViewLeftAdapter leftAdapter;
    private QuanShangFenLeiRecyclerViewRightAdapter rightAdapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MallGoodsTypeActivity> activityWeakReference;

        public MyHandler(MallGoodsTypeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MallGoodsTypeActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.leftAdapter.setData(activity.fenLeiResult.getData().getList());
                    break;
                case 2:
                    activity.rightAdapter.setData(activity.childFenLeiResult.getData().getList());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_goods_type);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        mallGoodsFenLei();
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("商品分类");
        leftRecyclerView = findViewById(R.id.leftRecyclerView);
        rightRecyclerView = findViewById(R.id.rightRecyclerView);
        leftAdapter = new QuanShangFenLeiRecyclerViewLeftAdapter(this);
        leftAdapter.setOnLeftClickListener(this);
        rightAdapter = new QuanShangFenLeiRecyclerViewRightAdapter(this);
        LinearLayoutManager leftManager = new LinearLayoutManager(this);
        LinearLayoutManager rightManager = new LinearLayoutManager(this);
        leftManager.setOrientation(LinearLayoutManager.VERTICAL);
        rightManager.setOrientation(LinearLayoutManager.VERTICAL);
        leftRecyclerView.setLayoutManager(leftManager);
        rightRecyclerView.setLayoutManager(rightManager);
        leftRecyclerView.setAdapter(leftAdapter);
        rightRecyclerView.setAdapter(rightAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
        }
    }

    @Override
    public void onLeftClick(int position) {
        mallGoodsChildFenLei(fenLeiResult.getData().getList().get(position).getId());
    }

    private GoodsFenLeiResult fenLeiResult;
    private GoodsFenLeiResult childFenLeiResult;

    /**
     * 商城商品分类
     */
    private void mallGoodsFenLei() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MALL_GOODS_FENLEI);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                fenLeiResult = GsonUtils.GsonToBean(result, GoodsFenLeiResult.class);
                fenLeiResult.getData().getList().get(0).setSelected(true);
                mallGoodsChildFenLei(fenLeiResult.getData().getList().get(0).getId());
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

    /**
     * 子类
     */
    private void mallGoodsChildFenLei(long id) {
        RequestParams params =MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MALL_GOODS_CHILD_FENLEI);
        params.addBodyParameter("id", String.valueOf(id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                childFenLeiResult = GsonUtils.GsonToBean(result, GoodsFenLeiResult.class);
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
