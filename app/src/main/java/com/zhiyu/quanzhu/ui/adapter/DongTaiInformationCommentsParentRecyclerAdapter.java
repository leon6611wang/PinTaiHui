package com.zhiyu.quanzhu.ui.adapter;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CommentParent;
import com.zhiyu.quanzhu.model.result.CommentChildResult;
import com.zhiyu.quanzhu.ui.activity.ComplaintActivity;
import com.zhiyu.quanzhu.ui.dialog.AddCommentDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.popupwindow.CommentMenuWindow;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.ui.widget.MyLocationRecyclerView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * 动态详情评论-父级
 */
public class DongTaiInformationCommentsParentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private AddCommentDialog addCommentDialog;
    private List<CommentParent> list;
    private MyLocationRecyclerView myLocationRecyclerView;
    private MyHandler myHandler = new MyHandler(this);
    private YNDialog ynDialog;
    private int deletePosition = -1, prisePosition;
    private int total;

    private static class MyHandler extends Handler {
        WeakReference<DongTaiInformationCommentsParentRecyclerAdapter> adapterWeakReference;

        public MyHandler(DongTaiInformationCommentsParentRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            DongTaiInformationCommentsParentRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        if (null != adapter.onReplySuccessListener) {
                            adapter.onReplySuccessListener.onReplySuccess();
                        }
                    }
                    break;
                case 2:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        adapter.list.remove(adapter.deletePosition);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    if (adapter.baseResult.getCode() == 200) {
                        adapter.list.get(adapter.prisePosition).setIs_prise(!adapter.list.get(adapter.prisePosition).isIs_prise());
                        if (adapter.list.get(adapter.prisePosition).isIs_prise()) {
                            adapter.list.get(adapter.prisePosition).setPnum(adapter.list.get(adapter.prisePosition).getPnum() + 1);
                        } else {
                            adapter.list.get(adapter.prisePosition).setPnum(adapter.list.get(adapter.prisePosition).getPnum() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public void setList(List<CommentParent> parentList, int total) {
        this.list = parentList;
        this.total = total;
        notifyDataSetChanged();
    }

    private void initDialog() {
        addCommentDialog = new AddCommentDialog(context, R.style.inputDialog, new AddCommentDialog.OnAddCommentListener() {
            @Override
            public void onAddComment(String comment, int commentId) {
                dongtai_comment(comment, commentId);
            }
        });

        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                deleteComment(list.get(deletePosition).getId());
            }
        });
    }

    public DongTaiInformationCommentsParentRecyclerAdapter(Context context, MyLocationRecyclerView recyclerView) {
        this.context = context;
        this.myLocationRecyclerView = recyclerView;
        initDialog();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongtai_information_comments_parent, parent, false));
        } else if (viewType == 1) {
            return new ImagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongtai_information_header_text_images, parent, false));
        } else if (viewType == 2) {
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongtai_information_header_text_video, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myHolder, int position) {
        if (list.get(position).getAdapter_type() == 0 && myHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) myHolder;
            CommentParent commentParent = list.get(position);
            Glide.with(context).load(commentParent.getAvatar())
                    .error(R.mipmap.no_avatar)
                    .into(holder.avatarImageView);
            holder.publishTimeTextView.setText(commentParent.getDateline());
            holder.priseCountTextView.setText(String.valueOf(commentParent.getPnum()));
            if (list.get(position).isIs_prise()) {
                holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
            } else {
                holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
            }
            holder.priseLayout.setOnClickListener(new OnPriseClickListener(position));
            holder.nicknameTextView.setText(commentParent.getUsername());
            holder.contentTextView.setText(commentParent.getId() + "-" + commentParent.getContent());
            holder.contentTextView.setOnLongClickListener(new OnTextLongClick(position, holder.contentTextView));
            holder.adapter.setList(commentParent.getReply());
            if (commentParent.isReply_more()) {
                holder.expandCommentsTextView.setVisibility(View.VISIBLE);
            } else {
                holder.expandCommentsTextView.setVisibility(View.GONE);
            }
            holder.expandCommentsTextView.setOnClickListener(new OnExpandCommentsClick(commentParent, holder.adapter));
            if (null == commentParent.getReply() || commentParent.getReply().size() == 0) {
                holder.mRecyclerView.setVisibility(View.GONE);
            } else {
                holder.mRecyclerView.setVisibility(View.VISIBLE);
            }

            holder.replyCommentTextView.setOnClickListener(new OnReplyCommentClick(position));
        } else if (list.get(position).getAdapter_type() == 1 && myHolder instanceof ImagesViewHolder) {
            ImagesViewHolder holder = (ImagesViewHolder) myHolder;
            holder.commentCountTextView.setText("评论(" + total + ")");
            holder.viewNumTextView.setText(list.get(0).getInformation().getView_num() + "浏览");
            holder.mTextView.setText(list.get(0).getInformation().getDesc());
            holder.sourceTextView.setText(list.get(0).getInformation().getCircle().getName());
            holder.dateTextView.setText(list.get(0).getInformation().getTime());
            holder.imageGridAdapter.setData(list.get(0).getInformation().getImgs());
            if (list.get(0).getInformation().isIs_report()) {
                holder.complaintImageView.setVisibility(View.GONE);
                holder.complaintTextView.setText("已投诉");
            } else {
                holder.complaintImageView.setVisibility(View.VISIBLE);
                holder.complaintTextView.setText("投诉");
            }
            holder.complaintLayout.setOnClickListener(new OncomplaintFeedClick(list.get(0).getInformation().getId()));
        } else if (list.get(position).getAdapter_type() == 2 && myHolder instanceof VideoViewHolder) {
            VideoViewHolder holder = (VideoViewHolder) myHolder;
            holder.commentCountTextView.setText("评论(" + total + ")");
            holder.viewNumTextView.setText(list.get(0).getInformation().getView_num() + "浏览");
            holder.mTextView.setText(list.get(0).getInformation().getDesc());
            holder.sourceTextView.setText(list.get(0).getInformation().getCircle().getName());
            holder.dateTextView.setText(list.get(0).getInformation().getTime());
            if (list.get(0).getInformation().isIs_report()) {
                holder.complaintImageView.setVisibility(View.GONE);
                holder.complaintTextView.setText("已投诉");
            } else {
                holder.complaintImageView.setVisibility(View.VISIBLE);
                holder.complaintTextView.setText("投诉");
            }
            holder.complaintLayout.setOnClickListener(new OncomplaintFeedClick(list.get(0).getInformation().getId()));
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size() + 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getAdapter_type();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        DongTaiInformationCommentsChildRecyclerAdapter adapter;
        MyLocationRecyclerView mRecyclerView;
        LinearLayoutManager linearLayoutManager;
        TextView expandCommentsTextView;
        TextView contentTextView, nicknameTextView, priseCountTextView, publishTimeTextView, replyCommentTextView;
        CircleImageView avatarImageView;
        ImageView priseImageView;
        LinearLayout priseLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new DongTaiInformationCommentsChildRecyclerAdapter(context);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(adapter);
            expandCommentsTextView = itemView.findViewById(R.id.expandCommentsTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
            priseCountTextView = itemView.findViewById(R.id.priseCountTextView);
            priseImageView = itemView.findViewById(R.id.priseImageView);
            priseLayout = itemView.findViewById(R.id.priseLayout);
            publishTimeTextView = itemView.findViewById(R.id.publishTimeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            replyCommentTextView = itemView.findViewById(R.id.replyCommentTextView);
            adapter.setOnReplySuccessListener(new DongTaiInformationCommentsChildRecyclerAdapter.OnReplySuccessListener() {
                @Override
                public void onChildReplySuccess() {
                    if (null != onReplySuccessListener) {
                        onReplySuccessListener.onReplySuccess();
                    }
                }
            });
        }
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {
        TextView commentCountTextView, viewNumTextView, complaintTextView, dateTextView, sourceTextView, mTextView;
        LinearLayout complaintLayout;
        ImageView complaintImageView;
        MyRecyclerView labelRecyclerView;
        LabelRecyclerAdapter adapter;
        MyGridView imageGridView;
        ImageGridAdapter imageGridAdapter;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            viewNumTextView = itemView.findViewById(R.id.viewNumTextView);
            complaintLayout = itemView.findViewById(R.id.complaintLayout);
            complaintImageView = itemView.findViewById(R.id.complaintImageView);
            complaintTextView = itemView.findViewById(R.id.complaintTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            labelRecyclerView = itemView.findViewById(R.id.labelRecyclerView);
            mTextView = itemView.findViewById(R.id.mTextView);
            imageGridView = itemView.findViewById(R.id.imageGridView);
            imageGridAdapter = new ImageGridAdapter(context);
            imageGridView.setAdapter(imageGridAdapter);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView commentCountTextView, viewNumTextView, complaintTextView, dateTextView, sourceTextView, mTextView;
        LinearLayout complaintLayout;
        ImageView complaintImageView;
        MyRecyclerView labelRecyclerView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            commentCountTextView = itemView.findViewById(R.id.commentCountTextView);
            viewNumTextView = itemView.findViewById(R.id.viewNumTextView);
            complaintLayout = itemView.findViewById(R.id.complaintLayout);
            complaintImageView = itemView.findViewById(R.id.complaintImageView);
            complaintTextView = itemView.findViewById(R.id.complaintTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            labelRecyclerView = itemView.findViewById(R.id.labelRecyclerView);
            mTextView = itemView.findViewById(R.id.mTextView);
        }
    }

    private class OnExpandCommentsClick implements View.OnClickListener {
        private DongTaiInformationCommentsChildRecyclerAdapter adapter;
        private CommentParent commentParent;

        public OnExpandCommentsClick(CommentParent commentParent, DongTaiInformationCommentsChildRecyclerAdapter childRecyclerAdapter) {
            this.commentParent = commentParent;
            this.adapter = childRecyclerAdapter;
        }

        @Override
        public void onClick(View v) {
            childComments(commentParent.getId());
//            List<String> list = new ArrayList<>();
//            for (int i = 0; i < 20; i++) {
//                list.add(String.valueOf(i * 10));
//            }
//            adapter.addList(list);
        }
    }


    private class OnReplyCommentClick implements View.OnClickListener {
        private int position;

        public OnReplyCommentClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            addCommentDialog.show();
            addCommentDialog.setCommentData(list.get(position).getId(), list.get(position).getUsername());
        }
    }

    private CommentChildResult commentChildResult;

    private void childComments(int comment_id) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DONGTAI_COMMENTS_CHILD_LIST);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                commentChildResult = GsonUtils.GsonToBean(result, CommentChildResult.class);
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

    private BaseResult baseResult;

    private void dongtai_comment(String content, int comment_id) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DONGTAI_COMMENT);
        params.addBodyParameter("feeds_id", "25");
        params.addBodyParameter("content", content);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
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

    private OnReplySuccessListener onReplySuccessListener;

    public void setOnReplySuccessListener(OnReplySuccessListener listener) {
        this.onReplySuccessListener = listener;
    }

    public interface OnReplySuccessListener {
        void onReplySuccess();
    }

    private class OnTextLongClick implements View.OnLongClickListener {
        private int position;
        private TextView textView;

        public OnTextLongClick(int position, TextView textView) {
            this.position = position;
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View v) {
            int[] location = new int[2];
            textView.getLocationOnScreen(location);
            int y = location[1];
            new CommentMenuWindow(context).showMeu(textView, y, (list.get(position).isIs_author() ||
                    list.get(position).isIs_circle_own() || list.get(position).isIs_own()), new CommentMenuWindow.OnMenuClickListener() {
                @Override
                public void onMenuClick(int index) {
                    switch (index) {
                        case 1:
                            CopyUtils.getInstance().copy(context, textView.getText().toString().trim());
                            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            deletePosition = position;
                            ynDialog.show();
                            break;
                        case 3:
                            System.out.println("投诉");
                            break;
                    }

                }
            });
            return false;
        }
    }

    private void deleteComment(int comment_id) {
        System.out.println("deleteComment: " + comment_id);
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_COMMENT);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private class OnPriseClickListener implements View.OnClickListener {
        private int position;

        public OnPriseClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            prisePosition = position;
            priseComment(prisePosition);
        }
    }

    private void priseComment(int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("module_type", "feedscomment");
        params.addBodyParameter("type", (list.get(position).isIs_prise() ? "1" : "0"));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
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

    private class OncomplaintFeedClick implements View.OnClickListener {
        private int feed_id;

        public OncomplaintFeedClick(int feed_id) {
            this.feed_id = feed_id;
        }

        @Override
        public void onClick(View v) {
            if (list.get(0).getInformation().isIs_report()) {
                Toast.makeText(context, "您已投诉过该动态，无需重复提交", Toast.LENGTH_SHORT).show();
            } else {
                Intent complaintIntent = new Intent(context, ComplaintActivity.class);
                complaintIntent.putExtra("report_id", feed_id);
                complaintIntent.putExtra("module_type", "feeds");
                complaintIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(complaintIntent);
            }
        }
    }

}
