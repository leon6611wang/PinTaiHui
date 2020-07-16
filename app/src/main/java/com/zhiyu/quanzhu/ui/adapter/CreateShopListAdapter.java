package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopSearch;

import java.util.List;

public class CreateShopListAdapter extends BaseAdapter {
    private List<ShopSearch> list;

    public void setList(List<ShopSearch> qiChaChaList) {
        this.list = qiChaChaList;
        System.out.println("企查查数据: "+list.size());
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
        TextView nameTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_shop_name, parent,false);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTextView.setText(list.get(position).getName());
        return convertView;
    }
}
