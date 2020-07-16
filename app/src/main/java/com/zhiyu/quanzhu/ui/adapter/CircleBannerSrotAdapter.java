package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

public class CircleBannerSrotAdapter extends RecyclerView.Adapter<CircleBannerSrotAdapter.CircleBannerViewHolder> implements ItemTouchHelperAdapter {
    private List<String> list;
    private Context context;
    private LinearLayout.LayoutParams ll;
    private int dp_15, dp_7_5;

    public CircleBannerSrotAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_7_5 = (int) context.getResources().getDimension(R.dimen.dp_7_5);
        int width = Math.round((screenWidth - dp_15 * 3) / 2);
        int height = Math.round(0.563636f * width);
        ll = new LinearLayout.LayoutParams(width, height);
        ll.bottomMargin = dp_7_5;
        ll.topMargin = dp_7_5;
        ll.leftMargin = dp_7_5;
        ll.rightMargin = dp_7_5;

    }

    public void setList(List<String> imgs) {
        this.list = imgs;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CircleBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircleBannerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_banner_sort, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CircleBannerViewHolder holder, final int position) {
        if (list.get(position).equals("add")) {
            Glide.with(context).load(R.mipmap.add_circle_banner).error(R.drawable.image_error).into(holder.imageView);
        } else {
            Glide.with(context).load(list.get(position)).error(R.drawable.image_error).into(holder.imageView);
        }
        holder.imageView.setLayoutParams(ll);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onBannerOperationListener) {
                    if (position < list.size() - 1) {
                        onBannerOperationListener.onBannerClick(position);
                    } else if (position == list.size() - 1) {
                        onBannerOperationListener.onAddBannerClick();
                    }
                }
            }
        });
    }

    class CircleBannerViewHolder extends RecyclerView.ViewHolder {
        NiceImageView imageView;

        public CircleBannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == list.size() - 1 ||
                toPosition == list.size() - 1) {
//            Toast.makeText(context, "此项不可移动", Toast.LENGTH_SHORT).show();
        } else {
            /**
             * 在这里进行给原数组数据的移动
             */
            Collections.swap(list, fromPosition, toPosition);
            /**
             * 通知数据移动
             */
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    public List<String> getList() {
        return list;
    }


    @Override
    public void onSwipe(int position) {
        /**
         * 原数据移除数据
         */
        list.remove(position);
        /**
         * 通知移除
         */
        notifyItemRemoved(position);
    }

    private OnBannerOperationListener onBannerOperationListener;

    public void setOnBannerOperationListener(OnBannerOperationListener listener) {
        this.onBannerOperationListener = listener;
    }

    public interface OnBannerOperationListener {
        void onBannerClick(int position);
        void onAddBannerClick();
    }

}
