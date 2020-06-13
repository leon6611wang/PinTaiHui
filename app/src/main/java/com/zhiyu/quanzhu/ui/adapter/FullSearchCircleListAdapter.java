package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchCircle;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class FullSearchCircleListAdapter extends BaseAdapter {
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
        CircleImageView avatarImageView;
        TextView nameTextView, dayTextView, circleNameTextView, descTextView, cityTextView, industryTextView, pnumTextView, fnumTextView;
        NiceImageView coverImageView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_cirlcle, null);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.dayTextView = convertView.findViewById(R.id.dayTextView);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.circleNameTextView = convertView.findViewById(R.id.circleNameTextView);
            holder.descTextView = convertView.findViewById(R.id.descTextView);
            holder.cityTextView = convertView.findViewById(R.id.cityTextView);
            holder.industryTextView = convertView.findViewById(R.id.industryTextView);
            holder.pnumTextView = convertView.findViewById(R.id.pnumTextView);
            holder.fnumTextView = convertView.findViewById(R.id.fnumTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getUsername()))
            holder.nameTextView.setText(list.get(position).getUsername());
        holder.dayTextView.setText(String.valueOf(list.get(position).getDays()));
        Glide.with(parent.getContext()).load(list.get(position).getThumb().getFile()).error(R.drawable.image_error).into(holder.coverImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getName()))
            holder.circleNameTextView.setText(list.get(position).getName());
        if (!StringUtils.isNullOrEmpty(list.get(position).getDescirption()))
            holder.descTextView.setText(list.get(position).getDescirption());
        holder.pnumTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.fnumTextView.setText(String.valueOf(list.get(position).getFnum()));
        if (StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.GONE);
        } else {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(list.get(position).getCity_name());
        }
        if (StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.GONE);
        } else {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        }
        return convertView;
    }
}
