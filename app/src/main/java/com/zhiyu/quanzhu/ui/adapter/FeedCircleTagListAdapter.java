package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CircleTag;
import com.zhiyu.quanzhu.model.bean.FeedsTag;

import java.util.List;

/**
 * 首页-圈子-动态-tag列表适配器
 */
public class FeedCircleTagListAdapter extends BaseAdapter {
    private List<String> list;

    public void setList(List<String> tagList) {
        this.list = tagList;
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
        TextView tagTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_circle_tag, null);
            holder.tagTextView = convertView.findViewById(R.id.tagTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tagTextView.setText("#" + list.get(position));
        return convertView;
    }
}
