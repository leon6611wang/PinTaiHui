package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class ImageGridRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_ADD = 2;
    private List<String> list;
    private Context context;

    public ImageGridRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> img_list) {
        this.list = img_list;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        RoundImageView mImageView;
        ImageView mDeleteImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mDeleteImageView = itemView.findViewById(R.id.mDeleteImageView);
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;

        public AddViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).equals("add")) {
            return TYPE_ADD;
        } else {
            return TYPE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_grid, parent, false));
        } else if (viewType == TYPE_ADD) {
            return new AddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_grid_add, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder myHolder = (ImageViewHolder) holder;
            Glide.with(context).load(list.get(position)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (resource instanceof GifDrawable) {
                        ((GifDrawable) resource).setLoopCount(0);
                    }
                    return false;
                }
            }).into(myHolder.mImageView);
            myHolder.mDeleteImageView.setOnClickListener(new OnDeleteClickListener(position));
        } else if (holder instanceof AddViewHolder) {
            AddViewHolder myHolder = (AddViewHolder) holder;
            myHolder.mLinearLayout.setOnClickListener(new ChooseImageListener());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ChooseImageListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null != onAddImageListener) {
                onAddImageListener.onAddImage();
            }
        }
    }

    private OnAddImageListener onAddImageListener;

    public void setOnAddImageListener(OnAddImageListener listener) {
        this.onAddImageListener = listener;
    }

    public interface OnAddImageListener {
        void onAddImage();
    }

    private class OnDeleteClickListener implements View.OnClickListener {
        private int index;

        public OnDeleteClickListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onDeleteImageListener) {
                onDeleteImageListener.onDelete(index);
            }
        }
    }

    private OnDeleteImageListener onDeleteImageListener;

    public void setOnDeleteImageListener(OnDeleteImageListener listener) {
        this.onDeleteImageListener = listener;
    }

    public interface OnDeleteImageListener {
        void onDelete(int position);
    }
}
