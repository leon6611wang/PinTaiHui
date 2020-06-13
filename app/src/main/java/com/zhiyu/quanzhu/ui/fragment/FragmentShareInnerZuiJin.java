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
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.result.ZuiJinUserResult;
import com.zhiyu.quanzhu.ui.adapter.InnerShareQuanLiaoAdapter;
import com.zhiyu.quanzhu.ui.adapter.InnerShareZuiJinAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class FragmentShareInnerZuiJin extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private InnerShareZuiJinAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentShareInnerZuiJin> weakReference;

        public MyHandler(FragmentShareInnerZuiJin fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentShareInnerZuiJin fragment = weakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.adapter.setList(fragment.userResult.getData().getUserinfo());
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share_inner_zuijin, null);
        initViews();
        initData();
        return view;
    }

    public List<Integer> getSelectedIdList(){
        return adapter.getSelectedIdList();
    }

    private List<Integer> idsList = new ArrayList<>();

    private void initData() {
        long timeStamp = 0;
        int count = 50;
        RongIMClient.getInstance().getConversationListByPage(new RongIMClient.
                ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (null != conversations && conversations.size() > 0) {
                    for (Conversation c : conversations) {
//                        System.out.println(c.getLatestMessage().getUserInfo().getUserId() + " , " + c.getLatestMessage().getUserInfo().getName());
                        System.out.println(c.getTargetId() + " , " + c.getSenderUserId());
                        if (c.getTargetId().equals(String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)))) {
                            idsList.add(Integer.parseInt(c.getSenderUserId()));
                        } else {
                            idsList.add(Integer.parseInt(c.getTargetId()));
                        }
                    }
                    userInfoList();
                }
            }

            /**
             * 错误回调
             */
            @Override
            public void onError(RongIMClient.ErrorCode ErrorCode) {
                System.out.println("error " + ErrorCode.getMessage());
            }
        }, timeStamp, count, Conversation.ConversationType.PRIVATE);
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new InnerShareZuiJinAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private ZuiJinUserResult userResult;

    private void userInfoList() {
        System.out.println(GsonUtils.GsonString(idsList));
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_INFO_LIST);
        params.addBodyParameter("uids", GsonUtils.GsonString(idsList));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                userResult = GsonUtils.GsonToBean(result, ZuiJinUserResult.class);
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
