package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianImg;
import com.zhiyu.quanzhu.ui.activity.DongTaiVideoPlayerActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class ItemItemQuanZiTuiJianDuoTuAdapter extends RecyclerView.Adapter<ItemItemQuanZiTuiJianDuoTuAdapter.ViewHolder> {
    private List<QuanZiTuiJianImg> list;
    private Activity activity;
    private LinearLayout.LayoutParams ll;
    private int dp_40;

    public ItemItemQuanZiTuiJianDuoTuAdapter(Activity aty, int card_width) {
        this.activity = aty;
        dp_40 = (int) activity.getResources().getDimension(R.dimen.dp_40);
        int img_width = Math.round((card_width - dp_40) / 3);
        int img_height = img_width;
        ll = new LinearLayout.LayoutParams(img_width, img_height);
    }

    public void setList(List<QuanZiTuiJianImg> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_quanzi_tuijian_duotu, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mImageView.setLayoutParams(ll);
        Glide.with(activity).load(list.get(position).getThumb_file())
                //异常时候显示的图片
                .error(R.mipmap.img_error)
                //加载成功前显示的图片
                .placeholder(R.mipmap.img_loading)
                //url为空的时候,显示的图片
                .fallback(R.mipmap.img_error)
                .into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.mImageView, "img");
//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(holder.mImageView, holder.mImageView.getWidth() / 2, holder.mImageView.getHeight() / 2, 0, 0);
                Intent intent = new Intent(activity, LargeImageActivity.class);
                intent.putExtra("imgUrl", list.get(position).getFile());
                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


}
