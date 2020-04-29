package com.zhiyu.quanzhu.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.result.FeedCommentResult;
import com.zhiyu.quanzhu.model.result.FeedInfoResult;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoCommentListParentAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.VideoCoverUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 *
 */
public class VideoInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout videoLayout, contentLayout;
    private FrameLayout bottomCommentLayout;
    private FrameLayout.LayoutParams videoLayoutParams, contentLayoutParams;
    private LinearLayout.LayoutParams videoParams;
    private ListView listView;
    private View listHeaderView;
    private TextView contentTextView, timeTextView, commentCountTextView0, commentCountTextView, priseCountTextView0, priseCountTextView, nameTextView, followTextView;
    private HorizontalListView labelHorizontalListView;
    private EditText commentEditText0, commentEditText;
    private FrameLayout priseLayout0, priseLayout;
    private ImageView priseImageView0, priseImageView, shareImageView0, shareImageView;
    private LinearLayout backLayout;
    private CircleImageView avatarImageView;

    private ArticleInfoCommentListParentAdapter adapter;
    private int screenWidth, screenHeight, videoWidth, videoHeight, videoTime, dp_200;
    private float scale = 0.0f;
    private VideoPlayerTrackView videoPlayer;
    private String videoUrl1 = "https://flv3.bn.netease.com/51e2c823f07b06ce988e8625fb6ece1a5c056467358d35585638a508eb04cbce0930f987af8e90d939b33df3b64f2a1218119904be4b4d3fd8ad8f6e16841358620d2c056d9de8388bfb7dc1be3a5c69be8bc7d8392ca4aae11f980d094409ed9d392f3bdcb69f20973dfd1f7cedfe907538d5a89d53db39.mp4";
    private String videoUrl2 = "https://flv3.bn.netease.com/51e2c823f07b06ce52dd6ec0a6c8f02bf8c8a82ca2df65fb9120ff2145ec501ef1b2bce7d0452e01843b0b62ad288bf47a3d1c8117227421e9f875a458abf91b5acb6e74c596403bcf77c8092f34ed85910d493006db2282baea8f2eea4f906f541d4e9d144c2d9026880971649ea00a27fda5ba486a512a.mp4";
    private String videoUrl = videoUrl2;
    private int feeds_id, comment_id,myCommentId;
    private String commentContent;
    private ShareDialog shareDialog;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<VideoInformationActivity> activityWeakReference;

        public MyHandler(VideoInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.initLayoutParams();
                    break;
                case 2:
                    activity.adapter.setList(activity.list);
                    break;
                case 3:
                    Glide.with(activity).load(activity.feedInfoResult.getData().getDetail().getAvatar()).into(activity.avatarImageView);
                    activity.nameTextView.setText(activity.feedInfoResult.getData().getDetail().getUsername());
                    activity.contentTextView.setText(activity.feedInfoResult.getData().getDetail().getDesc());
                    activity.timeTextView.setText(activity.feedInfoResult.getData().getDetail().getTime());
                    activity.commentCountTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getComment_num()));
                    activity.commentCountTextView0.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getComment_num()));
                    if (!activity.feedInfoResult.getData().getDetail().isIs_follow()) {
                        activity.followTextView.setVisibility(View.VISIBLE);
                    } else {
                        activity.followTextView.setVisibility(View.GONE);
                    }
                    activity.priseCountTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getPrise_num()));
                    activity.priseCountTextView0.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getPrise_num()));
                    activity.playVideo(activity.feedInfoResult.getData().getDetail().getVideo_url());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
        myCommentId=getIntent().getIntExtra("myCommentId",0);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(this);
        dp_200 = (int) getResources().getDimension(R.dimen.dp_200);
        scale = (float) dp_200 / (float) screenHeight;
        contentLayoutParams = new FrameLayout.LayoutParams(screenWidth, Math.round(screenHeight - dp_200));
        contentLayoutParams.gravity = Gravity.BOTTOM;
        initViews();
        initDialogs();
        initHideContentLayout();
        videoInfo();
        commentList();
    }

    private void initViews() {
        videoLayout = findViewById(R.id.videoLayout);
        videoLayout.setOnClickListener(this);
        contentLayout = findViewById(R.id.contentLayout);
        contentLayout.setLayoutParams(contentLayoutParams);
        listHeaderView = LayoutInflater.from(this).inflate(R.layout.header_video_information_listview, null);
        listView = findViewById(R.id.listView);
        adapter = new ArticleInfoCommentListParentAdapter(this);
        listView.setAdapter(adapter);
        listView.addHeaderView(listHeaderView);
        videoPlayer = findViewById(R.id.videoPlayer);
        bottomCommentLayout = findViewById(R.id.bottomCommentLayout);
        bottomCommentLayout.setOnClickListener(this);

        contentTextView = findViewById(R.id.contentTextView);
        timeTextView = findViewById(R.id.timeTextView);
        commentCountTextView0 = findViewById(R.id.commentCountTextView0);
        commentCountTextView = findViewById(R.id.commentCountTextView);
        priseCountTextView0 = findViewById(R.id.priseCountTextView0);
        priseCountTextView = findViewById(R.id.priseCountTextView);
        nameTextView = findViewById(R.id.nameTextView);
        followTextView = findViewById(R.id.followTextView);
        labelHorizontalListView = findViewById(R.id.labelHorizontalListView);
        commentEditText0 = findViewById(R.id.commentEditText0);
        commentEditText0.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(VideoInformationActivity.this);
                    commentContent = commentEditText.getText().toString().trim();
                    commentEditText.setText(null);
                    feedComment();
                    return true;
                }
                return false;
            }
        });
        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(VideoInformationActivity.this);
                    commentContent = commentEditText.getText().toString().trim();
                    commentEditText.setText(null);
                    feedComment();
                    return true;
                }
                return false;
            }
        });

        priseLayout0 = findViewById(R.id.priseLayout0);
        priseLayout0.setOnClickListener(this);
        priseLayout = findViewById(R.id.priseLayout);
        priseLayout.setOnClickListener(this);
        priseImageView0 = findViewById(R.id.priseImageView0);
        priseImageView = findViewById(R.id.priseImageView);
        shareImageView0 = findViewById(R.id.shareImageView0);
        shareImageView0.setOnClickListener(this);
        shareImageView = findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        avatarImageView = findViewById(R.id.avatarImageView);

    }

    private void playVideo(String url) {
        videoPlayer.setDataSource(url, null);
        videoPlayer.startPlayVideo();
        Glide.with(this).load(feedInfoResult.getData().getDetail().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(videoPlayer.getCoverController().getVideoCover());
        videoWidth=feedInfoResult.getData().getDetail().getVideo_width();
        videoHeight=feedInfoResult.getData().getDetail().getVideo_height();
        initLayoutParams();
    }

    private void initDialogs() {
        shareDialog = new ShareDialog(this, this, R.style.dialog);
    }

    private void initLayoutParams() {
        videoLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        videoLayoutParams.gravity = Gravity.CENTER;
        videoLayout.setLayoutParams(videoLayoutParams);
        int width, height;
        if (videoWidth >= videoHeight) {//宽视频，视频宽度=屏幕宽度，计算对应视频高度
            height = Math.round(screenWidth * videoHeight / videoWidth);
            videoParams = new LinearLayout.LayoutParams(screenWidth, height);
        } else {//窄视频，视频高度=屏幕高度，计算对应视频宽度
            width = Math.round(screenHeight * videoWidth / videoHeight);
            videoParams = new LinearLayout.LayoutParams(width, screenHeight);
        }
        videoParams.gravity = Gravity.CENTER;
        videoPlayer.setLayoutParams(videoParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomCommentLayout:
                if (isLarge) {
                    narrow();
                    moveTop();
                    showContentLayout();
                } else {
                    enlarge();
                    moveCenter();
                    hideContentLayout();
                }
                break;
            case R.id.videoLayout:
                if (isLarge) {
                    narrow();
                    moveTop();
                    showContentLayout();
                } else {
                    enlarge();
                    moveCenter();
                    hideContentLayout();
                }
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.shareImageView:
                shareDialog.show();
                break;
            case R.id.shareImageView0:
                shareDialog.show();
                break;

        }
    }


    private boolean isLarge = true;

    /**
     * 缩小
     */
    private void narrow() {
        isLarge = false;
        if (videoHeight > videoWidth) {
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(videoPlayer, "scaleY", 1, scale, scale);
            scaleY.setDuration(500);
            scaleY.start();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(videoPlayer, "scaleX", 1, scale, scale);
            scaleX.setDuration(500);
            scaleX.start();
        }
    }

    /**
     * 放大
     */
    private void enlarge() {
        isLarge = true;
        if (videoHeight > videoWidth) {
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(videoPlayer, "scaleY", scale, 1, 1);
            scaleY.setDuration(500);
            scaleY.start();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(videoPlayer, "scaleX", scale, 1, 1);
            scaleX.setDuration(500);
            scaleX.start();
        }
    }

    /**
     * 视频移动到顶部
     */
    private void moveTop() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(videoPlayer, "translationY", 0.0f, -(screenHeight - dp_200) / 2, -(screenHeight - dp_200) / 2);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    /**
     * 视频移动到中间
     */
    private void moveCenter() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(videoPlayer, "translationY", -(screenHeight - dp_200) / 2, 0.0f, 0.0f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    /**
     * 初始化隐藏评论内容区域
     */
    private void initHideContentLayout() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", 0.0f, Math.round(screenHeight - dp_200), Math.round(screenHeight - dp_200));
        objectAnimator.start();
    }

    /**
     * 隐藏评论
     */
    private void hideContentLayout() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", 0.0f, Math.round(screenHeight - dp_200), Math.round(screenHeight - dp_200));
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    /**
     * 显示评论
     */
    private void showContentLayout() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", Math.round(screenHeight - dp_200), 0, 0);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }


    private FeedCommentResult commentResult;
    private List<FeedCommentParent> list;
    private int page = 1;
    private boolean isRefresh = true;

    private void commentList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_COMMENTS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(25));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                commentResult = GsonUtils.GsonToBean(result, FeedCommentResult.class);
//                System.out.println(result);
                if (isRefresh) {
                    list = commentResult.getData().getList();
                } else {
                    list.addAll(commentResult.getData().getList());
                }
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

    private FeedInfoResult feedInfoResult;

    private void videoInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("comment_id",String.valueOf(myCommentId));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("videoInfo: " + result);
                feedInfoResult = GsonUtils.GsonToBean(result, FeedInfoResult.class);
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

    private BaseResult baseResult;

    /**
     * 评论/回复评论
     */
    private void feedComment() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_COMMENT);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        params.addBodyParameter("content", commentContent);
        commentContent = null;
        comment_id = 0;
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("feedComment: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("feedComment: " + ex.toString());
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
