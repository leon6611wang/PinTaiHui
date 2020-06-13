package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.leon.chic.dao.CardDao;
import com.leon.chic.utils.LogUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.FriendSettingResult;
import com.zhiyu.quanzhu.ui.dialog.EditNoteNameDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 私聊设置
 */
public class ConversationPrivateSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private CircleImageView avatarImageView;
    private String targetId;
    private YNDialog ynDialog;
    private EditNoteNameDialog editNoteNameDialog;
    private LinearLayout noteNameLayout, clearRecordLayout, complaintLayout;
    private TextView noteNameTextView, nameTextView, nickNameTextView, deleteFriendTextView;
    private SwitchButton silenceButton, topButton, blackButton;
    private String noteName;
    private int silence, top, black;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ConversationPrivateSettingActivity> weakReference;

        public MyHandler(ConversationPrivateSettingActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ConversationPrivateSettingActivity activity = weakReference.get();
            switch (msg.what) {
                case 0:
                    if (200 == activity.friendSettingResult.getCode()) {
                        activity.initFriendSettingData();
                    } else {
                        MessageToast.getInstance(activity).show(activity.friendSettingResult.getMsg());
                    }
                    break;
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.deleteSuccess();
                    }
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;
            }
        }
    }

    private void deleteSuccess() {
        CardDao.getInstance().deleteCard(Integer.parseInt(targetId), BaseApplication.getInstance());
        removeRongIMMessage(targetId);
        Intent intent = new Intent();
        intent.putExtra("isdelete", 1);
        this.setResult(1003, intent);
        finish();
    }

    /**
     * 删除聊天item的方法
     */
    public void removeRongIMMessage(String targetId) {
        RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("删除成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                System.out.println("删除失败");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_private_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        targetId = getIntent().getStringExtra("targetId");
        initViews();
        initDialog();
        friendSettingInfo();
    }

    private boolean silenceFirst = true, topFirst = true, blackFirst = true;
    private boolean updateNoteName = true;

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("通知设置");
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        nickNameTextView = findViewById(R.id.nickNameTextView);
        noteNameLayout = findViewById(R.id.noteNameLayout);
        noteNameLayout.setOnClickListener(this);
        noteNameTextView = findViewById(R.id.noteNameTextView);
        deleteFriendTextView = findViewById(R.id.deleteFriendTextView);
        deleteFriendTextView.setOnClickListener(this);
        silenceButton = findViewById(R.id.silenceButton);
        silenceButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (!silenceFirst) {
                    silence = isOpen ? 1 : 0;
                    friendSetting();
                    setIMSilence();
                } else {
                    silenceFirst = false;
                }

            }
        });
        topButton = findViewById(R.id.topButton);
        topButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (!topFirst) {
                    top = isOpen ? 1 : 0;
                    friendSetting();
                    topConversation(isOpen);
                } else {
                    topFirst = false;
                }

            }
        });
        blackButton = findViewById(R.id.blackButton);
        blackButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (!blackFirst) {
                    black = isOpen ? 1 : 0;
                    friendSetting();
                } else {
                    blackFirst = false;
                }

            }
        });
        clearRecordLayout = findViewById(R.id.clearRecordLayout);
        clearRecordLayout.setOnClickListener(this);
        complaintLayout = findViewById(R.id.complaintLayout);
        complaintLayout.setOnClickListener(this);

    }

    private void initFriendSettingData() {
        Glide.with(this).load(friendSettingResult.getData().getAvatar()).error(R.drawable.image_error).into(avatarImageView);
        if (!StringUtils.isNullOrEmpty(friendSettingResult.getData().getTruename()))
            nameTextView.setText(friendSettingResult.getData().getTruename());
        if (!StringUtils.isNullOrEmpty(friendSettingResult.getData().getUsername()))
            nickNameTextView.setText(friendSettingResult.getData().getUsername());
        if (!StringUtils.isNullOrEmpty(friendSettingResult.getData().getNotename()))
            noteNameTextView.setText(friendSettingResult.getData().getNotename());
        switch (silence) {
            case 0:
                silenceButton.close();
                break;
            case 1:
                silenceButton.open();
                break;
        }
        switch (top) {
            case 0:
                topButton.close();
                break;
            case 1:
                topButton.open();
                break;
        }
        switch (black) {
            case 0:
                blackButton.close();
                break;
            case 1:
                blackButton.open();
                break;
        }
    }

    private void initDialog() {
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                deleteCard();
            }
        });
        editNoteNameDialog = new EditNoteNameDialog(this, R.style.dialog, new EditNoteNameDialog.EditNoteNameListener() {
            @Override
            public void editNoteName(String note_name) {
                noteName = note_name;
                noteNameTextView.setText(noteName);
                updateNoteName = true;
                friendSetting();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.noteNameLayout:
                editNoteNameDialog.show();
                break;
            case R.id.deleteFriendTextView:
                ynDialog.show();
                ynDialog.setTitle("确定删除该好友？");
                break;
            case R.id.clearRecordLayout:
                clearMessage();
                break;
            case R.id.complaintLayout:
                Intent intent = new Intent(ConversationPrivateSettingActivity.this, ComplaintActivity.class);
                intent.putExtra("report_id", Integer.parseInt(targetId));
                intent.putExtra("module_type", "user");
                startActivity(intent);
                break;
        }
    }

    private void setIMSilence() {
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                final int value = conversationNotificationStatus.getValue();
                String title;
                final Conversation.ConversationNotificationStatus conversationNotificationStatus1;
//                if (value == 1) {
//                    conversationNotificationStatus1 = conversationNotificationStatus.setValue(0);
//                    title = "免打扰";
//                } else {
//                    conversationNotificationStatus1 = conversationNotificationStatus.setValue(1);
//                    title = "取消免打扰";
//                }
//                System.out.println(title);
                if (silence == 1) {
                    conversationNotificationStatus1 = conversationNotificationStatus.setValue(0);
                } else {
                    conversationNotificationStatus1 = conversationNotificationStatus.setValue(1);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void clearMessage() {
        RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("清除成功");
                MessageToast.getInstance(ConversationPrivateSettingActivity.this).show("聊天记录清除成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                System.out.println("清除失败");
            }
        });
    }

    private void topConversation(boolean isTop) {
        RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, targetId, isTop, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("会话是否置顶: " + aBoolean);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void deleteCard() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_CARD);
        params.addBodyParameter("tuid", targetId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("deleteCard: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private FriendSettingResult friendSettingResult;

    private void friendSettingInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FRIEND_SETTING_INFO);
        params.addBodyParameter("tuid", targetId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("friendSettingInfo: " + result);
                friendSettingResult = GsonUtils.GsonToBean(result, FriendSettingResult.class);
                if (200 == friendSettingResult.getCode()) {
                    silence = friendSettingResult.getData().getMessagesilence();
                    top = friendSettingResult.getData().getMessagetop();
                    black = friendSettingResult.getData().getIs_black();
                }
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private BaseResult baseResult;

    private void friendSetting() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FRIEND_SETTING);
        params.addBodyParameter("tuid", targetId);
        params.addBodyParameter("notename", noteName);
        params.addBodyParameter("messagetop", String.valueOf(top));
        params.addBodyParameter("messagesilence", String.valueOf(silence));
        params.addBodyParameter("is_black", String.valueOf(black));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("setting: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    System.out.println("updateNoteName: " + updateNoteName);
                    if (updateNoteName) {
                        CardDao.getInstance().updateCardNoteName(Integer.parseInt(targetId), noteName, BaseApplication.getInstance());
                        updateNoteName = false;
                    }
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
