package com.zhiyu.quanzhu.ui.adapter;

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
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FeedCommentChild;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.result.FeedChildCommentResult;
import com.zhiyu.quanzhu.ui.activity.CommentInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ComplaintActivity;
import com.zhiyu.quanzhu.ui.popupwindow.CommentMenuWindow;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyListView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyBoardUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class ArticleInfoCommentListParentAdapter extends BaseAdapter implements ArticleInfoCommentListChildAdapter.OnReplyChildCommentListener {
    private List<FeedCommentParent> list;
    private Context context;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ArticleInfoCommentListParentAdapter> adapterWeakReference;

        public MyHandler(ArticleInfoCommentListParentAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            ArticleInfoCommentListParentAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).setIs_prise(!adapter.list.get(posiiton).isIs_prise());
                        int priseCount = adapter.list.get(posiiton).getPnum();
                        adapter.list.get(posiiton).setPnum(adapter.list.get(posiiton).isIs_prise() ? priseCount + 1 : priseCount - 1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        int posiiton = (Integer) msg.obj;
                        adapter.list.get(posiiton).setIs_del(1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 3:

                    break;
            }
        }
    }

    public ArticleInfoCommentListParentAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<FeedCommentParent> commentList) {
        this.list = commentList;
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
        TextView nameTextView, contentTextView, replyTextView, priseNumTextView, timeTextView, expandCommentsTextView;
        ImageView priseImageView;
        MyListView mListView;
        ArticleInfoCommentListChildAdapter adapter = new ArticleInfoCommentListChildAdapter(context);
        LinearLayout priseLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_info_comment_listview_parent, null);
            holder.mListView = convertView.findViewById(R.id.mListView);
            holder.mListView.setAdapter(holder.adapter);
            holder.avatarImageView = convertView.findViewById(R.id.avatarImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.priseImageView = convertView.findViewById(R.id.priseImageView);
            holder.priseNumTextView = convertView.findViewById(R.id.priseNumTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            holder.contentTextView = convertView.findViewById(R.id.contentTextView);
            holder.expandCommentsTextView = convertView.findViewById(R.id.expandCommentsTextView);
            holder.replyTextView = convertView.findViewById(R.id.replyTextView);
            holder.priseLayout = convertView.findViewById(R.id.priseLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getAvatar()).into(holder.avatarImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        if (list.get(position).getIs_del() == 1) {
            holder.contentTextView.setText("该评论已删除");
        } else {
            holder.contentTextView.setText(list.get(position).getContent());
            holder.contentTextView.setOnLongClickListener(new OnCommentMenuLongClickListener(position, holder.contentTextView));
        }

        holder.priseNumTextView.setText(String.valueOf(list.get(position).getPnum()));
        if (list.get(position).isIs_prise()) {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        holder.timeTextView.setText(list.get(position).getDateline());
        holder.replyTextView.setOnClickListener(new OnReplyCommentClick(position));
        if (null == list.get(position).getReply() || list.get(position).getReply().size() == 0) {
            holder.mListView.setVisibility(View.GONE);
        } else {
            holder.mListView.setVisibility(View.VISIBLE);
            holder.adapter.setList(list.get(position).getReply());
            holder.adapter.setOnReplyChildCommentListener(this);
        }

        if (list.get(position).getReply_total() > 2) {
            holder.expandCommentsTextView.setVisibility(View.VISIBLE);
            holder.expandCommentsTextView.setText("展开10条回复");
            list.get(position).setLast_id(holder.adapter.getLastId());
            list.get(position).setPage(list.get(position).getPage() + 1);
            holder.expandCommentsTextView.setOnClickListener(new OnReplyCommentListClick(position));
        } else {
            holder.expandCommentsTextView.setVisibility(View.GONE);
        }

        holder.priseLayout.setOnClickListener(new OnPriseClick(position));
        return convertView;
    }

    private class OnReplyCommentClick implements View.OnClickListener {
        private int position;

        public OnReplyCommentClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onReplyParentCommentListener) {
                onReplyParentCommentListener.onReplyParentComment(list.get(position).getId());
            }
        }
    }

    private OnReplyParentCommentListener onReplyParentCommentListener;

    public void setOnReplyParentCommentListener(OnReplyParentCommentListener listener) {
        this.onReplyParentCommentListener = listener;
    }

    public interface OnReplyParentCommentListener {
        void onReplyParentComment(int comment_id);
    }

    @Override
    public void onReplyChildComment(int comment_id) {
        if (null != onReplyParentCommentListener) {
            onReplyParentCommentListener.onReplyParentComment(comment_id);
        }
    }

    private class OnCommentMenuLongClickListener implements View.OnLongClickListener {
        private int position;
        private TextView textView;

        public OnCommentMenuLongClickListener(int position, TextView textView) {
            this.position = position;
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View v) {
            int[] location = new int[2];
            textView.getLocationOnScreen(location);
            final int y = location[1];
            new CommentMenuWindow(context).showMeu(textView, y, (list.get(position).isIs_author() ||
                    list.get(position).isIs_circle_own() || list.get(position).isIs_own() ||
                    list.get(position).isIs_manger()), new CommentMenuWindow.OnMenuClickListener() {
                @Override
                public void onMenuClick(int index) {
                    switch (index) {
                        case 1:
                            System.out.println("复制");
                            boolean copySuccess = CopyBoardUtils.getInstance().copy(context, list.get(position).getContent());
                            if (copySuccess) {
                                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            System.out.println("删除");
                            deleteComment(position);
                            break;
                        case 3:
                            System.out.println("投诉");
                            Intent complaintIntent = new Intent(context, ComplaintActivity.class);
                            complaintIntent.putExtra("report_id", list.get(position).getId());
                            complaintIntent.putExtra("module_type", "feeds_comment");
                            complaintIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(complaintIntent);
                            break;
                    }
                }
            });
            return false;
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
        params.addBodyParameter("prise_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("module_type", "feedscomment");
        params.addBodyParameter("type", (list.get(position).isIs_prise() ? "1" : "0"));
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

    private void deleteComment(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_COMMENT);
        params.addBodyParameter("comment_id", String.valueOf(list.get(position).getId()));
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

    private class OnReplyCommentListClick implements View.OnClickListener {
        private int position;

        public OnReplyCommentListClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
           Intent commentInfoIntent=new Intent(context, CommentInformationActivity.class);
            commentInfoIntent.putExtra("comment_id",list.get(position).getId());
            commentInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(commentInfoIntent);

        }
    }


}
