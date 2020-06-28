package com.leon.chic.utils;

import android.app.Application;
import android.os.Bundle;

import com.leon.chic.dao.MessageDao;
import com.leon.chic.listener.NotificationActionListener;
import com.leon.chic.model.CardMessage;
import com.leon.chic.model.JMessage;
import com.leon.chic.model.JPushCard;
import com.leon.chic.model.JPushMessage;
import com.leon.chic.model.ServiceMessage;
import com.leon.chic.model.ServiceMessageMessage;
import com.leon.chic.model.SystemMessage;

import java.text.SimpleDateFormat;

public class MessageUtils {
    private static final String TAG = "MessageUtils";
    private static MessageUtils utils;
    private static final String EXTRA_EXTRA = "cn.jpush.android.EXTRA";
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private boolean isCurrentPage;

    public static MessageUtils getInstance() {
        if (null == utils) {
            synchronized (MessageUtils.class) {
                utils = new MessageUtils();
            }
        }
        return utils;
    }

    public void setCurrentPage(boolean isCurrent) {
        this.isCurrentPage = isCurrent;
    }

    public void getMessage(Bundle bundle, Application app, NotificationActionListener listener) {
        String extras = bundle.getString(EXTRA_EXTRA);
        System.out.println("extras: " + extras);
        LogUtils.getInstance().show(TAG, "extras: " + extras);
        JMessage jMessage = GsonUtils.GsonToBean(extras, JMessage.class);
        LogUtils.getInstance().show(TAG, "jMessage: " + jMessage.getMessage());
        JPushMessage jPushMessage = GsonUtils.GsonToBean(jMessage.getMessage(), JPushMessage.class);
        LogUtils.getInstance().show(TAG, "jPushMessage: " + GsonUtils.GsonString(jPushMessage));
        if (jPushMessage.getMessage_type() == MessageTypeUtils.TOTAL_SYSTEM_MESSAGE) {//系统消息
            SystemMessage systemMessage = GsonUtils.GsonToBean(jPushMessage.getMessage_content(), SystemMessage.class);
            System.out.println("系统消息: " + systemMessage.getMessage_content());
            MessageDao.getInstance().saveSystemMessage(systemMessage, app);
            if (!isCurrentPage && null != listener) {
                String notificationTitle = null;
                switch (systemMessage.getMessage_type()) {
                    case MessageTypeUtils.XIAO_MI_SHU:
                        notificationTitle = "圈助小秘书新消息";
                        break;
                    case MessageTypeUtils.QUAN_YOU_QING_QIU:
                        notificationTitle = "圈友请求通知";
                        break;
                    case MessageTypeUtils.QUAN_ZI_SHEN_HE:
                        notificationTitle = "圈子审核通知";
                        break;
                    case MessageTypeUtils.FU_KUAN_TONG_ZHI:
                        notificationTitle = "支付通知";
                        break;
                    case MessageTypeUtils.KA_QUAN_TONG_ZHI:
                        notificationTitle = "卡券通知";
                        break;
                    case MessageTypeUtils.GUAN_ZHU_DIAN_PU:
                        notificationTitle = "关注店铺通知";
                        break;
                    case MessageTypeUtils.TOU_SU_FAN_KUI:
                        notificationTitle = "投诉反馈通知";
                        break;
                }

                listener.onNotificationAction(MessageTypeUtils.TOTAL_SYSTEM_MESSAGE, systemMessage.getMessage_type(), 0, null, notificationTitle,
                        systemMessage.getMessage_content(), sdf.format(systemMessage.getMessage_time()));
            }
        } else if (jPushMessage.getMessage_type() == MessageTypeUtils.TOTAL_CUSTOMER_SERVICE) {//客服消息
            ServiceMessage serviceMessage = GsonUtils.GsonToBean(jPushMessage.getMessage_content(), ServiceMessage.class);
            System.out.println("silence: " + serviceMessage.getSilence());
            if (serviceMessage.getSilence() == 0 && !isCurrentPage) {
                if (null != listener) {
                    String notificationContent = null;
                    switch (serviceMessage.getMsg_type()) {
                        case MessageTypeUtils.SERVICE_TYPE_TXT:
                            notificationContent = serviceMessage.getMessage().getTxt().getContent();
                            break;
                        case MessageTypeUtils.SERVICE_TYPE_IMAGE:
                            notificationContent = "[图片]";
                            break;
                        case MessageTypeUtils.SERVICE_TYPE_GOODS:
                            notificationContent = "[商品]";
                            break;
                        case MessageTypeUtils.SERVICE_TYPE_ORDER:
                            notificationContent = "[订单]";
                            break;

                    }
                    listener.onNotificationAction(MessageTypeUtils.TOTAL_CUSTOMER_SERVICE, 0, serviceMessage.getShop_id(),
                            serviceMessage.getUser_avatar(),
                            serviceMessage.getUser_name(), notificationContent,
                            sdf.format(Long.parseLong(serviceMessage.getCreate_time())));
                }
            }

            ServiceMessageMessage serviceMessageMessage = serviceMessage.getMessage();
            serviceMessage.setMessage_content(GsonUtils.GsonString(serviceMessageMessage));
            MessageDao.getInstance().saveServiceMessage(serviceMessage, app);
        } else if (jPushMessage.getMessage_type() == MessageTypeUtils.TOTAL_CARD) {//名片消息
            System.out.println("名片消息" + jPushMessage.getMessage_content());
            CardMessage cardMessage = GsonUtils.GsonToBean(jPushMessage.getMessage_content(), CardMessage.class);
            JPushCard card = GsonUtils.GsonToBean(cardMessage.getCard(), JPushCard.class);
            cardMessage.setjPushCard(card);
            MessageDao.getInstance().operatCardMessage(cardMessage,app);
            System.out.println(card.getId() + " , " + card.getUid());
        }
    }


}
