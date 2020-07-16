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
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Address;
import com.zhiyu.quanzhu.model.bean.OrderConfirmShop;
import com.zhiyu.quanzhu.model.result.AlipayOrderInfo;
import com.zhiyu.quanzhu.model.result.OrderAddResult;
import com.zhiyu.quanzhu.model.result.OrderConfirmResult;
import com.zhiyu.quanzhu.model.result.WxpayOrderInfo;
import com.zhiyu.quanzhu.ui.adapter.OrderConfirmRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.OrderConfirmUseCouponDialog;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.AliPayUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.WxPayUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 订单确认
 */
public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener, OrderConfirmRecyclerAdapter.OnRemarkEditListener,
        AliPayUtils.OnAlipayCallbackListener, WXEntryActivity.OnWxpayCallbackListener, OrderConfirmRecyclerAdapter.OnUseCouponListener {
    private LinearLayout backLayout;
    private TextView titleTextView, countTextView, zhengshuTextView, xiaoshuTextView;
    private RecyclerView mRecyclerView;
    //    private GouWuCheJieSuanRecyclerAdapter adapter;
    private OrderConfirmRecyclerAdapter adapter;
    private LinearLayout addressLayout, addAddressLayout;
    private View orderConfirmHeaderView;
    private TextView zhifuTextView, changeAddressTextView, userNameTextView, phonenumTextView, addressTextView;
    private PayWayDialog payWayDialog;
    private PasswordCheckDialog passwordCheckDialog;
    private String ids;
    private String goods_id, goods_num;
    private String norms_id;
    private LoadingDialog loadingDialog;
    private String address_id, address_user_name, address_mobile, address_info;
    private int payType;
    private MyHandler myHandler = new MyHandler(this);
    private int balancePayType;
    private boolean canPayOrder;//是否可以支付(取决于订单中是否有物流不予配送情况)

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
                    activity.initOrderConfirmData();
                    break;
                case 2://下单成功
                    if (200 == activity.orderAddResult.getCode()) {
                        activity.payWayDialog.show();
                        activity.payWayDialog.setMoney(activity.orderAddResult.getData().getAll_price());
                    } else {
                        MessageToast.getInstance(activity).show(activity.orderAddResult.getMsg());
                    }
                    break;
                case 3:

                    break;
                case 11://微信支付订单详情回调
                    if (200 == activity.wxpayOrderInfo.getCode()) {
                        WxPayUtils.getInstance().wxPay(activity, activity.wxpayOrderInfo.getData());
                    } else {
                        MessageToast.getInstance(activity).show(activity.wxpayOrderInfo.getMsg());
                    }
                    break;
                case 12://支付宝支付订单详情回调
                    if (200 == activity.alipayOrderInfo.getCode()) {
                        AliPayUtils.getInstance(activity).aliPay(activity, activity.alipayOrderInfo.getData());
                    } else {
                        MessageToast.getInstance(activity).show(activity.alipayOrderInfo.getMsg());
                    }
                    break;
                case 13:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.pageChange();
                    }
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        AliPayUtils.getInstance(this).setOnAlipayCallbackListener(this);
        WXEntryActivity.setOnWxpayCallbackListener(this);
        ids = getIntent().getStringExtra("ids");
        goods_id = getIntent().getStringExtra("goods_id");
        norms_id = getIntent().getStringExtra("norms_id");
        goods_num = getIntent().getStringExtra("goods_num");
        initViews();
        initDialogs();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isNullOrEmpty(goods_id)) {
            payType = 2;
            goodsSettlement();
        } else if (!StringUtils.isNullOrEmpty(ids)) {
            payType = 1;
            cartSettlement();
        }
    }

    private void pageChange() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAlipayCallBack() {
        pageChange();
    }

    @Override
    public void onWxpayCallback() {
        pageChange();
    }

    private void initDialogs() {
        payWayDialog = new PayWayDialog(this, R.style.dialog, new PayWayDialog.OnPayWayListener() {
            @Override
            public void onPayWay(int payWay, String payWayStr) {
                switch (payWay) {
                    case 1://微信支付
                        wxpayRequest();
                        break;
                    case 2://支付宝支付
                        alipayRequest();
                        break;
                    case 3://微信余额支付
                        passwordCheckDialog.show();
                        passwordCheckDialog.setPasswordType(2);
                        balancePayType = 1;
                        break;
                    case 4://支付宝余额支付
                        passwordCheckDialog.show();
                        passwordCheckDialog.setPasswordType(2);
                        balancePayType = 2;
                        break;
                }
            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.dialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {
                if (payWayDialog.isShowing()) {
                    payWayDialog.dismiss();
                }
                balancePay(password);
            }
        });

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("订单确认");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        orderConfirmHeaderView = LayoutInflater.from(this).inflate(R.layout.header_order_cofirm_recyclerview, null);
        addressLayout = orderConfirmHeaderView.findViewById(R.id.addressLayout);
        addAddressLayout = orderConfirmHeaderView.findViewById(R.id.addAddressLayout);
        addAddressLayout.setOnClickListener(this);
        changeAddressTextView = orderConfirmHeaderView.findViewById(R.id.changeAddressTextView);
        changeAddressTextView.setOnClickListener(this);
        userNameTextView = orderConfirmHeaderView.findViewById(R.id.userNameTextView);
        phonenumTextView = orderConfirmHeaderView.findViewById(R.id.phonenumTextView);
        addressTextView = orderConfirmHeaderView.findViewById(R.id.addressTextView);
        adapter = new OrderConfirmRecyclerAdapter(this);
        adapter.setHeaderView(orderConfirmHeaderView);
        adapter.setOnRemarkEditListener(this);
        adapter.setOnUseCouponListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        zhifuTextView = findViewById(R.id.zhifuTextView);
        zhifuTextView.setOnClickListener(this);
        countTextView = findViewById(R.id.countTextView);
        zhengshuTextView = findViewById(R.id.zhengshuTextView);
        xiaoshuTextView = findViewById(R.id.xiaoshuTextView);

    }

    private void initOrderConfirmData() {
        if (null != orderConfirmResult.getData() && null != orderConfirmResult.getData().getAddress()) {
            addressLayout.setVisibility(View.VISIBLE);
            addAddressLayout.setVisibility(View.GONE);
            address_id = orderConfirmResult.getData().getAddress().get_id();
            address_user_name = orderConfirmResult.getData().getAddress().getName();
            address_mobile = orderConfirmResult.getData().getAddress().getPhone();
            address_info = orderConfirmResult.getData().getAddress().getProvince_name() + " " +
                    orderConfirmResult.getData().getAddress().getCity_name() + " " +
                    orderConfirmResult.getData().getAddress().getAddress();
            userNameTextView.setText("收货人：" + orderConfirmResult.getData().getAddress().getName());
            phonenumTextView.setText("电话：" + orderConfirmResult.getData().getAddress().getPhone());
            addressTextView.setText("地址：" + orderConfirmResult.getData().getAddress().getProvince_name() + " " +
                    orderConfirmResult.getData().getAddress().getCity_name() + " " +
                    orderConfirmResult.getData().getAddress().getAddress());
        } else {
            addressLayout.setVisibility(View.GONE);
            addAddressLayout.setVisibility(View.VISIBLE);
        }
        sum_price = orderConfirmResult.getData().getSum_price();
        countTextView.setText(String.valueOf(orderConfirmResult.getData().getCount()));
        zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(orderConfirmResult.getData().getSum_price()));
        xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(orderConfirmResult.getData().getSum_price()));
        adapter.clearDatas();
        adapter.addDatas(orderConfirmResult.getData().getList());
    }

    private long sum_price;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhifuTextView:
                boolean isCanPay = true;
                if (null != orderConfirmResult.getData().getList() && orderConfirmResult.getData().getList().size() > 0) {
                    for (OrderConfirmShop shop : orderConfirmResult.getData().getList()) {
                        if (shop.getPostage_status() == 2 || shop.getPostage_status() == 1) {
                            isCanPay = false;
                        }
                    }
                }
                if (!isCanPay) {
                    MessageToast.getInstance(this).show("超出范围，不予配送，无法下单!");
                    return;
                }

                if (null != orderAddResult && null != orderAddResult.getData() && !StringUtils.isNullOrEmpty(orderAddResult.getData().getOid())) {
                    Intent intent = new Intent(this, MyOrderActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    switch (payType) {
                        case 1:
                            cardOrderAdd();
                            break;
                        case 2:
                            orderAdd();
                            break;
                    }
                }
                break;
            case R.id.changeAddressTextView:
                Intent addressIntent = new Intent(this, AddressActivity.class);
                addressIntent.putExtra("isSelectAddress", 1);
                startActivityForResult(addressIntent, 10036);
                break;
            case R.id.addAddressLayout:
                Intent addaddressIntent = new Intent(this, AddressActivity.class);
                addaddressIntent.putExtra("isSelectAddress", 1);
                startActivityForResult(addaddressIntent, 10036);
                break;
        }
    }

    @Override
    public void onRemarkEdit(int position, String remark) {
        orderConfirmResult.getData().getList().get(position).setRemark(remark);
    }

    private Set<Integer> set = new HashSet<>();

    @Override
    public void onUseCoupon(int position, boolean isUse) {
        orderConfirmResult.getData().getList().get(position).setUseCoupon(isUse);
        sum_price = 0;
        for (int i = 0; i < orderConfirmResult.getData().getList().size(); i++) {
            if (orderConfirmResult.getData().getList().get(i).isUseCoupon()) {
                sum_price += orderConfirmResult.getData().getList().get(i).getPay_price();
            } else {
                sum_price += orderConfirmResult.getData().getList().get(i).getAll_price() + orderConfirmResult.getData().getList().get(i).getPostage_price();
            }
        }
        zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(sum_price));
        xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(sum_price));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 10036) {
            Bundle bundle = intent.getExtras();
            Address address = (Address) bundle.get("address");
            address_id = address.get_id();
            address_user_name = address.getName();
            address_mobile = address.getPhone();
            address_info = address.getAddress();
            userNameTextView.setText("收货人：" + address.getName());
            phonenumTextView.setText("电话：" + address.getPhone());
            addressTextView.setText("地址：" + address.getProvince_name() + " " + address.getCity_name() + " " + address.getAddress());
            switch (payType) {
                case 1:
                    cartSettlement();
                    break;
                case 2:
                    goodsSettlement();
                    break;
            }
        }
    }


    private OrderConfirmResult orderConfirmResult;

    /**
     * 购物车结算
     */
    private void cartSettlement() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_SETTLEMENT);
        params.addBodyParameter("ids", ids);
        params.addBodyParameter("address_id", address_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("购物车结算: " + result);
                orderConfirmResult = GsonUtils.GsonToBean(result, OrderConfirmResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("goodsSettlement: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private OrderAddResult orderAddResult;

    /**
     * 购物车-下单支付
     */
    private void cardOrderAdd() {
        Map<Integer, String> remarkMap = new HashMap<>();
        Map<Integer, Boolean> couponkMap = new HashMap<>();
        for (OrderConfirmShop shop : orderConfirmResult.getData().getList()) {
            if (!StringUtils.isNullOrEmpty(shop.getRemark())) {
                remarkMap.put(shop.getShop_id(), shop.getRemark());
            }
            couponkMap.put(shop.getShop_id(), shop.isUseCoupon());
        }
        String remark = null;
        if (remarkMap.size() > 0) {
            remark = GsonUtils.GsonString(remarkMap);
        }
        String discount = null;
        if (couponkMap.size() > 0) {
            discount = GsonUtils.GsonString(couponkMap);
        }
        System.out.println("discount: " + discount);
//        System.out.println("ids: " + ids + " , address_id: " + address_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_ORDER_ADD);
        params.addBodyParameter("ids", ids);//数组
        params.addBodyParameter("spm", "");
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("address_name", address_user_name);
        params.addBodyParameter("address_mobile", address_mobile);
        params.addBodyParameter("address", address_info);
        params.addBodyParameter("remark", remark);//键值对map
        params.addBodyParameter("discount", discount);
        params.addBodyParameter("type", String.valueOf(payType));//购物车结算1，其他2
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("购物车结算: " + result);
                orderAddResult = GsonUtils.GsonToBean(result, OrderAddResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("购物车结算: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void orderAdd() {
        Map<Integer, String> remarkMap = new HashMap<>();
        Map<Integer, Boolean> couponkMap = new HashMap<>();
        for (OrderConfirmShop shop : orderConfirmResult.getData().getList()) {
            if (!StringUtils.isNullOrEmpty(shop.getRemark())) {
                remarkMap.put(shop.getShop_id(), shop.getRemark());
            }
            couponkMap.put(shop.getShop_id(), shop.isUseCoupon());
        }
        String remark = null;
        if (remarkMap.size() > 0) {
            remark = GsonUtils.GsonString(remarkMap);
        }
        String discount = null;
        if (couponkMap.size() > 0) {
            discount = GsonUtils.GsonString(couponkMap);
        }
//        System.out.println("ids: " + ids + " , address_id: " + address_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_ADD);
        params.addBodyParameter("spm", "");
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("norms_id", String.valueOf(norms_id));
        params.addBodyParameter("num", String.valueOf(goods_num));
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("address_name", address_user_name);
        params.addBodyParameter("address_mobile", address_mobile);
        params.addBodyParameter("address", address_info);
        params.addBodyParameter("remark", remark);//键值对map
        params.addBodyParameter("discount", discount);
        params.addBodyParameter("type", String.valueOf(payType));//购物车结算1，其他2
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("购物车结算: " + result);
                orderAddResult = GsonUtils.GsonToBean(result, OrderAddResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("购物车结算: " + ex.toString());
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
     * 立即购买
     */
    private void goodsSettlement() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_SETTLEMENT);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("norms_id", norms_id);
        params.addBodyParameter("num", String.valueOf(goods_num));
        params.addBodyParameter("address_id", address_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("goodsSettlement: " + result);
                orderConfirmResult = GsonUtils.GsonToBean(result, OrderConfirmResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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

    private AlipayOrderInfo alipayOrderInfo;

    private void alipayRequest() {
//        System.out.println("oid: " + orderAddResult.getData().getOid());
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ALIPAY);
        params.addBodyParameter("oid", orderAddResult.getData().getOid());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("alipay: " + result);
                alipayOrderInfo = GsonUtils.GsonToBean(result, AlipayOrderInfo.class);
                Message message = myHandler.obtainMessage(12);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
//                System.out.println("alipay: " + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private WxpayOrderInfo wxpayOrderInfo;

    private void wxpayRequest() {
        System.out.println("oid: " + orderAddResult.getData().getOid());
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.WXPAY);
        params.addBodyParameter("oid", orderAddResult.getData().getOid());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("wxpay: " + result);
                wxpayOrderInfo = GsonUtils.GsonToBean(result, WxpayOrderInfo.class);
                Message message = myHandler.obtainMessage(11);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("wxpay: " + ex.toString());
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

    private BaseResult baseResult;

    private void balancePay(String pwd) {
//        System.out.println("oid: " + orderAddResult.getData().getOid() + " , password: " + pwd + " , type: " + (balancePayType == 1 ? "wechat" : "ali"));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BALANCE_PAY);
        params.addBodyParameter("oid", orderAddResult.getData().getOid());
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("type", balancePayType == 1 ? "wechat" : "ali");//ali,wechat
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("余额支付: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(13);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("余额支付: " + ex.toString());
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
