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
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.GoodsCoupon;
import com.zhiyu.quanzhu.model.result.ShopCouponResult;
import com.zhiyu.quanzhu.ui.adapter.GoodsCouponsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
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
public class GoodsCouponsDialog extends Dialog implements GoodsCouponsRecyclerAdapter.OnGetCouponListener, View.OnClickListener {
    private Context mContext;
    private TextView confirmTextView;
    private float heightRatio = 1.07f;
    private int screenHeight, dialogHeight, dp_10;
    private RecyclerView mRecyclerView;
    private GoodsCouponsRecyclerAdapter adapter;
    private List<GoodsCoupon> list;
    private int shop_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<GoodsCouponsDialog> dialogWeakReference;

        public MyHandler(GoodsCouponsDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsCouponsDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (dialog.shopCouponResult.getCode() == 200) {
                        dialog.adapter.setList(dialog.list);
                    }
                    break;
                case 1:
                    MessageToast.getInstance(dialog.getContext()).show(dialog.baseResult.getMsg());
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

    public GoodsCouponsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        screenHeight = ScreentUtils.getInstance().getScreenWidth(mContext);
        dp_10 = (int) mContext.getResources().getDimension(R.dimen.dp_10);
        dialogHeight = (int) (heightRatio * screenHeight);
    }

    public void setShopId(int shopId) {
        this.shop_id = shopId;
        if (null == list || list.size() == 0) {
            shopCoupons();
        }
    }

    public void setList(List<GoodsCoupon> couponList) {
        this.list = couponList;
        adapter.setList(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goods_coupons);
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
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new GoodsCouponsRecyclerAdapter(mContext);
        adapter.setOnGetCouponListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_10);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                dismiss();
                break;
        }
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
//                System.out.println("getCoupon: " + result);
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

    private ShopCouponResult shopCouponResult;

    private void shopCoupons() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_COUPONS);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("coupons: " + result);
                shopCouponResult = GsonUtils.GsonToBean(result, ShopCouponResult.class);
                list = shopCouponResult.getData().getCoupon();
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("coupons: " + ex.toString());
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
