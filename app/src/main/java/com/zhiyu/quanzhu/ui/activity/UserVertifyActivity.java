package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.VertifyResult;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.VertifyBenefitDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 用户实名认证
 */
public class UserVertifyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView, zhengmianTextView, fanmianTextView, vertifyBenefitTextView, confirmTextView;
    private ImageView fanmianImageView, fanmianEgImageView, zhengmianImageView, zhengmianEgImageView;
    private VertifyBenefitDialog vertifyBenefitDialog;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<UserVertifyActivity> activityWeakReference;

        public MyHandler(UserVertifyActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            UserVertifyActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 100:
                    FailureToast.getInstance(activity).show();
                    break;
                case 0:
                    activity.loadingDialog.dismiss();
                    if (activity.vertifyResult.getCode() == 200 && null != activity.vertifyResult.getData()) {
                        if (!StringUtils.isNullOrEmpty(activity.vertifyResult.getData().getIdentity_front())) {
                            activity.zhengmianEgImageView.setVisibility(View.GONE);
                            Glide.with(activity).load(activity.vertifyResult.getData().getIdentity_front()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                    .fallback(R.drawable.image_error).into(activity.zhengmianImageView);
                        }
                        if (!StringUtils.isNullOrEmpty(activity.vertifyResult.getData().getIdentity_back())) {
                            activity.fanmianEgImageView.setVisibility(View.GONE);
                            Glide.with(activity).load(activity.vertifyResult.getData().getIdentity_back()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                    .fallback(R.drawable.image_error).into(activity.fanmianImageView);
                        }
                        activity.zhengMianUrl = activity.vertifyResult.getData().getIdentity_front();
                        activity.fanMianUrl = activity.vertifyResult.getData().getIdentity_back();
                    }
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_vertify);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();

        vertifyDetail();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("实名认证");
        zhengmianTextView = findViewById(R.id.zhengmianTextView);
        zhengmianTextView.setOnClickListener(this);
        fanmianTextView = findViewById(R.id.fanmianTextView);
        fanmianTextView.setOnClickListener(this);
        zhengmianEgImageView = findViewById(R.id.zhengmianEgImageView);
        zhengmianImageView = findViewById(R.id.zhengmianImageView);
        fanmianEgImageView = findViewById(R.id.fanmianEgImageView);
        fanmianImageView = findViewById(R.id.fanmianImageView);
        vertifyBenefitTextView = findViewById(R.id.vertifyBenefitTextView);
        vertifyBenefitTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    private void initDialogs() {
        vertifyBenefitDialog = new VertifyBenefitDialog(this, R.style.dialog);
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zhengmianTextView:
                selectZhengMianImage();
                break;
            case R.id.fanmianTextView:
                selectFanMianImage();
                break;
            case R.id.vertifyBenefitTextView:
                vertifyBenefitDialog.show();
                vertifyBenefitDialog.setUrl(vertifyResult.getData().getUrl());
                break;
            case R.id.confirmTextView:
                if (!StringUtils.isNullOrEmpty(zhengMianUrl) && !StringUtils.isNullOrEmpty(fanMianUrl) && !"null".equals(zhengMianUrl) && !"null".equals(fanMianUrl)) {
                    userVertify();
                } else {
                    MessageToast.getInstance(this).show("身份证正反面必传!");
                }

                break;
        }
    }

    private ArrayList<String> zhengMianList = new ArrayList<>();
    private ArrayList<String> fanMianList = new ArrayList<>();
    private final int ZHENGMIAN_CODE = 10022;
    private final int FANMIAN_CODE = 10023;


    private void selectZhengMianImage() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(zhengMianList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(UserVertifyActivity.this, ZHENGMIAN_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectFanMianImage() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(fanMianList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(UserVertifyActivity.this, FANMIAN_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ZHENGMIAN_CODE && resultCode == RESULT_OK) {
            zhengMianList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            zhengmianEgImageView.setVisibility(View.GONE);
            Glide.with(this).load(zhengMianList.get(0)).into(zhengmianImageView);
            uploadZhengMian();
        } else if (requestCode == FANMIAN_CODE && resultCode == RESULT_OK) {
            fanMianList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            fanmianEgImageView.setVisibility(View.GONE);
            Glide.with(this).load(fanMianList.get(0)).into(fanmianImageView);
            uploadFanMian();
        }
    }

    private String zhengMianUrl = null, fanMianUrl = null;

    private void uploadZhengMian() {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, zhengMianList.get(0), new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                zhengMianUrl = name;
            }
        });
    }

    private void uploadFanMian() {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, fanMianList.get(0), new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                fanMianUrl = name;
            }
        });
    }

    private BaseResult baseResult;

    /**
     * 用户认证
     */
    private void userVertify() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VERTIFY);
        params.addBodyParameter("identity_front", zhengMianUrl);
        params.addBodyParameter("identity_back", fanMianUrl);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("vertify: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(100);
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

    private VertifyResult vertifyResult;

    /**
     * 认证详情
     */
    private void vertifyDetail() {
        loadingDialog.show();
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VERTIFY_DETAIL);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                vertifyResult = GsonUtils.GsonToBean(result, VertifyResult.class);
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
                System.out.println("vertifyDetail: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(100);
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
