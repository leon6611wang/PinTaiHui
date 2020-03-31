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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderConfirmShop;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class OrderConfirmRecyclerAdapter extends BaseRecyclerAdapter<OrderConfirmShop> {
    private Context context;

    public OrderConfirmRecyclerAdapter(Context context) {
        this.context = context;
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
    public void onBind(RecyclerView.ViewHolder viewHolder, final int RealPosition, OrderConfirmShop data) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        Glide.with(context).load(data.getIcon()).error(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(data.getName());
        holder.goodsCountTextView.setText(String.valueOf(data.getGoods_num()));
        holder.zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(data.getAll_price()));
        holder.xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(data.getAll_price()));
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
        TextView nameTextView, goodsCountTextView, zhengshuTextView, xiaoshuTextView;
        EditText remarkEditText;

        public ViewHolder(View itemView) {
            super(itemView);
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

        }
    }

    private OnRemarkEditListener onRemarkEditListener;

    public void setOnRemarkEditListener(OnRemarkEditListener listener) {
        this.onRemarkEditListener = listener;
    }

    public interface OnRemarkEditListener {
        void onRemarkEdit(int position, String remark);
    }
}
