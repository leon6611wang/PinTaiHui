package com.zhiyu.quanzhu.ui.widget.rongfrend;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = FrendMessage.class, showReadState = false)
public class FrendMessageItemProvider extends IContainerItemProvider.MessageProvider<FrendMessage> {
    private Context context;


    public FrendMessageItemProvider(Context context) {
        super();
        this.context=context;
    }

    @Override
    public void bindView(View view, int i, FrendMessage frendMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        //AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
        //holder.tvTitle.setText(redPackageMessage.getTitle());
        Glide.with(context).load(frendMessage.getImage()).into(holder.imageImageView);
        holder.nameTextView.setText(frendMessage.getName());
        holder.titleTextView.setText(frendMessage.getTitle());
        //holder.tvDesc1.setText(redPackageMessage.getDesc1());
        //holder.tvDesc2.setText(redPackageMessage.getDesc2());
    }

    @Override
    public Spannable getContentSummary(FrendMessage frendMessage) {
        return new SpannableString(frendMessage.getName());
    }

    @Override
    public void onItemClick(View view, int i, FrendMessage frendMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.rong_mingpian_message, null);
        ViewHolder holder = new ViewHolder();
        holder.titleTextView = view.findViewById(R.id.titleTextView);
        holder.nameTextView = view.findViewById(R.id.nameTextView);
        holder.imageImageView = view.findViewById(R.id.imageImageView);
        view.setTag(holder);
        return view;
    }

    private static class ViewHolder {
        RoundImageView imageImageView;
        TextView nameTextView;
        TextView titleTextView;
    }
}
