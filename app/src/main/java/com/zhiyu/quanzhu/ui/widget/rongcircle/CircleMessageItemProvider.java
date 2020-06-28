package com.zhiyu.quanzhu.ui.widget.rongcircle;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.GsonUtils;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = CircleMessage.class, showReadState = true)
public class CircleMessageItemProvider extends IContainerItemProvider.MessageProvider<CircleMessage> {
    private Context context;


    public CircleMessageItemProvider(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void bindView(View view, int i, CircleMessage circleMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        System.out.println("CircleMessage : "+ GsonUtils.GsonString(circleMessage));
        Glide.with(context).load(circleMessage.getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        if (!StringUtils.isNullOrEmpty(circleMessage.getUser_name()))
            holder.nameTextView.setText(circleMessage.getUser_name());
        holder.dayTextView.setText(String.valueOf(circleMessage.getDays()));
        Glide.with(context).load(circleMessage.getThumb()).error(R.drawable.image_error).into(holder.coverImageView);
        if (!StringUtils.isNullOrEmpty(circleMessage.getName()))
            holder.circleNameTextView.setText(circleMessage.getName());
        if (!StringUtils.isNullOrEmpty(circleMessage.getDescirption()))
            holder.descTextView.setText(circleMessage.getDescirption());
        holder.pnumTextView.setText(String.valueOf(circleMessage.getPnum()));
        holder.fnumTextView.setText(String.valueOf(circleMessage.getFnum()));
        if (StringUtils.isNullOrEmpty(circleMessage.getCity_name())) {
            holder.cityTextView.setVisibility(View.GONE);
        } else {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(circleMessage.getCity_name());
        }
        if (StringUtils.isNullOrEmpty(circleMessage.getTwo_industry())) {
            holder.industryTextView.setVisibility(View.GONE);
        } else {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(circleMessage.getTwo_industry());
        }
    }

    @Override
    public Spannable getContentSummary(CircleMessage circleMessage) {
        return new SpannableString(circleMessage.getName());
    }

    @Override
    public void onItemClick(View view, int i, CircleMessage circleMessage, UIMessage uiMessage) {
        System.out.println("圈子 点击");
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rong_circle_message, null);
        ViewHolder holder = new ViewHolder();
        holder.avatarImageView = view.findViewById(R.id.avatarImageView);
        holder.nameTextView = view.findViewById(R.id.nameTextView);
        holder.dayTextView = view.findViewById(R.id.dayTextView);
        holder.coverImageView = view.findViewById(R.id.coverImageView);
        holder.circleNameTextView = view.findViewById(R.id.circleNameTextView);
        holder.descTextView = view.findViewById(R.id.descTextView);
        holder.cityTextView = view.findViewById(R.id.cityTextView);
        holder.industryTextView = view.findViewById(R.id.industryTextView);
        holder.pnumTextView = view.findViewById(R.id.pnumTextView);
        holder.fnumTextView = view.findViewById(R.id.fnumTextView);
        holder.mCardView=view.findViewById(R.id.mCardView);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("圈子cardview点击");
            }
        });
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, dayTextView, circleNameTextView, descTextView, cityTextView, industryTextView, pnumTextView, fnumTextView;
        NiceImageView coverImageView;
        CardView mCardView;
    }
}
