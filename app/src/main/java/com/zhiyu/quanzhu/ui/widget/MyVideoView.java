package com.zhiyu.quanzhu.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.StringUtils;
import io.vov.vitamio.widget.VideoView;

public class MyVideoView extends LinearLayout implements View.OnClickListener {
    private Context context;
    private View view;
    private VideoView videoView;
    private String urlString = "https://flv3.bn.netease.com/c5d39796715c6dffb63a5f6d63a890ebab8bae6091712a37190db88b8991570b0f3b7c5435281288e959fa3b8990ebca3318e57662bd830ec45b8cfea9f72cfbc39b878df01eaf302e40097222a8c8374aaf53328bd68d5f230289a05ed6d3d3141b3ce6fb4c36b063bad28584c5d45bb31c4d64faff7a4b.mp4";
    //    private String urlString = "http://flv3.bn.netease.com/f5e519707cd7c4439104fa0538ee782a0c05986e0ac700e1a51d8baa03ccddfd2fa546c9590a2e5c4733131fea37773b48a02d3b43805b6474767d5b55932a86a27536d6006af01185442fb055f9899f284cf8daad27724f713c6744df8459d5a85fbaa4190e6f0ac5e4037d931108d309e84960ddfe6b24.mp4";
//        private String urlString="http://flv3.bn.netease.com/f5e519707cd7c443ea88addd2b2ed92c578cd362a43ffd8df2a9160e973a7f8e4021d7c27b8c1ee9b5abc32c155a96461b4e5ea5ca374e6e95dc5d830023830e59364cf9316ea6fef02d46b662228bde78a99ba125c009dcf7c517ffa21b7f7251fa989aa60caaa7f0933b2176fc8b400cce8a56627e63e8.mp4";
//    private String urlString = "https://flv3.bn.netease.com/a89fcc64a976fb76de87a56bd1b24d42b29a5cbb3ba80f9608dcc691eac58db04d91ba88aee98b5de81e0954d283ecce670f6d093bdd91f5a08cd9bacc4047c42e78bc66d5233f6d9fa782d7da883518419b17fa00841667739392d7b561cdc16261256291dac4822df7e22c1db9dfa00fda71617e65299c.mp4";
    private LinearLayout bottomControlLayout;
    private ImageView playPauseSmallImaveView, fullScreenImageView;
    private TextView currentTimeTextView, totalTimeTextView;
    private SeekBar seekBar;
    private boolean isPlay;
    private boolean isFinish;
    private boolean isFullScreen;
    private long pauseSize;
    private long size;
    private Timer timer;
    private TimerTask task;
    private MyHandler myHandler = new MyHandler(this);
    private LinearLayout videoViewBgLayout;
    private FrameLayout rootLayout;
    private boolean isTouchChangeSeek = false;

    private float screenRatio;//手机屏幕宽高比
    private float screenHWRation;//手机屏幕高宽比
    private int screenWidth, screenHeight;//手机屏幕宽高

    private float layoutRatio = 0.53333f;//布局宽高比
    private int layoutWidth, layoutHeight;//布局宽高
    private LinearLayout.LayoutParams layoutParams;//框架布局参数

    private float videoRatio;//视频真实宽高比
    private int videoWidth, videoHeight;//视频真实宽高
    private RelativeLayout.LayoutParams videoParams;//视频布局参数

    private LinearLayout voiceLightLayout;
    private ImageView voiceLightImageView;
    private TextView voiceLightTextView;

    private GestureDetector mGestureDetector;

    private Activity activity;

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

    private static class MyHandler extends Handler {
        WeakReference<MyVideoView> viewWeakReference;

        public MyHandler(MyVideoView videoView) {
            viewWeakReference = new WeakReference<>(videoView);
        }

        @Override
        public void handleMessage(Message msg) {
            MyVideoView view = viewWeakReference.get();
            switch (msg.what) {
                case 1:
//                    System.out.println("myhandler: "+StringUtils.generateTime(view.videoView
//                            .getCurrentPosition()));
                    view.currentTimeTextView.setText(StringUtils.generateTime(view.videoView
                            .getCurrentPosition()));
                    if (null != view.videoView) {
                        view.seekBar(view.videoView.getCurrentPosition());
                    }
                    break;
            }
        }
    }

    public MyVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.widget_myvideoview, null);
        initViews();
        initData();
        initTimerTask();
        this.addView(view);
    }

    private void initData() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(context);
        screenRatio = (float) screenWidth / (float) screenHeight;
        screenHWRation = (float) screenHeight / (float) screenWidth;
        System.out.println("screenRatio: " + screenRatio);
        layoutWidth = screenWidth;
        layoutHeight = (int) (layoutRatio * screenWidth);

        // 手势操作类，并设置手势监听
        mGestureDetector = new GestureDetector(context, new MyGestureListener());


