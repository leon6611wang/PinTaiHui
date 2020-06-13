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

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Address;
import com.zhiyu.quanzhu.model.result.PointGoodsOrderConfirmResult;
import com.zhiyu.quanzhu.ui.adapter.OrderConfirmRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 积分商品-订单确认
 */
public class PointGoodsOrderConfirmActivity extends BaseActivity implements View.OnClickListener, OrderConfirmRecyclerAdapter.OnRemarkEditListener {
    private LinearLayout backLayout;
    private TextView titleTextView, goodsNameTextView, pointTextView1, pointTextView2;
    private TextView zhifuTextView, changeAddressTextView, userNameTextView, phonenumTextView, addressTextView;
    private NiceImageView goodsImageImageView;
    private int goods_id;
    private LoadingDialog loadingDialog;
    private String address_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PointGoodsOrderConfirmActivity> activityWeakReference;

        public MyHandler(PointGoodsOrderConfirmActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PointGoodsOrderConfirmActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.loadingDialog.dismiss();
                    if (activity.confirmResult.getCode() == 200) {
                        activity.address_id = activity.confirmResult.getData().getAddress().get_id();
                        activity.userNameTextView.setText("收货人：" + activity.confirmResult.getData().getAddress().getName());
                        activity.phonenumTextView.setText("电话：" + activity.confirmResult.getData().getAddress().getPhone());
                        activity.addressTextView.setText("地址：" + activity.confirmResult.getData().getAddress().getProvince_name() + " " + activity.confirmResult.getData().getAddress().getCity_name() + " " + activity.confirmResult.getData().getAddress().getAddress());
                        Glide.with(activity).load(activity.confirmResult.getData().getThumb()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.goodsImageImageView);
                        activity.goodsNameTextView.setText(activity.confirmResult.getData().getGoods_name());
                        activity.pointTextView1.setText(String.valueOf(activity.confirmResult.getData().getNeed_inegral()));
                        activity.pointTextView2.setText(String.valueOf(activity.confirmResult.getData().getNeed_inegral()));
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        Intent intent = new Intent();
                        intent.putExtra("exchange_success", true);
                        activity.setResult(1003, intent);
                        activity.finish();
                    }
                    break;
                case 3:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_goods_order_confirm);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        goods_id = getIntent().getIntExtra("goods_id", 0);
        initViews();
        initDialogs();
        orderConfirmInfo();
    }

    private void initDialogs() {
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("订单确认");
        changeAddressTextView = findViewById(R.id.changeAddressTextView);
        changeAddressTextView.setOnClickListener(this);
        userNameTextView = findViewById(R.id.userNameTextView);
        phonenumTextView = findViewById(R.id.phonenumTextView);
        addressTextView = findViewById(R.id.addressTextView);
        zhifuTextView = findViewById(R.id.zhifuTextView);
        zhifuTextView.setOnClickListener(this);
        goodsImageImageView = findViewById(R.id.goodsImageImageView);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        pointTextView1 = findViewById(R.id.pointTextView1);
        pointTextView2 = findViewById(R.id.pointTextView2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhifuTextView:
                System.out.println("立即兑换");
                if (!StringUtils.isNullOrEmpty(userNameTextView.getText().toString().trim()) &&
                        !StringUtils.isNullOrEmpty(phonenumTextView.getText().toString().trim()) &&
                        !StringUtils.isNullOrEmpty(addressTextView.getText().toString().trim())) {
                    pointGoodsExchange();
                } else {
                    MessageToast.getInstance(this).show("请补全收货地址");
                }
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

    private PointGoodsOrderConfirmResult confirmResult;

    private void orderConfirmInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.POINT_GOODS_ORDER_CONFIRM);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("积分订单确认: " + result);
                confirmResult = GsonUtils.GsonToBean(result, PointGoodsOrderConfirmResult.class);
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

    private void pointGoodsExchange() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.POINT_GOODS_EXCHANGE);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("address_mobile", phonenumTextView.getText().toString().trim());
        params.addBodyParameter("address_name", userNameTextView.getText().toString().trim());
        params.addBodyParameter("address", addressTextView.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品兑换: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("商品兑换: " + ex.toString());
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
