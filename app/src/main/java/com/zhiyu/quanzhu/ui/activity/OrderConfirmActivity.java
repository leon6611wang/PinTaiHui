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

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.Address;
import com.zhiyu.quanzhu.model.bean.CartShop;
import com.zhiyu.quanzhu.model.bean.OrderConfirmShop;
import com.zhiyu.quanzhu.model.result.OrderConfirmResult;
import com.zhiyu.quanzhu.ui.adapter.GouWuCheJieSuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.OrderConfirmRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单确认
 */
public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener, OrderConfirmRecyclerAdapter.OnRemarkEditListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private RecyclerView mRecyclerView;
    //    private GouWuCheJieSuanRecyclerAdapter adapter;
    private OrderConfirmRecyclerAdapter adapter;
    private View orderConfirmHeaderView;
    private TextView zhifuTextView, changeAddressTextView, userNameTextView, phonenumTextView, addressTextView;
    private PayWayDialog payWayDialog;
    private String ids;
    private LoadingDialog loadingDialog;
    private String address_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<OrderConfirmActivity> activityWeakReference;

        public MyHandler(OrderConfirmActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OrderConfirmActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.loadingDialog.dismiss();
                    if (activity.orderConfirmResult.getCode() == 200) {
                        activity.address_id = activity.orderConfirmResult.getData().getAddress().get_id();
                        activity.userNameTextView.setText("收货人：" + activity.orderConfirmResult.getData().getAddress().getName());
                        activity.phonenumTextView.setText("电话：" + activity.orderConfirmResult.getData().getAddress().getPhone());
                        activity.addressTextView.setText("地址：" + activity.orderConfirmResult.getData().getAddress().getProvince_name() + " " + activity.orderConfirmResult.getData().getAddress().getCity_name() + " " + activity.orderConfirmResult.getData().getAddress().getAddress());
                        activity.adapter.addDatas(activity.orderConfirmResult.getData().getList());
                    }
                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        ids = getIntent().getStringExtra("ids");
        initViews();
        initDialogs();
        cartSettlement(ids);
    }

    private void initDialogs() {
        payWayDialog = new PayWayDialog(this, R.style.dialog, new PayWayDialog.OnPayWayListener() {
            @Override
            public void onPayWay(int payWay, String payWayStr) {

            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("订单确认");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        orderConfirmHeaderView = LayoutInflater.from(this).inflate(R.layout.header_order_cofirm_recyclerview, null);
        changeAddressTextView = orderConfirmHeaderView.findViewById(R.id.changeAddressTextView);
        changeAddressTextView.setOnClickListener(this);
        userNameTextView = orderConfirmHeaderView.findViewById(R.id.userNameTextView);
        phonenumTextView = orderConfirmHeaderView.findViewById(R.id.phonenumTextView);
        addressTextView = orderConfirmHeaderView.findViewById(R.id.addressTextView);
        adapter = new OrderConfirmRecyclerAdapter(this);
        adapter.setHeaderView(orderConfirmHeaderView);
        adapter.setOnRemarkEditListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        zhifuTextView = findViewById(R.id.zhifuTextView);
        zhifuTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhifuTextView:
//                payWayDialog.show();
                cartOrderAdd();
                break;
            case R.id.changeAddressTextView:
                Intent addressIntent = new Intent(this, AddressActivity.class);
                addressIntent.putExtra("isSelectAddress", 1);
                startActivityForResult(addressIntent, 10036);
                break;
        }
    }

    @Override
    public void onRemarkEdit(int position, String remark) {
        orderConfirmResult.getData().getList().get(position).setRemark(remark);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 10036) {
            Bundle bundle = intent.getExtras();
            Address address = (Address) bundle.get("address");
            address_id = address.get_id();
            userNameTextView.setText("收货人：" + address.getName());
            phonenumTextView.setText("电话：" + address.getPhone());
            addressTextView.setText("地址：" + address.getProvince_name() + " " + address.getCity_name() + " " + address.getAddress());
        }
    }


    private OrderConfirmResult orderConfirmResult;

    /**
     * 购物车结算
     */
    private void cartSettlement(String idsList) {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_SETTLEMENT);
        params.addBodyParameter("ids", idsList);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("cartSettlement: " + result);
                orderConfirmResult = GsonUtils.GsonToBean(result, OrderConfirmResult.class);
                System.out.println("cartSettlement: " + orderConfirmResult.getData().getList().get(0).getList().size());
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("goodsSettlement: " + ex.toString());
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
     * 购物车-下单支付
     */
    private void cartOrderAdd() {
        Map<Integer, String> map = new HashMap<>();
        for (OrderConfirmShop shop : orderConfirmResult.getData().getList()) {
            if (!StringUtils.isNullOrEmpty(shop.getRemark())) {
                map.put(shop.getShop_id(), shop.getRemark());
            }
        }
        String remark = null;
        if (map.size() > 0) {
            remark = GsonUtils.GsonString(map);
        }
        System.out.println("购物车结算params --> ids:" + ids + ",address_id:" + address_id + ",remark:" + remark);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_ORDER_ADD);
        params.addBodyParameter("ids", ids);//数组
        params.addBodyParameter("spm", "");
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("remark", remark);//键值对map
        params.addBodyParameter("type", "1");//购物车结算1，其他2
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("购物车结算: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("购物车结算: " + ex.toString());
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
