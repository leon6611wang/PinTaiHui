package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.CartShop;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.GoodsCouponsDialog;

import java.util.List;

public class CartAvailableShopRecyclerAdapter extends RecyclerView.Adapter<CartAvailableShopRecyclerAdapter.ViewHodler> implements CartAvailableGoodsRecyclerAdapter.OnItemItemSelected,
        CartAvailableGoodsRecyclerAdapter.OnGoodsNumChangeListener {
    private Context context;
    private List<CartShop> list;
    private GoodsCouponsDialog couponsDialog;

    private boolean manage;

    public void setManage(boolean isManage) {
        this.manage = isManage;
        notifyDataSetChanged();
    }

    public void setData(List<CartShop> gouWuCheItemList) {
        this.list = gouWuCheItemList;
        notifyDataSetChanged();
    }

    public List<CartShop> getList() {
        return list;
    }

    public CartAvailableShopRecyclerAdapter(Context context) {
        this.context = context;
        couponsDialog = new GoodsCouponsDialog(context, R.style.dialog);
    }

    class ViewHodler extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        CartAvailableGoodsRecyclerAdapter adapter;
        LinearLayoutManager linearLayoutManager;
        ImageView itemSelectedImageView;
        LinearLayout itemSelectedLayout;
        ImageView shopIconImageView;
        TextView shopNameTextView, getCouponTextView;

        public ViewHodler(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new CartAvailableGoodsRecyclerAdapter(context);
            adapter.setOnChangeNormsListener(new CartAvailableGoodsRecyclerAdapter.OnChangeNormsListener() {
                @Override
                public void onChangeNorms() {
                    if (null != onChangeNormsListener) {
                        onChangeNormsListener.onChangeNorms();
                    }
                }
            });
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
        return new ViewHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_available_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, final int position) {
        if (list.get(position).isSelected()) {
            holder.itemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.itemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_unselect));
        }
        if (list.get(position).isHas_counpon()) {
            holder.getCouponTextView.setVisibility(View.VISIBLE);
            holder.getCouponTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    couponsDialog.show();
                    couponsDialog.setShopId(Integer.parseInt(list.get(position).getShop_id()));
                }
            });
        } else {
            holder.getCouponTextView.setVisibility(View.INVISIBLE);
        }
        holder.shopNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopInformationActivity.class);
                intent.putExtra("shop_id", list.get(position).getShop_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.itemSelectedLayout.setOnClickListener(new OnItemSelectedListener(position));
        holder.adapter.setData(list.get(position).getList(), position);
        holder.adapter.setOnItemItemSelected(this);
        holder.adapter.setOnGoodsNumChangeListener(this);
        holder.adapter.setManage(manage);
        holder.mRecyclerView.setLayoutManager(holder.linearLayoutManager);
        holder.mRecyclerView.setAdapter(holder.adapter);
        Glide.with(context).load(list.get(position).getIcon())
                .error(R.drawable.image_error)
                .into(holder.shopIconImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getName()))
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
//            if (!selected) {
//                for (CartGoods itemItem : list.get(position).getList()) {
//                    itemItem.setSelected(!selected, false);
//                }
//            }
            for (CartGoods itemItem : list.get(position).getList()) {
                itemItem.setSelected(!selected, manage);
            }
            notifyDataSetChanged();
            if (null != onCartItemSelectListener) {
                onCartItemSelectListener.onCartItemSelect();
            }
        }
    }

    @Override
    public void onItemItemSelected(int parentPosition, int childPosition, boolean selected, boolean isManage) {
        list.get(parentPosition).getList().get(childPosition).setSelected(selected, isManage);
        boolean allItemSelected = true;
        for (CartGoods itemItem : list.get(parentPosition).getList()) {
            if (!itemItem.isSelected()) {
                allItemSelected = false;
            }
        }
        list.get(parentPosition).setSelected(allItemSelected);
        notifyDataSetChanged();
        if (null != onCartItemSelectListener) {
            onCartItemSelectListener.onCartItemSelect();
        }
    }

    @Override
    public void onGoodsNumChange() {
        if (null != onCartItemSelectListener) {
            onCartItemSelectListener.onCartItemSelect();
        }
    }

    private OnCartItemSelectListener onCartItemSelectListener;

    public void setOnCartItemSelectListener(OnCartItemSelectListener listener) {
        this.onCartItemSelectListener = listener;
    }

    public interface OnCartItemSelectListener {
        void onCartItemSelect();
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    private OnChangeNormsListener onChangeNormsListener;

    public void setOnChangeNormsListener(OnChangeNormsListener listener) {
        this.onChangeNormsListener = listener;
    }

    public interface OnChangeNormsListener {
        void onChangeNorms();
    }
}
