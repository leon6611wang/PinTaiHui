package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.WrapImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class CircleTuiJianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ARTICLE = 1, VIDEO = 2, FEED = 3, FEED_ONE_IMAGE = 4;
    private List<QuanZiTuiJian> list;
    private Context context;
    private int width, height, screenWidth;
    private int dp_240;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleTuiJianAdapter> adapterWeakReference;

        public MyHandler(CircleTuiJianAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleTuiJianAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int position = (Integer) msg.obj;
                        int uid = adapter.list.get(position).getContent().getUid();
                        for (int i = 0; i < adapter.list.size(); i++) {
                            if (adapter.list.get(i).getContent().getUid() == uid) {
                                adapter.list.get(i).getContent().setIs_follow(!adapter.list.get(i).getContent().isIs_follow());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public CircleTuiJianAdapter(Context context, int cardWidth, int cardHeight) {
        this.context = context;
        this.width = cardWidth;
        this.height = cardHeight;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_240 = (int) context.getResources().getDimension(R.dimen.dp_240);
    }

    public void setList(List<QuanZiTuiJian> tuiJianList) {
        this.list = tuiJianList;
        notifyDataSetChanged();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        CardView cardView;
        MyGridView imageGridView;
        CircleTuiJianFeedImagesGridAdapter imagesGridAdapter = new CircleTuiJianFeedImagesGridAdapter(context, width);
        VideoPlayerTrackView videoPlayer;
        NiceImageView feedImageView;
        LinearLayout followLayout;
        CircleImageView avatarImageView;
        ImageView followImageView;
        TextView nameTextView, timeTextView, followTextView, contentTextView;

        public FeedViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            cardView = itemView.findViewById(R.id.cardView);
            imageGridView = itemView.findViewById(R.id.imageGridView);
            imageGridView.setAdapter(imagesGridAdapter);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            feedImageView = itemView.findViewById(R.id.feedImageView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            followLayout = itemView.findViewById(R.id.followLayout);
            followImageView = itemView.findViewById(R.id.followImageView);
            followTextView = itemView.findViewById(R.id.followTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    class FeedOneImageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout, bottomLayout, followLayout;
        CardView cardView;
        ImageView feedImageView, followImageView;
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, followTextView;

        public FeedOneImageViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            cardView = itemView.findViewById(R.id.cardView);
            feedImageView = itemView.findViewById(R.id.feedImageView);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            bottomLayout.getBackground().mutate().setAlpha(110);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            followLayout = itemView.findViewById(R.id.followLayout);
            followImageView = itemView.findViewById(R.id.followImageView);
            followTextView = itemView.findViewById(R.id.followTextView);
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        CardView cardView;
        LinearLayout followLayout;
        CircleImageView avatarImageView;
        ImageView followImageView;
        TextView nameTextView, timeTextView, followTextView, titleTextView, contentTextView;
        WrapImageView coverImageView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            cardView = itemView.findViewById(R.id.cardView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            followLayout = itemView.findViewById(R.id.followLayout);
            followImageView = itemView.findViewById(R.id.followImageView);
            followTextView = itemView.findViewById(R.id.followTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            coverImageView = itemView.findViewById(R.id.coverImageView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout, bottomLayout;
        CardView cardView;
        VideoPlayerTrackView videoPlayer;
        LinearLayout followLayout;
        CircleImageView avatarImageView;
        ImageView followImageView;
        TextView nameTextView, timeTextView, followTextView, contentTextView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            cardView = itemView.findViewById(R.id.cardView);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            bottomLayout.getBackground().mutate().setAlpha(110);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            followLayout = itemView.findViewById(R.id.followLayout);
            followImageView = itemView.findViewById(R.id.followImageView);
            followTextView = itemView.findViewById(R.id.followTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FEED) {
            return new FeedViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_tuijian_feed, parent, false));
        } else if (viewType == ARTICLE) {
            return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_tuijian_article, parent, false));
        } else if (viewType == VIDEO) {
            return new VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_tuijian_video, parent, false));
        } else if (viewType == FEED_ONE_IMAGE) {
            return new FeedOneImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_circle_tuijian_feed_one_image, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {
            FeedViewHolder feed = (FeedViewHolder) holder;
            feed.itemLayout.setLayoutParams(list.get(position).getItemLayoutParams(context, width));
            feed.cardView.setLayoutParams(list.get(position).getCardViewParams(context, width, height));
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                feed.contentTextView.setVisibility(View.VISIBLE);
                feed.contentTextView.setText(list.get(position).getContent().getContent());
            } else {
                feed.contentTextView.setVisibility(View.GONE);
            }

            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getVideo_url())) {
                feed.feedImageView.setVisibility(View.GONE);
                feed.imageGridView.setVisibility(View.GONE);
                feed.videoPlayer.setVisibility(View.VISIBLE);
                feed.videoPlayer.setDataSource(list.get(position).getContent().getVideo_url(), "");
                Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(feed.videoPlayer.getCoverController().getVideoCover());
                feed.videoPlayer.setLayoutParams(list.get(position).getFeedParams(screenWidth, width, dp_240, true));
            } else {
                if (list.get(position).getContent().getImgs().size() == 1) {
                    feed.feedImageView.setVisibility(View.VISIBLE);
                    feed.imageGridView.setVisibility(View.GONE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.feedImageView.setLayoutParams(list.get(position).getFeedParams(screenWidth, width, dp_240, false));
                    Glide.with(context).load(list.get(position).getContent().getImgs().get(0).getFile()).error(R.mipmap.img_error)
                            .into(feed.feedImageView);
                    feed.feedImageView.setOnClickListener(new OnLargeImageClick(list.get(position).getContent().getImgs().get(0).getFile()));
                } else {
                    feed.feedImageView.setVisibility(View.GONE);
                    feed.imageGridView.setVisibility(View.VISIBLE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.imagesGridAdapter.setList(list.get(position).getContent().getImgs());
                }
            }
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.mipmap.no_avatar).into(feed.avatarImageView);
            feed.nameTextView.setText(list.get(position).getContent().getUsername());
            feed.timeTextView.setText(list.get(position).getContent().getTime());

            if (list.get(position).getContent().isIs_follow()) {
                feed.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
                feed.followImageView.setVisibility(View.GONE);
                feed.followTextView.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
                feed.followTextView.setText("已关注");
            } else {
                feed.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                feed.followImageView.setVisibility(View.VISIBLE);
                feed.followTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                feed.followTextView.setText("关注");
            }
            feed.followLayout.setOnClickListener(new OnFollowClickListener(position));
            feed.itemLayout.setOnClickListener(new OnFeedInformationClick(position));
        } else if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder article = (ArticleViewHolder) holder;
            article.itemLayout.setLayoutParams(list.get(position).getItemLayoutParams(context, width));
            article.cardView.setLayoutParams(list.get(position).getCardViewParams(context, width, height));
            article.titleTextView.setText(list.get(position).getContent().getTitle());
            if (null != list.get(position).getContent().getThumb()) {
                article.coverImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getContent().getThumb().getFile());
            } else {
                article.coverImageView.setVisibility(View.GONE);
            }
            article.contentTextView.setText(list.get(position).getContent().getContent());
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.mipmap.no_avatar).into(article.avatarImageView);
            article.nameTextView.setText(list.get(position).getContent().getUsername());
            article.timeTextView.setText(list.get(position).getContent().getTime());
            if (list.get(position).getContent().isIs_follow()) {
                article.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
                article.followImageView.setVisibility(View.GONE);
                article.followTextView.setTextColor(context.getResources().getColor(R.color.color_dfdfdf));
                article.followTextView.setText("已关注");
            } else {
                article.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                article.followImageView.setVisibility(View.VISIBLE);
                article.followTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                article.followTextView.setText("关注");
            }
            article.followLayout.setOnClickListener(new OnFollowClickListener(position));
            article.itemLayout.setOnClickListener(new OnArticleInfoClick(position));
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder video = (VideoViewHolder) holder;
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                video.contentTextView.setVisibility(View.VISIBLE);
                video.contentTextView.setText(list.get(position).getContent().getContent());
            } else {
                video.contentTextView.setVisibility(View.GONE);
            }

            video.itemLayout.setLayoutParams(list.get(position).getItemLayoutParams(context, width));
            video.cardView.setLayoutParams(list.get(position).getCardViewParams(context, width, height));
            video.videoPlayer.setLayoutParams(list.get(position).getVideoParams(context, width, height));
            video.videoPlayer.setDataSource(list.get(position).getContent().getVideo_url(), "");
            Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(video.videoPlayer.getCoverController().getVideoCover());
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.mipmap.no_avatar).into(video.avatarImageView);
            video.nameTextView.setText(list.get(position).getContent().getUsername());
            video.timeTextView.setText(list.get(position).getContent().getTime());
            if (list.get(position).getContent().isIs_follow()) {
                video.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
                video.followImageView.setVisibility(View.GONE);
                video.followTextView.setTextColor(context.getResources().getColor(R.color.color_dfdfdf));
                video.followTextView.setText("已关注");
            } else {
                video.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                video.followImageView.setVisibility(View.VISIBLE);
                video.followTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                video.followTextView.setText("关注");
            }
            video.followLayout.setOnClickListener(new OnFollowClickListener(position));
            video.itemLayout.setOnClickListener(new OnVideoInformationClick(position));
        } else if (holder instanceof FeedOneImageViewHolder) {
            FeedOneImageViewHolder oneImage = (FeedOneImageViewHolder) holder;
            oneImage.itemLayout.setLayoutParams(list.get(position).getItemLayoutParams(context, width));
            oneImage.cardView.setLayoutParams(list.get(position).getCardViewParams(context, width, height));
            if (null != list.get(position).getContent().getImgs() && list.get(position).getContent().getImgs().size() > 0) {
                Glide.with(context).load(list.get(position).getContent().getImgs().get(0).getFile()).into(oneImage.feedImageView);
            }
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.mipmap.no_avatar).into(oneImage.avatarImageView);
            oneImage.nameTextView.setText(list.get(position).getContent().getUsername());
            oneImage.timeTextView.setText(list.get(position).getContent().getTime());
            if (list.get(position).getContent().isIs_follow()) {
                oneImage.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
                oneImage.followImageView.setVisibility(View.GONE);
                oneImage.followTextView.setTextColor(context.getResources().getColor(R.color.color_dfdfdf));
                oneImage.followTextView.setText("已关注");
            } else {
                oneImage.followLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                oneImage.followImageView.setVisibility(View.VISIBLE);
                oneImage.followTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                oneImage.followTextView.setText("关注");
            }
            oneImage.followLayout.setOnClickListener(new OnFollowClickListener(position));
            oneImage.itemLayout.setOnClickListener(new OnFeedInformationClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).getFeed_type();
        if (type == FEED && StringUtils.isNullOrEmpty(list.get(position).getContent().getContent()) && null != list.get(position).getContent().getImgs()) {
            type = FEED_ONE_IMAGE;
        }
        return type;
    }

    private class OnFollowClickListener implements View.OnClickListener {
        private int position;

        public OnFollowClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            onFollow(position);
        }
    }

    private BaseResult baseResult;

    private void onFollow(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(list.get(position).getContent().getUid()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", list.get(position).getContent().isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private class OnLargeImageClick implements View.OnClickListener {
        private String imageUrl;

        public OnLargeImageClick(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public void onClick(View v) {
            Intent largeIntent = new Intent(context, LargeImageActivity.class);
            largeIntent.putExtra("imgUrl", imageUrl);
            largeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(largeIntent);
        }
    }

    private class OnArticleInfoClick implements View.OnClickListener {
        private int position;

        public OnArticleInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent articleInfoIntent = new Intent(context, ArticleInformationActivity.class);
            articleInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            articleInfoIntent.putExtra("article_id", list.get(position).getContent().getId());
            context.startActivity(articleInfoIntent);
        }
    }

    private class OnVideoInformationClick implements View.OnClickListener {
        private int position;

        public OnVideoInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent videoInfoIntent = new Intent(context, VideoInformationActivity.class);
            videoInfoIntent.putExtra("feeds_id", list.get(position).getContent().getId());
            videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(videoInfoIntent);
        }
    }

    private class OnFeedInformationClick implements View.OnClickListener {
        private int position;

        public OnFeedInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent feedInfoIntent = new Intent(context, FeedInformationActivity.class);
            feedInfoIntent.putExtra("feed_id", list.get(position).getContent().getId());
            feedInfoIntent.putExtra("feed_type", !StringUtils.isNullOrEmpty(list.get(position).getContent().getVideo_url()) ? "video" : "image");
            feedInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(feedInfoIntent);

        }
    }
}
