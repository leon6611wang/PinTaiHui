package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class ShenSuShenSuImageRecyclerViewAdapter extends RecyclerView.Adapter<ShenSuShenSuImageRecyclerViewAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    private LinearLayout.LayoutParams ll;

    public ShenSuShenSuImageRecyclerViewAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int dp_6 = (int) context.getResources().getDimension(R.dimen.dp_6);
        int dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        int dp = Math.round((screenWidth - dp_6 * 2 - dp_15 * 2) / 3);
        ll = new LinearLayout.LayoutParams(dp, dp);
    }

    public void setList(List<String> imgList) {
        this.list = imgList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mImageView.setLayoutParams(ll);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_corner_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(list.get(position).equals("add")){
            Glide.with(context).load(R.mipmap.add_circle_img).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=onShenSuImageListener){
                        onShenSuImageListener.addImage();
                    }
                }
            });
        }else{
            Glide.with(context).load(list.get(position)).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LargeImageActivity.class);
                    intent.putExtra("imgUrl", list.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
    private OnShenSuImageListener onShenSuImageListener;
    public void setOnShenSuImageListener(OnShenSuImageListener listener){
        this.onShenSuImageListener=listener;
    }
    public interface OnShenSuImageListener {
        void addImage();
    }
}
