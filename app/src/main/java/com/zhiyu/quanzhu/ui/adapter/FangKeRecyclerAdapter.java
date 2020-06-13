package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Visitor;
import com.zhiyu.quanzhu.ui.dialog.FangKeXiangQingDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class FangKeRecyclerAdapter extends RecyclerView.Adapter<FangKeRecyclerAdapter.ViewHolder> {
    private List<Visitor> list;
    private FangKeXiangQingDialog fangKeXiangQingDialog;
    private Activity activity;
    private boolean isShowInfo = false;

    public void setShowInfo(boolean show) {
        this.isShowInfo = show;
        this.isShowInfo = show;
    }

    public FangKeRecyclerAdapter(Activity aty) {
        this.activity = aty;
        fangKeXiangQingDialog = new FangKeXiangQingDialog(activity, R.style.dialog);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView itemCardView;
        CircleImageView avatarImageView;
        TextView nameTextView, countTextView, timeTextView, contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemCardView = itemView.findViewById(R.id.itemCardView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    public void setList(List<Visitor> visitorList) {
        this.list = visitorList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fangke, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(activity).load(list.get(position).getAvatar()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.countTextView.setText(String.valueOf(list.get(position).getCount()));
        holder.timeTextView.setText(list.get(position).getDate());
        holder.contentTextView.setText(Html.fromHtml(list.get(position).getType()));
        if (isShowInfo) {
            holder.itemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fangKeXiangQingDialog.show();
                    fangKeXiangQingDialog.setVisitor(list.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
