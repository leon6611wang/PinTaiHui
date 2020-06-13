package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchCircle;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class FullSearchAllCircleListAdapter extends BaseAdapter {

    private List<FullSearchCircle> list;

    public void setList(List<FullSearchCircle> circleList) {
        this.list = circleList;
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
        CircleImageView iconImageView;
        TextView nameTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_all_circle, null);
            holder.iconImageView = convertView.findViewById(R.id.iconImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getThumb().getFile())
                .error(R.drawable.image_error)
                .into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        return convertView;
    }
}
