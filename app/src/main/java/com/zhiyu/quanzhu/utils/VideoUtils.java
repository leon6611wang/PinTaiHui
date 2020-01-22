package com.zhiyu.quanzhu.utils;

import android.media.MediaMetadataRetriever;

import java.util.HashMap;

public class VideoUtils {
    private static VideoUtils utils;

    public static VideoUtils getInstance() {
        if (null == utils) {
            synchronized (VideoUtils.class) {
                utils = new VideoUtils();
            }
        }
        return utils;
    }


    //获取视频的宽高,和时长
    public void getVideoWidthAndHeightAndVideoTimes(String videoUrl, final OnCaculateVideoWidthHeightListener listener) {
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoUrl, new HashMap<String, String>());
        new Thread() {
            @Override
            public void run() {
                float videoTimes = 0;
                float videoWidth = 0;
                float videoHeight = 0;
                super.run();
                try {
                    videoTimes = Float.parseFloat(mediaMetadataRetriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION));
                    videoWidth = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    videoHeight = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                } catch (Exception e) {
                    videoTimes = 0;
                    videoWidth = 0;
                    videoHeight = 0;
                } finally {
                    mediaMetadataRetriever.release();
                    if (null != listener)
                        listener.onVideoWidthHeight(videoWidth, videoHeight, videoTimes);
                }
            }
        }.start();
    }

    public interface OnCaculateVideoWidthHeightListener {
        void onVideoWidthHeight(float w, float h, float vt);
    }
}
