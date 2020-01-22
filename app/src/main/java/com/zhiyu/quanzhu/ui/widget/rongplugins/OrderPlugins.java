package com.zhiyu.quanzhu.ui.widget.rongplugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.rongmingpian.MingPianMessage;
import com.zhiyu.quanzhu.ui.widget.rongorder.OrderMessage;
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
public class OrderPlugins implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.mingpian_plugins);
    }

    @Override
    public String obtainTitle(Context context) {
        return "订单确认";
    }

    @Override
    public void onClick(final Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
        System.out.println("OrderPlugins targetId: "+targetId);
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setAll_count(4);
        orderMessage.setAll_price(2189.67f);
        orderMessage.setOrder_status(1);
        orderMessage.setOrder_status_desc("未确认");
        orderMessage.setBuyer_name("擎天柱");
        orderMessage.setBuyer_phone("18757591055");
        orderMessage.setBuyer_address("浙江省杭州市滨江区江虹国际产业园6栋301");
        OrderMessage.OrderGoods g1 = orderMessage.new OrderGoods();
        g1.setGoods_count(8);
        g1.setGoods_image("https://c-ssl.duitang.com/uploads/item/201607/27/20160727215710_3rsCH.thumb.700_0.jpeg");
        g1.setGoods_name("订单消息商品测试1");
        OrderMessage.OrderGoods g2 = orderMessage.new OrderGoods();
        g2.setGoods_count(7);
        g2.setGoods_image("https://c-ssl.duitang.com/uploads/item/201610/11/20161011165733_FJPAC.thumb.700_0.jpeg");
        g2.setGoods_name("订单消息商品测试2");
        OrderMessage.OrderGoods g3 = orderMessage.new OrderGoods();
        g3.setGoods_count(2);
        g3.setGoods_image("https://c-ssl.duitang.com/uploads/item/201701/19/20170119121245_VeaMW.thumb.700_0.jpeg");
        g3.setGoods_name("订单消息商品测试3");
        OrderMessage.OrderGoods g4 = orderMessage.new OrderGoods();
        g4.setGoods_count(5);
        g4.setGoods_image("https://c-ssl.duitang.com/uploads/item/201701/19/20170119121357_3mMNh.thumb.700_0.jpeg");
        g4.setGoods_name("订单消息商品测试4");
        List<OrderMessage.OrderGoods> goodsList = new ArrayList<>();
        goodsList.add(g1);
        goodsList.add(g2);
        goodsList.add(g3);
        goodsList.add(g4);
        orderMessage.setOrder_goods_list(GsonUtils.GsonString(goodsList));
        Message message = Message.obtain(targetId, conversationType, orderMessage);
        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }

            @Override
            public void onSuccess(Message message) {
                System.out.println("targetId: "+targetId);
//                Toast.makeText(fragment.getActivity(), "消息发送成功, 示例获取 Context", Toast.LENGTH_SHORT).show();
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
