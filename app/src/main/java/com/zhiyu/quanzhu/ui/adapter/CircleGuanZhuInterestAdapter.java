package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CircleInfo;
import com.zhiyu.quanzhu.model.bean.GuanZhuCircle;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;

import java.util.List;

public class CircleGuanZhuInterestAdapter extends RecyclerView.Adapter<CircleGuanZhuInterestAdapter.ViewHolder> {
    private Context context;
    private List<GuanZhuCircle> list;

    public void setList(List<GuanZhuCircle> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public CircleGuanZhuInterestAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView, descTextView;
        View rightView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            rightView = itemView.findViewById(R.id.rightView);
            mCardView = itemView.findViewById(R.id.mCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_circle_guanzhu_interest, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load((null == list.get(position).getThumb()) ? "" : list.get(position).getThumb().getFile()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.descTextView.setText(list.get(position).getDescirption());
        if (position < list.size() - 1) {
            holder.rightView.setVisibility(View.VISIBLE);
        } else {
            holder.rightView.setVisibility(View.GONE);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CircleInfoActivity.class);
                intent.putExtra("circle_id", (long) list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
