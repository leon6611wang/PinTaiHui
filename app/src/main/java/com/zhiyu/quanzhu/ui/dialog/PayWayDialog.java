package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.result.PurseResult;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;


/**
 * 支付方式
 */
public class PayWayDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LinearLayout wechatlaout, alipaylayout, wxpayBalancelayout, alipayBalancelayout;
    private View wechatview, alipayview;
    private TextView wxpayBalancetextview, alipayBalancetextview;
    private TextView confirmTextView;
    private View wxpayBalanceview, alipayBalanceview;
    private int selectPayWay = 1;
    private String selectPayWayStr = "微信支付";
    private int all_price, ali_banlance, wx_banlance;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PayWayDialog> weakReference;

        public MyHandler(PayWayDialog dialog) {
            weakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            PayWayDialog dialog = weakReference.get();
            switch (msg.what) {
                case 0:
                    dialog.initBalanceMoney();
                    break;
            }
        }
    }

    public PayWayDialog(@NonNull Context context, int themeResId, OnPayWayListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onPayWayListener = listener;
    }

    public void setMoney(int allprice) {
        this.all_price = allprice;
        confirmTextView.setText("支付 ¥" + PriceParseUtils.getInstance().parsePrice(all_price));
    }


    private void initBalanceMoney() {
        if (null != purseResult && null != purseResult.getData()) {
            wx_banlance = (int) purseResult.getData().getWechat_money();
            ali_banlance = (int) purseResult.getData().getAli_money();
        }
        wxpayBalancetextview.setText("剩余" + PriceParseUtils.getInstance().parsePrice(wx_banlance));
        alipayBalancetextview.setText("剩余" + PriceParseUtils.getInstance().parsePrice(ali_banlance));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payway);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        purseInformation();
    }


    private void initViews() {
        wechatlaout = findViewById(R.id.wechatlayout);
        wechatlaout.setOnClickListener(this);
        alipaylayout = findViewById(R.id.alipaylayout);
        alipaylayout.setOnClickListener(this);
        wxpayBalancelayout = findViewById(R.id.wxpayBalancelayout);
        wxpayBalancelayout.setOnClickListener(this);
        alipayBalancelayout = findViewById(R.id.alipayBalancelayout);
        alipayBalancelayout.setOnClickListener(this);
        wechatview = findViewById(R.id.wechatview);
        alipayview = findViewById(R.id.alipayview);
        wxpayBalancetextview = findViewById(R.id.wxpayBalancetextview);
        alipayBalancetextview = findViewById(R.id.alipayBalancetextview);
        wxpayBalanceview = findViewById(R.id.wxpayBalanceview);
        alipayBalanceview = findViewById(R.id.alipayBalanceview);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        wayChange(1);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        wayChange(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechatlayout:
                wayChange(1);
                selectPayWay = 1;
                selectPayWayStr = "微信支付";
                break;
            case R.id.alipaylayout:
                wayChange(2);
                selectPayWay = 2;
                selectPayWayStr = "支付宝支付";
                break;
            case R.id.wxpayBalancelayout:
                if (wx_banlance >= all_price) {
                    wayChange(3);
                    selectPayWay = 3;
                    selectPayWayStr = "微信余额支付";
                } else {
                    MessageToast.getInstance(getContext()).show("余额不足，无法支付.");
                }

                break;
            case R.id.alipayBalancelayout:
                if (ali_banlance >= all_price) {
                    wayChange(4);
                    selectPayWay = 4;
                    selectPayWayStr = "支付宝余额支付";
                } else {
                    MessageToast.getInstance(getContext()).show("余额不足，无法支付.");
                }

                break;
            case R.id.confirmTextView:
                if (null != onPayWayListener) {
                    onPayWayListener.onPayWay(selectPayWay, selectPayWayStr);
                }
                break;
        }
    }

    private void wayChange(int position) {
        wechatview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        alipayview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        wxpayBalanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        alipayBalanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        switch (position) {
            case 1:
                wechatview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 2:
                alipayview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 3:
                wxpayBalanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 4:
                alipayBalanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
        }
    }

    private OnPayWayListener onPayWayListener;

    public interface OnPayWayListener {
        void onPayWay(int payWay, String payWayStr);
    }

    private PurseResult purseResult;

    //钱包详情
    private void purseInformation() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_PURSE_INFORMATION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                purseResult = GsonUtils.GsonToBean(result, PurseResult.class);
                Message message = myHandler.obtainMessage(0);
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
