package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FeedTag;

import java.util.List;

public class FullSearchFeedTagListAdapter extends BaseAdapter {
    private List<FeedTag> list;

    public void setList(List<FeedTag> tagList){
        this.list=tagList;
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

    class ViewHolder {
        TextView tagTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_feed_tag, null);
            holder.tagTextView = convertView.findViewById(R.id.tagTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tagTextView.setText("#"+list.get(position).getTag_name());
        return convertView;
    }
}
