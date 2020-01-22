package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Hobby;

import java.util.List;

public class HobbySelectLeftListAdapter extends BaseAdapter {
    private List<Hobby> list;

    public void setList(List<Hobby> hobbyList) {
        this.list = hobbyList;
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

    private int currentItem;

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView contentTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby_select_leftlist, null);
            holder.contentTextView = convertView.findViewById(R.id.contentTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (currentItem == position) {
            //如果被点击，设置当前TextView被选中
            holder.contentTextView.setTextColor(parent.getResources().getColor(R.color.text_color_yellow));
            holder.contentTextView.setBackground(parent.getResources().getDrawable(R.drawable.hobby_selected));
        } else {
            //如果没有被点击，设置当前TextView未被选中
            holder.contentTextView.setTextColor(parent.getResources().getColor(R.color.text_color_black));
            holder.contentTextView.setBackground(parent.getResources().getDrawable(R.drawable.hobby_unselected));
        }
        holder.contentTextView.setText(list.get(position).getName());
        return convertView;
    }
}
