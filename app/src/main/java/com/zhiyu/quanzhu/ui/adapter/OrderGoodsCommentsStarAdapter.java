package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CommentStar;

import java.util.List;

public class OrderGoodsCommentsStarAdapter extends RecyclerView.Adapter<OrderGoodsCommentsStarAdapter.ViewHolder> {
    private Context context;
    private List<CommentStar> list;

    public void setList(List<CommentStar> starList) {
        this.list = starList;
        notifyDataSetChanged();
    }

    public OrderGoodsCommentsStarAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView starImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            starImageView = itemView.findViewById(R.id.starImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_goods_comments_star, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            holder.starImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.comment_star_yellow));
        } else {
            holder.starImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.comment_star_gray));
        }
        holder.starImageView.setOnClickListener(new OnStarClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnStarClick implements View.OnClickListener {
        private int position;

        public OnStarClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelected(!list.get(position).isSelected());
            for (int i = 0; i < position; i++) {
                list.get(i).setSelected(true);
            }
            for (int i = position + 1; i < list.size(); i++) {
                list.get(i).setSelected(false);
            }
            int sum = 0;
            for (CommentStar star : list) {
                if (star.isSelected()) {
                    sum++;
                }
            }
            if (null != onCommentStarListener) {
                onCommentStarListener.onStar(sum);
            }
            notifyDataSetChanged();
        }
    }

    private OnCommentStarListener onCommentStarListener;

    public void setOnCommentStarListener(OnCommentStarListener listener) {
        this.onCommentStarListener = listener;
    }

    public interface OnCommentStarListener {
        void onStar(int sum);
    }
}
