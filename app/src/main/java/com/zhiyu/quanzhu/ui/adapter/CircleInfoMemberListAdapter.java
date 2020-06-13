package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CircleInfoUser;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class CircleInfoMemberListAdapter extends BaseAdapter {
    private List<CircleInfoUser> list;


    public void setList(List<CircleInfoUser> userList) {
        this.list = userList;
        if (null != list && list.size() > 6) {
            CircleInfoUser user = new CircleInfoUser("","");
            list.add(user);
        }
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
        TextView nameTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_info_member, null);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null != list && list.size() > 6) {
            if (position < 5) {
                Glide.with(parent.getContext()).load(list.get(position).getAvatar())
                        .error(R.mipmap.no_avatar)
                        .into(holder.avatarImageView);
                holder.nameTextView.setText(list.get(position).getName());
            } else {
                holder.avatarImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.conversation_group_menu));
                holder.nameTextView.setText("更多");
            }
        } else {
            Glide.with(parent.getContext()).load(list.get(position).getAvatar())
                    .error(R.drawable.image_error)
                    .into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getName());
        }


        return convertView;
    }
}
