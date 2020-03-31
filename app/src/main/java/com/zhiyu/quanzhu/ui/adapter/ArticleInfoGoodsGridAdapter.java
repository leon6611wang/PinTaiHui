package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ArticleInformationGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class ArticleInfoGoodsGridAdapter extends BaseAdapter {
    private Context context;
    private int dp_15, dp_1, screenWidth, cardWidth, cardHeight;
    private float ratio = 1.454545f;
    private LinearLayout.LayoutParams params;
    private FrameLayout.LayoutParams params2;
    private List<ArticleInformationGoods> list;

    public void setList(List<ArticleInformationGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    public ArticleInfoGoodsGridAdapter(Context context) {
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

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        CardView goodsLayout;
        LinearLayout goodsLayout2;
        ImageView coverImageView;
        TextView titleTextView, zhengshuTextView, xiaoshuTextView, saleNumTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_info_goods_gridview, null);
            holder.goodsLayout = convertView.findViewById(R.id.goodsLayout);
            holder.goodsLayout2 = convertView.findViewById(R.id.goodsLayout2);
            holder.goodsLayout.setLayoutParams(params);
            holder.goodsLayout2.setLayoutParams(params2);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.zhengshuTextView = convertView.findViewById(R.id.zhengshuTextView);
            holder.xiaoshuTextView = convertView.findViewById(R.id.xiaoshuTextView);
            holder.saleNumTextView = convertView.findViewById(R.id.saleNumTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getImg().getUrl()).into(holder.coverImageView);
        holder.titleTextView.setText(list.get(position).getGoods_name());
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.saleNumTextView.setText(String.valueOf(list.get(position).getSale_num()));
        holder.goodsLayout.setOnClickListener(new OnGoodsInformationClick(position));
        return convertView;
    }

    private class OnGoodsInformationClick implements View.OnClickListener {
        private int position;

        public OnGoodsInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent goodsInforIntent = new Intent(context, GoodsInformationActivity.class);
            goodsInforIntent.putExtra("goods_id", list.get(position).getGoods_id());
            goodsInforIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goodsInforIntent);

        }
    }
}
