package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FeedsGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class FeedsGoodsRecyclerAdapter extends RecyclerView.Adapter<FeedsGoodsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<FeedsGoods> list;
    private int dp_15, dp_1, screenWidth, cardWidth, cardHeight;
    private float ratio = 1.454545f;
    private LinearLayout.LayoutParams params;
    private FrameLayout.LayoutParams params2;

    public FeedsGoodsRecyclerAdapter(Context context) {
        this.context = context;
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_1 = (int) context.getResources().getDimension(R.dimen.dp_1);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        cardWidth = Math.round((screenWidth - dp_15 * 3 - dp_1 * 4) / 2);
        cardHeight = Math.round(cardWidth * ratio);
        params = new LinearLayout.LayoutParams(cardWidth, cardHeight);
        params2 = new FrameLayout.LayoutParams(cardWidth, cardHeight);
        params.leftMargin = dp_1;
        params.topMargin = 1;
        params.rightMargin = 1;
        params.bottomMargin = 1;
    }

    public void setList(List<FeedsGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView goodsLayout;
        LinearLayout goodsLayout2;
        ImageView goodsImageImageView;
        TextView titleTextView, zhengshuTextView, xiaoshuTextView, saleNumTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsLayout = itemView.findViewById(R.id.goodsLayout);
            goodsLayout2 = itemView.findViewById(R.id.goodsLayout2);
            goodsLayout.setLayoutParams(params);
            goodsLayout2.setLayoutParams(params2);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            saleNumTextView = itemView.findViewById(R.id.saleNumTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_shop_goods, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg().getUrl()).into(holder.goodsImageImageView);
        holder.titleTextView.setText(list.get(position).getGoods_name());
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.saleNumTextView.setText(String.valueOf(list.get(position).getSale_num()));
        holder.goodsLayout.setOnClickListener(new OnGoodsInfoClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnGoodsInfoClick implements View.OnClickListener {
        private int position;

        public OnGoodsInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent goodsInfoIntent = new Intent(context, GoodsInformationActivity.class);
            goodsInfoIntent.putExtra("goods_id", list.get(position).getId());
            goodsInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goodsInfoIntent);
        }
    }
}
