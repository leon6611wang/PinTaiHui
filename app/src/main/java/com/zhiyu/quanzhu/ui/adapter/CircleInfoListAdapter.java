package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CircleInfoFeed;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.WrapImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class CircleInfoListAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private MyHandler myHandler = new MyHandler(this);
    private ShareDialog shareDialog;

    private static class MyHandler extends Handler {
        WeakReference<CircleInfoListAdapter> adapterWeakReference;

        public MyHandler(CircleInfoListAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleInfoListAdapter adapter = adapterWeakReference.get();
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

    public CircleInfoListAdapter(Activity aty,Context context) {
        this.activity=aty;
        this.context = context;
        initDialog();
    }

    private void initDialog(){
        shareDialog=new ShareDialog(activity,context,R.style.dialog);
    }

    private List<CircleInfoFeed> list;

    public void setList(List<CircleInfoFeed> feedList) {
        this.list = feedList;
        notifyDataSetChanged();
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
        TextView nameTextView, timeTextView, mTextView;
        WrapImageView feedImageView;
        MyGridView imageGridView;
        CircleInfoFeedImagesGridAdapter imagesGridAdapter = new CircleInfoFeedImagesGridAdapter(context);
        VideoPlayerTrackView videoPlayer;
        HorizontalListView tagListView;
        CircleInfoTagListAdapter adapter = new CircleInfoTagListAdapter();
        ImageView collectImageView, priseImageView;
        TextView shareTextView, commentTextView, priseTextView;
        LinearLayout priseLayout,rootLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_info, null);
            holder.rootLayout=convertView.findViewById(R.id.rootLayout);
            holder.priseLayout = convertView.findViewById(R.id.priseLayout);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            holder.mTextView = convertView.findViewById(R.id.mTextView);
            holder.collectImageView = convertView.findViewById(R.id.collectImageView);
            holder.shareTextView = convertView.findViewById(R.id.shareTextView);
            holder.commentTextView = convertView.findViewById(R.id.commentTextView);
            holder.priseImageView = convertView.findViewById(R.id.priseImageView);
            holder.priseTextView = convertView.findViewById(R.id.priseTextView);
            holder.feedImageView = convertView.findViewById(R.id.feedImageView);
            holder.imageGridView = convertView.findViewById(R.id.imageGridView);
            holder.videoPlayer = convertView.findViewById(R.id.videoPlayer);
            holder.imageGridView.setAdapter(holder.imagesGridAdapter);
            holder.tagListView = convertView.findViewById(R.id.tagListView);
            holder.tagListView.setAdapter(holder.adapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getContent().isIs_collect()) {
            holder.collectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.shoucang_yellow));
        } else {
            holder.collectImageView.setImageDrawable(convertView.getResources().getDrawable(R.mipmap.shoucang_gray));
        }
        holder.shareTextView.setText(String.valueOf(list.get(position).getContent().getShare_num()));
        holder.commentTextView.setText(String.valueOf(list.get(position).getContent().getComment_num()));
        holder.priseTextView.setText(String.valueOf(list.get(position).getContent().getPrise_num()));
        if (list.get(position).getContent().isIs_prise()) {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        Glide.with(parent.getContext()).load(list.get(position).getContent().getAvatar()).error(R.mipmap.no_avatar).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getContent().getUsername());
        holder.timeTextView.setText(list.get(position).getContent().getTime());
        holder.mTextView.setText(list.get(position).getContent().getContent());
        holder.collectImageView.setOnClickListener(new OnCollectListener(position));
        holder.priseLayout.setOnClickListener(new OnPriseClick(position));
        holder.shareTextView.setOnClickListener(new OnShareClick(position));
        holder.commentTextView.setOnClickListener(new OnCommentClick(position));
        holder.rootLayout.setOnClickListener(new OnFeedInfoClick(position));
        holder.feedImageView.setVisibility(View.GONE);
        holder.imageGridView.setVisibility(View.GONE);
        holder.videoPlayer.setVisibility(View.GONE);
        switch (list.get(position).getType()) {
            case 4:
                holder.imageGridView.setVisibility(View.VISIBLE);
                holder.imagesGridAdapter.setList(list.get(position).getContent().getImgs());
                break;
        }
        return convertView;
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

    private class OnFeedInfoClick implements View.OnClickListener{
        private int position;

        public OnFeedInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent feedInfoIntent=new Intent(context, FeedInformationActivity.class);
            feedInfoIntent.putExtra("feed_id",list.get(position).getContent().getId());
            feedInfoIntent.putExtra("feed_type",String.valueOf(list.get(position).getType()));
            feedInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(feedInfoIntent);
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
}
