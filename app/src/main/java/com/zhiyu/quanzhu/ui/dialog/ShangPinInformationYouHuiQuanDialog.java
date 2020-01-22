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
import android.view.WindowManager;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.GoodsCoupon;
import com.zhiyu.quanzhu.ui.adapter.ShangPinInfoYouHuiQuanRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 商品详情-优惠券
 */
public class ShangPinInformationYouHuiQuanDialog extends Dialog implements ShangPinInfoYouHuiQuanRecyclerAdapter.OnGetCouponListener {
    private Context mContext;
    private float heightRatio = 1.07f;
    private int screenHeight, dialogHeight, dp_10;
    private RecyclerView mRecyclerView;
    private ShangPinInfoYouHuiQuanRecyclerAdapter adapter;
    private List<GoodsCoupon> list;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShangPinInformationYouHuiQuanDialog> dialogWeakReference;

        public MyHandler(ShangPinInformationYouHuiQuanDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            ShangPinInformationYouHuiQuanDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(dialog.getContext(), dialog.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (dialog.baseResult.getCode() == 200) {
                        for (GoodsCoupon coupon : dialog.list) {
                            if (coupon.getId() == dialog.coupon_id) {
                                coupon.setIs_has(true);
                            }
                        }
                        dialog.adapter.setList(dialog.list);
                    }
                    break;
            }
        }
    }

    public ShangPinInformationYouHuiQuanDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        screenHeight = ScreentUtils.getInstance().getScreenWidth(mContext);
        dp_10 = (int) mContext.getResources().getDimension(R.dimen.dp_10);
        dialogHeight = (int) (heightRatio * screenHeight);
    }

    public void setList(List<GoodsCoupon> couponList) {
        this.list = couponList;
        adapter.setList(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shangpin_information_youhuiquan);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = dialogHeight;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ShangPinInfoYouHuiQuanRecyclerAdapter(mContext);
        adapter.setOnGetCouponListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_10);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
    }

    @Override
    public void onGetCoupon(long coupon_id) {
        getCoupon(coupon_id);
    }

    private long coupon_id;
    private BaseResult baseResult;

    private void getCoupon(long coupon_id) {
        this.coupon_id = coupon_id;
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GET_COUPON);
        params.addBodyParameter("coupon_id", String.valueOf(coupon_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("getCoupon: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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
