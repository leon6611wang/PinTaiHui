package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.leon.chic.dao.MessageDao;
import com.leon.chic.listener.ServiceMessageListener;
import com.leon.chic.listener.ServiceMessageListener2;
import com.leon.chic.listener.SystemMessageListener;
import com.leon.chic.listener.SystemMessageVariableListener;
import com.leon.chic.model.ServiceMessage;
import com.leon.chic.model.ServiceMessageMessage;
import com.leon.chic.model.SystemMessage;
import com.leon.chic.utils.GsonUtils;
import com.leon.chic.utils.MessageTypeUtils;
import com.leon.chic.utils.MessageUtils;
import com.leon.chic.utils.TimeUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.XiTongXiaoXi;
import com.zhiyu.quanzhu.ui.adapter.XiaoXiXiTongRecyclerAdapter;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class FragmentXiaoXiXiTong extends Fragment implements SystemMessageListener, ServiceMessageListener2, SystemMessageVariableListener {
    private View view;
    private RecyclerView mRecyclerView;
    private XiaoXiXiTongRecyclerAdapter adapter;
    private List<XiTongXiaoXi> list = new ArrayList<>();

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentXiaoXiXiTong> fragmentXiaoXiXiaoXiWeakReference;

        public MyHandler(FragmentXiaoXiXiTong fragment) {
            fragmentXiaoXiXiaoXiWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            FragmentXiaoXiXiTong fragment = fragmentXiaoXiXiaoXiWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xiaoxi_xitong, container, false);
        initDatas();
        initViews();
        initSystemMessageList();
        MessageDao.getInstance().setSystemMessageListener(this);
        MessageDao.getInstance().setSystemMessageVariableListener(this);
        MessageDao.getInstance().setServiceMessageListener2(this);
        MessageDao.getInstance().customerServiceList(BaseApplication.getInstance());
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MessageUtils.getInstance().setCurrentPage(true);
        } else {
            MessageUtils.getInstance().setCurrentPage(false);
        }

    }

    private void initSystemMessageList() {
        SystemMessage xiaoMiShuMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.XIAO_MI_SHU, BaseApplication.getInstance());
        if (null != xiaoMiShuMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.XIAO_MI_SHU) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(xiaoMiShuMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(xiaoMiShuMessage.getMessage_time()));
            list.get(index).setMsg(xiaoMiShuMessage.getMessage_content());
            list.get(index).setMessage_time(xiaoMiShuMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage quanYouQingQiuMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.QUAN_YOU_QING_QIU, BaseApplication.getInstance());
        if (null != quanYouQingQiuMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.QUAN_YOU_QING_QIU) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(quanYouQingQiuMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(quanYouQingQiuMessage.getMessage_time()));
            list.get(index).setMsg(quanYouQingQiuMessage.getMessage_content());
            list.get(index).setMessage_time(quanYouQingQiuMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage quanZiShenHeMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.QUAN_ZI_SHEN_HE, BaseApplication.getInstance());
        if (null != quanZiShenHeMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.QUAN_ZI_SHEN_HE) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(quanZiShenHeMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(quanZiShenHeMessage.getMessage_time()));
            list.get(index).setMsg(quanZiShenHeMessage.getMessage_content());
            list.get(index).setMessage_time(quanZiShenHeMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage fuKuanTongZhiMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.FU_KUAN_TONG_ZHI, BaseApplication.getInstance());
        if (null != fuKuanTongZhiMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.FU_KUAN_TONG_ZHI) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(fuKuanTongZhiMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(fuKuanTongZhiMessage.getMessage_time()));
            list.get(index).setMsg(fuKuanTongZhiMessage.getMessage_content());
            list.get(index).setMessage_time(fuKuanTongZhiMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage kaQuanTongZhiMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.KA_QUAN_TONG_ZHI, BaseApplication.getInstance());
        if (null != kaQuanTongZhiMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.KA_QUAN_TONG_ZHI) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(kaQuanTongZhiMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(kaQuanTongZhiMessage.getMessage_time()));
            list.get(index).setMsg(kaQuanTongZhiMessage.getMessage_content());
            list.get(index).setMessage_time(kaQuanTongZhiMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage guanZhuDianPuMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.GUAN_ZHU_DIAN_PU, BaseApplication.getInstance());
        if (null != guanZhuDianPuMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.GUAN_ZHU_DIAN_PU) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(guanZhuDianPuMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(guanZhuDianPuMessage.getMessage_time()));
            list.get(index).setMsg(guanZhuDianPuMessage.getMessage_content());
            list.get(index).setMessage_time(guanZhuDianPuMessage.getMessage_time());
            adapter.setList(list);
        }
        SystemMessage touSuFanKuiMessage = MessageDao.getInstance().getLatestSystemMessage(MessageTypeUtils.TOU_SU_FAN_KUI, BaseApplication.getInstance());
        if (null != touSuFanKuiMessage) {
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMessage_type() == MessageTypeUtils.TOU_SU_FAN_KUI) {
                    index = i;
                    break;
                }
            }
            list.get(index).setMsgCount(touSuFanKuiMessage.getUnReadCount());
            list.get(index).setTime(TimeUtils.getInstance().getDisTime(touSuFanKuiMessage.getMessage_time()));
            list.get(index).setMsg(touSuFanKuiMessage.getMessage_content());
            list.get(index).setMessage_time(touSuFanKuiMessage.getMessage_time());
            adapter.setList(list);
        }
    }

    @Override
    public void onSystemMessage(int message_type, String message_content, String time, int count) {
        System.out.println("message_type: "+message_type+" , message_content: "+message_content+" , time: "+time+" , count: "+count);
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMessage_type() == message_type) {
                index = i;
                break;
            }
        }
        System.out.println("systemMessage-index: "+index);
        list.get(index).setMsgCount(count);
        list.get(index).setTime(time);
        list.get(index).setMsg(message_content);
        list.get(index).setMessage_time(Long.parseLong(time));
        adapter.setList(list);
    }

    @Override
    public void serviceMessage(ServiceMessage message) {
        String content = null;
        switch (message.getMsg_type()) {
            case MessageTypeUtils.SERVICE_TYPE_TXT:
                if (!StringUtils.isNullOrEmpty(message.getMessage_content())) {
                    ServiceMessageMessage serviceMessageMessage = GsonUtils.GsonToBean(message.getMessage_content(), ServiceMessageMessage.class);
                    content = serviceMessageMessage.getTxt().getContent();
                } else {
                    content = "文字消息";
                }
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
        System.out.println("客服消息回调: id: " + message.getShop_id() + " , url: " + message.getUser_avatar() + " , title: " + message.getUser_name() + " , content: " + content + " , time: " + message.getCreate_time());
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getShop_id() == message.getShop_id()) {
                index = i;
                break;
            }
        }
        System.out.println("----> index: "+index);
        if (index > -1) {
            list.remove(index);
        }
        XiTongXiaoXi xx = new XiTongXiaoXi();
        xx.setAvatar(message.getUser_avatar());
        xx.setName(message.getUser_name());
        xx.setMsg(content);
        xx.setTime(TimeUtils.getInstance().getDisTime(Long.parseLong(message.getCreate_time())));
        xx.setMessage_time(Long.parseLong(message.getCreate_time()));
        xx.setShop_id(message.getShop_id());
        xx.setGuanFang(false);
        xx.setMessage_type(0);
        list.add(xx);
        adapter.setList(list);
    }

    @Override
    public void onSystemMessageVariable(int system_message_type, int id, String img_url, String title, String content, String time) {
//        System.out.println("type: " + system_message_type + " , id: " + id + " , url: " + img_url + " , title: " + title + " , content: " + content + " , time: " + time);
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getShop_id() == id) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            list.remove(index);
        }
        XiTongXiaoXi xx = new XiTongXiaoXi();
        xx.setAvatar(img_url);
        xx.setName(title);
        xx.setMsg(content);
        xx.setTime(TimeUtils.getInstance().getDisTime(Long.parseLong(time)));
        xx.setMessage_time(Long.parseLong(time));
        xx.setShop_id(id);
        xx.setGuanFang(false);
        xx.setMessage_type(0);
        list.add(xx);
        adapter.setList(list);
    }

    private void initDatas() {
        if (null != list && list.size() == 0) {
            XiTongXiaoXi x1 = new XiTongXiaoXi();
            x1.setIcon(R.mipmap.quanzhuxiaomishu);
            x1.setName("圈助小秘书");
            x1.setGuanFang(true);
            x1.setMessage_type(MessageTypeUtils.XIAO_MI_SHU);
            list.add(x1);

            XiTongXiaoXi x2 = new XiTongXiaoXi();
            x2.setIcon(R.mipmap.quanyouqingqiu);
            x2.setName("圈友请求");
            x2.setMessage_type(MessageTypeUtils.QUAN_YOU_QING_QIU);
            x2.setGuanFang(false);
            list.add(x2);

            XiTongXiaoXi x3 = new XiTongXiaoXi();
            x3.setIcon(R.mipmap.quanzishenhe);
            x3.setName("圈子审核");
            x3.setMessage_type(MessageTypeUtils.QUAN_ZI_SHEN_HE);
            x3.setGuanFang(false);
            list.add(x3);

            XiTongXiaoXi x4 = new XiTongXiaoXi();
            x4.setIcon(R.mipmap.tuikuantongzhi);
            x4.setMessage_type(MessageTypeUtils.FU_KUAN_TONG_ZHI);
            x4.setName("支付通知");
            x4.setGuanFang(false);
            list.add(x4);

            XiTongXiaoXi x5 = new XiTongXiaoXi();
            x5.setIcon(R.mipmap.kaquantongzhi);
            x5.setMessage_type(MessageTypeUtils.KA_QUAN_TONG_ZHI);
            x5.setName("卡券通知");
            x5.setGuanFang(false);
            list.add(x5);

            XiTongXiaoXi x6 = new XiTongXiaoXi();
            x6.setIcon(R.mipmap.guanzhudianpu);
            x6.setName("关注店铺");
            x6.setMessage_type(MessageTypeUtils.GUAN_ZHU_DIAN_PU);
            x6.setGuanFang(false);
            list.add(x6);

            XiTongXiaoXi x7 = new XiTongXiaoXi();
            x7.setIcon(R.mipmap.tousufankui);
            x7.setName("投诉反馈");
            x7.setMessage_type(MessageTypeUtils.TOU_SU_FAN_KUI);
            x7.setGuanFang(false);
            list.add(x7);
        }
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new XiaoXiXiTongRecyclerAdapter(getContext());
        adapter.setList(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }


}
