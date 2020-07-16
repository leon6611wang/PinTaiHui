package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FooterPrintComment;
import com.zhiyu.quanzhu.ui.activity.ArticleInformationActivity;
import com.zhiyu.quanzhu.ui.activity.CommentInformationActivity;
import com.zhiyu.quanzhu.ui.activity.FeedInformationActivity;
import com.zhiyu.quanzhu.ui.activity.VideoInformationActivity;

import java.util.List;

public class PingLunRecyclerAdapter extends RecyclerView.Adapter<PingLunRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<FooterPrintComment> list;

    public void setList(List<FooterPrintComment> commentList) {
        this.list = commentList;
        notifyDataSetChanged();
    }

    public PingLunRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewCommentTextView, timeTextView, titleTextView, contentTextView;
        View readView;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewCommentTextView = itemView.findViewById(R.id.reviewCommentTextView);
            readView = itemView.findViewById(R.id.readView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinglun, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getIs_read() == 1) {
            holder.readView.setVisibility(View.GONE);
        } else {
            holder.readView.setVisibility(View.VISIBLE);
        }
        holder.timeTextView.setText(list.get(position).getDateline());
        holder.titleTextView.setText(list.get(position).getFeeds_title());
        holder.contentTextView.setText("评论内容：" + list.get(position).getContent());
        holder.reviewCommentTextView.setOnClickListener(new OnReviewCommentClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnReviewCommentClick implements View.OnClickListener {
        private int position;

        public OnReviewCommentClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (list.get(position).getFeeds_type()) {
                case 1://文章
                    Intent articleIntent = new Intent(context, ArticleInformationActivity.class);
                    articleIntent.putExtra("article_id", list.get(position).getFeeds_id());
                    articleIntent.putExtra("myCommentId", list.get(position).getId());
                    articleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(articleIntent);
                    break;
                case 2://视频
                    Intent videoIntent = new Intent(context, VideoInformationActivity.class);
                    videoIntent.putExtra("feeds_id", list.get(position).getFeeds_id());
                    videoIntent.putExtra("myCommentId", list.get(position).getId());
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(videoIntent);
                    break;
                case 3://动态
                    Intent feedsIntent = new Intent(context, FeedInformationActivity.class);
                    feedsIntent.putExtra("feed_id", list.get(position).getFeeds_id());
                    feedsIntent.putExtra("myCommentId", list.get(position).getId());
                    feedsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(feedsIntent);
                    break;
            }
//            list.get(position).setIs_read(1);
//            notifyItemChanged(position);
//            Intent commentIntent = new Intent(context, CommentInformationActivity.class);
//            commentIntent.putExtra("comment_id", list.get(position).getId());
//            commentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(commentIntent);
        }
    }
}
