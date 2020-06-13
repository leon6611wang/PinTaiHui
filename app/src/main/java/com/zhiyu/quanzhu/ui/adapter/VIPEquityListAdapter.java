package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.VIPEquity;

import java.util.List;

public class VIPEquityListAdapter extends BaseAdapter {
    private List<VIPEquity> list;

    public void setList(List<VIPEquity> equityList) {
        this.list = equityList;
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
        ImageView vipEquityIconImageView;
        TextView vipEquityNameTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vip_equity, null);
            holder.vipEquityIconImageView = convertView.findViewById(R.id.vipEquityIconImageView);
            holder.vipEquityNameTextView = convertView.findViewById(R.id.vipEquityNameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getIcon()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.vipEquityIconImageView);
        holder.vipEquityNameTextView.setText(list.get(position).getName());
        return convertView;
    }
}
