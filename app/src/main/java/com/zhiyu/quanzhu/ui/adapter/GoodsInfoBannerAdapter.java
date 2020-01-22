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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
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
    private List<String> list;
    private final int VIDEO = 1;
    private final int IMG = 2;
    private Context context;
    private LinearLayout.LayoutParams ll;
    private FrameLayout.LayoutParams fl;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private AudioManager mAudioManager;

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public GoodsInfoBannerAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        ll = new LinearLayout.LayoutParams(screenWidth, screenWidth);
        fl = new FrameLayout.LayoutParams(screenWidth, screenWidth);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImgViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mImageView.setLayoutParams(ll);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView mVideoView;
        FrameLayout videoLayout;
        ImageView playPauseImageView;
        SeekBar seekBar;
        TextView currentTimeTextView, totalTimeTextView;
        ImageView videoVoiceImageView;
        private Timer timer;
        private TimerTask task;
        private MyHandler myHandler = new MyHandler(this);
        private int count = 0;

        class MyHandler extends Handler {
            WeakReference<VideoViewHolder> holderWeakReference;

            public MyHandler(VideoViewHolder holder) {
                holderWeakReference = new WeakReference<>(holder);
            }

            @Override
            public void handleMessage(Message msg) {
                VideoViewHolder holder = holderWeakReference.get();
                switch (msg.what) {
                    case 0:
                        count++;
                        int size = mVideoView.getCurrentPosition();
                        currentTimeTextView.setText(sdf.format(size));
                        if (mVideoView.isPlaying()) {
                            long mMax = holder.mVideoView.getDuration();
                            totalTimeTextView.setText(sdf.format(mMax));
                            int sMax = seekBar.getMax();
                            seekBar.setProgress((int) (size * sMax / mMax));
                        }

                        if (currentTimeTextView.getVisibility() == View.VISIBLE && count == 5 && mVideoView.isPlaying()) {
                            currentTimeTextView.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            totalTimeTextView.setVisibility(View.GONE);
                            playPauseImageView.setVisibility(View.GONE);
                            videoVoiceImageView.setVisibility(View.GONE);
                            count = 0;
                        }
                        break;
                }
            }
        }

        public VideoViewHolder(View itemView) {
            super(itemView);
            mVideoView = itemView.findViewById(R.id.mVideoView);
            videoLayout = itemView.findViewById(R.id.videoLayout);
            videoLayout.setLayoutParams(fl);
            playPauseImageView = itemView.findViewById(R.id.playPauseImageView);
            seekBar = itemView.findViewById(R.id.seekBar);
            currentTimeTextView = itemView.findViewById(R.id.currentTimeTextView);
            totalTimeTextView = itemView.findViewById(R.id.totalTimeTextView);
            videoVoiceImageView = itemView.findViewById(R.id.videoVoiceImageView);
            if (null == timer) {
                timer = new Timer();
            }
            if (null == task) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = myHandler.obtainMessage(0);
                        message.sendToTarget();
                    }
                };
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).endsWith(".jpg") || list.get(position).endsWith(".JPG") || list.get(position).endsWith(".jpeg") || list.get(position).endsWith(".JPEG") || list.get(position).endsWith(".png") || list.get(position).endsWith(".PNG")) {
            return IMG;
        } else {
            return VIDEO;
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
            Glide.with(context).load(list.get(position))
                    //异常时候显示的图片
                    .error(R.mipmap.img_error)
                    //加载成功前显示的图片
                    .placeholder(R.mipmap.img_loading)
                    //url为空的时候,显示的图片
                    .fallback(R.mipmap.img_error)
                    .into(myHolder.mImageView);
        } else if (holder instanceof VideoViewHolder) {
            final VideoViewHolder myHolder = (VideoViewHolder) holder;
            Field field;
            try {
                field = TimerTask.class.getDeclaredField("state");
                field.setAccessible(true);
                field.set(myHolder.task, 0);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            myHolder.timer.schedule(myHolder.task, 0, 1000);
            myHolder.mVideoView.setVideoPath(list.get(position));
            myHolder.playPauseImageView.setVisibility(View.VISIBLE);
            myHolder.currentTimeTextView.setVisibility(View.VISIBLE);
            myHolder.totalTimeTextView.setVisibility(View.VISIBLE);
            myHolder.seekBar.setVisibility(View.VISIBLE);
            myHolder.videoVoiceImageView.setVisibility(View.VISIBLE);
            myHolder.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    myHolder.playPauseImageView.setVisibility(View.GONE);
                    myHolder.currentTimeTextView.setVisibility(View.GONE);
                    myHolder.totalTimeTextView.setVisibility(View.GONE);
                    myHolder.seekBar.setVisibility(View.GONE);
                    myHolder.videoVoiceImageView.setVisibility(View.GONE);
                }
            });
            /**
             * 视频播放完成时回调
             */
            myHolder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    myHolder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_pause));
                    myHolder.playPauseImageView.setVisibility(View.VISIBLE);
                }
            });

            myHolder.playPauseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myHolder.mVideoView.isPlaying()) {
                        myHolder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_pause));
                        myHolder.mVideoView.pause();
                    } else {
                        myHolder.mVideoView.start();
                        myHolder.playPauseImageView.setVisibility(View.GONE);
                        myHolder.currentTimeTextView.setVisibility(View.GONE);
                        myHolder.totalTimeTextView.setVisibility(View.GONE);
                        myHolder.seekBar.setVisibility(View.GONE);
                        myHolder.videoVoiceImageView.setVisibility(View.GONE);
                    }
                }
            });
            myHolder.mVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (myHolder.mVideoView.isPlaying()) {
                        myHolder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_play));
                    } else {
                        myHolder.playPauseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_pause));
                    }
                    myHolder.playPauseImageView.setVisibility(View.VISIBLE);
                    myHolder.currentTimeTextView.setVisibility(View.VISIBLE);
                    myHolder.totalTimeTextView.setVisibility(View.VISIBLE);
                    myHolder.seekBar.setVisibility(View.VISIBLE);
                    myHolder.videoVoiceImageView.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            myHolder.videoVoiceImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前音乐多媒体是否静音
                    boolean muteFlag = mAudioManager.isStreamMute(AudioManager.STREAM_MUSIC);
                    if (muteFlag) {
                        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);//设为静音
                        myHolder.videoVoiceImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_voice));
                    } else {
                        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);//取消静音
                        myHolder.videoVoiceImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.goods_info_unvoice));
                    }
                }
            });
            myHolder.mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    myHolder.mVideoView.stopPlayback(); //播放异常，则停止播放，防止弹窗使界面阻塞
                    return true;
                }
            });
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
            Intent intent = new Intent(context, LargeImageListActivity.class);
            ArrayList<String> imgList = new ArrayList<>();
            boolean has_video = false;
            for (String url : list) {
                if (url.endsWith(".jpg") || url.endsWith(".JPG") || url.endsWith(".jpeg") || url.endsWith(".JPEG") || url.endsWith(".png") || url.endsWith(".PNG")) {
                    imgList.add(url);
                } else {
                    has_video = true;
                }
            }
            intent.putExtra("position", (has_video ? (position - 1) : position));
            intent.putStringArrayListExtra("imgList", imgList);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
