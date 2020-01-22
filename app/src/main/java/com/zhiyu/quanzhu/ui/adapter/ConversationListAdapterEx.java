package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

public class ConversationListAdapterEx extends ConversationListAdapter {
    private Context context;

    public ConversationListAdapterEx(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        return super.newView(context, position, group);
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        super.bindView(v, position, data);
        if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
            data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);
        }
        v.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

}
