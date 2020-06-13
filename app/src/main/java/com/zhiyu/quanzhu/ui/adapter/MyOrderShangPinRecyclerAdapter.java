package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderGoods;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class MyOrderShangPinRecyclerAdapter extends RecyclerView.Adapter<MyOrderShangPinRecyclerAdapter.ViewHolder> {
    private Context context;

    public MyOrderShangPinRecyclerAdapter(Context context) {
        this.context = context;
    }

    private List<OrderGoods> list;

    public void setList(List<OrderGoods> orderGoodsList) {
        this.list = orderGoodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView goodsImageImageView;
        TextView goodsNameTextView, goodsNormsTextView, goodsPriceTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            goodsPriceTextView = itemView.findViewById(R.id.goodsPriceTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myorder_shangpin, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getGoods_img()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getGoods_normas_name());
        holder.goodsPriceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getGoods_price()) + "×" + list.get(position).getGoods_num());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
