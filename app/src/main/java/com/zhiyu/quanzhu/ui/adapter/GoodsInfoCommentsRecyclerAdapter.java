package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsComment;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;

import java.util.List;

/**
 * 商品详情-精品评价
 */
public class GoodsInfoCommentsRecyclerAdapter extends RecyclerView.Adapter<GoodsInfoCommentsRecyclerAdapter.ViewHolder> {
    private List<GoodsComment> list;
    private Context context;
    private int dp_10;

    public GoodsInfoCommentsRecyclerAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
    }

    public void setList(List<GoodsComment> commentList) {
        this.list = commentList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headerImageView;
        TextView nickNameTextView, dateTextView, contentTextView;
        RecyclerView commentImagesRecyclerView;
        ItemGoodsInfoCommentsRecyclerAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            headerImageView = itemView.findViewById(R.id.headerImageView);
            nickNameTextView = itemView.findViewById(R.id.nickNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            commentImagesRecyclerView = itemView.findViewById(R.id.commentImagesRecyclerView);
            adapter = new ItemGoodsInfoCommentsRecyclerAdapter(context);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, dp_10, false);
            commentImagesRecyclerView.setAdapter(adapter);
            commentImagesRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            commentImagesRecyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodsinfo_comments_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.headerImageView);
        holder.nickNameTextView.setText(list.get(position).getUsername());
        holder.dateTextView.setText(list.get(position).getDate());
        holder.contentTextView.setText(list.get(position).getContent());
        holder.adapter.setList(list.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
