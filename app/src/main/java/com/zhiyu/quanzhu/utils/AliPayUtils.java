package com.zhiyu.quanzhu.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

//import com.alipay.sdk.app.PayTask;
import com.zhiyu.quanzhu.model.bean.AliPayResult;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.SuccessToast;

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
                    SuccessToast.getInstance(activity).show("支付成功");
                    break;
                case 2:
                    FailureToast.getInstance(activity).show("支付失败");
                    break;
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


    public void alipay(final Activity activity, final String orderInfo) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
//                PayTask alipay = new PayTask(activity);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                AliPayResult payResult = new AliPayResult(result);
//                String resultStatus = payResult.getResultStatus();
//                // 判断resultStatus 为9000则代表支付成功
//                if (TextUtils.equals(resultStatus, "9000")) {/*支付成功*/
////                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
//                    Message msg = myHandler.obtainMessage(1);
//                    msg.sendToTarget();
//                } else {/*支付失败*/
////                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
//                    Message msg = myHandler.obtainMessage(2);
//                    msg.sendToTarget();
//                }
            }
        });
    }

}
