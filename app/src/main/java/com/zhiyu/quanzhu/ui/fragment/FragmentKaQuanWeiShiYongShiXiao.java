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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.KaQuanRecyclerAdapter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的卡券-未使用-即将失效
 */
public class FragmentKaQuanWeiShiYongShiXiao extends Fragment {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private RecyclerView mRecyclerView;
    private KaQuanRecyclerAdapter adapter;

    private static class MyHandler extends Handler {
        WeakReference<FragmentKaQuanWeiShiYongShiXiao> fragmentKaQuanWeiShiYongShiXiaoWeakReference;

        public MyHandler(FragmentKaQuanWeiShiYongShiXiao fragmentKaQuanWeiShiYongShiXiao) {
            fragmentKaQuanWeiShiYongShiXiaoWeakReference = new WeakReference<>(fragmentKaQuanWeiShiYongShiXiao);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentKaQuanWeiShiYongShiXiao fragment = fragmentKaQuanWeiShiYongShiXiaoWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kaquan_weishiyong_shixiao, null);
        initViews();
        return view;
    }

    private void initViews() {
        ptrFrameLayout = view.findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new KaQuanRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

}
