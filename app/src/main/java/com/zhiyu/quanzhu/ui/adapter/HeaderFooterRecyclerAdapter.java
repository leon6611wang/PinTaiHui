package com.zhiyu.quanzhu.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class HeaderFooterRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;
    private ArrayList<T> list = new ArrayList<>();
    private View mHeaderView, mFooterView;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(list.size() + 1);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setDatas(ArrayList<T> data_list) {
        list.addAll(data_list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        if (null != mHeaderView && null != mFooterView) {
            if (position == list.size() + 1) return TYPE_FOOTER;
        } else {
            if (position == list.size()) return TYPE_FOOTER;
        }

        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new Holder(mHeaderView);
        if (null != mFooterView && viewType == TYPE_FOOTER) return new Holder(mFooterView);
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (getItemViewType(position) == TYPE_FOOTER) return;
        final int pos = getRealPosition(viewHolder);
        final T data = list.get(pos);
        onBind(viewHolder, pos, data);

        if (mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        int index = 0;
        if (null == mHeaderView && null == mFooterView) {
            index = position;
        } else if (null == mHeaderView && null != mFooterView) {
            index = position - 1;
        } else if (null != mHeaderView && null == mFooterView) {
            index = position - 1;
        } else if (null != mHeaderView && null != mFooterView) {
            if (position == 1) {
                index = 0;
            } else if (position >= list.size()) {
                index = position - 2;
            } else {
                index = position - 1;
            }
        }
        return index;
    }

    @Override
    public int getItemCount() {
        int size = null == list ? 0 : list.size();
        if (null != mHeaderView && null == mFooterView) {
            size += 1;
        } else if (null == mHeaderView && null != mFooterView) {
            size += 1;
        } else if (null != mHeaderView && null != mFooterView) {
            size += 2;
        }
        return size;
    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data);

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }
}