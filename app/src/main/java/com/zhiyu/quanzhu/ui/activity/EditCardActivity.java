package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.dialog.VideoFullScreenDialog;
import com.zhiyu.quanzhu.ui.dialog.VideoPlayDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MediaUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.view.View.VISIBLE;

/**
 * 编辑名片
 */
public class EditCardActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, EasyPermissions.PermissionCallbacks {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private ToggleButton gongkaiToggleButton, yanzhengToggleButton;
    private boolean gongkaiChecked, yanzhengChecked;
    private EditText xingmingedittext, shoujiedittext, weixinedittext, youxiangedittext, gongsiedittext, zhiweiedittext, introEditText;
    private LinearLayout chengshilayout, hangyelayout;
    private TextView chengshitextview, hangyetextview, saveButtonTextView;
    private String xingming, shouji, weixin, youxiang, gongsi, zhiwei, chengshi, hangye, neirong;
    private int is_open, is_verifiy;
    private ProvinceCityDialog provinceCityDialog;
    private IndustryDialog industryDialog;
    private ChoosePhotoDialog choosePhotoDialog;
    private RoundImageView cardImageView;
    private CircleImageView headerpicImageView;
    private VideoPlayDialog videoPlayDialog;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;
    private Uri image_uri, video_uri;
    private String video_path;
    private int chooseType = 0;//1:头像；2：名片介绍图片；3：名片介绍视频
    private String upload_avatar_name, upload_cover_name, upload_video_name;
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
                    if (!TextUtils.isEmpty(activity.cardResult.getData().getDetail().getCard_thumb())) {
                        Glide.with(activity).load(activity.cardResult.getData().getDetail().getCard_thumb()).into(activity.headerpicImageView);
                    }
                    if (!TextUtils.isEmpty(activity.cardResult.getData().getDetail().getImg())) {
                        Glide.with(activity).load(activity.cardResult.getData().getDetail().getImg()).into(activity.cardImageView);
                    }

                    if (!TextUtils.isEmpty(activity.cardResult.getData().getDetail().getVideo_intro())) {
                        activity.uploadvideoImageView.setBackgroundColor(activity.getResources().getColor(R.color.black));
                        Glide.with(activity).load(activity.cardResult.getData().getDetail().getVideo_intro() + "?vframe/jpg/offset/10").into(activity.uploadvideoImageView);
                    }
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
                case 2:
                    activity.videoFullScreenDialog.show();
                    activity.videoFullScreenDialog.setVideoPath(activity.video_path);
                    break;
                case 3:
                    Toast.makeText(activity,activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
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
                chengshi = areaProvince.getName() + " " + areaCity.getName();
                chengshitextview.setText(chengshi);
            }
        });
        industryDialog = new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {
                hangye = parent.getName() + " " + child.getName();
                hangyetextview.setText(hangye);
            }
        });
        choosePhotoDialog = new ChoosePhotoDialog(this, R.style.dialog, new ChoosePhotoDialog.OnChoosePhotoListener() {
            @Override
            public void xiangce() {
                goPhotoAlbum();
            }

            @Override
            public void paizhao() {
                goCamera();
            }
        });
        videoPlayDialog = new VideoPlayDialog(this, EditCardActivity.this, R.style.dialog);

        videoFullScreenDialog = new VideoFullScreenDialog(this, R.style.dialog);
    }

    private void showVideoFullScreenDialog() {
//        videoFullScreenDialog.show();
//        videoFullScreenDialog.setVideoPath(video_path);
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
                chooseType = 1;
                getPermission();
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
                chooseType = 2;
                getPermission();
                break;
            case R.id.uploadvideoImageView:
                chooseType = 3;
                getVideo();
//                videoFullScreenDialog.show();
//                videoFullScreenDialog.setVideoPath(video_path);
//                showVideoFullScreenDialog();
                break;
        }
    }

    private boolean checkValues() {
        boolean checked = true;
        xingming = xingmingedittext.getText().toString().trim();
        if (TextUtils.isEmpty(xingming)) {
            checked = false;
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return checked;
        }
        if (TextUtils.isEmpty(chengshi)) {
            checked = false;
            Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
            return checked;
        }
        if (TextUtils.isEmpty(hangye)) {
            checked = false;
            Toast.makeText(this, "请选择行业", Toast.LENGTH_SHORT).show();
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
                Log.i("addeditmingpian", "公开: " + isChecked);
                break;
            case R.id.yanzhengToggleButton:
                yanzhengChecked = isChecked;
                Log.i("addeditmingpian", "验证: " + isChecked);
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

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            choosePhotoDialog.show();
//            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }


    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    private void getVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("video/*");
        startActivityForResult(intent, 3);
    }

    //激活相机操作
    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            image_uri = FileProvider.getUriForFile(this, "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            image_uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        this.startActivityForResult(intent, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        choosePhotoDialog.show();
    }

    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                path = String.valueOf(cameraSavePath);
            } else {
                path = image_uri.getEncodedPath();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            path = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
        }
        switch (chooseType) {
            case 1:
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.AVATAR, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        upload_avatar_name = name;
                    }
                });
                Glide.with(this).load(path).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable) resource).setLoopCount(0);
                        }
                        return false;
                    }
                }).into(headerpicImageView);
                break;
            case 2:
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CARDS, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        upload_cover_name = name;
                    }
                });
                Glide.with(this).load(path).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable) resource).setLoopCount(0);
                        }
                        return false;
                    }
                }).into(cardImageView);
                break;
        }


        if (requestCode == 3 && resultCode == RESULT_OK) {
            video_path = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CARDS, video_path, new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    upload_video_name = name;
                }
            });
            Bitmap bitmap = MediaUtils.getInstance().getVideoThumb(video_path);
            if (null != bitmap) {
                uploadvideoImageView.setBackgroundColor(getResources().getColor(R.color.black));
                uploadvideoImageView.setImageBitmap(bitmap);
            }
            System.out.println("video_path: " + video_path);
