package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class MyTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "MyTextureView";
    private String playingUrl = "";
    private MediaPlayer mMediaPlayer;
    private Surface surface;
    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnErrorListener errorListener;
    private MediaPlayer.OnCompletionListener completionListener;
    private MediaPlayer.OnInfoListener infoListener;
    private MediaPlayer.OnSeekCompleteListener seekCompleteListener;
    private Timer timer;
    private TimerTask task;
    private int current, total;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyTextureView> viewWeakReference;

        public MyHandler(MyTextureView view) {
            viewWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            MyTextureView view = viewWeakReference.get();
            switch (msg.what) {
                case 0:
                    view.current = view.mMediaPlayer.getCurrentPosition();
                    if (view.total == 0) {
                        view.total = view.mMediaPlayer.getDuration();
                    }
                    if (null != view.onCurrentListener) {
                        view.onCurrentListener.onCurrent(view.current, view.total, view.mMediaPlayer.isPlaying());
                    }
                    if (view.current == view.total) {
                        view.stopTask();
                    }
                    break;
            }
        }
    }

    public MyTextureView(Context context) {
        super(context);
        initView();
    }

    public MyTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    public void startPlay() {
        if (mMediaPlayer != null) {
            startTask();
            mMediaPlayer.start();
            Log.e(TAG, "startPlay");
        } else {
            Log.e(TAG, "start Error media is null");
        }
    }

    public void pausePlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            stopTask();
            Log.e(TAG, "stopPlay");
        } else {
            Log.e(TAG, "pause Error media is null");
        }
    }

    public void resetPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            stopTask();
            Log.e(TAG, "resetPlay");
        } else {
            Log.e(TAG, "reset Error media is null");
        }
    }


    public void destory() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        stopTask();
    }


    public void setUrl(String path) {
//        if (!playingUrl.equals(path)) {
        mMediaPlayer.reset();
        try {
            playingUrl = path;
            if (path.contains("http")) {
                mMediaPlayer.setDataSource(path);
            } else {
                FileInputStream fis = null;
                fis = new FileInputStream(new File(path));
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setDataSource(fis.getFD());
            }
            mMediaPlayer.prepareAsync();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        } else {
//            startTask();
//            mMediaPlayer.start();
//        }
        if (preparedListener != null) {
            mMediaPlayer.setOnPreparedListener(preparedListener);
        }
    }

    private void initView() {
        setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        initMediaPlayer(surfaceTexture);
    }

    /**
     * @param surfaceTexture
     * @return -1 初始化失败
     */
    private int initMediaPlayer(SurfaceTexture surfaceTexture) {
        if (surfaceTexture == null)
            return 1;
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                Log.e(TAG, " initMediaPlayer new media");
            }
            surface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);
            Log.e(TAG, " initMediaPlayer Success");
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, " initMediaPlayer-" + e.getMessage());
            return -1;
        }

    }

    private void startTask() {
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

    }

    private void stopTask() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    private OnCurrentListener onCurrentListener;

    public void setmMediaPlayer(MediaPlayer mMediaPlayer, OnCurrentListener listener) {
        this.mMediaPlayer = mMediaPlayer;
        mMediaPlayer.setSurface(new Surface(this.getSurfaceTexture()));
        this.onCurrentListener = listener;
    }

    public String getPlayingUrl() {
        return playingUrl;
    }

    public void setPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void setErrorListener(MediaPlayer.OnErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setCompletionListener(MediaPlayer.OnCompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public void setInfoListener(MediaPlayer.OnInfoListener infoListener) {
        this.infoListener = infoListener;
    }

    public void setSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekCompleteListener) {
        this.seekCompleteListener = seekCompleteListener;
    }

    /**
     * 播放进度回调
     */
    public interface OnCurrentListener {
        void onCurrent(int currentPosition, int totalPosition, boolean isPlay);
    }
}
