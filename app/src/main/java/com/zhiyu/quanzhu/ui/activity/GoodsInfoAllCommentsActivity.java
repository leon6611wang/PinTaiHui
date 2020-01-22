package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.GoodsComment;
import com.zhiyu.quanzhu.model.result.GoodsCommentResult;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoAllCommentsRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商品详情-全部评价
 */
public class GoodsInfoAllCommentsActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private GoodsInfoAllCommentsRecyclerAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);
    private long goods_id;
    private PtrFrameLayout ptrFrameLayout;
    private List<GoodsComment> list;
    private int page;
    private boolean isRefresh = true;
    private TextView allCommentsTextView, hasImageTextView;

    private static class MyHandler extends Handler {
        WeakReference<GoodsInfoAllCommentsActivity> allCommentsActivityWeakReference;

        public MyHandler(GoodsInfoAllCommentsActivity activity) {
            allCommentsActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsInfoAllCommentsActivity activity = allCommentsActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.allCommentsTextView.setText("全部(" + activity.commentResult.getData().getAllnum() + ")");
                    activity.hasImageTextView.setText("有图(" + activity.commentResult.getData().getImgnum() + ")");
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsinfo_all_comments);
        goods_id = getIntent().getLongExtra("goods_id", 0l);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
        goodsInfoAllComments();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new GoodsInfoAllCommentsRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        allCommentsTextView = findViewById(R.id.allCommentsTextView);
        allCommentsTextView.setOnClickListener(this);
        hasImageTextView = findViewById(R.id.hasImageTextView);
        hasImageTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allCommentsTextView:
                barChange(0);
                break;
            case R.id.hasImageTextView:
                barChange(1);
                break;
        }
    }

    private void barChange(int position) {
        allCommentsTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        hasImageTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        allCommentsTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_ededed_gray));
        hasImageTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_ededed_gray));
        switch (position) {
            case 0:
                allCommentsTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                allCommentsTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                break;
            case 1:
                hasImageTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                hasImageTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                break;
        }
        comments_type = position;
        page = 1;
        isRefresh = true;
        if (null != list) {
            list.clear();
        }
        goodsInfoAllComments();
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
                goodsInfoAllComments();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                if (null != list) {
                    list.clear();
                }
                goodsInfoAllComments();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    //类型-0：全部；1：有图
    private int comments_type = 0;
    private GoodsCommentResult commentResult;

    private void goodsInfoAllComments() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_ALL_COMMENTS);
        params.addBodyParameter("type", String.valueOf(comments_type));
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                commentResult = GsonUtils.GsonToBean(result, GoodsCommentResult.class);
                System.out.println("all comments: " + commentResult.getData().getList().size());
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


}
