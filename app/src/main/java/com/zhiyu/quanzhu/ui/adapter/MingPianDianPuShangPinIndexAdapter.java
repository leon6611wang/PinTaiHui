package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Goods;

import java.util.List;

public class MingPianDianPuShangPinIndexAdapter extends RecyclerView.Adapter<MingPianDianPuShangPinIndexAdapter.ViewHolder> {
    private Context context;

    public MingPianDianPuShangPinIndexAdapter(Context context) {
        this.context = context;
    }

    private int indexSize, currentIndex, index = -1;

    public void setIndexSize(int size) {
        this.indexSize = size;
        if (index != currentIndex) {
            index = currentIndex;
            notifyDataSetChanged();
        }
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View indexView;

        public ViewHolder(View itemView) {
            super(itemView);
            indexView = itemView.findViewById(R.id.indexView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dianpu_shangpin_index, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (currentIndex == position) {
            holder.indexView.setBackgroundColor(context.getResources().getColor(R.color.text_color_yellow));
        } else {
            holder.indexView.setBackgroundColor(context.getResources().getColor(R.color.text_color_gray));
        }
    }

    @Override
    public int getItemCount() {
        return indexSize;
    }
}
