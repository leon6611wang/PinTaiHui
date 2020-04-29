package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.DeliveryCompany;
import com.zhiyu.quanzhu.model.result.DeliveryCompanyResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DeliveryCompanyDialog extends Dialog implements View.OnClickListener {
    private LoopView mLoopView;
    private List<String> list = new ArrayList<>();
    private String deliveryCompany;
    private TextView cancelTextView, confirmTextView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<DeliveryCompanyDialog> dialogWeakReference;

        public MyHandler(DeliveryCompanyDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            DeliveryCompanyDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == dialog.deliveryCompanyResult.getCode()) {
                        dialog.initData();
                    }
                    break;
            }
        }
    }

    public DeliveryCompanyDialog(@NonNull Context context, int themeResId, OnDeliveryCompanyListener listener) {
        super(context, themeResId);
        this.onDeliveryCompanyListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delivery_company);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        deliveryCompanyList();
    }

    private void initData() {
        if (null != companyList && companyList.size() > 0) {
            for (DeliveryCompany company : companyList) {
                list.add(company.getName());
            }
        }
        deliveryCompany = list.get(0);
        mLoopView.setItems(list);
    }

    private void initViews() {
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mLoopView = findViewById(R.id.mLoopView);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                deliveryCompany = list.get(index);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onDeliveryCompanyListener) {
                    onDeliveryCompanyListener.onDeliveryCompany(companyList.get(mLoopView.getSelectedItem()));
                }
                dismiss();
                break;
        }
    }

    private OnDeliveryCompanyListener onDeliveryCompanyListener;
    private List<DeliveryCompany> companyList;

    public interface OnDeliveryCompanyListener {
        void onDeliveryCompany(DeliveryCompany deliveryCompany);
    }

    private DeliveryCompanyResult deliveryCompanyResult;

    private void deliveryCompanyList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELIVERY_COMPANY_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("快递公司: " + result);
                deliveryCompanyResult = GsonUtils.GsonToBean(result, DeliveryCompanyResult.class);
                companyList = deliveryCompanyResult.getData().getList();
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

}
