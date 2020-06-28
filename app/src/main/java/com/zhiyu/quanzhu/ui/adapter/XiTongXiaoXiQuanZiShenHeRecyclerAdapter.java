package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QuanZiShenHe;
import com.zhiyu.quanzhu.ui.activity.CircleInfoActivity;
import com.zhiyu.quanzhu.ui.dialog.RefuseReasonDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class XiTongXiaoXiQuanZiShenHeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private RefuseReasonDialog refuseReasonDialog;
    private List<QuanZiShenHe> list;
    private int currentPosition;
    private String refuse_reason;
    private MyHandler myHandler = new MyHandler(this);
    private BaseResult baseResult;

    private static class MyHandler extends Handler {
        WeakReference<XiTongXiaoXiQuanZiShenHeRecyclerAdapter> adapterWeakReference;

        public MyHandler(XiTongXiaoXiQuanZiShenHeRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            XiTongXiaoXiQuanZiShenHeRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(adapter.context).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    adapter.list.get(adapter.currentPosition).setStatus(adapter.operation_type);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public XiTongXiaoXiQuanZiShenHeRecyclerAdapter(Context context) {
        this.context = context;
        refuseReasonDialog = new RefuseReasonDialog(context, R.style.dialog, new RefuseReasonDialog.OnRefuseJoinCircleListener() {
            @Override
            public void onRefuseJoinCircle(String refuse_content) {
                refuse_reason = refuse_content;
                operation_type = 2;
                circleApplyOperation();
            }
        });
    }

    public void setList(List<QuanZiShenHe> quanZiShenHeList) {
        this.list = quanZiShenHeList;
        notifyDataSetChanged();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView timeTextView, circleNameTextView, contentTextView, refuseTextView, confirmTextView, resultTextView;
        CircleImageView avatarImageView;

        public ViewHolder0(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            refuseTextView = itemView.findViewById(R.id.refuseTextView);
            confirmTextView = itemView.findViewById(R.id.confirmTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            resultTextView = itemView.findViewById(R.id.resultTextView);

        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView timeTextView, circleNameTextView, contentTextView, refuseTextView, confirmTextView, resultTextView,statusDescTextView;
        CircleImageView avatarImageView;

        public ViewHolder1(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            refuseTextView = itemView.findViewById(R.id.refuseTextView);
            confirmTextView = itemView.findViewById(R.id.confirmTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            resultTextView = itemView.findViewById(R.id.resultTextView);
            statusDescTextView=itemView.findViewById(R.id.statusDescTextView);

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView timeTextView, circleNameTextView, contentTextView;
        CircleImageView avatarImageView;
        TextView applyTextView;

        public ViewHolder2(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            applyTextView = itemView.findViewById(R.id.applyTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanzishenhe_0, parent, false));
        } else if (viewType == 1) {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanzishenhe_1, parent, false));
        } else if (viewType == 2) {
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanzishenhe_2, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myholder, final int position) {
        if (myholder instanceof ViewHolder0) {
            ViewHolder0 holder = (ViewHolder0) myholder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            String content = "<font color='#FE8627'>" + list.get(position).getUsername() + "</font><font color='#666666'>" + " 想要加入您的圈子 " + "</font>" +
                    "<font color='#FE8627'>" + list.get(position).getCircle_name() + "</font>";
            holder.contentTextView.setText(Html.fromHtml(content));
            holder.resultTextView.setText("验证消息："+list.get(position).getContent());
            holder.refuseTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    refuseReasonDialog.show();
                }
            });
            holder.confirmTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    operation_type = 1;
                    circleApplyOperation();
                }
            });
        } else if (myholder instanceof ViewHolder1) {
            ViewHolder1 holder = (ViewHolder1) myholder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            String content = "<font color='#FE8627'>" + list.get(position).getUsername() + "</font><font color='#666666'>" + " 想要加入您的圈子 " + "</font>" +
                    "<font color='#FE8627'>" + list.get(position).getCircle_name() + "</font>";
            holder.contentTextView.setText(Html.fromHtml(content));
            holder.resultTextView.setText("验证消息："+list.get(position).getContent());
            switch (list.get(position).getStatus()){
                case 1:
                    holder.statusDescTextView.setText("已通过");
                    holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                    break;
                case 2:
                    holder.statusDescTextView.setText("已拒绝");
                    holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_red_2));
                    break;
            }
        } else if (myholder instanceof ViewHolder2) {
            ViewHolder2 holder = (ViewHolder2) myholder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            String content = "<font color='#666666'>" + "您申请的加入 " + "</font><font color='#FE8627'>" + list.get(position).getCircle_name() + "</font>" +
                    " <font color='#333333'>" + list.get(position).getStatus_desc() + "</font>";
            holder.circleNameTextView.setText(Html.fromHtml(content));
            holder.contentTextView.setText("回复消息: " + list.get(position).getRefuse_reason());
            holder.applyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    currentPosition = position;
//                    joinCircle();
                    Intent intent = new Intent(context, CircleInfoActivity.class);
                    intent.putExtra("circle_id", (long) list.get(position).getCircle_id());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    //1别人加我的
    //2 我加别人的
    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        viewType = list.get(position).getFrom();
        switch (list.get(position).getFrom()) {
            case 1:
                switch (list.get(position).getStatus()) {
                    case 0:
                        viewType = 0;
                        break;
                    case 1:
                        viewType = 1;
                        break;
                    case 2:
                        viewType = 1;
                        break;
                }
                break;
            case 2:
                viewType = 2;
                break;
        }
        return viewType;
    }

    private int operation_type;

    private void circleApplyOperation() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.JOIN_CIRCLE_OPERATION);
        params.addBodyParameter("id", String.valueOf(list.get(currentPosition).getMsg_id()));
        params.addBodyParameter("type", String.valueOf(operation_type));
        params.addBodyParameter("refuse_reason", refuse_reason);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
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

    private void joinCircle() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.JOIN_CIRCLE);
        params.addBodyParameter("circle_id", String.valueOf(list.get(currentPosition).getCircle_id()));
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
}
