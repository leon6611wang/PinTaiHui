package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

public class ConversationQuickReplyRecyclerAdapter extends RecyclerView.Adapter<ConversationQuickReplyRecyclerAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView replyTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            replyTextView=itemView.findViewById(R.id.replyTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_quick_reply,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
