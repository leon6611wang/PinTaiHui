package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ComplaintActivity;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.DeleteFeedDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * 首页-圈子-关注列表适配器
 */
public class CardInfoFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Feed> list;
    private ShareDialog shareDialog;
    private DeleteFeedDialog deleteFeedDialog;
    private Activity activity;
    private Context context;
    private int dp_240;
    private int width, height;
    private MyHandler myHandler = new MyHandler(this);

    public CardInfoFeedsAdapter(Activity aty, final Context context) {
        this.context = context;
        shareConfig();
        this.activity = aty;
        dp_240 = (int) context.getResources().getDimension(R.dimen.dp_240);
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        height = (int) context.getResources().getDimension(R.dimen.dp_240);
        int dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
        width = screenWidth - dp_30;
        shareDialog = new ShareDialog(activity, context, R.style.dialog);
        deleteFeedDialog = new DeleteFeedDialog(context, R.style.dialog, new DeleteFeedDialog.OnDeleteFeedListener() {
            @Override
            public void onDeleteFeed(int index, int position, String desc) {
                System.out.println("index: " + index + " , " + desc);
                switch (index) {
                    case 1://取消关注
                        onUnFollowUser(position);
                        break;
                    case 2://投诉
                        Intent complaintIntent = new Intent(context, ComplaintActivity.class);
                        complaintIntent.putExtra("report_id", list.get(position).getContent().getId());
                        complaintIntent.putExtra("module_type", "feeds");
                        context.startActivity(complaintIntent);
                        break;
                }
            }
        });
    }

    private static class MyHandler extends Handler {
        WeakReference<CardInfoFeedsAdapter> adapterWeakReference;

        public MyHandler(CardInfoFeedsAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CardInfoFeedsAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).getContent().setIs_collect(!adapter.list.get(posiiton).getContent().isIs_collect());
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).getContent().setIs_prise(!adapter.list.get(posiiton).getContent().isIs_prise());
                        int priseCount = adapter.list.get(posiiton).getContent().getPrise_num();
                        adapter.list.get(posiiton).getContent().setPrise_num(adapter.list.get(posiiton).getContent().isIs_prise() ? priseCount + 1 : priseCount - 1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.remove(posiiton);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private boolean stopVideo;

    public void stopVideo(boolean stop) {
        this.stopVideo = stop;
        notifyDataSetChanged();
    }


    public void setList(List<Feed> feedList) {
        this.list = feedList;
        notifyDataSetChanged();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        CircleGuanZhuFeedTagListAdapter adapter = new CircleGuanZhuFeedTagListAdapter();
        MyGridView imageGridView;
        FullSearchFeedImagesGridAdapter imagesGridAdapter = new FullSearchFeedImagesGridAdapter(context);
        VideoPlayerTrackView videoPlayer;
        NiceImageView feedImageView;
        LinearLayout priseLayout, itemRootLayout;
        RelativeLayout closeLayout;

        public FeedViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            mTextView = itemView.findViewById(R.id.mTextView);
            collectImageView = itemView.findViewById(R.id.collectImageView);
            itemRootLayout = itemView.findViewById(R.id.itemRootLayout);
            priseLayout = itemView.findViewById(R.id.priseLayout);
            shareTextView = itemView.findViewById(R.id.shareTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            priseImageView = itemView.findViewById(R.id.priseImageView);
            priseNumTextView = itemView.findViewById(R.id.priseNumTextView);
            tagListView = itemView.findViewById(R.id.tagListView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            tagListView.setAdapter(adapter);
            imageGridView = itemView.findViewById(R.id.imageGridView);
            imageGridView.setAdapter(imagesGridAdapter);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            feedImageView = itemView.findViewById(R.id.feedImageView);
            closeLayout = itemView.findViewById(R.id.closeLayout);
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootLayout;
        CircleImageView avatarImageView;
        TextView nameTextView, titleTextView, sourceTextView, timeTextView, shareTextView, commentTextView, priseTextView;
        LinearLayout priseLayout;
        NiceImageView coverImageView;
        HorizontalListView tagListView;
        CircleGuanZhuFeedTagListAdapter adapter = new CircleGuanZhuFeedTagListAdapter();
        ImageView collectImageView, priseImageView;
        RelativeLayout closeLayout;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            coverImageView = itemView.findViewById(R.id.coverImageView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            collectImageView = itemView.findViewById(R.id.collectImageView);
            shareTextView = itemView.findViewById(R.id.shareTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            priseLayout = itemView.findViewById(R.id.priseLayout);
            priseImageView = itemView.findViewById(R.id.priseImageView);
            priseTextView = itemView.findViewById(R.id.priseTextView);
            tagListView = itemView.findViewById(R.id.tagListView);
            tagListView.setAdapter(adapter);
            closeLayout = itemView.findViewById(R.id.closeLayout);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        CircleGuanZhuFeedTagListAdapter adapter = new CircleGuanZhuFeedTagListAdapter();
        VideoPlayerTrackView videoPlayer;
        LinearLayout priseLayout, itemRootLayout;
        RelativeLayout closeLayout;

        public VideoViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            mTextView = itemView.findViewById(R.id.mTextView);
            collectImageView = itemView.findViewById(R.id.collectImageView);
            itemRootLayout = itemView.findViewById(R.id.itemRootLayout);
            priseLayout = itemView.findViewById(R.id.priseLayout);
            shareTextView = itemView.findViewById(R.id.shareTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            priseImageView = itemView.findViewById(R.id.priseImageView);
            priseNumTextView = itemView.findViewById(R.id.priseNumTextView);
            tagListView = itemView.findViewById(R.id.tagListView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            itemRootLayout = itemView.findViewById(R.id.itemRootLayout);
            tagListView.setAdapter(adapter);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            closeLayout = itemView.findViewById(R.id.closeLayout);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FEED) {
            return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_feed, parent, false));
        } else if (viewType == ARTICLE) {
            return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_article, parent, false));
        } else if (viewType == VIDEO) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_video, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {
            FeedViewHolder feed = (FeedViewHolder) holder;
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.drawable.image_error).into(feed.avatarImageView);
            feed.nameTextView.setText(list.get(position).getContent().getUsername());
            feed.timeTextView.setText(list.get(position).getContent().getTime());
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                feed.mTextView.setVisibility(View.VISIBLE);
                feed.mTextView.setText(list.get(position).getContent().getContent());
            } else {
                feed.mTextView.setVisibility(View.GONE);
            }

            if (null == list.get(position).getContent().getFeeds_tags() || list.get(position).getContent().getFeeds_tags().size() == 0) {
                feed.tagListView.setVisibility(View.GONE);
            } else {
                feed.tagListView.setVisibility(View.VISIBLE);
                feed.adapter.setList(list.get(position).getContent().getFeeds_tags());
            }
            feed.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            if (list.get(position).getContent().isIs_collect()) {
                feed.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
            } else {
                feed.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_gray));
            }
            feed.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
            feed.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
            feed.priseNumTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
            if (list.get(position).getContent().isIs_prise()) {
                feed.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
            } else {
                feed.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
            }

            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getVideo_url())) {
                feed.feedImageView.setVisibility(View.GONE);
                feed.imageGridView.setVisibility(View.GONE);
                feed.videoPlayer.setVisibility(View.VISIBLE);
                feed.videoPlayer.setDataSource(list.get(position).getContent().getVideo_url(), "");
                Glide.with(context).load(list.get(position).getContent().getVideo_thumb()).into(feed.videoPlayer.getCoverController().getVideoCover());
//                Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(feed.videoPlayer.getCoverController().getVideoCover());
                feed.videoPlayer.setLayoutParams(list.get(position).getContent().getLayoutParams(dp_240, true));
            } else {
                if (list.get(position).getContent().getImgs().size() == 1) {
                    feed.feedImageView.setVisibility(View.VISIBLE);
                    feed.imageGridView.setVisibility(View.GONE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.feedImageView.setLayoutParams(list.get(position).getContent().getLayoutParams(dp_240, false));
                    Glide.with(context).load(list.get(position).getContent().getImgs().get(0).getFile()).error(R.drawable.image_error)
                            .into(feed.feedImageView);
                    feed.feedImageView.setOnClickListener(new OnLargeImageClick(list.get(position).getContent().getImgs().get(0).getFile()));
                } else {
                    feed.feedImageView.setVisibility(View.GONE);
                    feed.imageGridView.setVisibility(View.VISIBLE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.imagesGridAdapter.setList(list.get(position).getContent().getImgs());
                }
            }
            if (stopVideo && feed.videoPlayer.isPlaying()) {
                feed.videoPlayer.getVideoController().pause();
            }
            feed.collectImageView.setOnClickListener(new OnCollectListener(position));
            feed.shareTextView.setOnClickListener(new OnShareClick(position));
            feed.commentTextView.setOnClickListener(new OnFeedInfoClick(position));
            feed.itemRootLayout.setOnClickListener(new OnFeedInfoClick(position));
            feed.priseLayout.setOnClickListener(new OnPriseClick(position));
            feed.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
        } else if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder article = (ArticleViewHolder) holder;
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.drawable.image_error).into(article.avatarImageView);
            article.nameTextView.setText(list.get(position).getContent().getUsername());
            article.titleTextView.setText(list.get(position).getContent().getTitle());
            if (null != list.get(position).getContent().getNewthumb()) {
                article.coverImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getContent().getNewthumb().getFile()).error(R.drawable.image_error).
                        placeholder(R.drawable.image_error)
                        .fallback(R.drawable.image_error)
                        .into(article.coverImageView);
            } else {
                article.coverImageView.setVisibility(View.GONE);
            }
            article.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            article.timeTextView.setText(list.get(position).getContent().getTime());
            article.adapter.setList(list.get(position).getContent().getFeeds_tags());
            if (list.get(position).getContent().isIs_collect()) {
                article.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
            } else {
                article.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_gray));
            }
            article.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
            article.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
            article.priseTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
            if (list.get(position).getContent().isIs_prise()) {
                article.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
            } else {
                article.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
            }
            article.collectImageView.setOnClickListener(new OnCollectListener(position));
            article.commentTextView.setOnClickListener(new OnArticleInfoClick(position));
            article.priseLayout.setOnClickListener(new OnPriseClick(position));
            article.shareTextView.setOnClickListener(new OnShareClick(position));
            article.rootLayout.setOnClickListener(new OnArticleInfoClick(position));
            article.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder video = (VideoViewHolder) holder;
            Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.drawable.image_error).into(video.avatarImageView);
            video.nameTextView.setText(list.get(position).getContent().getUsername());
            video.timeTextView.setText(list.get(position).getContent().getTime());
            video.mTextView.setText(list.get(position).getContent().getContent());
            if (null == list.get(position).getContent().getFeeds_tags() || list.get(position).getContent().getFeeds_tags().size() == 0) {
                video.tagListView.setVisibility(View.GONE);
            } else {
                video.tagListView.setVisibility(View.VISIBLE);
                video.adapter.setList(list.get(position).getContent().getFeeds_tags());
            }
            video.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            if (list.get(position).getContent().isIs_collect()) {
                video.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
            } else {
                video.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_gray));
            }
            video.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
            video.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
            video.priseNumTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
            if (list.get(position).getContent().isIs_prise()) {
                video.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
            } else {
                video.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
            }
            video.videoPlayer.setLayoutParams(list.get(position).getContent().getLayoutParams(width, height));
            video.videoPlayer.setDataSource(list.get(position).getContent().getVideo_url(), "");
            if (stopVideo && video.videoPlayer.isPlaying()) {
                video.videoPlayer.getVideoController().pause();
            }
            Glide.with(context).load(list.get(position).getContent().getVideo_thumb()).into(video.videoPlayer.getCoverController().getVideoCover());
