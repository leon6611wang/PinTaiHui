package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.RichTextMenuRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PublishArticle2Activity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, RichTextMenuRecyclerAdapter.OnRichTextMenuClickListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView;
    private RecyclerView richTextMenuRecyclerView;
    private RichTextMenuRecyclerAdapter menuAdapter;
    private List<Integer> iconList = new ArrayList<>();
    private EditText mEditText;
    private Editable mEditable;
    private int start, end;
    private ChoosePhotoDialog choosePhotoDialog;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;
    private Uri image_uri, video_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article_2);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initDatas();
        initViews();
    }

    private void initDatas() {
        iconList.add(R.mipmap.rich_text_bold);
        iconList.add(R.mipmap.rich_text_slash);
        iconList.add(R.mipmap.rich_text_left);
        iconList.add(R.mipmap.rich_text_center);
        iconList.add(R.mipmap.rich_text_right);
        iconList.add(R.mipmap.rich_text_circle);
        iconList.add(R.mipmap.rich_text_image);
        iconList.add(R.mipmap.rich_text_video);

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("编辑正文");
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText("保存");
        richTextMenuRecyclerView = findViewById(R.id.richTextMenuRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        menuAdapter = new RichTextMenuRecyclerAdapter(this);
        menuAdapter.setList(iconList);
        menuAdapter.setOnRichTextMenuClickListener(this);
        richTextMenuRecyclerView.setLayoutManager(linearLayoutManager);
        richTextMenuRecyclerView.setAdapter(menuAdapter);
        mEditText = findViewById(R.id.mEditText);
        mEditable = mEditText.getText();
        start = mEditText.getSelectionStart();
        end = mEditText.getSelectionEnd();
        String selectedString = mEditable.toString().substring(start, end);
        System.out.println(selectedString);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:

                break;
        }
    }

    @Override
    public void onRichTextMenuClick(int position) {
        switch (position) {
            case 0://加粗

                break;
            case 1://斜体

                break;
            case 2://左对齐
                mEditText.setGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case 3://居中对齐
                mEditText.setGravity(Gravity.CENTER | Gravity.TOP);
                break;
            case 4://右对齐
                mEditText.setGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            case 5://文字加重点符

                break;
            case 6://图片
                getPermission();
                break;
            case 7://视频

                break;
        }

    }

    private void inertImage(EditText editText, String imgPath) {
        Bitmap imgInfo = BitmapFactory.decodeFile(imgPath);//通过图片地址获取到Bitmap
        //配置 SpannableString
        SpannableString spannableString = new SpannableString(imgPath);
        Drawable drawable = new BitmapDrawable(editText.getContext().getResources(), imgInfo);
        drawable.setBounds(0, 0, imgInfo.getWidth(), imgInfo.getHeight());
        ImageSpan span = new ImageSpan(drawable);
        spannableString.setSpan(span, 0, imgPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Editable text = editText.getText();
        int start = editText.getSelectionStart();//获取光标位置
        text.insert(start, spannableString);//添加图片
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            choosePhotoDialog.show();
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
        inertImage(mEditText, path);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
