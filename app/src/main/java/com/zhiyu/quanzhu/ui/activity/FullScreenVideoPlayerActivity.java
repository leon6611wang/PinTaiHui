package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;

import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 视频全屏播放
 */
public class FullScreenVideoPlayerActivity extends BaseActivity {
    private String videoUrl = "https://flv3.bn.netease.com/1761ad371d868032949671524177c4dd036f0c625430e58728220c8724a88929b4e97d70d54de730277dbf4355df301b10947037f3b170657da5edeaca2a141a394c4adf97a95c1847a08450c04f10e7396efb1d8b3ba5213c64f28a8b71f0673de3edbe1b16e83c9900322531e28431e886942a4a0113b0.mp4";
    private VideoPlayerTrackView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video_player);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
    }

    private void initViews() {
        videoPlayer = findViewById(R.id.videoPlayer);
        videoPlayer.setDataSource(videoUrl, "测试-窄视频");
    }
}
