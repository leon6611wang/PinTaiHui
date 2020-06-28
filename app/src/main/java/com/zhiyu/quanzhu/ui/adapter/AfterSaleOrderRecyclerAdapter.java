package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.AfterSaleOrder;
import com.zhiyu.quanzhu.model.bean.OrderShop;
import com.zhiyu.quanzhu.ui.activity.AfterSaleOrderActivity;
import com.zhiyu.quanzhu.ui.activity.AfterSaleServiceInformationActivity;
import com.zhiyu.quanzhu.ui.activity.OrderInformationActivity;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.TimeDownTextView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

public class AfterSaleOrderRecyclerAdapter extends RecyclerView.Adapter<AfterSaleOrderRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<AfterSaleOrder> list;

    public void setList(List<AfterSaleOrder> orderShopList) {
        this.list = orderShopList;
        notifyDataSetChanged();
    }

    public AfterSaleOrderRecyclerAdapter(Context context) {
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopNameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, payWayTextView;
        TextView infoTextView;
        NiceImageView goodsImageImageView;
        TextView goodsNameTextView, goodsNormsTextView, goodsPriceTextView;
        TextView typeTextView, statusDescTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            infoTextView = itemView.findViewById(R.id.infoTextView);
            payWayTextView = itemView.findViewById(R.id.payWayTextView);
            goodsImageImageView = itemView.findViewById(R.id.goodsImageImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            goodsPriceTextView = itemView.findViewById(R.id.goodsPriceTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            statusDescTextView = itemView.findViewById(R.id.statusDescTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_after_sale_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.shopNameTextView.setText(list.get(position).getShop_name());
        holder.shopNameTextView.setOnClickListener(new OnShopInfoClick(position));
        String refund_type = "";
        switch (list.get(position).getRefund_type()) {
            case 1:
                refund_type = "仅退款";
                break;
            case 2:
                refund_type = "退款退货";
                break;
            case 3:
                refund_type = "换货";
                break;
        }
        holder.typeTextView.setText(refund_type);
        holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
        String status_desc = "";
        if (list.get(position).isIs_kefu()) {
            switch (list.get(position).getKefu_status()) {
                case 0:
                    status_desc = "客服介入 处理中";
                    break;
                case 1:
                    status_desc = "客服介入 买家责任，无需退款";
                    break;
                case 2:
                    status_desc = "客服介入 商家责任，全额退款";
                    break;
                case 3:
                    status_desc = "客服介入 商家责任，仅退货款";
                    break;
            }
        } else {
            switch (list.get(position).getRefund_status()) {
                case 0:
                    status_desc = "待处理";
                    break;
                case 1:
                    status_desc = "已同意,待退货";
                    break;
                case 2:
                    status_desc = "已退货,待收货";
                    break;
                case 3:
                    status_desc = "退款成功 ¥" + PriceParseUtils.getInstance().parsePrice(list.get(position).getRefund_price());
                    break;
                case 4:
                    status_desc = "退款失败";
                    break;
                case 5:
                    status_desc = " 取消退款";
                    holder.statusDescTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
                    break;
                case 6:
                    status_desc = "拒绝退款";
                    break;
                case 7:
                    status_desc = "平台强退";
                    break;
            }
        }
        holder.statusDescTextView.setText(status_desc);
        holder.goodsCountTextView.setText(String.valueOf(list.get(position).getGoods_num()));
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(list.get(position).getTotal_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(list.get(position).getTotal_price()));
        Glide.with(context).load(list.get(position).getGoods_img()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsImageImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getGoods_normas_name());
        holder.goodsPriceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getGoods_price()) + "×" + list.get(position).getGoods_num());
        holder.infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AfterSaleServiceInformationActivity.class);
                intent.putExtra("refund_id", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
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
            Intent shopIntent = new Intent(context, ShopInformationActivity.class);
            shopIntent.putExtra("shop_id", String.valueOf(list.get(position).getShop_id()));
            shopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shopIntent);
        }
    }

}
