package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyConversation;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.DateUtils;

import java.util.List;


public class XiaoXiXiaoXiListAdapter extends BaseAdapter {
    private List<MyConversation> list;

    public void setList(List<MyConversation> mList) {
        this.list = mList;
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

    private class ViewHolder {
        CircleImageView headerPicImageView;
        TextView userNameTextView,messageTimeTextView,isReadTextView,messageContentTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xiaoxi_xiaoxi, null);
            holder.headerPicImageView=convertView.findViewById(R.id.headerPicImageView);
            holder.userNameTextView=convertView.findViewById(R.id.userNameTextView);
            holder.messageTimeTextView=convertView.findViewById(R.id.messageTimeTextView);
            holder.isReadTextView=convertView.findViewById(R.id.isReadTextView);
            holder.messageContentTextView=convertView.findViewById(R.id.messageContentTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(parent.getContext()).load(list.get(position).getHeaderPic()).into(holder.headerPicImageView);
        holder.messageContentTextView.setText(list.get(position).getMessageContent());
        holder.messageTimeTextView.setText(DateUtils.getInstance().parseDate(list.get(position).getMessageTime()));
        holder.isReadTextView.setText(list.get(position).isRead()?"[已读]":"[未读]");
        holder.userNameTextView.setText(list.get(position).getUserName());
        return convertView;
    }
}
