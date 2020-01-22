package com.zhiyu.quanzhu.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import java.io.File;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoPlayActivity extends BaseActivity {
    private VideoView videoView;
    private TextView tvProgress;
    private TextView tvDownloadSpeed;
    private String urlString = "http://vd2.bdstatic.com/mda-jkv4qrxm0dmm3vz4/sc/mda-jkv4qrxm0dmm3vz4.mp4";
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        File file = Environment.getExternalStorageDirectory();
        File file1 = new File(file, "aa.mp4");
        //判断Vitamio是否已经初始化【并且初始化】
        if (Vitamio.initialize(this)) {
            videoView = (VideoView) findViewById(R.id.vitamio);
            videoView.setVideoURI(Uri.parse(urlString));//播放网络视频
//            videoView.setVideoPath(file1.getPath());//播放本地视频
            controller = new MediaController(this);//视频控制器
            videoView.setMediaController(controller);//关联控制器
            videoView.start();
        }
        initView();
        setListener();

    }

    private void setListener() {
        //设置缓冲进度的监听
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                tvProgress.setText(percent + "%");
            }
        });
        //设置缓冲下载监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲时的视图变化
                        tvProgress.setVisibility(View.VISIBLE);
                        tvDownloadSpeed.setVisibility(View.VISIBLE);
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲好后的视图变化（可播放）
                        tvProgress.setVisibility(View.GONE);
                        tvDownloadSpeed.setVisibility(View.GONE);
                        mp.start();
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        tvDownloadSpeed.setText("当前网速:" + extra + "kb/s");//下载是速度
                        break;
                }
                return true;
            }
        });
        //设置准备监听（判断一准备好播放）
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setBufferSize(512 * 1025);//设置缓冲区大小
            }
        });
    }

    private void initView() {
        tvProgress = (TextView) findViewById(R.id.tv_progress_home);
        tvDownloadSpeed = (TextView) findViewById(R.id.tv_download_home);
    }


}
