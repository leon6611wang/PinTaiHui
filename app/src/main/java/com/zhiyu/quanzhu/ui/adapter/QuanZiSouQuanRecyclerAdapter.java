package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.ArrayList;
import java.util.List;

public class QuanZiSouQuanRecyclerAdapter extends RecyclerView.Adapter<QuanZiSouQuanRecyclerAdapter.ViewHolder> {
    private Context context;

    private List<Circle> list;
    public void setList(List<Circle> circleList){
        this.list=circleList;
        notifyDataSetChanged();
    }
    public QuanZiSouQuanRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        CircleImageView avatarImageView;
        TextView nameTextView,timeTextView,circleNameTextView,descriptionTextView,cityTextView,industryTextView,chengyuanTextView,dongtaiTextView;
        NiceImageView circleImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mCardView=itemView.findViewById(R.id.mCardView);
            avatarImageView=itemView.findViewById(R.id.avatarImageView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
            circleNameTextView=itemView.findViewById(R.id.circleNameTextView);
            descriptionTextView=itemView.findViewById(R.id.descriptionTextView);
            cityTextView=itemView.findViewById(R.id.cityTextView);
            industryTextView=itemView.findViewById(R.id.industryTextView);
            chengyuanTextView=itemView.findViewById(R.id.chengyuanTextView);
            dongtaiTextView=itemView.findViewById(R.id.dongtaiTextView);
            circleImageView=itemView.findViewById(R.id.circleImageView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_souquan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUseravatar()).error(R.mipmap.no_avatar).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.timeTextView.setText("创建"+list.get(position).getDays()+"天");
        Glide.with(context).load(list.get(position).getThumb()).into(holder.circleImageView);
        holder.circleNameTextView.setText(list.get(position).getName());
        holder.descriptionTextView.setText(list.get(position).getDescirption());
        if(!StringUtils.isNullOrEmpty(list.get(position).getCity_name())){
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(list.get(position).getCity_name());
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
        holder.mCardView.setOnClickListener(new OnCircleInfoClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }

    private class OnCircleInfoClickListener implements View.OnClickListener{
        private int position;

        public OnCircleInfoClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent circleInfoIntent=new Intent(context, CircleInfoActivity.class);
            circleInfoIntent.putExtra("circle_id",list.get(position).getId());
            circleInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(circleInfoIntent);
        }
    }
}
