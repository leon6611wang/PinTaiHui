package com.zhiyu.quanzhu.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
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
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.bean.FeedsGoods;
import com.zhiyu.quanzhu.model.result.FeedCommentResult;
import com.zhiyu.quanzhu.model.result.FeedInfoResult;
import com.zhiyu.quanzhu.model.result.FeedsGoodsResult;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoCommentListParentAdapter;
import com.zhiyu.quanzhu.ui.adapter.FeedsGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
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
public class VideoInformationActivity extends BaseActivity implements View.OnClickListener, ArticleInfoCommentListParentAdapter.OnReplyParentCommentListener {
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
    private LinearLayout backLayout, headerUserLayout;
    private CircleImageView avatarImageView;
    private ArticleInfoCommentListParentAdapter adapter;
    private int screenWidth, screenHeight, videoWidth, videoHeight, videoTime, dp_200;
    private float scale = 0.0f;
    private VideoPlayerTrackView videoPlayer;
    private int feeds_id, comment_id, myCommentId, user_id;
    private String commentContent;
    private ShareDialog shareDialog;
    private int fromCircleInfo = 0;//0:默认值，不是圈子详情来源，1：来自圈子详情，点击圈子不跳转到圈子详情
    private boolean is_follow = false, is_prise = false;
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
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;
                case 1:
                    activity.initLayoutParams();
                    break;
                case 2:
                    activity.adapter.setList(activity.list);
                    break;
                case 3:
                    Glide.with(activity).load(activity.feedInfoResult.getData().getDetail().getAvatar()).into(activity.avatarImageView);
                    activity.nameTextView.setText(activity.feedInfoResult.getData().getDetail().getUsername());
                    activity.contentTextView.setText(activity.feedInfoResult.getData().getDetail().getContent());
                    activity.timeTextView.setText(activity.feedInfoResult.getData().getDetail().getTime());
                    if (!activity.feedInfoResult.getData().getDetail().isIs_follow()) {
                        activity.followTextView.setVisibility(View.VISIBLE);
                    } else {
                        activity.followTextView.setVisibility(View.GONE);
                    }
                    activity.changeCommentViewData();
                    activity.changePriseViewData();
                    activity.playVideo(activity.feedInfoResult.getData().getDetail().getVideo_url());
                    activity.initHeaderViewData();
                    break;
                case 4://关注/取消关注
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.is_follow = !activity.is_follow;
                        activity.changeFollowViewData();
                    }
                    break;
                case 5://点赞/取消点赞
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.is_prise = !activity.is_prise;
                        activity.changePriseViewData();
                    }
                    break;
                case 6://评论
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.commentCount++;
                        activity.changeCommentViewData();
                        activity.commentList();
                    }
                    break;
                case 7://关联的商品
                    if (null != activity.feedsGoodsList && activity.feedsGoodsList.size() > 0) {
                        activity.goodsCardView.setVisibility(View.VISIBLE);
                        activity.goodsRecyclerAdapter.setList(activity.feedsGoodsList);
                    } else {
                        activity.goodsCardView.setVisibility(View.GONE);
                    }
                    break;
                case 8:
                    if (activity.feedInfoResult.getData().getDetail().isIs_report()) {
                        activity.reportImageView.setVisibility(View.GONE);
                        activity.reportTextView.setText("已投诉");
                    } else {
                        activity.reportImageView.setVisibility(View.VISIBLE);
                        activity.reportTextView.setText("投诉");
                    }
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
        myCommentId = getIntent().getIntExtra("myCommentId", 0);
        fromCircleInfo = getIntent().getIntExtra("fromCircleInfo", 0);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(this);
        dp_200 = (int) getResources().getDimension(R.dimen.dp_200);
        scale = (float) dp_200 / (float) screenHeight;
        contentLayoutParams = new FrameLayout.LayoutParams(screenWidth, Math.round(screenHeight - dp_200));
        contentLayoutParams.gravity = Gravity.BOTTOM;
        initViews();
        initDialogs();
        shareConfig();
        initHideContentLayout();
        videoInfo();
        feedsGoods();
        commentList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlayer.isPlaying()) {
            videoPlayer.pause();
        }
    }

    private void initViews() {
        videoLayout = findViewById(R.id.videoLayout);
        videoLayout.setOnClickListener(this);
        contentLayout = findViewById(R.id.contentLayout);
        contentLayout.setLayoutParams(contentLayoutParams);
        listHeaderView = LayoutInflater.from(this).inflate(R.layout.header_video_information_listview, null);
        initHeaderView();
        listView = findViewById(R.id.listView);
        adapter = new ArticleInfoCommentListParentAdapter(this);
        adapter.setOnReplyParentCommentListener(this);
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
        followTextView.setOnClickListener(this);
        labelHorizontalListView = findViewById(R.id.labelHorizontalListView);
        commentEditText0 = findViewById(R.id.commentEditText0);
        commentEditText0.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(VideoInformationActivity.this);
                    commentContent = commentEditText0.getText().toString().trim();
                    commentEditText0.setText(null);
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
        priseImageView0.setOnClickListener(this);
        priseImageView = findViewById(R.id.priseImageView);
        priseImageView.setOnClickListener(this);
        shareImageView0 = findViewById(R.id.shareImageView0);
        shareImageView0.setOnClickListener(this);
        shareImageView = findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        headerUserLayout = findViewById(R.id.headerUserLayout);
        avatarImageView = findViewById(R.id.avatarImageView);

    }

    private CircleImageView userAvatarImageView, circleAvatarImageView;
    private TextView userNameTextView, userFollowCountTextView;
    private LinearLayout userFollowLayout;
    private ImageView userFollowImageView, reportImageView;
    private TextView userFollowTextView, videoContentTextView,
            reportTextView, viewCountTextView, circleNickNameTextView,
            daysTextView, circleNameTextView, circleDescTextView,
            cityTextView, industryTextView, pnumTextView,
            fnumTextView, headerCommentCountTextView;
    private NiceImageView circleIconImageView;
    private LinearLayout reportLayout;
    private CardView circleCardView, goodsCardView;
    private HorizontalListView tagListView;
    private RecyclerView goodsRecyclerView;
    private FeedsGoodsRecyclerAdapter goodsRecyclerAdapter;

    private void initHeaderView() {
        userAvatarImageView = listHeaderView.findViewById(R.id.userAvatarImageView);
        userNameTextView = listHeaderView.findViewById(R.id.userNameTextView);
        userFollowCountTextView = listHeaderView.findViewById(R.id.userFollowCountTextView);
        userFollowLayout = listHeaderView.findViewById(R.id.userFollowLayout);
        userFollowLayout.setOnClickListener(this);
        userFollowImageView = listHeaderView.findViewById(R.id.userFollowImageView);
        userFollowTextView = listHeaderView.findViewById(R.id.userFollowTextView);
        videoContentTextView = listHeaderView.findViewById(R.id.videoContentTextView);
        tagListView = listHeaderView.findViewById(R.id.tagListView);
        reportLayout = listHeaderView.findViewById(R.id.reportLayout);
        reportLayout.setOnClickListener(this);
        reportImageView = listHeaderView.findViewById(R.id.reportImageView);
        reportTextView = listHeaderView.findViewById(R.id.reportTextView);
        viewCountTextView = listHeaderView.findViewById(R.id.viewCountTextView);
        circleCardView = listHeaderView.findViewById(R.id.circleCardView);
        circleCardView.setOnClickListener(this);
        circleAvatarImageView = listHeaderView.findViewById(R.id.circleAvatarImageView);
        circleNickNameTextView = listHeaderView.findViewById(R.id.circleNickNameTextView);
        daysTextView = listHeaderView.findViewById(R.id.daysTextView);
        circleIconImageView = listHeaderView.findViewById(R.id.circleIconImageView);
        circleNameTextView = listHeaderView.findViewById(R.id.circleNameTextView);
        circleDescTextView = listHeaderView.findViewById(R.id.circleDescTextView);
        cityTextView = listHeaderView.findViewById(R.id.cityTextView);
        industryTextView = listHeaderView.findViewById(R.id.industryTextView);
        pnumTextView = listHeaderView.findViewById(R.id.pnumTextView);
        fnumTextView = listHeaderView.findViewById(R.id.fnumTextView);
        headerCommentCountTextView = listHeaderView.findViewById(R.id.headerCommentCountTextView);
        goodsCardView = listHeaderView.findViewById(R.id.goodsCardView);
        goodsRecyclerView = listHeaderView.findViewById(R.id.goodsRecyclerView);
        goodsRecyclerAdapter = new FeedsGoodsRecyclerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        goodsRecyclerView.setLayoutManager(layoutManager);
        goodsRecyclerView.setAdapter(goodsRecyclerAdapter);
    }

    private void initHeaderViewData() {
        Glide.with(this).load(feedInfoResult.getData().getDetail().getAvatar()).error(R.drawable.image_error).into(userAvatarImageView);
        userNameTextView.setText(feedInfoResult.getData().getDetail().getUsername());
        userFollowCountTextView.setText(feedInfoResult.getData().getDetail().getFollow_num());
        if (is_follow) {
            userFollowLayout.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_gray));
            userFollowImageView.setVisibility(View.GONE);
            userFollowTextView.setText("已关注");
            userFollowTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else {
            userFollowLayout.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
            userFollowImageView.setVisibility(View.VISIBLE);
            userFollowTextView.setText("关注");
            userFollowTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
        }
        videoContentTextView.setText(feedInfoResult.getData().getDetail().getContent());
        if (feedInfoResult.getData().getDetail().isIs_report()) {
            reportImageView.setVisibility(View.GONE);
            reportTextView.setText("已投诉");
        } else {
            reportImageView.setVisibility(View.VISIBLE);
            reportTextView.setText("投诉");
        }
        viewCountTextView.setText(feedInfoResult.getData().getDetail().getFormat_view_num());
        if (null != feedInfoResult.getData().getDetail().getCircle()) {
            circleCardView.setVisibility(View.VISIBLE);
            Glide.with(this).load(feedInfoResult.getData().getDetail().getCircle().getAvatar()).error(R.drawable.image_error).into(circleAvatarImageView);
            circleNickNameTextView.setText(feedInfoResult.getData().getDetail().getCircle().getUsername());
            daysTextView.setText(String.valueOf(feedInfoResult.getData().getDetail().getCircle().getDays()));
            Glide.with(this).load(feedInfoResult.getData().getDetail().getCircle().getThumb()).error(R.drawable.image_error).into(circleIconImageView);
            circleNameTextView.setText(feedInfoResult.getData().getDetail().getCircle().getName());
            circleDescTextView.setText(feedInfoResult.getData().getDetail().getCircle().getDescirption());
            if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getCircle().getCity_name())) {
                cityTextView.setVisibility(View.VISIBLE);
                cityTextView.setText(feedInfoResult.getData().getDetail().getCircle().getCity_name());
            } else {
                cityTextView.setVisibility(View.GONE);
            }
            if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getCircle().getThree_industry())) {
                industryTextView.setVisibility(View.VISIBLE);
                industryTextView.setText(feedInfoResult.getData().getDetail().getCircle().getThree_industry());
            } else {
                industryTextView.setVisibility(View.GONE);
            }
            pnumTextView.setText(String.valueOf(feedInfoResult.getData().getDetail().getCircle().getPnum()));
            fnumTextView.setText(String.valueOf(feedInfoResult.getData().getDetail().getCircle().getFnum()));
        } else {
            circleCardView.setVisibility(View.GONE);
        }
    }

    private void changeFollowViewData() {
        if (is_follow) {
            userFollowLayout.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_gray));
            userFollowImageView.setVisibility(View.GONE);
            userFollowTextView.setText("已关注");
            userFollowTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
            followTextView.setVisibility(View.GONE);
        } else {
            userFollowLayout.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
            userFollowImageView.setVisibility(View.VISIBLE);
            userFollowTextView.setText("关注");
            userFollowTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
            followTextView.setVisibility(View.VISIBLE);
        }
    }

    private void changePriseViewData() {
        int priseCount = feedInfoResult.getData().getDetail().getPrise_num();
        if (is_prise) {
            priseImageView0.setImageDrawable(getResources().getDrawable(R.mipmap.dianzan_yellow));
            priseImageView.setImageDrawable(getResources().getDrawable(R.mipmap.dianzan_yellow));
            priseCount += 1;
        } else {
            priseImageView0.setImageDrawable(getResources().getDrawable(R.mipmap.dianzan_white));
            priseImageView.setImageDrawable(getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        if (priseCount > 0) {
            priseCountTextView.setText(String.valueOf(priseCount));
            priseCountTextView0.setText(String.valueOf(priseCount));
            priseCountTextView.setVisibility(View.VISIBLE);
            priseCountTextView0.setVisibility(View.VISIBLE);
        } else {
            priseCountTextView.setVisibility(View.INVISIBLE);
            priseCountTextView0.setVisibility(View.INVISIBLE);
        }
    }

    private int commentCount;

    private void changeCommentViewData() {
        if (commentCount > 0) {
            commentCountTextView.setText(String.valueOf(commentCount));
            commentCountTextView0.setText(String.valueOf(commentCount));
            headerCommentCountTextView.setText(String.valueOf(commentCount));
            commentCountTextView.setVisibility(View.VISIBLE);
            commentCountTextView0.setVisibility(View.VISIBLE);
        } else {
            commentCountTextView.setVisibility(View.INVISIBLE);
            commentCountTextView0.setVisibility(View.INVISIBLE);
        }

    }

    private void playVideo(String url) {
        videoPlayer.setDataSource(url, null);
        videoPlayer.startPlayVideo();
        Glide.with(this).load(feedInfoResult.getData().getDetail().getVideo_thumb()).into(videoPlayer.getCoverController().getVideoCover());
//        Glide.with(this).load(feedInfoResult.getData().getDetail().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(videoPlayer.getCoverController().getVideoCover());
        videoWidth = feedInfoResult.getData().getDetail().getVideo_width();
        videoHeight = feedInfoResult.getData().getDetail().getVideo_height();
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
                if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getContent())) {
                    shareResult.getData().getShare().setContent(feedInfoResult.getData().getDetail().getContent());
                }
                if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getVideo_thumb())) {
                    shareResult.getData().getShare().setImage_url(feedInfoResult.getData().getDetail().getVideo_thumb());
                }
                shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_VIDEO);
                shareDialog.show();
                shareDialog.setShare(shareResult.getData().getShare(), feeds_id);
                break;
            case R.id.shareImageView0:
                if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getContent())) {
                    shareResult.getData().getShare().setContent(feedInfoResult.getData().getDetail().getContent());
                }
                if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getVideo_thumb())) {
                    shareResult.getData().getShare().setImage_url(feedInfoResult.getData().getDetail().getVideo_thumb());
                }
                shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_VIDEO);
                shareDialog.show();
                shareDialog.setShare(shareResult.getData().getShare(), feeds_id);
                break;
            case R.id.followTextView:
                follow();
                break;
            case R.id.userFollowLayout:
                follow();
                break;
            case R.id.reportLayout:
                if (null != feedInfoResult && null != feedInfoResult.getData() && null != feedInfoResult.getData().getDetail()) {
                    Intent intent = new Intent(this, ComplaintActivity.class);
                    intent.putExtra("report_id", feedInfoResult.getData().getDetail().getId());
                    intent.putExtra("module_type", "feeds");
                    startActivityForResult(intent, 1132);
                }

                break;
            case R.id.priseImageView:
                prise();
                break;
            case R.id.priseImageView0:
                prise();
                break;
            case R.id.circleCardView:
                if (fromCircleInfo == 0)
                    if (null != feedInfoResult && null != feedInfoResult.getData() && null != feedInfoResult.getData().getDetail()) {
                        Intent circleIntent = new Intent(this, CircleInfoActivity.class);
                        circleIntent.putExtra("circle_id", (long) feedInfoResult.getData().getDetail().getCircle().getId());
                        startActivity(circleIntent);
                    }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
        if (resultCode == 1132) {
            if (null != data && data.hasExtra("complaint")) {
                feedInfoResult.getData().getDetail().setIs_report(true);
                Message message = myHandler.obtainMessage(8);
                message.sendToTarget();
            }
        }
    }

    @Override
    public void onReplyParentComment(int cm_id) {
        this.comment_id = cm_id;
//        System.out.println("comment_id: " + comment_id);
        SoftKeyboardUtil.showSoftKeyboard(this, commentEditText);
    }

    private boolean isLarge = true;

    /**
     * 缩小
     */
    private void narrow() {
        isLarge = false;
        headerUserLayout.setVisibility(View.INVISIBLE);
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
        headerUserLayout.setVisibility(View.VISIBLE);
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
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
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
        params.addBodyParameter("comment_id", String.valueOf(myCommentId));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("videoInfo: " + result);
                feedInfoResult = GsonUtils.GsonToBean(result, FeedInfoResult.class);
                if (200 == feedInfoResult.getCode() && null != feedInfoResult.getData() && null != feedInfoResult.getData().getDetail()) {
                    is_follow = feedInfoResult.getData().getDetail().isIs_follow();
                    is_prise = feedInfoResult.getData().getDetail().isIs_prise();
                    commentCount = feedInfoResult.getData().getDetail().getComment_num();
                    user_id = feedInfoResult.getData().getDetail().getUid();
                }
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
                Message message = myHandler.obtainMessage(6);
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

    private void follow() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(user_id));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", is_follow ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
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

    private void prise() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(feeds_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", is_prise ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("点赞: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(5);
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

    private FeedsGoodsResult feedsGoodsResult;
    private List<FeedsGoods> feedsGoodsList;

    private void feedsGoods() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_GOODS);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态关联的商品: " + result);
                feedsGoodsResult = GsonUtils.GsonToBean(result, FeedsGoodsResult.class);
                if (null != feedsGoodsResult && null != feedsGoodsResult.getData() && null != feedsGoodsResult.getData().getList()) {
                    feedsGoodsList = feedsGoodsResult.getData().getList();
                }
                Message message = myHandler.obtainMessage(7);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("动态关联的商品: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private ShareResult shareResult;

    private void shareConfig() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_FEED);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("share_config: " + result);
                shareResult = GsonUtils.GsonToBean(result, ShareResult.class);
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
