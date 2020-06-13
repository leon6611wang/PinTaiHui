package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsTag;

import java.util.List;

public class GoodsInfoTagAdapter extends RecyclerView.Adapter<GoodsInfoTagAdapter.ViewHolder>{
    private List<GoodsTag> list;

    public void setList(List<GoodsTag> tagList){
        this.list=tagList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tagTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            tagTextView=itemView.findViewById(R.id.tagTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_info_tag,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tagTextView.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
