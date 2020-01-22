package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

public class KaQuanRecyclerAdapter extends RecyclerView.Adapter<KaQuanRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_10;

    public KaQuanRecyclerAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        KaQuanItemItemRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        SpaceItemDecoration spaceItemDecoration;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            spaceItemDecoration = new SpaceItemDecoration(dp_10);
            mRecyclerView.addItemDecoration(spaceItemDecoration);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kaquan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.adapter = new KaQuanItemItemRecyclerAdapter();
        holder.mRecyclerView.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
