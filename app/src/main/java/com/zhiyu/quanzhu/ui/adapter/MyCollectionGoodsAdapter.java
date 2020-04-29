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
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.bean.FullSearchGoods;
import com.zhiyu.quanzhu.ui.activity.GoodsInformationActivity;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionGoodsAdapter extends BaseAdapter {
    private Context context;
    private int dp_15, dp_1, screenWidth, cardWidth, cardHeight;
    private float ratio = 1.454545f;
    private FrameLayout.LayoutParams params;
    private FrameLayout.LayoutParams params2;
    private List<FullSearchGoods> list;
    private boolean isSelectModel;

    public void setAllSelect(boolean isAllSelected) {
        for (FullSearchGoods feed : list) {
            feed.setSelected(isAllSelected);
        }
        notifyDataSetChanged();
    }

    public void setSelectModel(boolean isSelected) {
        this.isSelectModel = isSelected;
        notifyDataSetChanged();
    }

    public void setList(List<FullSearchGoods> goodsList) {
        this.list = goodsList;
        notifyDataSetChanged();
    }

    public String getCancelCollectIds() {
        String ids = "";
        for (FullSearchGoods feed : list) {
            if (feed.isSelected()) {
                ids += feed.getId() + ",";
            }
        }
        return ids;
    }

    public void cancelCollectSuccess() {
        List<Integer> positionList = new ArrayList<>();
        for (int i = list.size()-1; i >0; i--) {
            if (list.get(i).isSelected()) {
                positionList.add(i);
            }
        }
        for (Integer position : positionList) {
            list.remove((int) position);
        }
        notifyDataSetChanged();
    }

    public MyCollectionGoodsAdapter(Context context) {
        this.context = context;
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_1 = (int) context.getResources().getDimension(R.dimen.dp_1);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        cardWidth = Math.round((screenWidth - dp_15 * 3 - dp_1 * 4) / 2);
        cardHeight = Math.round(cardWidth * ratio);
        params = new FrameLayout.LayoutParams(cardWidth, cardHeight);
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
        ImageView selectedImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection_goods, null);
            holder.goodsLayout = convertView.findViewById(R.id.goodsLayout);
            holder.goodsLayout2 = convertView.findViewById(R.id.goodsLayout2);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.zhengshuTextView = convertView.findViewById(R.id.zhengshuTextView);
            holder.saleNumTextView = convertView.findViewById(R.id.saleNumTextView);
            holder.xiaoshuTextView = convertView.findViewById(R.id.xiaoshuTextView);
            holder.selectedImageView = convertView.findViewById(R.id.selectedImageView);
            holder.goodsLayout.setLayoutParams(params);
            holder.goodsLayout2.setLayoutParams(params2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position).getImg().getUrl()).error(R.drawable.image_error).into(holder.coverImageView);
        holder.titleTextView.setText(list.get(position).getGoods_name());
        holder.saleNumTextView.setText(String.valueOf(list.get(position).getSale_num()));
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        if (isSelectModel) {
            holder.selectedImageView.setVisibility(View.VISIBLE);
            if (list.get(position).isSelected()) {
                holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.choose_goods_selected));
            } else {
                holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.choose_goods_unselect));
            }
        } else {
            holder.selectedImageView.setVisibility(View.GONE);
        }
        if(isSelectModel){
            holder.goodsLayout.setOnClickListener(new OnSelectGoodsClickListener(position));
        }else{
            holder.goodsLayout.setOnClickListener(new OnGoodsInformationClick(position));
        }

        return convertView;
    }

    private class OnSelectGoodsClickListener implements View.OnClickListener {
        private int position;

        public OnSelectGoodsClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelected(!list.get(position).isSelected());
            notifyDataSetChanged();
        }
    }

    public List<FullSearchGoods> getList() {
        return list;
    }

    private class OnGoodsInformationClick implements View.OnClickListener{
        private int position;

        public OnGoodsInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent goodsInfoIntent=new Intent(context, GoodsInformationActivity.class);
            goodsInfoIntent.putExtra("goods_id",list.get(position).getId());
            goodsInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goodsInfoIntent);
        }
    }

}
