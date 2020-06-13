package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.QuanZiGuanZhuUser;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.PublishDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class QuanZiGuanZhuHeaderRecyclerAdapter extends RecyclerView.Adapter<QuanZiGuanZhuHeaderRecyclerAdapter.Holder> {
    private List<QuanZiGuanZhuUser> list;
    private Context context;
    private PublishDialog publishDialog;

    public QuanZiGuanZhuHeaderRecyclerAdapter(Context context) {
        this.context = context;
        initDialog();
    }

    public void setList(List<QuanZiGuanZhuUser> userList) {
        list = userList;
        notifyDataSetChanged();
    }

    private void initDialog() {
        publishDialog = new PublishDialog(context, R.style.dialog);
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView addImageView;
        CircleImageView avatarImageView;
        TextView nameTextView;

        public Holder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            addImageView = itemView.findViewById(R.id.addImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_guanzhu_header, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        if (position == 0) {
            holder.addImageView.setVisibility(View.VISIBLE);
        } else {
            holder.addImageView.setVisibility(View.GONE);
        }
        Glide.with(context).load(list.get(position).getAvatar()).error(R.mipmap.no_avatar).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getId() == SPUtils.getInstance().getUserId(BaseApplication.applicationContext)) {
                    publishDialog.show();
                } else {
                    Intent intent = new Intent(context, CardInformationActivity.class);
                    intent.putExtra("card_id", (long) list.get(position).getCard_id());
                    intent.putExtra("uid", (long) list.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
