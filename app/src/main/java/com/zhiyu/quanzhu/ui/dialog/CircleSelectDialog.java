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

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.result.MyCircleResult;
import com.zhiyu.quanzhu.ui.adapter.CircleSelectRecyclerAdapter;
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

/**
 * @圈子选择
 */
public class CircleSelectDialog extends Dialog implements View.OnClickListener {
    private PtrFrameLayout ptrFrameLayout;
    private CircleSelectRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<MyCircle> list;
    private TextView cancelTextView, confirmTextView;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleSelectDialog> dialogWeakReference;

        public MyHandler(CircleSelectDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleSelectDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.ptrFrameLayout.refreshComplete();
                    dialog.adapter.setList(dialog.list);
                    break;
            }
        }
    }

    public CircleSelectDialog(@NonNull Context context, int themeResId, OnCircleSeletedListener listener) {
        super(context, themeResId);
        this.onCircleSeletedListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_circle_select);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initViews();
        myCircles();
    }

    private void initViews() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                isRefresh = false;
                page++;
                myCircles();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                myCircles();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CircleSelectRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                MyCircle circle = adapter.getSelectedCircle();
                if (null != onCircleSeletedListener) {
                    onCircleSeletedListener.onCircleSelected(circle);
                }
                dismiss();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private MyCircleResult myCircleResult;

    private void myCircles() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_MY_CIRCLE);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("myCircle " + result);
                myCircleResult = GsonUtils.GsonToBean(result, MyCircleResult.class);
                if (isRefresh) {
                    list = myCircleResult.getData().getCircle_list();
                } else {
                    list.addAll(myCircleResult.getData().getCircle_list());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("myCircle " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private OnCircleSeletedListener onCircleSeletedListener;

    public interface OnCircleSeletedListener {
        void onCircleSelected(MyCircle circle);
    }

}
