package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.WebLoginShop;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class WebLoginShopAdapter extends RecyclerView.Adapter<WebLoginShopAdapter.ViewHolder> {
    private Context context;
    private List<WebLoginShop> list;

    public void setList(List<WebLoginShop> shopList) {
        this.list = shopList;
        notifyDataSetChanged();
    }

    public WebLoginShopAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout shopLayout;
        NiceImageView iconImageView;
        TextView nameTextView;
        ImageView selectImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            shopLayout = itemView.findViewById(R.id.shopLayout);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            selectImageView = itemView.findViewById(R.id.selectImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_web_login_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getLogo()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.selectImageView.setImageDrawable(list.get(position).isSelect() ? context.getResources().getDrawable(R.mipmap.draft_selected) :
                context.getResources().getDrawable(R.mipmap.draft_unselect));
        holder.shopLayout.setOnClickListener(new OnSelectClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnSelectClick implements View.OnClickListener {
        private int position;

        public OnSelectClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (WebLoginShop shop : list) {
                shop.setSelect(false);
            }
            list.get(position).setSelect(true);
            notifyDataSetChanged();
            if (null != onSelectShopListener) {
                onSelectShopListener.onSelectShop(list.get(position));
            }

        }
    }

    private OnSelectShopListener onSelectShopListener;

    public void setOnSelectShopListener(OnSelectShopListener listener) {
        this.onSelectShopListener = listener;
    }

    public interface OnSelectShopListener {
        void onSelectShop(WebLoginShop shop);
    }
}
