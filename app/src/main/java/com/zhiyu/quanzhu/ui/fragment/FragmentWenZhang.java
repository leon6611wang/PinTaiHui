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
import com.zhiyu.quanzhu.ui.adapter.ShangQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.WenZhangRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

public class FragmentWenZhang extends Fragment {

    private View view;
    private MyRecyclerView mRecyclerView;
    private WenZhangRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_wenzhang,container,false);
        initViews();
        return view;
    }
    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new WenZhangRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }
}
