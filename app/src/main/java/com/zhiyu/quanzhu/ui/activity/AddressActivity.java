package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.Address;
import com.zhiyu.quanzhu.model.result.AddressResult;
import com.zhiyu.quanzhu.ui.adapter.AddressRecyclerAdapter;
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

/**
 * 管理收货地址
 */
public class AddressActivity extends BaseActivity implements View.OnClickListener, AddressRecyclerAdapter.OnSelectAddressListener {
    private LinearLayout backLayout, rightLayout;
    private RecyclerView mRecyclerView;
    private AddressRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private int isSelectAddress = 0;//1.选择地址

    private static class MyHandler extends Handler {
        WeakReference<AddressActivity> shouHuoDiZhiGuanLiActivityWeakReference;

        public MyHandler(AddressActivity activity) {
            shouHuoDiZhiGuanLiActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddressActivity activity = shouHuoDiZhiGuanLiActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show("数据错误");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        isSelectAddress = getIntent().getIntExtra("isSelectAddress", 0);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);

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
                addressList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                addressList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new AddressRecyclerAdapter(this);
        adapter.setOnSelectAddressListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        isRefresh = true;
        addressList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent addeditIntent = new Intent(this, AddEditAddressActivity.class);
                startActivity(addeditIntent);
                break;
        }
    }

    @Override
    public void onSelectAddress(Address address) {
        if (isSelectAddress == 1) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("address", address);
            intent.putExtras(bundle);
            setResult(10036, intent);
            finish();
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private AddressResult addressResult;
    private List<Address> list;

    private void addressList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADDRESS_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                addressResult = GsonUtils.GsonToBean(result, AddressResult.class);
                if (isRefresh) {
                    list = addressResult.getData().getAddress();
                } else {
                    list.addAll(addressResult.getData().getAddress());
                }
                System.out.println("address list: " + list.size());
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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
