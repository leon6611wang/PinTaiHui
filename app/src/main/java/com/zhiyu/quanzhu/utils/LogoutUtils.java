package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.leon.chic.utils.LogUtils;
import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.activity.LoginGetVertifyCodeActivity;
import com.zhiyu.quanzhu.ui.activity.LoginInputVertifyCodeActivity;
import com.zhiyu.quanzhu.ui.toast.MessageToast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

import io.rong.imlib.RongIMClient;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class LogoutUtils {
    private static LogoutUtils utils;

    public static LogoutUtils getInstance() {
        if (null == utils) {
            synchronized (LogoutUtils.class) {
                utils = new LogoutUtils();
                context = BaseApplication.applicationContext;
            }
        }
        return utils;
    }


    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<LogoutUtils> weakReference;

        public MyHandler(LogoutUtils utils) {
            weakReference = new WeakReference<>(utils);
        }

        @Override
        public void handleMessage(Message msg) {
            LogoutUtils utils = weakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == utils.baseResult.getCode()) {
                        utils.logoutSP();
                    } else {
                        MessageToast.getInstance(context).show(utils.baseResult.getMsg());
                    }
                    break;
                case 99:
                    MessageToast.getInstance(context).show("服务器内部错误，退出登录失败.");
                    break;
            }
        }
    }


    private static Context context;

    public void logout() {
        LogUtils.getInstance().show("leon", "token过期");
        logoutSP();
    }

    private void logoutSP() {
        if (!isShow) {
            SPUtils.getInstance().userLogout(BaseApplication.applicationContext);
            RongIMClient.getInstance().logout();
            Intent loginIntent = new Intent(context, LoginGetVertifyCodeActivity.class);
//            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(loginIntent);
        }
    }

    private static boolean isShow;


    private BaseResult baseResult;

    private void logoutService() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.LOGOUT);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
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
