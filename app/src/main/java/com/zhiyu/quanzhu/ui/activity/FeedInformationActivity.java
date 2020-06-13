package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CommentParent;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.result.CommentResult;
import com.zhiyu.quanzhu.model.result.FeedCommentResult;
import com.zhiyu.quanzhu.model.result.FeedInfoResult;
import com.zhiyu.quanzhu.model.result.FeedInformationResult;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.ui.adapter.CircleGuanZhuAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleGuanZhuFeedTagListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FeedInfoCommentsParentRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.FeedInformationCommentListParentAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchFeedImagesGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddCommentDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MyLocationRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 动态详情
 */
public class FeedInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, followLayout, complaintLayout;
    private PtrFrameLayout ptrFrameLayout;
    private ListView listView;
    private CircleImageView avatarImageView, circleAvatarImageView;
    private TextView userNameTextView, circleUserNameTextView, circleTimeTextView, circleNameTextView, circleDescTextView, circleCityTextView,
            circleIndustryTextView, chengyuanTextView, dongtaiTextView, commentCountTextView, followTextView, complaintTextView, viewCountTextView;
    private NiceImageView circleImageView;
    private ImageView followImageView, complaintImageView;
    private ImageView collectImageView, priseImageView;
    private TextView collectNumTextView, priseNumTextView;
    private CardView circleCardView;
    private EditText commentEditText;
    private TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView;
    private ExpandableTextView mTextView;
    private HorizontalListView tagListView;
    private CircleGuanZhuFeedTagListAdapter tagListAdapter;
    private MyGridView imageGridView;
    private FullSearchFeedImagesGridAdapter imagesGridAdapter;
    private VideoPlayerTrackView videoPlayer;
    private NiceImageView feedImageView;
    private LinearLayout priseLayout, itemRootLayout;
    private int feed_id, comment_id;
    private int commentCount;
    private boolean isComment;
    private int dp_240;
    private String commentContent = null;
    private FeedInformationCommentListParentAdapter adapter;
    private View headerView;


    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FeedInformationActivity> weakReference;

        public MyHandler(FeedInformationActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FeedInformationActivity activity = weakReference.get();
            switch (msg.what) {
                case 0:
                    Glide.with(activity).load(activity.feedInfoResult.getData().getDetail().getAvatar()).into(activity.avatarImageView);
                    activity.userNameTextView.setText(activity.feedInfoResult.getData().getDetail().getUsername());
                    if (activity.feedInfoResult.getData().getDetail().isIs_follow()) {
                        activity.followLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_solid_bg_ededed_gray));
                        activity.followImageView.setVisibility(View.GONE);
                        activity.followTextView.setText("已关注");
                        activity.followTextView.setTextColor(activity.getResources().getColor(R.color.text_color_gray));
                    } else {
                        activity.followLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                        activity.followImageView.setVisibility(View.VISIBLE);
                        activity.followTextView.setText("关注");
                        activity.followTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }
                    activity.feedDataView();
                    if (activity.feedInfoResult.getData().getDetail().isIs_report()) {
                        activity.complaintImageView.setVisibility(View.GONE);
                        activity.complaintTextView.setText("已投诉");
                    } else {
                        activity.complaintImageView.setVisibility(View.VISIBLE);
                        activity.complaintTextView.setText("投诉");
                    }
                    activity.viewCountTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getView_num()));
                    if (null == activity.feedInfoResult.getData().getDetail().getCircle()) {
                        activity.circleCardView.setVisibility(View.GONE);
                    } else {
                        activity.circleCardView.setVisibility(View.VISIBLE);
                        Glide.with(activity).load(activity.feedInfoResult.getData().getDetail().getCircle().getAvatar())
                                .error(R.drawable.image_error)
                                .into(activity.circleAvatarImageView);
                        activity.circleUserNameTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle().getUsername());
                        activity.circleTimeTextView.setText(String.valueOf("创建" + activity.feedInfoResult.getData().getDetail().getCircle().getDays()) + "天");
                        Glide.with(activity).load(activity.feedInfoResult.getData().getDetail().getCircle().getThumb())
                                .error(R.drawable.image_error)
                                .into(activity.circleImageView);
                        if (!StringUtils.isNullOrEmpty(activity.feedInfoResult.getData().getDetail().getCircle().getName()))
                            activity.circleNameTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle().getName());
                        if (!StringUtils.isNullOrEmpty(activity.feedInfoResult.getData().getDetail().getCircle().getDescirption()))
                            activity.circleDescTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle().getDescirption());
                        if (!StringUtils.isNullOrEmpty(activity.feedInfoResult.getData().getDetail().getCircle().getCity_name())) {
                            activity.circleCityTextView.setVisibility(View.VISIBLE);
                            activity.circleCityTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle().getCity_name());
                        } else {
                            activity.circleCityTextView.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isNullOrEmpty(activity.feedInfoResult.getData().getDetail().getCircle().getThree_industry())) {
                            activity.circleIndustryTextView.setVisibility(View.VISIBLE);
                            activity.circleIndustryTextView.setText(activity.feedInfoResult.getData().getDetail().getCircle().getThree_industry());
                        } else {
                            activity.circleIndustryTextView.setVisibility(View.GONE);
                        }
                        activity.chengyuanTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getCircle().getPnum()));
                        activity.dongtaiTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getCircle().getFnum()));
                    }
                    activity.commentCountTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getComment_num()));
                    if (activity.feedInfoResult.getData().getDetail().getCollect_num() > 0) {
                        activity.collectNumTextView.setVisibility(View.VISIBLE);
                        activity.collectNumTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getCollect_num()));
                    } else {
                        activity.collectNumTextView.setVisibility(View.INVISIBLE);
                    }
                    if (activity.feedInfoResult.getData().getDetail().getPrise_num() > 0) {
                        activity.priseNumTextView.setVisibility(View.VISIBLE);
                        activity.priseNumTextView.setText(String.valueOf(activity.feedInfoResult.getData().getDetail().getPrise_num()));
                    } else {
                        activity.priseNumTextView.setVisibility(View.INVISIBLE);
                    }
                    if (activity.feedInfoResult.getData().getDetail().isIs_collect()) {
                        activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_yellow_big));
                    } else {
                        activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_gray_big));
                    }
                    if (activity.feedInfoResult.getData().getDetail().isIs_prise()) {
                        activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_yellow_big));
                    } else {
                        activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_gray_big));
                    }
                    break;
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    if (activity.isComment) {
                        activity.listView.smoothScrollToPosition(1);
                        activity.isComment = false;
                    }
                    break;
                case 2:
                    if (activity.baseResult.getCode() == 200) {
                        activity.commentCount += 1;
                        activity.commentCountTextView.setText(String.valueOf(activity.commentCount));
                        MessageToast.getInstance(activity).show("评论成功");
                        activity.page = 1;
                        activity.isRefresh = true;
                        activity.commentList();
                    } else {
                        MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    }
                    break;
                case 4:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("操作失败.");
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        dp_240 = (int) getResources().getDimension(R.dimen.dp_240);
        feed_id = getIntent().getIntExtra("feed_id", 0);
        comment_id = getIntent().getIntExtra("comment_id", 0);
        System.out.println("动态详情-feed_id: " + feed_id);
        initPtr();
        initViews();
        feedInformation();
    }

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                commentList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                commentList();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        followLayout = findViewById(R.id.followLayout);
        followLayout.setOnClickListener(this);
        followImageView = findViewById(R.id.followImageView);
        followTextView = findViewById(R.id.followTextView);

        listView = findViewById(R.id.listView);
        adapter = new FeedInformationCommentListParentAdapter(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_feed_information_listview, null);
        circleCardView = headerView.findViewById(R.id.circleCardView);
        circleCardView.setOnClickListener(this);
        circleAvatarImageView = headerView.findViewById(R.id.circleAvatarImageView);
        circleUserNameTextView = headerView.findViewById(R.id.circleUserNameTextView);
        circleTimeTextView = headerView.findViewById(R.id.circleTimeTextView);
        circleImageView = headerView.findViewById(R.id.circleImageView);
        circleNameTextView = headerView.findViewById(R.id.circleNameTextView);
        circleDescTextView = headerView.findViewById(R.id.circleDescTextView);
        circleCityTextView = headerView.findViewById(R.id.circleCityTextView);
        circleIndustryTextView = headerView.findViewById(R.id.circleIndustryTextView);
        chengyuanTextView = headerView.findViewById(R.id.chengyuanTextView);
        dongtaiTextView = headerView.findViewById(R.id.dongtaiTextView);
        commentCountTextView = headerView.findViewById(R.id.commentCountTextView);
        complaintLayout = headerView.findViewById(R.id.complaintLayout);
        complaintLayout.setOnClickListener(this);
        complaintImageView = headerView.findViewById(R.id.complaintImageView);
        complaintTextView = headerView.findViewById(R.id.complaintTextView);
        viewCountTextView = headerView.findViewById(R.id.viewCountTextView);
        timeTextView = headerView.findViewById(R.id.timeTextView);
        mTextView = headerView.findViewById(R.id.mTextView);
        collectImageView = headerView.findViewById(R.id.collectImageView);
        itemRootLayout = headerView.findViewById(R.id.itemRootLayout);
        priseLayout = headerView.findViewById(R.id.priseLayout);
        shareTextView = headerView.findViewById(R.id.shareTextView);
        commentTextView = headerView.findViewById(R.id.commentTextView);
        priseImageView = headerView.findViewById(R.id.priseImageView);
        priseNumTextView = headerView.findViewById(R.id.priseNumTextView);
        timeTextView = headerView.findViewById(R.id.timeTextView);
        tagListAdapter = new CircleGuanZhuFeedTagListAdapter();
        tagListView = headerView.findViewById(R.id.tagListView);
        sourceTextView = headerView.findViewById(R.id.sourceTextView);
        tagListView.setAdapter(tagListAdapter);
        imageGridView = headerView.findViewById(R.id.imageGridView);
        imagesGridAdapter = new FullSearchFeedImagesGridAdapter(this);
        imageGridView.setAdapter(imagesGridAdapter);
        videoPlayer = headerView.findViewById(R.id.videoPlayer);
        feedImageView = headerView.findViewById(R.id.feedImageView);
        listView.setAdapter(adapter);
        listView.addHeaderView(headerView);

        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(FeedInformationActivity.this);
                    commentContent = commentEditText.getText().toString().trim();
                    commentEditText.setText(null);
                    feedComment();
                    return true;
                }
                return false;
            }
        });
        collectImageView = findViewById(R.id.collectImageView);
        collectImageView.setOnClickListener(this);
        collectNumTextView = findViewById(R.id.collectNumTextView);
        priseImageView = findViewById(R.id.priseImageView);
        priseImageView.setOnClickListener(this);
        priseNumTextView = findViewById(R.id.priseNumTextView);


    }

    private void feedDataView() {
        timeTextView.setText(feedInfoResult.getData().getDetail().getTime());
        if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getContent())) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(feedInfoResult.getData().getDetail().getContent());
        } else {
            mTextView.setVisibility(View.GONE);
        }

        if (null == feedInfoResult.getData().getDetail().getFeeds_tags() || feedInfoResult.getData().getDetail().getFeeds_tags().size() == 0) {
            tagListView.setVisibility(View.GONE);
        } else {
            tagListView.setVisibility(View.VISIBLE);
            tagListAdapter.setList(feedInfoResult.getData().getDetail().getFeeds_tags());
        }
        sourceTextView.setText(feedInfoResult.getData().getDetail().getCircle_name());

        if (!StringUtils.isNullOrEmpty(feedInfoResult.getData().getDetail().getVideo_url())) {
            feedImageView.setVisibility(View.GONE);
            imageGridView.setVisibility(View.GONE);
            videoPlayer.setVisibility(View.VISIBLE);
            videoPlayer.setDataSource(feedInfoResult.getData().getDetail().getVideo_url(), "");
            Glide.with(this).load(feedInfoResult.getData().getDetail().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(videoPlayer.getCoverController().getVideoCover());
            videoPlayer.setLayoutParams(feedInfoResult.getData().getDetail().getLayoutParams(dp_240, true));
        } else {
            if (null == feedInfoResult.getData().getDetail().getImgs() || feedInfoResult.getData().getDetail().getImgs().size() == 0) {
                feedImageView.setVisibility(View.GONE);
                imageGridView.setVisibility(View.GONE);
                videoPlayer.setVisibility(View.GONE);
            } else if (null != feedInfoResult.getData().getDetail().getImgs() && feedInfoResult.getData().getDetail().getImgs().size() == 1) {
                feedImageView.setVisibility(View.VISIBLE);
                imageGridView.setVisibility(View.GONE);
                videoPlayer.setVisibility(View.GONE);
                feedImageView.setLayoutParams(feedInfoResult.getData().getDetail().getLayoutParams(dp_240, false));
                Glide.with(this).load(feedInfoResult.getData().getDetail().getImgs().get(0).getFile()).error(R.mipmap.img_error)
                        .into(feedImageView);
//                feedImageView.setOnClickListener(new CircleGuanZhuAdapter.OnLargeImageClick(list.get(position).getContent().getImgs().get(0).getFile()));
            } else {
                feedImageView.setVisibility(View.GONE);
                imageGridView.setVisibility(View.VISIBLE);
                videoPlayer.setVisibility(View.GONE);
                imagesGridAdapter.setList(feedInfoResult.getData().getDetail().getImgs());
            }
        }

        if (isStop && videoPlayer.isPlaying()) {
            videoPlayer.pause();
        }
    }

    private boolean isStop = false;

    @Override
    protected void onResume() {
        super.onResume();
        isStop = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStop = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.followLayout:
                followMember();
                break;
            case R.id.collectImageView:
                collect();
                break;
            case R.id.priseImageView:
                prise();
                break;
            case R.id.circleCardView:
                Intent intent = new Intent(this, CircleInfoActivity.class);
                intent.putExtra("circle_id", (long) feedInfoResult.getData().getDetail().getCircle().getId());
                startActivity(intent);
                break;
        }
    }

    private FeedInformationResult feedInfoResult;

    private void feedInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(feed_id));
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态详情: " + result);
                feedInfoResult = GsonUtils.GsonToBean(result, FeedInformationResult.class);
                if(200==feedInfoResult.getCode()){
                    commentCount=feedInfoResult.getData().getDetail().getComment_num();
                }
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("动态详情: " + ex.toString());
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

    private FeedCommentResult commentResult;
    private List<FeedCommentParent> list;
    private int page = 1;
    private boolean isRefresh = true;

    private void commentList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_COMMENTS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(feed_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("comentList: " + result);
                commentResult = GsonUtils.GsonToBean(result, FeedCommentResult.class);
                System.out.println(result);
                if (isRefresh) {
                    list = commentResult.getData().getList();
                } else {
                    list.addAll(commentResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
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

    private void followMember() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(feedInfoResult.getData().getDetail().getUid()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", feedInfoResult.getData().getDetail().isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    feedInformation();
                }
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

    /**
     * 评论/回复评论
     */
    private void feedComment() {
        isComment = true;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_COMMENT);
        params.addBodyParameter("feeds_id", String.valueOf(feed_id));
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

    private void collect() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(feed_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", feedInfoResult.getData().getDetail().isIs_collect() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    feedInformation();
                }
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
        params.addBodyParameter("prise_id", String.valueOf(feed_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", feedInfoResult.getData().getDetail().isIs_prise() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    feedInformation();
                }
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
}
