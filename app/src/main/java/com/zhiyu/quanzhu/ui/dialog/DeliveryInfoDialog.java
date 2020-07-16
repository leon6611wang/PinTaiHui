package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.result.DeliveryInfoResult;
import com.zhiyu.quanzhu.model.result.OrderDeliveryResult;
import com.zhiyu.quanzhu.ui.adapter.DeliveryInfoRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

/**
 * 物流
 */
public class DeliveryInfoDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageView closeImageView;
    private DeliveryInfoRecyclerAdapter adapter;
    private int type=0;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<DeliveryInfoDialog> dialogWeakReference;

        public MyHandler(DeliveryInfoDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            DeliveryInfoDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(dialog.getContext()).show("服务器内部错误");
                    break;
                case 1:
                    if (null != dialog.orderDeliveryResult && null != dialog.orderDeliveryResult.getData() && null != dialog.orderDeliveryResult.getData().getList() &&
                            dialog.orderDeliveryResult.getData().getList().size() > 0) {
                        dialog.adapter.setData(dialog.orderDeliveryResult.getData().getList().get(0).getData());
                    } else {
                        dialog.adapter.setData(null);
                        MessageToast.getInstance(dialog.getContext()).show("暂无物流信息");
                    }

                    break;
            }
        }
    }

    public void setOrderId(int oid) {
        deliveryInfo(oid);
    }

    public DeliveryInfoDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public DeliveryInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public DeliveryInfoDialog(@NonNull Context context, int themeResId,int t) {
        super(context, themeResId);
        this.mContext = context;
        this.type=t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delivery_info);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext) / 20 * 18;
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new DeliveryInfoRecyclerAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                dismiss();
                break;
        }
    }

    private DeliveryInfoResult orderDeliveryResult;

    /**
     * 物流详情
     */
    private void deliveryInfo(int order_id) {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELIVERY_INFO);
        params.addBodyParameter("oid", String.valueOf(order_id));
        params.addBodyParameter("type",String.valueOf(type));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("物流详情: " + result);
                orderDeliveryResult = GsonUtils.GsonToBean(result, DeliveryInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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
