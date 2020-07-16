package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.GoodsImg;
import com.zhiyu.quanzhu.ui.activity.LargeImageList2Activity;
import com.zhiyu.quanzhu.ui.activity.LargeImageListActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoodsInfoBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GoodsImg> list;
    private final int VIDEO = 1;
    private final int IMG = 2;
    private Context context;
    private int screenWidth;
    private LinearLayout.LayoutParams ll;

    public void setList(List<GoodsImg> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public GoodsInfoBannerAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        ll = new LinearLayout.LayoutParams(screenWidth, screenWidth);
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        LinearLayout imageLayout;

        public ImgViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            imageLayout.setLayoutParams(ll);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout videoLayout;
        VideoPlayerTrackView videoPlayer;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            videoLayout = itemView.findViewById(R.id.videoLayout);
            videoLayout.setLayoutParams(ll);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isVideo()) {
            return VIDEO;
        } else {
            return IMG;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_info_banner_img, null);
            return new ImgViewHolder(view);
        } else if (viewType == VIDEO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_info_banner_video, null);
            return new VideoViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImgViewHolder) {
            ImgViewHolder myHolder = (ImgViewHolder) holder;
            myHolder.mImageView.setOnClickListener(new OnImageClick(position));
            myHolder.mImageView.setLayoutParams(list.get(position).getLayoutParams(screenWidth));
            Glide.with(context).load(list.get(position).getUrl())
                    //异常时候显示的图片
                    .error(R.drawable.image_error)
                    //加载成功前显示的图片
                    .placeholder(R.drawable.image_error)
                    //url为空的时候,显示的图片
                    .fallback(R.drawable.image_error)
                    .into(myHolder.mImageView);
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder myHolder = (VideoViewHolder) holder;
            myHolder.videoPlayer.setLayoutParams(list.get(position).getVideoParams(context));
            myHolder.videoPlayer.setDataSource(list.get(position).getUrl(), "");
            Glide.with(context).load(list.get(position).getUrl()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(myHolder.videoPlayer.getCoverController().getVideoCover());
        }
    }


    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnImageClick implements View.OnClickListener {
        int position;

        public OnImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LargeImageList2Activity.class);
            ArrayList<String> imgList = new ArrayList<>();
            boolean has_video = false;
            for (GoodsImg img : list) {
                if (img.getUrl().endsWith(".jpg") || img.getUrl().endsWith(".JPG") ||
                        img.getUrl().endsWith(".jpeg") || img.getUrl().endsWith(".JPEG") ||
                        img.getUrl().endsWith(".png") || img.getUrl().endsWith(".PNG") ||
                        img.getUrl().endsWith(".gif") || img.getUrl().endsWith(".GIF")) {
                    imgList.add(img.getUrl());
                } else {
                    has_video = true;
                }
            }
//            for(GoodsImg img:list){
//                imgList.add(img.getUrl());
//            }
            intent.putExtra("position", (has_video ? (position - 1) : position));
            intent.putStringArrayListExtra("imgList", imgList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
