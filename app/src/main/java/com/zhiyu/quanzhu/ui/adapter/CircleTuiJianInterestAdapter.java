package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianQuanZi;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class CircleTuiJianInterestAdapter extends RecyclerView.Adapter<CircleTuiJianInterestAdapter.ViewHolder> {
    private Context context;
    private List<QuanZiTuiJianQuanZi> list;

    public void setList(List<QuanZiTuiJianQuanZi> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public CircleTuiJianInterestAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, dayTextView,
                titleTextView, descTextView, cityTextView,
                industryTextView, pTextView, fTextView;
        NiceImageView iconImageView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            industryTextView = itemView.findViewById(R.id.industryTextView);
            pTextView = itemView.findViewById(R.id.pTextView);
            fTextView = itemView.findViewById(R.id.fTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_circle_tuijian_interest, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.avatarImageView);
        Glide.with(context).load(null == list.get(position).getThumb() ? "" : list.get(position).getThumb().getFile()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getName()))
            holder.titleTextView.setText(list.get(position).getName());
        if (!StringUtils.isNullOrEmpty(list.get(position).getUsername()))
            holder.nameTextView.setText(list.get(position).getUsername());
        holder.dayTextView.setText("创建" + list.get(position).getDays() + "天");
        if (!StringUtils.isNullOrEmpty(list.get(position).getDescirption()))
            holder.descTextView.setText(list.get(position).getDescirption());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(list.get(position).getCity_name());
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        } else {
            holder.industryTextView.setVisibility(View.GONE);
        }
        holder.pTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.fTextView.setText(String.valueOf(list.get(position).getFnum()));
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
