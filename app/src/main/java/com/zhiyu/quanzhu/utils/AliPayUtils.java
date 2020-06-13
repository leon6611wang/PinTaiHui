package com.zhiyu.quanzhu.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

//import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.app.PayTask;
import com.zhiyu.quanzhu.model.bean.AliPayResult;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.toast.SuccessToast;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 支付宝支付工具类
 */
public class AliPayUtils {
    private static AliPayUtils utils;
    private MyHandler myHandler = new MyHandler(this);
    private static Activity activity;

    private static class MyHandler extends Handler {
        WeakReference<AliPayUtils> utilsWeakReference;

        public MyHandler(AliPayUtils utils) {
            utilsWeakReference = new WeakReference<>(utils);
        }

        @Override
        public void handleMessage(Message msg) {
            AliPayUtils utils = utilsWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show("支付宝支付成功");
                    break;
                case 2:
                    MessageToast.getInstance(activity).show("支付宝支付失败");
                    break;
            }
            if(null!=utils.onAlipayCallbackListener){
                utils.onAlipayCallbackListener.onAlipayCallBack();
            }
        }
    }

    public static AliPayUtils getInstance(Activity aty) {
        if (null == utils) {
            synchronized (AliPayUtils.class) {
                utils = new AliPayUtils();
            }
        }
        activity = aty;
        return utils;
    }


    public void aliPay(final Activity activity, final String orderInfo) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                AliPayResult payResult = new AliPayResult(result);
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {/*支付成功*/
//                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    Message msg = myHandler.obtainMessage(1);
                    msg.sendToTarget();
                } else {/*支付失败*/
//                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    Message msg = myHandler.obtainMessage(2);
                    msg.sendToTarget();
                }
            }
        });
    }

    private OnAlipayCallbackListener onAlipayCallbackListener;
    public void setOnAlipayCallbackListener(OnAlipayCallbackListener listener){
        this.onAlipayCallbackListener=listener;
    }
    public interface OnAlipayCallbackListener{
        void onAlipayCallBack();
    }

}
