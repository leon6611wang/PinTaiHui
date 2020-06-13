package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.AlipayOrderInfo;
import com.zhiyu.quanzhu.model.result.DeliveryInfoResult;
import com.zhiyu.quanzhu.model.result.OrderInformationResult;
import com.zhiyu.quanzhu.model.result.WxpayOrderInfo;
import com.zhiyu.quanzhu.ui.adapter.OrderInformationGoodsRecyclerviewAdapter;
import com.zhiyu.quanzhu.ui.dialog.DeliveryInfoDialog;
import com.zhiyu.quanzhu.ui.dialog.OrderConfirmUseCouponDialog;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.TimeDownTextView;
import com.zhiyu.quanzhu.utils.AliPayUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyBoardUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.OrderStatusUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.WxPayUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class OrderInformationActivity extends BaseActivity implements View.OnClickListener, AliPayUtils.OnAlipayCallbackListener, WXEntryActivity.OnWxpayCallbackListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private CardView buttonLayout;
    private MyRecyclerView goodsRecyclerView;
    private OrderInformationGoodsRecyclerviewAdapter adapter;
    private LinearLayout daifukuanLayout, yiquxiaoLayout, daishouhuoLayout, daipingjiaLayout, yiwanchengLayout, timeDownLayout,
            deliveryLayout, priceCalculateLayout, payTimeLayout, customerServiceLayout;
    private int order_status, order_id;
    private TextView orderStatusTextView, refoundTextView;
    private TextView orderNoTextView, copyOrderNoTextView, payTypeTextView, shopNameTextView,
            discountPriceTextView, remarkTextView, numTextView, zhengshuTextView, xiaoshuTextView,
            copyAddressTextView, addressNameTextView, addressPhoneTextView, addressTextView,
            addTimeTextView, payTimeTextView, feightPriceTextView, deliveryTimeTextView, deliveryContextTextView,
            showOrderDeliveryTextView;
    private TimeDownTextView timeDownTextView;
    private TextView cancelOrderTextView, payTextView, deleteOrderTextView, confirmShouHuoTextView,
            commentTextView, orderAgainTextView;
    private NiceImageView shopIconImageView;
    private DeliveryInfoDialog deliveryDialog;
    private YNDialog ynDialog;
    private PayWayDialog payWayDialog;
    private PasswordCheckDialog passwordCheckDialog;
    private OrderConfirmUseCouponDialog useCouponDialog;
    private int balancePayType;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<OrderInformationActivity> activityWeakReference;

        public MyHandler(OrderInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OrderInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
                case 1:
                    if (200 == activity.orderInformationResult.getCode()) {
                        activity.order_status = activity.orderInformationResult.getData().getDetail().getStatus();
                        activity.initOrderStatusViews();
                        activity.adapter.setList(activity.orderInformationResult.getData().getDetail().getGoods(),
                                activity.orderInformationResult.getData().getDetail().getStatus(),
                                activity.orderInformationResult.getData().getDetail().getId());
                        activity.orderNoTextView.setText(activity.orderInformationResult.getData().getDetail().getOrder_no());
                        activity.payTypeTextView.setText(activity.orderInformationResult.getData().getDetail().getPay_type_desc());
                        activity.orderStatusTextView.setText(activity.orderInformationResult.getData().getDetail().getStatus_desc());
                        Glide.with(activity).load(activity.orderInformationResult.getData().getDetail().getShop_icon()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.shopIconImageView);
                        activity.shopNameTextView.setText(activity.orderInformationResult.getData().getDetail().getShop_name());
                        activity.feightPriceTextView.setText(activity.orderInformationResult.getData().getDetail().getFeight_price() == 0 ?
                                "包邮" : PriceParseUtils.getInstance().parsePrice(activity.orderInformationResult.getData().getDetail().getFeight_price()));
                        activity.discountPriceTextView.setText("-￥" + PriceParseUtils.getInstance().parsePrice(activity.orderInformationResult.getData().getDetail().getDiscount_price()));
                        activity.remarkTextView.setText(activity.orderInformationResult.getData().getDetail().getRemark());
                        activity.numTextView.setText(String.valueOf(activity.orderInformationResult.getData().getDetail().getNum()));
                        activity.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(activity.orderInformationResult.getData().getDetail().getPaymoney()));
                        activity.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(activity.orderInformationResult.getData().getDetail().getPaymoney()));
                        activity.addressNameTextView.setText(activity.orderInformationResult.getData().getDetail().getAddress_name());
                        activity.addressPhoneTextView.setText(activity.orderInformationResult.getData().getDetail().getAddress_phone());
                        activity.addressTextView.setText("地址：" + activity.orderInformationResult.getData().getDetail().getAddress());
                        activity.addTimeTextView.setText(activity.orderInformationResult.getData().getDetail().getAddtime());
                        activity.payTimeTextView.setText(activity.orderInformationResult.getData().getDetail().getPaytime());

                    }
                    break;
                case 2:
                    if (200 == activity.orderDeliveryResult.getCode()) {
                        activity.deliveryTimeTextView.setText(activity.orderDeliveryResult.getData().getList().getData().get(0).getTime());
                        activity.deliveryContextTextView.setText(activity.orderDeliveryResult.getData().getList().getData().get(0).getContext());
                    }
                    break;
                case 3://删除订单
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
                case 4://取消订单、确认收货
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.orderInformation();
                    }
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
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        AliPayUtils.getInstance(this).setOnAlipayCallbackListener(this);
        WXEntryActivity.setOnWxpayCallbackListener(this);
        order_status = getIntent().getIntExtra("order_status", 0);
        order_id = getIntent().getIntExtra("order_id", 0);
        initDialogs();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderInformation();
        if (order_status == OrderStatusUtils.DAISHOUHUO) {
            deliveryInfo();
        }
    }

    private void initDialogs() {
        deliveryDialog = new DeliveryInfoDialog(this, R.style.dialog);
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                switch (orderOperateType) {
                    case 1:
                        deleteOrder();
                        break;
                    case 2:
                        cancelOrder();
                        break;
                    case 3:
                        confirmShouHuo();
                        break;
                }

            }
        });
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
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.dialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {
                if (payWayDialog.isShowing()) {
                    payWayDialog.dismiss();
                }
                balancePay(password);
            }
        });
        useCouponDialog = new OrderConfirmUseCouponDialog(this, R.style.dialog, new OrderConfirmUseCouponDialog.OnSelectCouponListener() {
            @Override
            public void onSelectCoupon(int index, boolean isUseCoupon) {

            }
        });
    }

    @Override
    public void onAlipayCallBack() {

    }

    @Override
    public void onWxpayCallback() {

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("订单详情");
        orderNoTextView = findViewById(R.id.orderNoTextView);
        copyOrderNoTextView = findViewById(R.id.copyOrderNoTextView);
        copyOrderNoTextView.setOnClickListener(this);
        timeDownTextView = findViewById(R.id.timeDownTextView);
        payTypeTextView = findViewById(R.id.payTypeTextView);
        shopIconImageView = findViewById(R.id.shopIconImageView);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        discountPriceTextView = findViewById(R.id.discountPriceTextView);
        remarkTextView = findViewById(R.id.remarkTextView);
        numTextView = findViewById(R.id.numTextView);
        cancelOrderTextView = findViewById(R.id.cancelOrderTextView);
        cancelOrderTextView.setOnClickListener(this);
        payTextView = findViewById(R.id.payTextView);
        payTextView.setOnClickListener(this);
        deleteOrderTextView = findViewById(R.id.deleteOrderTextView);
        deleteOrderTextView.setOnClickListener(this);
        confirmShouHuoTextView = findViewById(R.id.confirmShouHuoTextView);
        confirmShouHuoTextView.setOnClickListener(this);
        deliveryContextTextView = findViewById(R.id.deliveryContextTextView);
        showOrderDeliveryTextView = findViewById(R.id.showOrderDeliveryTextView);
        showOrderDeliveryTextView.setOnClickListener(this);
        commentTextView = findViewById(R.id.commentTextView);
        commentTextView.setOnClickListener(this);
        orderAgainTextView = findViewById(R.id.orderAgainTextView);
        orderAgainTextView.setOnClickListener(this);
        zhengshuTextView = findViewById(R.id.zhengshuTextView);
        xiaoshuTextView = findViewById(R.id.xiaoshuTextView);
        copyAddressTextView = findViewById(R.id.copyAddressTextView);
        deliveryTimeTextView = findViewById(R.id.deliveryTimeTextView);
        copyAddressTextView.setOnClickListener(this);
        addressNameTextView = findViewById(R.id.addressNameTextView);
        addressPhoneTextView = findViewById(R.id.addressPhoneTextView);
        addressTextView = findViewById(R.id.addressTextView);
        addTimeTextView = findViewById(R.id.addTimeTextView);
        payTimeTextView = findViewById(R.id.payTimeTextView);
        goodsRecyclerView = findViewById(R.id.goodsRecyclerView);
        feightPriceTextView = findViewById(R.id.feightPriceTextView);
        adapter = new OrderInformationGoodsRecyclerviewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goodsRecyclerView.setLayoutManager(layoutManager);
        goodsRecyclerView.setAdapter(adapter);
        buttonLayout = findViewById(R.id.buttonLayout);
        daifukuanLayout = findViewById(R.id.daifukuanLayout);
        yiquxiaoLayout = findViewById(R.id.yiquxiaoLayout);
        yiquxiaoLayout.setOnClickListener(this);
        daishouhuoLayout = findViewById(R.id.daishouhuoLayout);
        daipingjiaLayout = findViewById(R.id.daipingjiaLayout);
        yiwanchengLayout = findViewById(R.id.yiwanchengLayout);
//        timeDownLayout = findViewById(R.id.timeDownLayout);
        orderStatusTextView = findViewById(R.id.orderStatusTextView);
        deliveryLayout = findViewById(R.id.deliveryLayout);
        priceCalculateLayout = findViewById(R.id.priceCalculateLayout);
        refoundTextView = findViewById(R.id.refoundTextView);
        payTimeLayout = findViewById(R.id.payTimeLayout);
        customerServiceLayout = findViewById(R.id.customerServiceLayout);
        customerServiceLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.copyOrderNoTextView:
                MessageToast.getInstance(this).show("订单号复制成功");
                CopyBoardUtils.getInstance().copy(this, orderInformationResult.getData().getDetail().getOrder_no());
                break;
            case R.id.copyAddressTextView:
                String address = orderInformationResult.getData().getDetail().getAddress_name() + " " +
                        orderInformationResult.getData().getDetail().getAddress_phone() + " " +
                        orderInformationResult.getData().getDetail().getAddress();
                MessageToast.getInstance(this).show("收件信息复制成功");
                CopyBoardUtils.getInstance().copy(this, address);
                break;
            case R.id.showOrderDeliveryTextView:
                deliveryDialog.show();
                deliveryDialog.setOrderId(order_id);
                break;
            case R.id.customerServiceLayout:
                Intent serviceIntent = new Intent(this, CustomerServiceActivity.class);
                serviceIntent.putExtra("shop_id", orderInformationResult.getData().getDetail().getShop_id());
                startActivity(serviceIntent);
                break;
            case R.id.deleteOrderTextView://删除订单
                orderOperateType = 1;
                ynDialog.show();
                ynDialog.setTitle("确定删除该订单？");
                break;
            case R.id.cancelOrderTextView://取消订单
                orderOperateType = 2;
                ynDialog.show();
                ynDialog.setTitle("确定取消该订单？");
                break;
            case R.id.payTextView://立即支付
                payWayDialog.show();
                payWayDialog.setMoney((int) orderInformationResult.getData().getDetail().getPaymoney());
                break;
            case R.id.confirmShouHuoTextView://确定收货
                orderOperateType = 3;
                ynDialog.show();
                ynDialog.setTitle("确定已收到货物？");
                break;
            case R.id.commentTextView://立即评价
                Intent commentIntent = new Intent(this, OrderGoodsCommentsActivity.class);
                commentIntent.putExtra("orderShop", GsonUtils.GsonString(orderInformationResult.getData().getDetail()));
                startActivity(commentIntent);
                break;
            case R.id.orderAgainTextView://再来一单
                Intent shopIntent = new Intent(this, ShopInformationActivity.class);
                shopIntent.putExtra("shop_id", String.valueOf(orderInformationResult.getData().getDetail().getShop_id()));
                startActivity(shopIntent);
                break;
        }
    }

    private int orderOperateType = 0;

    private void initOrderStatusViews() {
        switch (order_status) {
            case OrderStatusUtils.DAIFUKUAN:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.VISIBLE);
                yiquxiaoLayout.setVisibility(View.GONE);
                daishouhuoLayout.setVisibility(View.GONE);
                daipingjiaLayout.setVisibility(View.GONE);
                yiwanchengLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.GONE);
                orderStatusTextView.setText("待支付");
                break;
            case OrderStatusUtils.DAIFAHUO:
                buttonLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("待发货");
                break;
            case OrderStatusUtils.DAISHOUHUO:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.GONE);
                yiquxiaoLayout.setVisibility(View.GONE);
                daishouhuoLayout.setVisibility(View.VISIBLE);
                daipingjiaLayout.setVisibility(View.GONE);
                yiwanchengLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.VISIBLE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("待收货");
                break;
            case OrderStatusUtils.DAIPINGJIA:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.GONE);
                yiquxiaoLayout.setVisibility(View.GONE);
                daishouhuoLayout.setVisibility(View.GONE);
                daipingjiaLayout.setVisibility(View.VISIBLE);
                yiwanchengLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("待评价");
                break;
            case OrderStatusUtils.YIQUXIAO:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.GONE);
                yiquxiaoLayout.setVisibility(View.VISIBLE);
                daishouhuoLayout.setVisibility(View.GONE);
                daipingjiaLayout.setVisibility(View.GONE);
                yiwanchengLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.GONE);
                orderStatusTextView.setText("已取消");
                break;
            case OrderStatusUtils.YIWANCHENG:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.GONE);
                yiquxiaoLayout.setVisibility(View.GONE);
                daishouhuoLayout.setVisibility(View.GONE);
                daipingjiaLayout.setVisibility(View.GONE);
                yiwanchengLayout.setVisibility(View.VISIBLE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("已完成");
                break;
            case OrderStatusUtils.SHOUHOUZHONG:
                buttonLayout.setVisibility(View.GONE);
//                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.GONE);
                refoundTextView.setVisibility(View.VISIBLE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("售后处理中");
                break;
        }
    }

    private OrderInformationResult orderInformationResult;

    private void orderInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_INFORMATION);
        params.addBodyParameter("oid", String.valueOf(order_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("orderInformation: " + result);
                orderInformationResult = GsonUtils.GsonToBean(result, OrderInformationResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("orderInformation: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private DeliveryInfoResult orderDeliveryResult;

    /**
     * 物流详情
     */
    private void deliveryInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELIVERY_INFO);
        params.addBodyParameter("oid", String.valueOf(order_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("物流详情: " + result);
                orderDeliveryResult = GsonUtils.GsonToBean(result, DeliveryInfoResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("物流详情: " + ex.toString());
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

    private void deleteOrder() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_ORDER);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    private void cancelOrder() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CANCEL_ORDER);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
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

    private void confirmShouHuo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CONFIRM_SHOU_HUO);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
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

    private AlipayOrderInfo alipayOrderInfo;

    private void alipayRequest() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ALIPAY);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        params.addBodyParameter("paytype","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("alipay: " + result);
                alipayOrderInfo = GsonUtils.GsonToBean(result, AlipayOrderInfo.class);
                Message message = myHandler.obtainMessage(12);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("alipay: " + ex.toString());

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
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.WXPAY);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        params.addBodyParameter("paytype","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("wxpay: " + result);
                wxpayOrderInfo = GsonUtils.GsonToBean(result, WxpayOrderInfo.class);
                Message message = myHandler.obtainMessage(11);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("wxpay: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void balancePay(String pwd) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.BALANCE_PAY);
        params.addBodyParameter("oid", String.valueOf(orderInformationResult.getData().getDetail().getId()));
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("type", balancePayType == 1 ? "wechat" : "ali");//ali,wechat
        params.addBodyParameter("paytype","1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("余额支付: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(13);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("余额支付: " + ex.toString());
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
