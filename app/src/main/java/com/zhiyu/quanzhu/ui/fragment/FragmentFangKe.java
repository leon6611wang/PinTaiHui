package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Visitor;
import com.zhiyu.quanzhu.model.result.MyVisitorResult;
import com.zhiyu.quanzhu.ui.activity.BuyVIPActivity;
import com.zhiyu.quanzhu.ui.adapter.FangKeRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.ControllScrollLayoutManager;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FragmentFangKe extends Fragment implements View.OnClickListener,BuyVIPActivity.OnBuyVIPSuccessListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private FangKeRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private ImageView visitorBgImageView;
    private ControllScrollLayoutManager layoutManager;
    private LinearLayout isNotVipLayout;
    private TextView buyVipButton;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentFangKe> fragmentWeakReference;

        public MyHandler(FragmentFangKe fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentFangKe fragment = fragmentWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    if (fragment.myVisitorResult.getData().isIs_vip()) {
                        fragment.isNotVipLayout.setVisibility(View.GONE);
                        fragment.layoutManager.setScrollEnabled(true);
                        fragment.ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                        fragment.adapter.setShowInfo(true);
                    } else {
                        fragment.isNotVipLayout.setVisibility(View.VISIBLE);
                        fragment.layoutManager.setScrollEnabled(false);
                        fragment.ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
                        fragment.adapter.setShowInfo(false);
                    }
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_fangke, container, false);
        initPtr();
        initViews();
        BuyVIPActivity.setOnBuyVIPSuccessListener(this);
        return view;
    }

    private void initPtr() {
        ptrFrameLayout = view.findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                myVisitors();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                myVisitors();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);

    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new FangKeRecyclerAdapter(getActivity());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
        layoutManager = new ControllScrollLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);  //垂直
        layoutManager.setScrollEnabled(false);  //禁止滑动
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        visitorBgImageView = view.findViewById(R.id.visitorBgImageView);
        isNotVipLayout = view.findViewById(R.id.isNotVipLayout);
        buyVipButton = view.findViewById(R.id.buyVipButton);
        buyVipButton.setOnClickListener(this);
    }

    @Override
    public void onBuyVIPSuccess() {
        isNotVipLayout.setVisibility(View.GONE);
        layoutManager.setScrollEnabled(true);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        adapter.setShowInfo(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyVipButton:
                Intent intent = new Intent(getContext(), BuyVIPActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private MyVisitorResult myVisitorResult;
    private List<Visitor> list;

    private void myVisitors() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_VISITOR_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("我的访客: " + result);
                myVisitorResult = GsonUtils.GsonToBean(result, MyVisitorResult.class);
                if (isRefresh) {
                    list = myVisitorResult.getData().getList();
                } else {
                    list.addAll(myVisitorResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("我的访客: " + ex.toString());
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
