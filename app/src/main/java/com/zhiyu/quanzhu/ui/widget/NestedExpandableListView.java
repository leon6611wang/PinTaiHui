package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class NestedExpandableListView extends ExpandableListView {

    public NestedExpandableListView(Context context) {
        super(context);
    }


    public NestedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //给一个较大值即可，不一定为Integer.MAX_VALUE >> 2
        int heightMeasureSpecNew = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew);
    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
//                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
//        ViewGroup.LayoutParams params = getLayoutParams(); // 存在一个问题 ，如果是全部收起的话 ，就会导致页面空白
//        params.height = getMeasuredHeight();
//
//    }
//
//    //为listview/Y，设置初始值,默认为0.0(ListView条目一位置)
//    private float mLastY;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        //重点在这里
//        int action = ev.getAction();
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                super.onInterceptTouchEvent(ev);
//                //不允许上层viewGroup拦截事件.
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //满足listView滑动到顶部，如果继续下滑，那就让scrollView拦截事件
//                if (getFirstVisiblePosition() == 0 && (ev.getY() - mLastY) > 0) {
//                    //允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                //满足listView滑动到底部，如果继续上滑，那就让scrollView拦截事件
//                else if (getLastVisiblePosition() == getCount() - 1 && (ev.getY() - mLastY) < 0) {
//                    //允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    //不允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                //不允许上层viewGroup拦截事件
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//        }
//
//        mLastY = ev.getY();
//        return super.dispatchTouchEvent(ev);
//
//    }


}
