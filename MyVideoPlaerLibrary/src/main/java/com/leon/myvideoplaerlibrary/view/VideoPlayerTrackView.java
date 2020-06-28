package com.leon.myvideoplaerlibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.leon.myvideoplaerlibrary.R;
import com.leon.myvideoplaerlibrary.base.BaseVideoPlayer;
import com.leon.myvideoplaerlibrary.controller.DefaultCoverController;
import com.leon.myvideoplaerlibrary.controller.DefaultGestureController;
import com.leon.myvideoplaerlibrary.controller.DefaultVideoController;

/**
 * TinyHung@Outlook.com
 * 2019/4/8
 * Default Video
 * 示例视频播放器
 */

public class VideoPlayerTrackView extends BaseVideoPlayer<DefaultVideoController,
        DefaultCoverController, DefaultGestureController> {

    @Override
    protected int getLayoutID() {
        return R.layout.video_default_track_layout;
    }

    public VideoPlayerTrackView(@NonNull Context context) {
        this(context,null);
    }

    public VideoPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}