package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedsTag;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.result.AddFeedResult;
import com.zhiyu.quanzhu.model.result.ArticleInformationResult;
import com.zhiyu.quanzhu.model.result.VideoInfoResult;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.DrafDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.WaitDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布视频
 */
public class PublishVideoActivity extends BaseActivity implements View.OnClickListener, PublishParamSettingActivity.OnPublishFinishListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private LinearLayout addVideoLayout;
    private RoundImageView videoImageView;
    private int screenWidth, dp_15, dp_240;
    private FrameLayout.LayoutParams videoParams;
    private LinearLayout tagLayout;
    private EditText videoDescEditText;
    private TextView tagTextView, nextButton;
    private AddTagDialog addTagDialog;
    private LoadingDialog loadingDialog;
    private DrafDialog drafDialog;
    private String tags = "";
    private String tagIds = "";
    private String video_url;
    private int feeds_id = 0;
    private int video_width, video_height;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;
    private ArrayList<String> mImageList = new ArrayList<>();
    private DeleteImageDialog deleteImageDialog;
    private ArrayList<Tag> tagList = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishVideoActivity> activityWeakReference;

        public MyHandler(PublishVideoActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishVideoActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (200 == activity.videoInfoResult.getCode()) {
                        activity.videoDescEditText.setText(activity.videoInfoResult.getData().getDetail().getContent());
                        Glide.with(activity).load(activity.videoInfoResult.getData().getDetail().getVideo_thumb()).into(activity.videoImageView);
                        activity.video_url = activity.videoInfoResult.getData().getDetail().getVideo_url();
                        activity.video_width = activity.videoInfoResult.getData().getDetail().getVideo_width();
                        activity.video_height = activity.videoInfoResult.getData().getDetail().getVideo_height();
                        activity.videoImageView.setVisibility(View.VISIBLE);
                        activity.addVideoLayout.setVisibility(View.GONE);
                        if (null != activity.videoInfoResult.getData().getDetail().getFeeds_tags() &&
                                activity.videoInfoResult.getData().getDetail().getFeeds_tags().size() > 0) {
                            for (FeedsTag feedsTag : activity.videoInfoResult.getData().getDetail().getFeeds_tags()) {
                                Tag tag = new Tag();
                                tag.setTag_id(feedsTag.getTag_id());
                                tag.setName(feedsTag.getTag_name());
                                activity.tagList.add(tag);
                            }
                            if (activity.tagList.size() > 0) {
                                for (int i = 0; i < activity.tagList.size(); i++) {
                                    activity.tags += activity.tagList.get(i).getName();
                                    activity.tagIds += activity.tagList.get(i).getId();
                                    if (i < activity.tagList.size() - 1) {
                                        activity.tags += ",";
                                        activity.tagIds += ",";
                                    }
                                }
                                activity.tagTextView.setText(activity.tags);
                            }
                        }
                    }
                    break;
                case 1:
                    activity.loadingDialog.dismiss();
                    break;
                case 2:
                    activity.loadingDialog.dismiss();
                    activity.nextButton.setClickable(true);
                    MessageToast.getInstance(activity).show(activity.addFeedResult.getMsg());
                    if (activity.addFeedResult.getCode() == 200) {
                        activity.feeds_id = activity.addFeedResult.getData().getFeeds_id();
                        if (activity.is_draf == 0) {
                            activity.goToPublishSetting();
                        } else {
                            activity.finish();
                        }
                    }

                    break;
                case 3:
                    activity.nextButton.setClickable(true);
                    activity.loadingDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.goToPublishSetting();
                    }
                    break;
                case 99:
                    activity.loadingDialog.dismiss();
                    activity.nextButton.setClickable(true);
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后重试.");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
        PublishParamSettingActivity.setOnPublishFinishListener(this);
        initViews();
        initDialogs();
        if (feeds_id > 0) {
            videoInformation();
        }
    }

    private void initDialogs() {
        addTagDialog = new AddTagDialog(this, this, R.style.dialog, new AddTagDialog.OnTagsSelectedListener() {
            @Override
            public void onTagsSelected(List<Tag> list) {
                tagList.clear();
                tagList.addAll(list);
                tags = "";
                tagIds = "";
                if (tagList.size() > 0) {
                    for (int i = 0; i < tagList.size(); i++) {
                        tags += tagList.get(i).getName();
                        tagIds += tagList.get(i).getId();
                        if (i < tagList.size() - 1) {
                            tags += ",";
                            tagIds += ",";
                        }
                    }
                    tagTextView.setText(tags);
                }
            }
        });

        deleteImageDialog = new DeleteImageDialog(this, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                addVideoLayout.setVisibility(View.VISIBLE);
                videoImageView.setVisibility(View.GONE);
                mImageList.clear();
                video_width = 0;
                video_height = 0;
                video_url = null;
            }
        });

        drafDialog = new DrafDialog(this, R.style.dialog, new DrafDialog.OnDrafListener() {
            @Override
            public void onConfirm() {
                is_draf = 1;
                addFeed();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });

        loadingDialog = new LoadingDialog(this, R.style.dialog);
    }

    private void initViews() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        dp_240 = (int) getResources().getDimension(R.dimen.dp_240);
        videoParams = new FrameLayout.LayoutParams((screenWidth - dp_15 * 2), dp_240);
        videoParams.gravity = Gravity.CENTER;
        addVideoLayout = findViewById(R.id.addVideoLayout);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布视频");
        tagLayout = findViewById(R.id.tagLayout);
        tagLayout.setOnClickListener(this);
        videoDescEditText = findViewById(R.id.videoDescEditText);
        tagTextView = findViewById(R.id.tagTextView);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        addVideoLayout = findViewById(R.id.addVideoLayout);
        addVideoLayout.setOnClickListener(this);
        videoImageView = findViewById(R.id.videoImageView);
        videoImageView.setLayoutParams(videoParams);
        videoImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.tagLayout:
                addTagDialog.show();
                if (null != tagList && tagList.size() > 0) {
                    addTagDialog.setTagList(tagList);
                }
                break;
            case R.id.nextButton:
                if (StringUtils.isNullOrEmpty(video_url)) {
                    MessageToast.getInstance(this).show("请选择视频");
                    break;
                }
                if (StringUtils.isNullOrEmpty(videoDescEditText.getText().toString().trim())) {
                    MessageToast.getInstance(this).show("视频简介不能为空");
                    break;
                }
                loadingDialog.show();
                if (feeds_id > 0) {
                    updateFeed();
                } else {
                    addFeed();
                }

                break;
            case R.id.addVideoLayout:
                selectVideo();
                break;
            case R.id.videoImageView:
                deleteImageDialog.show();
                break;
        }
    }

    private void goToPublishSetting() {
        Intent paramsIntent = new Intent(PublishVideoActivity.this, PublishParamSettingActivity.class);
        paramsIntent.putExtra("feeds_id", feeds_id);
        paramsIntent.putExtra("publishType", 2);
        if (tagList.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("tagList", tagList);
            paramsIntent.putExtras(bundle);
        }
        startActivityForResult(paramsIntent,10041);
    }

    private void selectVideo() {
        mImageList.remove("add");
        ImagePicker.getInstance()
                .setTitle("视频选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(false)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(PublishVideoActivity.this, REQUEST_SELECT_VIDEO_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10041) {
            if (null != data) {
                if (data.hasExtra("isSuccess")) {
                    finish();
                }
            }
        }
        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            Glide.with(PublishVideoActivity.this).load(mImageList.get(0)).into(videoImageView);
            videoImageView.setVisibility(View.VISIBLE);
            addVideoLayout.setVisibility(View.GONE);
            loadingDialog.show();
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, mImageList.get(0), new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    video_url = name;
                    ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                        @Override
                        public void run() {
                            VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(video_url, new VideoUtils.OnCaculateVideoWidthHeightListener() {
                                @Override
                                public void onVideoWidthHeight(float w, float h, float vt) {
                                    video_width = (int) w;
                                    video_height = (int) h;
                                    Message message = myHandler.obtainMessage(1);
                                    message.sendToTarget();
                                }
                            });
                        }
                    });

                }
            });

        }
    }

    private int is_draf;//是否草稿 1是
    private AddFeedResult addFeedResult;

    private void addFeed() {
        nextButton.setClickable(false);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_FEED);
        params.addBodyParameter("type", "2");
        params.addBodyParameter("is_draf", "1");
        params.addBodyParameter("content", videoDescEditText.getText().toString().trim());
        params.addBodyParameter("video_url", video_url);
        params.addBodyParameter("video_width", String.valueOf(video_width));
        params.addBodyParameter("video_height", String.valueOf(video_height));
        params.addBodyParameter("tags", tagIds);
        params.addBodyParameter("feeds_type","2");
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("province_name", SPUtils.getInstance().getLocationProvince(BaseApplication.applicationContext));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("发布视频: " + result);
                addFeedResult = GsonUtils.GsonToBean(result, AddFeedResult.class);
                if (200 == addFeedResult.getCode()) {
                    feeds_id = addFeedResult.getData().getFeeds_id();
                }
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

    private BaseResult baseResult;

    private void updateFeed() {
        nextButton.setClickable(false);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_FEED);
        params.addBodyParameter("type", "2");
        params.addBodyParameter("is_draf", "1");
        params.addBodyParameter("content", videoDescEditText.getText().toString().trim());
        params.addBodyParameter("video_url", video_url);
        params.addBodyParameter("video_width", String.valueOf(video_width));
        params.addBodyParameter("video_height", String.valueOf(video_height));
        params.addBodyParameter("tags", tagIds);
        params.addBodyParameter("feeds_type","2");
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("province_name", SPUtils.getInstance().getLocationProvince(BaseApplication.applicationContext));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!StringUtils.isNullOrEmpty(videoDescEditText.getText().toString().trim()) ||
                    !StringUtils.isNullOrEmpty(tags) ||
                    !StringUtils.isNullOrEmpty(video_url)) &&
                    feeds_id == 0
                    ) {
                drafDialog.show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPublishFinish() {
        finish();
    }

    private VideoInfoResult videoInfoResult;

    private void videoInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("videoInfo: " + result);
                videoInfoResult = GsonUtils.GsonToBean(result, VideoInfoResult.class);
                Message message = myHandler.obtainMessage(0);
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
