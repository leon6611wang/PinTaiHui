package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.IMCircle;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class HomeQuanLiaoRecyclerAdapter extends RecyclerView.Adapter<HomeQuanLiaoRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_15, screenWidth, layoutHeight, layoutWidth;
    private LinearLayout.LayoutParams ll;
    private float ratio = 0.5768f;
    private List<IMCircle> list;

    public void setUnReadCount(int position, int count) {
        if (null != list && list.size() > 0 && list.size() > position) {
            list.get(position).setUnReadCount(count);
            notifyDataSetChanged();
        }
    }

    public void setList(List<IMCircle> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public HomeQuanLiaoRecyclerAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        layoutWidth = screenWidth - dp_15 * 2;
        layoutHeight = (int) (layoutWidth * ratio);
        ll = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView circleImageView;
        TextView circleNameTextView, countTextView, userNameTextView,
                newMsgCountTextView, noticeTextView;
        CircleImageView userAvatarImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
            userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            newMsgCountTextView = itemView.findViewById(R.id.newMsgCountTextView);
            noticeTextView = itemView.findViewById(R.id.noticeTextView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_xiaoxi_quanliao, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getThumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.circleImageView);
        holder.circleNameTextView.setText(list.get(position).getName());
        holder.countTextView.setText(String.valueOf(list.get(position).getNum()));
        Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.userAvatarImageView);
        holder.userNameTextView.setText(list.get(position).getUsername());
        holder.newMsgCountTextView.setText("[" + list.get(position).getUnReadCount() + "条新消息]");
        holder.noticeTextView.setText("【公告】" + list.get(position).getNotice());
        holder.mCardView.setLayoutParams(ll);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                SharedPreferencesUtils.getInstance(context).setConversationType(Conversation.ConversationType.GROUP.getName().toLowerCase());
                try {
                    RongIM.getInstance().startConversation(context, Conversation.ConversationType.GROUP, String.valueOf(list.get(position).getId()), "群聊");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
