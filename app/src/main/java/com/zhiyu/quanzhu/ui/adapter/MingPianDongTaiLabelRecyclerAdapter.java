package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.ArrayList;
import java.util.List;

public class MingPianDongTaiLabelRecyclerAdapter extends RecyclerView.Adapter<MingPianDongTaiLabelRecyclerAdapter.ViewHolder>{
    private List<String> list;

    public MingPianDongTaiLabelRecyclerAdapter() {
        list=new ArrayList<>();
        list.add("马鞍山");
        list.add("户外生活");
        list.add("钓鱼");
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView labelTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView=itemView.findViewById(R.id.labelTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian_dongtai_label,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.labelTextView.setText("#"+list.get(position));
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
