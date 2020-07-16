package com.zhiyu.quanzhu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.result.CircleInfoResult;
import com.zhiyu.quanzhu.ui.dialog.CircleBannerSortDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyBoardListener;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CircleSettingActivity extends BaseActivity implements View.OnClickListener, SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private CardView jiesanCardView;
    private LinearLayout videoLayout, bannerLayout, coverLayout, logoLayout,
            profileLayout, historyLayout, searchHistoryLayout, moneyLayout;
    private SwitchButton shenheSwitchButton, moneySwitchButton, topSwitchButton, silenceSwitchButton;
    private EditText moneyEditText;
    private CircleImageView logoImageView;
    private ImageView coverImageView, bannerImageView, videoImageView;
    private String moneyStr;
    private long circle_id;
    private CircleBannerSortDialog circleBannerSortDialog;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleSettingActivity> activityWeakReference;

        public MyHandler(CircleSettingActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleSettingActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    if (200 == activity.circleInfoResult.getCode()) {
                        activity.initCircleDatasViews();
                    } else {
                        MessageToast.getInstance(activity).show(activity.circleInfoResult.getMsg());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        Intent intent = new Intent();
                        intent.putExtra("jiesan", true);
                        activity.setResult(10071, intent);
                        if (null != onRemoveCircleListener) {
                            onRemoveCircleListener.onRemoveCircle();
                        }
                        activity.finish();
                    }
                    break;
                case 3:
                    activity.loadingDialog.dismiss();
                    Glide.with(activity).load(activity.video_url).error(R.drawable.image_error).into(activity.videoImageView);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        initViews();
        initDialogs();
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(this);
    }

    @Override
    public void keyBoardShow(int height) {
        System.out.println("show");
    }

    @Override
    public void keyBoardHide(int height) {
        String jiage = moneyEditText.getText().toString().trim();
        price = Integer.parseInt(jiage) * 100;
        updateCircle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleBase();
    }

    private void initDialogs() {
        circleBannerSortDialog = new CircleBannerSortDialog(this, this, R.style.dialog);
        circleBannerSortDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                circleBase();
            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        jiesanCardView = findViewById(R.id.jiesanCardView);
        jiesanCardView.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("圈子设置");
        videoLayout = findViewById(R.id.videoLayout);
        videoLayout.setOnClickListener(this);
        bannerLayout = findViewById(R.id.bannerLayout);
        bannerLayout.setOnClickListener(this);
        coverLayout = findViewById(R.id.coverLayout);
        coverLayout.setOnClickListener(this);
        logoLayout = findViewById(R.id.logoLayout);
        logoLayout.setOnClickListener(this);
        shenheSwitchButton = findViewById(R.id.shenheSwitchButton);
        shenheSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_verify = isOpen ? 1 : 0;
                if (isFirt) {
                    isFirt = false;
                } else {
                    updateCircle();
                }

            }
        });
        moneyEditText = findViewById(R.id.moneyEditText);
        moneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                moneyStr = moneyEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        moneySwitchButton = findViewById(R.id.moneySwitchButton);
        moneySwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {

            }
        });
        profileLayout = findViewById(R.id.profileLayout);
        profileLayout.setOnClickListener(this);
        historyLayout = findViewById(R.id.historyLayout);
        historyLayout.setOnClickListener(this);
        moneyLayout = findViewById(R.id.moneyLayout);
        topSwitchButton = findViewById(R.id.topSwitchButton);
        topSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {

            }
        });
        silenceSwitchButton = findViewById(R.id.silenceSwitchButton);
        silenceSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {

            }
        });
        searchHistoryLayout = findViewById(R.id.searchHistoryLayout);
        searchHistoryLayout.setOnClickListener(this);
        logoImageView = findViewById(R.id.logoImageView);
        coverImageView = findViewById(R.id.coverImageView);
        bannerImageView = findViewById(R.id.bannerImageView);
        videoImageView = findViewById(R.id.videoImageView);
    }

    private boolean isFirt = true;
    private int price = 0;

    private void initCircleDatasViews() {
        is_verify = circleInfoResult.getData().isIs_verify() ? 1 : 0;
        switch (is_verify) {
            case 0:
                shenheSwitchButton.close();
                break;
            case 1:
                shenheSwitchButton.open();
                break;
        }
        if (circleInfoResult.getData().getIs_price() == 1) {
            price = circleInfoResult.getData().getPrice();
            String floatPrice = String.valueOf(circleInfoResult.getData().getPrice() / 100);
            moneyEditText.setText(floatPrice);
            moneyEditText.setSelection(floatPrice.length());
            moneyLayout.setVisibility(View.VISIBLE);
        } else {
            moneyLayout.setVisibility(View.GONE);
        }
//        Glide.with(this).load(circleInfoResult.getData().getLogo()).error(R.drawable.image_error).into(logoImageView);
//        Glide.with(this).load(circleInfoResult.getData().getThumb()).error(R.drawable.image_error).into(coverImageView);
        if (null != circleInfoResult && null != circleInfoResult.getData() && null != circleInfoResult.getData().getImgs() && circleInfoResult.getData().getImgs().size() > 0)
            Glide.with(this).load(circleInfoResult.getData().getImgs().get(0)).error(R.drawable.image_error).into(bannerImageView);
        video_url = circleInfoResult.getData().getVideo();
        Glide.with(this).load(video_url).error(R.drawable.image_error).into(videoImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jiesanCardView:
                jiesanCircle();
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.videoLayout:
                selectType = 3;
                selectVideo();
                break;
            case R.id.bannerLayout:
                circleBannerSortDialog.show();
                circleBannerSortDialog.setList(circleInfoResult.getData().getImgs(), circle_id);
                break;
            case R.id.coverLayout:
                selectType = 2;
                selectImage();
                break;
            case R.id.logoLayout:
                selectType = 1;
                selectImage();
                break;
            case R.id.profileLayout:
                Intent profileIntent = new Intent(this, UpdateCircleProfileActivity.class);
                profileIntent.putExtra("circle_id", circle_id);
                startActivity(profileIntent);
                break;
            case R.id.historyLayout:

                break;
            case R.id.searchHistoryLayout:

                break;
        }
    }

    private int selectType = 0;//1.logo,2.cover,3.video
    private ArrayList<String> mImageList = new ArrayList<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;

    private void selectImage() {
        mImageList.clear();
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(CircleSettingActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectVideo() {
        mImageList.clear();
        ImagePicker.getInstance()
                .setTitle("视频选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(false)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(CircleSettingActivity.this, REQUEST_SELECT_VIDEO_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private String video_url;
    private int video_width, video_height;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        circleBannerSortDialog.setActivityForResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
//            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
//            uploadImage(selectType, mImageList.get(0));
//        }

        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            loadingDialog.show();
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            String videoPath = mImageList.get(0);
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLE, videoPath, new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    video_url = name;
//                    System.out.println("视频上传video_url回调: " + video_url);
                    updateCircle();
//                    Message message = myHandler.obtainMessage(3);
//                    message.sendToTarget();
//                    VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(video_url, new VideoUtils.OnCaculateVideoWidthHeightListener() {
//                        @Override
//                        public void onVideoWidthHeight(float w, float h, float vt) {
//                            video_width = (int) w;
//                            video_height = (int) h;
//                        }
//                    });
                }
            });
        }
    }

    private UploadImage logoUploadImage, coverUploadImage;

    private void uploadImage(final int uploadType, final String path) {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLE, path, new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                UploadImage uploadImage = ImageUtils.getInstance().getUploadImage(path, name);
                switch (uploadType) {
                    case 1:
                        logoUploadImage = uploadImage;
                        System.out.println("logo");
                        break;
                    case 2:
                        coverUploadImage = uploadImage;
                        System.out.println("cover");
                        break;
                }
//                System.out.println("url: " + uploadImage.getFile() + " , width: " + uploadImage.getWidth() + " , height: " + uploadImage.getHeight());
            }
        });
    }

    private CircleInfoResult circleInfoResult;

    /**
     * 圈子详情-基础信息
     */
    private void circleBase() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_BASE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("circle info : " + result);
                circleInfoResult = GsonUtils.GsonToBean(result, CircleInfoResult.class);
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

    private BaseResult baseResult;

    private void jiesanCircle() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_USER_LIST_OPERATION);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("type", "3");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private int is_verify;

    private void updateCircle() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_CIRCLE);
        params.addBodyParameter("video", video_url);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("is_verify", String.valueOf(is_verify));
        params.addBodyParameter("price", String.valueOf(price));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    private static OnRemoveCircleListener onRemoveCircleListener;

    public static void setOnRemoveCircleListener(OnRemoveCircleListener listener) {
        onRemoveCircleListener = listener;
    }

    public interface OnRemoveCircleListener {
        void onRemoveCircle();
    }
}
