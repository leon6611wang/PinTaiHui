package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.bean.UploadImage;
import com.zhiyu.quanzhu.model.bean.WhoCanSee;
import com.zhiyu.quanzhu.ui.adapter.ComplaintImagesGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.ChoosePhotoDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.DrafDialog;
import com.zhiyu.quanzhu.ui.dialog.HobbyDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.dialog.WaitDialog;
import com.zhiyu.quanzhu.ui.dialog.WhoCanSeeDialog;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.RecyclerScrollView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GetPhotoFromPhotoAlbum;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MediaUtils;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private WaitDialog waitDialog;
    private String video_url;
    private int video_width, video_height;
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
                case 1:
                    activity.waitDialog.dismiss();
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_publish_feed);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        mImageList.add("add");
        initViews();
        initDialogs();
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
    private IndustryDialog industryDialog;
    private HobbyDialog hobbyDialog;
    private ChoosePhotoDialog choosePhotoDialog;
    private List<Tag> tagList;
    private String tags = "";
    private String industry_parent, industry_child;
    private String hobby_parent, hobby_child;

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
                imageGridAdapter.setData(mImageList);
            }
        });
        circleSelectDialog = new CircleSelectDialog(this, R.style.dialog, new CircleSelectDialog.OnCircleSeletedListener() {
            @Override
            public void onCircleSelected(MyCircle circle) {
                circle_id = circle.getId();
                quanziTextView.setText(circle.getName());
            }
        });
        whoCanSeeDialog = new WhoCanSeeDialog(this, R.style.dialog, new WhoCanSeeDialog.OnWhoCanSeeListener() {
            @Override
            public void onWhoCanSee(WhoCanSee whoCanSee) {
                is_secret = whoCanSee.getIndex();
                fanweiTextView.setText(whoCanSee.getTitle());
                System.out.println(whoCanSee);
            }
        });
        waitDialog = new WaitDialog(this, R.style.dialog);

        industryDialog = new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {
                industry_parent = parent.getName();
                industry_child = child.getName();
                industryTextView.setText(parent.getName() + "/" + child.getName());
            }
        });
        hobbyDialog = new HobbyDialog(this, R.style.dialog, new HobbyDialog.OnChooseHobbyListener() {
            @Override
            public void onChooseHobby(HobbyDaoParent parent, HobbyDaoChild child) {
                hobby_parent = parent.getName();
                hobby_child = child.getName();
                hobbyTextView.setText(parent.getName() + "/" + child.getName());
            }
        });

        choosePhotoDialog = new ChoosePhotoDialog(this, R.style.dialog, new ChoosePhotoDialog.OnChoosePhotoListener() {
            @Override
            public void xiangce() {
                selectImages();
            }

            @Override
            public void paizhao() {
                selectVideo();
            }
        });

        drafDialog = new DrafDialog(this, R.style.dialog, new DrafDialog.OnDrafListener() {
            @Override
            public void onConfirm() {
                finish();
            }

            @Override
            public void onCancel() {
                is_draf = 1;
                publishFeed();
                finish();
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
                break;
            case R.id.atquanziLayout:
                circleSelectDialog.show();
                break;
            case R.id.fanweiLayout:
                whoCanSeeDialog.show();
                break;
            case R.id.publishTextView:
                waitDialog.show();
                waitDialog.setNotice("正在发布，请稍等...");
                publishFeed();
                break;
            case R.id.hobbyLayout:
                hobbyDialog.show();
                break;
            case R.id.industryLayout:
                industryDialog.show();
                break;
        }
    }

    @Override
    public void onAddImages() {
        choosePhotoDialog.show();
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
                .start(PublishFeedActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
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
        params.addBodyParameter("industry", industry_child);//行业
        params.addBodyParameter("p_hobby", hobby_parent);
        params.addBodyParameter("hobby", hobby_child);//兴趣
        params.addBodyParameter("content", contentEditText.getText().toString().trim());
        params.addBodyParameter("video_url", video_url);
        params.addBodyParameter("video_width", String.valueOf(video_width));
        params.addBodyParameter("video_height", String.valueOf(video_height));
        System.out.println("photos size: " + uploadImageList.size());
        if (null != uploadImageList && uploadImageList.size() > 0) {
            params.addBodyParameter("photos", GsonUtils.GsonString(uploadImageList));
        }
        params.addBodyParameter("is_secret", String.valueOf(is_secret));
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


}
