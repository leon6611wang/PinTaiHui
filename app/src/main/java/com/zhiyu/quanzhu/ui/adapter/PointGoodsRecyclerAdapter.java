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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.PointGoods;
import com.zhiyu.quanzhu.ui.activity.PointGoodsInformationActivity;

import java.util.List;

public class PointGoodsRecyclerAdapter extends RecyclerView.Adapter<PointGoodsRecyclerAdapter.ViewHolder> {
    private List<PointGoods> list;
    private Context context;

    public PointGoodsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PointGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImageImageView;
        LinearLayout soldOutLayout;
        TextView nameTextView, pointTextView, saleNumTextView;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            soldOutLayout = itemView.findViewById(R.id.soldOutLayout);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            pointTextView = itemView.findViewById(R.id.pointTextView);
            saleNumTextView = itemView.findViewById(R.id.saleNumTextView);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getThumb()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.nameTextView.setText(list.get(position).getGoods_name());
        holder.pointTextView.setText(list.get(position).getGoods_credit() + "积分");
        holder.saleNumTextView.setText("剩余" + list.get(position).getGoods_stock() + "件");
        if (list.get(position).getGoods_stock() > 0) {
            holder.soldOutLayout.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.soldOutLayout.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color_dfdfdf));
        }
        holder.cardView.setOnClickListener(new OnPointGoodsClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnPointGoodsClickListener implements View.OnClickListener{
        private int position;

        public OnPointGoodsClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent pointGoodsInfoIntent=new Intent(context, PointGoodsInformationActivity.class);
            pointGoodsInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pointGoodsInfoIntent.putExtra("goods_id",list.get(position).getId());
            context.startActivity(pointGoodsInfoIntent);
        }
    }
}
