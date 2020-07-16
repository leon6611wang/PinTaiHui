package com.zhiyu.quanzhu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 分享工具类
 */
public class ShareUtils {
    private static final String WX_APP_ID = "wx6353102e13eeeceb";
    private static final String QQ_APP_ID = "101762258";
    private static Tencent mTencent;
    private static ShareUtils utils;
    private static Activity activity;
    /**
     * app,invite,feed,circle,card,goods,shop
     */
    public static final String SHARE_TYPE_FEED="feed";
    public static final String SHARE_TYPE_ARTICLE="article";
    public static final String SHARE_TYPE_VIDEO="video";
    public static final String SHARE_TYPE_INVITE="invite";
    public static final String SHARE_TYPE_SHOP="shop";
    public static final String SHARE_TYPE_GOODS="goods";
    public static final String SHARE_TYPE_CIRCLE="circle";
    public static final String SHARE_TYPE_CARD="card";
    public static final String SHARE_TYPE_APP="app";


    public static ShareUtils getInstance(Activity aty) {
        activity = aty;
        if (null == utils) {
            synchronized (ShareUtils.class) {
                utils = new ShareUtils();
            }
        }
        if (null == mTencent) {
            mTencent = Tencent.createInstance(QQ_APP_ID, activity.getApplicationContext());
        }
        return utils;
    }

    public void shareToQZone() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "Hi,叶应是叶");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "欢迎访问我的博客");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://blog.csdn.net/new_one_object");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.csdn.net/B/0/1/1_new_one_object.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "圈助");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                System.out.println("onComplete: " + o.toString());
            }

            @Override
            public void onError(UiError uiError) {
                System.out.println("onError: " + uiError.toString());
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }
        });
    }


    private IUiListener shareListener;

    public void shareToQQ(String title,String desc,String imageUrl,String webUrl,IUiListener listener) {
        this.shareListener = listener;
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,webUrl);// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);// 网络图片地址　　
        mTencent.shareToQQ(activity, params, shareListener);
    }

    public void setQQShareCallback(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        if (resultCode == Constants.REQUEST_QQ_SHARE) {
            mTencent.handleResultData(data, shareListener);
        }
    }

}
