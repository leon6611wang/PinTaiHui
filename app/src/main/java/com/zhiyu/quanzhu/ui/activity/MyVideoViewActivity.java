package com.zhiyu.quanzhu.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.MyVideoView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class MyVideoViewActivity extends BaseActivity implements MyVideoView.OnFullScreenListener{
    private String video_url_v = "http://flv3.bn.netease.com/f5e519707cd7c443d557034ab3f21769de46a282499100d732ea34f2552e2f9fd679123e6e45c6e97831d8a2e84e04646a69a6fa1d8d6db92ffa1d8adf4d58c120ca298d2e74b1dd21a29585202c39ad8d165cb5a181b53ea475fc8ebd6fac7dfe473bca839ffb9d103126bef3b7e06c4ec4db176c62f5d4.mp4";
    private String video_url_h = "http://flv3.bn.netease.com/f5e519707cd7c443a7865d94a5c5b14fe0beb2a03212e2768bcfc8bdf3add63367024b9bb991d04a5c497ef49863f0d53f605ffaf338a29144656757120a798d27557cca77954a5ab02ad24c730f0438d5fad397ad6f368d16fa9d5387c888dd32466333040da0939332d494a2e9a25974f45db7eb4efabb.mp4";
    private MyVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvideoview);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        mVideoView = findViewById(R.id.mVideoView);
        mVideoView.initVideo(null,this);
        mVideoView.setOnFullScreenListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.destory();
    }

    @Override
    public void onFullScreen(int width, int height, float ratio,boolean isFullScreen) {
        if(isFullScreen){
            if(ratio>1.0f){
                landscape();
            }else{
                portrait();
            }
        }else{
            portrait();
        }

    }

    /**
     * 强制横屏
     */
    private void landscape() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 强制竖屏
     */
    private void portrait() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
