package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.CircleInfoUser;
import com.zhiyu.quanzhu.ui.popupwindow.CircleMemberManageBottomMenuWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleMemberManageTopMenuWindow;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class CircleMemeberManageListAdapter extends BaseAdapter {
    private List<CircleInfoUser> list;
    private ListView listView;
    private Context context;
    private int topLayoutHeight;
    private int screenHeight;
    private int own;
    private int my_user_id;
    private int circle_id;

    public void setList(List<CircleInfoUser> userList) {
        this.list = userList;
        notifyDataSetChanged();
    }

    //own:0->圈主,1->管理员,2->成员
    public void setTopLayoutHeight(int height, int own) {
        this.topLayoutHeight = height;
        this.own = own;
        String myown="";
        switch (own){
            case -1:
                myown="啥都不是";
                break;
            case 0:
                myown="圈主";
                break;
            case 1:
                myown="管理员";
                break;
            case 2:
                myown="老百姓";
                break;
        }
        System.out.println(myown);
    }

    public CircleMemeberManageListAdapter(ListView listView, Context context, int circle_id) {
        this.listView = listView;
        this.context = context;
        this.circle_id = circle_id;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(context);
        String userId = String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext));
        my_user_id = Integer.parseInt(userId);

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
        FrameLayout roleLayout;
        TextView circlerTextView, managerTextView;
        RelativeLayout menuLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_member_manage, null);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.roleLayout = convertView.findViewById(R.id.roleLayout);
            holder.circlerTextView = convertView.findViewById(R.id.circlerTextView);
            holder.managerTextView = convertView.findViewById(R.id.managerTextView);
            holder.menuLayout = convertView.findViewById(R.id.menuLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(list.get(position).getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getName());
        switch (list.get(position).getRole()) {
            case 0://圈主
                holder.circlerTextView.setVisibility(View.VISIBLE);
                holder.managerTextView.setVisibility(View.GONE);
                break;
            case 1://管理员
                holder.circlerTextView.setVisibility(View.GONE);
                holder.managerTextView.setVisibility(View.VISIBLE);
                break;
            case 2://成员
                holder.circlerTextView.setVisibility(View.GONE);
                holder.managerTextView.setVisibility(View.GONE);
                break;
        }
        if (list.get(position).getId() == my_user_id) {
            holder.menuLayout.setVisibility(View.INVISIBLE);
        } else {
            holder.menuLayout.setVisibility(View.VISIBLE);
        }
        holder.menuLayout.setOnClickListener(new OnMenuClickListener(position, holder.menuLayout));
        return convertView;
    }


    private class OnMenuClickListener implements View.OnClickListener {
        private int position;
        private RelativeLayout menuLayout;

        public OnMenuClickListener(int position, RelativeLayout layout) {
            this.position = position;
            this.menuLayout = layout;
        }

        @Override
        public void onClick(View v) {
//            System.out.println("own: "+own+"role: "+list.get(position).getRole()+" , circle_id: "+circle_id+" , tuid: "+list.get(position).getId());
            int itemHeight = v.getHeight();
            int count = position - listView.getFirstVisiblePosition() + 1;
            int disY = topLayoutHeight + itemHeight * count;
            if (disY > (screenHeight - topLayoutHeight) / 2) {
                CircleMemberManageTopMenuWindow window=   new CircleMemberManageTopMenuWindow(context, own, list.get(position).getRole(), circle_id, list.get(position).getId(), new CircleMemberManageTopMenuWindow.CircleMemberManageTopListener() {
                    @Override
                    public void onComplete() {
                        if (null != onRefreshUserListener) {
                            onRefreshUserListener.onRefreshUserList();
                        }
                    }
                });
                window.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int popHeight=window.getContentView().getMeasuredHeight();
                window.showAtTop(menuLayout,popHeight);
            } else {
                new CircleMemberManageBottomMenuWindow(context, own, list.get(position).getRole(), circle_id, list.get(position).getId(), new CircleMemberManageBottomMenuWindow.CircleMemberManageBottomListener() {
                    @Override
                    public void onComplete() {
                        if (null != onRefreshUserListener) {
                            onRefreshUserListener.onRefreshUserList();
                        }
                    }
                }).showAtBottom(menuLayout);
            }
        }
    }

    private OnRefreshUserListener onRefreshUserListener;

    public void setOnRefreshUserListener(OnRefreshUserListener listener) {
        this.onRefreshUserListener = listener;
    }

    public interface OnRefreshUserListener {
        void onRefreshUserList();
    }

}
