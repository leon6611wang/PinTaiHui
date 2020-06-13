package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.KaQuanTongZhi;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class XiTongXiaoXiKaQuanTongZhiRecyclerAdapter extends RecyclerView.Adapter<XiTongXiaoXiKaQuanTongZhiRecyclerAdapter.ViewHolder> {
    private Context context;

    public XiTongXiaoXiKaQuanTongZhiRecyclerAdapter(Context context) {
        this.context = context;
    }

    private List<KaQuanTongZhi> list;

    public void setList(List<KaQuanTongZhi> kaQuanTongZhiList) {
        this.list = kaQuanTongZhiList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, couponTitleTextView,
                couponContentTextView, timeTextView;
        NiceImageView couponImageView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            couponTitleTextView = itemView.findViewById(R.id.couponTitleTextView);
            couponContentTextView = itemView.findViewById(R.id.couponContentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            couponImageView = itemView.findViewById(R.id.couponImageView);
            mCardView = itemView.findViewById(R.id.mCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_kaquantongzhi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getCoupon_thumb()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.couponImageView);
        holder.titleTextView.setText(list.get(position).getTitle());
        holder.couponTitleTextView.setText(list.get(position).getCoupon_title());
        holder.couponContentTextView.setText(list.get(position).getCoupon_content());
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("shop_id", String.valueOf(list.get(position).getShop_id()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
