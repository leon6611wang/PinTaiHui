package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShangQuan;

import java.util.List;

public class ShangQuanTuiJianRecyclerViewAdapter extends RecyclerView.Adapter<ShangQuanTuiJianRecyclerViewAdapter.ShangQuanTuiJianHolder>{
    private List<ShangQuan> list;
    private Context context;

    public ShangQuanTuiJianRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ShangQuan> shangQuanList){
        this.list=shangQuanList;
        notifyDataSetChanged();
    }

     class ShangQuanTuiJianHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        public ShangQuanTuiJianHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public ShangQuanTuiJianHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShangQuanTuiJianHolder holder=new ShangQuanTuiJianHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shangquantuijian,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShangQuanTuiJianHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        Glide.with(context)
                .load(list.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
