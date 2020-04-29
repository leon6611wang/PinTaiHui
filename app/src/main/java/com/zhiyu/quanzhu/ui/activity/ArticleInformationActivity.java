package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.ArticleContent;
import com.zhiyu.quanzhu.model.bean.ArticleInformationGoods;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.result.ArticleInformationGoodsResult;
import com.zhiyu.quanzhu.model.result.ArticleInformationResult;
import com.zhiyu.quanzhu.model.result.FeedCommentResult;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoCommentListParentAdapter;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoGoodsGridAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.LeonDrawerMenuLayout;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 文章详情
 */
public class ArticleInformationActivity extends BaseActivity implements View.OnClickListener, LeonDrawerMenuLayout.OnMenuOperationListener, ArticleInfoCommentListParentAdapter.OnReplyParentCommentListener {
    private LeonDrawerMenuLayout mleonDrawerMenuLayout;
    private PtrFrameLayout ptrFrameLayout, goodsListPtr;
    private View informationHeaderLayout;
    private GridView goodsGridView;
    private ArticleInfoGoodsGridAdapter goodsGridAdapter;
    private TextView operationTextView;
    private ImageView operationImageView;
    private ListView listView;
    private View listViewHeaderView;
    private EditText commentEditText;
    private ImageView collectImageView, priseImageView;
    private TextView collectNumTextView, priseNumTextView;
    private ArticleInfoCommentListParentAdapter parentAdapter;
    private LinearLayout backLayout, followLayout, contentLayout, complaintLayout;
    private CircleImageView avatarImageView, circleAvatarImageView;
    private TextView userNameTextView, followTextView, titleTextView, complaintTextView, viewCountTextView,
            circleUserNameTextView, circleTimeTextView, circleNameTextView, circleDescTextView, circleCityTextView,
            circleIndustryTextView, chengyuanTextView, dongtaiTextView, commentCountTextView;
    private ImageView followImageView, shareImageView, complaintImageView;
    private CardView circleCardView;
    private NiceImageView circleImageView;
    private ShareDialog shareDialog;
    private int dp_15, dp_5, screenWidth, screenHeight;
    private String commentContent = null;
    private int article_id,myCommentId;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ArticleInformationActivity> articleInformationActivityWeakReference;

