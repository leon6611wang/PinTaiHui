package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.ShangPinRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

public class FragmentShangPin extends Fragment {

    private View view;
    private MyRecyclerView mRecyclerView;
    private ShangPinRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shangpin, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new ShangPinRecyclerAdapter();
        GridLayoutManager gm = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gm);
        mRecyclerView.setAdapter(adapter);
    }
}
