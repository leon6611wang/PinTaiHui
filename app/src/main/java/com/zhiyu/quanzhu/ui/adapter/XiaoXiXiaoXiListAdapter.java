package com.zhiyu.quanzhu.ui.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.dao.IMUserDao;
import com.leon.chic.model.IMUser;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.MyConversation;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.DateUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
                    int position = (Integer) msg.obj;
                    IMUser user = IMUserDao.getInstance().selectIMUser(adapter.list.get(position).getUserId(), BaseApplication.getInstance());
                    if (null != user && null != adapter.list) {
                        String name = user.getUsername();
                        String avatar = user.getAvatar();
                        adapter.list.get(position).setUserName(name);
                        adapter.list.get(position).setHeaderPic(avatar);
                        adapter.list.get(position).setNeedRequestUserProfile(false);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public void setList(List<MyConversation> mList) {
        this.list = mList;
        notifyDataSetChanged();
    }

    public void setUnReadMsgCount(int position, String content, long time, int count, boolean isRead) {
        if (null != list && list.size() > 0 && list.size() > position) {
            list.get(position).setMessageContent(content);
            list.get(position).setMessageTime(time);
            list.get(position).setUnreadCount(count);
            list.get(position).setRead(isRead);
            notifyDataSetChanged();
        }
    }

    public void setUnReadMsgCountConversation(MyConversation conversation) {
        if (null != list && list.size() > 0) {
            list.add(0, conversation);
            notifyDataSetChanged();
        }
    }

    public void setConversationTop(int position, boolean isTop) {
        if (null != list && list.size() > 0 && list.size() > position) {
            list.get(position).setTop(isTop);
            MyConversation myConversation = list.get(position);
            list.remove(position);
            list.add(0, myConversation);
            notifyDataSetChanged();
        }
    }

    public void deleteConversation(int position) {
        System.out.println("adatper position: "+position);
        if (null != list && list.size() > 0 && list.size() > position) {
            System.out.println("删除对话");
            list.remove(position);
            notifyDataSetChanged();
        }
        System.out.println("list size: "+(null==list?0:list.size()));
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
        TextView userNameTextView, messageTimeTextView, isReadTextView, messageContentTextView, unReadCountTextView;
        CardView conversationCardView;
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
            holder.unReadCountTextView = convertView.findViewById(R.id.unReadCountTextView);
            holder.conversationCardView = convertView.findViewById(R.id.conversationCardView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtils.isNullOrEmpty(list.get(position).getUserName()) ||
                StringUtils.isNullOrEmpty(list.get(position).getHeaderPic())) {
            if (list.get(position).isNeedRequestUserProfile()) {
                getImUserProfile(position);
            }
        }

        if (list.get(position).isTop()) {
            holder.conversationCardView.setCardBackgroundColor(parent.getContext().getResources().getColor(R.color.conversation_top));
        } else {
            holder.conversationCardView.setCardBackgroundColor(parent.getContext().getResources().getColor(R.color.white));
        }

        Glide.with(parent.getContext()).load(list.get(position).getHeaderPic()).into(holder.headerPicImageView);
        holder.userNameTextView.setText(list.get(position).getUserName());
        holder.messageContentTextView.setText(list.get(position).getMessageContent());
        if (list.get(position).getUnreadCount() > 0) {
            holder.unReadCountTextView.setVisibility(View.VISIBLE);
            holder.unReadCountTextView.setText(String.valueOf(list.get(position).getUnreadCount()));
        } else {
            holder.unReadCountTextView.setVisibility(View.INVISIBLE);
        }
        holder.messageTimeTextView.setText(DateUtils.getInstance().parseDate(list.get(position).getMessageTime()));
        holder.isReadTextView.setText(list.get(position).isRead() ? "[已读]" : "[未读]");

        return convertView;
    }

    private void getImUserProfile(final int position) {
        RequestParams params = MyRequestParams.getInstance(BaseApplication.applicationContext).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_INFO_LIST);
        params.addBodyParameter("uids", list.get(position).getUserId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("头像昵称: " + result);
//                System.out.println("getImUserProfile success: " + result);
                IMUserDao.getInstance().saveIMUserProfile(result, BaseApplication.getInstance());
                Message message = myHandler.obtainMessage(1);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("getImUserProfile error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
