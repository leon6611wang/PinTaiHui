package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.SystemSettingInfoResult;
import com.zhiyu.quanzhu.ui.dialog.TimeDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageNotificationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, mLayout;
    private TextView titleTextView;
    private TimeDialog startTimeDialog, endTimeDialog;
    private LinearLayout startTimeLayout, endTimeLayout;
    private TextView startTimeTextView, endTimeTextView;
    private SwitchButton messaageSwitchButton, noDisturbSwitchButton;
    private long start_time, end_time;
    private MyHandler myHandler = new MyHandler(this);
    private boolean isMessageFirst = true, isSilenceFirst = true;

    private static class MyHandler extends Handler {
        WeakReference<MessageNotificationActivity> activityWeakReference;

        public MyHandler(MessageNotificationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MessageNotificationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.infoResult.getCode() == 200) {
                        if (activity.infoResult.getData().getMessagestatus() == 1) {
                            activity.messaageSwitchButton.open();
                        } else {
                            activity.messaageSwitchButton.close();
                        }
                        if (activity.infoResult.getData().getSilencestatus() == 1) {
                            activity.noDisturbSwitchButton.open();
                        } else {
                            activity.noDisturbSwitchButton.close();
                        }
                        activity.startTimeTextView.setText(activity.infoResult.getData().getSilence_start());
                        activity.endTimeTextView.setText(activity.infoResult.getData().getSilence_end());
                    }
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notification);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDialogs();
        initViews();
        systemSetInfo();
    }

    private void initDialogs() {
        startTimeDialog = new TimeDialog(this, R.style.dialog, true, new TimeDialog.OnTimeListener() {
            @Override
            public void onTime(String hour, String minute, boolean isNextDay) {
                silence_start = hour + ":" + minute;
                start_time = getTimeStamp(silence_start);
                startTimeTextView.setText(silence_start);
                systemSetting();
            }
        });
        endTimeDialog = new TimeDialog(this, R.style.dialog, false, new TimeDialog.OnTimeListener() {
            @Override
            public void onTime(String hour, String minute, boolean isNextDay) {
                String time = hour + ":" + minute;
                is_next_day = isNextDay ? 1 : 0;
                long one_day = 1000 * 60 * 60 * 24;
                end_time = getTimeStamp(time);
                if (isNextDay) {
                    end_time += one_day;
                }
                if (end_time < start_time) {
                    MessageToast.getInstance(MessageNotificationActivity.this).show("结束时间必须大于开始时间");
                } else {
                    silence_end = (isNextDay ? "次日" : "") + time;
                    endTimeTextView.setText(silence_end);
                }
                systemSetting();
            }
        });
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("新消息通知");
        mLayout = findViewById(R.id.mLayout);
        startTimeLayout = findViewById(R.id.startTimeLayout);
        startTimeLayout.setOnClickListener(this);
        startTimeTextView = findViewById(R.id.startTimeTextView);
        endTimeLayout = findViewById(R.id.endTimeLayout);
        endTimeLayout.setOnClickListener(this);
        endTimeTextView = findViewById(R.id.endTimeTextView);
        messaageSwitchButton = findViewById(R.id.messaageSwitchButton);
        messaageSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                mLayout.setVisibility(isOpen ? View.VISIBLE : View.INVISIBLE);
                messagestatus = isOpen ? 1 : 0;
                if (!isMessageFirst)
                    systemSetting();
                isMessageFirst = false;
            }
        });
        noDisturbSwitchButton = findViewById(R.id.noDisturbSwitchButton);
        noDisturbSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                silencestatus = isOpen ? 1 : 0;
                if (!isSilenceFirst)
                    systemSetting();
                isSilenceFirst = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.startTimeLayout:
                startTimeDialog.show();
                break;
            case R.id.endTimeLayout:
                endTimeDialog.show();
                break;
        }
    }

    private long getTimeStamp(String time) {
        long timestamp = 0;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            //设置要读取的时间字符串格式
            Date date = format.parse(time);
            //转换为Date类
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    private SystemSettingInfoResult infoResult;

    private void systemSetInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SYSTEM_SET_INFO);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("设置详情: " + result);
                infoResult = GsonUtils.GsonToBean(result, SystemSettingInfoResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private int silencestatus, messagestatus;
    private String silence_start, silence_end;
    private int is_next_day;
    private BaseResult baseResult;

    private void systemSetting() {
        if (!StringUtils.isNullOrEmpty(silence_end) && silence_end.contains("次日")) {
            silence_end = silence_end.replace("次日", "");
        }
        System.out.println("设置: silencestatus: " + silencestatus + " , messagestatus: " + messagestatus + " , is_next_day: " + is_next_day + " , silence_end: " + silence_end);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SET_SYSTEM_SETTING);
        params.addBodyParameter("silencestatus", String.valueOf(silencestatus));
        params.addBodyParameter("messagestatus", String.valueOf(messagestatus));
        params.addBodyParameter("silence_start", silence_start);
        params.addBodyParameter("silence_end", silence_end);
        params.addBodyParameter("is_next_day", String.valueOf(is_next_day));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("设置: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("设置: " + ex.toString());
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
