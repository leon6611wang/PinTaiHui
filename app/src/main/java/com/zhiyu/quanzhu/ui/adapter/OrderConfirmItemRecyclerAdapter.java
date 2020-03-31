package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderConfirmGoods;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class OrderConfirmItemRecyclerAdapter extends RecyclerView.Adapter<OrderConfirmItemRecyclerAdapter.ViewHolder> {
    private List<OrderConfirmGoods> list;
    private Context context;

    public OrderConfirmItemRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<OrderConfirmGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView origPriceTextView, origDecimTextView;
        NiceImageView goodsImageImageView;
        TextView goodsNameTextView, goodsNormsTextView, zhengshuTextView, xiaoshuTextView, goodsCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);

            origPriceTextView = itemView.findViewById(R.id.origPriceTextView);
            origDecimTextView = itemView.findViewById(R.id.origDecimTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_order_cofirm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg()).error(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getNorms_name());
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
        holder.goodsCountTextView.setText("x" + list.get(position).getNum());


        holder.origPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.origDecimTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
