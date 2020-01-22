package com.zhiyu.quanzhu.ui.widget.rongplugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.rongmingpian.MingPianMessage;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 名片
 */
public class MingPianPlugins implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.mingpian_plugins);
    }

    @Override
    public String obtainTitle(Context context) {
        return "名片";
    }

    @Override
    public void onClick(final Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
        MingPianMessage mingPianMessage = new MingPianMessage();
        mingPianMessage.setImage("https://c-ssl.duitang.com/uploads/item/201701/31/20170131212334_xFrhT.thumb.700_0.jpeg");
        mingPianMessage.setName("哆啦A梦");
        mingPianMessage.setTitle("机器人");
        Message message = Message.obtain(targetId, conversationType, mingPianMessage);
//        Message message = Message.obtain(targetId, conversationType, TextMessage.obtain("示例插件功能"));
        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                Toast.makeText(fragment.getActivity(), "消息发送成功, 示例获取 Context", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
