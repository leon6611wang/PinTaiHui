package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Visitor;
import com.zhiyu.quanzhu.model.result.MyVisitorResult;
import com.zhiyu.quanzhu.ui.adapter.MyVisitorDialogRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FangKeXiangQingDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private CircleImageView avatarImageView;
    private TextView nameTextView, countTextView, closeTextView, contentTextView;
    private Visitor visitor;
    private RecyclerView mRecyclerView;
    private PtrFrameLayout ptrFrameLayout;
    private MyVisitorDialogRecyclerAdapter adapter;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FangKeXiangQingDialog> dialogWeakReference;

        public MyHandler(FangKeXiangQingDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            FangKeXiangQingDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.ptrFrameLayout.refreshComplete();
                    dialog.adapter.setList(dialog.list);
                    break;
            }
        }
    }

    public void setVisitor(Visitor v) {
        this.visitor = v;
        Glide.with(getContext()).load(visitor.getAvatar()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(avatarImageView);
        nameTextView.setText(visitor.getUsername());
        countTextView.setText(String.valueOf(visitor.getCount()));
        contentTextView.setText(visitor.getDate() + " 最后一次" + visitor.getType());
        visitorInfo();
    }

    public FangKeXiangQingDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public FangKeXiangQingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fangke_xiangqing);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initPtr();
        initViews();
    }

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                visitorInfo();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                visitorInfo();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

    }

    private void initViews() {
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        countTextView = findViewById(R.id.countTextView);
        closeTextView = findViewById(R.id.closeTextView);
        closeTextView.setOnClickListener(this);
        contentTextView = findViewById(R.id.contentTextView);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyVisitorDialogRecyclerAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeTextView:
                dismiss();
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private MyVisitorResult visitorResult;
    private List<Visitor> list;

    private void visitorInfo() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.VISITOR_INFO);
        params.addBodyParameter("id", String.valueOf(visitor.getId()));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("visitorInfo: " + result);
                visitorResult = GsonUtils.GsonToBean(result, MyVisitorResult.class);
                if (isRefresh) {
                    list = visitorResult.getData().getList();
                } else {
                    list.addAll(visitorResult.getData().getList());
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
