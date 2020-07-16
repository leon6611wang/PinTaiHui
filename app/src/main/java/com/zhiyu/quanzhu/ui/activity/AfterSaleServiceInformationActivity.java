package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.DeliveryCompany;
import com.zhiyu.quanzhu.model.bean.OrderInformationGoods;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.result.AfterSaleServiceInformationResult;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.DeliveryCompanyDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.RefundTimeDownTextView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ServiceUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AfterSaleServiceInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private int refund_id;
    private RefundTimeDownTextView timeDownTextView;
    private TextView serviceStatusTextView, serviceDescTextView,
            goodsNameTextView, goodsNormsTextView, goodsCountTextView, deliverySelectTextView,
            deliveryStatusTextView, reasonTextView, refundPriceTextView, applyTimeTextView,
            refundNoTextView, servicerTextView, cancelRefundTextView, editRefundTextView, remarkTextView,
            reasonLeftTextView, numberLeftTextView, bottomButtonLeftTextView, bottomButtonRightTextView;
    private NiceImageView goodsImageImageView;
    private EditText deliveryNoEditText;
    private LinearLayout deliverySelectLayout, deliveryNoLayout, deliveryStatusLayout, reasonLayout, refundPriceLayout,
            applyTimeLayout, refundNoLayout, midButtonLayout;
    //    private LinearLayout midButtonLayout2;
    private LinearLayout coordinateHistoryLayout, applyKeFuReasonLayout, addressLaout;
    private TextView applyKeFuReasonTextView, userNameTextView, phoneNumberTextView, addressTextView;
    private MyRecyclerView mRecyclerView;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private ArrayList<String> mImageList = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private DeliveryCompanyDialog deliveryCompanyDialog;
    private YNDialog ynDialog;
    private int refundType, refundStatus;
    private long refund_money;
    private String deliveryCompanyCode;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<AfterSaleServiceInformationActivity> activityWeakReference;

        public MyHandler(AfterSaleServiceInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AfterSaleServiceInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    activity.loadingDialog.dismiss();
                    if (200 == activity.informationResult.getCode()) {
                        activity.refundType = activity.informationResult.getData().getData().getRefund();
                        activity.refundStatus = activity.informationResult.getData().getData().getStatus();
                        activity.changeViews();
                        activity.serviceStatusTextView.setText(activity.informationResult.getData().getData().getTitle());
                        if (activity.informationResult.getData().getData().getTime() > 0) {
                            activity.timeDownTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.timeDownTextView.setVisibility(View.GONE);
                        }
                        activity.timeDownTextView.setOverTime(activity.informationResult.getData().getData().getTime(), new RefundTimeDownTextView.OnTimeDownListener() {
                            @Override
                            public void onTImeDownFinish() {

                            }
                        });
                        if (!StringUtils.isNullOrEmpty(activity.informationResult.getData().getData().getDesc())) {
                            activity.serviceDescTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.serviceDescTextView.setVisibility(View.GONE);
                        }
                        activity.serviceDescTextView.setText(activity.informationResult.getData().getData().getDesc());
                        Glide.with(activity).load(activity.informationResult.getData().getData().getGoods_img()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.goodsImageImageView);
                        activity.goodsNameTextView.setText(activity.informationResult.getData().getData().getGoods_name());
//                        activity.goodsNormsTextView.setText(activity.informationResult.getData().getData().get);
                        activity.goodsCountTextView.setText("X" + activity.informationResult.getData().getData().getGoods_num());
                        activity.reasonTextView.setText(activity.informationResult.getData().getData().getReason());
                        activity.refundNoTextView.setText(activity.informationResult.getData().getData().getRefund_sn());
                        activity.applyTimeTextView.setText(activity.informationResult.getData().getData().getAddtime());
                        activity.refundPriceTextView.setText("￥" + PriceParseUtils.getInstance().parsePrice(activity.informationResult.getData().getData().getRefund_price()));
                        activity.applyKeFuReasonTextView.setText(activity.informationResult.getData().getData().getKefu_reason());
                        activity.userNameTextView.setText("收货人：" + activity.informationResult.getData().getData().getUser_name());
                        activity.phoneNumberTextView.setText("联系电话：" + activity.informationResult.getData().getData().getPhone_number());
                        activity.addressTextView.setText("退货地址：" + activity.informationResult.getData().getData().getAddress());
                        if (!StringUtils.isNullOrEmpty(activity.informationResult.getData().getData().getRemark())) {
                            activity.remarkTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.remarkTextView.setVisibility(View.GONE);
                        }
                        activity.remarkTextView.setText(activity.informationResult.getData().getData().getRemark());
                        if (null != activity.informationResult.getData().getData().getImg() && activity.informationResult.getData().getData().getImg().size() > 0) {
                            if (null != activity.mImageList) {
                                activity.mImageList.clear();
                            }
                            for (UploadImage img : activity.informationResult.getData().getData().getImg()) {
                                activity.mImageList.add(img.getFile());
                            }
                        }
                        activity.imageGridAdapter.setData(activity.mImageList);

                    }
                    break;
                case 2://撤销申请
                    activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
                case 3:
                    activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.refundInformation();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_service_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        refund_id = getIntent().getIntExtra("refund_id", 0);
        refund_money = getIntent().getLongExtra("refund_money", 0l);
//        System.out.println("refund_id: "+refund_id);
        initDialogs();
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refundInformation();
    }

    private void initDialogs() {
        loadingDialog = new LoadingDialog(this, R.style.dialog);
        deliveryCompanyDialog = new DeliveryCompanyDialog(this, R.style.dialog, new DeliveryCompanyDialog.OnDeliveryCompanyListener() {
            @Override
            public void onDeliveryCompany(DeliveryCompany deliveryCompany) {
                deliveryCompanyCode = deliveryCompany.getCode();
                deliverySelectTextView.setText(deliveryCompany.getName());
            }
        });
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                cancelRefund();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("售后详情");
        serviceStatusTextView = findViewById(R.id.serviceStatusTextView);
        timeDownTextView = findViewById(R.id.timeDownTextView);
        serviceDescTextView = findViewById(R.id.serviceDescTextView);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        goodsNormsTextView = findViewById(R.id.goodsNormsTextView);
        goodsCountTextView = findViewById(R.id.goodsCountTextView);
        deliverySelectLayout = findViewById(R.id.deliverySelectLayout);
        deliverySelectLayout.setOnClickListener(this);
        deliveryStatusTextView = findViewById(R.id.deliveryStatusTextView);
        deliverySelectTextView = findViewById(R.id.deliverySelectTextView);
        reasonTextView = findViewById(R.id.reasonTextView);
        refundPriceTextView = findViewById(R.id.refundPriceTextView);
        applyTimeTextView = findViewById(R.id.applyTimeTextView);
        refundNoTextView = findViewById(R.id.refundNoTextView);
        servicerTextView = findViewById(R.id.servicerTextView);
        servicerTextView.setOnClickListener(this);
        cancelRefundTextView = findViewById(R.id.cancelRefundTextView);
        cancelRefundTextView.setOnClickListener(this);
        editRefundTextView = findViewById(R.id.editRefundTextView);
        editRefundTextView.setOnClickListener(this);
        remarkTextView = findViewById(R.id.remarkTextView);
        goodsImageImageView = findViewById(R.id.goodsImageImageView);
        deliveryNoEditText = findViewById(R.id.deliveryNoEditText);
        deliverySelectLayout = findViewById(R.id.deliverySelectLayout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        deliveryNoLayout = findViewById(R.id.deliveryNoLayout);
        deliveryStatusLayout = findViewById(R.id.deliveryStatusLayout);
        refundPriceLayout = findViewById(R.id.refundPriceLayout);
        reasonLayout = findViewById(R.id.reasonLayout);
        applyTimeLayout = findViewById(R.id.applyTimeLayout);
        refundNoLayout = findViewById(R.id.refundNoLayout);
        midButtonLayout = findViewById(R.id.midButtonLayout);
//        midButtonLayout2 = findViewById(R.id.midButtonLayout2);
        imageGridAdapter = new PublishFeedImagesGridAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        imageGridAdapter.setData(mImageList);
        mRecyclerView.setAdapter(imageGridAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        reasonLeftTextView = findViewById(R.id.reasonLeftTextView);
        numberLeftTextView = findViewById(R.id.numberLeftTextView);
        bottomButtonLeftTextView = findViewById(R.id.bottomButtonLeftTextView);
        bottomButtonLeftTextView.setOnClickListener(this);
        bottomButtonRightTextView = findViewById(R.id.bottomButtonRightTextView);
        bottomButtonRightTextView.setOnClickListener(this);
        coordinateHistoryLayout = findViewById(R.id.coordinateHistoryLayout);
        coordinateHistoryLayout.setOnClickListener(this);
        applyKeFuReasonLayout = findViewById(R.id.applyKeFuReasonLayout);
        applyKeFuReasonTextView = findViewById(R.id.applyKeFuReasonTextView);
        addressLaout = findViewById(R.id.addressLaout);
        userNameTextView = findViewById(R.id.userNameTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        addressTextView = findViewById(R.id.addressTextView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.deliverySelectLayout:
                deliveryCompanyDialog.show();
                break;
            case R.id.bottomButtonLeftTextView:
                if (informationResult.getData().getData().isIs_kefu()) {
                    //撤销申请
                    cancelRefund();
                } else {
                    Intent serviceIntent = new Intent(this, CustomerServiceActivity.class);
                    serviceIntent.putExtra("shop_id", informationResult.getData().getData().getShop_id());
                    startActivity(serviceIntent);
                }
                break;
            case R.id.bottomButtonRightTextView:
//                0 未处理
//                1 已同意,待退货
//                2 已退货,待收货
//                3 退款成功
//                4 客服介入
//                5 取消退款
//                6 拒绝退款
                switch (refundStatus) {
                    case ServiceUtils.REFUND_DAI_SHOU_HUO:
                    case ServiceUtils.REFUND_TUI_KUAN_CHENG_GONG:
                        goHome();
                        break;
                    case ServiceUtils.REFUND_DAI_TUI_HUO:
                        if (StringUtils.isNullOrEmpty(deliveryNoEditText.getText().toString().trim())) {
                            MessageToast.getInstance(this).show("请补全物流单号");
                            return;
                        }
                        if (StringUtils.isNullOrEmpty(deliveryCompanyCode)) {
                            MessageToast.getInstance(this).show("请选择物流公司");
                            return;
                        }
                        postDeliveryNo();
                        break;
                    case ServiceUtils.REFUND_DAI_CHU_LI:
                    case ServiceUtils.REFUND_QU_XIAO:
                    case ServiceUtils.REFUND_JU_JUE:
                    case ServiceUtils.REFUND_KE_FU_JIE_RU:
                        if (informationResult.getData().getData().getKefu_status() == 1) {
                            if (StringUtils.isNullOrEmpty(deliveryNoEditText.getText().toString().trim())) {
                                MessageToast.getInstance(this).show("请补全物流单号");
                                return;
                            }
                            if (StringUtils.isNullOrEmpty(deliveryCompanyCode)) {
                                MessageToast.getInstance(this).show("请选择物流公司");
                                return;
                            }
                            postDeliveryNo();
                        } else {
                            Intent serviceIntent = new Intent(this, CustomerServiceActivity.class);
                            serviceIntent.putExtra("shop_id", informationResult.getData().getData().getPlatform_id());
                            startActivity(serviceIntent);
                        }

                        break;
                }
                break;
            case R.id.servicerTextView://客服介入
                if (informationResult.getData().getData().getKefu_status() == 3) {
                    if (informationResult.getData().getData().isIs_kefu()) {
                        //撤销申请
                        cancelRefund();
                    } else {
                        Intent serviceIntent = new Intent(this, CustomerServiceActivity.class);
                        serviceIntent.putExtra("shop_id", informationResult.getData().getData().getShop_id());
                        startActivity(serviceIntent);
                    }
                } else {
                    Intent intent = new Intent(this, StartCoordinateActivity.class);
                    intent.putExtra("id", informationResult.getData().getData().getId());
                    startActivity(intent);
                }

                break;
            case R.id.cancelRefundTextView:
                ynDialog.show();
                ynDialog.setTitle("确定撤销申请？");
                break;
            case R.id.editRefundTextView:
                Intent afterSaleServiceIntent = new Intent(this, AfterSaleServiceActivity.class);
                OrderInformationGoods goods = new OrderInformationGoods();
                goods.setGoods_id(informationResult.getData().getData().getGoods_id());
                goods.setGoods_img(informationResult.getData().getData().getGoods_img());
                goods.setGoods_num(informationResult.getData().getData().getGoods_num());
                goods.setGoods_name(informationResult.getData().getData().getGoods_name());
                goods.setNorms_name(informationResult.getData().getData().getNorms_name());
                goods.setRefund_remark(informationResult.getData().getData().getRemark());
                goods.setRefund_img_list(informationResult.getData().getData().getImg());
                String goodsJson = GsonUtils.GsonString(goods);
                afterSaleServiceIntent.putExtra("order_id", informationResult.getData().getData().getId());
                afterSaleServiceIntent.putExtra("goodsJson", goodsJson);
                afterSaleServiceIntent.putExtra("refund_id", informationResult.getData().getData().getId());
                afterSaleServiceIntent.putExtra("isUpdate", 1);
                afterSaleServiceIntent.putExtra("refund_money", refund_money);
                afterSaleServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(afterSaleServiceIntent);
                break;
            case R.id.coordinateHistoryLayout:
                Intent coordinateIntent = new Intent(this, CoordinateHistoryActivity.class);
                coordinateIntent.putExtra("oid", informationResult.getData().getData().getOid());
                coordinateIntent.putExtra("itemid", informationResult.getData().getData().getItemid());
                startActivity(coordinateIntent);
                break;
        }
    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void changeViews() {
        serviceTypeChangeViews();
        serviceStatusChangeViews();
    }

    private void serviceTypeChangeViews() {
        switch (refundType) {
            case ServiceUtils.TYPE_TUIHUO:
                titleTextView.setText("换货详情");
                reasonLeftTextView.setText("换货原因");
                numberLeftTextView.setText("换货编号");
                break;
            case ServiceUtils.TYPE_TUIHUOTUIKUAN:
            case ServiceUtils.TYPE_TUIKUAN:
                titleTextView.setText("退款详情");
                reasonLeftTextView.setText("退款原因");
                numberLeftTextView.setText("退款编号");
                break;
        }
        if (informationResult.getData().getData().isIs_kefu()) {
            titleTextView.setText("客服介入");
            reasonLeftTextView.setText("退款原因");
            numberLeftTextView.setText("退款编号");
        }
    }

    private void serviceStatusChangeViews() {
//        refundStatus = 5;
        deliveryNoLayout.setVisibility(View.GONE);
        deliverySelectLayout.setVisibility(View.GONE);
        deliveryStatusLayout.setVisibility(View.GONE);
        coordinateHistoryLayout.setVisibility(View.GONE);
        applyKeFuReasonLayout.setVisibility(View.GONE);
        reasonLayout.setVisibility(View.VISIBLE);
        refundPriceLayout.setVisibility(View.VISIBLE);
        applyTimeLayout.setVisibility(View.VISIBLE);
        refundNoLayout.setVisibility(View.VISIBLE);
        midButtonLayout.setVisibility(View.GONE);
        bottomButtonLeftTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
        bottomButtonLeftTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
        bottomButtonLeftTextView.setClickable(true);
//        midButtonLayout2.setVisibility(View.GONE);
//        0 未处理
//        1 已同意,待退货
//        2 已退货,待收货
//        3 退款成功
//        4 客服介入
//        5 取消退款
//        6 拒绝退款
        switch (refundStatus) {
            case ServiceUtils.REFUND_DAI_CHU_LI://待处理
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                midButtonLayout.setVisibility(View.VISIBLE);
                bottomButtonLeftTextView.setText("联系卖家");
                bottomButtonRightTextView.setText("圈助客服");
                break;
            case ServiceUtils.REFUND_JU_JUE://拒绝退款
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                bottomButtonLeftTextView.setText("联系卖家");
                bottomButtonRightTextView.setText("圈助客服");
                break;
            case ServiceUtils.REFUND_DAI_TUI_HUO://快递寄回
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusableInTouchMode(true);
                deliveryNoEditText.setFocusable(true);
                deliveryNoEditText.requestFocus();
                deliveryNoEditText.setHint("请填写物流单号");
                deliverySelectLayout.setVisibility(View.VISIBLE);
                addressLaout.setVisibility(View.VISIBLE);
                bottomButtonLeftTextView.setText("联系卖家");
                bottomButtonRightTextView.setText("提交信息");
                break;
            case ServiceUtils.REFUND_QU_XIAO://用户取消
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("圈助客服");
                bottomButtonLeftTextView.setText("联系卖家");
                break;
            case ServiceUtils.REFUND_TUI_KUAN_CHENG_GONG://退款完成
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusable(false);
                deliveryNoEditText.setFocusableInTouchMode(false);
                deliveryNoEditText.setHint("物流单号");
                deliveryStatusLayout.setVisibility(View.VISIBLE);
                bottomButtonLeftTextView.setText("联系卖家");
                bottomButtonRightTextView.setText("返回首页");
                break;
            case ServiceUtils.REFUND_DAI_SHOU_HUO://快递寄回中
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusable(false);
                deliveryNoEditText.setFocusableInTouchMode(false);
                deliveryNoEditText.setHint("物流单号");
                deliveryStatusLayout.setVisibility(View.VISIBLE);
                bottomButtonLeftTextView.setText("联系卖家");
                bottomButtonRightTextView.setText("返回首页");
                break;
            case ServiceUtils.REFUND_KE_FU_JIE_RU://客服介入
                coordinateHistoryLayout.setVisibility(View.VISIBLE);
                applyKeFuReasonLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("圈助客服");
                bottomButtonLeftTextView.setText("撤销申请");
                if (informationResult.getData().getData().getKefu_status() > 0) {
                    bottomButtonLeftTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_gray));
                    bottomButtonLeftTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
                    bottomButtonLeftTextView.setClickable(false);
                    if (informationResult.getData().getData().getKefu_status() == 1) {
                        coordinateHistoryLayout.setVisibility(View.VISIBLE);
                        deliveryNoLayout.setVisibility(View.VISIBLE);
                        deliveryNoEditText.setFocusableInTouchMode(true);
                        deliveryNoEditText.setFocusable(true);
                        deliveryNoEditText.requestFocus();
                        deliveryNoEditText.setHint("请填写物流单号");
                        deliverySelectLayout.setVisibility(View.VISIBLE);
                        addressLaout.setVisibility(View.VISIBLE);
                        bottomButtonLeftTextView.setText("联系卖家");
                        bottomButtonRightTextView.setText("提交信息");
                    }
                }
                break;
        }
    }

    private AfterSaleServiceInformationResult informationResult;

    private void refundInformation() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_REFUND_INFORMATION);
        params.addBodyParameter("id", String.valueOf(refund_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("refundInformation: " + result);
                informationResult = GsonUtils.GsonToBean(result, AfterSaleServiceInformationResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("refundInformation: " + ex.toString());
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

    private void cancelRefund() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CANCEL_REFUND);
        params.addBodyParameter("id", String.valueOf(refund_id));
        if (null != informationResult && null != informationResult.getData().getData() && informationResult.getData().getData().isIs_kefu()) {
            params.addBodyParameter("type", "1");
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("撤销申请: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("撤销申请: " + ex.toString());
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

    private void postDeliveryNo() {
        String type = null;
        if (informationResult.getData().getData().isIs_kefu()) {
            type = "1";
        } else {
            type = "2";
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ORDER_EDIT_DELIVERY);
        params.addBodyParameter("id", String.valueOf(informationResult.getData().getData().getId()));
        params.addBodyParameter("wl_no", deliveryNoEditText.getText().toString().trim());
        params.addBodyParameter("wl_company", deliveryCompanyCode);
        params.addBodyParameter("type", type);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("填写订单信息: " + result);
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

}