//        layoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
//        rootLayout.setLayoutParams(layoutParams);
    }

    private void initTimerTask() {
        if (null == timer) {
            timer = new Timer();
        }
        if (null == task) {
            task = new TimerTask() {
                @Override
                public void run() {
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                }
            };
        }
    }

    private void initViews() {
        rootLayout = view.findViewById(R.id.rootLayout);
        bottomControlLayout = view.findViewById(R.id.bottomControlLayout);
        bottomControlLayout.getBackground().setAlpha(100);
        videoViewBgLayout = findViewById(R.id.videoViewBgLayout);
        playPauseSmallImaveView = view.findViewById(R.id.playPauseSmallImaveView);
        playPauseSmallImaveView.setOnClickListener(this);
        fullScreenImageView = view.findViewById(R.id.fullScreenImageView);
        fullScreenImageView.setOnClickListener(this);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView);
        seekBar = view.findViewById(R.id.videoSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isTouchChangeSeek) {
                    int value = (int) (seekBar.getProgress()
                            * videoView.getDuration() / seekBar.getMax());
                    videoView.seekTo(value);
                    videoView.start();
                    isPlay = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlay = false;
                videoView.pause();
                isTouchChangeSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouchChangeSeek = false;
                updateSeekBar();
            }
        });

        voiceLightLayout = view.findViewById(R.id.voiceLightLayout);
        voiceLightLayout.getBackground().setAlpha(160);
        voiceLightImageView = view.findViewById(R.id.voiceLightImageView);
        voiceLightTextView = view.findViewById(R.id.voiceLightTextView);
    }


    public void initVideo(String path, Activity aty) {
        this.activity = aty;
        // 获取系统服务
        mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        // 获取系统的最大音量
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        voice_unit_percent_v = ((float) screenHeight) / ((float) mMaxVolume);
        voice_unit_percent_h = ((float) screenWidth) / ((float) mMaxVolume);
        setLight();
        if (Vitamio.initialize(context)) {
            videoView = view.findViewById(R.id.mVideoView);
            videoView.setVideoURI(Uri.parse(urlString));//播放网络视频
//            videoView.setVideoPath(path);//播放本地视频
            //设置高画质
            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
            videoView.requestFocus();
            isPlay = true;
//            videoView.start();
            setListener();
//            update = new UpDateSeekBar();
//            new Thread(update).start();

        }
    }

    private void setListener() {
        //设置缓冲进度的监听
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                System.out.println(percent + "%");
//                tvProgress.setText(percent + "%");
            }
        });
        //设置缓冲下载监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲时的视图变化
                        caculateVideoWidthAndHeight();
                        unFullScreen();
                        mp.pause();
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲好后的视图变化（可播放）
                        if (isFullScreen) {
                            fullScreen();
                        } else {
                            unFullScreen();
                        }

                        mp.start();
                        updateSeekBar();
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                        tvDownloadSpeed.setText("当前网速:" + extra + "kb/s");//下载是速度
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
                totalTimeTextView.setText(StringUtils.generateTime(videoView
                        .getDuration()));
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playPauseSmallImaveView:
                videoPlayPause();
                break;
            case R.id.fullScreenImageView:
                if (isFullScreen) {
                    if (null != onFullScreenListener) {
                        onFullScreenListener.onFullScreen(videoWidth, videoHeight, videoRatio, !isFullScreen);
                        unFullScreen();
                    }
                } else {
                    if (null != onFullScreenListener) {
                        onFullScreenListener.onFullScreen(videoWidth, videoHeight, videoRatio, !isFullScreen);
                        fullScreen();
                    }
                }

                break;
        }
    }

    private void videoPlayPause() {
        if (videoView.isPlaying()) {
            playPauseSmallImaveView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_pause_small));
            videoView.pause();
            isPlay = false;
            seekBar.setEnabled(false);
        } else {
            playPauseSmallImaveView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_play_small));
            videoView.start();
            updateSeekBar();
            isPlay = true;
            seekBar.setEnabled(true);
        }
    }

    public void pause() {
        videoView.pause();
    }

    public void resume() {
        if (isPlay) {
            videoView.start();
        }
    }

    public void destory() {
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        isPlay = false;
        isFinish = true;
        System.gc();

    }

    private void seekBar(long size) {
        if (videoView.isPlaying()) {
            long mMax = videoView.getDuration();
            int sMax = seekBar.getMax();
            seekBar.setProgress((int) (size * sMax / mMax));
        }
    }


    private void updateSeekBar() {
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
        timer.schedule(task, 0, 100);
    }


    /**
     * 计算视频宽高
     */
    private void caculateVideoWidthAndHeight() {
        videoWidth = videoView.getVideoWidth();
        videoHeight = videoView.getVideoHeight();
        videoRatio = videoView.getVideoAspectRatio();
    }

    private OnFullScreenListener onFullScreenListener;

    public void setOnFullScreenListener(OnFullScreenListener listener) {
        this.onFullScreenListener = listener;
    }

    public interface OnFullScreenListener {
        void onFullScreen(int width, int height, float ratio, boolean isFullScreen);
    }

    /**
     * 全屏操作
     */
    private void fullScreen() {
        isFullScreen = true;
        RelativeLayout.LayoutParams videoParams = null;
        int video_width = 0;
        int video_height = 0;
//        System.out.println("screenWdith: "+screenWidth+" , screenHeight: "+screenHeight);
//        System.out.println("videoRatio: "+videoRatio+" , screenHWRation: "+screenHWRation);
        if (videoRatio > 1.0f) {//宽视频
            if (videoRatio >= screenHWRation) {//视频宽度以屏幕高度为准
//                System.out.println("11");
                video_width = screenHeight;
                video_height = (int) (video_width / videoRatio);
            } else {//视频高度以屏幕宽度为准(未找到视频源，待测)
//                System.out.println("12");
                video_height = screenWidth;
                video_width = (int) (videoRatio * video_height);
            }
        } else {//竖视频
            if (videoRatio >= screenRatio) {//视频宽度以屏幕宽度为准
//                System.out.println("21");
                video_width = screenWidth;
                video_height = (int) (video_width / videoRatio);
            } else {//视频高度以屏幕高度为准
//                System.out.println("22");
                video_height = screenHeight;
                video_width = (int) (videoRatio * video_height);
            }
        }
        fullScreenImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_unfullscreen));
        layoutParams = new LinearLayout.LayoutParams(video_width, video_height);
        rootLayout.setLayoutParams(layoutParams);
        videoParams = new RelativeLayout.LayoutParams(video_width, video_height);
        videoView.setLayoutParams(videoParams);
    }

    private void unFullScreen() {
        isFullScreen = false;
        RelativeLayout.LayoutParams videoParams = null;
        int video_width = 0;
        int video_height = 0;
        if (videoRatio > 1.0f) {
            if (videoRatio >= layoutRatio) {
                video_width = layoutWidth;
                video_height = (int) (video_width / videoRatio);
            } else {
                video_height = layoutHeight;
                video_width = (int) (videoRatio * video_height);
            }
        } else {
            video_height = layoutHeight;
            video_width = (int) (videoRatio * video_height);
        }
        fullScreenImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_fullscreen));
        layoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
        rootLayout.setLayoutParams(layoutParams);
        videoParams = new RelativeLayout.LayoutParams(video_width, video_height);
        videoParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoView.setLayoutParams(videoParams);
    }


    private float light_percent = 0.8f;
    private float voice_percent = 0.2f;
    private boolean change_light = false;
    private boolean change_voice = false;
    float startX, startY, currentX, currentY, disX, disY;
    private float voice_unit_percent_v, voice_unit_percent_h;//纵向滑动声音单位尺度，横向滑动声音单位尺度
    private int voice_value_temp = 0;//声音调节临时存量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                voice_value_temp = 0;
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 处理手势结束
//                endGesture();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = event.getRawY();
                disY = startY - currentY;
                if (isFullScreen) {//全屏
                    if (videoRatio > 1.0f) {//屏幕横置

                    } else {
                        // 右边滑动
                        if (startX > screenWidth / 2) {
                            if (disY > 0) {//增加音量
                                float count_float = disY / voice_unit_percent_v;
                                int count_int = Math.round(count_float);
                                if (count_int != voice_value_temp) {
                                    voice_value_temp = count_int;
                                    mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    if (mVolume < 0)
                                        mVolume = 0;
//                                    System.out.println("count_int: " + count_int + " , mVolume: " + mVolume);
                                    int index = count_int + mVolume;
                                    if (index > mMaxVolume) {
                                        index = mMaxVolume;
                                    } else if (index < 0) {
                                        index = 0;
                                    }
//                                    mVolume = index;
                                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
                                    voiceLightTextView.setText(String.valueOf(mVolume));
                                    if (index == 0) {
                                        voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_voice_no));
                                    } else {
                                        voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_voice));
                                    }
                                }


                            } else if (disY < 0) {//减小音量
                                float count_float = disY / voice_unit_percent_v;
                                int count_int = Math.round(count_float);
                                if (count_int != voice_value_temp) {
                                    voice_value_temp = count_int;
                                    mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    if (mVolume < 0)
                                        mVolume = 0;
                                    int index = mVolume + count_int;
                                    if (index > mMaxVolume) {
                                        index = mMaxVolume;
                                    } else if (index < 0) {
                                        index = 0;
                                    }
                                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
                                    voiceLightTextView.setText(String.valueOf(index));
                                    if (index == 0) {
                                        voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_voice_no));
                                    } else {
                                        voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_voice));
                                    }
                                }
                            }

                        } else if (startX < screenWidth / 2) {// 左边滑动
                            System.out.println("左边滑动percent" + (startY - currentY) / screenHeight);
                        }
                    }
                } else {

                }
