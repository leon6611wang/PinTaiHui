package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.QuanYouShenHe;
import com.zhiyu.quanzhu.ui.dialog.RefuseReasonDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class XiTongXiaoXiQuanYouShenHeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<QuanYouShenHe> list;
    private RefuseReasonDialog refuseReasonDialog;
    private String reason;
    private int currentPosition;
    private int type;//0:拒绝，1:同意

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<XiTongXiaoXiQuanYouShenHeRecyclerAdapter> adapterWeakReference;

        public MyHandler(XiTongXiaoXiQuanYouShenHeRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            XiTongXiaoXiQuanYouShenHeRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 99:
                    MessageToast.getInstance(adapter.context).show("服务器内部错误，请稍后再试");
                    break;
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
                    adapter.list.get(adapter.currentPosition).setStatus(adapter.type);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public XiTongXiaoXiQuanYouShenHeRecyclerAdapter(Context context) {
        this.context = context;
        refuseReasonDialog = new RefuseReasonDialog(context, R.style.dialog, new RefuseReasonDialog.OnRefuseJoinCircleListener() {
            @Override
            public void onRefuseJoinCircle(String refuse_content) {
                reason = refuse_content;
                type = 2;
                userOperation();
            }
        });
    }

    public void setList(List<QuanYouShenHe> quanYouShenHeList) {
        this.list = quanYouShenHeList;
        notifyDataSetChanged();
    }

    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView,
                statusDescTextView, resultTextView,
                leftButtonTextView, rightButtonTextView;
        CircleImageView avatarImageView;

        public ViewHolder0(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusDescTextView = itemView.findViewById(R.id.statusDescTextView);
            resultTextView = itemView.findViewById(R.id.resultTextView);
            leftButtonTextView = itemView.findViewById(R.id.leftButtonTextView);
            rightButtonTextView = itemView.findViewById(R.id.rightButtonTextView);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView,
                statusDescTextView, resultTextView,
                leftButtonTextView, rightButtonTextView;
        CircleImageView avatarImageView;

        public ViewHolder1(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusDescTextView = itemView.findViewById(R.id.statusDescTextView);
            resultTextView = itemView.findViewById(R.id.resultTextView);
            leftButtonTextView = itemView.findViewById(R.id.leftButtonTextView);
            rightButtonTextView = itemView.findViewById(R.id.rightButtonTextView);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView,
                statusDescTextView, resultTextView,
                leftButtonTextView, rightButtonTextView;
        CircleImageView avatarImageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            statusDescTextView = itemView.findViewById(R.id.statusDescTextView);
            resultTextView = itemView.findViewById(R.id.resultTextView);
            leftButtonTextView = itemView.findViewById(R.id.leftButtonTextView);
            rightButtonTextView = itemView.findViewById(R.id.rightButtonTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == view0) {
            return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanyoushenhe_0, parent, false));
        } else if (viewType == view1) {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanyoushenhe_1, parent, false));
        } else if (viewType == view2) {
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_quanyoushenhe_2, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myHolder, final int position) {
        if (myHolder instanceof ViewHolder0) {
            ViewHolder0 holder = (ViewHolder0) myHolder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            holder.timeTextView.setText(list.get(position).getAdd_time());
            String content = "<font color='#FE8627'>" + list.get(position).getUsername() + "</font><font color='#666666'>" + " 想要加您为圈友 " + "</font>";
            holder.nameTextView.setText(Html.fromHtml(content));
            holder.resultTextView.setText("验证消息：" + list.get(position).getContent());
            holder.leftButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
//                    refuseReasonDialog.show();
                    type = 2;
                    userOperation();
                }
            });
            holder.rightButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = 1;
                    currentPosition = position;
                    userOperation();
                }
            });
        } else if (myHolder instanceof ViewHolder1) {
            ViewHolder1 holder = (ViewHolder1) myHolder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            holder.timeTextView.setText(list.get(position).getAdd_time());
            String content = "<font color='#FE8627'>" + list.get(position).getUsername() + "</font><font color='#666666'>" + " 想要加您为圈友 " + "</font>";
            holder.nameTextView.setText(Html.fromHtml(content));
            holder.resultTextView.setText("验证消息：" + list.get(position).getRefuse_reason());
            holder.statusDescTextView.setText(list.get(position).getStatus_desc());
            String buttonText = "";
            switch (list.get(position).getStatus()) {
                case 1:
                    buttonText = "去聊天";
                    holder.rightButtonTextView.setVisibility(View.VISIBLE);
                    holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
                    break;
                case 2:
                    buttonText = "已拒绝";
                    holder.rightButtonTextView.setVisibility(View.GONE);
                    holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_red_2));
                    break;
            }
            holder.rightButtonTextView.setText(buttonText);
            holder.rightButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChat(position);
                }
            });
        } else if (myHolder instanceof ViewHolder2) {
            ViewHolder2 holder = (ViewHolder2) myHolder;
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.avatarImageView);
            holder.timeTextView.setText(list.get(position).getAdd_time());
            holder.nameTextView.setText(list.get(position).getUsername());
            holder.resultTextView.setText("验证消息：" + list.get(position).getRefuse_reason());
            holder.statusDescTextView.setText(list.get(position).getStatus_desc());
            holder.rightButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    change();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private int view0 = 0, view1 = 1, view2 = 2;

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (list.get(position).getFrom()) {
            case 1://别人加我
                switch (list.get(position).getStatus()) {
                    case 0://正在审核
                        type = view0;
                        break;
                    case 1://审核通过
                        type = view1;
                        break;
                    case 2://被拒绝
                        type = view1;
                        break;
                }
                break;
            case 2://我加别人
                type = view2;
                break;
        }
        return type;
    }

    private void onChat(int position) {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(context).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        try {
            RongIM.getInstance().startPrivateChat(context, String.valueOf(list.get(position).getUid()),
                    TextUtils.isEmpty(list.get(position).getUsername()) ? "聊天界面" : list.get(position).getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            MessageToast.getInstance(context).show("无法进入会话界面");
        }
    }

    private BaseResult baseResult;

    private void userOperation() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_OPERATION);
        params.addBodyParameter("id", String.valueOf(list.get(currentPosition).getMsg_id()));
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("refuse_reason", reason);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("userOperation: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("userOperation: " + ex.toString());
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

    private void change() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_EXCHANGE);
        params.addBodyParameter("tuid", String.valueOf(list.get(currentPosition).getUid()));
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
