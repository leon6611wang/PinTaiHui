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
import com.zhiyu.quanzhu.model.bean.MessageGoods;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class OrderMessageGoodsAdapter extends RecyclerView.Adapter<OrderMessageGoodsAdapter.ViewHolder> {
    private Context context;
    private List<MessageGoods> list;

    public void setList(List<MessageGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    public OrderMessageGoodsAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView goodsImageImageView;
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_message_goods, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getGoods_thumb()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsCountTextView.setText("X" + list.get(position).getGoods_num());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
