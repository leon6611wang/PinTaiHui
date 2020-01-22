package com.zhiyu.quanzhu.ui.dialog;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.TextureMediaplayer;
import com.zhiyu.quanzhu.ui.widget.VideoPlayer;
import com.zhiyu.quanzhu.utils.ScreentUtils;


/**
 * 视频播放-全屏
 */
public class VideoFullScreenDialog extends Dialog {
    private Context context;
    private VideoPlayer videoPlayer;

    public VideoFullScreenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video_fullscreen);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        params.height = ScreentUtils.getInstance().getScreenHeight(context);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    public void setVideoPath(String url) {
//        String p=Environment.getExternalStorageDirectory().getAbsolutePath();
//        System.out.println("p: "+p);
//        url=url.replace("/storage/emulated/0/","/sdcard/");
        url = "https://flv3.bn.netease.com/c6517b5284a75afa715c54ad530f72736403e38706e220707d534d09a3aa6eaabfb655546b565c3d164ef7779ecb8fe092dc40bdeb8b90e144729229e40c2ff62af3bf12616d6860a3166bcbdc4cabc8e5ff026971f583b21bf98054d2527940f21bc47cf8282958b7096aa31a3d1b2e38e715cb31bc8949.mp4";
//        url="https://flv3.bn.netease.com/68ab2b4d795aa81d6ca70df977d56e325cfb6d0439fd3a2827307f8f3214b669fca909c60e6e4058a267709462e0960739ea7b9069544b5638b28e53afdabc58494eea30ccc6a4aab4b64fca5ad5fecb49624c066ae9782f00231ab081380fbf2bfb5b4aab7d39c72b0c18a8f837f8ae533be087cff85ec9.mp4";
//        url="https://flv3.bn.netease.com/68ab2b4d795aa81d3ebab8384c994a85bb2332281b5215c8151e8b2c9861bb17fb50ba7e7f723f7cec6dfc18fae2803cca0b02f0d9ec7298910f3d5cc9a0a2fe292cc07e4333e7f7f27513960388bd38aa6f6669542bc8c996633d5372aa5c3e63ccf4104f57522ad870fa6848b91b5be83e3fe560d83daf.mp4";
//        url="https://flv3.bn.netease.com/68ab2b4d795aa81deb4ce66626a8e12519e56e242b2f129363ad449a9ec0343e2533325543c2c03b13f5d49544187c1c0e71a49e07a616e275f5a9d0390b5f5a05ccce678336b4ac237809a8bf416f721a825d62c15d24744f3dba0afe271a7e0b9cfaab4eb8389586a95ab7466291578f7c74f2229680db.mp4";
//        url = "https://flv3.bn.netease.com/68ab2b4d795aa81dc55602bab0fb2024d90661bc91df25717342777542a29930816c4ce9c72c198b7fa993f422f7ed3fdf4505e8e459d731725eba95f0eb56b92b94a6a9e98178b0e0b53a1045fc28127a06977ecf22493323f5b8ea8130b1502fb08ba5bbbea3364597ffc0b680eda6fd88b20f3be3e228.mp4";
//        System.out.println("播放视频"+url);
        videoPlayer.setVideoUrl(url);
    }

    private void initViews() {
        videoPlayer = findViewById(R.id.videoPlayer);
    }

    //Dialog时隐藏状态栏
    private void fullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                uiOptions |= 0x00001000;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }

    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void show() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        fullScreenImmersive(getWindow().getDecorView());
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        videoPlayer.stop();
//        videoPlayer.release();
    }
}
