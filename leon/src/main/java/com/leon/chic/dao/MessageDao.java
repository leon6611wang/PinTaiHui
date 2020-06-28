package com.leon.chic.dao;

import android.app.Application;
import android.database.Cursor;

import com.leon.chic.listener.ServiceMessageListener;
import com.leon.chic.listener.ServiceMessageListener2;
import com.leon.chic.listener.SystemMessageListener;
import com.leon.chic.listener.SystemMessageVariableListener;
import com.leon.chic.model.CardMessage;
import com.leon.chic.model.JPushCard;
import com.leon.chic.model.JPushMessage;
import com.leon.chic.model.ServiceMessage;
import com.leon.chic.model.ServiceMessageMessage;
import com.leon.chic.model.SystemMessage;
import com.leon.chic.utils.DaoUtils;
import com.leon.chic.utils.GsonUtils;
import com.leon.chic.utils.LogUtils;
import com.leon.chic.utils.MessageTypeUtils;
import com.leon.chic.utils.TimeUtils;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private final String TAG = "MessageDao";
    private static MessageDao dao;

    public static MessageDao getInstance() {
        if (null == dao) {
            synchronized (MessageDao.class) {
                dao = new MessageDao();
            }
        }
        return dao;
    }

    /*******************系统消息***********************/
    public void saveSystemMessage(SystemMessage message, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().saveBindingId(message);
            MessageDao.getInstance().systemMessageCallBack(message.getMessage_type(), app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSystemMessage(int message_id, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().delete(SystemMessage.class, WhereBuilder.b("message_id", "=", String.valueOf(message_id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SystemMessage selectSystemMessage(int message_id, Application app) {
        SystemMessage systemMessage = null;
        try {
            systemMessage = DaoUtils.getInstance(app).getDb().selector(SystemMessage.class).where("message_id", "=", String.valueOf(message_id)).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemMessage;
    }


    private int selectUnReadCount(int message_type, Application app) {
        int unReadCount = 0;
        try {
            unReadCount = DaoUtils.getInstance(app).getDb().selector(SystemMessage.class).where("message_type", "=", String.valueOf(message_type)).and(WhereBuilder.b("read", "=", "0")).findAll().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unReadCount;
    }

    public SystemMessage getLatestSystemMessage(int message_type, Application app) {
        SystemMessage systemMessage = null;
        try {
            systemMessage = DaoUtils.getInstance(app).getDb().selector(SystemMessage.class).where("message_type", "=", String.valueOf(message_type))
                    .orderBy("message_time", true).findFirst();
            int unReadCount = selectUnReadCount(message_type, app);
            systemMessage.setUnReadCount(unReadCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemMessage;
    }

    private List<SystemMessage> selectAllSystemMessage(int message_type, Application app) {
        List<SystemMessage> list = null;
        try {
            list = DaoUtils.getInstance(app).getDb().selector(SystemMessage.class).where("message_type", "=", String.valueOf(message_type)).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void clearSystemMessage(Application app) {
        try {
            DaoUtils.getInstance(app).getDb().dropTable(SystemMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readAllUnReadSystemMessage(int message_type, Application app) {
        try {
            KeyValue read = new KeyValue("read", "1");
            DaoUtils.getInstance(app).getDb().update(SystemMessage.class,
                    WhereBuilder.b("message_type", "=", String.valueOf(message_type)), read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void systemMessageCallBack(int message_type, Application app) {
        SystemMessage systemMessage = getLatestSystemMessage(message_type, app);
        System.out.println("systemMessageCallBack: " + systemMessage.getMessage_content());
        if (null != systemMessageListener) {
            System.out.println("客服消息监听回调");
            systemMessageListener.onSystemMessage(systemMessage.getMessage_type(), systemMessage.getMessage_content(), TimeUtils.getInstance().getDisTime(systemMessage.getMessage_time()),
                    systemMessage.getUnReadCount());
        }
    }

    private SystemMessageListener systemMessageListener;

    public void setSystemMessageListener(SystemMessageListener listener) {
        this.systemMessageListener = listener;
    }

    /*******************客服消息***********************/

    private ServiceMessageListener serviceMessageListener;

    public void setServiceMessageListener(ServiceMessageListener listener) {
        this.serviceMessageListener = listener;
    }

    private ServiceMessageListener2 serviceMessageListener2;

    public void setServiceMessageListener2(ServiceMessageListener2 listener) {
        this.serviceMessageListener2 = listener;
    }

    public void clearServiceMessage(Application app) {
        try {
            DaoUtils.getInstance(app).getDb().dropTable(ServiceMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveServiceMessage(ServiceMessage message, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().saveBindingId(message);
            MessageDao.getInstance().serviceMessageCallBack(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serviceMessageCallBack(Application app) {
        ServiceMessage serviceMessage = getLatestServiceMessage(app);
        if (null != serviceMessageListener) {
            serviceMessageListener.serviceMessage(serviceMessage);
        }
        if (null != serviceMessageListener2) {
            serviceMessageListener2.serviceMessage(serviceMessage);
        }
    }

    public ServiceMessage getLatestServiceMessage(Application app) {
        ServiceMessage serviceMessage = null;
        try {
            serviceMessage = DaoUtils.getInstance(app).getDb().selector(ServiceMessage.class).orderBy("create_time", true).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceMessage;
    }

    private int serviceMessageCount(Application app) {
        int count = 0;
        try {
            count = DaoUtils.getInstance(app).getDb().findAll(ServiceMessage.class).size();
//            System.out.println("serviceMessageCount: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void serviceMessageList(Application app) {
        try {
            List<ServiceMessage> list = DaoUtils.getInstance(app).getDb().selector(ServiceMessage.class).orderBy("create_time", true).findAll();
            for (ServiceMessage message : list) {
                System.out.println(message.getMessage_content());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customerServiceList(Application app) {
        try {
            String sql = "SELECT * FROM service_message sm WHERE sm.create_time IN ( SELECT MAX(s.create_time) FROM service_message s GROUP BY s.shop_id ) ORDER BY sm.create_time DESC";
//            System.out.println("sql: "+sql);
            Cursor cursor = DaoUtils.getInstance(app).getDb().execQuery(sql);
            List<ServiceMessage> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                ServiceMessage sm = new ServiceMessage();
                sm.setShop_id(cursor.getInt(cursor.getColumnIndex("shop_id")));
                sm.setUser_avatar(cursor.getString(cursor.getColumnIndex("user_avatar")));
                sm.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
                sm.setMessage_content(cursor.getString(cursor.getColumnIndex("message_content")));
                sm.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
                sm.setMsg_type(cursor.getInt(cursor.getColumnIndex("msg_type")));
                String content = null;
                if (null != sm.getMessage_content() && !"".equals(sm.getMessage_content())) {
                    ServiceMessageMessage serviceMessageMessage = GsonUtils.GsonToBean(sm.getMessage_content(), ServiceMessageMessage.class);
                    if (null != serviceMessageMessage) {
                        switch (sm.getMsg_type()) {
                            case MessageTypeUtils.SERVICE_TYPE_TXT:
                                content = serviceMessageMessage.getTxt().getContent();
                                break;
                            case MessageTypeUtils.SERVICE_TYPE_IMAGE:
                                content = "[图片] 消息";
                                break;
                            case MessageTypeUtils.SERVICE_TYPE_GOODS:
                                content = "[商品] 消息";
                                break;
                            case MessageTypeUtils.SERVICE_TYPE_ORDER:
                                content = "[订单] 消息";
                                break;
                        }
                    }
                }
                if (null != sm && null != systemMessageVariableListener) {
                    systemMessageVariableListener.onSystemMessageVariable(0, sm.getShop_id(),
                            sm.getUser_avatar(), sm.getUser_name(), content, sm.getCreate_time());
                }
                list.add(sm);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SystemMessageVariableListener systemMessageVariableListener;

    public void setSystemMessageVariableListener(SystemMessageVariableListener listener) {
        this.systemMessageVariableListener = listener;
    }

    /*******************名片消息*****************/
    public void operatCardMessage(CardMessage cardMessage, Application app) {
        System.out.println(cardMessage.getjPushCard().getCard_name());
        switch (cardMessage.getMessage_type()) {
            case MessageTypeUtils.CARD_TYPE_EDIT:
                System.out.println("名片消息 edit");
                CardDao.getInstance().save(GsonUtils.GsonString(cardMessage.getjPushCard()), app);
                break;
            case MessageTypeUtils.CARD_TYPE_DELETE:
                System.out.println("名片消息 delete");
                CardDao.getInstance().deleteCard(cardMessage.getjPushCard().getId(), app);
                break;
            case MessageTypeUtils.CARD_TYPE_ADD:
                System.out.println("名片消息 add");
                CardDao.getInstance().save(GsonUtils.GsonString(cardMessage.getjPushCard()), app);
                break;
        }
        if (null != onCardMessageChangeListener) {
            onCardMessageChangeListener.onCardMessageChange();
        }
    }

    private OnCardMessageChangeListener onCardMessageChangeListener;

    public void setOnCardMessageChangeListener(OnCardMessageChangeListener listener) {
        this.onCardMessageChangeListener = listener;
    }

    public interface OnCardMessageChangeListener {
        void onCardMessageChange();
    }

}
