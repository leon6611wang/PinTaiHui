package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.CartShop;

import java.util.List;

public class CartInvalidShopRecyclerAdapter extends RecyclerView.Adapter<CartInvalidShopRecyclerAdapter.ViewHodler> implements CartInvalidGoodsRecyclerAdapter.OnItemItemSelected {
    private Context context;
    private List<CartShop> list;

    public void setData(List<CartShop> gouWuCheItemList) {
        this.list = gouWuCheItemList;
        notifyDataSetChanged();
    }

    public List<CartShop> getList() {
        return list;
    }

    public CartInvalidShopRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHodler extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        CartInvalidGoodsRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        ImageView itemSelectedImageView;
        LinearLayout itemSelectedLayout;
        ImageView shopIconImageView;
        TextView shopNameTextView, getCouponTextView;

        public ViewHodler(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new CartInvalidGoodsRecyclerAdapter(context);
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            itemSelectedImageView = itemView.findViewById(R.id.itemSelectedImageView);
            itemSelectedLayout = itemView.findViewById(R.id.itemSelectedLayout);
            shopIconImageView = itemView.findViewById(R.id.shopIconImageView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
            getCouponTextView = itemView.findViewById(R.id.getCouponTextView);
        }
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_invalid_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        if (list.get(position).isSelected()) {
            holder.itemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.itemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_unselect));
        }
        holder.itemSelectedLayout.setOnClickListener(new OnItemSelectedListener(position));
        holder.adapter.setData(list.get(position).getList(), position);
        holder.adapter.setOnItemItemSelected(this);
        holder.mRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.mRecyclerView.setAdapter(holder.adapter);
        Glide.with(context).load(list.get(position).getIcon())
                .error(R.mipmap.img_error)
                .into(holder.shopIconImageView);
        holder.shopNameTextView.setText(list.get(position).getName());
    }


    class OnItemSelectedListener implements View.OnClickListener {
        private int position;

        public OnItemSelectedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            boolean selected = list.get(position).isSelected();
            list.get(position).setSelected(!selected);
            for (CartGoods itemItem : list.get(position).getList()) {
                itemItem.setSelected(!selected);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onItemItemSelected(int parentPosition, int childPosition, boolean selected) {
        list.get(parentPosition).getList().get(childPosition).setSelected(selected);
        boolean allItemSelected = true;
        for (CartGoods itemItem : list.get(parentPosition).getList()) {
            if (!itemItem.isSelected()) {
                allItemSelected = false;
            }
        }
        list.get(parentPosition).setSelected(allItemSelected);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
