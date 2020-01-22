package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.zxing.android.BeepManager;
import com.zhiyu.quanzhu.ui.widget.zxing.android.CaptureActivityHandler2;
import com.zhiyu.quanzhu.ui.widget.zxing.android.FinishListener;
import com.zhiyu.quanzhu.ui.widget.zxing.android.InactivityTimer;
import com.zhiyu.quanzhu.ui.widget.zxing.android.IntentSource;
import com.zhiyu.quanzhu.ui.widget.zxing.camera.CameraManager;
import com.zhiyu.quanzhu.ui.widget.zxing.view.ViewfinderView;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.QRHelper;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 扫一扫
 */
public class ScanActivity extends BaseActivity implements
        SurfaceHolder.Callback, View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private final String TAG = "ScanActivity";
    private Camera.Parameters params;
    private Camera camera;
    private CameraManager cameraManager;
    private CaptureActivityHandler2 handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private boolean isOpen = false;
    private TextView myQrCodeTextView;
    private ImageView lightImageView;
    private LinearLayout backLayout, rightLayout;
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }

    private void initViews() {
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        lightImageView = findViewById(R.id.lightImageView);
        lightImageView.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        myQrCodeTextView=findViewById(R.id.myQrCodeTextView);
        myQrCodeTextView.setOnClickListener(this);
    }

    private void openLight() {
        camera = CameraManager.getCamera();
        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview(); // 开始亮灯
        isOpen = true;
        lightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.light_on));
    }

    private void closeLight() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params); // 关掉亮灯
        isOpen = false;
        lightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.light_off));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lightImageView:
                if (!isOpen) {
                    openLight();
                } else {
                    closeLight();
                }
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                getPermission();
                break;
            case R.id.myQrCodeTextView:
                Intent intent=new Intent(ScanActivity.this,MyQRCodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        viewfinderView = findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            beepManager.playBeepSoundAndVibrate();
            Log.i("scanqrcode", rawResult.getText() + " , barcode: " + barcode);

//            Intent intent = getIntent();
//            intent.putExtra("codedContent", rawResult.getText());
//            intent.putExtra("codedBitmap", barcode);
//            setResult(RESULT_OK, intent);
//            finish();
        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler2(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("摄像头故障");
        builder.setPositiveButton("OK", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            goPhotoAlbum();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        goPhotoAlbum();
    }

    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String photoPath;
        if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            String result = QRHelper.getReult(BitmapFactory.decodeFile(photoPath));
            Log.i("photoPath", "photoPath: " + photoPath);
            Log.i("scanqrcode", "result: " + result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
