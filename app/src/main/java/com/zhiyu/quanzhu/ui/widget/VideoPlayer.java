package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.utils.MediaUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.VideoUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义视频播放控制控件
 */
public class VideoPlayer extends FrameLayout implements View.OnClickListener {
    private Context context;
    private int videoplayerWidth, videoplayerHeight;
    private boolean is_fullscreen;
    private int videoWidth, videoHeight;
    private View rootView;
    private FrameLayout videoLayout;
    private ImageView playPauseImageView;
    private MySeekBar seekBar;
    private TextView currentTimeTextView, totalTimeTextView;
    private ImageView videoVoiceImageView;
    private FrameLayout.LayoutParams ll_v, ll_h;
    private FrameLayout.LayoutParams layoutParams;
    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    private TextureMediaplayer textureMediaplayer;
    private boolean is_controller_show = true;
    private LinearLayout controllerLayout;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private Timer timer;
    private TimerTask task;
    private MyHandler myHandler = new MyHandler(this);
    private int duration = 0, totalPosition = 0;
    private int ww, hh;
    private ImageView coverImageView;

    private static class MyHandler extends Handler {
        WeakReference<VideoPlayer> videoPlayerWeakReference;

        public MyHandler(VideoPlayer videoPlayer) {
            videoPlayerWeakReference = new WeakReference<>(videoPlayer);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoPlayer videoPlayer = videoPlayerWeakReference.get();
            switch (msg.what) {
                case 0:
                    int currentPosition = videoPlayer.mMediaPlayer.getCurrentPosition();
                    if (videoPlayer.totalPosition == 0) {
                        videoPlayer.totalPosition = videoPlayer.mMediaPlayer.getDuration();
                    }
                    if (currentPosition == videoPlayer.totalPosition) {
                        videoPlayer.controllerLayout.setVisibility(VISIBLE);
                    } else {
                        videoPlayer.controllerLayout.setVisibility(GONE);
                    }
                    videoPlayer.currentTimeTextView.setText(videoPlayer.sdf.format(currentPosition));
                    int sMax = videoPlayer.seekBar.getMax();
                    videoPlayer.seekBar.setProgress((int) (currentPosition * sMax / videoPlayer.duration));
                    break;
                case 1:
                    videoPlayer.totalTimeTextView.setText(videoPlayer.sdf.format(videoPlayer.duration));
                    if (videoPlayer.is_fullscreen) {
                        if (videoPlayer.ww > videoPlayer.hh) {
                            float video_ratio = (float) videoPlayer.ww / (float) videoPlayer.hh;
                            float screen_ratio = (float) videoPlayer.videoWidth / (float) videoPlayer.videoHeight;
                            if (video_ratio > screen_ratio) {
                                int currentVideoHeight = Math.round(videoPlayer.videoHeight * ((float) videoPlayer.hh / (float) videoPlayer.ww));
                                videoPlayer.ll_h = new FrameLayout.LayoutParams(videoPlayer.videoHeight, currentVideoHeight);
                            } else {
                                int currentVideoWidth = Math.round(videoPlayer.videoWidth * ((float) videoPlayer.ww / (float) videoPlayer.hh));
                                videoPlayer.ll_h = new FrameLayout.LayoutParams(currentVideoWidth, videoPlayer.videoWidth);
                            }
                            videoPlayer.layoutParams = new FrameLayout.LayoutParams(videoPlayer.videoHeight, videoPlayer.videoWidth);
                            videoPlayer.layoutParams.gravity = Gravity.CENTER;
                            videoPlayer.ll_h.gravity = Gravity.CENTER;
                            videoPlayer.controllerLayout.setLayoutParams(videoPlayer.layoutParams);
                            videoPlayer.mTextureView.setLayoutParams(videoPlayer.ll_h);
                            videoPlayer.controllerLayout.setRotation(90);
                            videoPlayer.mTextureView.setRotation(90);
                        } else {
                            float video_ratio = (float) videoPlayer.hh / (float) videoPlayer.ww;
                            float screen_ratio = (float) videoPlayer.videoHeight / (float) videoPlayer.videoWidth;
                            if (video_ratio > screen_ratio) {
                                int currentVideoWidth = Math.round(((float) videoPlayer.ww / (float) videoPlayer.hh) * videoPlayer.videoHeight);
                                videoPlayer.ll_v = new FrameLayout.LayoutParams(currentVideoWidth, videoPlayer.videoHeight);
                            } else {
                                int currentVideoHeight = Math.round(video_ratio * videoPlayer.videoWidth);
                                videoPlayer.ll_v = new FrameLayout.LayoutParams(videoPlayer.videoWidth, currentVideoHeight);
                            }
                            videoPlayer.layoutParams = new FrameLayout.LayoutParams(videoPlayer.videoWidth, videoPlayer.videoHeight);
                            videoPlayer.layoutParams.gravity = Gravity.CENTER;
                            videoPlayer.ll_v.gravity = Gravity.CENTER;
                            videoPlayer.mTextureView.setLayoutParams(videoPlayer.ll_v);
                        }
                    } else {
                        videoPlayer.layoutParams = new FrameLayout.LayoutParams(videoPlayer.videoWidth, videoPlayer.videoHeight);
                        videoPlayer.layoutParams.gravity = Gravity.CENTER;
                        if (videoPlayer.ww > videoPlayer.hh) {
                            float video_ratio = (float) videoPlayer.ww / (float) videoPlayer.hh;
                            float screen_ratio = (float) videoPlayer.videoWidth / (float) videoPlayer.videoHeight;
                            if (video_ratio > screen_ratio) {
                                int currentVideoHeight = Math.round(videoPlayer.videoWidth * ((float) videoPlayer.hh / (float) videoPlayer.ww));
                                videoPlayer.ll_h = new FrameLayout.LayoutParams(videoPlayer.videoWidth, currentVideoHeight);
                            } else {
                                int currentVideoWidth = Math.round(videoPlayer.videoHeight * ((float) videoPlayer.ww / (float) videoPlayer.hh));
                                videoPlayer.ll_h = new FrameLayout.LayoutParams(currentVideoWidth, videoPlayer.videoHeight);
                            }
                            videoPlayer.ll_h.gravity = Gravity.CENTER;
                            videoPlayer.controllerLayout.setLayoutParams(videoPlayer.layoutParams);
                            videoPlayer.mTextureView.setLayoutParams(videoPlayer.ll_h);
                        } else {
                            int currentVideoWidth = Math.round(((float) videoPlayer.ww / (float) videoPlayer.hh) * videoPlayer.videoHeight);
                            videoPlayer.ll_v = new FrameLayout.LayoutParams(currentVideoWidth, videoPlayer.videoHeight);
                            videoPlayer.ll_v.gravity = Gravity.CENTER;
                            videoPlayer.mTextureView.setLayoutParams(videoPlayer.ll_v);
                            videoPlayer.controllerLayout.setLayoutParams(videoPlayer.layoutParams);
                        }
                    }

                    break;
            }
        }
    }


