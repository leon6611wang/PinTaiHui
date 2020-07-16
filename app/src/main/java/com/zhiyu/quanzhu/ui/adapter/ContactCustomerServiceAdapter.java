package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CommonQuestion;

import java.util.List;

public class ContactCustomerServiceAdapter extends BaseAdapter {
    private List<CommonQuestion> list;

    public void stList(List<CommonQuestion> questionList) {
        this.list = questionList;
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
        TextView titleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_constact_customer_service, null);
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTextView.setText(list.get(position).getTitle());
        return convertView;
    }
}
