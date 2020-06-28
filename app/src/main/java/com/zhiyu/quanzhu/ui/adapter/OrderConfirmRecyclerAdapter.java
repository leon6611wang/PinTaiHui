package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderConfirmShop;
import com.zhiyu.quanzhu.ui.dialog.OrderConfirmUseCouponDialog;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmRecyclerAdapter extends BaseRecyclerAdapter<OrderConfirmShop> {
    private Context context;
    private OrderConfirmUseCouponDialog useCouponDialog;
    private Map<Integer, Boolean> map = new HashMap<>();

    public OrderConfirmRecyclerAdapter(Context context) {
        this.context = context;
        useCouponDialog = new OrderConfirmUseCouponDialog(context, R.style.dialog, new OrderConfirmUseCouponDialog.OnSelectCouponListener() {
            @Override
            public void onSelectCoupon(int index, boolean isUseCoupon) {
                if (null != onUseCouponListener) {
                    onUseCouponListener.onUseCoupon(index, isUseCoupon);
                }
                map.put(index, isUseCoupon);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gouwuche_jiesuan, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, final int RealPosition, final OrderConfirmShop data) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        Glide.with(context).load(data.getIcon()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(data.getName());
        holder.goodsCountTextView.setText(String.valueOf(data.getGoods_num()));
        long payPrice = 0;
        if (map.containsKey(RealPosition)) {
            if (map.get(RealPosition)) {
                holder.useCouponTextView.setText("组合优惠");
                holder.discountPriceTextView.setText("-￥" + PriceParseUtils.getInstance().parsePrice(data.getDiscount_price()));
                payPrice = data.getPay_price();
            } else {
                holder.useCouponTextView.setText("不使用优惠");
                holder.discountPriceTextView.setText("-￥0.00");
                payPrice = data.getAll_price() + data.getPostage_price();
            }
        } else {
            holder.useCouponTextView.setText("组合优惠");
            holder.discountPriceTextView.setText("-￥" + PriceParseUtils.getInstance().parsePrice(data.getDiscount_price()));
            payPrice = data.getPay_price();
        }
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(payPrice));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(payPrice));
        if (data.getDiscount_price() > 0) {
            holder.couponLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    useCouponDialog.show();
                    useCouponDialog.setIndex(RealPosition, data.getDiscount_price());
                    boolean use = true;
                    if (map.containsKey(RealPosition)) {
                        use = map.get(RealPosition);
                    }
                    useCouponDialog.setUseCoupon(use);
                }
            });
        }
        String postage = "";
        switch (data.getPostage_status()) {
            case 2:
                postage = "所有商品超出配送范围";
                break;
            case 1:
                postage = "部分商品超出配送范围";
                break;
            case 0:
                if (data.getPostage_price() == 0) {
                    postage = "包邮";
                } else if (data.getPostage_price() > 0) {
                    postage = "￥" + PriceParseUtils.getInstance().parsePrice(data.getPostage_price());
                }
                break;
        }
        holder.postagePriceTextView.setText(postage);
        holder.mRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.adapter.setList(data.getList());
        holder.remarkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != onRemarkEditListener) {
                    onRemarkEditListener.onRemarkEdit(RealPosition, holder.remarkEditText.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        OrderConfirmItemRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        ImageView iconImageView;
        TextView nameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView, discountPriceTextView,
                postagePriceTextView;
        EditText remarkEditText;
        LinearLayout couponLayout;
        TextView useCouponTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            couponLayout = itemView.findViewById(R.id.couponLayout);
            useCouponTextView = itemView.findViewById(R.id.useCouponTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            goodsCountTextView = itemView.findViewById(R.id.goodsCountTextView);
            zhengshuTextView = itemView.findViewById(R.id.zhengshuTextView);
            xiaoshuTextView = itemView.findViewById(R.id.xiaoshuTextView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new OrderConfirmItemRecyclerAdapter(context);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setAdapter(adapter);
            remarkEditText = itemView.findViewById(R.id.remarkEditText);
            discountPriceTextView = itemView.findViewById(R.id.discountPriceTextView);
            postagePriceTextView = itemView.findViewById(R.id.postagePriceTextView);

        }
    }

    private OnRemarkEditListener onRemarkEditListener;

    public void setOnRemarkEditListener(OnRemarkEditListener listener) {
        this.onRemarkEditListener = listener;
    }

    public interface OnRemarkEditListener {
        void onRemarkEdit(int position, String remark);
    }

    private OnUseCouponListener onUseCouponListener;

    public void setOnUseCouponListener(OnUseCouponListener listener) {
        this.onUseCouponListener = listener;
    }

    public interface OnUseCouponListener {
        void onUseCoupon(int position, boolean isUse);
    }
}
