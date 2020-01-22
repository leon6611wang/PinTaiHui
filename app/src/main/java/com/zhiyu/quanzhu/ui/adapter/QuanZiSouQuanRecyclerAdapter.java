package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuanZiSouQuanRecyclerAdapter extends RecyclerView.Adapter<QuanZiSouQuanRecyclerAdapter.ViewHolder> {
    private Context context;

    public QuanZiSouQuanRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MyRecyclerView typeRecyclerView;
        TypeRecyclerAdapter adapter;
        private List<String> list=new ArrayList<>();
        public ViewHolder(View itemView) {
            super(itemView);
            typeRecyclerView=itemView.findViewById(R.id.typeRecyclerView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_souquan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.adapter=new TypeRecyclerAdapter(context);
        LinearLayoutManager ms = new LinearLayoutManager(context);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.typeRecyclerView.setLayoutManager(ms);
        holder.typeRecyclerView.setAdapter(holder.adapter);
        holder.list.add("马鞍山");
        holder.list.add("IT圈");
        holder.list.add("生活");
        holder.list.add("美食");
        holder.list.add("游戏");
        holder.list.add("汽车");
        holder.list.add("读书");
        holder.adapter.setData(holder.list);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
