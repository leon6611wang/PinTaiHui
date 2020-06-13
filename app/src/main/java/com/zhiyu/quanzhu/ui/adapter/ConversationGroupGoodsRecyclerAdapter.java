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
import com.zhiyu.quanzhu.model.bean.ConversationCircleGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

/**
 * 圈聊商品item
 */
public class ConversationGroupGoodsRecyclerAdapter extends RecyclerView.Adapter<ConversationGroupGoodsRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<ConversationCircleGoods> list;

    public void setList(List<ConversationCircleGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    public ConversationGroupGoodsRecyclerAdapter(Context context) {
        this.context = context;

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout rootLayout;
        ImageView goodsImageImageView;
        TextView nameTextView, zhengshuTextView,
                xiaoshuTextView, saleNumTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            saleNumTextView = itemView.findViewById(R.id.saleNumTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_group_goods, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mCardView.setLayoutParams(list.get(position).getLayoutParams(context, position));
        Glide.with(context).load(list.get(position).getImg().getUrl()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.nameTextView.setText(list.get(position).getGoods_name());
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.saleNumTextView.setText(String.valueOf(list.get(position).getSale_num()));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsInformationActivity.class);
                intent.putExtra("goods_id", list.get(position).getGoods_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
