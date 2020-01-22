package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.ui.widget.rongorder.OrderMessage;

import java.util.List;

public class OrderMessageGoodsRecyclerAdapter extends RecyclerView.Adapter<OrderMessageGoodsRecyclerAdapter.ViewHolder> {
    private List<OrderMessage.OrderGoods> list;
    private Context context;

    public OrderMessageGoodsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<OrderMessage.OrderGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView goodsImageImageView;
        TextView goodsNameTextView, goodsCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_message_recycler, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getGoods_image()).into(holder.goodsImageImageView);
        holder.goodsCountTextView.setText("X" + list.get(position).getGoods_count());
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
