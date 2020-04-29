package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.cropimage.CropImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;


public class CropImageActivity extends BaseActivity implements View.OnClickListener, CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView;
    private CropImageView cropImageView;
    private Uri imageUri;
    private String outImagePath = null;
    private String inImagePath = null;
    private final int REQUEST_CROP_IMAGES_CODE = 10023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        outImagePath = getCacheDir().getAbsolutePath() + getOutImageName();
        inImagePath = getIntent().getStringExtra("imagePath");
        initViews();
        if (!StringUtils.isNullOrEmpty(inImagePath)) {
            Uri imageUri = null;
            try {
                FileInputStream fs = new FileInputStream(inImagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fs);
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cropImageView.setImageUriAsync(imageUri);
        }
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("图片裁剪");
        titleTextView.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        rightTextView.setText("确定");
        cropImageView = findViewById(R.id.cropImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                cropImage();
                break;
        }
    }

    protected void cropImage() {
        Uri outputUri = getOutputUri();
        cropImageView.saveCroppedImageAsync(outputUri,
                Bitmap.CompressFormat.JPEG,
                100,
                400,
                400,
                CropImageView.RequestSizeOptions.SAMPLING);
    }

    protected Uri getOutputUri() {
        Uri outputUri = null;
        File imageFile = new File(outImagePath);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (Exception e) {

            }
        }
        outputUri = Uri.fromFile(imageFile);
        return outputUri;
    }

    @Override
    protected void onStart() {
        super.onStart();
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cropImageView.setOnSetImageUriCompleteListener(null);
        cropImageView.setOnCropImageCompleteListener(null);
    }


    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        try {
            FileInputStream fs = new FileInputStream(outImagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(fs);
            System.out.println("bitmap: " + bitmap.getWidth() + " , " + bitmap.getHeight());
            imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            System.out.println("图片地址: " + outImagePath + " , imageUri: " + imageUri);
            Intent intent = new Intent();
            intent.putExtra("cropImagePath", outImagePath);
            setResult(REQUEST_CROP_IMAGES_CODE, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOutImageName() {
        StringBuilder str = new StringBuilder();
        str.append("/CROP_");
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        str.append(".JPG");
        return str.toString();
    }
}
