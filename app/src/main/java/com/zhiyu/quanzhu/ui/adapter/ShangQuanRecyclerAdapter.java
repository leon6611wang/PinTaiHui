package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.PraiseCircle;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class ShangQuanRecyclerAdapter extends RecyclerView.Adapter<ShangQuanRecyclerAdapter.ViewHolder> {
    private List<PraiseCircle> list;
    private Context context;

    public ShangQuanRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PraiseCircle> circleList){
        this.list=circleList;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nickNameTextView,dayTextView,circleNameTextView,
                descTextView,cityTextView,industryTextView,chengyuanTextView,
                dongtaiTextView;
        NiceImageView iconImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView=itemView.findViewById(R.id.avatarImageView);
            nickNameTextView=itemView.findViewById(R.id.nickNameTextView);
            dayTextView=itemView.findViewById(R.id.dayTextView);
            iconImageView=itemView.findViewById(R.id.iconImageView);
            circleNameTextView=itemView.findViewById(R.id.circleNameTextView);
            descTextView=itemView.findViewById(R.id.descTextView);
            cityTextView=itemView.findViewById(R.id.cityTextView);
            industryTextView=itemView.findViewById(R.id.industryTextView);
            chengyuanTextView=itemView.findViewById(R.id.chengyuanTextView);
            dongtaiTextView=itemView.findViewById(R.id.dongtaiTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shangquan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        holder.nickNameTextView.setText(list.get(position).getUsername());
        holder.dayTextView.setText(String.valueOf(list.get(position).getAllday()));
        Glide.with(context).load(list.get(position).getCircle_thumb()).error(R.drawable.image_error).into(holder.iconImageView);
        holder.circleNameTextView.setText(list.get(position).getCircle_name());
        holder.descTextView.setText(list.get(position).getCircle_descirption());
        if(!StringUtils.isNullOrEmpty(list.get(position).getCity_name())){
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.circleNameTextView.setText(list.get(position).getCity_name());
        }else{
            holder.cityTextView.setVisibility(View.GONE);
        }
        if(!StringUtils.isNullOrEmpty(list.get(position).getIndustry())){
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        }else{
            holder.industryTextView.setVisibility(View.GONE);
        }
        holder.chengyuanTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.dongtaiTextView.setText(String.valueOf(list.get(position).getFnum()));
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
