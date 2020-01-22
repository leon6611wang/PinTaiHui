package com.zhiyu.quanzhu.utils.recyclerTouchHelper;

public interface ItemTouchHelperAdapter {
    /**
     * @param fromPosition 起始位置
     * @param toPosition 移动的位置
     */
    void onMove(int fromPosition, int toPosition);
    void onSwipe(int position);
}
