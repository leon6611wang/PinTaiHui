package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CustomerServiceMessage;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.List;

public class SearchServiceChatHistoryAdapter extends RecyclerView.Adapter<SearchServiceChatHistoryAdapter.ViewHolder> {
    private Context context;
    private List<CustomerServiceMessage> list;
    private String keyword;

    public SearchServiceChatHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CustomerServiceMessage> messageList, String kw) {
        this.list = messageList;
        this.keyword = kw;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_service_chat_history, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUser_avatar()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUser_name());
        holder.timeTextView.setText(list.get(position).getCreate_time());
        String content = list.get(position).getMessage().getTxt().getContent();
        if (!StringUtils.isNullOrEmpty(keyword)) {
            if (list.get(position).getMessage().getTxt().getContent().contains(keyword) &&
                    !list.get(position).getMessage().getTxt().getContent().contains("<font color ='#FE8627'>")) {
                String s = "<font color ='#FE8627'>" + keyword + "</font>";
                content=content.replaceAll(keyword, s);
                System.out.println("keyword: " + keyword + " , content: " + content);
            }
        }
        System.out.println(content);
        holder.contentTextView.setText(Html.fromHtml(content));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
