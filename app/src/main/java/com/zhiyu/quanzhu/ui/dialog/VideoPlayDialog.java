package com.zhiyu.quanzhu.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 视频播放
 */

public class VideoPlayDialog extends Dialog {
    private Context context;
    //    private String url = "https://flv2.bn.netease.com/videolib1/1811/26/OqJAZ893T/HD/OqJAZ893T-mobile.mp4";
    private String url = "http://sl.video.ali.n0808.com/r_b1d9c99bc5b575cb8efa8a86f20aff99324b898a?sign=2776f47559aaec8969e84f2022330b64&t=bbb8b186&auth_key=1574779331-0-0-fa843cbffb363d108c947e951b508fe8";
    private VideoView mVideoView;
    private int videoDuration, videoCurrentPosition;
    private Activity activity;

    public VideoPlayDialog(@NonNull Context context,Activity aty, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.activity=aty;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_videoplay);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        params.height = ScreentUtils.getInstance().getScreenHeight(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);
        //设置dialog沉浸式效果
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        mVideoView = findViewById(R.id.mVideoView);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(activity))
            return;
//        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.setVideoPath(url);
        mVideoView.setMediaController(new MediaController(activity));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);//设置播放速度
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mVideoView.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
