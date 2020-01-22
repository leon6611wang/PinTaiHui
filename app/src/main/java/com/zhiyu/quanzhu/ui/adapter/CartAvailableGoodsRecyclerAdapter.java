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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class CartAvailableGoodsRecyclerAdapter extends RecyclerView.Adapter<CartAvailableGoodsRecyclerAdapter.ViewHolder> {
    private List<CartGoods> list;
    private Context context;
    private int itemIndex;

    public CartAvailableGoodsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CartGoods> gouWuCheItemItemList, int index) {
        this.list = gouWuCheItemItemList;
        this.itemIndex = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemItemSelectedImageView;
        LinearLayout itemItemSelectedLayout, noStockLayout;
        TextView jianTextView, numberTextView, jiaTextView;
        RoundImageView goodsImgImageView;
        TextView goodsNameTextView, goodsNormsTextView, zhengshuPriceTextView, xiaoshuPriceTextView, noStockTextView;

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
            noStockLayout = itemView.findViewById(R.id.noStockLayout);
            noStockTextView = itemView.findViewById(R.id.noStockTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_available_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.itemItemSelectedImageView.setImageDrawable(context.getDrawable(R.mipmap.gouwuche_unselect));
        }
        holder.itemItemSelectedLayout.setOnClickListener(new OnItemItemSelectedListener(position, holder.numberTextView));

        holder.jianTextView.setOnClickListener(new OnJianClickListener(position, holder.numberTextView, holder.jianTextView, holder.jiaTextView));
        holder.jiaTextView.setOnClickListener(new OnJiaClickListener(position, holder.numberTextView, holder.jianTextView, holder.jiaTextView));
        Glide.with(context).load(list.get(position).getImg())
                .error(R.mipmap.img_error)
                .into(holder.goodsImgImageView);
        holder.goodsNameTextView.setText(list.get(position).getStock() + " - " + list.get(position).getGoods_name());
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
        list.get(position).setCurrentNum(list.get(position).getNum());

        if (list.get(position).getNum() > 1) {
            holder.jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
        } else if (list.get(position).getNum() == 1) {
            holder.jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
        }
        if (list.get(position).getStock() == 0 || list.get(position).getStock() < list.get(position).getNum()) {
            holder.noStockLayout.setVisibility(View.VISIBLE);
            holder.jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
        } else {
            holder.noStockLayout.setVisibility(View.GONE);
            holder.jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
        }
    }


    class OnItemItemSelectedListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView;
        private int currentNumber;

        public OnItemItemSelectedListener(int position, TextView numberTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr))
                currentNumber = Integer.parseInt(numberstr);
            if (list.get(position).getStock() == 0 || list.get(position).getStock() < currentNumber) {
                Toast.makeText(context, "库存不足，无法选定.", Toast.LENGTH_SHORT).show();
            } else {
                boolean selected = list.get(position).isSelected();
                list.get(position).setSelected(!selected);
                notifyDataSetChanged();
                if (null != onItemItemSelected) {
                    onItemItemSelected.onItemItemSelected(itemIndex, position, !selected);
                }
            }

        }
    }

    class OnJianClickListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView, jianTextView, jiaTextView;
        private int currentNumber;

        public OnJianClickListener(int position, TextView numberTextView, TextView jianTextView, TextView jiaTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
            this.jianTextView = jianTextView;
            this.jiaTextView = jiaTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr))
                currentNumber = Integer.parseInt(numberstr);
            if (currentNumber > 1) {
                currentNumber--;
                numberTextView.setText(String.valueOf(currentNumber));
                list.get(position).setCurrentNum(currentNumber);
            }
            if (currentNumber > 1) {
                jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
            } else {
                jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
            }

            if (currentNumber < list.get(position).getStock() && currentNumber >= 1) {
                jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
            } else {
                jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
            }
        }
    }

    class OnJiaClickListener implements View.OnClickListener {
        private int position;
        private TextView numberTextView, jianTextView, jiaTextView;
        private int currentNumber;

        public OnJiaClickListener(int position, TextView numberTextView, TextView jianTextView, TextView jiaTextView) {
            this.position = position;
            this.numberTextView = numberTextView;
            this.jianTextView = jianTextView;
            this.jiaTextView = jiaTextView;
        }

        @Override
        public void onClick(View v) {
            String numberstr2 = numberTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(numberstr2))
                currentNumber = Integer.parseInt(numberstr2);
            if (currentNumber < list.get(position).getStock()) {
                currentNumber++;
                numberTextView.setText(String.valueOf(currentNumber));
                list.get(position).setCurrentNum(currentNumber);
                if (currentNumber > 1) {
                    jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jianTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }

                if (currentNumber < list.get(position).getStock()) {
                    jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_usable));
                } else {
                    jiaTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_buy_count_unusable));
                }
            } else {
                Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
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
