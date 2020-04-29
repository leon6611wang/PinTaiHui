package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderShop;
import com.zhiyu.quanzhu.ui.activity.OrderGoodsCommentsActivity;
import com.zhiyu.quanzhu.ui.activity.OrderInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.DeliveryInfoDialog;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class MyOrderDaiPingJiaRecyclerAdapter extends RecyclerView.Adapter<MyOrderDaiPingJiaRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<OrderShop> list;
    private DeliveryInfoDialog deliveryInfoDialog;

    public void setList(List<OrderShop> orderShopList) {
        this.list = orderShopList;
        notifyDataSetChanged();
    }

    public MyOrderDaiPingJiaRecyclerAdapter(Context context) {
        this.context = context;
        initDialogs();
    }

    private void initDialogs() {
        deliveryInfoDialog = new DeliveryInfoDialog(context, R.style.dialog);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView reviewDeliveryTextView, commentTextView;
        ImageView payWayImageView;
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);
            adapter = new MyOrderShangPinRecyclerAdapter(context);
            shangpinRecyclerView.setLayoutManager(linearLayoutManager);
            shangpinRecyclerView.setAdapter(adapter);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            payWayImageView = itemView.findViewById(R.id.payWayImageView);
            reviewDeliveryTextView = itemView.findViewById(R.id.reviewDeliveryTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daipingjia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.shopNameTextView.setText(list.get(position).getShop_name());
        switch (list.get(position).getPay_type()) {
            case 1:
                holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_alipay));
                holder.payWayTextView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_wechatpay));
                holder.payWayTextView.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                holder.payWayTextView.setVisibility(View.VISIBLE);
                holder.payWayTextView.setText("余额（支付宝）");
                break;
            case 4:
                holder.payWayImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.myorder_balancepay));
                holder.payWayTextView.setVisibility(View.VISIBLE);
                holder.payWayTextView.setText("余额（微信）");
                break;
        }
        holder.goodsCountTextView.setText(String.valueOf(list.get(position).getNum()));
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getPrice()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getPrice()));
        holder.adapter.setList(list.get(position).getGoods());
        holder.shangpinRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return holder.itemView.onTouchEvent(event);
            }
        });
        holder.itemView.setOnClickListener(new OnOrderInformationClick(position));
        holder.reviewDeliveryTextView.setOnClickListener(new OnReviewDeliveryClick(position));
        holder.commentTextView.setOnClickListener(new OnCommentGoodsClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnOrderInformationClick implements View.OnClickListener {
        private int position;

        public OnOrderInformationClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderInformationActivity.class);
            intent.putExtra("order_id", list.get(position).getId());
            intent.putExtra("order_status", list.get(position).getStatus());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private class OnReviewDeliveryClick implements View.OnClickListener {
        private int position;

        public OnReviewDeliveryClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            deliveryInfoDialog.show();
            deliveryInfoDialog.setOrderId(list.get(position).getId());
        }
    }

    private class OnCommentGoodsClick implements View.OnClickListener {
        private int position;

        public OnCommentGoodsClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OrderGoodsCommentsActivity.class);
            String json = GsonUtils.GsonString(list.get(position));
            intent.putExtra("orderShop", json);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
