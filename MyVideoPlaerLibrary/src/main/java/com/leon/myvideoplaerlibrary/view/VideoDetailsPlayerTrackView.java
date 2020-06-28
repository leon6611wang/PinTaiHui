package com.leon.myvideoplaerlibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.leon.myvideoplaerlibrary.R;
import com.leon.myvideoplaerlibrary.base.BaseVideoPlayer;
import com.leon.myvideoplaerlibrary.controller.DefaultGestureController;
import com.leon.myvideoplaerlibrary.controller.DefaultVideoController;
import com.leon.myvideoplaerlibrary.controller.DetailsCoverController;

/**
 * TinyHung@Outlook.com
 * 2019/4/11
 * Video Default Controller
 */

public class VideoDetailsPlayerTrackView extends BaseVideoPlayer<DefaultVideoController,
        DetailsCoverController,DefaultGestureController>{

    @Override
    protected int getLayoutID() {
        return R.layout.video_default_track_layout;
    }

    public VideoDetailsPlayerTrackView(@NonNull Context context) {
        this(context,null);
    }

    public VideoDetailsPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoDetailsPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}