package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderInformationGoods;
import com.zhiyu.quanzhu.ui.activity.AfterSaleServiceActivity;
import com.zhiyu.quanzhu.ui.activity.AfterSaleServiceInformationActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.OrderStatusUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class OrderInformationGoodsRecyclerviewAdapter extends RecyclerView.Adapter<OrderInformationGoodsRecyclerviewAdapter.ViewHolder> {
    private List<OrderInformationGoods> list;
    private Context context;
    private int order_status;
    private int order_id;
    private long refund_money;

    public OrderInformationGoodsRecyclerviewAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<OrderInformationGoods> goodsList, int status, int id) {
        this.list = goodsList;
        this.order_status = status;
        this.order_id = id;
        notifyDataSetChanged();
    }

    public void setRefundMoney(long money) {
        this.refund_money = money;
        System.out.println("adapter --> refund_money： " + refund_money);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView goodsImageImageView;
        TextView goodsNameTextView, goodsNormsTextView,
                zhengshuTextView, xiaoshuTextView, countTextView,
                buttonTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
            buttonTextView = itemView.findViewById(R.id.buttonTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_information_goods, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getGoods_img()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getNorms_name());
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getGoods_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getGoods_price()));
        holder.countTextView.setText("x" + list.get(position).getGoods_num());
        if (!StringUtils.isNullOrEmpty(list.get(position).getRefund_desc())) {
            holder.buttonTextView.setVisibility(View.VISIBLE);
            holder.buttonTextView.setText(list.get(position).getRefund_desc());
        } else if (StringUtils.isNullOrEmpty(list.get(position).getRefund_desc()) ||
                order_status == 0 ||//待付款
                order_status == 1) {//已取消

            holder.buttonTextView.setVisibility(View.GONE);
        }
        holder.buttonTextView.setOnClickListener(new OnButtonClick(position));
//        switch (order_status) {
//            case OrderStatusUtils.DAIFUKUAN:
//                holder.buttonTextView.setVisibility(View.GONE);
//                break;
//            case OrderStatusUtils.YIQUXIAO:
//                holder.buttonTextView.setVisibility(View.GONE);
//                break;
//            case OrderStatusUtils.DAIFAHUO:
//                holder.buttonTextView.setVisibility(View.VISIBLE);
//                holder.buttonTextView.setText(list.get(position).getRefund_desc());
//                break;
//            case OrderStatusUtils.DAISHOUHUO:
//                holder.buttonTextView.setVisibility(View.VISIBLE);
//                holder.buttonTextView.setText(list.get(position).getRefund_desc());
//                break;
//            case OrderStatusUtils.DAIPINGJIA:
//                holder.buttonTextView.setVisibility(View.VISIBLE);
//                holder.buttonTextView.setText(list.get(position).getRefund_desc());
//                break;
//            case OrderStatusUtils.YIWANCHENG:
//                holder.buttonTextView.setVisibility(View.VISIBLE);
//                holder.buttonTextView.setText(list.get(position).getRefund_desc());
//                break;
//            case OrderStatusUtils.SHOUHOUZHONG:
//                holder.buttonTextView.setVisibility(View.VISIBLE);
//                holder.buttonTextView.setText(list.get(position).getRefund_desc());
//                break;
//        }
//        if (StringUtils.isNullOrEmpty(list.get(position).getRefund_desc())) {
//            holder.buttonTextView.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    private class OnButtonClick implements View.OnClickListener {
        private int position;

        public OnButtonClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if ("申请售后".equals(list.get(position).getRefund_desc()) || "退款".equals(list.get(position).getRefund_desc())) {
                Intent afterSaleServiceIntent = new Intent(context, AfterSaleServiceActivity.class);
                OrderInformationGoods goods = list.get(position);
                String goodsJson = GsonUtils.GsonString(goods);
                afterSaleServiceIntent.putExtra("order_id", order_id);
                afterSaleServiceIntent.putExtra("goodsJson", goodsJson);
                afterSaleServiceIntent.putExtra("refund_money", list.get(position).getRefund_price());
                afterSaleServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(afterSaleServiceIntent);
            } else {
                Intent afterSaleServiceInfoIntent = new Intent(context, AfterSaleServiceInformationActivity.class);
                afterSaleServiceInfoIntent.putExtra("refund_id", list.get(position).getRefund_id());
                afterSaleServiceInfoIntent.putExtra("refund_money", list.get(position).getRefund_price());
                afterSaleServiceInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(afterSaleServiceInfoIntent);
            }
        }
    }
}