    public VideoPlayer(@NonNull Context context, int video_width, int video_height) {
        super(context);
        videoplayerWidth = video_width;
        videoplayerHeight = video_height;
        if (videoplayerWidth > 0 && videoplayerHeight > 0) {
            videoWidth = videoplayerWidth;
            videoHeight = videoplayerHeight;
        } else {
            videoWidth = ScreentUtils.getInstance().getScreenWidth(context);
            videoHeight = ScreentUtils.getInstance().getScreenHeight(context);
        }

        System.out.println("videoWidth: " + videoWidth + " , videoHeight: " + videoHeight);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_videoplayer, null);
        this.addView(rootView);
        initViews();
        initTimerTask();
    }

    public VideoPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayer);
        if (null != a) {
            videoplayerWidth = (int) a.getDimension(R.styleable.VideoPlayer_videoplayer_width, 0);
            videoplayerHeight = (int) a.getDimension(R.styleable.VideoPlayer_videoplayer_height, 0);
            is_fullscreen = a.getBoolean(R.styleable.VideoPlayer_is_fullscreen, false);
            if (videoplayerWidth > 0 && videoplayerHeight > 0) {
                videoWidth = videoplayerWidth;
                videoHeight = videoplayerHeight;
            } else {
                videoWidth = ScreentUtils.getInstance().getScreenWidth(context);
                videoHeight = ScreentUtils.getInstance().getScreenHeight(context);
            }
        }
        System.out.println("videoWidth: " + videoWidth + " , videoHeight: " + videoHeight);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_videoplayer, null);
        this.addView(rootView);
        initViews();
        initTimerTask();
    }

    private void initViews() {
        videoLayout = rootView.findViewById(R.id.videoLayout);
        mTextureView = rootView.findViewById(R.id.mTextureView);
        mTextureView.setFocusable(false);
        coverImageView = rootView.findViewById(R.id.coverImageView);
        mTextureView.setOnClickListener(this);
        controllerLayout = rootView.findViewById(R.id.controllerLayout);
        playPauseImageView = rootView.findViewById(R.id.playPauseImageView);
        playPauseImageView.setOnClickListener(this);
        currentTimeTextView = rootView.findViewById(R.id.currentTimeTextView);
        seekBar = rootView.findViewById(R.id.seekBar);
        totalTimeTextView = rootView.findViewById(R.id.totalTimeTextView);
        videoVoiceImageView = rootView.findViewById(R.id.videoVoiceImageView);
        mMediaPlayer = BaseApplication.getInstance().getMediaPlayer();
    }

    public void setVideoUrl(String url) {
        mTextureView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        coverImageView.setVisibility(VISIBLE);
        if (url.startsWith("http")) {
            if (url.startsWith("http://video.pintaihui.cn")) {
                Glide.with(context).load(url + "?vframe/jpg/offset/10").into(coverImageView);
            } else {
                coverImageView.setVisibility(GONE);
            }
        } else {
            Bitmap bitmap = MediaUtils.getInstance().getVideoThumb(url);
            if (null != bitmap) {
                coverImageView.setImageBitmap(bitmap);
            }
        }

        VideoUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(url, new VideoUtils.OnCaculateVideoWidthHeightListener() {
            @Override
            public void onVideoWidthHeight(float w, float h, float vt) {
                ww = (int) w;
                hh = (int) h;
                duration = (int) vt;
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }
        });
        textureMediaplayer = new TextureMediaplayer(mTextureView, mMediaPlayer, url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playPauseImageView:
                coverImageView.setVisibility(GONE);
                if (mMediaPlayer.isPlaying()) {
                    textureMediaplayer.pause();
                    controllerLayout.setVisibility(VISIBLE);
                    is_controller_show = true;
                    playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_play));
                } else {
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
                    textureMediaplayer.startPlay();
                    controllerLayout.setVisibility(GONE);
                    is_controller_show = false;
                    playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_pause));
                }
                break;
            case R.id.mTextureView:
                System.out.println("mTextureView 点击");
                if (is_controller_show) {
                    is_controller_show = false;
                    controllerLayout.setVisibility(GONE);
                } else {
                    is_controller_show = true;
                    controllerLayout.setVisibility(VISIBLE);
                }
                break;
        }
    }

    public void start() {
        textureMediaplayer.start();
    }

    public void pause() {
        textureMediaplayer.pause();
    }

    public void restart() {
        textureMediaplayer.restart();
    }

    public void stop() {
        textureMediaplayer.stop();
    }

    public void release() {
        textureMediaplayer.release();
        if (null != textureMediaplayer) {
            textureMediaplayer = null;
        }
        if (null != mMediaPlayer) {
            mMediaPlayer = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
        System.gc();
    }

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    Message message = myHandler.obtainMessage(0);
                    message.sendToTarget();
                }
            };
        }
    }
}
