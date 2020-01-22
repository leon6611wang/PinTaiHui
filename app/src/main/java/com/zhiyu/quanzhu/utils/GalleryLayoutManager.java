package com.zhiyu.quanzhu.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class GalleryLayoutManager extends RecyclerView.LayoutManager {
    private int totalWidth;
    private int scrollOffset;
    private SparseArray<Rect> allRects;

    public GalleryLayoutManager() {
        allRects = new SparseArray<>();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        calculateChildrenSize(recycler);
        recyclerAndFillView(recycler, state);
    }

    private void calculateChildrenSize(RecyclerView.Recycler recycler) {
        totalWidth = (int) (getWidth() * 0.1);
        for (int i = 0; i < getItemCount(); i++) {
            View v = recycler.getViewForPosition(i);
            addView(v);
            measureChildWithMargins(v, (int) (getWidth() * 0.3), (int) (getHeight() * 0.1));
            calculateItemDecorationsForChild(v, new Rect());
            int width = getDecoratedMeasuredWidth(v);
            int height = getDecoratedMeasuredHeight(v);
            Rect childRect = allRects.get(i);
            if (null == childRect) {
                childRect = new Rect();
            }
            childRect.set(totalWidth, (getHeight() - height) / 2, totalWidth + width, (getHeight() + height) / 2);
            totalWidth += width;
            allRects.put(i, childRect);
        }
        totalWidth += getWidth() * 0.1;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (scrollOffset + dx < 0) {
            dx = -scrollOffset;
        } else if (scrollOffset + dx > totalWidth - getWidth()) {
            dx = totalWidth - getWidth() - scrollOffset;
        }
        offsetChildrenHorizontal(-dx);
        recyclerAndFillView(recycler, state);
        scrollOffset += dx;
        return dx;
    }

    private void recyclerAndFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || state.isPreLayout()) return;
        int w = getWidth();
        int h = getHeight();
        detachAndScrapAttachedViews(recycler);
        Rect displayRect = new Rect(scrollOffset, 0, scrollOffset + w, h);

        float scale;
        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayRect, allRects.get(i))) {
                View v = recycler.getViewForPosition(i);
                measureChildWithMargins(v, (int) (getWidth() * 0.3), (int) (getHeight() * 0.1));
                addView(v);

                Rect rect = allRects.get(i);
                scale = (float) (rect.left + getWidth() * 0.35 - getWidth() * 0.5 - scrollOffset);
                scale /= translate();
                scale = Math.abs(scale);
                v.setAlpha(1 - scale * 0.5f);
                scale *= 2 / 9f;
                scale = 1 - scale;
                v.setScaleX(scale);
                v.setScaleY(scale);
                layoutDecorated(v, rect.left - scrollOffset, rect.top, rect.right - scrollOffset, rect.bottom);
            }
        }
    }

    private float translate() {
        return (float) (getWidth() * 0.7);
    }
}
