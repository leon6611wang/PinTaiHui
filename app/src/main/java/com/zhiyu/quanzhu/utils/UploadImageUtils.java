package com.zhiyu.quanzhu.utils;

import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zhiyu.quanzhu.model.result.QiNiuTokenResult;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 七牛图片上传
 */
public class UploadImageUtils {
    //头像
    public static final String AVATAR = "avatar";
    //圈子
    public static final String CIRCLE = "circle";
    //商店
    public static final String SHOP = "shop";
    //动态
    public static final String CIRCLEFEES = "circlefees";
    //名片
    public static final String CARDS = "cards";
    //商品
    public static final String GOODS = "goods";
    //投诉
    public static final String REPORT = "report";
    private static UploadImageUtils utils;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public static UploadImageUtils getInstance() {
        if (null == utils) {
            synchronized (UploadImageUtils.class) {
                utils = new UploadImageUtils();
            }
        }
        return utils;
    }

    public void uploadFile(final String imageType, final String path, final OnUploadCallback onUploadCallback) {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.QI_NIU_TOKEN);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                qiNiuTokenResult = GsonUtils.GsonToBean(result, QiNiuTokenResult.class);
                upload(imageType, path, onUploadCallback);
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

    private void upload(String imageType, String path, final OnUploadCallback onUploadCallback) {
        qiniuToken(path);
        Configuration config = new Configuration.Builder()
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone0)        // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        UploadManager uploadManager = new UploadManager(config);
        String key = parsePath2Key(imageType, path);
        String token = qiNiuTokenResult.getData().getToken();
        uploadManager.put(path, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "七牛上传成功");
                            if (null != onUploadCallback) {
                                onUploadCallback.onUploadSuccess(qiNiuTokenResult.getData().getDomain() + key);
                            }
                        } else {
                            Log.i("qiniu", "七牛上传失败");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

    private QiNiuTokenResult qiNiuTokenResult;

    private void qiniuToken(String path) {
        String type = parsePath2Type(path);
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.QI_NIU_TOKEN);
        params.addBodyParameter("type", type);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("七牛token "+result);
                qiNiuTokenResult = GsonUtils.GsonToBean(result, QiNiuTokenResult.class);
//                Log.i("qiniu", qiNiuTokenResult.getData().getToken());
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

    private String parsePath2Key(String imageType, String path) {
        String key = "";
        key += imageType + "/" + sdf.format(new Date().getTime()) + "/" + new Date().getTime();
        if (path.endsWith(".jpg") || path.endsWith(".JPG")) {
            key += ".jpg";
        } else if (path.endsWith(".jpeg") || path.endsWith(".JPEG")) {
            key += ".jpeg";
        } else if (path.endsWith(".png") || path.endsWith(".PNG")) {
            key += ".png";
        } else if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            key += ".gif";
        } else {
            int lastIndex = path.lastIndexOf(".");
            String p = path.substring(lastIndex);
            key += p;
        }
        return key.trim();
    }

    private String parsePath2Type(String path) {
        int type = 0;//1.图片，2.视频
        if (path.endsWith(".jpg") || path.endsWith(".JPG") ||
                path.endsWith(".jpeg") || path.endsWith(".JPEG") ||
                path.endsWith(".png") || path.endsWith(".PNG") ||
                path.endsWith(".gif") || path.endsWith(".GIF")) {
            type = 1;
        } else {
            type = 2;
        }
        return String.valueOf(type);
    }


    public interface OnUploadCallback {
        void onUploadSuccess(String name);
    }

}
