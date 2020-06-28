package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.bean.WhoCanSee;
import com.zhiyu.quanzhu.model.result.FeedInfoResult;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.DrafDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryHobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.WhoCanSeeDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.RecyclerScrollView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 发布动态
 */
public class PublishFeedActivity extends BaseActivity implements View.OnClickListener, PublishFeedImagesGridAdapter.OnAddImagesListener, PublishFeedImagesGridAdapter.OnDeleteImageListener {
    private LinearLayout backLayout, addTagLayout, atquanziLayout, fanweiLayout, hobbyLayout, industryLayout;
    private TextView titleTextView, tagTextView, atquanziTextView, quanziTextView, fanweiTextView, publishTextView, hobbyTextView, industryTextView;
    private MyRecyclerView imageGridView;
    private EditText contentEditText;
    private DrafDialog drafDialog;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private final int REQUEST_SELECT_VIDEO_CODE = 1002;
    private ArrayList<String> mImageList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private List<String> uploadImgList = new ArrayList<>();
    private RecyclerScrollView mScrollView;
    private LoadingDialog waitDialog;
    private int feeds_id;
    private String video_url;
    private int video_width, video_height;
    private String circleName;
    private int secretIndex;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishFeedActivity> activityWeakReference;

        public MyHandler(PublishFeedActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishFeedActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.publishTextView.setClickable(true);
                    if (200 == activity.feedInfoResult.getCode()) {
                        activity.contentEditText.setText(activity.feedInfoResult.getData().getDetail().getContent());
                        activity.mImageList.remove("add");
                        if (!StringUtils.isNullOrEmpty(activity.feedInfoResult.getData().getDetail().getVideo_url())) {
                            activity.mImageList.add(activity.feedInfoResult.getData().getDetail().getVideo_thumb());
                            activity.video_url = activity.feedInfoResult.getData().getDetail().getVideo_url();
                            activity.video_width = activity.feedInfoResult.getData().getDetail().getVideo_width();
                            activity.video_height = activity.feedInfoResult.getData().getDetail().getVideo_height();
                        } else {
                            for (int i = 0; i < activity.feedInfoResult.getData().getDetail().getImgs().size(); i++) {
                                activity.mImageList.add(activity.feedInfoResult.getData().getDetail().getImgs().get(i).getFile());
                            }
                        }
                        activity.mImageList.add("add");
                        activity.imageGridAdapter.setData(activity.mImageList);
                        activity.circleName = activity.feedInfoResult.getData().getDetail().getCircle_name();
                        activity.industry_parent = activity.feedInfoResult.getData().getDetail().getP_industry();
                        activity.industry_child = activity.feedInfoResult.getData().getDetail().getIndustry();
                        activity.hobby_parent = activity.feedInfoResult.getData().getDetail().getP_hobby();
                        activity.hobby_child = activity.feedInfoResult.getData().getDetail().getHobby();
                        activity.secretIndex = activity.feedInfoResult.getData().getDetail().getIs_secret();
                        if (null != activity.feedInfoResult.getData().getDetail().getFeeds_tags() && activity.feedInfoResult.getData().getDetail().getFeeds_tags().size() > 0) {
                            String tagNames = "";
                            List<Tag> taglist = new ArrayList<>();
                            for (int i = 0; i < activity.feedInfoResult.getData().getDetail().getFeeds_tags().size(); i++) {
                                tagNames += activity.feedInfoResult.getData().getDetail().getFeeds_tags().get(i).getTag_name();
                                Tag tag = new Tag();
                                tag.setSelected(true);
                                tag.setName(activity.feedInfoResult.getData().getDetail().getFeeds_tags().get(i).getTag_name());
                                tag.setTag_id(activity.feedInfoResult.getData().getDetail().getFeeds_tags().get(i).getId());
                                taglist.add(tag);
                                activity.tags += activity.feedInfoResult.getData().getDetail().getFeeds_tags().get(i).getId();
                                if (i < (activity.feedInfoResult.getData().getDetail().getFeeds_tags().size() - 1)) {
                                    tagNames += ",";
                                    activity.tags += ",";
                                }
                            }
                            activity.tagList = taglist;
                            activity.tagTextView.setText(tagNames);
                        }
                        activity.industryTextView.setText(activity.feedInfoResult.getData().getDetail().getP_industry() + "/" +
                                activity.feedInfoResult.getData().getDetail().getIndustry());
                        activity.hobbyTextView.setText(activity.feedInfoResult.getData().getDetail().getP_hobby() + "/" +
                                activity.feedInfoResult.getData().getDetail().getHobby());
                        activity.is_secret = activity.feedInfoResult.getData().getDetail().getIs_secret();
                        activity.fanweiTextView.setText(activity.feedInfoResult.getData().getDetail().getSecret_desc());
                        activity.circle_id = activity.feedInfoResult.getData().getDetail().getCircle().getId();
                        activity.quanziTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle_name());
                    }
                    break;
                case 1:
                    activity.publishTextView.setClickable(true);
                    activity.waitDialog.dismiss();
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.finish();
                    }
                    break;
                case 2:
                    activity.publishTextView.setClickable(true);
                    activity.waitDialog.dismiss();
                    FailureToast.getInstance(activity).show("发布失败");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
        mImageList.add("add");
        initViews();
        initDialogs();
        if (feeds_id > 0) {
            feedInformation();
        }
    }

    private void initViews() {
        publishTextView = findViewById(R.id.publishTextView);
        publishTextView.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布动态");
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
        addTagLayout = findViewById(R.id.addTagLayout);
        addTagLayout.setOnClickListener(this);
        atquanziLayout = findViewById(R.id.atquanziLayout);
        atquanziLayout.setOnClickListener(this);
        fanweiLayout = findViewById(R.id.fanweiLayout);
        fanweiLayout.setOnClickListener(this);
        tagTextView = findViewById(R.id.tagTextView);
        atquanziTextView = findViewById(R.id.atquanziTextView);
        atquanziTextView.setText("@圈子");
        quanziTextView = findViewById(R.id.quanziTextView);
        fanweiTextView = findViewById(R.id.fanweiTextView);
        hobbyLayout = findViewById(R.id.hobbyLayout);
        hobbyLayout.setOnClickListener(this);
        hobbyTextView = findViewById(R.id.hobbyTextView);
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
        industryTextView = findViewById(R.id.industryTextView);
        contentEditText = findViewById(R.id.contentEditText);
        is_secret = 1;
        fanweiTextView.setText("公开");

    }

    private AddTagDialog addTagDialog;
    private CircleSelectDialog circleSelectDialog;
    private WhoCanSeeDialog whoCanSeeDialog;
    private IndustryHobbyDialog industryDialog, hobbyDialog;
    private ChoosePhotoDialog choosePhotoDialog;
    private List<Tag> tagList;
    private String tags = "";
    private String industry_parent, industry_child;
    private String hobby_parent, hobby_child;
    private boolean isSelectCircle = false;

    private void initDialogs() {
        addTagDialog = new AddTagDialog(this, this, R.style.dialog, new AddTagDialog.OnTagsSelectedListener() {
            @Override
            public void onTagsSelected(List<Tag> list) {
                tagList = list;
                tags = "";
                String tagNames = "";
                if (null != tagList && tagList.size() > 0) {
                    for (int i = 0; i < tagList.size(); i++) {
                        tags += tagList.get(i).getId();
                        tagNames += tagList.get(i).getName();
                        if (i < (tagList.size() - 1)) {
                            tags += ",";
                            tagNames += ",";
                        }
                    }
                }
                tagTextView.setText(tagNames);
            }
        });
        deleteImageDialog = new DeleteImageDialog(this, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                map.remove(mImageList.get(delete_position));
                mImageList.remove(delete_position);
                mImageList.remove("add");
                mImageList.add("add");
                if (mImageList.size() == 1) {
                    selectType = 0;
                }
                imageGridAdapter.setData(mImageList);
            }
        });
        circleSelectDialog = new CircleSelectDialog(this, R.style.dialog, 0, new CircleSelectDialog.OnCircleSeletedListener() {
            @Override
            public void onCircleSelected(MyCircle circle) {
                circleName = circle.getName();
                circle_id = circle.getId();
                quanziTextView.setText(circle.getName());
                if (circle_id > 0) {
                    isSelectCircle = true;
                }
            }
        });
        whoCanSeeDialog = new WhoCanSeeDialog(this, R.style.dialog, new WhoCanSeeDialog.OnWhoCanSeeListener() {
            @Override
            public void onWhoCanSee(WhoCanSee whoCanSee) {
                secretIndex = whoCanSee.getIndex();
                is_secret = whoCanSee.getIndex();
                fanweiTextView.setText(whoCanSee.getTitle());
            }
        });
        waitDialog = new LoadingDialog(this, R.style.dialog);


        choosePhotoDialog = new ChoosePhotoDialog(this, R.style.dialog, new ChoosePhotoDialog.OnChoosePhotoListener() {
            @Override
            public void xiangce() {
                if (mImageList.size() > 0 && selectType == 2) {
                    MessageToast.getInstance(PublishFeedActivity.this).show("动态不能同时发布图片和视频");
                } else {
                    selectImages();
                }
            }

            @Override
            public void paizhao() {
                if (mImageList.size() > 0 && selectType == 1) {
                    MessageToast.getInstance(PublishFeedActivity.this).show("动态不能同时发布图片和视频");
                } else {
                    selectVideo();
                }
            }
        });

        drafDialog = new DrafDialog(this, R.style.dialog, new DrafDialog.OnDrafListener() {
            @Override
            public void onConfirm() {
                is_draf = 1;
                publishFeed();
            }

            @Override
            public void onCancel() {

                finish();
            }
        });

        industryDialog = new IndustryHobbyDialog(this, R.style.dialog, true, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby p, IndustryHobby c) {
                industry_parent = p.getName();
                industry_child = c.getName();
                industryTextView.setText(p.getName() + "/" + c.getName());
            }
        });
        hobbyDialog = new IndustryHobbyDialog(this, R.style.dialog, false, new IndustryHobbyDialog.OnIndustryHobbySelectedListener() {
            @Override
            public void onIndustryHobbySelected(IndustryHobby p, IndustryHobby c) {
                hobby_parent = p.getName();
                hobby_child = c.getName();
                hobbyTextView.setText(p.getName() + "/" + c.getName());
            }
        });
    }

    private int circle_id;
    private int is_secret;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.addTagLayout:
                addTagDialog.show();
                if (null != tagList && tagList.size() > 0) {
                    addTagDialog.setTagList(tagList);
                }
                break;
            case R.id.atquanziLayout:
                circleSelectDialog.show();
                circleSelectDialog.setFeedsType(3);
                if (!StringUtils.isNullOrEmpty(circleName)) {
                    circleSelectDialog.setCircleName(circleName);
                }
                break;
            case R.id.fanweiLayout:
                whoCanSeeDialog.show();
                whoCanSeeDialog.setSelectCircle(isSelectCircle);
                if (null != feedInfoResult && secretIndex > 0) {
                    whoCanSeeDialog.setWhoCanSee(secretIndex);
                }
                break;
            case R.id.publishTextView:
                waitDialog.show();
                if (feeds_id > 0) {
                    updateFeed();
                } else {
                    publishFeed();
                }
                break;
            case R.id.hobbyLayout:
                hobbyDialog.show();
