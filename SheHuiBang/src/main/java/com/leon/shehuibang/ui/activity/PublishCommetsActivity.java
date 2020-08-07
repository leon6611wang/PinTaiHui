package com.leon.shehuibang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.leon.shehuibang.R;
import com.leon.shehuibang.base.BaseActivity;
import com.leon.shehuibang.model.MyResult;
import com.leon.shehuibang.model.bean.CommentsTemplate;
import com.leon.shehuibang.ui.dialog.CommentsTemplateDialog;
import com.leon.shehuibang.ui.dialog.LoadingDialog;
import com.leon.shehuibang.ui.widget.MessageToast;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_1_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_1_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_2_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_2_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_2;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_3;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_4;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_5;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_4_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_4_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_5_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_6_0;
import com.leon.shehuibang.utils.ConstantsUtils;
import com.leon.shehuibang.utils.GlideLoader;
import com.leon.shehuibang.utils.GsonUtils;
import com.leon.shehuibang.utils.ScreentUtils;
import com.leon.shehuibang.utils.ThumbnailUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PublishCommetsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout.LayoutParams params;
    private LinearLayout backLayout, publisLayout;
    private FrameLayout containerLayout;
    private TextView titleTextView;
    private ImageView txtImageView, imgImageView, videoImageView;
    private CommentsTemplateDialog commentsTemplateDialog;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishCommetsActivity> weakReference;

        public MyHandler(PublishCommetsActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishCommetsActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.uploadCount == 0) {
                        activity.loadingDialog.dismiss();
                    }
                    break;
                case 2:
                    if (activity.uploadCount == 0) {
                        MessageToast.getInstance(activity).show("动态发布成功");
                        activity.mImageList.clear();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comments);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initData();
        initViews();
        initDialogs();
    }

    private void initDialogs() {
        commentsTemplateDialog = new CommentsTemplateDialog(this, R.style.dialog_transparent, new CommentsTemplateDialog.OnCommentsTemplateChooseListener() {
            @Override
            public void onCommentsTemplateChoose(CommentsTemplate commentsTemplate) {
                template = commentsTemplate;
                initContainer();
                selectImages(template.getImage_count());
            }
        });
        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initData() {
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        int dp_10 = (int) getResources().getDimension(R.dimen.dp_10);
        params = new LinearLayout.LayoutParams(screenWidth - dp_10 * 2, screenWidth - dp_10 * 2);
        params.leftMargin = dp_10;
        params.rightMargin = dp_10;
        params.topMargin = dp_10;
        params.bottomMargin = dp_10;
        params.gravity = Gravity.CENTER;
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("PUBLISH COMMENTS");
        publisLayout = findViewById(R.id.publisLayout);
        publisLayout.setOnClickListener(this);
        txtImageView = findViewById(R.id.txtImageView);
        txtImageView.setOnClickListener(this);
        imgImageView = findViewById(R.id.imgImageView);
        imgImageView.setOnClickListener(this);
        videoImageView = findViewById(R.id.videoImageView);
        videoImageView.setOnClickListener(this);

        containerLayout = findViewById(R.id.containerLayout);
        containerLayout.setLayoutParams(params);

    }

    private CommentsTemplate template;
    private View templateView = null;

    private void initContainer() {
        if (containerLayout.getChildCount() > 0) {
            containerLayout.removeAllViews();
        }
        switch (template.getTemplate_code()) {
            case "1_0":
                templateView = new CommentsTemplate_1_0(this);
                break;
            case "1_1":
                templateView = new CommentsTemplate_1_1(this);
                break;
            case "2_0":
                templateView = new CommentsTemplate_2_0(this);
                break;
            case "2_1":
                templateView = new CommentsTemplate_2_1(this);
                break;
            case "3_0":
                templateView = new CommentsTemplate_3_0(this);
                break;
            case "3_1":
                templateView = new CommentsTemplate_3_1(this);
                break;
            case "3_2":
                templateView = new CommentsTemplate_3_2(this);
                break;
            case "3_3":
                templateView = new CommentsTemplate_3_3(this);
                break;
            case "3_4":
                templateView = new CommentsTemplate_3_4(this);
                break;
            case "3_5":
                templateView = new CommentsTemplate_3_5(this);
                break;
            case "4_0":
                templateView = new CommentsTemplate_4_0(this);
                break;
            case "4_1":
                templateView = new CommentsTemplate_4_1(this);
                break;
            case "5_0":
                templateView = new CommentsTemplate_5_0(this);
                break;
            case "6_0":
                templateView = new CommentsTemplate_6_0(this);
                break;
        }
        containerLayout.addView(templateView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.publisLayout:
                publish_comments();
                break;
            case R.id.txtImageView:
                change(0);
                break;
            case R.id.imgImageView:
                change(1);
                commentsTemplateDialog.show();
                break;
            case R.id.videoImageView:
                change(2);
                break;
        }
    }

    private void change(int index) {
        txtImageView.setImageDrawable(getResources().getDrawable(R.mipmap.txt_black));
        imgImageView.setImageDrawable(getResources().getDrawable(R.mipmap.img_black));
        videoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_black));
        switch (index) {
            case 0:
                txtImageView.setImageDrawable(getResources().getDrawable(R.mipmap.txt_blue));
                break;
            case 1:
                imgImageView.setImageDrawable(getResources().getDrawable(R.mipmap.img_blue));
                break;
            case 2:
                videoImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_blue));
                break;
        }
    }


    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;
    private ArrayList<String> mImageList = new ArrayList<>();
    private int uploadCount;

    private void selectImages(int count) {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(count)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(PublishCommetsActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (mImageList.size() > 0) {
                loadingDialog.show();
                loadImages();
                uploadCount = mImageList.size();
                for (String path : mImageList) {
                    ThumbnailUtils.getInstance().thumbnailImage(path, PublishCommetsActivity.this, new ThumbnailUtils.OnThumbnailListener() {
                        @Override
                        public void onThumbnail(final String thumb_path) {
                            uploadCount--;
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        }
                    });
                }
            }
//            uploadImages();
        }

        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
        }
    }

    private void loadImages() {
        switch (template.getTemplate_code()) {
            case "1_0":
                ((CommentsTemplate_1_0) templateView).setImageUrl(mImageList);
                break;
            case "1_1":
                ((CommentsTemplate_1_1) templateView).setImageUrl(mImageList);
                break;
            case "2_0":
                ((CommentsTemplate_2_0) templateView).setImageUrl(mImageList);
                break;
            case "2_1":
                ((CommentsTemplate_2_1) templateView).setImageUrl(mImageList);
                break;
            case "3_0":
                ((CommentsTemplate_3_0) templateView).setImageUrl(mImageList);
                break;
            case "3_1":
                ((CommentsTemplate_3_1) templateView).setImageUrl(mImageList);
                break;
            case "3_2":
                ((CommentsTemplate_3_2) templateView).setImageUrl(mImageList);
                break;
            case "3_3":
                ((CommentsTemplate_3_3) templateView).setImageUrl(mImageList);
                break;
            case "3_4":
                ((CommentsTemplate_3_4) templateView).setImageUrl(mImageList);
                break;
            case "3_5":
                ((CommentsTemplate_3_5) templateView).setImageUrl(mImageList);
                break;
            case "4_0":
                ((CommentsTemplate_4_0) templateView).setImageUrl(mImageList);
                break;
            case "4_1":
                ((CommentsTemplate_4_1) templateView).setImageUrl(mImageList);
                break;
            case "5_0":
                ((CommentsTemplate_5_0) templateView).setImageUrl(mImageList);
                break;
            case "6_0":
                ((CommentsTemplate_6_0) templateView).setImageUrl(mImageList);
                break;
        }
    }


    private void uploadImages() {
        if (mImageList.size() > 0) {
            for (int i = 0; i < mImageList.size(); i++) {
                upload(mImageList.get(i), i);
            }
        }
    }

    private void upload(String path, int order) {
        RequestParams params = new RequestParams(ConstantsUtils.HOST + "image/upload_comments_image");
        params.setMultipart(true);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        KeyValue keyValue = new KeyValue("file", new File(path));
        list.add(keyValue);
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        params.addParameter("comments_id", myResult.getComments_id());
        params.addParameter("image_order", order);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("upload: " + result);
                uploadCount--;
                myResult = GsonUtils.GsonToBean2(result, MyResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("upload: " + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private MyResult myResult;

    private void publish_comments() {
        uploadCount = mImageList.size();
        RequestParams params = new RequestParams(ConstantsUtils.HOST + "comments/publish_comments");
        params.addHeader("Content-Type", "application/json; charset=UTF-8");
        params.addParameter("user_id", 1);
        params.addParameter("txt", "动态发布1");
        params.addParameter("template_code", template.getTemplate_code());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                myResult = GsonUtils.GsonToBean2(result, MyResult.class);
                System.out.println("publish comments: " + result);
                if (200 == myResult.getCode()) {
                    uploadImages();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("publish comments: " + ex.toString());
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
