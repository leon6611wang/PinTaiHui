package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class RefundTimeDownTextView extends AppCompatTextView {

    private MyHandler myHandler = new MyHandler(this);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd天HH小时mm分ss秒");

    private static class MyHandler extends Handler {
        WeakReference<RefundTimeDownTextView> textViewWeakReference;

        public MyHandler(RefundTimeDownTextView textView) {
            textViewWeakReference = new WeakReference<>(textView);
        }

        @Override
        public void handleMessage(Message msg) {
            RefundTimeDownTextView textView = textViewWeakReference.get();
            switch (msg.what) {
                case 0:
                    textView.setText("剩余" + textView.timeDown());
                    break;
                case 1:
                    if (null != textView.onTimeDownListener) {
                        textView.onTimeDownListener.onTImeDownFinish();
                    }
                    break;
            }
        }
    }

    private Timer timer;
    private TimerTask task;

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    if (mTimeLong > 0) {
                        mTimeLong -= 1000;
                        if (mTimeLong < 0)
                            mTimeLong = 0;
                        Message message = myHandler.obtainMessage(0);
                        message.sendToTarget();
                    } else {
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                        stopTask();
                    }

                }
            };
        }
        startTask();
    }

    private void stopTask() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
    }

    private void startTask() {
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
    }

    private long mTimeLong;

    public void setOverTime(long over_time, OnTimeDownListener listener) {
        this.onTimeDownListener = listener;
        long time = System.currentTimeMillis();
        mTimeLong = over_time * 1000 - time;
        initTimerTask();
    }

    private String timeDown() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTimeLong);
        time = sdf.format(calendar.getTime());
        return time;
    }

    public RefundTimeDownTextView(Context context) {
        super(context);
    }

    public RefundTimeDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnTimeDownListener onTimeDownListener;

    public interface OnTimeDownListener {
        void onTImeDownFinish();
    }
}
