package com.zhiyu.quanzhu.model.bean;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;

import java.util.List;

public class QuanZiTuiJian {
    //视频地址
    private String videoUrl;
    //视频封面图片
    private String videoCover;
    //是否已经布局
    private boolean isLayout;
    //视频是否播放
    private boolean isPlay = false;
    private boolean isSetVideoUrl = false;

    private int type = -1;
    private int feed_type;
    private QuanZiTuiJianContent content;
    private List<QuanZiTuiJianQuanZi> quanzi;

    /**
     * 卡片布局参数
     */
    private LinearLayout.LayoutParams cardViewParams;
    /**
     * list 子项item根布局参数
     */
    private ViewGroup.LayoutParams itemLayoutParams;
    /**
     * 视频布局参数
     */
    private FrameLayout.LayoutParams videoParams;

    public LinearLayout.LayoutParams getCardViewParams(Context context, int width, int height) {
        if (null == cardViewParams) {
            int dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
            int dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
            cardViewParams = new LinearLayout.LayoutParams(width, height);
            cardViewParams.topMargin = dp_10;
            cardViewParams.bottomMargin = dp_10;
            cardViewParams.leftMargin = dp_5;
            cardViewParams.rightMargin = dp_5;
        }
        return cardViewParams;
    }

    public ViewGroup.LayoutParams getItemLayoutParams(Context context, int width) {
        if (null == itemLayoutParams) {
            int dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
            itemLayoutParams = new ViewGroup.LayoutParams(width + dp_5 * 2, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return itemLayoutParams;
    }

    public FrameLayout.LayoutParams getVideoParams(Context context, int width, int height) {
        if (null == videoParams) {
            int videoWidth = getContent().getVideo_width();
            int videoHeight = getContent().getVideo_height();
            if (videoWidth >= videoHeight) {
                int params_height = Math.round(width * videoHeight / videoWidth);
                videoParams = new FrameLayout.LayoutParams(width, params_height);
            } else {
                float videoRatio = (float) videoHeight / (float) videoWidth;
                float ratio = (float) height / (float) width;
                if (videoRatio >= ratio) {
                    int params_width = Math.round(height * videoWidth / videoHeight);
                    videoParams = new FrameLayout.LayoutParams(params_width, height);
                } else {
                    int params_height = Math.round(width * videoHeight / videoWidth);
                    videoParams = new FrameLayout.LayoutParams(width, params_height);
                }
            }
            videoParams.gravity = Gravity.CENTER;
        }
        return videoParams;
    }

    private FrameLayout.LayoutParams feedParams = null;

    public FrameLayout.LayoutParams getFeedParams(int screenWidth, int cardWidth, int dp240_, boolean isVideo) {
        if (null == feedParams) {
            int dp240 = Math.round(((float) cardWidth / (float) screenWidth) * dp240_);
            int width;
            int height;
            if (isVideo) {
                width = getContent().getVideo_width();
                height = getContent().getVideo_height();
            } else {
                width = getContent().getImgs().get(0).getWidth();
                height = getContent().getImgs().get(0).getHeight();
            }
            if (width >= height) {
                if (width >= dp240) {
                    int height_ = Math.round(dp240 * ((float) height / (float) width));
                    feedParams = new FrameLayout.LayoutParams(dp240, height_);
                } else {
                    int height_ = Math.round(width * ((float) height / (float) width));
                    feedParams = new FrameLayout.LayoutParams(width, height_);
                }
            } else {
                if (height >= dp240) {
                    int width_ = Math.round(dp240 * ((float) width / (float) height));
                    feedParams = new FrameLayout.LayoutParams(width_, dp240);
                } else {
                    int width_ = Math.round(height * ((float) width / (float) height));
                    feedParams = new FrameLayout.LayoutParams(width_, height);
                }
            }
        }
        return feedParams;
    }


    public QuanZiTuiJian(int type, String videoCover, String videoUrl) {
        this.type = type;
        this.videoCover = videoCover;
        this.videoUrl = videoUrl;
    }

    public int getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(int feed_type) {
        this.feed_type = feed_type;
    }

    public boolean isSetVideoUrl() {
        return isSetVideoUrl;
    }

    public void setSetVideoUrl(boolean setVideoUrl) {
        isSetVideoUrl = setVideoUrl;
    }

    public QuanZiTuiJianContent getContent() {
        return content;
    }

    public void setContent(QuanZiTuiJianContent content) {
        this.content = content;
    }

    public List<QuanZiTuiJianQuanZi> getQuanzi() {
        return quanzi;
    }

    public void setQuanzi(List<QuanZiTuiJianQuanZi> quanzi) {
        this.quanzi = quanzi;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public boolean isLayout() {
        return isLayout;
    }

    public void setLayout(boolean layout) {
        isLayout = layout;
    }
}
