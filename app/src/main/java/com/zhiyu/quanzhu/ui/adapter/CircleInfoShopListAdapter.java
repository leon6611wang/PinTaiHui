package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CircleInfoShop;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class CircleInfoShopListAdapter extends BaseAdapter {
    private List<CircleInfoShop> list;

    public void setList(List<CircleInfoShop> shopList) {
        this.list = shopList;
        notifyDataSetChanged();
    }

    private Context context;

    public CircleInfoShopListAdapter(Context context) {
        this.context = context;
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
        RoundImageView iconImageView;
        TextView nameTextView, cityTextView, indutryTextView, pingfenTextView, enterShopTextView;
        HorizontalListView goodsListView;
        CircleInfoShopGoodsListAdapter adapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_info_shop, null);
            holder.iconImageView = convertView.findViewById(R.id.iconImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.cityTextView = convertView.findViewById(R.id.cityTextView);
            holder.enterShopTextView = convertView.findViewById(R.id.enterShopTextView);
            holder.indutryTextView = convertView.findViewById(R.id.indutryTextView);
            holder.pingfenTextView = convertView.findViewById(R.id.pingfenTextView);
            holder.goodsListView = convertView.findViewById(R.id.goodsListView);
            holder.adapter = new CircleInfoShopGoodsListAdapter(context);
            holder.goodsListView.setAdapter(holder.adapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getIcon())
                .error(R.mipmap.no_avatar)
                .into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.cityTextView.setText(list.get(position).getCity_name());
        holder.indutryTextView.setText(list.get(position).getShop_type_name());
        holder.pingfenTextView.setText(String.valueOf(list.get(position).getMark()));
        holder.adapter.setList(list.get(position).getGoods_list());
        holder.goodsListView.setVisibility((list.get(position).getGoods_list() == null || list.get(position).getGoods_list().size() == 0) ? View.GONE : View.VISIBLE);
        holder.enterShopTextView.setOnClickListener(new OnEnterShopClick(position));
        return convertView;
    }

    private class OnEnterShopClick implements View.OnClickListener {
        private int position;

        public OnEnterShopClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent shopInfoIntent = new Intent(context, ShopInformationActivity.class);
            shopInfoIntent.putExtra("shop_id", list.get(position).getId());
            shopInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shopInfoIntent);
        }
    }
}
