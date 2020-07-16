package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.IMCircle;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.model.result.IMCircleResult;
import com.zhiyu.quanzhu.ui.activity.CircleSettingActivity;
import com.zhiyu.quanzhu.ui.adapter.HomeQuanLiaoRecyclerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;

public class FragmentXiaoXiQuanLiao extends Fragment implements CircleSettingActivity.OnRemoveCircleListener {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private HomeQuanLiaoRecyclerAdapter adapter;
    private int dp_15;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentXiaoXiQuanLiao> fragmentXiaoXiQuanLiaoWeakReference;

        public MyHandler(FragmentXiaoXiQuanLiao fragment) {
            fragmentXiaoXiQuanLiaoWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentXiaoXiQuanLiao fragment = fragmentXiaoXiQuanLiaoWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
                case 2:
                    Bundle bundle = (Bundle) msg.obj;
                    int position = bundle.getInt("position");
                    int count = bundle.getInt("count");
                    fragment.adapter.setUnReadCount(position, count);
                    break;
                case 99:
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xiaoxi_quanliao, null);
        CircleSettingActivity.setOnRemoveCircleListener(this);
        dp_15 = (int) getContext().getResources().getDimension(R.dimen.dp_15);
        initPtr();
        initViews();
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.GROUP);
        return view;
    }

    private boolean isRequesting = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting &&
                !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext()))) {
            page = 1;
            isRefresh = true;
            circleList();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isRequesting && (null == list || list.size() == 0) &&
                !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext()))) {
            page = 1;
            isRefresh = true;
            circleList();
        }
    }


    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new HomeQuanLiaoRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_15);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }


    /**
     * 未读消息监听回调
     *
     * @param i
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
//            System.out.println("未读消息监听: " + i);
            if (null != list && list.size() > 0) {
                for (int index = 0; index < list.size(); index++) {
                    final int position = index;
                    RongIMClient.getInstance().getConversation(Conversation.ConversationType.GROUP, String.valueOf(list.get(position).getId()), new RongIMClient.ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            if (null != conversation) {
                                Message message = myHandler.obtainMessage(2);
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                bundle.putInt("count", conversation.getUnreadMessageCount());
                                message.obj = bundle;
                                message.sendToTarget();
//                                System.out.println("圈聊-->  未读数: " + conversation.getUnreadMessageCount());
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
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
                circleList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                circleList();
            }
        });
//        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    @Override
    public void onRemoveCircle() {
        page = 1;
        isRefresh = true;
        circleList();
    }

    private int page = 1;
    private boolean isRefresh = true;
    private IMCircleResult circleResult;
    private List<IMCircle> list;

    private void circleList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_IM_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("circle im list: " + result);
                circleResult = GsonUtils.GsonToBean(result, IMCircleResult.class);
                if (isRefresh) {
                    list = circleResult.getData();
                } else {
                    list.addAll(circleResult.getData());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
//
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
