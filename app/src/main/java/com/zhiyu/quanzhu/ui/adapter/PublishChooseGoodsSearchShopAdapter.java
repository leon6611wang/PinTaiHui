package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.LinkShop;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class PublishChooseGoodsSearchShopAdapter extends BaseAdapter {
    private List<LinkShop> list;

    public void setList(List<LinkShop> shopList) {
        this.list = shopList;
        notifyDataSetChanged();
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
        NiceImageView shopIconImageView;
        TextView nameTextView, goodsCountTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_choose_goods_shop, null);
            holder.shopIconImageView = convertView.findViewById(R.id.shopIconImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.goodsCountTextView = convertView.findViewById(R.id.goodsCountTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getIcon()).into(holder.shopIconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.goodsCountTextView.setText(String.valueOf(list.get(position).getCount()));
        return convertView;
    }
}
