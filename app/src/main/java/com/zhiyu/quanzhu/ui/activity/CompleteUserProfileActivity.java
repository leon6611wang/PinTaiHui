package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryHobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
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
 * 完善用户基本信息
 */
public class CompleteUserProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView industryTextView, confirmTextView, cityTextView;
    private CircleImageView addheaderpicImageView;
    private EditText nicknameEditText, companyEditText, postEditText;
    private LinearLayout industryLayout, cityLayout;
    private final int REQUEST_SELECT_IMAGES_CODE = 10022;
    private final int REQUEST_CROP_IMAGES_CODE = 10023;
    private ProvinceCityDialog cityDialog;
    private IndustryHobbyDialog industryDialog;
//    private IndustryDialog industryDialog;
    private String userName, company, occupation;
    private AreaProvince areaProvince;
    private AreaCity areaCity;
    private IndustryHobby industryParent;
    private IndustryHobby industryChild;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CompleteUserProfileActivity> activityWeakReference;

        public MyHandler(CompleteUserProfileActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            CompleteUserProfileActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.loadingDialog.isShowing())
                        activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.pageChange();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_profile);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initDialogs();
        initViews();
    }

    private void initDialogs() {
        cityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                areaProvince = province;
                areaCity = city;
                cityTextView.setText(province.getName() + " " + city.getName());
            }
        });
        industryDialog = new IndustryHobbyDialog(this, R.style.dialog, true, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby p, IndustryHobby c) {
                industryParent = p;
                industryChild = c;
                industryTextView.setText(p.getName() + " " + c.getName());
            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        industryTextView = findViewById(R.id.industryTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        addheaderpicImageView = findViewById(R.id.addheaderpicImageView);
        addheaderpicImageView.setOnClickListener(this);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = nicknameEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cityLayout = findViewById(R.id.cityLayout);
        cityLayout.setOnClickListener(this);
        cityTextView = findViewById(R.id.cityTextView);
        companyEditText = findViewById(R.id.companyEditText);
        companyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                company = companyEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        postEditText = findViewById(R.id.postEditText);
        postEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                occupation = postEditText.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                if (StringUtils.isNullOrEmpty(avatarUrl)) {
                    MessageToast.getInstance(CompleteUserProfileActivity.this).show("请选择头像");
                    break;
                }
                if (StringUtils.isNullOrEmpty(userName)) {
                    MessageToast.getInstance(CompleteUserProfileActivity.this).show("请输入昵称");
                    break;
                }
                if (null == areaProvince || null == areaCity) {
                    MessageToast.getInstance(CompleteUserProfileActivity.this).show("请选择地区");
                    break;
                }
                if (null == industryParent || null == industryChild) {
                    MessageToast.getInstance(CompleteUserProfileActivity.this).show("请选择行业");
                    break;
                }
                fillProfile();
                break;
            case R.id.industryLayout:
                industryDialog.show();
                break;
            case R.id.addheaderpicImageView:
                selectImage();
                break;
            case R.id.cityLayout:
                cityDialog.show();
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
                .start(CompleteUserProfileActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            imgList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            cropImage(imgList.get(0));
        } else if (requestCode == REQUEST_CROP_IMAGES_CODE) {
            String cropImagePath = data.getStringExtra("cropImagePath");
            uploadAvatar(cropImagePath);
            Glide.with(this).load(cropImagePath).into(addheaderpicImageView);
            System.out.println("cropImagePath: " + cropImagePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void cropImage(String imagePath) {
        Intent intent = new Intent(this, CropImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        startActivityForResult(intent, REQUEST_CROP_IMAGES_CODE);
    }

    private String avatarUrl;

    /**
     * 上传头像
     */
    private void uploadAvatar(String imagePath) {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, imagePath, new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                avatarUrl = name;

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            Intent intent = new Intent(CompleteUserProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }


    private BaseResult baseResult;

    private void fillProfile() {
        loadingDialog.show();
//        System.out.println("avatar:" + avatarUrl + ",username:" + userName + ",province:" + areaProvince.getCode() + ",province_name:" + areaProvince.getName() +
//                ",city:" + areaCity.getCode() + ",city_name:" + areaCity.getName() + ",industry:" + industryParent.getName() + "/" + industryChild.getName() +
//                ",company:" + company + ",occupation:" + occupation);
//        System.out.println("token:" + SPUtils.getInstance().getUserToken(BaseApplication.applicationContext));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_USER_PROFILE);
        params.addBodyParameter("avatar", avatarUrl);
        params.addBodyParameter("username", userName);
        params.addBodyParameter("province", String.valueOf(areaProvince.getCode()));
        params.addBodyParameter("province_name", areaProvince.getName());
        params.addBodyParameter("city", String.valueOf(areaCity.getCode()));
        params.addBodyParameter("city_name", areaCity.getName());
        params.addBodyParameter("industry", industryParent.getName() + "/" + industryChild.getName());
        params.addBodyParameter("company", company);
        params.addBodyParameter("occupation", occupation);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("完善信息: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    SPUtils.getInstance().userFillProfile(BaseApplication.applicationContext);
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("完善信息: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void pageChange() {
        if (!SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
            Intent intent = new Intent(this, HobbySelectActivity.class);
            startActivity(intent);
        } else {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        }
        finish();
    }
}
