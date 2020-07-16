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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.MyConversation;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.ui.activity.ConversationPrivateSettingActivity;
import com.zhiyu.quanzhu.ui.adapter.XiaoXiXiaoXiListAdapter;
import com.zhiyu.quanzhu.ui.dialog.MessageMenuUpDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.rongcircle.CircleMessage;
import com.zhiyu.quanzhu.ui.widget.rongmingpian.MingPianMessage;
import com.zhiyu.quanzhu.ui.widget.rongshare.ShareMessage;
import com.zhiyu.quanzhu.utils.BaseDataUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;


public class FragmentXiaoXiXiaoXi extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener,
        ConversationPrivateSettingActivity.OnDeleteConversationListener,
        FragmentHomeRenMai.OnRemoveConversationListener {
    private View view;
    private ListView listView;
    private LinearLayout emptyLayout;
    private XiaoXiXiaoXiListAdapter adapter;
    private List<MyConversation> list = new ArrayList<>();
    private MessageMenuUpDialog upDialog;
    private int menuX, menuY;
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
                    fragment.isRequesting = false;
                    if (null != fragment.list && fragment.list.size() > 0) {
                        fragment.adapter.setList(fragment.list);
                        fragment.listView.setVisibility(View.VISIBLE);
                        fragment.emptyLayout.setVisibility(View.GONE);
                    } else {
                        fragment.listView.setVisibility(View.VISIBLE);
                        fragment.emptyLayout.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    Bundle bundle1 = (Bundle) msg.obj;
                    int position = bundle1.getInt("position");
                    MyConversation conversation = (MyConversation) bundle1.getSerializable("conversation");
                    fragment.refreshUnReadMsg(position, conversation.getMessageContent(), conversation.getMessageTime(), conversation.getUnreadCount(), conversation.isRead());
                    break;
                case 3:
                    Bundle bundle2 = (Bundle) msg.obj;
                    MyConversation conversation2 = (MyConversation) bundle2.getSerializable("conversation");
                    fragment.refreshUnReadMsgConversation(conversation2);
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
        ConversationPrivateSettingActivity.setOnDeleteConversationListener(this);
        FragmentHomeRenMai.setOnRemoveConversationListener(this);
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting && list.size() == 0) {
            isRequesting = true;
            getConversationList();
        }
        getResumeConversationList();
    }

    private boolean isRequesting = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isRequesting && list.size() == 0) {
            isRequesting = true;
            getConversationList();
        }
    }

    @Override
    public void onDeleteConversation(int id) {
        if (null != list && list.size() > 0) {
            int index = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserId().equals(String.valueOf(id))) {
                    index = i;
                    break;
                }
            }
            System.out.println("index: " + index);
            if (index > -1) {
                adapter.deleteConversation(index);
            }
        }
    }

    @Override
    public void onRemoveConversation(int id) {
        if (null != list && list.size() > 0) {
            int index = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserId().equals(String.valueOf(id))) {
                    index = i;
                    break;
                }
            }
            System.out.println("index: " + index);
            if (index > -1) {
                adapter.deleteConversation(index);
            }
        }
    }

    /**
     * 未读消息监听回调
     *
     * @param i
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            getCurrentConversationList();
        }
    };

    private void refreshUnReadMsg(int position, String content, long time, int count, boolean isRead) {
        if (null != adapter) {
            if (list.size() > 0 && list.size() > position) {
                list.get(position).setMessageContent(content);
                list.get(position).setMessageTime(time);
                list.get(position).setUnreadCount(count);
                list.get(position).setRead(isRead);
            }
            adapter.setUnReadMsgCount(position, content, time, count, isRead);
        }
    }

    private void refreshUnReadMsgConversation(MyConversation refreshConversation) {
        list.add(0, refreshConversation);
        adapter.setList(list);
    }


    private void initViews() {
        emptyLayout = view.findViewById(R.id.emptyLayout);
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
                upDialog.setLocation(menuX, menuY, position);
                return true;
            }
        });
        listView.setOnTouchListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initDialogs() {
        upDialog = new MessageMenuUpDialog(getContext(), R.style.dialog, new MessageMenuUpDialog.OnMessageMenuListener() {
            @Override
            public void onTop(final int position) {
                RongIMClient.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, list.get(position).getUserId(),
                        true, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                if (aBoolean) {
                                    adapter.setConversationTop(position, aBoolean);
                                }
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                MessageToast.getInstance(getContext()).show("操作失败，请稍后再试");
                            }
                        });
            }

            @Override
            public void onDelete(final int position) {
                RongIMClient.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            adapter.deleteConversation(position);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        MessageToast.getInstance(getContext()).show("操作失败，请稍后再试");
                    }
                });
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            menuX = (int) event.getX();
            menuY = (int) event.getY();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
    }

    private void getConversationList() {
//        System.out.println("getConversationList");
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (null != conversations && conversations.size() > 0) {
                    list.clear();
                    for (int i = 0; i < conversations.size(); i++) {
                        final Conversation conversation = conversations.get(i);
//                        System.out.println("单聊: " + (null==conversation.getLatestMessage().getUserInfo()?"未获取到个人信息":conversation.getLatestMessage().getUserInfo().getName()));
                        final MyConversation myConversation = new MyConversation();
                        if (conversation.getConversationType().getName().toLowerCase().equals(SharedPreferencesUtils.IM_PRIVATE)) {
                            myConversation.setType(conversation.getConversationType().getName().toLowerCase());
                            myConversation.setTop(conversation.isTop());

                            myConversation.setUnreadCount(conversation.getUnreadMessageCount());
                            if (String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)).equals(conversation.getSenderUserId())) {
                                myConversation.setUserId(conversation.getTargetId());
                            } else {
                                myConversation.setUserId(conversation.getSenderUserId());
                            }
                            myConversation.setUserName(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getUser_name());
                            myConversation.setHeaderPic(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getHeader_pic());
                            myConversation.setMessageTime(conversation.getSentTime());
                            RongIMClient.getInstance().getMessage(conversation.getLatestMessageId(), new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    if (message.getContent() instanceof TextMessage) {
                                        final TextMessage textMessage = (TextMessage) message.getContent();
                                        myConversation.setMessageContent(textMessage.getContent());
                                    } else if (message.getContent() instanceof ShareMessage) {
                                        myConversation.setMessageContent("【分享消息】");
                                    } else if (message.getContent() instanceof MingPianMessage) {
                                        myConversation.setMessageContent("【名片】");
                                    } else if (message.getContent() instanceof CircleMessage) {
                                        myConversation.setMessageContent("【圈子】");
                                    } else if (message.getContent() instanceof ImageMessage) {
                                        myConversation.setMessageContent("【图片】");
                                    } else if (message.getContent() instanceof FileMessage) {
                                        myConversation.setMessageContent("【文件】");
                                    }
                                    myConversation.setRead(conversation.getReceivedStatus().isRead());
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

    private void getCurrentConversationList() {
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
                            myConversation.setUnreadCount(conversation.getUnreadMessageCount());
                            if (String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)).equals(conversation.getSenderUserId())) {
                                myConversation.setUserId(conversation.getTargetId());
                            } else {
                                myConversation.setUserId(conversation.getSenderUserId());
                            }
                            myConversation.setUserName(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getUser_name());
                            myConversation.setHeaderPic(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getHeader_pic());
                            myConversation.setMessageTime(conversation.getSentTime());
                            RongIMClient.getInstance().getMessage(conversation.getLatestMessageId(), new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
//                                    System.out.println("消息监听: "+GsonUtils.GsonString(message));
                                    if (message.getContent() instanceof TextMessage) {
                                        final TextMessage textMessage = (TextMessage) message.getContent();
                                        myConversation.setMessageContent(textMessage.getContent());
                                    } else if (message.getContent() instanceof ShareMessage) {
//                                        final ShareMessage shareMessage = (ShareMessage) message.getContent();
//                                        System.out.println("消息监听--> shareMessage: " + GsonUtils.GsonString(shareMessage));
                                        myConversation.setMessageContent("【分享消息】");
                                    } else if (message.getContent() instanceof MingPianMessage) {
                                        myConversation.setMessageContent("【名片】");
                                    } else if (message.getContent() instanceof CircleMessage) {
                                        myConversation.setMessageContent("【圈子】");
                                    } else if (message.getContent() instanceof ImageMessage) {
                                        myConversation.setMessageContent("【图片】");
                                    } else if (message.getContent() instanceof FileMessage) {
                                        myConversation.setMessageContent("【文件】");
                                    }
                                    myConversation.setRead(conversation.getReceivedStatus().isRead());
                                    getConversationPosition(myConversation);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void getResumeConversationList() {
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
                            myConversation.setUnreadCount(conversation.getUnreadMessageCount());
                            if (String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)).equals(conversation.getSenderUserId())) {
                                myConversation.setUserId(conversation.getTargetId());
                            } else {
                                myConversation.setUserId(conversation.getSenderUserId());
                            }
                            myConversation.setUserName(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getUser_name());
                            myConversation.setHeaderPic(ConversationDao.getDao(getContext()).selectById(myConversation.getUserId()).getHeader_pic());
                            myConversation.setMessageTime(conversation.getSentTime());
                            RongIMClient.getInstance().getMessage(conversation.getLatestMessageId(), new RongIMClient.ResultCallback<Message>() {
                                @Override
                                public void onSuccess(Message message) {
                                    if (message.getContent() instanceof TextMessage) {
                                        final TextMessage textMessage = (TextMessage) message.getContent();
                                        myConversation.setMessageContent(textMessage.getContent());
                                    } else if (message.getContent() instanceof ShareMessage) {
                                        myConversation.setMessageContent("【分享消息】");
                                    } else if (message.getContent() instanceof MingPianMessage) {
                                        myConversation.setMessageContent("【名片】");
                                    } else if (message.getContent() instanceof CircleMessage) {
                                        myConversation.setMessageContent("【圈子】");
                                    } else if (message.getContent() instanceof ImageMessage) {
                                        myConversation.setMessageContent("【图片】");
                                    } else if (message.getContent() instanceof FileMessage) {
                                        myConversation.setMessageContent("【文件】");
                                    }
                                    myConversation.setRead(conversation.getReceivedStatus().isRead());
                                    getResumeConversationPosition(myConversation);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void getConversationPosition(MyConversation myConversation) {
        if (null == myConversation) {
            return;
        }
        int position = -1;
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserId().equals(myConversation.getUserId())) {
                    position = i;
                }
            }
        }
//        System.out.println("position: " + position + " , user_id: " + myConversation.getUserId() + " , count: " + myConversation.getUnreadCount());
        if (position > -1) {
            android.os.Message message = myHandler.obtainMessage(2);
            Bundle bundle = new Bundle();
            bundle.putSerializable("conversation", myConversation);
            bundle.putInt("position", position);
            message.obj = bundle;
            message.sendToTarget();
        } else if (position == -1 && myConversation.getUnreadCount() > 0) {
            android.os.Message message = myHandler.obtainMessage(3);
            Bundle bundle = new Bundle();
            bundle.putSerializable("conversation", myConversation);
            message.obj = bundle;
            message.sendToTarget();
        }
    }

    private void getResumeConversationPosition(MyConversation myConversation) {
        if (null == myConversation) {
            return;
        }
        int position = -1;
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUserId().equals(myConversation.getUserId())) {
                    position = i;
                }
            }
        }
        if (position == -1) {
            android.os.Message message = myHandler.obtainMessage(3);
            Bundle bundle = new Bundle();
            bundle.putSerializable("conversation", myConversation);
            message.obj = bundle;
            message.sendToTarget();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(getContext()).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());
//        System.out.println("userId:" + list.get(position).getUserId() + " , userName: " + (TextUtils.isEmpty(list.get(position).getUserName()) ? "聊天界面" : list.get(position).getUserName()));
        try {
            RongIM.getInstance().startPrivateChat(getActivity(), list.get(position).getUserId(),
                    TextUtils.isEmpty(list.get(position).getUserName()) ? "聊天界面" : list.get(position).getUserName());

//            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), list.get(position).getUser_name());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "无法进入会话界面", Toast.LENGTH_SHORT).show();
        }
    }
}
