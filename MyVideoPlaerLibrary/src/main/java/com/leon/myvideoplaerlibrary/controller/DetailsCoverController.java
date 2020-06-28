package com.leon.myvideoplaerlibrary.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.leon.myvideoplaerlibrary.R;
import com.leon.myvideoplaerlibrary.base.BaseCoverController;
import com.leon.myvideoplaerlibrary.utils.Logger;

/**
 * TinyHung@Outlook.com
 * 2019/4/11
 * Video Details Cover Controller
 */

public class DetailsCoverController extends BaseCoverController {

    private static final String TAG = "DetailsCoverController";
    public ImageView mVideoCover;

    public DetailsCoverController(@NonNull Context context) {
        this(context,null);
    }

    public DetailsCoverController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DetailsCoverController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.video_details_cover_controller_layout,this);
        mVideoCover = (ImageView) findViewById(R.id.video_cover_icon);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logger.d(TAG,"onFinishInflate");
    }

    public ImageView getVideoCover() {
        return mVideoCover;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=mVideoCover){
            mVideoCover.setImageResource(0);
            mVideoCover=null;
        }
    }
}