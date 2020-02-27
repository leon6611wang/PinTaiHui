package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.bean.WhoCanSee;
import com.zhiyu.quanzhu.ui.adapter.ComplaintImagesGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.PublishFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.WhoCanSeeDialog;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.RecyclerScrollView;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布动态
 */
public class PublishFeedActivity extends BaseActivity implements View.OnClickListener, PublishFeedImagesGridAdapter.OnAddImagesListener, PublishFeedImagesGridAdapter.OnDeleteImageListener {
    private LinearLayout backLayout, addTagLayout, atquanziLayout, fanweiLayout;
    private TextView titleTextView, tagTextView, atquanziTextView, quanziTextView, fanweiTextView;
    private MyRecyclerView imageGridView;
    private PublishFeedImagesGridAdapter imageGridAdapter;
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private List<String> uploadImgList = new ArrayList<>();
    private RecyclerScrollView mScrollView;


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
//        mScrollView = findViewById(R.id.mScrollView);
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

    }

    private AddTagDialog addTagDialog;
    private CircleSelectDialog circleSelectDialog;
    private WhoCanSeeDialog whoCanSeeDialog;
    private void initDialogs() {
        addTagDialog = new AddTagDialog(this,this, R.style.dialog);
        deleteImageDialog = new DeleteImageDialog(this, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                mImageList.remove(delete_position);
                mImageList.remove("add");
                mImageList.add("add");
                imageGridAdapter.setData(mImageList);
            }
        });
        circleSelectDialog=new CircleSelectDialog(this,R.style.dialog,new CircleSelectDialog.OnCircleSeletedListener(){
            @Override
            public void onCircleSelected(MyCircle circle) {
                System.out.println(circle);
            }
        });
        whoCanSeeDialog=new WhoCanSeeDialog(this, R.style.dialog, new WhoCanSeeDialog.OnWhoCanSeeListener() {
            @Override
            public void onWhoCanSee(WhoCanSee whoCanSee) {
                System.out.println(whoCanSee);
            }
        });

    }

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
        }
    }

    @Override
    public void onAddImages() {
        selectImages();
    }


    private void selectImages() {
        mImageList.remove("add");
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(PublishFeedActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
//            uploadImages();
            mImageList.add("add");
            imageGridAdapter.setData(mImageList);
        }
    }

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
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
}
