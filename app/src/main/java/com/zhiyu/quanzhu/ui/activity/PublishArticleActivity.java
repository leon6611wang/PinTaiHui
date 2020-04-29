package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ArticleContent;
import com.zhiyu.quanzhu.model.bean.ArticleInformation;
import com.zhiyu.quanzhu.model.bean.FeedsTag;
import com.zhiyu.quanzhu.model.bean.Tag;
import com.zhiyu.quanzhu.model.result.AddFeedResult;
import com.zhiyu.quanzhu.model.result.ArticleInformationResult;
import com.zhiyu.quanzhu.ui.dialog.AddTagDialog;
import com.zhiyu.quanzhu.ui.dialog.DrafDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布文章
 * 第一步
 */
public class PublishArticleActivity extends BaseActivity implements View.OnClickListener, PublishArticleContentEditActivity.OnSaveArticleContentListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private AddTagDialog addTagDialog;
    private DrafDialog drafDialog;
    private LinearLayout addTagLayout, editContentLayout;
    private TextView addTagTextView, nextButton;
    private String tagIds = "";
    private String tags = "";
    private ArrayList<Tag> tagList = new ArrayList<>();
    private int feeds_id;
    private EditText titleEditText;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishArticleActivity> activityWeakReference;

        public MyHandler(PublishArticleActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishArticleActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (activity.articleInformationResult.getCode() == 200) {
                        activity.titleEditText.setText(activity.articleInformationResult.getData().getDetail().getTitle());
                        if (null != activity.articleInformationResult.getData().getDetail().getFeeds_tags() && activity.articleInformationResult.getData().getDetail().getFeeds_tags().size() > 0) {
                            for (int i = 0; i < activity.articleInformationResult.getData().getDetail().getFeeds_tags().size(); i++) {
                                Tag tag = new Tag();
                                tag.setName(activity.articleInformationResult.getData().getDetail().getFeeds_tags().get(i).getTag_name());
                                tag.setTag_id(activity.articleInformationResult.getData().getDetail().getFeeds_tags().get(i).getTag_id());
                                activity.tagList.add(tag);
                                activity.tags += activity.articleInformationResult.getData().getDetail().getFeeds_tags().get(i).getTag_name();
                                activity.tagIds += activity.articleInformationResult.getData().getDetail().getFeeds_tags().get(i).getTag_id();
                                if (i < activity.articleInformationResult.getData().getDetail().getFeeds_tags().size() - 1) {
                                    activity.tags += ",";
                                    activity.tagIds += ",";
                                }
                            }
                            activity.addTagTextView.setText(activity.tags);
                            activity.contentList = activity.articleInformationResult.getData().getDetail().getContent();
                        }
                    }
                    break;
                case 1:

                    break;
                case 2:
                    Toast.makeText(activity, activity.addFeedResult.getMsg(), Toast.LENGTH_SHORT).show();
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
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.goToPublishSetting();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        feeds_id = getIntent().getIntExtra("article_id", 0);
        PublishArticleContentEditActivity.setOnSaveArticleContentListener(this);
        initDialogs();
        initViews();
        if (feeds_id > 0) {
            articleInformation();
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
                    addTagTextView.setText(tags);
                }
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
    }

    private void goToPublishSetting() {
        Intent paramsIntent = new Intent(PublishArticleActivity.this, PublishParamSettingActivity.class);
        paramsIntent.putExtra("feeds_id", feeds_id);
        paramsIntent.putExtra("publishType", 1);
        startActivity(paramsIntent);
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布文章");
        addTagLayout = findViewById(R.id.addTagLayout);
        addTagLayout.setOnClickListener(this);
        addTagTextView = findViewById(R.id.addTagTextView);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        editContentLayout = findViewById(R.id.editContentLayout);
        editContentLayout.setOnClickListener(this);
        titleEditText = findViewById(R.id.titleEditText);
    }

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
            case R.id.nextButton:
                if (feeds_id > 0) {
                    updateFeed();
                } else {
                    addFeed();
                }

//                Intent paramIntent = new Intent(PublishArticleActivity.this, PublishParamSettingActivity.class);
//                startActivity(paramIntent);
                break;
            case R.id.editContentLayout:
                Intent editContentIntent = new Intent(PublishArticleActivity.this, PublishArticleContentEditActivity.class);
                if (null != contentList && contentList.size() > 0) {
                    String json = GsonUtils.GsonString(contentList);
                    editContentIntent.putExtra("contentList", json);
                }
                startActivity(editContentIntent);
                break;
        }
    }

    private List<ArticleContent> contentList;

    @Override
    public void onSaveArticleContent(List<ArticleContent> list) {
        this.contentList = list;
        System.out.println("2 --> 图文： " + contentList.size());
    }

    private int is_draf;//是否草稿 1是
    private AddFeedResult addFeedResult;

    private void addFeed() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_FEED);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("is_draf", String.valueOf(is_draf));
        params.addBodyParameter("content", GsonUtils.GsonString(contentList));
        params.addBodyParameter("tags", tagIds);
        params.addBodyParameter("title", titleEditText.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                addFeedResult = GsonUtils.GsonToBean(result, AddFeedResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private BaseResult baseResult;

    private void updateFeed() {
        String content = GsonUtils.GsonString(contentList);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.UPDATE_FEED);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("is_draf", String.valueOf(is_draf));
        params.addBodyParameter("content", content);
        params.addBodyParameter("tags", tagIds);
        params.addBodyParameter("title", titleEditText.getText().toString().trim());
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!StringUtils.isNullOrEmpty(titleEditText.getText().toString().trim()) ||
                    !StringUtils.isNullOrEmpty(tags) ||
                    (null != contentList && contentList.size() > 0)) &&
                    feeds_id == 0
                    ) {
                drafDialog.show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private ArticleInformationResult articleInformationResult;

    private void articleInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                articleInformationResult = GsonUtils.GsonToBean(result, ArticleInformationResult.class);
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("articleInformation: " + ex.toString());
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
