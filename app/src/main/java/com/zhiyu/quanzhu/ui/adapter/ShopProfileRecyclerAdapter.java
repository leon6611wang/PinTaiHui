package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;

public class ShopProfileRecyclerAdapter extends BaseRecyclerAdapter<String> {
    private Context context;

    public ShopProfileRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_profile_recycler, parent,false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, String data) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder myHolder = (ViewHolder) viewHolder;
            Glide.with(context).load(data).into(myHolder.mImageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
        }
    }
}
