package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.XiTongXiaoXi;
import com.zhiyu.quanzhu.ui.adapter.XiaoXiXiTongRecyclerAdapter;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class FragmentXiaoXiXiTong extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private XiaoXiXiTongRecyclerAdapter adapter;
    private List<XiTongXiaoXi> list = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentXiaoXiXiTong> fragmentXiaoXiXiaoXiWeakReference;

        public MyHandler(FragmentXiaoXiXiTong fragment) {
            fragmentXiaoXiXiaoXiWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            FragmentXiaoXiXiTong fragment = fragmentXiaoXiXiaoXiWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xiaoxi_xitong, container, false);
        initDatas();
        initViews();
        return view;
    }

    private void initDatas() {
        if (null != list && list.size() == 0) {
            XiTongXiaoXi x1 = new XiTongXiaoXi();
            x1.setIcon(R.mipmap.quanzhuxiaomishu);
            x1.setName("圈助小秘书");
            x1.setMsg("圈助v2.0上线啦！");
            x1.setGuanFang(true);
            x1.setTime("5小时前");
            x1.setMsgCount(21);
            list.add(x1);

            XiTongXiaoXi x2 = new XiTongXiaoXi();
            x2.setIcon(R.mipmap.quanyouqingqiu);
            x2.setName("圈友请求");
            x2.setMsg("Vince 请求加您为好友");
            x2.setGuanFang(false);
            x2.setTime("4天前");
            x2.setMsgCount(101);
            list.add(x2);

            XiTongXiaoXi x3 = new XiTongXiaoXi();
            x3.setIcon(R.mipmap.quanzishenhe);
            x3.setName("圈子审核");
            x3.setMsg("Vince 请求加入您的圈子");
            x3.setGuanFang(false);
            x3.setTime("2天前");
            x3.setMsgCount(3);
            list.add(x3);

            XiTongXiaoXi x4 = new XiTongXiaoXi();
            x4.setIcon(R.mipmap.tuikuantongzhi);
            x4.setName("退款通知");
            x4.setMsg("您申请加入XXX商圈，由于超时未审核...");
            x4.setGuanFang(false);
            x4.setTime("2小时前");
            x4.setMsgCount(7);
            list.add(x4);

            XiTongXiaoXi x5 = new XiTongXiaoXi();
            x5.setIcon(R.mipmap.kaquantongzhi);
            x5.setName("卡券通知");
            x5.setMsg("您有一张优惠券待领取，快去领取吧~");
            x5.setGuanFang(false);
            x5.setTime("1天前");
            x5.setMsgCount(3);
            list.add(x5);

            XiTongXiaoXi x6 = new XiTongXiaoXi();
            x6.setIcon(R.mipmap.guanzhudianpu);
            x6.setName("关注店铺");
            x6.setMsg("您有一张优惠券待领取，快去领取吧~");
            x6.setGuanFang(false);
            x6.setTime("1天前");
            x6.setMsgCount(6);
            list.add(x6);
        }
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new XiaoXiXiTongRecyclerAdapter(getContext());
        adapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }


}
