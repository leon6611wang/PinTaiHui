package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ComplaintActivity;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.DeleteFeedDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
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
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * 首页-圈子-关注列表适配器
 */
public class CircleInfoFeedsAdapter extends BaseAdapter {
    private List<Feed> list;
    private ShareDialog shareDialog;
    private DeleteFeedDialog deleteFeedDialog;
    private Activity activity;
    private Context context;
    private int dp_240;
    private int width, height;
    private MyHandler myHandler = new MyHandler(this);

    public CircleInfoFeedsAdapter(Activity aty, final Context context) {
        this.context = context;
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
//                System.out.println("index: " + index + " , " + desc);
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

    public void setShareResultCode(int requestCode, int resultCode, Intent data) {
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }

    private static class MyHandler extends Handler {
        WeakReference<CircleInfoFeedsAdapter> adapterWeakReference;

        public MyHandler(CircleInfoFeedsAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleInfoFeedsAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).getContent().setIs_collect(!adapter.list.get(posiiton).getContent().isIs_collect());
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).getContent().setIs_prise(!adapter.list.get(posiiton).getContent().isIs_prise());
                        int priseCount = adapter.list.get(posiiton).getContent().getPrise_num();
                        adapter.list.get(posiiton).getContent().setPrise_num(adapter.list.get(posiiton).getContent().isIs_prise() ? priseCount + 1 : priseCount - 1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.remove(posiiton);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public void setList(List<Feed> feedList) {
        this.list = feedList;
        notifyDataSetChanged();
    }

    private boolean isStop;

    public void setVideoStop(boolean stop) {
        this.isStop = stop;
        notifyDataSetChanged();
    }

    class FeedViewHolder {
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
    }

    class ArticleViewHolder {
        LinearLayout rootLayout;
        CircleImageView avatarImageView;
        TextView nameTextView, titleTextView, sourceTextView, timeTextView, shareTextView, commentTextView, priseTextView;
        LinearLayout priseLayout;
        NiceImageView coverImageView;
        HorizontalListView tagListView;
        CircleGuanZhuFeedTagListAdapter adapter = new CircleGuanZhuFeedTagListAdapter();
        ImageView collectImageView, priseImageView;
        RelativeLayout closeLayout;
    }

    class VideoViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        CircleGuanZhuFeedTagListAdapter adapter = new CircleGuanZhuFeedTagListAdapter();
        VideoPlayerTrackView videoPlayer;
        LinearLayout priseLayout, itemRootLayout;
        RelativeLayout closeLayout;
    }


    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ArticleViewHolder article = null;
        VideoViewHolder video = null;
        FeedViewHolder feed = null;
        if (null == convertView) {
            switch (type) {
                case ARTICLE:
                    article = new ArticleViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_article, parent, false);
                    article.rootLayout = convertView.findViewById(R.id.rootLayout);
                    article.avatarImageView = convertView.findViewById(R.id.avatarImageView);
                    article.nameTextView = convertView.findViewById(R.id.nameTextView);
                    article.titleTextView = convertView.findViewById(R.id.titleTextView);
                    article.coverImageView = convertView.findViewById(R.id.coverImageView);
                    article.sourceTextView = convertView.findViewById(R.id.sourceTextView);
                    article.timeTextView = convertView.findViewById(R.id.timeTextView);
                    article.collectImageView = convertView.findViewById(R.id.collectImageView);
                    article.shareTextView = convertView.findViewById(R.id.shareTextView);
                    article.commentTextView = convertView.findViewById(R.id.commentTextView);
                    article.priseLayout = convertView.findViewById(R.id.priseLayout);
                    article.priseImageView = convertView.findViewById(R.id.priseImageView);
                    article.priseTextView = convertView.findViewById(R.id.priseTextView);
                    article.tagListView = convertView.findViewById(R.id.tagListView);
                    article.tagListView.setAdapter(article.adapter);
                    article.closeLayout = convertView.findViewById(R.id.closeLayout);
                    convertView.setTag(article);
                    break;
                case VIDEO:
                    video = new VideoViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_video, parent, false);
                    video.avatarImageView = convertView.findViewById(R.id.avatarImageView);
                    video.nameTextView = convertView.findViewById(R.id.nameTextView);
                    video.timeTextView = convertView.findViewById(R.id.timeTextView);
                    video.mTextView = convertView.findViewById(R.id.mTextView);
                    video.collectImageView = convertView.findViewById(R.id.collectImageView);
                    video.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
                    video.priseLayout = convertView.findViewById(R.id.priseLayout);
                    video.shareTextView = convertView.findViewById(R.id.shareTextView);
                    video.commentTextView = convertView.findViewById(R.id.commentTextView);
                    video.priseImageView = convertView.findViewById(R.id.priseImageView);
                    video.priseNumTextView = convertView.findViewById(R.id.priseNumTextView);
                    video.tagListView = convertView.findViewById(R.id.tagListView);
                    video.sourceTextView = convertView.findViewById(R.id.sourceTextView);
                    video.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
                    video.tagListView.setAdapter(video.adapter);
                    video.videoPlayer = convertView.findViewById(R.id.videoPlayer);
                    video.closeLayout = convertView.findViewById(R.id.closeLayout);
                    convertView.setTag(video);
                    break;
                case FEED:
                    feed = new FeedViewHolder();
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_guanzhu_feed, parent, false);
                    feed.avatarImageView = convertView.findViewById(R.id.avatarImageView);
                    feed.nameTextView = convertView.findViewById(R.id.nameTextView);
                    feed.timeTextView = convertView.findViewById(R.id.timeTextView);
                    feed.mTextView = convertView.findViewById(R.id.mTextView);
                    feed.collectImageView = convertView.findViewById(R.id.collectImageView);
                    feed.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
                    feed.priseLayout = convertView.findViewById(R.id.priseLayout);
                    feed.shareTextView = convertView.findViewById(R.id.shareTextView);
                    feed.commentTextView = convertView.findViewById(R.id.commentTextView);
                    feed.priseImageView = convertView.findViewById(R.id.priseImageView);
                    feed.priseNumTextView = convertView.findViewById(R.id.priseNumTextView);
                    feed.tagListView = convertView.findViewById(R.id.tagListView);
                    feed.sourceTextView = convertView.findViewById(R.id.sourceTextView);
                    feed.tagListView.setAdapter(feed.adapter);
                    feed.imageGridView = convertView.findViewById(R.id.imageGridView);
                    feed.imageGridView.setAdapter(feed.imagesGridAdapter);
                    feed.videoPlayer = convertView.findViewById(R.id.videoPlayer);
                    feed.feedImageView = convertView.findViewById(R.id.feedImageView);
                    feed.closeLayout = convertView.findViewById(R.id.closeLayout);
                    convertView.setTag(feed);
                    break;
            }
        } else {
            switch (type) {
                case ARTICLE:
                    article = (ArticleViewHolder) convertView.getTag();
                    break;
                case VIDEO:
                    video = (VideoViewHolder) convertView.getTag();
                    break;
                case FEED:
                    feed = (FeedViewHolder) convertView.getTag();
                    break;
            }
        }

        switch (type) {
            case ARTICLE:
                Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.drawable.image_error).into(article.avatarImageView);
                article.nameTextView.setText(list.get(position).getContent().getUsername());
                article.titleTextView.setText(list.get(position).getContent().getTitle());
                if (null != list.get(position).getContent().getNewthumb()) {
                    article.coverImageView.setVisibility(View.VISIBLE);
                    Glide.with(context).load(list.get(position).getContent().getNewthumb().getFile()).error(R.drawable.image_error).into(article.coverImageView);
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
                article.priseLayout.setOnClickListener(new OnPriseClick(position));
                article.shareTextView.setOnClickListener(new OnShareClick(position));
                article.rootLayout.setOnClickListener(new OnArticleInfoClick(position));
                article.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
                break;
            case VIDEO:
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
                Glide.with(context).load(list.get(position).getContent().getVideo_thumb()).into(video.videoPlayer.getCoverController().getVideoCover());
//                Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(video.videoPlayer.getCoverController().getVideoCover());
                video.collectImageView.setOnClickListener(new OnCollectListener(position));
                video.shareTextView.setOnClickListener(new OnShareClick(position));
                video.commentTextView.setOnClickListener(new OnCommentClick(position));
                video.priseLayout.setOnClickListener(new OnPriseClick(position));
                video.itemRootLayout.setOnClickListener(new OnVideoInformationClick(position));
                video.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
                if (isStop && video.videoPlayer.isPlaying()) {
                    video.videoPlayer.pause();
                }
                break;
            case FEED:
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
//                    Glide.with(context).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(feed.videoPlayer.getCoverController().getVideoCover());
                    feed.videoPlayer.setLayoutParams(list.get(position).getContent().getLayoutParams(dp_240, true));
                } else {
                    if (null == list.get(position).getContent().getImgs() || list.get(position).getContent().getImgs().size() == 0) {
                        feed.feedImageView.setVisibility(View.GONE);
                        feed.imageGridView.setVisibility(View.GONE);
                        feed.videoPlayer.setVisibility(View.GONE);
                    } else if (null != list.get(position).getContent().getImgs() && list.get(position).getContent().getImgs().size() == 1) {
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

                feed.collectImageView.setOnClickListener(new OnCollectListener(position));
                feed.shareTextView.setOnClickListener(new OnShareClick(position));
                feed.commentTextView.setOnClickListener(new OnCommentClick(position));
                feed.priseLayout.setOnClickListener(new OnPriseClick(position));
                feed.itemRootLayout.setOnClickListener(new OnCommentClick(position));
                feed.closeLayout.setOnClickListener(new OnDeleteFeedClick(position));
                if (isStop && feed.videoPlayer.isPlaying()) {
                    feed.videoPlayer.pause();
                }
                break;
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getFeeds_type() - 1;
    }


    private static OnMoreCircleClickListener onMoreCircleClickListener;

    public static void setOnMoreCircleClickListener(OnMoreCircleClickListener listener) {
        onMoreCircleClickListener = listener;
    }

    public interface OnMoreCircleClickListener {
        void onMoreCircle();
    }


    private final int ARTICLE = 0, VIDEO = 1, FEED = 2;


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
            Intent commentIntent = new Intent(context, FeedInformationActivity.class);
            commentIntent.putExtra("feed_id", list.get(position).getContent().getId());
            commentIntent.putExtra("fromCircleInfo",1);
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
            articleInfoIntent.putExtra("fromCircleInfo",1);
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
            videoInfoIntent.putExtra("fromCircleInfo",1);
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

}
