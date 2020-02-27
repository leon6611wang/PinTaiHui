package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.recyclerTouchHelper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PublishFeedImagesGridAdapter extends RecyclerView.Adapter<PublishFeedImagesGridAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<String> list = new ArrayList<>();
    private LinearLayout.LayoutParams ll;
    private int screenWidth, dp_15, dp_5, image_width;


    public PublishFeedImagesGridAdapter(Context context) {
        this.context = context;
        initDialog();
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        image_width = (screenWidth - dp_15 * 2 - dp_5 * 2) / 3;
        ll = new LinearLayout.LayoutParams(image_width, image_width);
        ll.rightMargin = dp_5;
        ll.bottomMargin = dp_5;
    }

    private void initDialog() {

    }

    public void setData(ArrayList<String> img_list) {
        this.list = img_list;
        notifyDataSetChanged();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == list.size() - 1) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setLayoutParams(ll);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_feed_imageview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).equals("add")) {
            Glide.with(context).load(R.mipmap.mingpian_add_img).into(holder.imageView);
            holder.imageView.setOnClickListener(new OnSelectImagesListener());
        } else {
            Glide.with(context).load(list.get(position)).into(holder.imageView);
            holder.imageView.setOnClickListener(new OnDeleteImageClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    public class OnSelectImagesListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null != onAddImagesListener) {
                onAddImagesListener.onAddImages();
            }
        }
    }

    private OnAddImagesListener onAddImagesListener;

    public void setOnAddImagesListener(OnAddImagesListener listener) {
        this.onAddImagesListener = listener;
    }

    public interface OnAddImagesListener {
        void onAddImages();
    }

    private class OnDeleteImageClick implements View.OnClickListener {
        private int position;

        public OnDeleteImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            deleteImageDialog.show();
            if (null != onDeleteImageListener) {
                onDeleteImageListener.onDeleteImage(position);
            }
        }
    }

    private OnDeleteImageListener onDeleteImageListener;

    public void setOnDeleteImageListener(OnDeleteImageListener listener) {
        this.onDeleteImageListener = listener;
    }

    public interface OnDeleteImageListener {
        void onDeleteImage(int position);
    }
}
