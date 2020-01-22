package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.QuanZiGuanZhu;
import com.zhiyu.quanzhu.ui.activity.DongTaiInformationActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MySeekBar;
import com.zhiyu.quanzhu.ui.widget.MyTextureView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.VideoPlayer;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class QuanZiGuanZhuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<QuanZiGuanZhu> list;
    private int screenWidth, dp_30, width, height;
    private LinearLayout.LayoutParams ll;
    private int currentPosition = -1;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM:ss");
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<QuanZiGuanZhuAdapter> adapterWeakReference;

        public MyHandler(QuanZiGuanZhuAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            QuanZiGuanZhuAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    ImageView collectImageView = (ImageView) msg.obj;
                    if (adapter.list.get(adapter.currentPosition).getContent().isIs_collect()) {
                        collectImageView.setImageDrawable(adapter.context.getResources().getDrawable(R.mipmap.shoucang_gray));
                        adapter.list.get(adapter.currentPosition).getContent().setIs_collect(false);
                        Toast.makeText(adapter.context, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        collectImageView.setImageDrawable(adapter.context.getResources().getDrawable(R.mipmap.shoucang_yellow));
                        adapter.list.get(adapter.currentPosition).getContent().setIs_collect(true);
                        Toast.makeText(adapter.context, "收藏成功", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 2:
                    System.out.println("currentPosition: " + adapter.currentPosition + " , list size:　" + adapter.list.size());
                    adapter.list.remove(adapter.currentPosition);
//                    adapter.notifyItemChanged(adapter.currentPosition);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(adapter.context, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public QuanZiGuanZhuAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
//        ll=new LinearLayout.LayoutParams()
    }

    public void setData(List<QuanZiGuanZhu> guanZhuList) {
        this.list = guanZhuList;
        notifyDataSetChanged();
    }

    /**
     * 文章
     */
    class ArticleHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout articleContentLayout;
        NiceImageView dongtaiImageView;

        public ArticleHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            articleContentLayout = itemView.findViewById(R.id.articleContentLayout);
            dongtaiImageView = itemView.findViewById(R.id.dongtaiImageView);
        }
    }

    /**
     * 文字-单图
     */
    class TextImageHolder extends RecyclerView.ViewHolder {
        ExpandableTextView mTextView;
        NiceImageView mImageView;

        public TextImageHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mTextView = itemView.findViewById(R.id.mTextView);
        }
    }

    /**
     * 文字-多图
     */
    class TextImagesHolder extends RecyclerView.ViewHolder {
        MyRecyclerView labelRecyclerView;
        LabelRecyclerAdapter adapter;
        MyGridView imageGridView;
        ImageGridAdapter imageGridAdapter;
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseTextView;
        ImageView collectImageView;
        ExpandableTextView mTextView;
        LinearLayout deleteLayout;

        public TextImagesHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            mTextView = itemView.findViewById(R.id.mTextView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            collectImageView = itemView.findViewById(R.id.collectImageView);
            shareTextView = itemView.findViewById(R.id.shareTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            priseTextView = itemView.findViewById(R.id.priseTextView);
            deleteLayout = itemView.findViewById(R.id.deleteLayout);
            labelRecyclerView = itemView.findViewById(R.id.labelRecyclerView);
            imageGridView = itemView.findViewById(R.id.imageGridView);
            imageGridAdapter = new ImageGridAdapter(context);
            imageGridView.setAdapter(imageGridAdapter);
        }
    }

    /**
     * 文字-视频
     */
    class TextVideoHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseTextView;
        ImageView collectImageView;
        ExpandableTextView mTextView;
        LinearLayout deleteLayout;
        VideoPlayer mVideoPlayer;
        MyTextureView myTextureView;
        LinearLayout controllerLayout;
        ImageView playPauseImageView;
        TextView currentTimeTextView, totalTimeTextView;
        MySeekBar seekBar;

        public TextVideoHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            mTextView = itemView.findViewById(R.id.mTextView);
            mVideoPlayer = itemView.findViewById(R.id.mVideoPlayer);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            collectImageView = itemView.findViewById(R.id.collectImageView);
            shareTextView = itemView.findViewById(R.id.shareTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            priseTextView = itemView.findViewById(R.id.priseTextView);
            deleteLayout = itemView.findViewById(R.id.deleteLayout);
            myTextureView = itemView.findViewById(R.id.myTextureView);
            controllerLayout = itemView.findViewById(R.id.controllerLayout);
            playPauseImageView = itemView.findViewById(R.id.playPauseImageView);
            currentTimeTextView = itemView.findViewById(R.id.currentTimeTextView);
            totalTimeTextView = itemView.findViewById(R.id.totalTimeTextView);
            seekBar = itemView.findViewById(R.id.seekBar);
        }
    }

    /**
     * 你可能感兴趣的圈子
     */
    class InterestCirleHolder extends RecyclerView.ViewHolder {
        public InterestCirleHolder(View itemView) {
            super(itemView);
        }
    }

    //文章：1，横视频：2，竖视频：3，多图：4，单图-有文字：5，单图-无文字：6，兴趣：7
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View article = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_zi_guanzhu_article, parent, false);
            return new ArticleHolder(article);
        } else if (viewType == 2 || viewType == 3) {
            View video = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_zi_guanzhu_text_video, parent, false);
            return new TextVideoHolder(video);
        } else if (viewType == 4 || viewType == 5 || viewType == 6) {
            View images = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_zi_guanzhu_text_images, parent, false);
            return new TextImagesHolder(images);
