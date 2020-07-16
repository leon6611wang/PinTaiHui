package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.constants.VideoConstants;
import com.leon.myvideoplaerlibrary.utils.VideoUtils;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.leon.myvideoplaerlibrary.view.VideoTextrueProvider;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FullSearchVideo;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;
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


public class FullSearchVideoListAdapter extends BaseAdapter {
    private Context context;
    private int width, height;
    private List<FullSearchVideo> list;
    private ShareDialog shareDialog;
    private Activity activity;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FullSearchVideoListAdapter> adapterWeakReference;

        public MyHandler(FullSearchVideoListAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            FullSearchVideoListAdapter adapter = adapterWeakReference.get();
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
            }
        }
    }

    public void setList(List<FullSearchVideo> videoList) {
        this.list = videoList;
        notifyDataSetChanged();
    }

    public void setShareResultCode(int requestCode, int resultCode, Intent data) {
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }

    public FullSearchVideoListAdapter(Activity aty, Context context) {
        this.context = context;
        this.activity = aty;
        shareConfig();
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        height = (int) context.getResources().getDimension(R.dimen.dp_240);
        int dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
        width = screenWidth - dp_30;
        initDialogs();
    }

    private void initDialogs() {
        shareDialog = new ShareDialog(activity, context, R.style.dialog);
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

    class ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, timeTextView, sourceTextView, shareTextView, commentTextView, priseNumTextView;
        ImageView collectImageView, priseImageView;
        ExpandableTextView mTextView;
        HorizontalListView tagListView;
        FullSearchFeedTagListAdapter adapter = new FullSearchFeedTagListAdapter();
        VideoPlayerTrackView videoPlayer;
        LinearLayout priseLayout, itemRootLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_video, null);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            holder.mTextView = convertView.findViewById(R.id.mTextView);
            holder.collectImageView = convertView.findViewById(R.id.collectImageView);
            holder.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
            holder.priseLayout = convertView.findViewById(R.id.priseLayout);
            holder.shareTextView = convertView.findViewById(R.id.shareTextView);
            holder.commentTextView = convertView.findViewById(R.id.commentTextView);
            holder.priseImageView = convertView.findViewById(R.id.priseImageView);
            holder.priseNumTextView = convertView.findViewById(R.id.priseNumTextView);
            holder.tagListView = convertView.findViewById(R.id.tagListView);
            holder.sourceTextView = convertView.findViewById(R.id.sourceTextView);
            holder.itemRootLayout = convertView.findViewById(R.id.itemRootLayout);
            holder.tagListView.setAdapter(holder.adapter);
            holder.videoPlayer = convertView.findViewById(R.id.videoPlayer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(list.get(position).getContent().getAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getContent().getUsername());
        holder.timeTextView.setText(list.get(position).getContent().getTime());
        holder.mTextView.setText(list.get(position).getContent().getContent());
        if (null == list.get(position).getContent().getFeeds_tags() || list.get(position).getContent().getFeeds_tags().size() == 0) {
            holder.tagListView.setVisibility(View.GONE);
        } else {
            holder.tagListView.setVisibility(View.VISIBLE);
            holder.adapter.setList(list.get(position).getContent().getFeeds_tags());
        }
        holder.sourceTextView.setText(list.get(position).getContent().getCircle_name());
        if (list.get(position).getContent().isIs_collect()) {
            holder.collectImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.shoucang_yellow));
        } else {
            holder.collectImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.shoucang_gray));
        }
        holder.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
        holder.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
        holder.priseNumTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
        if (list.get(position).getContent().isIs_prise()) {
            holder.priseImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.priseImageView.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        holder.videoPlayer.setLayoutParams(list.get(position).getContent().getLayoutParams(width, height));
        holder.videoPlayer.setDataSource(list.get(position).getContent().getVideo_url(), "");
        Glide.with(context).load(list.get(position).getContent().getVideo_thumb()).into(holder.videoPlayer.getCoverController().getVideoCover());
//        Glide.with(convertView).load(list.get(position).getContent().getVideo_url()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into( holder.videoPlayer.getCoverController().getVideoCover());
        holder.collectImageView.setOnClickListener(new OnCollectListener(position));
        holder.shareTextView.setOnClickListener(new OnShareClick(position));
        holder.commentTextView.setOnClickListener(new OnCommentClick(position));
        holder.priseLayout.setOnClickListener(new OnPriseClick(position));
        holder.itemRootLayout.setOnClickListener(new OnVideoInformationClick(position));
        return convertView;
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
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getVideo_thumb())) {
                shareResult.getData().getShare().setImage_url(list.get(position).getContent().getVideo_thumb());
            }
            if (!StringUtils.isNullOrEmpty(list.get(position).getContent().getContent())) {
                shareResult.getData().getShare().setContent(list.get(position).getContent().getContent());
            }
            shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_VIDEO);
            shareDialog.show();
            shareDialog.setShare(shareResult.getData().getShare(), list.get(position).getContent().getId());

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

    private ShareResult shareResult;

    private void shareConfig() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_FEED);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult = GsonUtils.GsonToBean(result, ShareResult.class);
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
