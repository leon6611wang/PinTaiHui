package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationBActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class GoodsInfoLikeGoodsAdapter extends RecyclerView.Adapter<GoodsInfoLikeGoodsAdapter.ViewHolder> {
    private List<Goods> list;
    private Context context;
    private int dp_5, dp_8, dp_10, dp_15, screenWidth, itemWidth, itemHeight;
    private float ratio;
    private LinearLayout.LayoutParams ll;

    public GoodsInfoLikeGoodsAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        dp_8 = (int) context.getResources().getDimension(R.dimen.dp_8);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        itemWidth = (screenWidth - dp_15 * 3) / 2;
        ratio = 1.454545f;
        itemHeight = (int) (ratio * itemWidth);
        ll = new LinearLayout.LayoutParams(itemWidth, itemHeight);
    }

    public void setList(List<Goods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_quanshang_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods = list.get(position);
        holder.rootLayout.setLayoutParams(ll);
        Glide.with(context).load(goods.getImg().getUrl())
                //异常时候显示的图片
                .error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error)
                .into(holder.goodsImageImageView);
        holder.mingchengTextView.setText(goods.getGoods_name());
        holder.priceZhengShuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.priceXiaoShuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.xiaoliangTextView.setText(String.valueOf(goods.getSale_num()));
        holder.rootLayout.setOnClickListener(new OnItemClick(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout rootLayout;
        TextView xiaoliangTextView, mingchengTextView, priceZhengShuTextView, priceXiaoShuTextView;
        ImageView goodsImageImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            xiaoliangTextView = itemView.findViewById(R.id.xiaoliangTextView);
            mingchengTextView = itemView.findViewById(R.id.mingchengTextView);
            priceZhengShuTextView = itemView.findViewById(R.id.priceZhengShuTextView);
            priceXiaoShuTextView = itemView.findViewById(R.id.priceXiaoShuTextView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
        }
    }

    class OnItemClick implements View.OnClickListener {
        private int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent infoIntent = new Intent(context, GoodsInformationBActivity.class);
            infoIntent.putExtra("goods_id", (int) list.get(position).getId());
            infoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(infoIntent);
        }
    }

}
