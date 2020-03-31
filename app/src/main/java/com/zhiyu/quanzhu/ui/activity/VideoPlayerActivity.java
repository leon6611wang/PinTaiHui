package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;

import com.leon.myvideoplaerlibrary.manager.VideoPlayerManager;
import com.leon.myvideoplaerlibrary.manager.VideoWindowManager;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class VideoPlayerActivity extends BaseActivity {
    private VideoPlayerTrackView videoPlayer;
    private String url="https://flv3.bn.netease.com/02ee5562a7ab0cdf3712f85dfea9d4618056095af873aedcbe7dcc9adedd8e7b6eca99b0143491737ecd8ff4212b01a8bbccb989fb2986505a04e7cf8a13068dd4cd031deb434a74fa418183ad2b667c297d9feb717674c4c6fb7260e6aeac34c35bff072ff50f7fe7c81c2336ee8a8ee41ea322ec960635.mp4";
    private String title="来源:网易新闻";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ScreentUtils.getInstance().setStatusBarLightMode(this,true);
        initViews();
    }

    private void initViews(){
        videoPlayer=findViewById(R.id.videoPlayer);
        videoPlayer.startPlayVideo(url,title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.getInstance().onPause();
    }

    @Override
    public void onBackPressed() {
        //尝试弹射返回
        if(VideoPlayerManager.getInstance().isBackPressed()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().onDestroy();
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy();
    }
}
