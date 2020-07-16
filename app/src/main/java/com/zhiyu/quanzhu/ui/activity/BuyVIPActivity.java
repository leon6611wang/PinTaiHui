package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.BuyVipIndex;
import com.zhiyu.quanzhu.model.result.AlipayOrderInfo;
import com.zhiyu.quanzhu.model.result.VIPResult;
import com.zhiyu.quanzhu.model.result.VipDetailResult;
import com.zhiyu.quanzhu.model.result.WxpayOrderInfo;
import com.zhiyu.quanzhu.ui.adapter.BuyVipIndexListAdapter;
import com.zhiyu.quanzhu.ui.adapter.BuyVipRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.AliPayUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;
import com.zhiyu.quanzhu.utils.WxPayUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 购买会员VIP
 */
public class BuyVIPActivity extends BaseActivity implements View.OnClickListener, BuyVipRecyclerAdapter.OnBuyVIPListener,
        AliPayUtils.OnAlipayCallbackListener, WXEntryActivity.OnWxpayCallbackListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private CircleImageView vipAvatarImageView;
    private TextView vipTitleTextView, vipLevelTextView, vipTimeTextView;
    private RecyclerView mRecyclerView;
    private BuyVipRecyclerAdapter adapter;
    private RecyclerView indexListView;
    private BuyVipIndexListAdapter indexAdapter;
    private List<BuyVipIndex> indexList = new ArrayList<>();
    private PayWayDialog payWayDialog;
    private PasswordCheckDialog passwordCheckDialog;
    private TextView xieyiTextView;
    private float widthRatio = 0.6666667f;
    private float heightRatio = 1.74f;
    private int screenWidth, cardWidth, cardHeight;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<BuyVIPActivity> activityWeakReference;

        public MyHandler(BuyVIPActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BuyVIPActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.vipDetailResult.getCode() == 200) {
                        Glide.with(activity).load(activity.vipDetailResult.getData().getAvatar()).error(R.drawable.image_error).into(activity.vipAvatarImageView);
                        activity.vipTitleTextView.setText(activity.vipDetailResult.getData().getTitle());
                        activity.vipLevelTextView.setText(StringUtils.isNullOrEmpty(activity.vipDetailResult.getData().getLevel()) ? "普通会员" : activity.vipDetailResult.getData().getLevel());
                        if (StringUtils.isNullOrEmpty(activity.vipDetailResult.getData().getEnd_time())) {
                            activity.vipTimeTextView.setVisibility(View.GONE);
                        } else {
                            activity.vipTimeTextView.setVisibility(View.VISIBLE);
                            activity.vipTimeTextView.setText("到期时间 " + activity.vipDetailResult.getData().getEnd_time());
                        }
                    }
                    break;
                case 2:
                    activity.adapter.setList(activity.vipResult.getData().getList());
                    if (null != activity.vipResult.getData().getList() && activity.vipResult.getData().getList().size() > 0) {
                        for (int i = 0; i < activity.vipResult.getData().getList().size(); i++) {
                            activity.indexList.add(new BuyVipIndex(i == 0 ? true : false));
                        }
                    }
                    activity.indexAdapter.setList(activity.indexList);
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
                        activity.vipDetail();
                        activity.vipList();
                        if (null != onBuyVIPSuccessListener) {
                            onBuyVIPSuccessListener.onBuyVIPSuccess();
                        }
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
        setContentView(R.layout.activity_buy_vip);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        AliPayUtils.getInstance(this).setOnAlipayCallbackListener(this);
        WXEntryActivity.setOnWxpayCallbackListener(this);
        initDatas();
        initDialogs();
        initViews();
        vipDetail();
        vipList();
    }

    private void initDatas() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        cardWidth = Math.round(screenWidth * widthRatio);
        cardHeight = Math.round(cardWidth * heightRatio);
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

    private int balancePayType;
    private int buyPosition;

    @Override
    public void onBuyVIP(int position) {
        this.buyPosition = position;
        payWayDialog.show();
        payWayDialog.setMoney(vipResult.getData().getList().get(buyPosition).getPrice());
    }

    @Override
    public void onAlipayCallBack() {
        if (null != onBuyVIPSuccessListener) {
            onBuyVIPSuccessListener.onBuyVIPSuccess();
        }
        vipDetail();
        vipList();
    }

    @Override
    public void onWxpayCallback() {
        if (null != onBuyVIPSuccessListener) {
            onBuyVIPSuccessListener.onBuyVIPSuccess();
        }
        vipDetail();
        vipList();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("VIP中心");
        vipAvatarImageView = findViewById(R.id.vipAvatarImageView);
        vipTitleTextView = findViewById(R.id.vipTitleTextView);
        vipLevelTextView = findViewById(R.id.vipLevelTextView);
        vipTimeTextView = findViewById(R.id.vipTimeTextView);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(lm);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
        SpaceItemDecoration decoration = new SpaceItemDecoration((int) getResources().getDimension(R.dimen.dp_20));
        mRecyclerView.addItemDecoration(decoration);
        adapter = new BuyVipRecyclerAdapter(this, cardWidth, cardHeight);
        adapter.setOnBuyVIPListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                disX -= dx;
                scaleRecyclerViewItem();
            }
        });
        indexListView = findViewById(R.id.indexListView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        indexListView.setLayoutManager(linearLayoutManager);
        indexAdapter = new BuyVipIndexListAdapter(this);
        indexListView.setAdapter(indexAdapter);
        xieyiTextView=findViewById(R.id.xieyiTextView);
        xieyiTextView.setOnClickListener(this);
    }

    private int disX, currentX;
    private float mScale = 0.75f;
    private boolean isInitScale = true;

    private void scaleRecyclerViewItem() {
        int count = adapter.getItemCount();
        if (count > 0) {
            View currentView = null, leftView = null, rightView = null;
            int currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(1).getLayoutParams()).getViewAdapterPosition();
            int indexPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
            if (indexList.size() > 0) {
                for (BuyVipIndex index : indexList) {
                    index.setCurrent(false);
                }
            }
            indexList.get(indexPosition).setCurrent(true);
            indexAdapter.setList(indexList);
            currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            currentView.setScaleY(1.0f);
            if (currentPosition > 0 && currentPosition < count) {
                leftView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition - 1);
                rightView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition + 1);
            }

            if (isInitScale) {
                if (null != leftView) {
                    leftView.setScaleY(mScale);
                }
                if (null != rightView) {
                    rightView.setScaleY(mScale);
                }
                isInitScale = false;
            }
            currentX = (int) ((currentPosition) * (cardWidth + getResources().getDimension(R.dimen.dp_20)));
            float offset = (Math.abs(disX) - currentX);
            float layout_width = (cardWidth + getResources().getDimension(R.dimen.dp_20));
            float percent = (float) Math.max(Math.abs(offset) * 1.0 / layout_width, 0.0001);
            if (leftView != null) {
                float left_scale = (1 - mScale) * percent + mScale;
                if (left_scale > 1.0f) {
                    left_scale = 1.0f;
                }
                if (left_scale > mScale * 1.1f)
                    leftView.setScaleY(left_scale);
            }
            if (currentView != null) {
                float current_scale = (mScale - 1) * percent + 1;
                current_scale = current_scale * 1.1f;//后面为校正系数
                if (current_scale > 1.0f) {
                    current_scale = 1.0f;
                }
                currentView.setScaleY(current_scale);
            }
            if (rightView != null) {
                float right_scale = (1 - mScale) * percent + mScale;
                if (right_scale > 1.0f) {
                    right_scale = 1.0f;
                }
                if (right_scale > mScale * 1.1f)
                    rightView.setScaleY(right_scale);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.xieyiTextView:
                Intent intent=new Intent(this,H5PageActivity.class);
                intent.putExtra("url","http://h5.pintaihui.test/#/vip");
                startActivity(intent);
                break;
        }
    }


    private VipDetailResult vipDetailResult;

    private void vipDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VIP_DETAIL);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("vipDetail: " + result);
                vipDetailResult = GsonUtils.GsonToBean(result, VipDetailResult.class);
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

    private VIPResult vipResult;

    private void vipList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VIP_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("vipList: " + result);
                vipResult = GsonUtils.GsonToBean(result, VIPResult.class);
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

    private AlipayOrderInfo alipayOrderInfo;

    private void alipayRequest() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VIP_ALIPAY);
        params.addBodyParameter("id", String.valueOf(vipResult.getData().getList().get(buyPosition).getId()));
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
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VIP_WXPAY);
        params.addBodyParameter("id", String.valueOf(vipResult.getData().getList().get(buyPosition).getId()));
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


    private BaseResult baseResult;

    private void balancePay(String pwd) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VIP_BALANCE_PAY);
        params.addBodyParameter("id", String.valueOf(vipResult.getData().getList().get(buyPosition).getId()));
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("type", balancePayType == 1 ? "wechat" : "ali");//ali,wechat
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

    private static OnBuyVIPSuccessListener onBuyVIPSuccessListener;

    public static void setOnBuyVIPSuccessListener(OnBuyVIPSuccessListener listener) {
        onBuyVIPSuccessListener = listener;
    }

    public interface OnBuyVIPSuccessListener {
        void onBuyVIPSuccess();
    }
}
