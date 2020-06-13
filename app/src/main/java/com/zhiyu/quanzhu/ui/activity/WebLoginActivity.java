package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.WebLoginShop;
import com.zhiyu.quanzhu.model.result.WebLoginShopResult;
import com.zhiyu.quanzhu.ui.adapter.WebLoginShopAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class WebLoginActivity extends BaseActivity implements View.OnClickListener, WebLoginShopAdapter.OnSelectShopListener {
    private LinearLayout backLayout;
    private RecyclerView mRecyclerView;
    private WebLoginShopAdapter adapter;
    private TextView confirmTextView, cancelTextView;
    private String url;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<WebLoginActivity> activityWeakReference;

        public MyHandler(WebLoginActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WebLoginActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.adapter.setList(activity.shopResult.getData().getList());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
                case 3:
                    MessageToast.getInstance(activity).show("无法访问主机");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        url = getIntent().getStringExtra("url");
        System.out.println("url: "+url);
        initViews();
        myShopList();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        adapter = new WebLoginShopAdapter(this);
        adapter.setOnSelectShopListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.confirmTextView:
                if (null != webLoginShop) {
                    webLogin();
                } else {
                    MessageToast.getInstance(this).show("请选择店铺");
                }
                break;
            case R.id.cancelTextView:
                finish();
                break;
        }
    }

    private WebLoginShop webLoginShop;

    @Override
    public void onSelectShop(WebLoginShop shop) {
        this.webLoginShop = shop;
        confirmTextView.setBackground((null == webLoginShop) ? getResources().getDrawable(R.drawable.shape_web_login_btn_gray) :
                getResources().getDrawable(R.drawable.shape_web_login_btn_yellow));
    }

    private WebLoginShopResult shopResult;

    private void myShopList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.WEB_LOGIN_SHOP_LIST);
        params.addBodyParameter("uid", String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("我的店铺列表: " + result);
                shopResult = GsonUtils.GsonToBean(result, WebLoginShopResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("我的店铺列表：" + ex.toString());
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

    private void webLogin() {
        System.out.println("url: "+url+" , shop_id: "+webLoginShop.getId()+" , uid: "+SPUtils.getInstance().getUserId(BaseApplication.applicationContext)+" , type: "+webLoginShop.getType());
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("shop_id", String.valueOf(webLoginShop.getId()));
        params.addBodyParameter("uid", String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)));
        params.addBodyParameter("type", String.valueOf(webLoginShop.getType()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("web登录：" + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("web登录：" + ex.toString());
                Message message = myHandler.obtainMessage(3);
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
