package com.leon.shehuibang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leon.shehuibang.R;
import com.leon.shehuibang.ui.adapter.FragmentCommentsCommentsAdapter;

public class FragmentHomeMine extends Fragment {

    private View view;
    private RecyclerView recycleView;
    private FragmentCommentsCommentsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home_mine,null);
        initViews();
        return view;
    }

    private void initViews() {
        recycleView = view.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new FragmentCommentsCommentsAdapter(getContext());
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(layoutManager);
    }
}
