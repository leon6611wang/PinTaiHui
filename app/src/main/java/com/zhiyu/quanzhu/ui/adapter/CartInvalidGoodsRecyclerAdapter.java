package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class CartInvalidGoodsRecyclerAdapter extends RecyclerView.Adapter<CartInvalidGoodsRecyclerAdapter.ViewHolder> {
    private List<CartGoods> list;
    private Context context;
    private int itemIndex;

    public CartInvalidGoodsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CartGoods> gouWuCheItemItemList, int index) {
        this.list = gouWuCheItemItemList;
        this.itemIndex = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemItemSelectedImageView;
        LinearLayout itemItemSelectedLayout;
        TextView jianTextView, numberTextView, jiaTextView;
        RoundImageView goodsImgImageView;
        TextView goodsNameTextView, goodsNormsTextView, zhengshuPriceTextView, xiaoshuPriceTextView,noStockTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemItemSelectedLayout = itemView.findViewById(R.id.itemItemSelectedLayout);
            itemItemSelectedImageView = itemView.findViewById(R.id.itemItemSelectedImageView);
            jianTextView = itemView.findViewById(R.id.jianTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            jiaTextView = itemView.findViewById(R.id.jiaTextView);
            goodsImgImageView = itemView.findViewById(R.id.goodsImgImageView);
            goodsNameTextView = itemView.findViewById(R.id.goodsNameTextView);
            goodsNormsTextView = itemView.findViewById(R.id.goodsNormsTextView);
            zhengshuPriceTextView = itemView.findViewById(R.id.zhengshuPriceTextView);
            xiaoshuPriceTextView = itemView.findViewById(R.id.xiaoshuPriceTextView);
            noStockTextView=itemView.findViewById(R.id.noStockTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_invalid_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_unselect));
        }
        holder.itemItemSelectedLayout.setOnClickListener(new OnItemItemSelectedListener(position));
        Glide.with(context).load(list.get(position).getImg())
                .error(R.mipmap.img_error)
                .into(holder.goodsImgImageView);
        holder.goodsNameTextView.setText(list.get(position).getGoods_name());
        holder.goodsNormsTextView.setText(list.get(position).getNorms_name());
        long zhengshuPrice = list.get(position).getPrice() / 100;
        holder.zhengshuPriceTextView.setText(String.valueOf(zhengshuPrice));
        long xiaoshu = list.get(position).getPrice() % 100;
        String xiaoshuStr = null;
        if (xiaoshu == 0) {
            xiaoshuStr = ".00";
        } else if (xiaoshu > 0 && xiaoshu < 10) {
            xiaoshuStr = ".0" + xiaoshu;
        } else if (xiaoshu > 10) {
            xiaoshuStr = "." + xiaoshu;
        }
        holder.xiaoshuPriceTextView.setText(xiaoshuStr);
        holder.numberTextView.setText(String.valueOf(list.get(position).getNum()));
        holder.jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
        holder.jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
    }


    class OnItemItemSelectedListener implements View.OnClickListener {
        private int position;

        public OnItemItemSelectedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            boolean selected = list.get(position).isSelected();
            list.get(position).setSelected(!selected);
            notifyDataSetChanged();
            if (null != onItemItemSelected) {
                onItemItemSelected.onItemItemSelected(itemIndex, position, !selected);
            }
        }
    }


    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private OnItemItemSelected onItemItemSelected;

    public void setOnItemItemSelected(OnItemItemSelected selected) {
        this.onItemItemSelected = selected;
    }

    public interface OnItemItemSelected {
        void onItemItemSelected(int parentPosition, int childPosition, boolean selected);
    }


}
