package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryHobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.dialog.VideoFullScreenDialog;
import com.zhiyu.quanzhu.ui.dialog.VideoPlayDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 编辑名片
 */
public class EditCardActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private ToggleButton gongkaiToggleButton, yanzhengToggleButton;
    private boolean gongkaiChecked, yanzhengChecked;
    private EditText xingmingedittext, shoujiedittext, weixinedittext, youxiangedittext, gongsiedittext, zhiweiedittext, introEditText;
    private LinearLayout chengshilayout, hangyelayout;
    private TextView chengshitextview, hangyetextview, saveButtonTextView;
    private String xingming, shouji, weixin, youxiang, gongsi, zhiwei, chengshi, hangye, neirong;
    private String province_name, city_name;
    private int province_code, city_code;
    private int is_open, is_verifiy;
    private ProvinceCityDialog provinceCityDialog;
    private IndustryHobbyDialog industryDialog;
    private ChoosePhotoDialog choosePhotoDialog;
    private RoundImageView cardImageView;
    private CircleImageView headerpicImageView;
    private VideoPlayDialog videoPlayDialog;
    private String avatarUrl, imageUrl, videoUrl;
    private int video_width, video_height;
    private RoundImageView uploadvideoImageView;
    private long card_id;
    private AreaProvince areaProvince;
    private AreaCity areaCity;
    private MyHandler myHandler = new MyHandler(this);
    private VideoFullScreenDialog videoFullScreenDialog;

    private static class MyHandler extends Handler {
        WeakReference<EditCardActivity> activityWeakReference;

        public MyHandler(EditCardActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditCardActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (!StringUtils.isNullOrEmpty(activity.avatarUrl))
                        Glide.with(activity).load(activity.avatarUrl).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.headerpicImageView);
                    if (!StringUtils.isNullOrEmpty(activity.imageUrl))
                        Glide.with(activity).load(activity.imageUrl).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.cardImageView);
                    if (!StringUtils.isNullOrEmpty(activity.videoUrl))
                        Glide.with(activity).load(activity.videoUrl).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.uploadvideoImageView);
                    activity.xingmingedittext.setText(activity.cardResult.getData().getDetail().getCard_name());
                    activity.shoujiedittext.setText(activity.cardResult.getData().getDetail().getMobile());
                    activity.weixinedittext.setText(activity.cardResult.getData().getDetail().getWx());
                    activity.youxiangedittext.setText(activity.cardResult.getData().getDetail().getEmail());
                    activity.gongsiedittext.setText(activity.cardResult.getData().getDetail().getCompany());
                    activity.zhiweiedittext.setText(activity.cardResult.getData().getDetail().getOccupation());
                    activity.chengshitextview.setText(activity.cardResult.getData().getDetail().getCity_name());
                    activity.hangyetextview.setText(activity.cardResult.getData().getDetail().getIndustry());
                    activity.introEditText.setText(activity.cardResult.getData().getDetail().getIntro());
                    activity.gongkaiToggleButton.setChecked(activity.cardResult.getData().getDetail().getIs_open() == 1 ? true : false);
                    activity.yanzhengToggleButton.setChecked(activity.cardResult.getData().getDetail().getIs_verifiy() == 1 ? true : false);
                    break;
                case 3:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.baseResult.getCode() == 200) {
                        activity.finish();
                    }
                    break;
                case 4:
                    Toast.makeText(activity, "内部错误，请稍后重试或联系客服.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        card_id = getIntent().getLongExtra("card_id", 0l);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
        cardInfo();
    }

    private void initDialogs() {
        provinceCityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                areaProvince = province;
                areaCity = city;
                province_code = areaProvince.getCode();
                province_name = areaProvince.getName();
                city_code = (int) areaCity.getCode();
                city_name = areaCity.getName();
                chengshi = areaProvince.getName() + " " + areaCity.getName();
                chengshitextview.setText(chengshi);
            }
        });
        industryDialog = new IndustryHobbyDialog(this, R.style.dialog,true, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby parent, IndustryHobby child) {
                hangye = parent.getName() + " " + child.getName();
                hangyetextview.setText(hangye);
            }
        });
        videoPlayDialog = new VideoPlayDialog(this, EditCardActivity.this, R.style.dialog);
        videoFullScreenDialog = new VideoFullScreenDialog(this, R.style.dialog);
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("编辑名片");
        gongkaiToggleButton = findViewById(R.id.gongkaiToggleButton);
        gongkaiToggleButton.setOnCheckedChangeListener(this);
        gongkaiChecked = gongkaiToggleButton.isChecked();
        yanzhengToggleButton = findViewById(R.id.yanzhengToggleButton);
        yanzhengToggleButton.setOnCheckedChangeListener(this);
        yanzhengChecked = yanzhengToggleButton.isChecked();
        xingmingedittext = findViewById(R.id.xingmingedittext);
        headerpicImageView = findViewById(R.id.headerpicImageView);
        headerpicImageView.setOnClickListener(this);
        shoujiedittext = findViewById(R.id.shoujiedittext);
        weixinedittext = findViewById(R.id.weixinedittext);
        youxiangedittext = findViewById(R.id.youxiangedittext);
        gongsiedittext = findViewById(R.id.gongsiedittext);
        zhiweiedittext = findViewById(R.id.zhiweiedittext);
        chengshilayout = findViewById(R.id.chengshilayout);
        chengshilayout.setOnClickListener(this);
        hangyelayout = findViewById(R.id.hangyelayout);
        hangyelayout.setOnClickListener(this);
        chengshitextview = findViewById(R.id.chengshitextview);
        hangyetextview = findViewById(R.id.hangyetextview);
        saveButtonTextView = findViewById(R.id.saveButtonTextView);
        saveButtonTextView.setOnClickListener(this);
        cardImageView = findViewById(R.id.cardImageView);
        cardImageView.setOnClickListener(this);
        introEditText = findViewById(R.id.introEditText);
        uploadvideoImageView = findViewById(R.id.uploadvideoImageView);
        uploadvideoImageView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.headerpicImageView:
                selectAvatar();
                break;
            case R.id.chengshilayout:
                provinceCityDialog.show();
                break;
            case R.id.hangyelayout:
                industryDialog.show();
                break;
            case R.id.saveButtonTextView:
                if (checkValues()) {
                    editCard();
                }
                break;
            case R.id.cardImageView:
                selectImage();
                break;
            case R.id.uploadvideoImageView:
                selectVideo();
                break;
        }
    }

    private final int REQUEST_SELECT_AVATAR_CODE = 1000;
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;
    private ArrayList<String> avatarList = new ArrayList<>();
    private ArrayList<String> imageList = new ArrayList<>();
    private ArrayList<String> videoList = new ArrayList<>();

    private void selectAvatar() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(avatarList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(EditCardActivity.this, REQUEST_SELECT_AVATAR_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectImage() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(imageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(EditCardActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectVideo() {
        ImagePicker.getInstance()
                .setTitle("视频选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(false)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(videoList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(EditCardActivity.this, REQUEST_SELECT_VIDEO_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }


    private boolean checkValues() {
        boolean checked = true;
        xingming = xingmingedittext.getText().toString().trim();
        if (TextUtils.isEmpty(xingming)) {
            checked = false;
            MessageToast.getInstance(this).show("请输入姓名");
            return checked;
        }
        if (TextUtils.isEmpty(city_name)) {
            checked = false;
            MessageToast.getInstance(this).show("请选择城市");
            return checked;
        }
        if (TextUtils.isEmpty(hangye)) {
            checked = false;
            MessageToast.getInstance(this).show("请选择行业");
            return checked;
        }
        shouji = shoujiedittext.getText().toString().trim();
        weixin = weixinedittext.getText().toString().trim();
        youxiang = youxiangedittext.getText().toString().trim();
        gongsi = gongsiedittext.getText().toString().trim();
        zhiwei = zhiweiedittext.getText().toString().trim();
        neirong = introEditText.getText().toString().trim();
        is_open = gongkaiChecked ? 1 : 0;
        is_verifiy = yanzhengChecked ? 1 : 0;
        return checked;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.gongkaiToggleButton:
                gongkaiChecked = isChecked;
                break;
            case R.id.yanzhengToggleButton:
                yanzhengChecked = isChecked;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (null != videoPlayDialog) {
            videoPlayDialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_AVATAR_CODE && resultCode == RESULT_OK) {
            avatarList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(EditCardActivity.this).load(avatarList.get(0)).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(headerpicImageView);
            upload(avatarList.get(0), 1);
        }
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            imageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(EditCardActivity.this).load(imageList.get(0)).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(cardImageView);
            upload(imageList.get(0), 2);
        }

        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            videoList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(EditCardActivity.this).load(videoList.get(0)).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(uploadvideoImageView);
            upload(videoList.get(0), 3);
//            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, videoList.get(0), new UploadImageUtils.OnUploadCallback() {
//                @Override
//                public void onUploadSuccess(String name) {
//                    videoUrl = name;
//                    VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(videoUrl, new VideoUtils.OnCaculateVideoWidthHeightListener() {
//                        @Override
//                        public void onVideoWidthHeight(float w, float h, float vt) {
//                            video_width = (int) w;
//                            video_height = (int) h;
//                        }
//                    });
//                }
//            });
        }
    }

    private void upload(String path, final int type) {
        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
            @Override
            public void onUploadSuccess(String name) {
                switch (type) {
                    case 1:
                        avatarUrl = name;
                        break;
                    case 2:
                        imageUrl = name;
                        break;
                    case 3:
                        videoUrl = name;
                        break;
                }
            }
        });
    }

    private BaseResult baseResult;

    private void editCard() {
        System.out.println("编辑名片 videoUrl: " + videoUrl);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_UPDATE);
        params.addBodyParameter("card_id", String.valueOf(card_id));
        params.addBodyParameter("card_name", xingming);
        params.addBodyParameter("province", String.valueOf(province_code));
        params.addBodyParameter("province_name", province_name);
        params.addBodyParameter("city", String.valueOf(city_code));
        params.addBodyParameter("city_name", city_name);
        params.addBodyParameter("industry", hangye);
        params.addBodyParameter("mobile", shouji);
        params.addBodyParameter("card_thumb", avatarUrl);
        params.addBodyParameter("occupation", zhiwei);
        params.addBodyParameter("wx", weixin);
        params.addBodyParameter("email", youxiang);
        params.addBodyParameter("company", gongsi);
        params.addBodyParameter("video_intro", videoUrl);
        params.addBodyParameter("img", imageUrl);
        params.addBodyParameter("intro", neirong);
        params.addBodyParameter("is_open", String.valueOf(is_open));
        params.addBodyParameter("is_verifiy", String.valueOf(is_verifiy));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("编辑名片 " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("编辑名片 " + ex.toString());
                Message message = myHandler.obtainMessage(4);
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

    private CardResult cardResult;

    private void cardInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO);
        params.addBodyParameter("card_id", String.valueOf(card_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                xingming = cardResult.getData().getDetail().getCard_name();
                avatarUrl = cardResult.getData().getDetail().getCard_thumb();
                city_name = cardResult.getData().getDetail().getCity_name();
                city_code = cardResult.getData().getDetail().getCity();
                province_name = cardResult.getData().getDetail().getProvince_name();
                province_code = cardResult.getData().getDetail().getProvince();
                hangye = cardResult.getData().getDetail().getIndustry();
                shouji = cardResult.getData().getDetail().getMobile();
                weixin = cardResult.getData().getDetail().getWx();
                youxiang = cardResult.getData().getDetail().getEmail();
                gongsi = cardResult.getData().getDetail().getCompany();
                zhiwei = cardResult.getData().getDetail().getOccupation();
                imageUrl = cardResult.getData().getDetail().getImg();
                videoUrl = cardResult.getData().getDetail().getVideo_intro();
                neirong = cardResult.getData().getDetail().getIntro();
                is_open = cardResult.getData().getDetail().getIs_open();
                is_verifiy = cardResult.getData().getDetail().getIs_verifiy();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
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

}
