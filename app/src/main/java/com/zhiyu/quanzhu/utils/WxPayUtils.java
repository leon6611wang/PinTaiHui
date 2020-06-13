package com.zhiyu.quanzhu.utils;

import android.content.Context;

import com.qiniu.android.utils.StringUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class WxPayUtils {
    private static WxPayUtils utils;

    public static WxPayUtils getInstance() {
        if (null == utils) {
            synchronized (WxPayUtils.class) {
                utils = new WxPayUtils();
            }
        }
        return utils;
    }

    public void wxPay(Context context, String json) {
        if (StringUtils.isNullOrEmpty(json)) {
            return;
        }
        String appid = null, partnerid = null, prepayid = null, timestamp = null, noncestr = null, _package = null, sign = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("appid")) {
                appid = jsonObject.getString("appid");
            }
            if (jsonObject.has("partnerid")) {
                partnerid = jsonObject.getString("partnerid");
            }
            if (jsonObject.has("prepayid")) {
                prepayid = jsonObject.getString("prepayid");
            }
            if (jsonObject.has("timestamp")) {
                timestamp = jsonObject.getString("timestamp");
            }
            if (jsonObject.has("noncestr")) {
                noncestr = jsonObject.getString("noncestr");
            }
            if (jsonObject.has("package")) {
                _package = jsonObject.getString("package");
            }
            if (jsonObject.has("sign")) {
                sign = jsonObject.getString("sign");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("appid: "+appid+" , partnerid: "+partnerid+" , prepayid: "+prepayid+" , _package: "+_package
        +" , noncestr: "+noncestr+" , timestamp: "+timestamp+" , sign: "+sign);
        IWXAPI api = WXAPIFactory.createWXAPI(context, WXUtils.APP_ID);
        api.registerApp(WXUtils.APP_ID);
        PayReq payRequest = new PayReq();
        payRequest.appId = appid;
        payRequest.partnerId = partnerid;
        payRequest.prepayId = prepayid;
        payRequest.packageValue = _package;
        payRequest.nonceStr = noncestr;
        payRequest.timeStamp = timestamp;
        payRequest.sign = sign;
        api.sendReq(payRequest);

    }
}
