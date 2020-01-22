package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.VideoPlayer;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;

public class Test2Activity extends BaseActivity {
    private LinearLayout rootLayout;
    private LinearLayout.LayoutParams ll;
    VideoPlayer mVideoPlayer, mVideoPlayer2;
    String url = "https://flv3.bn.netease.com/2840ec043817f032fd7ef5e61e0006ba411ac47150d9e131156608247018e86cabb2d3c6e454fb7246248e8a60df7323184eff441b18d0322da8dec07a2828dc172f6a218e9d224c58989eb8ebae373f2353b54bfba928b4e29ed9c948ab9d798c7227aaf01e7408cf47e36df1f9f10cdc3967a11e4ce9c1.mp4";
    String url2 = "https://flv3.bn.netease.com/2840ec043817f032e1351f56e983bfb844ee91879d77aedaf82f4368d9a138f97a0b7276a2c622905682ecdff9dde6ed84ed766ad55f4cfd3ad9337b415486fb509b15394caaf1b15ad40df179f4de26c8db58e38526787b09cfa173005cb40ae4db472671da3f382aaa3b220afb6da2ed2d71545a89e1bc.mp4";
    String url3 = "http://img.pintaihui.cn/cards/2020/01/15/1579090673297.mp4";
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<Test2Activity> activityWeakReference;

        public MyHandler(Test2Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Test2Activity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    System.out.println("设置数据源");
                    activity.initVideo2();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);
        rootLayout = findViewById(R.id.rootLayout);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mVideoPlayer = findViewById(R.id.mVideoPlayer);
        mVideoPlayer.setVideoUrl(url);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initVideo2() {
        int video_width, video_height;
        int screenWidth, dp_15;
        float ratio = 1.778f;
        dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        video_width = screenWidth - dp_15 * 2;
        video_height = Math.round(video_width / ratio);
        ll = new LinearLayout.LayoutParams(video_width, video_height);
        mVideoPlayer2 = new VideoPlayer(this, video_width, video_height);
        mVideoPlayer2.setLayoutParams(ll);
        rootLayout.addView(mVideoPlayer2);
        mVideoPlayer2.setVideoUrl(url2);
    }

}
