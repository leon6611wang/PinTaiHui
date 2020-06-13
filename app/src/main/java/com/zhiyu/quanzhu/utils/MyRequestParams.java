package com.zhiyu.quanzhu.utils;

import android.content.Context;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.base.BaseApplication;

import org.xutils.http.RequestParams;

/**
 * http请求头部
 */
public class MyRequestParams {
    private static MyRequestParams params;
    private static String version;
    private static String device_id;
    private static String user_agent;
    private static String user_token;

    public static MyRequestParams getInstance(Context context) {
        if (null == params) {
            synchronized (MyRequestParams.class) {
                params = new MyRequestParams();
            }
        }
        if (null == version) {
            version = AppUtils.getInstance().getVersionName(context);
        }
        if (null == device_id) {
            device_id = AppUtils.getInstance().getUUID(context);
        }
        if (null == user_agent) {
            String xitong = "Android";
            String sys_version = AppUtils.getInstance().getSystemVersion();
            String brand = AppUtils.getInstance().getDeviceBrand() + " " + AppUtils.getInstance().getSystemModel();
            user_agent = xitong + "/" + sys_version + "/" + brand;
        }

        return params;
    }

    public RequestParams getRequestParams(String url) {
        try {
            user_token = SPUtils.getInstance().getUserToken(BaseApplication.applicationContext);
        } catch (Exception e) {
            user_token = null;
        }
        RequestParams params = new RequestParams(url);
        params.addHeader("version", version);
        params.addHeader("os", "android");
        params.addHeader("device", device_id);
        params.addHeader("user_agent", user_agent);
        params.addHeader("Authorization", user_token);
        return params;
    }
}
