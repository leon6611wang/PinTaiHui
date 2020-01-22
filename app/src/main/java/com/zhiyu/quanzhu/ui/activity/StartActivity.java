package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends BaseActivity {

    private ImageView mImageView;
    private TextView skipTextView;
    private int timeCount=6;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<StartActivity> startActivityWeakReference;
        public MyHandler(StartActivity activity){
            startActivityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StartActivity activity=startActivityWeakReference.get();
            switch (msg.what){
                case 0:
                    activity.skipTextView.setText(activity.timeCount+"秒跳过");
                    if(activity.timeCount==0){
                        activity.goToHome();
                    }

                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        initTimerTask();
        timer.schedule(task,0,1000);
    }

    private void initViews(){
        mImageView=findViewById(R.id.mImageView);
        Glide.with(this).load("https://c-ssl.duitang.com/uploads/item/201804/02/20180402200756_A4aN3.thumb.700_0.jpeg").into(mImageView);
        skipTextView=findViewById(R.id.skipTextView);
        skipTextView.setText(timeCount+"秒跳过");
    }

    private Timer timer;
    private TimerTask task;
    private void initTimerTask(){
        timer=new Timer();
        task=new TimerTask() {
            @Override
            public void run() {
                timeCount--;
                Message message=myHandler.obtainMessage(0);
                message.sendToTarget();
            }
        };
    }

    private void goToHome(){
        Intent intent=new Intent(StartActivity.this,HomeActivity2.class);
        startActivity(intent);
        finish();
    }
}
