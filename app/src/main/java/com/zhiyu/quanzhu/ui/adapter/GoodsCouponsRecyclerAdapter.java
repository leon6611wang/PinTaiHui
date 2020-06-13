package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsCoupon;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class GoodsCouponsRecyclerAdapter extends RecyclerView.Adapter<GoodsCouponsRecyclerAdapter.ViewHolder> {
    private List<GoodsCoupon> list;
    private Context context;
    private LinearLayout.LayoutParams leftParams, rightParams;

    public GoodsCouponsRecyclerAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int dp_30 = (int) context.getResources().getDimension(R.dimen.dp_30);
        int leftWidth = (int) Math.round(0.6377 * (screenWidth - dp_30));
        int rightWidth = (int) Math.round((screenWidth - dp_30) * 0.3623);
        leftParams = new LinearLayout.LayoutParams(leftWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        rightParams = new LinearLayout.LayoutParams(rightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void setList(List<GoodsCoupon> couponList) {
        this.list = couponList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lingquTextView, yilingquTextView, amountTextView, titleTextView, descTextView, expTextView;
        LinearLayout leftLayout, rightLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.leftLayout);
            leftLayout.setLayoutParams(leftParams);
            rightLayout = itemView.findViewById(R.id.rightLayout);
            rightLayout.setLayoutParams(rightParams);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            expTextView = itemView.findViewById(R.id.expTextView);
            lingquTextView = itemView.findViewById(R.id.lingquTextView);
            yilingquTextView = itemView.findViewById(R.id.yilingquTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_coupons, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amountTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getAmount()));
        holder.titleTextView.setText(list.get(position).getTitle());
        holder.descTextView.setText(list.get(position).getDesc() + "  " + list.get(position).getLimit());
        holder.expTextView.setText(list.get(position).getExp_start() + " - " + list.get(position).getExp_end());
        if (list.get(position).isIs_has()) {
            holder.yilingquTextView.setVisibility(View.VISIBLE);
            holder.lingquTextView.setVisibility(View.GONE);
        } else {
            holder.yilingquTextView.setVisibility(View.GONE);
            holder.lingquTextView.setVisibility(View.VISIBLE);
            holder.lingquTextView.setOnClickListener(new OnLingQuClick(position));
        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    class OnLingQuClick implements View.OnClickListener {
        private int position;

        public OnLingQuClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onGetCouponListener) {
                onGetCouponListener.onGetCoupon(list.get(position).getId());
            }
        }
    }

    private OnGetCouponListener onGetCouponListener;

    public void setOnGetCouponListener(OnGetCouponListener listener) {
        this.onGetCouponListener = listener;
    }

    public interface OnGetCouponListener {
        void onGetCoupon(long coupon_id);
    }

}
