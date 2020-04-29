package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CustomerServiceMessage;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.WrapImageView;

import java.util.List;

public class CustomerServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomerServiceMessage> list;
    private Context context;

    public CustomerServiceAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CustomerServiceMessage> messageList) {
        this.list = messageList;
        notifyDataSetChanged();
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView, mTextView;
        CircleImageView avatarImageView;
        LinearLayout contentLayout;
        RelativeLayout imageLayout;
        WrapImageView mWrapImageView;
        LinearLayout orderLayout, goodsLayout;
        TextView goodsCountTextView, orderPriceTextView, addressNameTextView, addressPhoneTextView, addressAddressTextView,
                orderButtonTextView, goodsNameTextView;
        NiceImageView goodsImageImageView;

        public LeftViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            mTextView = itemView.findViewById(R.id.mTextView);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            mWrapImageView = itemView.findViewById(R.id.mWrapImageView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            addressNameTextView = itemView.findViewById(R.id.addressNameTextView);
            addressPhoneTextView = itemView.findViewById(R.id.addressPhoneTextView);
            addressAddressTextView = itemView.findViewById(R.id.addressAddressTextView);
            orderButtonTextView = itemView.findViewById(R.id.orderButtonTextView);
            goodsLayout = itemView.findViewById(R.id.goodsLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
        }
    }

    class RightViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, nameTextView, mTextView;
        CircleImageView avatarImageView;
        LinearLayout contentLayout;
        RelativeLayout imageLayout;
        WrapImageView mWrapImageView;
        LinearLayout orderLayout, goodsLayout;
        TextView goodsCountTextView, orderPriceTextView, addressNameTextView, addressPhoneTextView, addressAddressTextView,
                orderButtonTextView, goodsNameTextView;
        NiceImageView goodsImageImageView;

        public RightViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            mTextView = itemView.findViewById(R.id.mTextView);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            mWrapImageView = itemView.findViewById(R.id.mWrapImageView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            addressNameTextView = itemView.findViewById(R.id.addressNameTextView);
            addressPhoneTextView = itemView.findViewById(R.id.addressPhoneTextView);
            addressAddressTextView = itemView.findViewById(R.id.addressAddressTextView);
            orderButtonTextView = itemView.findViewById(R.id.orderButtonTextView);
            goodsLayout = itemView.findViewById(R.id.goodsLayout);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CustomerServiceMessage.LEFT:
                return new LeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_service_left, null));
            case CustomerServiceMessage.RIGHT:
                return new RightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_service_right, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LeftViewHolder) {
            LeftViewHolder holder = (LeftViewHolder) viewHolder;
            holder.timeTextView.setText(list.get(position).getTime());
            Glide.with(context).load(list.get(position).getUserAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getUserName());
            switch (list.get(position).getMessage_type()) {
                case CustomerServiceMessage.TYPE_TEXT:
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_IMAGE:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.VISIBLE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_ORDER:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.VISIBLE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_GOODS:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.VISIBLE);
                    break;
            }
        } else if (viewHolder instanceof RightViewHolder) {
            RightViewHolder holder = (RightViewHolder) viewHolder;
            holder.timeTextView.setText(list.get(position).getTime());
            Glide.with(context).load(list.get(position).getUserAvatar()).error(R.drawable.image_error).into(holder.avatarImageView);
            holder.nameTextView.setText(list.get(position).getUserName());
            switch (list.get(position).getMessage_type()) {
                case CustomerServiceMessage.TYPE_TEXT:
                    holder.mTextView.setVisibility(View.VISIBLE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_IMAGE:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.VISIBLE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_ORDER:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.VISIBLE);
                    holder.goodsLayout.setVisibility(View.GONE);
                    break;
                case CustomerServiceMessage.TYPE_GOODS:
                    holder.mTextView.setVisibility(View.GONE);
                    holder.imageLayout.setVisibility(View.GONE);
                    holder.orderLayout.setVisibility(View.GONE);
                    holder.goodsLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getOwner();
    }
}
