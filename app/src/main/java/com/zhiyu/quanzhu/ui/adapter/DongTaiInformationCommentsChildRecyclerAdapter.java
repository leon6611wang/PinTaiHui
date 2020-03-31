package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.zhiyu.quanzhu.model.bean.CommentChild;
import com.zhiyu.quanzhu.ui.dialog.AddCommentDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.popupwindow.CommentMenuWindow;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class DongTaiInformationCommentsChildRecyclerAdapter extends RecyclerView.Adapter<DongTaiInformationCommentsChildRecyclerAdapter.ViewHolder> {
    private List<CommentChild> list;
    private Context context;
    private AddCommentDialog addCommentDialog;
    private MyHandler myHandler = new MyHandler(this);
    private YNDialog ynDialog;
    private int deletePosition=-1;
    private static class MyHandler extends Handler {
        WeakReference<DongTaiInformationCommentsChildRecyclerAdapter> adapterWeakReference;

        public MyHandler(DongTaiInformationCommentsChildRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            DongTaiInformationCommentsChildRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    Toast.makeText(adapter.context, adapter.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (adapter.baseResult.getCode() == 200) {
                        if (null != adapter.onReplySuccessListener) {
                            adapter.onReplySuccessListener.onChildReplySuccess();
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

    public DongTaiInformationCommentsChildRecyclerAdapter(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {
        addCommentDialog = new AddCommentDialog(context, R.style.inputDialog, new AddCommentDialog.OnAddCommentListener() {
            @Override
            public void onAddComment(String comment, int commentId) {
                dongtai_comment(comment, commentId);
            }
        });
        ynDialog=new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                deleteComment(list.get(deletePosition).getId());
            }
        });
    }

    public void setList(List<CommentChild> commentsList) {
        this.list = commentsList;
        notifyDataSetChanged();
    }

    public void addList(List<CommentChild> commentsList) {
        if (null != list) {
            list.addAll(commentsList);
        }
        notifyDataSetChanged();
    }

    public int getListCount() {
        return null == list ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView, nicknameTextView, priseCountTextView, publishTimeTextView, replyCommentTextView;
        CircleImageView avatarImageView;
        ImageView priseImageView;
        LinearLayout priseLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
            publishTimeTextView = itemView.findViewById(R.id.publishTimeTextView);
            priseCountTextView = itemView.findViewById(R.id.priseCountTextView);
            priseImageView = itemView.findViewById(R.id.priseImageView);
            priseLayout=itemView.findViewById(R.id.priseLayout);
            replyCommentTextView = itemView.findViewById(R.id.replyCommentTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongtai_information_comments_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar())
                .error(R.mipmap.no_avatar)
                .into(holder.avatarImageView);
        holder.nicknameTextView.setText(list.get(position).getUsername());
        holder.priseCountTextView.setText(String.valueOf(list.get(position).getPnum()));
        if (list.get(position).isIs_prise()) {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.priseImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        holder.priseLayout.setOnClickListener(new OnPriseClickListener(position));
        holder.publishTimeTextView.setText(list.get(position).getDateline());
        if (list.get(position).getType() == 2) {
            holder.commentTextView.setText(list.get(position).getContent());
        } else if (list.get(position).getType() == 3) {
            String key = "回复@" + list.get(position).getReply_username() + "：";
            String s = list.get(position).getContent() + " ｜" + key + list.get(position).getPcontent();
            String str = s.replace(key, "<font color='#009DE0'>" + key + "</font>");
            holder.commentTextView.setTextSize(14);
            holder.commentTextView.setText(Html.fromHtml(str));
        }
        holder.commentTextView.setOnLongClickListener(new OnTextLongClick(position, holder.commentTextView));
        holder.replyCommentTextView.setOnClickListener(new OnReplyCommentClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
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

    private BaseResult baseResult;

    private void dongtai_comment(String content, int comment_id) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEED_COMMENT);
        params.addBodyParameter("feeds_id", "25");
        params.addBodyParameter("content", content);
        params.addBodyParameter("comment_id", String.valueOf(comment_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("评论动态 " + result);
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
        void onChildReplySuccess();
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
            final int y = location[1];
            new CommentMenuWindow(context).showMeu(textView, y, (list.get(position).isIs_author() ||
                    list.get(position).isIs_circle_own() || list.get(position).isIs_own() ||
                    list.get(position).isIs_manger()), new CommentMenuWindow.OnMenuClickListener() {
                @Override
                public void onMenuClick(int index) {
                    switch (index) {
                        case 1:
                            System.out.println("复制");
                            break;
                        case 2:
                            System.out.println("删除");
                            deletePosition=position;
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

    private int prisePosition=-1;
    private class OnPriseClickListener implements View.OnClickListener{
        private int position;

        public OnPriseClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            prisePosition=position;
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
                Message message=myHandler.obtainMessage(3);
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
