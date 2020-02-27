package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.result.CircleDetailResult;
import com.zhiyu.quanzhu.ui.adapter.ImageGridRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleTypeDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.widget.MyMediaController;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 创建圈子
 */
public class CreateCircleActivity extends BaseActivity implements View.OnClickListener, ImageGridRecyclerAdapter.OnAddImageListener,
        EasyPermissions.PermissionCallbacks, ImageGridRecyclerAdapter.OnDeleteImageListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView, jinggaoTextView;
    private boolean isCreate = true;//是否创建
    private boolean isEdit = false;//是否可编辑(审核中不可编辑，其他状态可编辑)
    private EditText mingchengEditText, jineEditText, jieshaoEditText;
    private LinearLayout xingzhiLayout, chengshiLayout, hangyeLayout, moneyLayout;
    private TextView xingzhiTextView, chengshiTextView, hangyeTextView, confirmTextView, industryTextView;
    private SwitchButton shenheSwitchButton, shoufeiSwitchButton;
    private RoundImageView logoImageView;
    private ImageView uploadvideoImageView;
    private RecyclerView mRecyclerView;
    private ImageGridRecyclerAdapter imageGridRecyclerAdapter;
    private List<String> list = new ArrayList<>();
    private ChoosePhotoDialog choosePhotoDialog;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;
    private Uri image_uri, video_uri;
    private String logo_image_path;
    private String video_path;
    private boolean isChooseLogo = true;
    private CircleTypeDialog circleTypeDialog;
    private ProvinceCityDialog cityDialog;
    private IndustryDialog industryDialog;
    private int circleType = 1;
    private long circle_id;
    private String name, province_name, city_name, descirption, logo, thumb, two_industry, three_industry, video;
    private int type, province, city, is_verify, is_price, price, status;
    private List<String> imgs = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CreateCircleActivity> activityWeakReference;

        public MyHandler(CreateCircleActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CreateCircleActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.circleEditable();
                    String title = null;
                    String buttonString = null;
                    String remark = null;
                    switch (activity.circleDetailResult.getData().getStatus()) {
                        case -2:
                            title = "圈子审核失败";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "审核失败，可修改信息重新提交审核" : activity.circleDetailResult.getData().getRemark();
                            buttonString = "重新提交审核";
                            break;
                        case -1:
                            title = "圈子审核中";
                            buttonString = "撤销审核";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "正在审核中，数据不可修改" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 2:
                            title = "圈子被后台下架";
                            buttonString = "申请重新上架";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被下架，可修改数据重新申请上架" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 3:
                            title = "圈子被后台禁用";
                            buttonString = "申请解除禁用";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被禁用，可修改数据重新申请" : activity.circleDetailResult.getData().getRemark();
                            break;
                        case 4:
                            title = "圈子被圈主解散";
                            buttonString = "删除圈子";
                            remark = (StringUtils.isNullOrEmpty(activity.circleDetailResult.getData().getRemark())) ? "已被圈助解散，可直接删除" : activity.circleDetailResult.getData().getRemark();
                            break;
                    }
                    activity.titleTextView.setText(title);
                    activity.confirmTextView.setText(buttonString);
                    activity.jinggaoTextView.setText(remark);
                    activity.mingchengEditText.setText(activity.circleDetailResult.getData().getName());
                    activity.name = activity.circleDetailResult.getData().getName();
                    activity.type = activity.circleDetailResult.getData().getType();
                    activity.xingzhiTextView.setText(activity.circleDetailResult.getData().getType() == 1 ? "行业" : "兴趣");
                    activity.two_industry = activity.circleDetailResult.getData().getTwo_industry();
                    activity.three_industry = activity.circleDetailResult.getData().getThree_industry();
                    activity.province = activity.circleDetailResult.getData().getProvince();
                    activity.province_name = activity.circleDetailResult.getData().getProvince_name();
                    activity.city = activity.circleDetailResult.getData().getCity();
                    activity.city_name = activity.circleDetailResult.getData().getCity_name();
                    activity.is_verify = activity.circleDetailResult.getData().getIs_verify();
                    activity.is_price = activity.circleDetailResult.getData().getIs_price();
                    activity.price = activity.circleDetailResult.getData().getPrice();
                    activity.descirption = activity.circleDetailResult.getData().getDescirption();
                    activity.imgs = activity.circleDetailResult.getData().getImgs();
                    activity.logo = activity.circleDetailResult.getData().getLogo();
                    activity.video = activity.circleDetailResult.getData().getVideo();

                    activity.hangyeTextView.setText(activity.circleDetailResult.getData().getTwo_industry() + "/" + activity.circleDetailResult.getData().getThree_industry());
                    activity.chengshiTextView.setText(activity.circleDetailResult.getData().getProvince_name() + " " + activity.circleDetailResult.getData().getCity_name());
                    switch (activity.circleDetailResult.getData().getIs_verify()) {
                        case 0:
                            activity.shenheSwitchButton.close();
                            break;
                        case 1:
                            activity.shenheSwitchButton.open();
                            break;
                    }
                    switch (activity.circleDetailResult.getData().getIs_price()) {
                        case 0:
                            activity.shoufeiSwitchButton.close();
                            break;
                        case 1:
                            activity.shoufeiSwitchButton.open();
                            break;
                    }
                    activity.jineEditText.setText(((float) activity.circleDetailResult.getData().getPrice() / 100f) + "元");
                    activity.jieshaoEditText.setText(activity.circleDetailResult.getData().getDescirption());
                    Glide.with(activity).load(activity.circleDetailResult.getData().getLogo()).into(activity.logoImageView);
                    activity.list.addAll(activity.circleDetailResult.getData().getImgs());
                    activity.imageGridRecyclerAdapter.setData(activity.list);
                    break;
                case 2:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        if (circle_id > 0l) {
            isCreate = false;
        }
        initViews();
        initDialogs();
        if (!isCreate) {
            circleDetail();
        }
        circleEditable();
    }

    private void initDialogs() {
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
        circleTypeDialog = new CircleTypeDialog(this, R.style.dialog, new CircleTypeDialog.OnCircleTypeListener() {
            @Override
            public void onCircleType(int type, String desc) {
                circleType = type;
                xingzhiTextView.setText(desc);
                industryTextView.setText(desc);
                System.out.println(desc);
            }
        });
        cityDialog = new ProvinceCityDialog(this, R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince p, AreaCity c) {
                province = p.getCode();
                province_name = p.getName();
                city = (int) c.getCode();
                city_name = c.getName();
                chengshiTextView.setText(p.getName() + " " + c.getName());
            }
        });
        industryDialog = new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {
                two_industry = parent.getName();
                three_industry = child.getName();
                hangyeTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isCreate ? "创建圈子" : "圈子审核中");
        rightTextView = findViewById(R.id.rightTextView);
        rightLayout.setVisibility(isCreate ? View.INVISIBLE : View.VISIBLE);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setText(isCreate ? "提交审核" : "撤销审核");
        confirmTextView.setOnClickListener(this);
        jinggaoTextView = findViewById(R.id.jinggaoTextView);

        mingchengEditText = findViewById(R.id.mingchengEditText);
        jineEditText = findViewById(R.id.jineEditText);
        jieshaoEditText = findViewById(R.id.jieshaoEditText);
        xingzhiLayout = findViewById(R.id.xingzhiLayout);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        moneyLayout = findViewById(R.id.moneyLayout);
        xingzhiTextView = findViewById(R.id.xingzhiTextView);
        xingzhiTextView.setText("行业");
        chengshiTextView = findViewById(R.id.chengshiTextView);
        hangyeTextView = findViewById(R.id.hangyeTextView);
        industryTextView = findViewById(R.id.industryTextView);
        shenheSwitchButton = findViewById(R.id.shenheSwitchButton);
        shoufeiSwitchButton = findViewById(R.id.shoufeiSwitchButton);
        shenheSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_verify=isOpen?1:0;
            }
        });
        shoufeiSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                is_price=isOpen?1:0;
                moneyLayout.setVisibility(isOpen ? View.VISIBLE : View.GONE);
            }
        });
        chengshiLayout = findViewById(R.id.chengshiLayout);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        shoufeiSwitchButton = findViewById(R.id.shoufeiSwitchButton);
        logoImageView = findViewById(R.id.logoImageView);
        list.add("add");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        imageGridRecyclerAdapter = new ImageGridRecyclerAdapter(this);
        imageGridRecyclerAdapter.setData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.dp_10), false);
        mRecyclerView.setAdapter(imageGridRecyclerAdapter);
        mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        uploadvideoImageView = findViewById(R.id.uploadvideoImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:

                break;
            case R.id.xingzhiLayout:
                circleTypeDialog.show();
                break;
            case R.id.chengshiLayout:
                cityDialog.show();
                break;
            case R.id.hangyeLayout:
                industryDialog.show();
                break;
            case R.id.uploadvideoImageView:
                getVideo();
                break;
            case R.id.logoImageView:
                isChooseLogo = true;
                getPermission();
                break;
            case R.id.confirmTextView:
                name = mingchengEditText.getText().toString().trim();
                descirption=jieshaoEditText.getText().toString().trim();
                String jine=jineEditText.getText().toString().trim();
                if(!StringUtils.isNullOrEmpty(jine)){
                    price=Integer.parseInt(jine)*100;
                }
                if(null!=imgs&&imgs.size()>0){
                    thumb=imgs.get(0);
                    imgs.remove(0);
                }
                System.out.println("thumb: "+thumb);
                logo="https://c-ssl.duitang.com/uploads/item/201801/31/20180131184558_jgycf.jpeg";
                if (isCreate) {
                    createCircle();
                } else {
                    switch (circleDetailResult.getData().getStatus()) {
                        case -2:

                            break;
                        case -1:

                            break;
                        case 1:

                            break;
                        case 2:

                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                    }
                }
                break;
        }
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
        if (isChooseLogo) {
            logo_image_path = path;
            Glide.with(this).load(logo_image_path).listener(new RequestListener<Drawable>() {
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
            }).into(logoImageView);
        } else {
            if (!TextUtils.isEmpty(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLE, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        imgs.add(name);
                        System.out.println("upload image: " + name);
                        System.out.println("imgs: "+imgs.size());
                    }
                });
                list.add(path);
                imageGridRecyclerAdapter.setData(list);
            }
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            video_path = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            video=video_path;
            System.out.println("video_path: " + video_path);
            initVideo(video_path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private MyMediaController controller;
    private VideoView videoView;
    private String urlString = "http://vd2.bdstatic.com/mda-jkv4qrxm0dmm3vz4/sc/mda-jkv4qrxm0dmm3vz4.mp4";

    private void initVideo(String path) {
        if (Vitamio.initialize(this)) {
            videoView = findViewById(R.id.mVideoView);
            videoView.setVisibility(View.VISIBLE);
//            videoView.setVideoURI(Uri.parse(urlString));//播放网络视频
            videoView.setVideoPath(path);//播放本地视频
            controller = new MyMediaController(this, videoView, this);//视频控制器
            videoView.setMediaController(controller);//关联控制器
            //设置高画质
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
            //设置显示时长
            controller.show(5000);
            videoView.requestFocus();
            videoView.start();
            setListener();
        }
    }

    private void setListener() {
        //设置缓冲进度的监听
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                tvProgress.setText(percent + "%");
            }
        });
        //设置缓冲下载监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲时的视图变化
