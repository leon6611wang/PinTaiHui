package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedCommentChild;
import com.zhiyu.quanzhu.model.result.FeedChildCommentResult;
import com.zhiyu.quanzhu.model.result.FeedCommentParentResult;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoCommentListChildAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyListView;
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
 * 评论详情
 */
public class CommentInformationActivity extends BaseActivity implements View.OnClickListener, ArticleInfoCommentListChildAdapter.OnReplyChildCommentListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private PtrFrameLayout ptrFrameLayout;
    private ListView listView;
    private CircleImageView avatarImageView;
    private TextView nameTextView, contentTextView, replyTextView, priseNumTextView, timeTextView;
    private ImageView priseImageView;
    private ArticleInfoCommentListChildAdapter adapter;
    private LinearLayout priseLayout;
    private EditText commentEditText;
    private String commentContent;
    private int comment_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CommentInformationActivity> activityWeakReference;

        public MyHandler(CommentInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    Glide.with(activity).load(activity.feedCommentParentResult.getData().getAvatar()).into(activity.avatarImageView);
                    activity.nameTextView.setText(activity.feedCommentParentResult.getData().getUsername());
                    if (activity.feedCommentParentResult.getData().isIs_prise()) {
                        activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.dianzan_yellow));
                    } else {
                        activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.dianzan_gray));
                    }
                    activity.priseNumTextView.setText(String.valueOf(activity.feedCommentParentResult.getData().getPnum()));
                    activity.timeTextView.setText(activity.feedCommentParentResult.getData().getDateline());
                    activity.contentTextView.setText(activity.feedCommentParentResult.getData().getContent());
                    break;
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
//                    if (activity.baseResult.getCode() == 200) {
//                        activity.feedCommentParentResult.getData().setIs_prise(!activity.feedCommentParentResult.getData().isIs_prise());
//                        if (activity.feedCommentParentResult.getData().isIs_prise()) {
//                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.dianzan_yellow));
//                            activity.feedCommentParentResult.getData().setPnum(activity.feedCommentParentResult.getData().getPnum() + 1);
//                            activity.priseNumTextView.setText(String.valueOf(activity.feedCommentParentResult.getData().getPnum()));
//                        } else {
//                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.dianzan_gray));
//                            activity.feedCommentParentResult.getData().setPnum(activity.feedCommentParentResult.getData().getPnum() - 1);
//                            activity.priseNumTextView.setText(String.valueOf(activity.feedCommentParentResult.getData().getPnum()));
//                        }
//                    }
                    break;
                case 3:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器错误，稍后再试.");
                    activity.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_comment_information);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        comment_id = getIntent().getIntExtra("comment_id", 0);
        reply_comment_id = comment_id;
        initViews();
        commentDetail();
        replyCommentList();
    }

    private void initViews() {
        initPtr();
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("评论详情");
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        priseImageView = findViewById(R.id.priseImageView);
        priseNumTextView = findViewById(R.id.priseNumTextView);
        timeTextView = findViewById(R.id.timeTextView);
        contentTextView = findViewById(R.id.contentTextView);
        replyTextView = findViewById(R.id.replyTextView);
        replyTextView.setOnClickListener(this);
        priseLayout = findViewById(R.id.priseLayout);
        priseLayout.setOnClickListener(this);
        adapter = new ArticleInfoCommentListChildAdapter(this);
        adapter.setOnReplyChildCommentListener(this);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SoftKeyboardUtil.hideSoftKeyboard(CommentInformationActivity.this);
                    commentContent = commentEditText.getText().toString().trim();
                    commentEditText.setText(null);
                    feedComment();
                    return true;
                }
                return false;
            }
        });
    }

    private int reply_comment_id;

    @Override
    public void onReplyChildComment(int comment_id) {
        System.out.println("comment_id: " + comment_id);
//        SoftKeyboardUtil.showSoftKeyBoard(getWindow());
        SoftKeyboardUtil.showSoftKeyboard(this, commentEditText);
        reply_comment_id = comment_id;
        System.out.println("reply_comment_id: " + reply_comment_id);
        commentEditText.setFocusable(true);

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
                replyCommentList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                replyCommentList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.priseLayout:
                priseFeed();
                break;
            case R.id.replyTextView:
                SoftKeyboardUtil.showSoftKeyboard(this, commentEditText);
                reply_comment_id = comment_id;
                commentEditText.setFocusable(true);
                System.out.println("reply_comment_id: " + reply_comment_id + " , comment_id: " + comment_id);
                break;
        }
    }

    private FeedCommentParentResult feedCommentParentResult;

    private void commentDetail() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COMMENT_DETAIL);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                feedCommentParentResult = GsonUtils.GsonToBean(result, FeedCommentParentResult.class);
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("commentDetail: "+ex.toString());
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

    private FeedChildCommentResult childCommentResult;
    private int page = 1;
    private boolean isRefresh = true;
    private List<FeedCommentChild> list;

    private void replyCommentList() {
        System.out.println("comment_id: " + comment_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.REPLY_COMMENT);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("评论-回复列表: " + result);
                childCommentResult = GsonUtils.GsonToBean(result, FeedChildCommentResult.class);
                if (isRefresh) {
                    list = childCommentResult.getData().getList();
                } else {
                    list.addAll(childCommentResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("reply commment list: " + ex.toString());

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

    private void priseFeed() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(comment_id));
        params.addBodyParameter("module_type", "feedscomment");
        params.addBodyParameter("type", (feedCommentParentResult.getData().isIs_prise() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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

    /**
     * 评论/回复评论
     */
    private void feedComment() {
        System.out.println("评论/回复评论 -- feeds_id: " + feedCommentParentResult.getData().getFeeds_id() + " , comment_id: " + comment_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_COMMENT);
        params.addBodyParameter("feeds_id", String.valueOf(feedCommentParentResult.getData().getFeeds_id()));
        params.addBodyParameter("comment_id", String.valueOf(reply_comment_id));
        params.addBodyParameter("content", commentContent);
        commentContent = null;
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                if (baseResult.getCode() == 200) {
                    page = 1;
                    isRefresh = true;
                    replyCommentList();
                }
                System.out.println("feedComment: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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
