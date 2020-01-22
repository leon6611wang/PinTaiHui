package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyConversation;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.ui.adapter.XiaoXiXiaoXiListAdapter;
import com.zhiyu.quanzhu.ui.dialog.MessageMenuUpDialog;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;


public class FragmentXiaoXiXiaoXi extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener {
    private View view;
    private ListView listView;
    private XiaoXiXiaoXiListAdapter adapter;
    private List<MyConversation> list = new ArrayList<>();
    private MessageMenuUpDialog upDialog;
    private int menuX, menuY;
    private List<String> headerPicList = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentXiaoXiXiaoXi> fragmentXiaoXiXiaoXiWeakReference;

        public MyHandler(FragmentXiaoXiXiaoXi fragment) {
            fragmentXiaoXiXiaoXiWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            FragmentXiaoXiXiaoXi fragment = fragmentXiaoXiXiaoXiWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.adapter.setList(fragment.list);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xiaoxi_xiaoxi, container, false);
        initViews();
        initDialogs();
//        initData();
        return view;
    }

    private void initData() {
        headerPicList.add("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190518/d38fda99a9654dd2b5b60950a1cb9967.jpeg");
        headerPicList.add("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190518/96429d741c0844abae96942dd5b403f8.jpeg");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201707/07/20170707113819_SMRch.thumb.700_0.jpeg");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201809/26/20180926162125_vjbwi.thumb.700_0.jpg");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201504/12/20150412H5653_LWfVe.thumb.700_0.jpeg");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201512/27/20151227103618_XYAkh.thumb.700_0.jpeg");
        headerPicList.add("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190518/b7f1ec8b65c740e38924380a500a7ad1.webp");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201505/11/20150511073557_PmUFZ.thumb.700_0.jpeg");
        headerPicList.add("https://c-ssl.duitang.com/uploads/item/201412/02/20141202074543_zsvjG.thumb.700_0.jpeg");
    }

    private void initViews() {

        listView = view.findViewById(R.id.listView);
        adapter = new XiaoXiXiaoXiListAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int y = (view.getHeight() + 1) * (position - listView.getFirstVisiblePosition() + 1 / 4);
//                System.out.println("y: "+y);
//                menuY=y;
                upDialog.show();
                upDialog.setLocation(menuX, menuY);
                return false;
            }
        });
        listView.setOnTouchListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initDialogs() {
        upDialog = new MessageMenuUpDialog(getContext(), R.style.dialog);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            menuX = (int) event.getX();
            menuY = (int) event.getY();
//            System.out.println("getY(): "+event.getY());
        }
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (list.size() > 0) {
                list.clear();
            }
            getConversationList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (list.size() > 0) {
            list.clear();
        }
        getConversationList();
    }

    private void getConversationList() {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (null != conversations && conversations.size() > 0) {
                    for (int i = 0; i < conversations.size(); i++) {
                        final Conversation conversation = conversations.get(i);
                        final MyConversation myConversation = new MyConversation();
                        if (conversation.getConversationType().getName().toLowerCase().equals(SharedPreferencesUtils.IM_PRIVATE)) {
                            myConversation.setType(conversation.getConversationType().getName().toLowerCase());
                            myConversation.setTop(conversation.isTop());
                            System.out.println("senderId: " + conversation.getSenderUserId() + " , targetId: " + conversation.getTargetId());
                            if (SharedPreferencesUtils.getInstance(getContext()).getUserId().equals(conversation.getSenderUserId())) {
                                myConversation.setUserId(conversation.getTargetId());
                            } else {
                                myConversation.setUserId(conversation.getSenderUserId());
                            }
//                            System.out.println(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getUser_name());
                            myConversation.setUserName(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getUser_name());
                            myConversation.setHeaderPic(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getHeader_pic());
                            myConversation.setMessageTime(conversation.getSentTime());
                            RongIMClient.getInstance().getMessage(conversation.getLatestMessageId(), new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    if (message.getContent() instanceof TextMessage) {
                                        final TextMessage textMessage = (TextMessage) message.getContent();
                                        myConversation.setMessageContent(textMessage.getContent());
                                        myConversation.setRead(conversation.getReceivedStatus().isRead());
//                                    System.out.println("content: " + textMessage.getContent() + " , isRead: " + conversation.getReceivedStatus().isRead());
                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });

                            list.add(myConversation);
                        }

                    }
                }
                android.os.Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(getContext()).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());

        System.out.println("userId:" + list.get(position).getUserId() + " , userName: " + (TextUtils.isEmpty(list.get(position).getUserName()) ? "聊天界面" : list.get(position).getUserName()));
        try {
            RongIM.getInstance().startPrivateChat(getActivity(), list.get(position).getUserId(),
                TextUtils.isEmpty(list.get(position).getUserName()) ? "聊天界面" : list.get(position).getUserName());

//            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), list.get(position).getUserName());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"无法进入会话界面",Toast.LENGTH_SHORT).show();
        }
    }
}
