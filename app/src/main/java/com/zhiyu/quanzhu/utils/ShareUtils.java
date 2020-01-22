package com.zhiyu.quanzhu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * 分享工具类
 */
public class ShareUtils {
    private static final String WX_APP_ID = "wx6353102e13eeeceb";
    private static final String QQ_APP_ID = "101762258";
    private static Tencent mTencent;
    private static ShareUtils utils;
    private static Activity activity;

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


    public void shareToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "俄奥委会:拒接受世界反兴奋剂机构禁赛处罚 将上诉");// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "当地时间12月24日，俄罗斯奥委会主席波兹德尼亚科夫和残奥委会主席卢金在新闻发布会上表示，俄罗斯奥委会执委会全会通过决议，支持俄罗斯反兴奋剂机构观察委员会19日所提出的，拒绝世界反兴奋剂机构对俄禁赛处罚决定的建议。随后，俄罗斯反兴奋剂机构全会审议并通过了上述决议。");// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://news.163.com/19/1224/23/F16T9GP60001899O.html");// 内容地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://cms-bucket.ws.126.net/2019/1224/2a2726d3j00q30woq0039c000ug00msc.jpg");// 网络图片地址　　
        mTencent.shareToQQ(activity, params, null);
    }
}
