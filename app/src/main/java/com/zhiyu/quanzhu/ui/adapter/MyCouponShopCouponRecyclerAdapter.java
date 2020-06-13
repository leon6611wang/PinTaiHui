package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCoupon;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;

import java.util.List;

public class MyCouponShopCouponRecyclerAdapter extends RecyclerView.Adapter<MyCouponShopCouponRecyclerAdapter.ViewHolder> {
    private List<MyCoupon> list;
    private Context context;
    private int shop_id;

    public void setList(List<MyCoupon> couponList) {
        this.list = couponList;
        notifyDataSetChanged();
    }

    public void setShop_id(int s_id) {
        this.shop_id = s_id;
    }

    public MyCouponShopCouponRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImageView;
        TextView amountTextView, markTextView, descTextView, timeTextView, usedescTextView, useCouponTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            statusImageView = itemView.findViewById(R.id.statusImageView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            markTextView = itemView.findViewById(R.id.markTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            usedescTextView = itemView.findViewById(R.id.usedescTextView);
            useCouponTextView = itemView.findViewById(R.id.useCouponTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_coupon_shop_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        statusImage(list.get(position).getStatus(), holder.statusImageView);
        holder.amountTextView.setText(list.get(position).getAmount());
        holder.markTextView.setText(list.get(position).getMark());
        holder.descTextView.setText(list.get(position).getTitle());
        holder.timeTextView.setText(list.get(position).getExp_start() + " 至 " + list.get(position).getExp_end());
        holder.usedescTextView.setText(list.get(position).getUsedesc());
        if (list.get(position).getStatus() == 0 || list.get(position).getStatus() == 1) {
            holder.useCouponTextView.setVisibility(View.VISIBLE);
            holder.useCouponTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shopInfoIntent = new Intent(context, ShopInformationActivity.class);
                    shopInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shopInfoIntent.putExtra("shop_id", String.valueOf(shop_id));
                    context.startActivity(shopInfoIntent);
                }
            });
        } else {
            holder.useCouponTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private void statusImage(int status, ImageView statusImageView) {
        //0即将过期 1:正常；2：已使用；-1：过期
        switch (status) {
            case 0:
                statusImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.kaquan_jiangshixiao));
                break;
            case 1:
                statusImageView.setVisibility(View.GONE);
                break;
            case 2:
                statusImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.kaquan_yishiyong));
                break;
            case -1:
                statusImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.kaquan_yiguoqi));
                break;
        }
    }
}
