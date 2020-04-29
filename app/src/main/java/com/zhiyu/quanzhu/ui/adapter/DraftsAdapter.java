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
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.PublishArticleActivity;
import com.zhiyu.quanzhu.ui.activity.PublishFeedActivity;
import com.zhiyu.quanzhu.ui.activity.PublishVideoActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;


public class DraftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Feed> list;
    private ShareDialog shareDialog;
    private Activity activity;
    private Context context;
    private int dp_240;
    private int width, height;
    private boolean isDeleteStatus;
    private MyHandler myHandler = new MyHandler(this);

    public DraftsAdapter(Activity aty, final Context context) {
        this.context = context;
        this.activity = aty;
        dp_240 = (int) context.getResources().getDimension(R.dimen.dp_240);
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        height = (int) context.getResources().getDimension(R.dimen.dp_240);
        int dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
        width = screenWidth - dp_30;
        shareDialog = new ShareDialog(activity, context, R.style.dialog);
    }

    private static class MyHandler extends Handler {
        WeakReference<DraftsAdapter> adapterWeakReference;

        public MyHandler(DraftsAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            DraftsAdapter adapter = adapterWeakReference.get();
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

    public void setDeleteStatus(boolean status) {
        System.out.println("isDeleteStatus: " + isDeleteStatus);
        this.isDeleteStatus = status;
        notifyDataSetChanged();
    }

    public void onDeleteSuccess() {
        List<Feed> feedList = new ArrayList<>();
        for (Feed feed : list) {
            if (!feed.isSelected()) {
                feedList.add(feed);
            }
        }
        list = feedList;
        this.isDeleteStatus = false;
        notifyDataSetChanged();
    }

    public List<Feed> getList() {
        return list;
    }

    public void setList(List<Feed> feedList) {
        this.list = feedList;
        notifyDataSetChanged();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        FeedCircleTagListAdapter adapter = new FeedCircleTagListAdapter();
        MyGridView imageGridView;
        FullSearchFeedImagesGridAdapter imagesGridAdapter = new FullSearchFeedImagesGridAdapter(context);
        VideoPlayerTrackView videoPlayer;
        NiceImageView feedImageView;
        LinearLayout priseLayout, itemRootLayout;
        RelativeLayout closeLayout;
        LinearLayout selectLayout;
        ImageView selectImageView;
        View marginView;

        public FeedViewHolder(View itemView) {
            super(itemView);
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
            selectLayout = itemView.findViewById(R.id.selectLayout);
            selectImageView = itemView.findViewById(R.id.selectImageView);
            marginView = itemView.findViewById(R.id.marginView);
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootLayout;
        TextView titleTextView, sourceTextView, timeTextView, shareTextView, commentTextView, priseTextView;
        LinearLayout priseLayout;
        NiceImageView coverImageView;
        HorizontalListView tagListView;
        FeedCircleTagListAdapter adapter = new FeedCircleTagListAdapter();
        ImageView collectImageView, priseImageView;
        RelativeLayout closeLayout;
        LinearLayout selectLayout;
        ImageView selectImageView;
        View marginView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
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
            selectLayout = itemView.findViewById(R.id.selectLayout);
            selectImageView = itemView.findViewById(R.id.selectImageView);
            marginView = itemView.findViewById(R.id.marginView);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        FeedCircleTagListAdapter adapter = new FeedCircleTagListAdapter();
        VideoPlayerTrackView videoPlayer;
        LinearLayout priseLayout, itemRootLayout;
        RelativeLayout closeLayout;
        LinearLayout selectLayout;
        ImageView selectImageView;
        View marginView;

        public VideoViewHolder(View itemView) {
            super(itemView);
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
            selectLayout = itemView.findViewById(R.id.selectLayout);
            selectImageView = itemView.findViewById(R.id.selectImageView);
            marginView = itemView.findViewById(R.id.marginView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FEED) {
            return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_feed, parent, false));
        } else if (viewType == ARTICLE) {
            return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_article, parent, false));
        } else if (viewType == VIDEO) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draft_video, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FeedViewHolder) {
            FeedViewHolder feed = (FeedViewHolder) holder;
            if (isDeleteStatus) {
                feed.selectLayout.setVisibility(View.VISIBLE);
                feed.marginView.setVisibility(View.GONE);
            } else {
                feed.selectLayout.setVisibility(View.GONE);
                feed.marginView.setVisibility(View.VISIBLE);
            }
            if (list.get(position).isSelected()) {
                feed.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_selected));
            } else {
                feed.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_unselect));
            }
            feed.selectLayout.setOnClickListener(new OnSelectListener(position));
            feed.timeTextView.setText(list.get(position).getContent().getTime());
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                feed.mTextView.setVisibility(View.VISIBLE);
                feed.mTextView.setText(list.get(position).getContent().getContent());
            } else {
                feed.mTextView.setVisibility(View.GONE);
            }

            if (null == list.get(position).getContent().getCircle_tags() || list.get(position).getContent().getCircle_tags().size() == 0) {
                feed.tagListView.setVisibility(View.GONE);
            } else {
                feed.tagListView.setVisibility(View.VISIBLE);
                feed.adapter.setList(list.get(position).getContent().getCircle_tags());
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
                Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(feed.videoPlayer.getCoverController().getVideoCover());
                feed.videoPlayer.setLayoutParams(list.get(position).getContent().getLayoutParams(dp_240, true));
                feed.videoPlayer.getCoverController().getVideoCover().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent videoInfoIntent = new Intent(context, PublishFeedActivity.class);
                        videoInfoIntent.putExtra("feeds_id", list.get(position).getContent().getId());
                        videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(videoInfoIntent);
                    }
                });
            } else {
                if (list.get(position).getContent().getImgs().size() == 1) {
                    feed.feedImageView.setVisibility(View.VISIBLE);
                    feed.imageGridView.setVisibility(View.GONE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.feedImageView.setLayoutParams(list.get(position).getContent().getLayoutParams(dp_240, false));
                    Glide.with(context).load(list.get(position).getContent().getImgs().get(0).getFile()).error(R.mipmap.img_error)
                            .into(feed.feedImageView);
                    feed.feedImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent videoInfoIntent = new Intent(context, PublishFeedActivity.class);
                            videoInfoIntent.putExtra("feeds_id", list.get(position).getContent().getId());
                            videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(videoInfoIntent);
                        }
                    });
                } else {
                    feed.feedImageView.setVisibility(View.GONE);
                    feed.imageGridView.setVisibility(View.VISIBLE);
                    feed.videoPlayer.setVisibility(View.GONE);
                    feed.imagesGridAdapter.setList(list.get(position).getContent().getImgs());
                    feed.imagesGridAdapter.setEditFeed(true,list.get(position).getContent().getId());
                }
            }

            feed.collectImageView.setOnClickListener(new OnCollectListener(position));
            feed.shareTextView.setOnClickListener(new OnShareClick(position));
            feed.commentTextView.setOnClickListener(new OnCommentClick(position));
            feed.priseLayout.setOnClickListener(new OnPriseClick(position));
            feed.itemRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editFeedIntent = new Intent(context, PublishFeedActivity.class);
                    editFeedIntent.putExtra("feeds_id", list.get(position).getContent().getId());
                    editFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(editFeedIntent);
                }
            });
        } else if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder article = (ArticleViewHolder) holder;
            if (isDeleteStatus) {
                article.selectLayout.setVisibility(View.VISIBLE);
                article.marginView.setVisibility(View.GONE);
            } else {
                article.selectLayout.setVisibility(View.GONE);
                article.marginView.setVisibility(View.VISIBLE);
            }
            if (list.get(position).isSelected()) {
                article.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_selected));
            } else {
                article.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_unselect));
            }
            article.selectLayout.setOnClickListener(new OnSelectListener(position));
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getTitle())) {
                article.titleTextView.setVisibility(View.VISIBLE);
                article.titleTextView.setText(list.get(position).getContent().getTitle());
            } else {
                article.titleTextView.setVisibility(View.GONE);
            }
            article.sourceTextView.setText(list.get(position).getContent().getCircle_name());
            if (null != list.get(position).getContent().getThumb()) {
                article.coverImageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getContent().getThumb().getFile()).error(R.mipmap.img_error).into(article.coverImageView);
            } else {
                article.coverImageView.setVisibility(View.GONE);
            }

            article.timeTextView.setText(list.get(position).getContent().getTime());
            article.adapter.setList(list.get(position).getContent().getCircle_tags());
            if (null == list.get(position).getContent().getCircle_tags() || list.get(position).getContent().getCircle_tags().size() == 0) {
                article.tagListView.setVisibility(View.GONE);
            } else {
                article.tagListView.setVisibility(View.VISIBLE);
                article.adapter.setList(list.get(position).getContent().getCircle_tags());
            }
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
            article.priseLayout.setOnClickListener(new OnPriseClick(position));
            article.shareTextView.setOnClickListener(new OnShareClick(position));
            article.rootLayout.setOnClickListener(new OnEditArticleClick(position));
        } else if (holder instanceof VideoViewHolder) {
            VideoViewHolder video = (VideoViewHolder) holder;
            if (isDeleteStatus) {
                video.selectLayout.setVisibility(View.VISIBLE);
                video.marginView.setVisibility(View.GONE);
            } else {
                video.selectLayout.setVisibility(View.GONE);
                video.marginView.setVisibility(View.VISIBLE);
            }
            if (list.get(position).isSelected()) {
                video.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_selected));
            } else {
                video.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_unselect));
            }
            video.selectLayout.setOnClickListener(new OnSelectListener(position));
            video.timeTextView.setText(list.get(position).getContent().getTime());
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                video.mTextView.setVisibility(View.VISIBLE);
                video.mTextView.setText(list.get(position).getContent().getContent());
            } else {
                video.mTextView.setVisibility(View.GONE);
            }

            if (null == list.get(position).getContent().getCircle_tags() || list.get(position).getContent().getCircle_tags().size() == 0) {
                video.tagListView.setVisibility(View.GONE);
            } else {
                video.tagListView.setVisibility(View.VISIBLE);
                video.adapter.setList(list.get(position).getContent().getCircle_tags());
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
            Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(video.videoPlayer.getCoverController().getVideoCover());
            video.collectImageView.setOnClickListener(new OnCollectListener(position));
            video.shareTextView.setOnClickListener(new OnShareClick(position));
            video.commentTextView.setOnClickListener(new OnCommentClick(position));
            video.priseLayout.setOnClickListener(new OnPriseClick(position));
            video.videoPlayer.getCoverController().getVideoCover().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent videoInfoIntent = new Intent(context, PublishVideoActivity.class);
                    videoInfoIntent.putExtra("feeds_id", list.get(position).getContent().getId());
                    videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(videoInfoIntent);
                }
            });
            video.itemRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent videoInfoIntent = new Intent(context, PublishVideoActivity.class);
                    videoInfoIntent.putExtra("feeds_id", list.get(position).getContent().getId());
                    videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(videoInfoIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getFeed_type();
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
            shareDialog.show();

        }
    }

    private class OnCommentClick implements View.OnClickListener {
        private int position;

        public OnCommentClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            Intent commentIntent=new Intent(context,Comment)
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

    private class OnEditArticleClick implements View.OnClickListener {
        private int position;

        public OnEditArticleClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent articleInfoIntent = new Intent(context, PublishArticleActivity.class);
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


    private class OnSelectListener implements View.OnClickListener {
        private int position;

        public OnSelectListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelected(!list.get(position).isSelected());
            notifyItemChanged(position);
        }
    }

}
