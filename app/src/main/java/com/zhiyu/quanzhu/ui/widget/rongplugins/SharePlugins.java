package com.zhiyu.quanzhu.ui.widget.rongplugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.rongorder.OrderMessage;
import com.zhiyu.quanzhu.ui.widget.rongshare.ShareMessage;
import com.zhiyu.quanzhu.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 名片
 */
public class SharePlugins implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.mingpian_plugins);
    }

    @Override
    public String obtainTitle(Context context) {
        return "内部分享";
    }

    @Override
    public void onClick(final Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
        ShareMessage shareMessage=new ShareMessage();
        shareMessage.setTitle("朴槿惠成韩坐牢时间最长总统 入狱1000天量刑未定");
        shareMessage.setDescription("中新网12月25日电 从韩国宪政史上首位女总统，到韩国史上坐牢时间最长的总统——7年前的朴槿惠或许不曾想到，2019年的圣诞节，将是她在监狱中度过的第1000天。现年67岁的朴槿惠，一直饱受着病痛的折磨，三大案件压身，量刑仍未最终敲定。2020年，“亲信干政案”和“国家情报院贿赂案”将陆续再度迎来审判，此前她的刑期已达到32年，如果量刑再加重，朴槿惠出狱时恐将超过百岁。");
        shareMessage.setImage_url("http://crawl.ws.126.net/d1284d63be74274963819b379df03a8d.jpg");
        shareMessage.setUrl("https://news.163.com/19/1225/00/F16VID8K00018AOR.html");
        Message message = Message.obtain(targetId, conversationType, shareMessage);
        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                System.out.println("targetId: "+targetId);
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