//                hobbyDialog.show();
//                if (null != feedInfoResult && !StringUtils.isNullOrEmpty(hobby_parent)
//                        && !StringUtils.isNullOrEmpty(hobby_child)) {
//                    hobbyDialog.setHobby(hobby_parent,
//                            hobby_child);
//                }
                break;
            case R.id.industryLayout:
                industryDialog.show();
//                industryDialog.show();
//                if (null != feedInfoResult && !StringUtils.isNullOrEmpty(industry_parent)
//                        && !StringUtils.isNullOrEmpty(industry_child)) {
//                    industryDialog.setIndustry(industry_parent,
//                            industry_child);
//                }
                break;
        }
    }

    @Override
    public void onAddImages() {
        choosePhotoDialog.show();
        choosePhotoDialog.setMenu("图片", "视频");
    }

    //1.img,2.video
    private int selectType = 0;

    private void selectImages() {
        selectType = 1;
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
                .start(PublishFeedActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    private void selectVideo() {
        selectType = 2;
        mImageList.remove("add");
        System.out.println("video mImageList: " + mImageList.size());
        ImagePicker.getInstance()
                .setTitle("视频选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(false)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(PublishFeedActivity.this, REQUEST_SELECT_VIDEO_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
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

        if (requestCode == REQUEST_SELECT_VIDEO_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            mImageList.add("add");
            imageGridAdapter.setData(mImageList);
            UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, mImageList.get(0), new UploadImageUtils.OnUploadCallback() {
                @Override
                public void onUploadSuccess(String name) {
                    video_url = name;
                    System.out.println("视频上传video_url回调: " + video_url);
                    VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(video_url, new VideoUtils.OnCaculateVideoWidthHeightListener() {
                        @Override
                        public void onVideoWidthHeight(float w, float h, float vt) {
                            video_width = (int) w;
                            video_height = (int) h;
                        }
                    });
                }
            });

        }
    }

    private List<String> imagesUploadList = new ArrayList<>();

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
//                System.out.println("path: "+path);
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


    private DeleteImageDialog deleteImageDialog;
    private int delete_position = -1;

    @Override
    public void onDeleteImage(int position) {
        if (null != deleteImageDialog) {
            delete_position = position;
            deleteImageDialog.show();
        }
    }

    private BaseResult baseResult;
    private int is_draf;//是否草稿1是

    /**
     * 发布动态
     */
    private void publishFeed() {
        publishTextView.setClickable(false);
        if (null != map && map.size() > 0) {
            List<String> list = imageGridAdapter.getList();
            uploadImageList.clear();
            for (String key : list) {
                if (!key.equals("add"))
                    uploadImageList.add(ImageUtils.getInstance().getUploadImage(key, map.get(key)));
            }
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PUBLISH_FEED);
        params.addBodyParameter("type", "3");
        params.addBodyParameter("is_draf", String.valueOf(is_draf));
        params.addBodyParameter("tags", tags);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("p_industry", industry_parent);
        params.addBodyParameter("feeds_type", "3");
        params.addBodyParameter("industry", industry_child);//行业
        params.addBodyParameter("p_hobby", hobby_parent);
        params.addBodyParameter("hobby", hobby_child);//兴趣
        params.addBodyParameter("content", contentEditText.getText().toString().trim());

        if (null != uploadImageList && uploadImageList.size() > 0) {
            params.addBodyParameter("photos", GsonUtils.GsonString(uploadImageList));
        }
        params.addBodyParameter("is_secret", String.valueOf(is_secret));
        if (!StringUtils.isNullOrEmpty(video_url)) {
            params.addBodyParameter("video_url", video_url);
            params.addBodyParameter("video_width", String.valueOf(video_width));
            params.addBodyParameter("video_height", String.valueOf(video_height));
        }
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("province_name", SPUtils.getInstance().getLocationProvince(BaseApplication.applicationContext));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println(baseResult.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("发布动态: " + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void updateFeed() {
        publishTextView.setClickable(false);
        if (null != map && map.size() > 0) {
            List<String> list = imageGridAdapter.getList();
            uploadImageList.clear();
            for (String key : list) {
                if (!key.equals("add")) {
                    if (key.startsWith("http://") || key.startsWith("https://")) {
                        if (null != feedInfoResult.getData().getDetail().getImgs()) {
                            for (int i = 0; i < feedInfoResult.getData().getDetail().getImgs().size(); i++) {
                                if (key.equals(feedInfoResult.getData().getDetail().getImgs().get(i).getFile())) {
                                    UploadImage image = new UploadImage();
                                    image.setFile(key);
                                    image.setWidth(feedInfoResult.getData().getDetail().getImgs().get(i).getWidth());
                                    image.setHeight(feedInfoResult.getData().getDetail().getImgs().get(i).getHeight());
                                    uploadImageList.add(image);
                                }
                            }
                        }
                    } else {
                        uploadImageList.add(ImageUtils.getInstance().getUploadImage(key, map.get(key)));
                    }
                }
            }
        }
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_FEED);
        params.addBodyParameter("type", "3");
        params.addBodyParameter("is_draf", String.valueOf(is_draf));
        params.addBodyParameter("tags", tags);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("p_industry", industry_parent);
        params.addBodyParameter("industry", industry_child);//行业
        params.addBodyParameter("p_hobby", hobby_parent);
        params.addBodyParameter("hobby", hobby_child);//兴趣
        params.addBodyParameter("feeds_type", "3");
        params.addBodyParameter("content", contentEditText.getText().toString().trim());
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        if (null != uploadImageList && uploadImageList.size() > 0) {
            params.addBodyParameter("photos", GsonUtils.GsonString(uploadImageList));
        }
        params.addBodyParameter("is_secret", String.valueOf(is_secret));
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("province_name", SPUtils.getInstance().getLocationProvince(BaseApplication.applicationContext));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println(baseResult.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("发布动态: " + ex.toString());

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
            if (!StringUtils.isNullOrEmpty(contentEditText.getText().toString().trim()) ||
                    !StringUtils.isNullOrEmpty(video_url) ||
                    uploadImageList.size() > 0) {
                drafDialog.show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private FeedInfoResult feedInfoResult;

    private void feedInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("feedInformation: " + result);
                feedInfoResult = GsonUtils.GsonToBean(result, FeedInfoResult.class);
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("feedInformation: " + ex.toString());
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
