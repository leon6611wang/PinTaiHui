package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.LinkShop;

import java.util.List;

public class PublishChooseGoodsSearchMyShopAdapter extends BaseAdapter {
    private List<LinkShop> list;
    public void setList(List<LinkShop> shopList){
        this.list=shopList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return null==list?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView nameTextView,goodsCountTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_choose_goods_my_shop,null);
            holder.nameTextView=convertView.findViewById(R.id.nameTextView);
            holder.goodsCountTextView=convertView.findViewById(R.id.goodsCountTextView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.nameTextView.setText(list.get(position).getName());
        holder.goodsCountTextView.setText(String.valueOf(list.get(position).getCount()));
        return convertView;
    }
}
