package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.model.bean.Feed;

import java.util.List;

public class MultiFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Feed> list;

    public void setList(List<Feed> feedList) {
        this.list = feedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
