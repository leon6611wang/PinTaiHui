package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.result.UserProfileResult;
import com.zhiyu.quanzhu.ui.dialog.GenderDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.imagepicker.MyImagePicker;
import com.zhiyu.quanzhu.ui.widget.imagepicker.cropper.CropImage;
import com.zhiyu.quanzhu.ui.widget.imagepicker.cropper.CropImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
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
 * 个人资料/信息
 */
public class MyProfileActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleText, genderTextView, cityTextView, industryTextView, vertifyTextView;
    private LinearLayout genderlayout, citylayout, shouhuodizhilayout, industryLayout, vertifyLayout;
    private EditText nicknameEditText;
    private CircleImageView avatarImageView;

    private GenderDialog genderDialog;
    private ProvinceCityDialog provinceCityDialog;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);
    private final int REQUEST_SELECT_IMAGES_CODE = 10022;
    private AreaProvince province = new AreaProvince();
    private AreaCity city = new AreaCity();

    private static class MyHandler extends Handler {
        WeakReference<MyProfileActivity> activityWeakReference;

        public MyHandler(MyProfileActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyProfileActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.loadingDialog.dismiss();
                    FailureToast.getInstance(activity).show();
                    break;
                case 1:
                    activity.loadingDialog.dismiss();
                    if (200 == activity.profileResult.getCode()) {
                        Glide.with(activity).load(activity.profileResult.getData().getUser().getAvatar()).error(R.drawable.image_error).into(activity.avatarImageView);
                        activity.nicknameEditText.setText(activity.profileResult.getData().getUser().getUsername());
                        activity.genderTextView.setText(activity.profileResult.getData().getUser().getSex_desc());
                        activity.province.setCode(activity.profileResult.getData().getUser().getProvince_id());
                        activity.province.setName(activity.profileResult.getData().getUser().getProvince_name());
                        activity.city.setCode(activity.profileResult.getData().getUser().getCity());
                        activity.city.setName(activity.profileResult.getData().getUser().getCity_name());
                        String cityDesc = activity.profileResult.getData().getUser().getProvince_name() + " " + activity.profileResult.getData().getUser().getCity_name();
                        if (!StringUtils.isNullOrEmpty(cityDesc.trim())) {
                            activity.cityTextView.setText(cityDesc);
                        } else {
                            activity.cityTextView.setText("未选择");
                        }


                        activity.industryTextView.setText(StringUtils.isNullOrEmpty(activity.profileResult.getData().getUser().getIndustry()) ?
                                "未选择" : activity.profileResult.getData().getUser().getIndustry());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
        userProfile();
    }


    private int sex;

    private void initDialogs() {
        genderDialog = new GenderDialog(this, R.style.dialog, new GenderDialog.OnGenderSelectListener() {
            @Override
            public void onGenderSelect(int index, String gender) {
                sex = index;
            }
        });
        provinceCityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince p, AreaCity c) {
                province = p;
                city = c;
                cityTextView.setText(province.getName() + " " + city.getName());
            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleText = findViewById(R.id.titleTextView);
        titleText.setText("个人信息");
        genderlayout = findViewById(R.id.genderlayout);
        genderlayout.setOnClickListener(this);
        citylayout = findViewById(R.id.citylayout);
        citylayout.setOnClickListener(this);
        shouhuodizhilayout = findViewById(R.id.shouhuodizhilayout);
        shouhuodizhilayout.setOnClickListener(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(this);
        genderTextView = findViewById(R.id.genderTextView);
        cityTextView = findViewById(R.id.cityTextView);
        industryTextView = findViewById(R.id.industryTextView);
        vertifyTextView = findViewById(R.id.vertifyTextView);
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
        vertifyLayout = findViewById(R.id.vertifyLayout);
        vertifyLayout.setOnClickListener(this);
        nicknameEditText = findViewById(R.id.nicknameEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.genderlayout:
                genderDialog.show();
                break;
            case R.id.citylayout:
                provinceCityDialog.show();
                break;
            case R.id.shouhuodizhilayout:
                Intent dizhiIntent = new Intent(this, AddressActivity.class);
                startActivity(dizhiIntent);
                break;
            case R.id.avatarImageView:
//                selectImage();
                imagePicker();
                break;
            case R.id.industryLayout:

                break;
            case R.id.vertifyLayout:

                break;
        }
    }

    private ArrayList<String> imgList = new ArrayList<>();

    private void selectImage() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(imgList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(MyProfileActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void imagePicker() {
        MyImagePicker imagePicker = new MyImagePicker();
        // 设置标题
        imagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        // 启动图片选择器
        imagePicker.startChooser(this, new MyImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
                String imageUrl = GetPhotoFromPhotoAlbum.getRealPathFromUri(MyProfileActivity.this, imageUri);
                System.out.println("选择图片回调: " + imageUrl);
            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                String imageUrl = GetPhotoFromPhotoAlbum.getRealPathFromUri(MyProfileActivity.this, imageUri);
                System.out.println("裁剪图片回调: " + imageUrl);
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小（单位：px）
                        .setRequestedSize(400, 400)
                        // 裁剪框宽高比
                        .setAspectRatio(1, 1);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            imgList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(this).load(imgList.get(0)).into(avatarImageView);
            uploadAvatar();
        }
    }

    private String avatarUrl;

    /**
     * 上传头像
     */
    private void uploadAvatar() {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, imgList.get(0), new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                avatarUrl = name;
                updateUserProfile();
            }
        });
    }


    private UserProfileResult profileResult;

    /**
     * 用户详情
     */
    private void userProfile() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_PROFILE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                profileResult = GsonUtils.GsonToBean(result, UserProfileResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(0);
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

    //更新用户信息
    private void updateUserProfile() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_USER_PROFILE);
        params.addBodyParameter("avatar", avatarUrl);
        params.addBodyParameter("username", nicknameEditText.getText().toString().trim());
        params.addBodyParameter("sex", String.valueOf(sex));
        params.addBodyParameter("province", String.valueOf(province.getCode()));
        params.addBodyParameter("province_name", province.getName());
        params.addBodyParameter("city", String.valueOf(city.getCode()));
        params.addBodyParameter("city_name", city.getName());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(0);
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
