package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的-设置-账号与安全-修改手机号1
 */
public class EditPhoneNum1Activity extends BaseActivity implements View.OnClickListener {
    private TextView titleTextView, weihaoTextView, yanzhengmaButtonTextView, xiayibuButtonTextView;
    private EditText yanzhengmaEditView;
    private LinearLayout backLayout;
    private Timer timer;
    private TimerTask task;
    private final int COUNT = 10;
    private int timeCount = COUNT;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<EditPhoneNum1Activity> editPwd1ActivityWeakReference;

        public MyHandler(EditPhoneNum1Activity activity) {
            editPwd1ActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EditPhoneNum1Activity activity = editPwd1ActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (activity.timeCount > 0) {
                        activity.yanzhengmaButtonTextView.setClickable(false);
                        activity.yanzhengmaButtonTextView.setText(activity.timeCount + " s");
                    } else {
                        activity.yanzhengmaButtonTextView.setClickable(true);
                        activity.yanzhengmaButtonTextView.setText("获取短信验证码");
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phonenum1);
        initViews();
        initTimerTask();
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("更改手机号码");
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        yanzhengmaButtonTextView = findViewById(R.id.yanzhengmaButtonTextView);
        yanzhengmaButtonTextView.setOnClickListener(this);
        xiayibuButtonTextView = findViewById(R.id.xiayibuButtonTextView);
        xiayibuButtonTextView.setOnClickListener(this);
        weihaoTextView = findViewById(R.id.weihaoTextView);
        yanzhengmaEditView = findViewById(R.id.yanzhengmaEditView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.yanzhengmaButtonTextView:
                timeCount = COUNT;
                Field field;
                try {
                    field = TimerTask.class.getDeclaredField("state");
                    field.setAccessible(true);
                    field.set(task, 0);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timer.schedule(task, 0, 1000);
                break;
            case R.id.xiayibuButtonTextView:

                break;
        }
    }

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    timeCount--;
                    if (timeCount >= 0) {
                        Message message = myHandler.obtainMessage(0);
                        message.sendToTarget();
                    }
                }
            };
        }
    }
}
