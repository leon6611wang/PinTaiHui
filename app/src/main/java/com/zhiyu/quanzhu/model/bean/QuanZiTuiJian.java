package com.zhiyu.quanzhu.model.bean;

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
    private boolean isSetVideoUrl=false;

    private int type = -1;
    private QuanZiTuiJianContent content;
    private List<QuanZiTuiJianQuanZi> quanzi;


    public QuanZiTuiJian(int type,String videoCover, String videoUrl) {
        this.type=type;
        this.videoCover = videoCover;
        this.videoUrl = videoUrl;
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
