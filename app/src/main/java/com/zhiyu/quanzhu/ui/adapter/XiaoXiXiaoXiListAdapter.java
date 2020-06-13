package com.zhiyu.quanzhu.ui.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.dao.IMUserDao;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.MyConversation;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.DateUtils;

import java.lang.ref.WeakReference;
import java.util.List;


public class XiaoXiXiaoXiListAdapter extends BaseAdapter {
    private List<MyConversation> list;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<XiaoXiXiaoXiListAdapter> weakReference;

        public MyHandler(XiaoXiXiaoXiListAdapter adapter) {
            weakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            XiaoXiXiaoXiListAdapter adapter = weakReference.get();
            switch (msg.what) {
                case 1:
                    Bundle bundle = (Bundle) msg.obj;
                    int position = bundle.getInt("position");
                    String name = bundle.getString("name");
                    String avatar = bundle.getString("avatar");
                    adapter.list.get(position).setUserName(name);
                    adapter.list.get(position).setHeaderPic(avatar);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

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
        TextView userNameTextView, messageTimeTextView, isReadTextView, messageContentTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xiaoxi_xiaoxi, null);
            holder.headerPicImageView = convertView.findViewById(R.id.headerPicImageView);
            holder.userNameTextView = convertView.findViewById(R.id.userNameTextView);
            holder.messageTimeTextView = convertView.findViewById(R.id.messageTimeTextView);
            holder.isReadTextView = convertView.findViewById(R.id.isReadTextView);
            holder.messageContentTextView = convertView.findViewById(R.id.messageContentTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtils.isNullOrEmpty(list.get(position).getUserName()) ||
                StringUtils.isNullOrEmpty(list.get(position).getHeaderPic())) {
            if (list.get(position).getUserId().equals(String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)))) {
                list.get(position).setHeaderPic(SPUtils.getInstance().getUserAvatar(BaseApplication.applicationContext));
                list.get(position).setUserName(SPUtils.getInstance().getUserName(BaseApplication.applicationContext));
            } else {
                IMUserDao.getInstance().getUserNameAvatar(list.get(position).getUserId(), BaseApplication.getInstance(), new IMUserDao.OnUserNameAvatarListener() {
                    @Override
                    public void onNameAvatar(String name, String avatar) {
                        if(StringUtils.isNullOrEmpty(name)){
                            name="未知用户";
                        }
                        if(StringUtils.isNullOrEmpty(avatar)){
                            avatar="https://c-ssl.duitang.com/uploads/item/202005/28/20200528133442_jubsp.thumb.1000_0.jpeg";
                        }
                        System.out.println("name: " + name + " , avatar: " + avatar);
                        Message message = myHandler.obtainMessage(1);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("avatar", avatar);
                        bundle.putInt("position", position);
                        message.obj = bundle;
                        message.sendToTarget();
                    }
                });
            }
        }


        Glide.with(parent.getContext()).load(list.get(position).getHeaderPic()).into(holder.headerPicImageView);
        holder.userNameTextView.setText(list.get(position).getUserName());
        holder.messageContentTextView.setText(list.get(position).getMessageContent());
        holder.messageTimeTextView.setText(DateUtils.getInstance().parseDate(list.get(position).getMessageTime()));
        holder.isReadTextView.setText(list.get(position).isRead() ? "[已读]" : "[未读]");

        return convertView;
    }
}
