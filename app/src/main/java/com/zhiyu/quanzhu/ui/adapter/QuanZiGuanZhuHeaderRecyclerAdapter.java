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
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiGuanZhuUser;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class QuanZiGuanZhuHeaderRecyclerAdapter extends RecyclerView.Adapter<QuanZiGuanZhuHeaderRecyclerAdapter.Holder> {
    private List<QuanZiGuanZhuUser> list;
    private Context context;

    public QuanZiGuanZhuHeaderRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<QuanZiGuanZhuUser> userList) {
        list = userList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView addImageView;
        CircleImageView avatarImageView;
        TextView nameTextView;
        public Holder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            addImageView = itemView.findViewById(R.id.addImageView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_guanzhu_header, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (position == 0) {
            holder.addImageView.setVisibility(View.VISIBLE);
        } else {
            holder.addImageView.setVisibility(View.GONE);
        }
        Glide.with(context).load(list.get(position).getAvatar()).error(R.mipmap.no_avatar).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
