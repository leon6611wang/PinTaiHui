package com.leon.shehuibang.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 镜像图片方法
 */
public class MirrorImageUtils {
    private static Vibrator mVibrator;
    private static WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private static Context context;
    private ImageView mirrorImageView;
    private float alpha = 0.5f;
    private int mirrorWidth = 300, mirrorHeight = 300;

    private static MirrorImageUtils utils;

    public static MirrorImageUtils getInstance(Context ctxt) {
        if (null == utils) {
            synchronized (MirrorImageUtils.class) {
                utils = new MirrorImageUtils();
                context = ctxt;
                init();
            }
        }
        return utils;
    }

    public static void init() {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void createMirrorImage(Bitmap bitmap, int x, int y, int width, int height) {
        mirrorWidth = width == 0 ? mirrorWidth : width;
        mirrorHeight = height == 0 ? mirrorHeight : height;
        mVibrator.vibrate(50);
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowLayoutParams.x = x - mirrorWidth / 2;
        mWindowLayoutParams.y = y - mirrorHeight / 2;
        mWindowLayoutParams.alpha = alpha;
        mWindowLayoutParams.width = mirrorWidth;
        mWindowLayoutParams.height = mirrorHeight;
        mirrorImageView = new ImageView(context);
        mirrorImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mirrorImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mirrorImageView, mWindowLayoutParams);
    }

    public void moveMirrorImage(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mirrorWidth / 2;
        mWindowLayoutParams.y = moveY - mirrorHeight / 2;
        mWindowManager.updateViewLayout(mirrorImageView, mWindowLayoutParams);
    }

    public void clearMirrorImage() {
        mWindowManager.removeViewImmediate(mirrorImageView);
    }

    public void scaleMirrorImage(int width, int height) {
        mWindowLayoutParams.width = width;
        mWindowLayoutParams.height = height;
        mWindowManager.updateViewLayout(mirrorImageView, mWindowLayoutParams);
    }
}
