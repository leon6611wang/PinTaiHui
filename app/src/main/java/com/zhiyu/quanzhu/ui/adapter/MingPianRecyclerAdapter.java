package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Card;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
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

public class MingPianRecyclerAdapter extends RecyclerView.Adapter<MingPianRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Card> list;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MingPianRecyclerAdapter> adapterWeakReference;

        public MyHandler(MingPianRecyclerAdapter adapter) {
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            MingPianRecyclerAdapter adapter = adapterWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(adapter.context).show(adapter.baseResult.getMsg());
//                    if (200 == adapter.baseResult.getCode()) {
//                        int position = (Integer) msg.obj;
//                        adapter.list.get(position).setIs_follow(true);
//                        adapter.notifyItemChanged(position);
//                    }
                    break;
            }
        }
    }

    public void setList(List<Card> cardList) {
        this.list = cardList;
        notifyDataSetChanged();
    }


    public MingPianRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView avatarImageView;
        TextView nameTextView, occupionTextView, companyTextView,
                cityTextView, industryTextView;
        ImageView rightImageView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            occupionTextView = itemView.findViewById(R.id.occupionTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            industryTextView = itemView.findViewById(R.id.industryTextView);
            rightImageView = itemView.findViewById(R.id.rightImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getCard_thumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.avatarImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getCard_name()))
            holder.nameTextView.setText(list.get(position).getCard_name()+" "+list.get(position).getUid());
        if (!StringUtils.isNullOrEmpty(list.get(position).getOccupation()))
            holder.occupionTextView.setText(list.get(position).getOccupation());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCompany()))
            holder.companyTextView.setText(list.get(position).getCompany());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(list.get(position).getCity_name());
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        } else {
            holder.industryTextView.setVisibility(View.GONE);
        }
        if (list.get(position).isIs_follow()) {
            holder.rightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.mingpian_liaotian));
        } else {
            holder.rightImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.mingpian_add));
        }
        holder.rightImageView.setOnClickListener(new OnRightImageClick(position));
        holder.mCardView.setOnClickListener(new OnCardInfoClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnRightImageClick implements View.OnClickListener {
        private int position;

        public OnRightImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (list.get(position).isIs_follow()) {
                onChat(position);
            } else {
                addCard(position);
            }
        }
    }

    private BaseResult baseResult;

    private void addCard(final int position) {
        System.out.println("tuid: " + String.valueOf(list.get(position).getUid()));
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_EXCHANGE);
        params.addBodyParameter("tuid", String.valueOf(list.get(position).getUid()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void onChat(int position) {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(context).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        try {
            RongIM.getInstance().startPrivateChat(context, String.valueOf(list.get(position).getUid()),
                    TextUtils.isEmpty(list.get(position).getCard_name()) ? "聊天界面" : list.get(position).getCard_name());

//            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), list.get(position).getUser_name());
        } catch (Exception e) {
            e.printStackTrace();
            MessageToast.getInstance(context).show("无法进入会话界面");
        }
    }

    private class OnCardInfoClick implements View.OnClickListener {
        private int position;

        public OnCardInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CardInformationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("card_id", list.get(position).getId());
            intent.putExtra("uid", list.get(position).getUid());
            context.startActivity(intent);

        }
    }
}
