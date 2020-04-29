package com.zhiyu.quanzhu.ui.activity;

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
import com.zhiyu.quanzhu.model.result.DeliveryInfoResult;
import com.zhiyu.quanzhu.model.result.OrderInformationResult;
import com.zhiyu.quanzhu.ui.adapter.OrderInformationGoodsRecyclerviewAdapter;
import com.zhiyu.quanzhu.ui.dialog.DeliveryInfoDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyBoardUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.OrderStatusUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class OrderInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private CardView buttonLayout;
    private MyRecyclerView goodsRecyclerView;
    private OrderInformationGoodsRecyclerviewAdapter adapter;
    private LinearLayout daifukuanLayout, yiquxiaoLayout, daishouhuoLayout, daipingjiaLayout, yiwanchengLayout, timeDownLayout,
            deliveryLayout, priceCalculateLayout, payTimeLayout;
    private int order_status, order_id;
    private TextView orderStatusTextView, refoundTextView;
    private TextView orderNoTextView, copyOrderNoTextView, payTypeTextView, shopNameTextView,
            discountPriceTextView, remarkTextView, numTextView, zhengshuTextView, xiaoshuTextView,
            copyAddressTextView, addressNameTextView, addressPhoneTextView, addressTextView,
            addTimeTextView, payTimeTextView, feightPriceTextView, deliveryTimeTextView, deliveryContextTextView,
            showOrderDeliveryTextView;
    private NiceImageView shopIconImageView;
    private DeliveryInfoDialog deliveryDialog;
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
                case 1:
                    if (200 == activity.orderInformationResult.getCode()) {
                        activity.adapter.setList(activity.orderInformationResult.getData().getDetail().getGoods(),
                                activity.orderInformationResult.getData().getDetail().getStatus(),
                                activity.orderInformationResult.getData().getDetail().getId());
                        activity.orderNoTextView.setText(activity.orderInformationResult.getData().getDetail().getOrder_no());
                        activity.payTypeTextView.setText(activity.orderInformationResult.getData().getDetail().getPay_type_desc());
                        activity.orderStatusTextView.setText(activity.orderInformationResult.getData().getDetail().getStatus_desc());
                        Glide.with(activity).load(activity.orderInformationResult.getData().getDetail().getShop_icon()).error(R.drawable.image_error).into(activity.shopIconImageView);
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
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
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
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("订单详情");
        orderNoTextView = findViewById(R.id.orderNoTextView);
        copyOrderNoTextView = findViewById(R.id.copyOrderNoTextView);
        copyOrderNoTextView.setOnClickListener(this);
        payTypeTextView = findViewById(R.id.payTypeTextView);
        shopIconImageView = findViewById(R.id.shopIconImageView);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        discountPriceTextView = findViewById(R.id.discountPriceTextView);
        remarkTextView = findViewById(R.id.remarkTextView);
        numTextView = findViewById(R.id.numTextView);
        deliveryContextTextView = findViewById(R.id.deliveryContextTextView);
        showOrderDeliveryTextView = findViewById(R.id.showOrderDeliveryTextView);
        showOrderDeliveryTextView.setOnClickListener(this);
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
        daishouhuoLayout = findViewById(R.id.daishouhuoLayout);
        daipingjiaLayout = findViewById(R.id.daipingjiaLayout);
        yiwanchengLayout = findViewById(R.id.yiwanchengLayout);
        timeDownLayout = findViewById(R.id.timeDownLayout);
        orderStatusTextView = findViewById(R.id.orderStatusTextView);
        deliveryLayout = findViewById(R.id.deliveryLayout);
        priceCalculateLayout = findViewById(R.id.priceCalculateLayout);
        refoundTextView = findViewById(R.id.refoundTextView);
        payTimeLayout = findViewById(R.id.payTimeLayout);
        initOrderStatusViews();
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
        }
    }


    private void initOrderStatusViews() {
        switch (order_status) {
            case OrderStatusUtils.DAIFUKUAN:
                buttonLayout.setVisibility(View.VISIBLE);
                daifukuanLayout.setVisibility(View.VISIBLE);
                yiquxiaoLayout.setVisibility(View.GONE);
                daishouhuoLayout.setVisibility(View.GONE);
                daipingjiaLayout.setVisibility(View.GONE);
                yiwanchengLayout.setVisibility(View.GONE);
                timeDownLayout.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.GONE);
                orderStatusTextView.setText("待支付");
                break;
            case OrderStatusUtils.DAIFAHUO:
                buttonLayout.setVisibility(View.GONE);
                timeDownLayout.setVisibility(View.GONE);
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
                timeDownLayout.setVisibility(View.GONE);
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
                timeDownLayout.setVisibility(View.GONE);
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
                timeDownLayout.setVisibility(View.GONE);
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
                timeDownLayout.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.GONE);
                priceCalculateLayout.setVisibility(View.VISIBLE);
                refoundTextView.setVisibility(View.GONE);
                payTimeLayout.setVisibility(View.VISIBLE);
                orderStatusTextView.setText("已完成");
                break;
            case OrderStatusUtils.SHOUHOUZHONG:
                buttonLayout.setVisibility(View.GONE);
                timeDownLayout.setVisibility(View.GONE);
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
//                System.out.println("物流详情: " + result);
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

}
