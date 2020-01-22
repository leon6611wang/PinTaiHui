package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.widget.CircleProgressView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 动态全屏模式的视频播放
 */
public class DongTaiVideoPlayerActivity extends BaseActivity implements OnClickListener,
        OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
        OnVideoSizeChangedListener, SurfaceHolder.Callback, OnErrorListener,
        OnInfoListener, OnSeekCompleteListener {
    //    private String path = "https://flv3.bn.netease.com/c5d39796715c6dff4e5e27c980670ca160b023b84bf908607359a5c23058d629bf6dbf8618b2407867fef957c8940bae072928028b1b657c82fd9c386c96d366b124cd66389502d895ad028249955015cc4e1714ca89b70ede2767820ea121b2b70057dd720ee4fe31eb9cc7e328273e44efc0d2e194e76d.mp4";
    private String path ="https://flv3.bn.netease.com/711b98bb9cbd792fcbb6d988e986bb4b87e4e4d40ede334708fc08e1213af40d0bdba349e432618006b5ea499837c39fa60e6d7bf32f703437856f0e66fe64e639d8ace0d86d311cb21a82f567be242c6fe757c6653a686acdf1ed8a628d3a815a4ee8fc7c677ea401d9bd83a5ff49821ce6cb05b816d279.mp4";
    private ImageView rightPlayImageView;
    private VideoView videoView;
    private boolean isPlay;
    private SeekBar seekBar;
    private upDateSeekBar update;
    private TextView playTimeTextView, durationTimeTextView;
    private AudioManager mAudioManager;
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    private GestureDetector mGestureDetector;
    private boolean isFinish;
    private long pauseSize;
    private CircleProgressView huanchongView;
    private TextView netSpeedTextView;
    private ImageView centerPlayImageView;
    private LinearLayout rightPlayLayout;
    private ImageView coverImageView;

    /**
     * 音量/亮度调节
     */
    private LinearLayout volumeBrightnessLayout;
    private ImageView volumeBrightnessImageView;
    private TextView volumeBrightnessTextView;

    /**
     * 视频及封面图片尺寸参数
     *
     * @param savedInstanceState
     */
    private FrameLayout.LayoutParams videoLayoutParams;
    private int videoWidth, videoHeight;
    private float videoRatio = 1.7778f;
    private int screenWidth, screenHeight;
    private String coverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(getApplication());
        // 检测Vitamio是否解压解码包
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        // 定义布局
        setContentView(R.layout.activity_dongtai_videoplayer);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
//        coverUrl=getIntent().getStringExtra("coverUrl");
        coverUrl="https://c-ssl.duitang.com/uploads/blog/201401/09/20140109113824_Vu5Mi.thumb.700_0.jpeg";
        // 初始化布局
        initLayoutParams();
        initView();
        play();

    }

    private void initLayoutParams() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(this);
        if (videoWidth == 0f || videoHeight == 0f) {
            videoWidth = screenWidth;
            videoHeight = screenHeight;
        }
        videoLayoutParams = new FrameLayout.LayoutParams(videoWidth, videoHeight);
    }


    private void initView() {
        huanchongView = findViewById(R.id.huanchongView);
        netSpeedTextView = findViewById(R.id.netSpeedTextView);
        centerPlayImageView = findViewById(R.id.centerPlayImageView);
        centerPlayImageView.setOnClickListener(this);
        rightPlayLayout = findViewById(R.id.rightPlayLayout);
        rightPlayLayout.setOnClickListener(this);
        volumeBrightnessLayout = findViewById(R.id.volumeBrightnessLayout);
        volumeBrightnessImageView = findViewById(R.id.volumeBrightnessImageView);
        volumeBrightnessTextView = findViewById(R.id.volumeBrightnessTextView);
        volumeBrightnessLayout.getBackground().setAlpha(100);
        // 视频播放进度条
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        // 视频播放框
        videoView = findViewById(R.id.videoView);
        coverImageView = findViewById(R.id.coverImageView);
        videoView.setLayoutParams(videoLayoutParams);
        coverImageView.setLayoutParams(videoLayoutParams);
        Glide.with(this).load(coverUrl).into(coverImageView);
//        // 视频已经播放的时间
        playTimeTextView = (TextView) findViewById(R.id.playTimeTextView);
//        // 视频播放总时间
        durationTimeTextView = (TextView) findViewById(R.id.durationTimeTextView);
//        // 播放按钮，并设置监听
        rightPlayImageView = findViewById(R.id.rightPlayImageView);
        // 手势操作类，并设置手势监听
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        // 获取系统服务
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取系统的最大音量
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        portrait();
        // 更新seekbar,即视频播放进度监听
        setListener();
        // 更新seekbar时,根据视频播放进度跟新时间等数据
        update = new upDateSeekBar();
        new Thread(update).start();
    }

    private class MyGestureListener extends SimpleOnGestureListener {
        @Override
        // 当用户在屏幕上"滚动"时触发该方法
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // 得到x，y坐标当前值，相对于当前view
            float mOldX = e1.getX(), mOldY = e1.getY();
            // getRawY()是表示相对于屏幕左上角的y坐标值
            int y = (int) e2.getRawY();
            // 获得屏幕的宽高
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            // 右边滑动
            if (mOldX > windowWidth / 2)
                onVolumeSlide((mOldY - y) / windowHeight);
                // 左边滑动
            else if (mOldX < windowWidth / 2)
                onBrightnessSlide((mOldY - y) / windowHeight);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        if (index == 0) {
            volumeBrightnessImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_voice_no));
        } else {
            volumeBrightnessImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_voice));
        }
        volumeBrightnessTextView.setText(String.valueOf(index));

        System.out.println("声音: " + index);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }

        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);
        volumeBrightnessImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_light));
        volumeBrightnessTextView.setText((int) (lpa.screenBrightness * 100) + "%");
        System.out.println("亮度: " + lpa.screenBrightness);
    }

    //播放视频
    private void play() {
        isPlay = true;
        try {
            videoView.setVideoURI(Uri.parse(path));
            videoView.setOnCompletionListener(this);
            videoView.setOnBufferingUpdateListener(this);
            videoView.setOnErrorListener(this);
            videoView.setOnInfoListener(this);
            videoView.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.i("hck", "PlayActivity " + e.toString());
        }
    }

    class upDateSeekBar implements Runnable {
        @Override
        public void run() {
            if (!isFinish) {
                mHandler.sendMessage(Message.obtain());
                mHandler.postDelayed(update, 1000);
            }

        }
    }

    //通过用户指定的播放进度更新时间等信息
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (videoView == null) {
                return;
            }
            playTimeTextView.setText(StringUtils.generateTime(videoView
                    .getCurrentPosition()));
            if (videoView != null) {
                seekBar(videoView.getCurrentPosition());
            }

        }

        ;
    };

    private void seekBar(long size) {
        if (videoView.isPlaying()) {
            long mMax = videoView.getDuration();
            int sMax = seekBar.getMax();
            seekBar.setProgress((int) (size * sMax / mMax));
        }
    }

    private void setListener() {
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = (int) (seekBar.getProgress()
                        * videoView.getDuration() / seekBar.getMax());
                videoView.seekTo(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlay = false;
                isFirstPlay = false;
                videoView.pause();
                rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_pause_small));
                centerPlayImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 处理手势结束
                endGesture();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
    }


    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightPlayLayout:
                if (isPlay) {
                    videoView.pause();
                    rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_pause_small));
                    centerPlayImageView.setVisibility(View.VISIBLE);
                    isPlay = false;
                    seekBar.setEnabled(false);
                } else {
                    rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_play_small));
                    videoView.start();
                    centerPlayImageView.setVisibility(View.GONE);
                    isPlay = true;
                    seekBar.setEnabled(true);
                }
                break;
            case R.id.centerPlayImageView:
                if (isPlay) {
                    videoView.pause();
                    rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_pause_small));
                    centerPlayImageView.setVisibility(View.VISIBLE);
                    isPlay = false;
                    seekBar.setEnabled(false);
                } else {
                    rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_play_small));
                    centerPlayImageView.setVisibility(View.GONE);
                    videoView.start();
                    isPlay = true;
                    seekBar.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("hck", "surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        if (path.startsWith("http")) {
            videoView.setBufferSize(1024 * 1000);
        } else {
            videoView.setBufferSize(0);
        }
        durationTimeTextView.setText(StringUtils.generateTime(videoView.getDuration()));
        if (pauseSize > 0) {
            videoView.seekTo(pauseSize);
        }
        pauseSize = 0;
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        System.out.println("onCompletion");
//        videoView.stopPlayback();
        centerPlayImageView.setVisibility(View.VISIBLE);
        rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_pause_small));
        isPlay = false;
    }

    private boolean isFirstPlay = true;

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
        huanchongView.setProgress(videoView.getBufferPercentage());
        huanchongView.setText(huanchongView.getProgress() + "%");
        if (videoView.getBufferPercentage() < 99) {
            huanchongView.setVisibility(View.VISIBLE);
        } else {
            huanchongView.setVisibility(View.GONE);
        }

        if (videoView.getBufferPercentage() >= 99 && !isFirstPlay) {
            videoView.start();
            isPlay = true;
            centerPlayImageView.setVisibility(View.GONE);
            rightPlayImageView.setImageDrawable(getResources().getDrawable(R.mipmap.video_play_small));
        }
        System.out.println("视频缓冲进度: " + videoView.getBufferPercentage() + "%");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        videoView.setVideoLayout(3, 0);
    }

    @Override
    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
        Log.i("hck", "error :" + arg1 + arg2);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        switch (arg1) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (isPlay) {
                    videoView.pause();
                    isPlay = false;
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                videoView.start();
                isPlay=true;
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                netSpeedTextView.setText("当前网速: " + arg2 + "kb/s");
                System.out.println("当前网速: " + arg2 + "kb/s");
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        isPlay = false;
        isFinish = true;
        System.gc();

    }

    @Override
    public void onSeekComplete(MediaPlayer arg0) {

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
