package com.zhiyu.quanzhu.ui.widget;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

import static android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

/**
 * 视频播放自定义控件
 */
public class TextureMediaplayer implements TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private String url;
    private Surface surface;
    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    public TextureMediaplayer(TextureView textureView, MediaPlayer mediaPlayer, String url) {
        this.textureView = textureView;
        this.mediaPlayer = mediaPlayer;
        this.url = url;

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
    }

    private boolean isFirst = true;

    public void startPlay() {
        if (isFirst) {
            creat();
            isFirst = false;
        } else {
            start();
        }

    }

    public void creat() {
        textureView.setSurfaceTextureListener(this);
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new PlayerVideoThread().start();
    }


    private class PlayerVideoThread extends Thread {
        @Override
        public void run() {
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mediaPlayer.prepareAsync();
            } catch (IllegalStateException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        System.out.println("textureView 是否开启硬件加速" + textureView.isHardwareAccelerated());
        mp.setSurface(surface);
        mp.start();
        System.out.println("预加载完成，开始播放.");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("完成播放");
//        mp.start();
    }

    public void restart() {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        restart();
        return true;
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    public void setVolume(int volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


}
