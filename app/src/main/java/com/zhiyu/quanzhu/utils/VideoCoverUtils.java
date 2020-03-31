package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;

import java.util.HashMap;

/**
 * 视频封面
 */
public class VideoCoverUtils {
    private static VideoCoverUtils utils;

    public static VideoCoverUtils getInstance() {
        if (null == utils) {
            synchronized (VideoCoverUtils.class) {
                utils = new VideoCoverUtils();
            }
        }
        return utils;
    }

    public void setVideoCover(Context context, String videoUrl, VideoPlayerTrackView videoPlayer) {
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(100000)
                                .centerCrop()
                )
                .load(videoUrl)
                .into(videoPlayer.getCoverController().getVideoCover());
    }


    //获取视频的宽高,和时长
    public void getVideoWidthAndHeightAndVideoTimes(String videoUrl, final OnVideoWidthAndHeightListener listener) {
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
                    if (null != listener) {
                        listener.onVideoWidthAndHeight(videoWidth, videoHeight, videoTimes);
                    }
                }
            }
        }.start();
    }

    public interface OnVideoWidthAndHeightListener {
        void onVideoWidthAndHeight(float width, float height, float time);
    }
}
