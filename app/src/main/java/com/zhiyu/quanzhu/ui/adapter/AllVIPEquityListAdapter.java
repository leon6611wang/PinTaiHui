package com.zhiyu.quanzhu.ui.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.AllVIPEquity;

import java.util.List;

public class AllVIPEquityListAdapter extends BaseAdapter {
    private List<AllVIPEquity> list;

    public void setList(List<AllVIPEquity> allVIPEquityList) {
        this.list = allVIPEquityList;
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
        ImageView equityIconImageView;
        TextView equityTitleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_vip_equity_list, null);
            holder.equityIconImageView = convertView.findViewById(R.id.equityIconImageView);
            holder.equityTitleTextView = convertView.findViewById(R.id.equityTitleTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getIcon()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.equityIconImageView);
        holder.equityTitleTextView.setText(Html.fromHtml(list.get(position).getName()));
        return convertView;
    }
}
