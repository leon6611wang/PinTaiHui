package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.ui.activity.DongTaiVideoPlayerActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.CircleProgressView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.MD5Utils;
import com.zhiyu.quanzhu.utils.VideoCacheUtils;

import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

public class QuanZiTuiJianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private String videoPath = "https://flv3.bn.netease.com/c5d39796715c6dffb63a5f6d63a890ebab8bae6091712a37190db88b8991570b0f3b7c5435281288e959fa3b8990ebca3318e57662bd830ec45b8cfea9f72cfbc39b878df01eaf302e40097222a8c8374aaf53328bd68d5f230289a05ed6d3d3141b3ce6fb4c36b063bad28584c5d45bb31c4d64faff7a4b.mp4";
    private int cardWidth, cardHeight;
    private LinearLayout.LayoutParams cardLayoutParams, bottomCardLayoutParams, dantuhastextLayoutParams;
    private FrameLayout.LayoutParams contentLayoutParams, dantunotextLayoutParams;
    private ViewGroup.LayoutParams vl;
    private List<QuanZiTuiJian> list;
    private int dp_60, dp_10, dp_5, dp_240;
    private float dantu_ratio = 0.73846f;
    private int dantu_width, dantu_height;

    private int WEN_ZHANG = 1;//文章
    private int VIDEO_H = 2;//横视频
    private int VIDEO_V = 3;//竖视频
    private int DUO_TU = 4;//多图
    private int DAN_TU_HAS_TEXT = 5;//单图-有文字
    private int DAN_TU_NO_TEXT = 6;//单图-无文字
    private int XING_QU = 7;//感兴趣的圈子


    public QuanZiTuiJianAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setWidthHeight(int card_width, int card_height) {
        this.cardWidth = card_width;
        this.cardHeight = card_height;
        dp_5 = (int) activity.getResources().getDimension(R.dimen.dp_5);
        dp_10 = (int) activity.getResources().getDimension(R.dimen.dp_10);
        dp_60 = (int) activity.getResources().getDimension(R.dimen.dp_60);
        cardLayoutParams = new LinearLayout.LayoutParams(cardWidth, cardHeight);
        contentLayoutParams = new FrameLayout.LayoutParams(card_width, card_height);
        dantu_width = Math.round(dantu_ratio * cardWidth);
        dantu_height = dantu_width;

        cardLayoutParams.topMargin = dp_10;
        cardLayoutParams.bottomMargin = dp_10;
        cardLayoutParams.leftMargin = dp_5;
        cardLayoutParams.rightMargin = dp_5;
        bottomCardLayoutParams = new LinearLayout.LayoutParams(card_width, dp_60);
        bottomCardLayoutParams.bottomMargin = dp_10;
        bottomCardLayoutParams.leftMargin = dp_5;
        bottomCardLayoutParams.rightMargin = dp_5;
        vl = new ViewGroup.LayoutParams(card_width + dp_5 * 2, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setData(List<QuanZiTuiJian> tuiJianList) {
        this.list = tuiJianList;
    }


    /**
     * 文章
     */
    class WenZhangViewHolder extends RecyclerView.ViewHolder {
        WebView mWebView;
        CardView mCardView;
        LinearLayout itemRootLayout;

        public WenZhangViewHolder(View itemView) {
            super(itemView);
            mWebView = itemView.findViewById(R.id.wenzhangWebView);
            mCardView = itemView.findViewById(R.id.wenzhangCardView);
            itemRootLayout = itemView.findViewById(R.id.wenzhangItemRootLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);

            // 设置WebView的客户端
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;// 返回false
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            // 让WebView能够执行javaScript
            webSettings.setJavaScriptEnabled(true);
            // 让JavaScript可以自动打开windows
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            // 设置缓存
            webSettings.setAppCacheEnabled(true);
            // 设置缓存模式,一共有四种模式
            webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
            // 设置缓存路径
//        webSettings.setAppCachePath("");
            // 支持缩放(适配到当前屏幕)
            webSettings.setSupportZoom(true);
            // 将图片调整到合适的大小
            webSettings.setUseWideViewPort(true);
            // 支持内容重新布局,一共有四种方式
            // 默认的是NARROW_COLUMNS
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            // 设置可以被显示的屏幕控制
            webSettings.setDisplayZoomControls(true);
            // 设置默认字体大小
            webSettings.setDefaultFontSize(12);

            // 设置WebView属性，能够执行Javascript脚本
            // mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    /**
     * 多图
     */
    class DuoTuViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        ItemItemQuanZiTuiJianDuoTuAdapter adapter;
        GridLayoutManager gridLayoutManager;
        GridSpacingItemDecoration gridSpacingItemDecoration;
        CardView mCardView;
        LinearLayout itemRootLayout;
        CircleImageView duotuHeaderImageView;
        TextView duotuContentTextView, duotuShopNameTextView, duotuTimeTextView;

        public DuoTuViewHolder(View itemView) {
            super(itemView);
            duotuHeaderImageView = itemView.findViewById(R.id.duotuHeaderImageView);
            duotuContentTextView = itemView.findViewById(R.id.duotuContentTextView);
            duotuShopNameTextView = itemView.findViewById(R.id.duotuShopNameTextView);
            duotuTimeTextView = itemView.findViewById(R.id.duotuTimeTextView);
            mRecyclerView = itemView.findViewById(R.id.duotuRecyclerView);
            adapter = new ItemItemQuanZiTuiJianDuoTuAdapter(activity, cardWidth);
            gridLayoutManager = new GridLayoutManager(activity, 3);
            gridSpacingItemDecoration = new GridSpacingItemDecoration(3, dp_5, false);
            mCardView = itemView.findViewById(R.id.duotuCardView);
            itemRootLayout = itemView.findViewById(R.id.duotuItemRootLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            mRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * 单图-有文字
     */
    class DanTuHasTextViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        RoundImageView contentImageView;
        CardView mCardView;
        LinearLayout itemRootLayout;

        public DanTuHasTextViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.dantuhastextContentTextView);
            contentImageView = itemView.findViewById(R.id.dantuhastextContentImageView);
            mCardView = itemView.findViewById(R.id.dantuhastextCardView);
            itemRootLayout = itemView.findViewById(R.id.dantuhastextItemRootLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);
        }
    }

    /**
     * 单图-无文字
     */
    class DanTuNoTextViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        CardView mCardView;
        LinearLayout itemRootLayout;

        public DanTuNoTextViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.dantunotextContentImageView);
            mCardView = itemView.findViewById(R.id.dantunotextCardView);
            itemRootLayout = itemView.findViewById(R.id.dantunotextItemRootLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);
        }
    }

    /**
     * 兴趣
     */
    class XingQuViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout itemRootLayout;
        RecyclerView xingquRecyclerView;
        ItemItemQuanZiTuiJianXingQuAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        LinearLayout xingquChangeLayout;

        public XingQuViewHolder(View itemView) {
            super(itemView);
            xingquRecyclerView = itemView.findViewById(R.id.xingquRecyclerView);
            mCardView = itemView.findViewById(R.id.xingquCardView);
            itemRootLayout = itemView.findViewById(R.id.xingquItemRootLayout);
            xingquChangeLayout = itemView.findViewById(R.id.xingquChangeLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);
            adapter = new ItemItemQuanZiTuiJianXingQuAdapter(activity, cardWidth);
            linearLayoutManager = new LinearLayoutManager(activity);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            xingquRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    /**
     * 视频-竖
     */
    class VideoVViewHolder extends RecyclerView.ViewHolder {
        VideoView videovVideoView;
        ImageView videovPlayImageView;
        ImageView videovCoverImageView;
        CardView videovCardView;
        LinearLayout videovItemRootLayout;
        LinearLayout videovBottomLayout;
        CircleProgressView videovHuanchongView;
        TextView videovContentTextView;
        CircleImageView videovHeaderImageView;
        TextView videovShopNameTextView, videovTimeTextView;

        public VideoVViewHolder(View itemView) {
            super(itemView);
            videovVideoView = itemView.findViewById(R.id.videovVideoView);
            videovPlayImageView = itemView.findViewById(R.id.videovPlayImageView);
            videovCoverImageView = itemView.findViewById(R.id.videovCoverImageView);
            videovContentTextView = itemView.findViewById(R.id.videovContentTextView);
            videovHeaderImageView = itemView.findViewById(R.id.videovHeaderImageView);
            videovShopNameTextView = itemView.findViewById(R.id.videovShopNameTextView);
            videovTimeTextView = itemView.findViewById(R.id.videovTimeTextView);
            videovCardView = itemView.findViewById(R.id.videovCardView);
//            bottomCardView = itemView.findViewById(R.id.bottomCardView);
            videovItemRootLayout = itemView.findViewById(R.id.videovItemRootLayout);
            videovItemRootLayout.setLayoutParams(vl);
            videovCardView.setLayoutParams(cardLayoutParams);
//            bottomCardView.setLayoutParams(bottomCardLayoutParams);
            videovCoverImageView.setLayoutParams(contentLayoutParams);
            videovVideoView.setLayoutParams(contentLayoutParams);
            videovHuanchongView = itemView.findViewById(R.id.videovHuanchongView);
            videovBottomLayout = itemView.findViewById(R.id.videovBottomLayout);
        }
    }

    /**
     * 视频-横
     */
    class ViewHViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout itemRootLayout;

        public ViewHViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.videohCardView);
            itemRootLayout = itemView.findViewById(R.id.videohItemRootLayout);
            itemRootLayout.setLayoutParams(vl);
            mCardView.setLayoutParams(cardLayoutParams);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == WEN_ZHANG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_wenzhang, parent, false);
            return new WenZhangViewHolder(view);

        } else if (viewType == VIDEO_H) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_video_h, parent, false);
            return new ViewHViewHolder(view);

        } else if (viewType == VIDEO_V) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_video_v, parent, false);
            return new VideoVViewHolder(view);

        } else if (viewType == DUO_TU) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_duotu, parent, false);
            return new DuoTuViewHolder(view);

        } else if (viewType == DAN_TU_HAS_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_dantu_has_text, parent, false);
            return new DanTuHasTextViewHolder(view);

        } else if (viewType == DAN_TU_NO_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_dantu_no_text, parent, false);
            return new DanTuNoTextViewHolder(view);

        } else if (viewType == XING_QU) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_xingqu, parent, false);
            return new XingQuViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, final int position) {
        if (mHolder instanceof WenZhangViewHolder) {
            WenZhangViewHolder holder = (WenZhangViewHolder) mHolder;
            holder.mWebView.loadUrl(list.get(position).getContent().getLink_url());
        } else if (mHolder instanceof DuoTuViewHolder) {
            DuoTuViewHolder holder = (DuoTuViewHolder) mHolder;

            holder.adapter.setList(list.get(position).getContent().getImgs());
            Glide.with(activity).load(list.get(position).getContent().getAvatar()).into(holder.duotuHeaderImageView);
            holder.duotuShopNameTextView.setText(list.get(position).getContent().getUsername());
            holder.duotuTimeTextView.setText(list.get(position).getContent().getTime());
            if (!TextUtils.isEmpty(list.get(position).getContent().getContent())) {
                holder.duotuContentTextView.setText(list.get(position).getContent().getContent());
                holder.duotuContentTextView.setVisibility(View.VISIBLE);
            } else {
                holder.duotuContentTextView.setVisibility(View.GONE);
            }

        } else if (mHolder instanceof DanTuHasTextViewHolder) {
            final DanTuHasTextViewHolder holder = (DanTuHasTextViewHolder) mHolder;
            holder.contentTextView.setText(list.get(position).getContent().getContent());
            if (null != list.get(position).getContent().getImgs() && list.get(position).getContent().getImgs().size() > 0) {
                if (list.get(position).getContent().getImgs().get(0).getWidth() >= list.get(position).getContent().getImgs().get(0).getHeight()) {
                    dantuhastextLayoutParams = new LinearLayout.LayoutParams(dantu_width, LinearLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    dantuhastextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dantu_width);
                }
                dantuhastextLayoutParams.leftMargin = dp_10 + dp_5;
                holder.contentImageView.setLayoutParams(dantuhastextLayoutParams);
                holder.contentImageView.setVisibility(View.VISIBLE);
                list.get(position).getContent().getImgs().get(0).setFile("http://pic1.win4000.com/wallpaper/1/57cd115d11c68.jpg");
                Glide.with(activity).load(list.get(position).getContent().getImgs().get(0).getFile())
                        //异常时候显示的图片
                        .error(R.mipmap.img_error)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_loading)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_error)
                        .into(holder.contentImageView);

                holder.contentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat optionsCompat =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.contentImageView, "img");
                        Intent intent = new Intent(activity, LargeImageActivity.class);
                        intent.putExtra("imgUrl", list.get(position).getContent().getImgs().get(0).getFile());
                        activity.startActivity(intent, optionsCompat.toBundle());
                    }
                });
            } else {
                holder.contentImageView.setVisibility(View.GONE);
            }
        } else if (mHolder instanceof DanTuNoTextViewHolder) {
            final DanTuNoTextViewHolder holder = (DanTuNoTextViewHolder) mHolder;
            if (null != list.get(position).getContent().getImgs() && list.get(position).getContent().getImgs().size() > 0) {
                System.out.println(list.get(position).getContent().getImgs().get(0).getFile());
                if (list.get(position).getContent().getImgs().get(0).getWidth() >= list.get(position).getContent().getImgs().get(0).getHeight()) {
                    list.get(position).getContent().getImgs().get(0).setFile("http://pic1.win4000.com/wallpaper/2019-11-28/5ddf5ac0919cc.jpg");
                    holder.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else {
                    list.get(position).getContent().getImgs().get(0).setFile("https://c-ssl.duitang.com/uploads/item/201906/16/20190616150215_Wt8Lk.jpeg");
                    holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                dantunotextLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                dantunotextLayoutParams.gravity = Gravity.CENTER;
                holder.mImageView.setLayoutParams(dantunotextLayoutParams);
                Glide.with(activity).load(list.get(position).getContent().getImgs().get(0).getFile())
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
                        Intent intent = new Intent(activity, LargeImageActivity.class);
                        intent.putExtra("imgUrl", list.get(position).getContent().getImgs().get(0).getFile());
                        activity.startActivity(intent, optionsCompat.toBundle());
                    }
                });
            }

        } else if (mHolder instanceof XingQuViewHolder) {
            XingQuViewHolder holder = (XingQuViewHolder) mHolder;
            holder.xingquRecyclerView.setAdapter(holder.adapter);
            holder.adapter.setList(list.get(position).getQuanzi());
            holder.xingquChangeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChangeXingQuListener.onChangeXingQu(position);
                }
            });
        } else if (mHolder instanceof ViewHViewHolder) {
            ViewHViewHolder holder = (ViewHViewHolder) mHolder;
//            System.out.println("横视频" + position + " , type: " + list.get(position).getType());

        } else if (mHolder instanceof VideoVViewHolder) {
            final VideoVViewHolder holder = (VideoVViewHolder) mHolder;
            holder.videovBottomLayout.getBackground().setAlpha(128);
            holder.videovContentTextView.setText(list.get(position).getContent().getContent());
            holder.videovShopNameTextView.setText(list.get(position).getContent().getUsername() + position + " - " + list.get(position).getType());
            holder.videovTimeTextView.setText(list.get(position).getContent().getTime());
            Glide.with(activity).load(list.get(position).getContent().getAvatar()).into(holder.videovHeaderImageView);
            Glide.with(activity).load(list.get(position).getContent().getVideo_thumb()).into(holder.videovCoverImageView);
            if (list.get(position).isPlay()) {
                System.out.println("setUrl: " + list.get(position).isSetVideoUrl());
                if (!list.get(position).isSetVideoUrl()) {
                    holder.videovHuanchongView.setProgress(0);
                    holder.videovHuanchongView.setText("0%");
                    holder.videovCoverImageView.setVisibility(View.GONE);
                    holder.videovPlayImageView.setVisibility(View.GONE);
                    holder.videovVideoView.setVisibility(View.VISIBLE);
                    holder.videovHuanchongView.setVisibility(View.VISIBLE);
                    int width = holder.videovVideoView.getLayoutParams().width;
                    int height = holder.videovVideoView.getLayoutParams().height;
                    holder.videovVideoView.setVideoSize(width, height);
//                list.get(position).getContent().setVideo_url("https://flv3.bn.netease.com/711b98bb9cbd792f3d4bdb17ff133614f5bdae03fcca23aaf434d724be4fe8b36a52544083a1be657423260b55eda3c3bf536015d653da150fe0f98d8786907ee1c48f2dd0bd687b44f89f2025b41f76f6b6158d15c98ae8b8c791aa1c947150acb2e055c9b4b16f629a91e0193316f6433b05bf2d8461ad.mp4");

//                String videoUrl = list.get(position).getContent().getVideo_url();
//                String videoUrl="https://flv3.bn.netease.com/711b98bb9cbd792fff07ad4d3db7d9a58c6b75df2c2f7b6e06562a648bd542e2347b420b90802d2453e9093cefc0b2664a5088da8049af4797e1c80ee14a8f3cc029e7533ee2d9e8a54d4ad7cd50b6248bbfbe956de427193161d8d5d85ea137faf5d4d5ce38ede7df497a3534aaf124c3a13cfd51992e79.mp4";
//                String videoUrl = "https://flv3.bn.netease.com/711b98bb9cbd792f3d4bdb17ff133614f5bdae03fcca23aaf434d724be4fe8b36a52544083a1be657423260b55eda3c3bf536015d653da150fe0f98d8786907ee1c48f2dd0bd687b44f89f2025b41f76f6b6158d15c98ae8b8c791aa1c947150acb2e055c9b4b16f629a91e0193316f6433b05bf2d8461ad.mp4";
//               String videoUrl="https://flv3.bn.netease.com/711b98bb9cbd792f77811939c97845631d13b5d000678b37136b379e89b329c285ecdf0f0cffacb62d531619d350ae985d8a6f07803039ad49fee234a29cdf4cbd45b1e92ee8bc859f89f72c8616fa4bb252f31f734bc23bdd802293516fedd3e9e5de610df5f9a81c678d6ce36f7f25bd43048ffa67f39a.mp4";
//                String videoUrl="https://flv3.bn.netease.com/711b98bb9cbd792f4066fb2929186ba8a37396106bf63a62a7d9fa82e30d1c62fed009c05a688f818484898ea109766a604a80ac4d77e315dd2d2bf3a1edce1e8ad047b7e0a06088baa7d8ed6095187af2eb91b00c53d9db02388d25a8a4778fcb192800fa0bb233371bb30e6468b81b1b35c08a6ce49cf1.mp4";
//                Uri u = Uri.parse(videoUrl);
//                Uri u1 = Uri.parse("cache:/data/data/com.zhiyu.pintaihui" + "/video_cache/" + MD5Utils.getMD5String(videoUrl) + ".mp4:" + u);
//                holder.videovVideoView.setVideoPath(list.get(position).getContent().getVideo_url());
//                holder.videovVideoView.setVideoPath("/sdcard/DCIM/Camera/123.mp4");
//                holder.videovVideoView.setVideoPath(list.get(position).getContent().getVideo_url());
                    System.out.println("video path: " + list.get(position).getContent().getVideo_url());
//                if (list.get(position).getContent().getVideo_url().startsWith("http")) {
//                    holder.videovVideoView.setVideoURI(Uri.parse(list.get(position).getContent().getVideo_url()));
//                } else {
//                    holder.videovVideoView.setVideoPath(list.get(position).getContent().getVideo_url());
//                }

                    if (!list.get(position).getContent().getVideo_url().contains("http")) {
                        holder.videovVideoView.setVideoPath(list.get(position).getContent().getVideo_url());
                    }


//                holder.videovVideoView.setVideoPath("/data/user/0/com.zhiyu.pintaihui/files/7b669a1b0e0b22515ea25922643ca964.mp4");
                    holder.videovVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                        System.out.println(holder.videovHuanchongView.getProgress() + "%");
                            holder.videovHuanchongView.setProgress(holder.videovVideoView.getBufferPercentage());
                            holder.videovHuanchongView.setText(holder.videovHuanchongView.getProgress() + "%");
                        }
                    });
                    holder.videovVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            switch (what) {
                                //开始缓冲
                                case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲时的视图变化
//                                System.out.println("开始缓冲");
                                    holder.videovCoverImageView.setVisibility(View.VISIBLE);
                                    holder.videovHuanchongView.setVisibility(View.VISIBLE);
                                    mp.pause();
                                    break;
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲好后的视图变化（可播放）
//                                System.out.println("缓冲好-可播放");
                                    holder.videovCoverImageView.setVisibility(View.GONE);
                                    holder.videovHuanchongView.setVisibility(View.GONE);
                                    mp.start();
                                    break;
                            }
                            return true;
                        }
                    });
                    holder.videovVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.videovCoverImageView.setVisibility(View.VISIBLE);
                            holder.videovPlayImageView.setVisibility(View.VISIBLE);
                            holder.videovVideoView.setVisibility(View.GONE);
                            holder.videovVideoView.stopPlayback();
                            System.gc();
                        }
                    });


                    holder.videovVideoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.videovCoverImageView.setVisibility(View.VISIBLE);
                            holder.videovPlayImageView.setVisibility(View.GONE);
                            holder.videovVideoView.pause();
                            holder.videovVideoView.stopPlayback();
                            System.gc();
                            ActivityOptionsCompat optionsCompat =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.videovCoverImageView, "dongtai_video_cover");
                            Intent intent = new Intent(activity, DongTaiVideoPlayerActivity.class);
                            intent.putExtra("coverUrl", list.get(position).getVideoCover());
                            activity.startActivity(intent, optionsCompat.toBundle());
                        }
                    });
                    list.get(position).setSetVideoUrl(true);
                } else {
                    holder.videovCoverImageView.setVisibility(View.GONE);
                    holder.videovPlayImageView.setVisibility(View.GONE);
                    holder.videovVideoView.setVisibility(View.VISIBLE);
                    holder.videovVideoView.start();
                }
            } else {
                holder.videovCoverImageView.setVisibility(View.VISIBLE);
                holder.videovPlayImageView.setVisibility(View.VISIBLE);
                holder.videovVideoView.setVisibility(View.GONE);
                holder.videovVideoView.pause();
//                holder.videovVideoView.stopPlayback();
//                System.gc();
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == WEN_ZHANG) {
            return WEN_ZHANG;
        } else if (list.get(position).getType() == VIDEO_H) {
            return VIDEO_H;
        } else if (list.get(position).getType() == VIDEO_V) {
            return VIDEO_V;
        } else if (list.get(position).getType() == DUO_TU) {
            return DUO_TU;
        } else if (list.get(position).getType() == DAN_TU_HAS_TEXT) {
            return DAN_TU_HAS_TEXT;
        } else if (list.get(position).getType() == DAN_TU_NO_TEXT) {
            return DAN_TU_NO_TEXT;
        } else if (list.get(position).getType() == XING_QU) {
            return XING_QU;
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 感兴趣的圈子推荐-换一批
     */
    public interface OnChangeXingQuListener {
        void onChangeXingQu(int position);
    }

    private OnChangeXingQuListener onChangeXingQuListener;

    public void setOnChangeXingQuListener(OnChangeXingQuListener listener) {
        this.onChangeXingQuListener = listener;
    }
}
