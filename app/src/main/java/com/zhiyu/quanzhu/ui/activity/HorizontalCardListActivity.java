package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianResult;
import com.zhiyu.quanzhu.ui.adapter.HorizontalCardListAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * 横向滚动卡片列表
 */
public class HorizontalCardListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private HorizontalCardListAdapter adapter;
    private List<QuanZiTuiJian> list;
    private QuanZiTuiJianResult result;
    private boolean isRefresh = true;
    private int card_width, card_height = 1600;
    private int currentPosition = -1;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<HorizontalCardListActivity> activityWeakReference;

        public MyHandler(HorizontalCardListActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HorizontalCardListActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    System.out.println("myhandler : " + (null == activity.list ? 0 : activity.list.size()));
                    activity.adapter.setData(activity.list);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_card_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDatas();
        initViews();
        requestTuiJianList();
    }

    private void initDatas() {
        int dp_20 = (int) getResources().getDimension(R.dimen.dp_20);
        card_height = card_height - dp_20;
        float width = (9f / 16f) * card_height;
        card_width = Math.round(width);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new HorizontalCardListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter.setWidthHeight(card_width, card_height);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        mRecyclerView.setAdapter(scaleInAnimationAdapter);
    }

    private int page = 1;

    private void requestTuiJianList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_LIST);
        params.addBodyParameter("city_name", "马鞍山市");
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                System.out.println(res);
                result = GsonUtils.GsonToBean(res, QuanZiTuiJianResult.class);
                list = result.getData().getList();
                System.out.println("size:" + (null == result.getData().getList() ? 0 : result.getData().getList().size()));
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Message message1 = myHandler.obtainMessage(1);
//                message1.sendToTarget();
//                Message message2 = myHandler.obtainMessage(2);
//                message2.sendToTarget();
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
