package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 完善用户基本信息
 */
public class CompleteUserProfileActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private TextView headerpicTextView, industryTextView, confirmTextView;
    private CircleImageView headerpicImageView;
    private ImageView addheaderpicImageView;
    private EditText nicknameEditText, areaEditText, companyEditText, postEditText;
    private LinearLayout industryLayout;
    private ChoosePhotoDialog choosePhotoDialog;
    private File cameraSavePath;
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_profile);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
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
        headerpicTextView = findViewById(R.id.headerpicTextView);
        industryTextView = findViewById(R.id.industryTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        headerpicImageView = findViewById(R.id.headerpicImageView);
        addheaderpicImageView = findViewById(R.id.addheaderpicImageView);
        addheaderpicImageView.setOnClickListener(this);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        areaEditText = findViewById(R.id.areaEditText);
        companyEditText = findViewById(R.id.companyEditText);
        postEditText = findViewById(R.id.postEditText);
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:

                break;
            case R.id.industryLayout:

                break;
            case R.id.addheaderpicImageView:
                getPermission();
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

    //激活相机操作
    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
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
//        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }
            if(!TextUtils.isEmpty(photoPath)){
                headerpicTextView.setVisibility(View.GONE);
                headerpicImageView.setVisibility(View.VISIBLE);
                Glide.with(this).load(photoPath).into(headerpicImageView);
            }else{
                headerpicTextView.setVisibility(View.VISIBLE);
                headerpicImageView.setVisibility(View.GONE);
            }
            Log.i("photoPath", "photoPath: " + photoPath);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            if(!TextUtils.isEmpty(photoPath)){
                headerpicTextView.setVisibility(View.GONE);
                headerpicImageView.setVisibility(View.VISIBLE);
                Glide.with(this).load(photoPath).into(headerpicImageView);
            }else{
                headerpicTextView.setVisibility(View.VISIBLE);
                headerpicImageView.setVisibility(View.GONE);
            }
            Log.i("photoPath", "photoPath: " + photoPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
