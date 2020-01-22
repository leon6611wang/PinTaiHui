package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CommentParent;
import com.zhiyu.quanzhu.model.result.CommentResult;
import com.zhiyu.quanzhu.model.result.DongTaiInformationResult;
import com.zhiyu.quanzhu.ui.adapter.DongTaiInformationCommentsParentRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.AddCommentDialog;
import com.zhiyu.quanzhu.ui.widget.MyLocationRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 动态详情
 */
public class DongTaiInformationActivity extends BaseActivity implements View.OnClickListener
        , DongTaiInformationCommentsParentRecyclerAdapter.OnReplySuccessListener {
    private MyLocationRecyclerView mRecyclerView;
    private DongTaiInformationCommentsParentRecyclerAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);
    private AddCommentDialog addCommentDialog;
    private TextView addCommentTextView;
    private PtrFrameLayout ptrFrameLayout;
    private ArrayList<CommentParent> list;
    private boolean isRefresh = true;
    private boolean isHasHeader = false;
    private boolean isHasList = false;
    private int feed_id;
    private String feed_type;
    private int feed_header_type;//1.image,2.video
    private ImageView collectImageView, priseImageView;
    private TextView collectNumTextView, priseNumTextView;

    private static class MyHandler extends Handler {
        WeakReference<DongTaiInformationActivity> activityWeakReference;

        public MyHandler(DongTaiInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DongTaiInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.isHasList = true;
                    if (activity.isHasHeader) {
                        if (null == activity.list) {
                            activity.list = new ArrayList<>();
                        }
                        if (activity.isRefresh) {
                            CommentParent parent = new CommentParent();
                            parent.setAdapter_type(activity.feed_header_type);
                            parent.setInformation(activity.dongTaiInformationResult.getData().getDetail());
                            activity.list.add(0, parent);
                        }
                        activity.adapter.setList(activity.list, activity.commentResult.getData().getTotal());
                    }
                    break;
                case 2:
                    if (activity.dongTaiInformationResult.getCode() == 200) {
                        if (activity.dongTaiInformationResult.getData().getDetail().isIs_collect()) {
                            activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_yellow_big));
                        } else {
                            activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_gray_big));
                        }
                        if (activity.dongTaiInformationResult.getData().getDetail().isIs_prise()) {
                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_yellow_big));
                        } else {
                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_gray_big));
                        }
                        if (activity.dongTaiInformationResult.getData().getDetail().getCollect_num() > 0) {
                            activity.collectNumTextView.setVisibility(View.VISIBLE);
                            activity.collectNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getCollect_num()));
                        } else {
                            activity.collectNumTextView.setVisibility(View.GONE);
                            activity.collectNumTextView.setText("0");
                        }
                        if (activity.dongTaiInformationResult.getData().getDetail().getPrise_num() > 0) {
                            activity.priseNumTextView.setVisibility(View.VISIBLE);
                            activity.priseNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getPrise_num()));
                        } else {
                            activity.priseNumTextView.setVisibility(View.GONE);
                            activity.priseNumTextView.setText("0");
                        }
                        activity.isHasHeader = true;
                        if (activity.isHasList) {
                            if (null == activity.list) {
                                activity.list = new ArrayList<>();
                            }
                            CommentParent parent = new CommentParent();
                            parent.setAdapter_type(activity.feed_header_type);
                            parent.setInformation(activity.dongTaiInformationResult.getData().getDetail());
                            activity.list.add(0, parent);
                            activity.adapter.setList(activity.list, activity.commentResult.getData().getTotal());
                        }
                    } else {
                        Toast.makeText(activity, activity.dongTaiInformationResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.baseResult.getCode() == 200) {
                        activity.refresh();
                    }
                    break;
                case 4://collect
                    if (activity.baseResult.getCode() == 200) {
                        if (!activity.dongTaiInformationResult.getData().getDetail().isIs_collect()) {
                            activity.dongTaiInformationResult.getData().getDetail().setIs_collect(true);
                            Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
                            activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_yellow_big));
                            activity.collectNumTextView.setVisibility(View.VISIBLE);
                            activity.dongTaiInformationResult.getData().getDetail().setCollect_num(activity.dongTaiInformationResult.getData().getDetail().getCollect_num() + 1);
                            activity.collectNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getCollect_num()));
                        } else {
                            activity.dongTaiInformationResult.getData().getDetail().setIs_collect(false);
                            activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.collect_gray_big));
                            activity.dongTaiInformationResult.getData().getDetail().setCollect_num(activity.dongTaiInformationResult.getData().getDetail().getCollect_num() - 1);
                            activity.collectNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getCollect_num()));
                            if (activity.dongTaiInformationResult.getData().getDetail().getCollect_num() <= 0) {
                                activity.collectNumTextView.setVisibility(View.GONE);
                            } else {
                                activity.collectNumTextView.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(activity, "取消收藏成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "操作失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5://prise
                    if (activity.baseResult.getCode() == 200) {
                        if (!activity.dongTaiInformationResult.getData().getDetail().isIs_prise()) {
                            activity.dongTaiInformationResult.getData().getDetail().setIs_prise(true);
                            Toast.makeText(activity, "点赞成功", Toast.LENGTH_SHORT).show();
                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_yellow_big));
                            activity.priseNumTextView.setVisibility(View.VISIBLE);
                            activity.dongTaiInformationResult.getData().getDetail().setPrise_num(activity.dongTaiInformationResult.getData().getDetail().getPrise_num() + 1);
                            activity.priseNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getPrise_num()));
                        } else {
                            activity.dongTaiInformationResult.getData().getDetail().setIs_prise(false);
                            activity.priseImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.prise_gray_big));
                            activity.dongTaiInformationResult.getData().getDetail().setPrise_num(activity.dongTaiInformationResult.getData().getDetail().getPrise_num() - 1);
                            activity.priseNumTextView.setText(String.valueOf(activity.dongTaiInformationResult.getData().getDetail().getPrise_num()));
                            if (activity.dongTaiInformationResult.getData().getDetail().getPrise_num() <= 0) {
                                activity.priseNumTextView.setVisibility(View.GONE);
                            } else {
                                activity.priseNumTextView.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(activity, "取消点赞成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "操作失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtai_information);
        feed_id = getIntent().getIntExtra("feed_id", 0);
        feed_type = getIntent().getStringExtra("feed_type");
        if (feed_type.equals("image")) {
            feed_header_type = 1;
        } else if (feed_type.equals("video")) {
            feed_header_type = 2;
        }
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
        initDialog();
        feedInformation();
        commentsList();
    }

    private int page = 1;

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                loadmore();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void refresh() {
        page = 1;
        if (null != list) {
            list.clear();
        }
        isRefresh = true;
        commentsList();
    }

    private void loadmore() {
        page++;
        isRefresh = false;
        commentsList();
    }

    @Override
    public void onReplySuccess() {
        System.out.println("回复成功，刷新列表");
        refresh();
    }


    private void initDialog() {
        addCommentDialog = new AddCommentDialog(this, R.style.inputDialog, new AddCommentDialog.OnAddCommentListener() {
            @Override
            public void onAddComment(String comment, int commentId) {
                dongtai_comment(comment);
            }
        });
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
//        imagesView = LayoutInflater.from(this).inflate(R.layout.item_dongtai_information_header_text_images, mRecyclerView, false);
//        videoView = LayoutInflater.from(this).inflate(R.layout.item_dongtai_information_header_text_video, null);
        adapter = new DongTaiInformationCommentsParentRecyclerAdapter(this, mRecyclerView);
        adapter.setOnReplySuccessListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        addCommentTextView = findViewById(R.id.addCommentTextView);
        addCommentTextView.setOnClickListener(this);

        collectImageView = findViewById(R.id.collectImageView);
        collectImageView.setOnClickListener(this);
        collectNumTextView = findViewById(R.id.collectNumTextView);
        priseImageView = findViewById(R.id.priseImageView);
        priseImageView.setOnClickListener(this);
        priseNumTextView = findViewById(R.id.priseNumTextView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCommentTextView:
                addCommentDialog.show();
                break;
            case R.id.priseImageView:
                priseFeed();
                break;
            case R.id.collectImageView:
                collectFeed();
                break;
        }
    }

    private CommentResult commentResult;

    private void commentsList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DONGTAI_COMMENTS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(feed_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("评论列表" + result);
                commentResult = GsonUtils.GsonToBean(result, CommentResult.class);
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

    private DongTaiInformationResult dongTaiInformationResult;

    private void feedInformation() {
        System.out.println("feedInformation feed_id: " + feed_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DONGTAI_INFORMATION);
        params.addBodyParameter("feeds_id", String.valueOf(feed_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态详情" + result);
                dongTaiInformationResult = GsonUtils.GsonToBean(result, DongTaiInformationResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("feedInformation error " + ex.toString());
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

    private void dongtai_comment(String content) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DONGTAI_COMMENT);
        params.addBodyParameter("feeds_id", "25");
        params.addBodyParameter("content", content);
        params.addBodyParameter("comment_id", "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
//                System.out.println("评论动态 " + result);
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

    private void priseFeed() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(feed_id));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", (dongTaiInformationResult.getData().getDetail().isIs_prise() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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

    private void collectFeed(){
        RequestParams params=MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id",String.valueOf(feed_id));
        params.addBodyParameter("module_type","feeds");
        params.addBodyParameter("type", (dongTaiInformationResult.getData().getDetail().isIs_collect() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult=GsonUtils.GsonToBean(result,BaseResult.class);
                Message message=myHandler.obtainMessage(4);
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
