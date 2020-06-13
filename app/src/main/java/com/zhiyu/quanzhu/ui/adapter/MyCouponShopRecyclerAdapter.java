package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCouponShop;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.util.List;

public class MyCouponShopRecyclerAdapter extends RecyclerView.Adapter<MyCouponShopRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_10;
    private List<MyCouponShop> list;

    public void setList(List<MyCouponShop> couponShopList) {
        this.list = couponShopList;
        notifyDataSetChanged();
    }

    public MyCouponShopRecyclerAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        TextView nameTextView, shopInfoTextView;
        MyCouponShopCouponRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        SpaceItemDecoration spaceItemDecoration;
        RoundImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            spaceItemDecoration = new SpaceItemDecoration(dp_10);
            mRecyclerView.addItemDecoration(spaceItemDecoration);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new MyCouponShopCouponRecyclerAdapter(context);
            mRecyclerView.setAdapter(adapter);
            shopInfoTextView = itemView.findViewById(R.id.shopInfoTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_coupon_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getIcon()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.adapter.setList(list.get(position).getList());
        holder.adapter.setShop_id(list.get(position).getShop_id());
        holder.shopInfoTextView.setOnClickListener(new OnShopInfoClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnShopInfoClick implements View.OnClickListener {
        private int position;

        public OnShopInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent shopInfoIntent = new Intent(context, ShopInformationActivity.class);
            shopInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shopInfoIntent.putExtra("shop_id", String.valueOf(list.get(position).getShop_id()));
            context.startActivity(shopInfoIntent);
        }
    }
}