//        } else if (viewType == 5 || viewType == 6) {
//            View image = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_zi_guanzhu_text_image, parent, false);
//            return new TextImageHolder(image);
        } else if (viewType == 7) {
            View interest = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_zi_guanzhu_interest, parent, false);
            return new InterestCirleHolder(interest);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myHolder, final int position) {
        if (myHolder instanceof ArticleHolder) {
            ArticleHolder holder = (ArticleHolder) myHolder;

        } else if (myHolder instanceof TextImageHolder) {
            TextImageHolder holder = (TextImageHolder) myHolder;
            holder.mTextView.setText(list.get(position).getContent().getContent());
//            if (null != list.get(position).getContent().getImgs() && list.get(position).getContent().getImgs().size() > 0) {
//                Glide.with(context).load(list.get(position).getContent().getImgs().get(0).getThumb_file()).into(holder.mImageView);
//            }
//            else {
//                holder.mImageView.setVisibility(View.GONE);
//            }
        } else if (myHolder instanceof TextImagesHolder) {
            TextImagesHolder holder = (TextImagesHolder) myHolder;
            Glide.with(context).load(list.get(position).getContent().getAvatar()).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getContent().getUsername());
            holder.timeTextView.setText(list.get(position).getContent().getTime());
            if (!TextUtils.isEmpty(list.get(position).getContent().getContent())) {
                holder.mTextView.setVisibility(View.VISIBLE);
                holder.mTextView.setText(list.get(position).getContent().getContent());
            } else {
                holder.mTextView.setVisibility(View.GONE);
            }
            holder.mTextView.setOnClickListener(new OnInformationClick(position,"image"));
            holder.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            holder.imageGridAdapter.setData(list.get(position).getContent().getImgs());
            holder.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
            holder.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
            holder.priseTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
            if (list.get(position).getContent().isIs_collect()) {
                holder.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
            } else {
                holder.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_gray));
            }
            holder.collectImageView.setOnClickListener(new OnCollectClick(position, holder.collectImageView));
            holder.deleteLayout.setOnClickListener(new OnDeleteClick(position));
        } else if (myHolder instanceof TextVideoHolder) {
            final TextVideoHolder holder = (TextVideoHolder) myHolder;
            final String video_url = list.get(position).getContent().getVideo_url();
            if (list.get(position).getContent().isIs_video_play()) {
                holder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_play));
            } else {
                holder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_pause));
            }

            holder.playPauseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("点击 播放/暂停 按钮");
                    if (!list.get(position).getContent().isIs_video_url_has()) {
                        System.out.println("注入视频URL");
//                        BaseApplication.getInstance().getMediaPlayer().reset();
//                        holder.myTextureView.setmMediaPlayer(BaseApplication.getInstance().getMediaPlayer(), new MyTextureView.OnCurrentListener() {
//                            @Override
//                            public void onCurrent(int currentPosition, int totalPosition, boolean isPlay) {
//                                System.out.println("currentPosition: " + currentPosition + " , totalPosition: " + totalPosition);
//                            }
//                        });
//                        holder.myTextureView.setPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mp) {
//                                holder.myTextureView.startPlay();
//                            }
//                        });
//                        holder.myTextureView.setUrl(video_url);
                    } else {
                        System.out.println("已有视频源，开始播放/暂停");
                        if (list.get(position).getContent().isIs_video_play()) {
                            holder.myTextureView.pausePlay();
                        } else {
                            holder.myTextureView.startPlay();
                        }

                    }
                }
            });
            Glide.with(context).load(list.get(position).getContent().getAvatar()).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getContent().getUsername());
            holder.timeTextView.setText(list.get(position).getContent().getTime());
            if (!TextUtils.isEmpty(list.get(position).getContent().getContent())) {
                holder.mTextView.setVisibility(View.VISIBLE);
                holder.mTextView.setText(list.get(position).getContent().getContent());
            } else {
                holder.mTextView.setVisibility(View.GONE);
            }
            holder.mTextView.setOnClickListener(new OnInformationClick(position,"video"));
            holder.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            holder.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
            holder.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
            holder.priseTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
            if (list.get(position).getContent().isIs_collect()) {
                holder.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
            } else {
                holder.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_gray));
            }
            holder.collectImageView.setOnClickListener(new OnCollectClick(position, holder.collectImageView));
            holder.deleteLayout.setOnClickListener(new OnDeleteClick(position));
            holder.playPauseImageView.setOnClickListener(new OnVideoPlayPauseClick(position, holder.myTextureView,
                    holder.currentTimeTextView, holder.totalTimeTextView, holder.seekBar));
        } else if (myHolder instanceof InterestCirleHolder) {
            InterestCirleHolder holder = (InterestCirleHolder) myHolder;

        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    //关注
    private class OnCollectClick implements View.OnClickListener {
        private int position;
        private ImageView collectImage;

        public OnCollectClick(int position, ImageView iv) {
            this.position = position;
            this.collectImage = iv;
        }

        @Override
        public void onClick(View v) {
            currentPosition = position;
            Message message = myHandler.obtainMessage(1);
            message.obj = collectImage;
            message.sendToTarget();
        }
    }

    //删除
    private class OnDeleteClick implements View.OnClickListener {
        private int position;

        public OnDeleteClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            currentPosition = position;
            Message message = myHandler.obtainMessage(2);
            message.sendToTarget();
        }
    }

    public class OnVideoPlayPauseClick implements View.OnClickListener {
        private int position;
        private MyTextureView myTextureView;
        private TextView currentText, totalText;
        private MySeekBar seekBar;

        public OnVideoPlayPauseClick(int position, MyTextureView myTextureView, TextView currentText, TextView totalText, MySeekBar seekBar) {
            this.position = position;
            this.myTextureView = myTextureView;
            this.currentText = currentText;
            this.totalText = totalText;
            this.seekBar = seekBar;
        }

        @Override
        public void onClick(View v) {
            if (!list.get(position).getContent().isIs_video_url_has()) {
                list.get(position).getContent().setIs_video_url_has(true);
                System.out.println("初始化");
                BaseApplication.getInstance().getMediaPlayer().reset();
                myTextureView.setmMediaPlayer(BaseApplication.getInstance().getMediaPlayer(), new MyTextureView.OnCurrentListener() {
                    @Override
                    public void onCurrent(int currentPosition, int totalPosition, boolean isPlay) {
                        int sMax = 0;
                        if (sMax == 0) {
                            sMax = seekBar.getMax();
                        }
                        seekBar.setProgress(currentPosition * sMax / totalPosition);
                        list.get(position).getContent().setIs_video_play(isPlay);
                        currentText.setText(sdf.format(currentPosition));
                        totalText.setText(sdf.format(totalPosition));
                        System.out.println("currentPosition: " + currentPosition + " , totalPosition: " + totalPosition);
                    }
                });
                myTextureView.setPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        list.get(position).getContent().setIs_video_play(true);
                        myTextureView.startPlay();
                    }
                });
                myTextureView.setUrl(list.get(position).getContent().getVideo_url());
            } else {
                if (list.get(position).getContent().isIs_video_play()) {
                    System.out.println("暂停");
                    myTextureView.pausePlay();
                    list.get(position).getContent().setIs_video_play(false);
                } else {
                    System.out.println("播放");
                    myTextureView.startPlay();
                    list.get(position).getContent().setIs_video_play(true);
                }
            }
        }
    }

    private void addVideoPlayer(LinearLayout videoLayout, String url) {
//        if (videoLayout.getChildCount() > 0) {
//            videoLayout.removeAllViews();
//        }

        int video_width, video_height;
        int screenWidth, dp_15;
        float ratio = 1.778f;
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        video_width = screenWidth - dp_15 * 2;
        video_height = Math.round(video_width / ratio);
        ll = new LinearLayout.LayoutParams(video_width, video_height);
        VideoPlayer mVideoPlayer2 = new VideoPlayer(context, video_width, video_height);
        mVideoPlayer2.setLayoutParams(ll);
        videoLayout.addView(mVideoPlayer2);
        mVideoPlayer2.setVideoUrl(url);
    }


    private class OnInformationClick implements View.OnClickListener{
        private int position;
        private String type;
        public OnInformationClick(int position,String feed_type) {
            this.position = position;
            this.type=feed_type;
        }

        @Override
        public void onClick(View v) {
            Intent infoIntent=new Intent(context, DongTaiInformationActivity.class);
            infoIntent.putExtra("feed_id",list.get(position).getContent().getFeed_id());
            infoIntent.putExtra("feed_type",type);
            infoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(infoIntent);
        }
    }
}
