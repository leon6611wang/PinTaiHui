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
import com.zhiyu.quanzhu.model.bean.MallAdGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class HomeQuanShangRecyclerAdapter extends BaseRecyclerAdapter<MallAdGoods> {

    private Context context;
    private int dp_5, dp_8, dp_10, dp_15, screenWidth, itemWidth, itemHeight;
    private float ratio;
    private LinearLayout.LayoutParams ll;

    public HomeQuanShangRecyclerAdapter(Context context) {
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

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_quanshang_recyclerview, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, MallAdGoods goods) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            int position = RealPosition + 1;
            if (position % 2 == 0) {
                holder.mingchengTextView.setText("第2列");
                ll.leftMargin = dp_8;
                ll.rightMargin = dp_15;
                ll.topMargin = dp_5;
                ll.bottomMargin = dp_5;
            } else {
                holder.mingchengTextView.setText("第1列");
                ll.leftMargin = dp_15;
                ll.rightMargin = dp_8;
                ll.topMargin = dp_5;
                ll.bottomMargin = dp_5;
            }
            holder.rootLayout.setLayoutParams(ll);
            Glide.with(context).load(goods.getImg().getUrl())
                    .error(R.drawable.image_error)
                    .placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error)
                    .into(holder.goodsImageImageView);
            holder.mingchengTextView.setText(goods.getGoods_name());
            holder.priceZhengShuTextView.setText(PriceParseUtils.getInstance().getZhengShu(goods.getGoods_price()));
            holder.priceXiaoShuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(goods.getGoods_price()));
            holder.xiaoliangTextView.setText(String.valueOf(goods.getSale_num()));
            holder.rootLayout.setOnClickListener(new OnItemClick((int) goods.getId()));
        }
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
        private int goods_id;

        public OnItemClick(int goods_id) {
            this.goods_id = goods_id;
        }

        @Override
        public void onClick(View v) {
            Intent infoIntent = new Intent(context, GoodsInformationActivity.class);
            infoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            infoIntent.putExtra("goods_id", goods_id);
            context.startActivity(infoIntent);
        }
    }
}
