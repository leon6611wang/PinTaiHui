package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.zhiyu.quanzhu.ui.adapter.ImageGridRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.widget.MyMediaController;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.util.DensityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateShangQuanActivity extends BaseActivity implements View.OnClickListener, ImageGridRecyclerAdapter.OnAddImageListener,
        EasyPermissions.PermissionCallbacks, ImageGridRecyclerAdapter.OnDeleteImageListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView;
    private boolean isCreate = false;
    private EditText mingchengTextView, jineEditText, jieshaoEditText;
    private LinearLayout xingzhiLayout, chengshiLayout, hangyeLayout;
    private TextView xingzhiTextView, chengshiTextView, hangyeTextView;
    private ToggleButton shenheToggleButton, shoufeiToggleButton;
    private RoundImageView logoImageView;
    private ImageView deleteLogoImagView;
    private RecyclerView mRecyclerView;
    private ImageGridRecyclerAdapter imageGridRecyclerAdapter;
    private List<String> list = new ArrayList<>();
    private RelativeLayout uploadvideoLayout;
    private ChoosePhotoDialog choosePhotoDialog;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;
    private Uri image_uri, video_uri;
    private String logo_image_path;
    private String video_path;
    private boolean isChooseLogo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shangquan);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
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
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isCreate ? "创建商圈" : "商圈审核中");
        rightTextView = findViewById(R.id.rightTextView);
        mingchengTextView = findViewById(R.id.mingchengTextView);
        jineEditText = findViewById(R.id.jineEditText);
        jieshaoEditText = findViewById(R.id.jieshaoEditText);
        xingzhiLayout = findViewById(R.id.xingzhiLayout);
        xingzhiLayout.setOnClickListener(this);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        chengshiLayout.setOnClickListener(this);
        hangyeLayout = findViewById(R.id.hangyeLayout);
        hangyeLayout.setOnClickListener(this);
        xingzhiTextView = findViewById(R.id.xingzhiTextView);
        chengshiTextView = findViewById(R.id.chengshiTextView);
        hangyeTextView = findViewById(R.id.hangyeTextView);
        shenheToggleButton = findViewById(R.id.shenheToggleButton);
        shoufeiToggleButton = findViewById(R.id.shoufeiToggleButton);
        logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setOnClickListener(this);
        deleteLogoImagView = findViewById(R.id.deleteLogoImagView);
        deleteLogoImagView.setOnClickListener(this);
        list.add("add");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        imageGridRecyclerAdapter = new ImageGridRecyclerAdapter(this);
        imageGridRecyclerAdapter.setOnAddImageListener(this);
        imageGridRecyclerAdapter.setOnDeleteImageListener(this);
        imageGridRecyclerAdapter.setData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.dp_10), false);
        mRecyclerView.setAdapter(imageGridRecyclerAdapter);
        mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        uploadvideoLayout = findViewById(R.id.uploadvideoLayout);
        uploadvideoLayout.setOnClickListener(this);
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

                break;
            case R.id.chengshiLayout:

                break;
            case R.id.hangyeLayout:

                break;
            case R.id.uploadvideoLayout:
                getVideo();
                break;
            case R.id.logoImageView:
                isChooseLogo = true;
                getPermission();
                break;
            case R.id.deleteLogoImagView:
                if (!TextUtils.isEmpty(logo_image_path)) {
                    logo_image_path = null;
                    Glide.with(this).load(R.mipmap.add_image).into(logoImageView);
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
                list.add(path);
                imageGridRecyclerAdapter.setData(list);
            }
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            video_path = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
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
            uploadvideoLayout.setVisibility(View.GONE);
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

}
