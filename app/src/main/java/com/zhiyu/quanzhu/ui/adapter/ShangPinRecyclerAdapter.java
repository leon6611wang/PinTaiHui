package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FooterPrintHistoryGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class ShangPinRecyclerAdapter extends RecyclerView.Adapter<ShangPinRecyclerAdapter.ViewHolder> {
    private List<FooterPrintHistoryGoods> list;
    private Context context;

    public ShangPinRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<FooterPrintHistoryGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImageImageView;
        TextView nameTextView, priceTextView, price2TextView, saleNumTextView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            cardView = itemView.findViewById(R.id.cardView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            price2TextView = itemView.findViewById(R.id.price2TextView);
            saleNumTextView = itemView.findViewById(R.id.saleNumTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_print_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImg().getUrl()).error(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.nameTextView.setText(list.get(position).getGoods_name());
        holder.priceTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.price2TextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.saleNumTextView.setText(String.valueOf(list.get(position).getSale_num()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goodsInfoIntent = new Intent(context, GoodsInformationActivity.class);
                goodsInfoIntent.putExtra("goods_id", list.get(position).getId());
                goodsInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(goodsInfoIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
