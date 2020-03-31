package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FollowMember;
import com.zhiyu.quanzhu.model.result.FollowMemberResult;
import com.zhiyu.quanzhu.ui.adapter.MyFollowMemberAdapter;
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

/**
 * 我的关注-人员
 */
public class FragmentMyFollowMember extends Fragment {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    private MyFollowMemberAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentMyFollowMember> fragmentMyFollowMemberWeakReference;

        public MyHandler(FragmentMyFollowMember fragment) {
            fragmentMyFollowMemberWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentMyFollowMember fragment = fragmentMyFollowMemberWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_follow_member, null);
        initViews();
        return view;
    }

    private void initViews() {
        initPtr();
        mListView = view.findViewById(R.id.mListView);
        adapter = new MyFollowMemberAdapter(getContext());
        mListView.setAdapter(adapter);
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
                myfollow();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                myfollow();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private int page = 1;
    private boolean isRefresh = true;
    private FollowMemberResult memberResult;
    private List<FollowMember> list;

    private void myfollow() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_FOLLOW);
        params.addBodyParameter("type", "user");
        params.addBodyParameter("from", "user");
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                memberResult = GsonUtils.GsonToBean(result, FollowMemberResult.class);
                if (isRefresh) {
                    list = memberResult.getData().getList();
                } else {
                    list.addAll(memberResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("myfollow member: " + list.size());
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