//                        tvProgress.setVisibility(View.VISIBLE);
//                        tvDownloadSpeed.setVisibility(View.VISIBLE);
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲好后的视图变化（可播放）
//                        tvProgress.setVisibility(View.GONE);
//                        tvDownloadSpeed.setVisibility(View.GONE);
                        mp.start();
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                        tvDownloadSpeed.setText("当前网速:" + extra + "kb/s");//下载是速度
                        break;
                }
                return true;
            }
        });
        //设置准备监听（判断一准备好播放）
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setBufferSize(512 * 1025);//设置缓冲区大小
            }
        });
    }

    @Override
    public void onAddImage() {
        isChooseLogo = false;
        if (list.size() < 10) {
            getPermission();
        } else {
            Toast.makeText(this, "最多9张图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelete(int position) {
        imgs.remove(position);
        list.remove(position);
        imageGridRecyclerAdapter.setData(list);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (videoView != null) {
//            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setEditTextEditable(EditText editText, boolean editable) {
        editText.setFocusable(editable);
        editText.setFocusableInTouchMode(editable);
        editText.setLongClickable(editable);
        editText.setInputType(editable ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_NULL);
    }

    /**
     * 创建圈子
     */
    private void createCircle() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_CIRCLE);
        params.addBodyParameter("circle_name", name);
        params.addBodyParameter("descirption", descirption);
        params.addBodyParameter("logo", logo);
        params.addBodyParameter("thumb", thumb);
        params.addBodyParameter("type", String.valueOf(circleType));
        params.addBodyParameter("is_price", String.valueOf(is_price));
        params.addBodyParameter("price", String.valueOf(price));
        params.addBodyParameter("is_verify", String.valueOf(is_verify));
        params.addBodyParameter("video", video);
        params.addBodyParameter("imgs", GsonUtils.GsonString(imgs));
        params.addBodyParameter("two_industry", two_industry);
        params.addBodyParameter("three_industry", three_industry);
        params.addBodyParameter("province",String.valueOf(province));
        params.addBodyParameter("province_name",province_name);
        params.addBodyParameter("city",String.valueOf(city));
        params.addBodyParameter("city_name",city_name);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
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

    private CircleDetailResult circleDetailResult;

    /**
     * 圈子详情
     */
    private void circleDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_DETAIL);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                circleDetailResult = GsonUtils.GsonToBean(result, CircleDetailResult.class);
                if (null != circleDetailResult.getData())
                    isEdit = (circleDetailResult.getData().getStatus() == -1) ? false : true;
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void circleEditable() {
        jinggaoTextView.setVisibility(isCreate ? View.GONE : View.VISIBLE);

        if (isCreate || isEdit) {
            setEditTextEditable(jieshaoEditText, true);
        } else {
            setEditTextEditable(jieshaoEditText, false);
        }
        if (isCreate || isEdit) {
            setEditTextEditable(jineEditText, true);
            jineEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            setEditTextEditable(jineEditText, false);
        }

        if (isCreate || isEdit) {
            setEditTextEditable(mingchengEditText, true);
        } else {
            setEditTextEditable(mingchengEditText, false);
        }
        if (isCreate || isEdit)
            xingzhiLayout.setOnClickListener(this);

        if (isCreate || isEdit)
            chengshiLayout.setOnClickListener(this);

        if (isCreate || isEdit)
            hangyeLayout.setOnClickListener(this);
        if (isCreate || isEdit) {
            shenheSwitchButton.setCanSwitch(true);
        } else {
            shenheSwitchButton.setCanSwitch(false);
        }
        if (isCreate || isEdit) {
            shoufeiSwitchButton.setCanSwitch(true);
        } else {
            shoufeiSwitchButton.setCanSwitch(false);
        }
        if (isCreate) {
            shenheSwitchButton.close();
            shoufeiSwitchButton.close();
        }
        if (isCreate || isEdit)
            logoImageView.setOnClickListener(this);
        if (isCreate || isEdit)
            imageGridRecyclerAdapter.setOnAddImageListener(this);
        if (isCreate || isEdit)
            imageGridRecyclerAdapter.setOnDeleteImageListener(this);
        if (isCreate || isEdit)
            uploadvideoImageView.setOnClickListener(this);
    }

}