//                // 右边滑动
//                if (startX > screenWidth * 4.0 / 5) {
//                    float percent = (startY - currentY) / screenHeight;
//                    System.out.println("右边滑动percent: " + percent);
//                    setVoice(percent);
//                } else if (startX < screenWidth / 5.0) {// 左边滑动
//                    System.out.println("左边滑动percent" + (startY - currentY) / screenHeight);
//                }
                break;
            default:
                break;
        }


//        mGestureDetector.onTouchEvent(event);
        return true;
//        return super.onTouchEvent(event);
//        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        System.out.println("手势结束");
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        // 当用户在屏幕上"滚动"时触发该方法
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
//            startX = e1.getX();
//            startY = e1.getY();
//            currentY = e2.getRawY();
//            System.out.println("startY: "+startY+" , currentY: "+currentY+" , distanceY: "+distanceY);
//            disY = (currentY - startY);
//            System.out.println("disY: "+disY);
            // 获得屏幕的宽高
//            int windowWidth = ScreentUtils.getInstance().getScreenWidth(context);
//            int windowHeight = ScreentUtils.getInstance().getScreenHeight(context);
//            // 右边滑动
//            if (startX > windowWidth / 2) {
//                if (!change_voice) {
//                    change_light = false;
//                    change_voice = true;
//                    voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_voice));
//                }
//
//                float percent = 0.0f;
//                if (isFullScreen) {
//                    if (videoRatio > 1.0f) {
//                        percent = (Math.abs(disY) / (float) (screenWidth * 0.6));
//                    } else {
//                        percent = (Math.abs(disY) / (float) (screenHeight * 0.6));
//                    }
//                } else {
//
//                }
////                System.out.println("percent: "+percent*100);
//                if (disY < 0) {
//                    voice_percent += percent;
//                } else {
//                    voice_percent -= percent;
//                }
////                System.out.println("voice_percent: " + voice_percent + " , percent: " + percent * 100);
//                if (voice_percent > 1.0f) {
//                    voice_percent = 1.0f;
//                } else if (voice_percent < 0.0f) {
//                    voice_percent = 0.0f;
//                }
////                setVoice(voice_percent);
//            } else if (startX < windowWidth / 2) {// 左边滑动
//                if (!change_light) {
//                    change_light = true;
//                    change_voice = false;
//                    voiceLightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.video_light));
//                }
//                float percent = (disY / (videoView.getLayoutParams().height * 20));
////                System.out.println("light_percent: " + light_percent + " , percent: " + percent);
//                if (disY < 0) {
//                    light_percent += percent;
//                } else {
//                    light_percent -= percent;
//                }
//
//                if (light_percent < 0) {
//                    light_percent = 0;
//                } else if (light_percent > 1.0f) {
//                    light_percent = 1.0f;
//                }
//                setLight();
////                System.out.println("左边滑动: " + light_percent);
//            }
//            return true;
            // 得到x，y坐标当前值，相对于当前view
            float mOldX = e1.getX(), mOldY = e1.getY();
            // getRawY()是表示相对于屏幕左上角的y坐标值
            int y = (int) e2.getRawY();
            // 获得屏幕的宽高
            int windowWidth = ScreentUtils.getInstance().getScreenWidth(context);
            int windowHeight = ScreentUtils.getInstance().getScreenHeight(context);
            // 右边滑动
            if (mOldX > windowWidth * 4.0 / 5) {
                float percent = (mOldY - y) / windowHeight;
                System.out.println("右边滑动percent: " + percent);
//                setVoice(percent);
            }


            // 左边滑动
            else if (mOldX < windowWidth / 5.0)
                System.out.println("左边滑动percent" + (mOldY - y) / windowHeight);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 调节当前屏幕亮度
     * brightness是一个0.0-1.0之间的一个float类型数值
     */
    private void setLight() {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = light_percent;
        window.setAttributes(lp);
        voiceLightTextView.setText((int) (light_percent * 100) + "%");
    }

    private void setVoice(int index) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        voiceLightTextView.setText(String.valueOf(mVolume));
    }
}