//            Message message=myHandler.obtainMessage(2);
//            message.sendToTarget();
//            showVideoFullScreenDialog();
//            initVideo(video_path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BaseResult baseResult;

    private void editCard() {
        System.out.println("编辑名片");
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_UPDATE);
        params.addBodyParameter("card_id", String.valueOf(card_id));
        params.addBodyParameter("card_name", xingming);
        if (null != areaProvince) {
            params.addBodyParameter("province", String.valueOf(areaProvince.getCode()));
            params.addBodyParameter("province_name", areaProvince.getName());
        }
        if (null != areaCity) {
            params.addBodyParameter("city", String.valueOf(areaCity.getCode()));
            params.addBodyParameter("city_name", areaCity.getName());
        }

        params.addBodyParameter("industry", hangye);
        params.addBodyParameter("mobile", shouji);
        if (!upload_avatar_name.startsWith("http")) {
            params.addBodyParameter("card_thumb", upload_avatar_name);
        }
        params.addBodyParameter("occupation", zhiwei);
        params.addBodyParameter("wx", weixin);
        params.addBodyParameter("email", youxiang);
        params.addBodyParameter("company", gongsi);
        if (!upload_video_name.startsWith("http")) {
            params.addBodyParameter("video_intro", upload_video_name);
        }
        if (!upload_cover_name.startsWith("http")) {
            params.addBodyParameter("img", upload_cover_name);
        }
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
                upload_avatar_name = cardResult.getData().getDetail().getCard_thumb();
                chengshi = cardResult.getData().getDetail().getCity_name();
                hangye = cardResult.getData().getDetail().getIndustry();
                shouji = cardResult.getData().getDetail().getMobile();
                weixin = cardResult.getData().getDetail().getWx();
                youxiang = cardResult.getData().getDetail().getEmail();
                gongsi = cardResult.getData().getDetail().getCompany();
                zhiwei = cardResult.getData().getDetail().getOccupation();
                upload_cover_name = cardResult.getData().getDetail().getImg();
                upload_video_name = cardResult.getData().getDetail().getVideo_intro();
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
