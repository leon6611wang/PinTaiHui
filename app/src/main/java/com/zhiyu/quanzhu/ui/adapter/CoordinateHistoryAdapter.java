package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CoordinateHistory;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class CoordinateHistoryAdapter extends RecyclerView.Adapter<CoordinateHistoryAdapter.ViewHolder> {
    private List<CoordinateHistory> list;

    public void setList(List<CoordinateHistory> l) {
        this.list = l;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, timeTextView, titleTextView,
                typeTextView, priceTextView, reasonTextView,
                descTextView;
        LinearLayout priceLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            priceLayout = itemView.findViewById(R.id.priceLayout);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            descTextView = itemView.findViewById(R.id.descTextView);

        }
    }


    @NonNull
    @Override
    public CoordinateHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinate_history, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CoordinateHistoryAdapter.ViewHolder holder, int position) {
        holder.nameTextView.setText(list.get(position).getFrom());
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.titleTextView.setText(list.get(position).getTitle());
        if (!StringUtils.isNullOrEmpty(list.get(position).getRefund_type()) || !StringUtils.isNullOrEmpty(list.get(position).getAddress_name())) {
            holder.typeTextView.setVisibility(View.VISIBLE);
            if (!StringUtils.isNullOrEmpty(list.get(position).getRefund_type())) {
                holder.typeTextView.setText("退款类型：" + list.get(position).getRefund_type());
            } else if (!StringUtils.isNullOrEmpty(list.get(position).getAddress_name())) {
                holder.typeTextView.setText("收货人：" + list.get(position).getAddress_name());
            }
        } else {
            holder.typeTextView.setVisibility(View.GONE);
        }
        if (list.get(position).getRefund_price() > 0) {
            holder.priceLayout.setVisibility(View.VISIBLE);
            holder.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getRefund_price()));
        } else {
            holder.priceLayout.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getRefund_reason()) || !StringUtils.isNullOrEmpty(list.get(position).getAddress_phone())) {
            holder.reasonTextView.setVisibility(View.VISIBLE);
            String reason_pre = "";
            switch (list.get(position).getType()) {
                case 1:
                    reason_pre = "退款原因：" + list.get(position).getRefund_reason();
                    break;
                case 3:
                    reason_pre = "联系电话：" + list.get(position).getAddress_phone();
                    break;
                case 4:
                    reason_pre = "拒绝原因：" + list.get(position).getRefund_reason();
                    break;
            }
            holder.reasonTextView.setText(reason_pre);
        } else {
            holder.reasonTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getRefund_desc()) || !StringUtils.isNullOrEmpty(list.get(position).getAddress())) {
            holder.descTextView.setVisibility(View.VISIBLE);
            String desc_pre = "";
            switch (list.get(position).getType()) {
                case 1:
                    desc_pre = "退款描述：" + list.get(position).getRefund_desc();
                    break;
                case 3:
                    desc_pre = "退货地址：" + list.get(position).getAddress();
                    break;
                case 6:
                    desc_pre = "处理备注：" + list.get(position).getRefund_desc();
                    break;
            }
            holder.descTextView.setText(desc_pre);
        } else {
            holder.descTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

}