//            Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(video.videoPlayer.getCoverController().getVideoCover());
            video.collectImageView.setOnClickListener(new OnCollectListener(position));
            video.shareTextView.setOnClickListener(new OnShareClick(position));
            video.commentTextView.setOnClickListener(new OnVideoInformationClick(position));
            video.priseLayout.setOnClickListener(new OnPriseClick(position));
            video.itemRootLayout.setOnClickListener(new OnVideoInformationClick(position));
            video.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getFeeds_type();
    }

    private final int ARTICLE = 1, VIDEO = 2, FEED = 3;


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

    private class OnCollectListener implements View.OnClickListener {
        private int position;

        public OnCollectListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            collectFeed(position);
        }
    }

    private class OnShareClick implements View.OnClickListener {
        private int position;

        public OnShareClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            String image_url;
            switch (list.get(position).getFeeds_type()) {
                case FEED:
                    if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getVideo_url())) {
                        image_url = list.get(position).getContent().getVideo_thumb();
                    } else {
                        if (null == list.get(position).getContent().getImgs() || list.get(position).getContent().getImgs().size() == 0) {
                            image_url = share_image_url;
                        } else {
                            image_url = list.get(position).getContent().getImgs().get(0).getFile();
                        }
                    }
                    shareResult.getData().getShare().setImage_url(image_url);
                    shareResult.getData().getShare().setContent(list.get(position).getContent().getContent());
                    break;
                case ARTICLE:
                    if (null != list.get(position).getContent().getNewthumb() && null != list.get(position).getContent().getNewthumb().getFile()) {
                        image_url = list.get(position).getContent().getNewthumb().getFile();
                    } else {
                        image_url = share_image_url;
                    }
                    shareResult.getData().getShare().setImage_url(image_url);
                    shareResult.getData().getShare().setContent(list.get(position).getContent().getTitle());
                    break;
                case VIDEO:
                    if (null != list.get(position).getContent().getVideo_thumb()) {
                        image_url = list.get(position).getContent().getVideo_thumb();
                    } else {
                        image_url = share_image_url;
                    }
                    shareResult.getData().getShare().setImage_url(image_url);
                    shareResult.getData().getShare().setContent(list.get(position).getContent().getContent());
                    break;
            }
            shareDialog.show();
            shareDialog.setShare(shareResult.getData().getShare(), list.get(position).getContent().getId());

        }
    }

    private class OnFeedInfoClick implements View.OnClickListener {
        private int position;

        public OnFeedInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent commentIntent = new Intent(context, FeedInformationActivity.class);
            commentIntent.putExtra("feed_id", list.get(position).getContent().getId());
            commentIntent.putExtra("feed_type",list.get(position).getType());
            commentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(commentIntent);
        }
    }

    private class OnPriseClick implements View.OnClickListener {
        private int position;

        public OnPriseClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            priseFeed(position);
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

    private BaseResult baseResult;

    private void priseFeed(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(list.get(position).getContent().getId()));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", (list.get(position).getContent().isIs_prise() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private void collectFeed(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(list.get(position).getContent().getId()));
        params.addBodyParameter("module_type", "feeds");
        params.addBodyParameter("type", (list.get(position).getContent().isIs_collect() ? "1" : "0"));
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


    private class OnDeleteFeedClick implements View.OnClickListener {
        private int position;

        public OnDeleteFeedClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            deleteFeedDialog.show();
            deleteFeedDialog.setPosition(position);
        }
    }

    /**
     * 取消关注
     *
     * @param position
     */
    private void onUnFollowUser(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(list.get(position).getContent().getUid()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("取消关注: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    private ShareResult shareResult;
    private String share_image_url;

    private void shareConfig() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_FEED);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult = GsonUtils.GsonToBean(result, ShareResult.class);
                share_image_url = shareResult.getData().getShare().getImage_url();
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
}
