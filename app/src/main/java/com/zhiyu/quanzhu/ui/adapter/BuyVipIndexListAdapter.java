package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.BuyVipIndex;

import java.util.List;

public class BuyVipIndexListAdapter extends RecyclerView.Adapter<BuyVipIndexListAdapter.ViewHolder> {
    private List<BuyVipIndex> list;
    private LinearLayout.LayoutParams currentParam, normalParams;
    private int height, width, margin;
    private Context context;

    public BuyVipIndexListAdapter(Context context) {
        this.context = context;
        height = (int) context.getResources().getDimension(R.dimen.dp_7);
        width = (int) context.getResources().getDimension(R.dimen.dp_20);
        margin = (int) context.getResources().getDimension(R.dimen.dp_5);
        currentParam = new LinearLayout.LayoutParams(width, height);
        currentParam.rightMargin = margin;
        currentParam.gravity = Gravity.CENTER;
        normalParams = new LinearLayout.LayoutParams(height, height);
        normalParams.rightMargin = margin;
        normalParams.gravity = Gravity.CENTER;
    }

    public void setList(List<BuyVipIndex> indexList) {
        this.list = indexList;
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_vip_index_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isCurrent()) {
            holder.mImageView.setLayoutParams(currentParam);
        } else {
            holder.mImageView.setLayoutParams(normalParams);
        }
        Glide.with(context).load(list.get(position).getIcon()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

}
