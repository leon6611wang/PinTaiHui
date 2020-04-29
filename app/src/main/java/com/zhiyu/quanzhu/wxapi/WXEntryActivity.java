package com.zhiyu.quanzhu.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.result.LoginTokenResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.WXUtils;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public int WX_LOGIN = 1;

    private IWXAPI iwxapi;

    private SendAuth.Resp resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        iwxapi = WXAPIFactory.createWXAPI(this, WXUtils.APP_ID, true);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi.handleIntent(getIntent(), this);

    }


    @Override
    public void onReq(BaseReq baseReq) {
    }


    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        System.out.println("------------------------" + baseResp.getType());
        //微信登录为getType为1，分享为0
        if (baseResp.getType() == WX_LOGIN) {
            //登录回调
            System.out.println("------------登陆回调------------");
            resp = (SendAuth.Resp) baseResp;
            System.out.println("------------登陆回调的结果------------：" + new Gson().toJson(resp));
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = String.valueOf(resp.code);
                    //获取用户信息
                    getAccessToken(code);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                    Toast.makeText(WXEntryActivity.this, "用户拒绝授权", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    Toast.makeText(WXEntryActivity.this, "用户取消登录", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        } else {
            //分享成功回调
            System.out.println("------------分享回调------------");
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //分享成功
                    Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //分享取消
                    Toast.makeText(WXEntryActivity.this, "分享取消", Toast.LENGTH_LONG).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //分享拒绝
                    Toast.makeText(WXEntryActivity.this, "分享拒绝", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        finish();
    }

    private void getAccessToken(String code) {
        //获取授权
        String http = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WXUtils.APP_ID + "&secret=" + WXUtils.APP_SERECET + "&code=" + code + "&grant_type=authorization_code";
        RequestParams params = new RequestParams(http);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("微信回调： " + result);
                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("access_token")) {
                        access = jsonObject.getString("access_token");
                    }
                    if (jsonObject.has("openid")) {
                        openId = jsonObject.getString("openid");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != onWxBondListener) {
                    onWxBondListener.onWxBondListener(openId);
                } else {
                    //获取个人信息
                    String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId + "";
                    getWxUserInfo(getUserInfo);
                }

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


//        OkHttpUtils.ResultCallback<String> resultCallback = new OkHttpUtils.ResultCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                String access = null;
//                String openId = null;
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    access = jsonObject.getString("access_token");
//                    openId = jsonObject.getString("openid");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //获取个人信息
//                String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId + "";
//                OkHttpUtils.ResultCallback<WeChatInfo> resultCallback = new OkHttpUtils.ResultCallback<WeChatInfo>() {
//                    @Override
//                    public void onSuccess(WeChatInfo response) {
//                        response.setErrCode(resp.errCode);
//                        Log.i("TAG获取个人信息",new Gson().toJson(response));
////                        Toast.makeText(WXEntryActivity.this, new Gson().toJson(response), Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//                        Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    }
//                };
//                OkHttpUtils.get(getUserInfo, resultCallback);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        };
    }


    private void getWxUserInfo(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String openid = null;
                String nickname = null;
                String headimgurl = null;
                String unionid = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("openid")) {
                        openid = jsonObject.getString("openid");
                    }
                    if (jsonObject.has("nickname")) {
                        nickname = jsonObject.getString("nickname");
                    }
                    if (jsonObject.has("headimgurl")) {
                        headimgurl = jsonObject.getString("headimgurl");
                    }
                    if (jsonObject.has("unionid")) {
                        unionid = jsonObject.getString("unionid");
                    }
                    if (null != onBondWechatAcountListener) {
                        onBondWechatAcountListener.onBondWechatAccount(openid, unionid, nickname, headimgurl);
                    } else {
                        wxLogin(openid, unionid, headimgurl, nickname);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("getWxUserInfo: " + result);
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

    private LoginTokenResult loginTokenResult;

    private void wxLogin(String openid, String unionid, String avatar, String nickname) {
        System.out.println("openid: " + openid + " , unionid: " + unionid + " , avatar: " + avatar + " , nickname: " + nickname);
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.WX_LOGIN);
        params.addBodyParameter("openid", openid);
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("avatar", avatar);
        params.addBodyParameter("nickname", nickname);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("wxlogin: " + result);
                loginTokenResult = GsonUtils.GsonToBean(result, LoginTokenResult.class);
                System.out.println("wxlogin token: " + loginTokenResult.getData().getToken());
                if (loginTokenResult.getCode() == 200) {
                    SharedPreferencesUtils.getInstance(BaseApplication.applicationContext).saveUserToken(loginTokenResult.getData().getToken());
                    if (null != onWxLoginSuccessListener) {
                        onWxLoginSuccessListener.onWxLoginSuccess();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("服务器-微信登录: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private static OnWxLoginSuccessListener onWxLoginSuccessListener;

    public static void setOnWxLoginSuccessListener(OnWxLoginSuccessListener loginSuccessListener) {
        onWxLoginSuccessListener = loginSuccessListener;
    }

    public interface OnWxLoginSuccessListener {
        void onWxLoginSuccess();
    }

    private static OnWxBondListener onWxBondListener;

    public static void setOnWxBondListener(OnWxBondListener listener) {
        onWxBondListener = listener;
    }

    public interface OnWxBondListener {
        void onWxBondListener(String open_id);
    }

    private static OnBondWechatAcountListener onBondWechatAcountListener;

    public static void setOnBondWechatAcountListener(OnBondWechatAcountListener listener) {
        onBondWechatAcountListener = listener;
    }

    public interface OnBondWechatAcountListener {
        void onBondWechatAccount(String openid, String unionid, String nickname, String avatar);
    }
}
