package com.zhiyu.quanzhu.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.adapter.ImageGridRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.AppUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener, ImageGridRecyclerAdapter.OnAddImageListener, EasyPermissions.PermissionCallbacks, ImageGridRecyclerAdapter.OnDeleteImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView, confirmTextView;
    private RecyclerView mRecyclerView;
    private ImageGridRecyclerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private EditText fankuiEditText;
    private File cameraSavePath;
    private Uri uri;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ChoosePhotoDialog choosePhotoDialog;
    private MyHandler myHandler = new MyHandler(this);
    private LoadingDialog loadingDialog;

    private static class MyHandler extends Handler {
        WeakReference<FeedBackActivity> activityWeakReference;

        public MyHandler(FeedBackActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FeedBackActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.loadingDialog.dismiss();
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialog();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("用户反馈");
        fankuiEditText = findViewById(R.id.fankuiEditText);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
//        list.add("https://c-ssl.duitang.com/uploads/item/201803/24/20180324081023_8FVre.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201708/15/20170815205322_vPnmr.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201603/13/20160313093434_emKxZ.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201607/25/20160725102949_2earM.jpeg");
//        list.add("http://b-ssl.duitang.com/uploads/item/201707/04/20170704103218_LrPiu.jpeg");
        list.add("add");
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ImageGridRecyclerAdapter(this);
        adapter.setOnAddImageListener(this);
        adapter.setOnDeleteImageListener(this);
        adapter.setData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreentUtils.getInstance().dip2px(this, 10)));
    }

    private void initDialog() {
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
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.confirmTextView:
                if (!StringUtils.isNullOrEmpty(fankuiEditText.getText().toString().trim()) || imagesUploadList.size() > 0)
                    feedback();
                break;
        }
    }


    @Override
    public void onAddImage() {
        if (list.size() < 10) {
            getPermission();
        } else {
            Toast.makeText(this, "最多9张图片", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDelete(int position) {
        list.remove(position);
        adapter.setData(list);
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
            uploadImage(photoPath);
            list.add(0, photoPath);
            adapter.setData(list);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            uploadImage(photoPath);
            list.add(0, photoPath);
            adapter.setData(list);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<String> imagesUploadList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();

    private void uploadImage(final String path) {
        if (!map.containsKey(path)) {
            loadingDialog.show();
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    map.put(path, name);
                    imagesUploadList.add(name);
                    Message message = myHandler.obtainMessage(0);
                    message.sendToTarget();
                }
            });
        }
    }

    private BaseResult baseResult;

    private void feedback() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_BACK);
        params.addBodyParameter("content", fankuiEditText.getText().toString().trim());
        params.addBodyParameter("version", AppUtils.getInstance().getVersionName(this));
        params.addBodyParameter("device", AppUtils.getInstance().getDeviceBrand());
        params.addBodyParameter("os", "android");
        params.addBodyParameter("pics", GsonUtils.GsonString(imagesUploadList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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
