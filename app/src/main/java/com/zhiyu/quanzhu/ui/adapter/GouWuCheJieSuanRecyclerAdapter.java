package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;

public class GouWuCheJieSuanRecyclerAdapter extends RecyclerView.Adapter<GouWuCheJieSuanRecyclerAdapter.ViewHolder> {
    private Context context;

    public GouWuCheJieSuanRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        OrderConfirmItemRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new OrderConfirmItemRecyclerAdapter(context);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gouwuche_jiesuan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.mRecyclerView.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
