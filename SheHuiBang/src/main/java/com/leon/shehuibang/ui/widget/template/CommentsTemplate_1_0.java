package com.leon.shehuibang.ui.widget.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.leon.shehuibang.R;

import java.util.List;


public class CommentsTemplate_1_0 extends LinearLayout implements View.OnClickListener {
    private View view;
    private ImageView imageView0;
    private Bitmap bitmap;

    public CommentsTemplate_1_0(Context context) {
        super(context);
        view = inflate(context, R.layout.template_comments_1_0, this);
        imageView0 = view.findViewById(R.id.imageView0);
    }

    public void setImageUrl(List<String> urlList) {
        if (null != urlList && urlList.size() == 1) {
            Glide.with(getContext()).load(urlList.get(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    bitmap = bd.getBitmap();
                }
            });
            Glide.with(getContext()).load(urlList.get(0)).error(R.drawable.image_bg).into(imageView0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView0:

                break;
        }
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                int dx = (int) event.getX();
//                int dy = (int) event.getY();
//                System.out.println("dx: " + dx + " , dy: " + dy);
//                if (null != bitmap) {
//                    MirrorImageUtils.getInstance(getContext()).createMirrorImage(bitmap, dx, dy, 300, 400);
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int mx = (int) event.getX();
//                int my = (int) event.getY();
//                System.out.println("mx: " + mx + " , my: " + my);
//                MirrorImageUtils.getInstance(getContext()).moveMirrorImage(mx, my);
//                if (Math.abs(my) > ScreentUtils.getInstance().getScreenHeight(getContext()) / 2) {
//                    MirrorImageUtils.getInstance(getContext()).scaleMirrorImage(600, 800);
//                } else {
//                    MirrorImageUtils.getInstance(getContext()).scaleMirrorImage(300, 400);
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("action up");
//                if (null != bitmap) {
//                    MirrorImageUtils.getInstance(getContext()).clearMirrorImage();
//                }
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

}