        public MyHandler(ArticleInformationActivity activity) {
            articleInformationActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ArticleInformationActivity activity = articleInformationActivityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.createArticleContent();
                    Glide.with(activity).load(activity.articleInformationResult.getData().getDetail().getAvatar()).into(activity.avatarImageView);
                    activity.userNameTextView.setText(activity.articleInformationResult.getData().getDetail().getUsername());
                    if (activity.articleInformationResult.getData().getDetail().isIs_follow()) {
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
                    activity.titleTextView.setText(activity.articleInformationResult.getData().getDetail().getTitle());
                    if (activity.articleInformationResult.getData().getDetail().isIs_report()) {
                        activity.complaintImageView.setVisibility(View.GONE);
                        activity.complaintTextView.setText("已投诉");
                    } else {
                        activity.complaintImageView.setVisibility(View.VISIBLE);
                        activity.complaintTextView.setText("投诉");
                    }
                    activity.viewCountTextView.setText(activity.articleInformationResult.getData().getDetail().getView_num());
                    if (null == activity.articleInformationResult.getData().getDetail().getCircle()) {
                        activity.circleCardView.setVisibility(View.GONE);
                    } else {
                        activity.circleCardView.setVisibility(View.VISIBLE);
                        Glide.with(activity).load(activity.articleInformationResult.getData().getDetail().getCircle().getAvatar()).into(activity.circleAvatarImageView);
                        activity.circleUserNameTextView.setText(activity.articleInformationResult.getData().getDetail().getCircle().getUsername());
                        activity.circleTimeTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getCircle().getDays()));
                        Glide.with(activity).load(activity.articleInformationResult.getData().getDetail().getCircle().getThumb()).into(activity.circleImageView);
                        activity.circleNameTextView.setText(activity.articleInformationResult.getData().getDetail().getCircle().getName());
                        activity.circleDescTextView.setText(activity.articleInformationResult.getData().getDetail().getCircle().getDescirption());
                        activity.circleCityTextView.setText(activity.articleInformationResult.getData().getDetail().getCircle().getCity_name());
                        activity.circleIndustryTextView.setText(activity.articleInformationResult.getData().getDetail().getCircle().getThree_industry());
                        activity.chengyuanTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getCircle().getPnum()));
                        activity.dongtaiTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getCircle().getFnum()));
                    }
                    activity.commentCountTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getComment_num()));
                    activity.collectNumTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getCollect_num()));
                    activity.priseNumTextView.setText(String.valueOf(activity.articleInformationResult.getData().getDetail().getPrise_num()));
                    break;
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.parentAdapter.setList(activity.list);
                    if (activity.isComment) {
                        activity.listView.smoothScrollToPosition(1);
                        activity.isComment = false;
                    }
                    break;
                case 2:
                    if (activity.baseResult.getCode() == 200) {
                        Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show();
                        activity.page = 1;
                        activity.isRefresh = true;
                        activity.commentList();
                    } else {
                        Toast.makeText(activity, "操作失败，请稍后再试.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    activity.goodsListPtr.refreshComplete();
                    activity.goodsGridAdapter.setList(activity.goodsList);
                    break;
                case 4:
                    if (200 == activity.baseResult.getCode()) {
                        Toast.makeText(activity, activity.articleInformationResult.getData().getDetail().isIs_follow() ?
                                "关注成功" : "取消关注成功", Toast.LENGTH_SHORT).show();
                        if (activity.articleInformationResult.getData().getDetail().isIs_follow()) {
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
                    } else {
                        Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if (activity.baseResult.getCode() == 200) {
                        Toast.makeText(activity, activity.articleInformationResult.getData().getDetail().isIs_collect() ? "收藏成功" : "取消收藏成功", Toast.LENGTH_SHORT).show();
                        activity.operationCollect();
                    } else {
                        Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (activity.baseResult.getCode() == 200) {
                        Toast.makeText(activity, activity.articleInformationResult.getData().getDetail().isIs_prise() ? "点赞成功" : "取消点赞成功", Toast.LENGTH_SHORT).show();
                        activity.operationPrise();
                    } else {
                        Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        article_id = getIntent().getIntExtra("article_id", 0);
        myCommentId = getIntent().getIntExtra("myCommentId", 0);
//        article_id=288;
        dp_5 = (int) getResources().getDimension(R.dimen.dp_5);
        dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(this);
        initDialogs();
        initViews();
        articleInformation();
        articleInformationGoodsList();
        commentList();
    }

    private void initDialogs() {
        shareDialog = new ShareDialog(this, this, R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {

            }
        });
    }

    private void initViews() {
        initPtr();
        initGoodsListPtr();
        informationHeaderLayout = findViewById(R.id.informationHeaderLayout);
        informationHeaderLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        informationHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        informationHeaderLayout.getWidth(); // 获取宽度
                        int height = informationHeaderLayout.getHeight(); // 获取高度
                        mleonDrawerMenuLayout.setHeight(screenHeight - height);
                        return true;
                    }
                });
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        avatarImageView = findViewById(R.id.avatarImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        followLayout = findViewById(R.id.followLayout);
        followLayout.setOnClickListener(this);
        followImageView = findViewById(R.id.followImageView);
        followTextView = findViewById(R.id.followTextView);
        shareImageView = findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(this);
        mleonDrawerMenuLayout = findViewById(R.id.mleonDrawerMenuLayout);
        mleonDrawerMenuLayout.setOnMenuOperationListener(this);
        goodsGridView = findViewById(R.id.goodsGridView);
        goodsGridAdapter = new ArticleInfoGoodsGridAdapter(this);
        goodsGridView.setAdapter(goodsGridAdapter);
        listView = findViewById(R.id.listView);
        listViewHeaderView = LayoutInflater.from(this).inflate(R.layout.header_article_info_listview, null);
        titleTextView = listViewHeaderView.findViewById(R.id.titleTextView);
        contentLayout = listViewHeaderView.findViewById(R.id.contentLayout);
        complaintLayout = listViewHeaderView.findViewById(R.id.complaintLayout);
        complaintLayout.setOnClickListener(this);
        complaintImageView = listViewHeaderView.findViewById(R.id.complaintImageView);
        complaintTextView = listViewHeaderView.findViewById(R.id.complaintTextView);
        viewCountTextView = listViewHeaderView.findViewById(R.id.viewCountTextView);
        circleCardView = listViewHeaderView.findViewById(R.id.circleCardView);
        circleAvatarImageView = listViewHeaderView.findViewById(R.id.circleAvatarImageView);
        circleUserNameTextView = listViewHeaderView.findViewById(R.id.circleUserNameTextView);
        circleTimeTextView = listViewHeaderView.findViewById(R.id.circleTimeTextView);
        circleImageView = listViewHeaderView.findViewById(R.id.circleImageView);
        circleNameTextView = listViewHeaderView.findViewById(R.id.circleNameTextView);
        circleDescTextView = listViewHeaderView.findViewById(R.id.circleDescTextView);
        circleCityTextView = listViewHeaderView.findViewById(R.id.circleCityTextView);
        circleIndustryTextView = listViewHeaderView.findViewById(R.id.circleIndustryTextView);
        chengyuanTextView = listViewHeaderView.findViewById(R.id.chengyuanTextView);
        dongtaiTextView = listViewHeaderView.findViewById(R.id.dongtaiTextView);
        commentCountTextView = listViewHeaderView.findViewById(R.id.commentCountTextView);
        listView.addHeaderView(listViewHeaderView);
        parentAdapter = new ArticleInfoCommentListParentAdapter(this);
        parentAdapter.setOnReplyParentCommentListener(this);
        listView.setAdapter(parentAdapter);

        operationTextView = findViewById(R.id.operationTextView);
        operationImageView = findViewById(R.id.operationImageView);

        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(ArticleInformationActivity.this);
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
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initGoodsListPtr() {
        goodsListPtr = findViewById(R.id.goodsListPtr);
        goodsListPtr.setHeaderView(new MyPtrRefresherHeader(this));
        goodsListPtr.addPtrUIHandler(new MyPtrHandlerHeader(this, goodsListPtr));
        goodsListPtr.setFooterView(new MyPtrRefresherFooter(this));
        goodsListPtr.addPtrUIHandler(new MyPtrHandlerFooter(this, goodsListPtr));
        goodsListPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                goodsListPage++;
                goodsListIsRefresh = false;
                articleInformationGoodsList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                goodsListPage = 1;
                goodsListIsRefresh = true;
                articleInformationGoodsList();
            }
        });
        goodsListPtr.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void createArticleContent() {
        if (null != articleInformationResult.getData().getDetail().getContent() && articleInformationResult.getData().getDetail().getContent().size() > 0) {
            for (ArticleContent content : articleInformationResult.getData().getDetail().getContent()) {
                if (!StringUtils.isNullOrEmpty(content.getContent())) {
                    switch (content.getType()) {
                        case 1://文字
                            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams((screenWidth - dp_15 * 2), LinearLayout.LayoutParams.WRAP_CONTENT);
                            tvParams.leftMargin = dp_15;
                            tvParams.rightMargin = dp_15;
                            tvParams.topMargin = dp_5;
                            tvParams.bottomMargin = dp_5;
                            tvParams.gravity = Gravity.CENTER;
                            TextView textView = new TextView(this);
                            textView.setTextSize(14);
                            textView.setTextColor(getResources().getColor(R.color.text_color_black));
                            textView.setText(content.getContent());
                            textView.setLayoutParams(tvParams);
                            contentLayout.addView(textView);
                            break;
                        case 2://图片
                            int height = Math.round((screenWidth - dp_15 * 2) * ((float) content.getHeight() / (float) content.getWidth()));
                            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams((screenWidth - dp_15 * 2), height);
                            ivParams.leftMargin = dp_15;
                            ivParams.rightMargin = dp_15;
                            ivParams.topMargin = dp_5;
                            ivParams.bottomMargin = dp_5;
                            ivParams.gravity = Gravity.CENTER;
                            ImageView imageView = new ImageView(this);
                            imageView.setLayoutParams(ivParams);
                            imageView.setBackgroundColor(getResources().getColor(R.color.text_color_gray));
                            Glide.with(this).load(content.getContent()).into(imageView);
                            contentLayout.addView(imageView);
                            break;
                    }
                }

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collectImageView:
                collect();
                break;
            case R.id.priseImageView:
                prise();
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.followLayout:
                followMember();
                break;
            case R.id.shareImageView:
                shareDialog.show();
                break;
            case R.id.complaintLayout:
                Intent complaintIntent = new Intent(this, ComplaintActivity.class);
                complaintIntent.putExtra("report_id", article_id);
                complaintIntent.putExtra("module_type", "feeds");
                startActivity(complaintIntent);
                break;

        }
    }

    private int comment_id;

    @Override
    public void onReplyParentComment(int comment_id) {
        this.comment_id = comment_id;
        SoftKeyboardUtil.showSoftKeyboard(this, commentEditText);
    }

    @Override
    public void onMenuOpen(boolean isShow) {
        if (isShow) {
            operationTextView.setText("点击收起");
            operationImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_yellow_up));
        } else {
            operationTextView.setText("查看作者推荐产品");
            operationImageView.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_yellow_down));
        }
    }

    private void operationCollect() {
        if (articleInformationResult.getData().getDetail().isIs_collect()) {
            collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.collect_yellow_big));
            int collectNum = Integer.parseInt(collectNumTextView.getText().toString().trim()) + 1;
            collectNumTextView.setText(String.valueOf(collectNum));
        } else {
            collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.collect_gray_big));
            int collectNum = Integer.parseInt(collectNumTextView.getText().toString().trim()) - 1;
            collectNumTextView.setText(String.valueOf(collectNum));
        }
    }

    private void operationPrise() {
        if (articleInformationResult.getData().getDetail().isIs_prise()) {
            priseImageView.setImageDrawable(getResources().getDrawable(R.mipmap.prise_yellow_big));
            int priseNum = Integer.parseInt(priseNumTextView.getText().toString().trim()) + 1;
            priseNumTextView.setText(String.valueOf(priseNum));
        } else {
            priseImageView.setImageDrawable(getResources().getDrawable(R.mipmap.prise_gray_big));
            int priseNum = Integer.parseInt(priseNumTextView.getText().toString().trim()) - 1;
            priseNumTextView.setText(String.valueOf(priseNum));
        }
    }

    private ArticleInformationResult articleInformationResult;

    private void articleInformation() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_INFO);
        params.addBodyParameter("feeds_id", String.valueOf(article_id));
        params.addBodyParameter("comment_id",String.valueOf(myCommentId));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("articleInformation: " + result);
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

    private FeedCommentResult commentResult;
    private List<FeedCommentParent> list;
    private int page = 1;
    private boolean isRefresh = true;

    private void commentList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_COMMENTS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(article_id));
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
    private boolean isComment;

    /**
     * 评论/回复评论
     */
    private void feedComment() {
        isComment = true;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_COMMENT);
        params.addBodyParameter("feeds_id", String.valueOf(article_id));
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


    private int goodsListPage = 1;
    private boolean goodsListIsRefresh = true;
    private ArticleInformationGoodsResult goodsResult;
    private List<ArticleInformationGoods> goodsList;

    private void articleInformationGoodsList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ARTICLE_INFORMATON_GOODS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(article_id));
        params.addBodyParameter("page", String.valueOf(goodsListPage));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                goodsResult = GsonUtils.GsonToBean(result, ArticleInformationGoodsResult.class);
                if (goodsListIsRefresh) {
                    goodsList = goodsResult.getData().getGoods();
                } else {
                    goodsList.addAll(goodsResult.getData().getGoods());
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


    private void followMember() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(articleInformationResult.getData().getDetail().getUid()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", articleInformationResult.getData().getDetail().isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    articleInformationResult.getData().getDetail().setIs_follow(!articleInformationResult.getData().getDetail().isIs_follow());
                }
                Message message = myHandler.obtainMessage(4);
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

    private void collect() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(article_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", articleInformationResult.getData().getDetail().isIs_collect() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    articleInformationResult.getData().getDetail().setIs_collect(!articleInformationResult.getData().getDetail().isIs_collect());
                }
                Message message = myHandler.obtainMessage(5);
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

    private void prise() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(article_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", articleInformationResult.getData().getDetail().isIs_prise() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    articleInformationResult.getData().getDetail().setIs_prise(!articleInformationResult.getData().getDetail().isIs_prise());
                }
                Message message = myHandler.obtainMessage(6);
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
