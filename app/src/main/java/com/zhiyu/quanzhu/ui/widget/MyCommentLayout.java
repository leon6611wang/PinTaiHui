package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class MyCommentLayout extends LinearLayout {
    private final String TAG = "MyCommentLayout";

    public MyCommentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (null != onMyCommentLayoutDistanceYListener) {
                    onMyCommentLayoutDistanceYListener.onMyCommentLayoutDistanceY(y - downY);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX=event.getX();
//                downY=event.getY();
//                System.out.println("down");
//                System.out.println("downX: "+downX+" , downY: "+downY);
//                Log.d(TAG,"downX: "+downX+" , downY: "+downY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float x=event.getX();
//                float y=event.getY();
//                System.out.println("move");
//                System.out.println("disX: "+(x-downX)+" , disY: "+(y-downY));
//                Log.d(TAG,"disX: "+(x-downX)+" , disY: "+(y-downY));
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("up");
//                break;
//        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX=event.getX();
//                downY=event.getY();
//                System.out.println("down");
//                System.out.println("downX: "+downX+" , downY: "+downY);
//                Log.d(TAG,"downX: "+downX+" , downY: "+downY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float x=event.getX();
//                float y=event.getY();
//                System.out.println("move");
//                System.out.println("disX: "+(x-downX)+" , disY: "+(y-downY));
//                Log.d(TAG,"disX: "+(x-downX)+" , disY: "+(y-downY));
//                break;
//            case MotionEvent.ACTION_UP:
//                System.out.println("up");
//                break;
//        }
        return super.dispatchTouchEvent(event);
    }

    public interface OnMyCommentLayoutDistanceYListener {
        void onMyCommentLayoutDistanceY(float distanceY);
    }

    private OnMyCommentLayoutDistanceYListener onMyCommentLayoutDistanceYListener;

    public void setOnMyCommentLayoutDistanceYListener(OnMyCommentLayoutDistanceYListener listener) {
        this.onMyCommentLayoutDistanceYListener = listener;
    }
}
