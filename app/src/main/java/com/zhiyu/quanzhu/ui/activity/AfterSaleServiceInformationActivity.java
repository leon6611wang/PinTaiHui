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
import java.util.List;

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
            applyTimeLayout, refundNoLayout, midButtonLayout, midButtonLayout2;
    private MyRecyclerView mRecyclerView;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private ArrayList<String> mImageList = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private DeliveryCompanyDialog deliveryCompanyDialog;
    private YNDialog ynDialog;
    private int refundType, refundStatus;
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
                        Glide.with(activity).load(activity.informationResult.getData().getData().getGoods_img()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.goodsImageImageView);
                        activity.goodsNameTextView.setText(activity.informationResult.getData().getData().getGoods_name());
//                        activity.goodsNormsTextView.setText(activity.informationResult.getData().getData().get);
                        activity.goodsCountTextView.setText("X" + activity.informationResult.getData().getData().getGoods_num());
                        activity.reasonTextView.setText(activity.informationResult.getData().getData().getReason());
                        activity.refundNoTextView.setText(activity.informationResult.getData().getData().getRefund_sn());
                        activity.applyTimeTextView.setText(activity.informationResult.getData().getData().getAddtime());
                        activity.refundPriceTextView.setText("￥" + PriceParseUtils.getInstance().parsePrice(activity.informationResult.getData().getData().getRefund_price()));


                        if (!StringUtils.isNullOrEmpty(activity.informationResult.getData().getData().getRemark())) {
                            activity.remarkTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.remarkTextView.setVisibility(View.GONE);
                        }
                        activity.remarkTextView.setText(activity.informationResult.getData().getData().getRemark());
                        if (null != activity.informationResult.getData().getData().getImg() && activity.informationResult.getData().getData().getImg().size() > 0) {
                            for (UploadImage img : activity.informationResult.getData().getData().getImg()) {
                                activity.mImageList.add(img.getFile());
                            }
                        }
                        activity.imageGridAdapter.setData(activity.mImageList);

                    }
                    break;
                case 2://撤销申请
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
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
        initDialogs();
        initViews();
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
        midButtonLayout2 = findViewById(R.id.midButtonLayout2);
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

                break;
            case R.id.bottomButtonRightTextView:
                switch (refundStatus) {
                    case ServiceUtils.STATUS_JIHUIZHONG://返回首页
                    case ServiceUtils.STATUS_WANCHENG://返回首页
                        goHome();
                        break;
                    case ServiceUtils.STATUS_TONGGUO://提交信息

                        break;
                    case ServiceUtils.STATUS_DAICHULI://拨打客服
                    case ServiceUtils.STATUS_YONGHUGUANBI://拨打客服
                    case ServiceUtils.STATUS_WEITONGGUO://拨打客服

                        break;
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
                afterSaleServiceIntent.putExtra("isUpdate", 1);
                afterSaleServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(afterSaleServiceIntent);
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
    }

    private void serviceStatusChangeViews() {
//        refundStatus = 5;
        deliveryNoLayout.setVisibility(View.GONE);
        deliverySelectLayout.setVisibility(View.GONE);
        deliveryStatusLayout.setVisibility(View.GONE);
        reasonLayout.setVisibility(View.VISIBLE);
        refundPriceLayout.setVisibility(View.VISIBLE);
        applyTimeLayout.setVisibility(View.VISIBLE);
        refundNoLayout.setVisibility(View.VISIBLE);
        midButtonLayout.setVisibility(View.GONE);
        midButtonLayout2.setVisibility(View.GONE);
        switch (refundStatus) {
            case ServiceUtils.STATUS_DAICHULI:
                midButtonLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("拨打客服");
                break;
            case ServiceUtils.STATUS_WEITONGGUO:
                midButtonLayout2.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("拨打客服");
                break;
            case ServiceUtils.STATUS_TONGGUO:
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusableInTouchMode(true);
                deliveryNoEditText.setFocusable(true);
                deliveryNoEditText.requestFocus();
                deliveryNoEditText.setHint("请填写物流单号");
                deliverySelectLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("提交信息");
                break;
            case ServiceUtils.STATUS_YONGHUGUANBI:
                bottomButtonRightTextView.setText("拨打客服");
                break;
            case ServiceUtils.STATUS_WANCHENG:
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusable(false);
                deliveryNoEditText.setFocusableInTouchMode(false);
                deliveryNoEditText.setHint("物流单号");
                deliveryStatusLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("返回首页");
                break;
            case ServiceUtils.STATUS_JIHUIZHONG:
                deliveryNoLayout.setVisibility(View.VISIBLE);
                deliveryNoEditText.setFocusable(false);
                deliveryNoEditText.setFocusableInTouchMode(false);
                deliveryNoEditText.setHint("物流单号");
                deliveryStatusLayout.setVisibility(View.VISIBLE);
                bottomButtonRightTextView.setText("返回首页");
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
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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
