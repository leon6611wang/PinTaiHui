package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.JiFenShangPinRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

public class FragmentJiFenDuiHaoLi extends Fragment {
    private View view;
    private JiFenShangPinRecyclerAdapter jiFenShangPinRecyclerAdapter;
    private MyRecyclerView myRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jifenduihaoli, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        myRecyclerView = view.findViewById(R.id.mRecyclerView);
        jiFenShangPinRecyclerAdapter = new JiFenShangPinRecyclerAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        myRecyclerView.setAdapter(jiFenShangPinRecyclerAdapter);
    }
}
