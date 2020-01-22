package com.zhiyu.quanzhu.ui.widget.rongplugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.dialog.ConversationQuickReplyDialog;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * 快速回复
 */
public class QuickReplyPlugins implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;
    private Context context;
    private ConversationQuickReplyDialog dialog;

    @Override
    public Drawable obtainDrawable(Context context) {
        this.context = context;
        return ContextCompat.getDrawable(context, R.mipmap.reply_plugins);
    }

    @Override
    public String obtainTitle(Context context) {
        this.context = context;
        return "快速回复";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        if (null == dialog) {
            dialog = new ConversationQuickReplyDialog(context, R.style.dialog_transparent);
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
