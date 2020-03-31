package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.ArticleContent;
import com.zhiyu.quanzhu.ui.dialog.ArticleImageClickDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 发布文章-正文编辑
 */
public class PublishArticleContentEditActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView, rightTextView;
    private LinearLayout contentEditLayout;
    private ImageView insertImageImageView;
    private ArticleImageClickDialog articleImageClickDialog;
    private List<View> viewList = new ArrayList<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();//相册已选
    private ArrayList<String> imgList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();//本地图片上传存放
    private List<String> imagesUploadList = new ArrayList<>();//已上传的图片
    private int screenWidth;
    private LoadingDialog loadingDialog;
    private MyHandler myHandler = new MyHandler(this);
    private int imageUploadCount = 1;

    private static class MyHandler extends Handler {
        WeakReference<PublishArticleContentEditActivity> articleContentEditActivityWeakReference;

        public MyHandler(PublishArticleContentEditActivity activity) {
            articleContentEditActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishArticleContentEditActivity activity = articleContentEditActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    System.out.println("imageUploadCount: " + activity.imageUploadCount);
                    if (activity.imageUploadCount == activity.mImageList.size()) {
//                        activity.loadingDialog.dismiss();
                        activity.saveArticleContent(activity.list);
//                        Toast.makeText(activity, "图片已上传完成", Toast.LENGTH_SHORT).show();
                    }
                    activity.imageUploadCount++;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article_content_edit);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        initDialog();
        initViews();
        insertEditText(0);
    }

    private void initDialog() {
        articleImageClickDialog = new ArticleImageClickDialog(this, R.style.dialog, new ArticleImageClickDialog.OnArticleImageClickListener() {
            @Override
            public void onArticleImageClick(int position, int index) {
                switch (position) {
                    case 1:
                        contentEditLayout.removeViewAt(index);
                        viewList.remove(index);
                        mImageList.remove(imageListIndex);
                        break;
                    case 2:
                        insertEditText(index);
                        break;
                    case 3:
                        insertEditText(index + 1);
                        break;
                }
            }
        });

        loadingDialog = new LoadingDialog(this, R.style.dialog);
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
        contentEditLayout = findViewById(R.id.contentEditLayout);
        insertImageImageView = findViewById(R.id.insertImageImageView);
        insertImageImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insertImageImageView:
                selectImages();
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                loadingDialog.show();
                uploadImages();
                break;
        }
    }


    private void saveArticleContent(List<ArticleContent> articleContentList) {
        int imgIndex = 0;
        if (viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                if (viewList.get(i) instanceof ImageView) {
                    articleContentList.get(imgIndex).setIndex(i);
                    imgIndex++;
                } else if (viewList.get(i) instanceof EditText) {
                    EditText et = (EditText) viewList.get(i);
                    if (!StringUtils.isNullOrEmpty(et.getText().toString().trim())) {
                        ArticleContent content = new ArticleContent();
                        content.setIndex(i);
                        content.setContent(et.getText().toString());
                        content.setType(1);
                        articleContentList.add(content);
                    }
                }
            }
        } else {
            Toast.makeText(this, "请编辑文章内容.", Toast.LENGTH_SHORT).show();
        }
        loadingDialog.dismiss();
        if(null!=onSaveArticleContentListener){
            onSaveArticleContentListener.onSaveArticleContent(articleContentList);
        }
        finish();
        System.out.println("article content list: " + articleContentList.size());
    }

    private int imageListIndex = -1;

    private void insertImage(final String imageUrl, int imageWidth, int imageHeight) {
        int id = (int) ((Math.random() * 9 + 1) * 100000);
        int dp_5 = (int) getResources().getDimension(R.dimen.dp_5);
        int dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        int height = Math.round((screenWidth - dp_15 * 2) * ((float) imageHeight / (float) imageWidth));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth - dp_15 * 2, height);
        params.gravity = Gravity.CENTER;
        params.topMargin = dp_5;
        params.bottomMargin = dp_5;
        params.leftMargin = dp_15;
        params.rightMargin = dp_15;
        ImageView imageView = new ImageView(this);
        imageView.setId(id);
        imageView.setLayoutParams(params);
        contentEditLayout.addView(imageView);
        Glide.with(this).load(imageUrl).into(imageView);
        viewList.add(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = -1;
                for (int i = 0; i < viewList.size(); i++) {
                    if (viewList.get(i).getId() == v.getId()) {
                        currentIndex = i;
                    }
                }

                for (int i = 0; i < mImageList.size(); i++) {
                    if (mImageList.get(i).equals(imageUrl)) {
                        imageListIndex = i;
                    }
                }

                boolean showTop = true, showBottom = true;
                int qianIndex = currentIndex - 1, houIndex = currentIndex + 1;
                if (qianIndex < 0) {
                    qianIndex = 0;
                }
                if (houIndex > viewList.size() - 1) {
                    houIndex = viewList.size() - 1;
                }
                if (viewList.get(qianIndex) instanceof EditText) {
                    showTop = false;
                }
                if (viewList.get(houIndex) instanceof EditText) {
                    showBottom = false;
                }
                System.out.println("showTop: " + showTop + " , showBottom: " + showBottom);
                articleImageClickDialog.show();
                articleImageClickDialog.setIndex(currentIndex);
                articleImageClickDialog.showTop(showTop);
                articleImageClickDialog.showBottom(showBottom);
            }
        });
    }

    private void insertEditText(int index) {
        int dp_5 = (int) getResources().getDimension(R.dimen.dp_5);
        int dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_100));
        params.gravity = Gravity.CENTER;
        params.topMargin = dp_5;
        params.bottomMargin = dp_5;
        params.leftMargin = dp_15;
        params.rightMargin = dp_15;
        EditText editText = new EditText(this);
        editText.setLayoutParams(params);
        editText.setBackground(getResources().getDrawable(R.drawable.shape_edit_bg));
        editText.setTextSize(13);
        editText.setTextColor(getResources().getColor(R.color.text_color_black));
        editText.setHint("编辑文字内容");
        editText.setHintTextColor(getResources().getColor(R.color.text_color_gray));
        editText.setPadding((int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10));
        editText.setGravity(Gravity.TOP);
        contentEditLayout.addView(editText, index);
        viewList.add(index, editText);
    }

    private void selectImages() {
        imgList.clear();
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(imgList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(PublishArticleContentEditActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            imgList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            mImageList.addAll(imgList);
            for (String imageUrl : imgList) {
                int[] wh = ImageUtils.getInstance().getLocalImageWidthHeight(imageUrl);
                insertImage(imageUrl, wh[0], wh[1]);
            }
        }

    }

    private List<ArticleContent> list = new ArrayList<>();

    private void uploadImages() {
        if (mImageList.size() > 0) {
            int count = mImageList.size();
            System.out.println("count: " + count);
            for (final String path : mImageList) {
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                            @Override
                            public void onUploadSuccess(String name) {
                                ArticleContent articleContent = new ArticleContent();
                                articleContent.setContent(name);
                                articleContent.setType(2);
                                int[] wh = ImageUtils.getInstance().getLocalImageWidthHeight(path);
                                articleContent.setWidth(wh[0]);
                                articleContent.setHeight(wh[1]);
                                list.add(articleContent);
                                System.out.println("list size: " + list.size());
                                Message message = myHandler.obtainMessage(1);
                                message.sendToTarget();
                            }
                        });
                    }
                });
            }

        }
    }


    private static OnSaveArticleContentListener onSaveArticleContentListener;
    public static void setOnSaveArticleContentListener(OnSaveArticleContentListener listener){
        onSaveArticleContentListener=listener;
    }
    public interface OnSaveArticleContentListener{
        void onSaveArticleContent(List<ArticleContent> list);
    }

}
