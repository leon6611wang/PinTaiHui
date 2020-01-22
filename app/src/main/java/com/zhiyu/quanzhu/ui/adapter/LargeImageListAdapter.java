package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class LargeImageListAdapter extends RecyclerView.Adapter<LargeImageListAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    private LinearLayout.LayoutParams ll;

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LargeImageListAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int screenHeight = ScreentUtils.getInstance().getScreenHeight(context);
        ll = new LinearLayout.LayoutParams(screenWidth, screenHeight);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LargeImageView largeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            largeImageView = itemView.findViewById(R.id.largeImageView);
            largeImageView.setLayoutParams(ll);
            largeImageView.setEnabled(true);
            largeImageView.setHorizontalScrollBarEnabled(false);
            largeImageView.setVerticalScrollBarEnabled(false);
            largeImageView.setBackgroundColor(context.getColor(R.color.black));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_large_image_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(list.get(position))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.largeImageView.setImage(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
