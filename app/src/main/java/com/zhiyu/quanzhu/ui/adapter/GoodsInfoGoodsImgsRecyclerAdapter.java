package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.LargeImageListActivity;

import java.util.ArrayList;

public class GoodsInfoGoodsImgsRecyclerAdapter extends RecyclerView.Adapter<GoodsInfoGoodsImgsRecyclerAdapter.ViewHolder> {
    private ArrayList<String> list;
    private Context context;

    public void setList(ArrayList<String> imglist) {
        this.list = imglist;
        list.add("https://c-ssl.duitang.com/uploads/item/201809/01/20180901173912_HVuyQ.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201607/17/20160717215208_KEAHT.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201908/09/20190809165642_fkdno.jpg");
        notifyDataSetChanged();
    }

    public GoodsInfoGoodsImgsRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsimgImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsimgImageView = itemView.findViewById(R.id.goodsimgImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodsinfo_goodsimgs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.goodsimgImageView.setImageDrawable(resource);
            }
        });
        holder.goodsimgImageView.setOnClickListener(new OnImageClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnImageClickListener implements View.OnClickListener {
        private int position;

        public OnImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LargeImageListActivity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("imgList", list);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
