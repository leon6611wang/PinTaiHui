package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCardFriend;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.activity.MingPianGuangChangActivity;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class RenMaiRecyclerAdapter extends BaseRecyclerAdapter<MyCardFriend> {
    private Context context;

    public RenMaiRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout emptyLayout;
        TextView addTextView;
        NiceImageView avatarImageView;
        TextView nameTextView, occupionTextView, companyTextView, cityTextView, industryTextView;
        ImageView rightImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            emptyLayout = itemView.findViewById(R.id.emptyLayout);
            addTextView = itemView.findViewById(R.id.addTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            occupionTextView = itemView.findViewById(R.id.occupionTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            industryTextView = itemView.findViewById(R.id.industryTextView);
            rightImageView = itemView.findViewById(R.id.rightImageView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renmai, parent, false));
    }


    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, final MyCardFriend data) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            if (data.isEmpty()) {
                holder.mCardView.setVisibility(View.GONE);
                holder.emptyLayout.setVisibility(View.VISIBLE);
                holder.addTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent guangchangIntent = new Intent(context, MingPianGuangChangActivity.class);
                        guangchangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(guangchangIntent);
                    }
                });
            } else {
                holder.mCardView.setVisibility(View.VISIBLE);
                holder.emptyLayout.setVisibility(View.GONE);
                Glide.with(context).load(data.getCard_thumb()).error(R.drawable.image_error).into(holder.avatarImageView);
                holder.nameTextView.setText(StringUtils.isNullOrEmpty(data.getNotename()) ? data.getCard_name() : data.getNotename());
                holder.occupionTextView.setText(data.getOccupation());
                holder.companyTextView.setText(data.getCompany());
                if (!StringUtils.isNullOrEmpty(data.getCity_name())) {
                    holder.cityTextView.setVisibility(View.VISIBLE);
                    holder.cityTextView.setText(data.getCity_name());
                } else {
                    holder.cityTextView.setVisibility(View.GONE);
                }
                if (!StringUtils.isNullOrEmpty(data.getIndustry())) {
                    holder.industryTextView.setVisibility(View.VISIBLE);
                    holder.industryTextView.setText(data.getIndustry());
                } else {
                    holder.industryTextView.setVisibility(View.GONE);
                }

                holder.rightImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onChat(data);
                    }
                });
                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CardInformationActivity.class);
                        intent.putExtra("card_id", (long) data.getId());
                        intent.putExtra("uid", (long) data.getUid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

    private void onChat(MyCardFriend cardFriend) {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(context).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        try {
            String name = StringUtils.isNullOrEmpty(cardFriend.getNotename()) ? cardFriend.getCard_name() : cardFriend.getNotename();
            RongIM.getInstance().startPrivateChat(context, String.valueOf(cardFriend.getUid()),
                    TextUtils.isEmpty(name) ? "聊天界面" : name);

//            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), list.get(position).getUser_name());
        } catch (Exception e) {
            e.printStackTrace();
            MessageToast.getInstance(context).show("无法进入会话界面");
        }
    }
}
