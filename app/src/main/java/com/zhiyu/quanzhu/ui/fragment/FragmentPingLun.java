package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.FangKeRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.PingLunRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

public class FragmentPingLun extends Fragment {
    private View view;
    private MyRecyclerView mRecyclerView;
    private PingLunRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pinglun, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new PingLunRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }
}