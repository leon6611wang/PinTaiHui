package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.GoodsComment;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

/**
 * 商品详情-全部评价
 */
public class GoodsInfoAllCommentsRecyclerAdapter extends RecyclerView.Adapter<GoodsInfoAllCommentsRecyclerAdapter.ViewHolder> implements ExpandableTextView.OnExpandListener {
    private List<GoodsComment> list;
    private Context context;
    private int dp_10;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<GoodsInfoAllCommentsRecyclerAdapter> weakReference;

        public MyHandler(GoodsInfoAllCommentsRecyclerAdapter adapter) {
            weakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsInfoAllCommentsRecyclerAdapter adapter = weakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(adapter.context).show("服务器内部错误，请稍后再试.");
                    break;
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    if (200 == adapter.baseResult.getCode()) {
                        int position = (Integer) msg.obj;
                        adapter.list.get(position).setIs_prise(!adapter.list.get(position).isIs_prise());
                        int priseCount = adapter.list.get(position).getPnum();
                        if (adapter.list.get(position).isIs_prise()) {
                            priseCount++;
                        } else {
                            priseCount--;
                        }
                        adapter.list.get(position).setPnum(priseCount);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    public GoodsInfoAllCommentsRecyclerAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
    }

    public void setList(List<GoodsComment> commentList) {
        this.list = commentList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headerImageView;
        TextView nickNameTextView, dateTextView;
        ExpandableTextView conentExpandableTextView;
        TextView replyTextView;
        RecyclerView commentImagesRecyclerView;
        ItemGoodsInfoCommentsRecyclerAdapter adapter;
        LinearLayout dianzanLayout;
        ImageView dianzanImageView;
        TextView normsTextView, dianzanTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            headerImageView = itemView.findViewById(R.id.headerImageView);
            nickNameTextView = itemView.findViewById(R.id.nickNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            conentExpandableTextView = itemView.findViewById(R.id.conentExpandableTextView);
            commentImagesRecyclerView = itemView.findViewById(R.id.commentImagesRecyclerView);
            adapter = new ItemGoodsInfoCommentsRecyclerAdapter(context);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, dp_10, false);
            commentImagesRecyclerView.setAdapter(adapter);
            commentImagesRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            commentImagesRecyclerView.setLayoutManager(gridLayoutManager);
            normsTextView = itemView.findViewById(R.id.normsTextView);
            dianzanLayout = itemView.findViewById(R.id.dianzanLayout);
            dianzanImageView = itemView.findViewById(R.id.dianzanImageView);
            dianzanTextView = itemView.findViewById(R.id.dianzanTextView);
            replyTextView = itemView.findViewById(R.id.replyTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodsinfo_all_comments_recyclerview, parent,false));
    }

    private int etvWidth;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.headerImageView);
        holder.nickNameTextView.setText(list.get(position).getUsername());
        holder.dateTextView.setText(list.get(position).getDate());
        if (etvWidth == 0) {
            holder.conentExpandableTextView.post(new Runnable() {
                @Override
                public void run() {
                    etvWidth = holder.conentExpandableTextView.getWidth();
                }
            });
        }
        holder.conentExpandableTextView.setTag(position);
        Integer state = mPositionsAndStates.get(position);
        holder.conentExpandableTextView.updateForRecyclerView(list.get(position).getContent(), etvWidth, state == null ? 0 : state);
        if (!StringUtils.isNullOrEmpty(list.get(position).getReply())) {
            String reply = "商家回复：" + list.get(position).getReply().trim();
            holder.replyTextView.setText(reply);
            holder.replyTextView.setVisibility(View.VISIBLE);
        } else {
            holder.replyTextView.setVisibility(View.GONE);
        }
        holder.adapter.setList(list.get(position).getImg());
        holder.normsTextView.setText(list.get(position).getNorms_name());
        holder.dianzanTextView.setText(String.valueOf(list.get(position).getPnum()));
        if (list.get(position).isIs_prise()) {
            holder.dianzanImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_yellow));
        } else {
            holder.dianzanImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.dianzan_gray));
        }
        holder.dianzanLayout.setOnClickListener(new OnDianZanClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public void onExpand(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    @Override
    public void onShrink(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    private class OnDianZanClickListener implements View.OnClickListener {
        private int position;

        public OnDianZanClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            prise(position);
        }
    }

    private BaseResult baseResult;

    private void prise(final int position) {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("module_type", "ordercomment");
        params.addBodyParameter("prise_id", String.valueOf(list.get(position).getId()));
        params.addBodyParameter("type", list.get(position).isIs_prise() ? "1" : "0");
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
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
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
