package com.leon.shehuibang.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.leon.shehuibang.R;
import com.leon.shehuibang.base.BaseActivity;
import com.leon.shehuibang.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends BaseActivity {
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<InitActivity> weakReference;
        public MyHandler(InitActivity activity){
            weakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            InitActivity activity=weakReference.get();
            switch (msg.what){
                case 1:
                    activity.page();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initTask();
        timer.schedule(task,500);
    }

    private Timer timer;
    private TimerTask task;
    private void initTask(){
        if(null!=timer){
            timer.cancel();
            timer=null;
        }
        if(null!=task){
            task.cancel();
            task=null;
        }
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();
                finish();
            }
        };
    }

    private void page(){
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
