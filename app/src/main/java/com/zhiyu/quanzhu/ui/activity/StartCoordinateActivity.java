package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 开始客服介入
 */
public class StartCoordinateActivity extends BaseActivity implements View.OnClickListener, PublishFeedImagesGridAdapter.OnAddImagesListener, PublishFeedImagesGridAdapter.OnDeleteImageListener {
    private LinearLayout backLayout;
    private TextView titleTextView, publishTextView;
    private EditText reasonEditText, descEditText;
    private String reason, desc;
    private MyRecyclerView imageGridView;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private int id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<StartCoordinateActivity> weakReference;

        public MyHandler(StartCoordinateActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StartCoordinateActivity activity = weakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_coordinate);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        id = getIntent().getIntExtra("id", 0);
        initViews();
        initDialogs();
    }

    private void initViews() {
        mImageList.add("add");
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("客服介入");
        reasonEditText = findViewById(R.id.reasonEditText);
        descEditText = findViewById(R.id.descEditText);
        imageGridView = findViewById(R.id.imageGridView);
        imageGridAdapter = new PublishFeedImagesGridAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        imageGridAdapter.setData(mImageList);
        imageGridAdapter.setOnAddImagesListener(this);
        imageGridAdapter.setOnDeleteImageListener(this);
        imageGridView.setAdapter(imageGridAdapter);
        imageGridView.setLayoutManager(gridLayoutManager);
        ItemTouchHelperCallback helperCallback = new ItemTouchHelperCallback(imageGridAdapter);
        helperCallback.setSwipeEnable(false);
        helperCallback.setDragEnable(true);
        ItemTouchHelper helper = new ItemTouchHelper(helperCallback);
        helper.attachToRecyclerView(imageGridView);
        publishTextView = findViewById(R.id.publishTextView);
        publishTextView.setOnClickListener(this);

    }

    private void initDialogs() {
        deleteImageDialog = new DeleteImageDialog(this, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                map.remove(mImageList.get(delete_position));
                mImageList.remove(delete_position);
                mImageList.remove("add");
                mImageList.add("add");
                imageGridAdapter.setData(mImageList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.publishTextView:
                reason = reasonEditText.getText().toString().trim();
                desc = descEditText.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(reason)) {
                    MessageToast.getInstance(this).show("请输入申请事由");
                    return;
                }
                if (StringUtils.isNullOrEmpty(desc)) {
                    MessageToast.getInstance(this).show("请描述您的问题");
                    return;
                }
                if (null != map && map.size() > 0) {
                    List<String> list = imageGridAdapter.getList();
                    uploadImageList.clear();
                    for (String key : list) {
                        if (!key.equals("add"))
                            uploadImageList.add(ImageUtils.getInstance().getUploadImage(key, map.get(key)));
                    }
                }
                if (null == uploadImageList || uploadImageList.size() == 0) {
                    MessageToast.getInstance(this).show("请上传图片凭证");
                    return;
                }
                publishCoordinateProfile();
                break;
        }
    }

    @Override
    public void onAddImages() {
        selectImages();
    }

    private DeleteImageDialog deleteImageDialog;
    private int delete_position = -1;

    @Override
    public void onDeleteImage(int position) {
        if (null != deleteImageDialog) {
            delete_position = position;
            deleteImageDialog.show();
        }
    }

    private void selectImages() {
        mImageList.remove("add");
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(StartCoordinateActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private List<UploadImage> uploadImageList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
            mImageList.add("add");
            imageGridAdapter.setData(mImageList);
        }
    }

    private List<String> imagesUploadList = new ArrayList<>();

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        imagesUploadList.add(name);
                    }
                });
            }
        }
    }

    private BaseResult baseResult;

    private void publishCoordinateProfile() {
//        MessageToast.getInstance(this).show("此页面缺少接口，缺少上传的图片类型，记得定义好");
//        MessageToast.getInstance(this).show("正式发起客服介入，缺少接口");
        System.out.println("上传的图片集: " + GsonUtils.GsonString(uploadImageList));
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.KE_FU_JIE_RU);
        params.addBodyParameter("id", String.valueOf(id));
        params.addBodyParameter("reson", reason);
        params.addBodyParameter("desc", desc);
        params.addBodyParameter("imgs", GsonUtils.GsonString(uploadImageList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("客服介入: "+result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("客服介入: "+ex.toString());
                Message message = myHandler.obtainMessage(99);
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


}
