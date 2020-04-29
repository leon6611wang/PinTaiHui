package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Delivery;

import java.util.List;

public class DeliveryInfoRecyclerAdapter extends RecyclerView.Adapter<DeliveryInfoRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Delivery> list;

    public void setData(List<Delivery> wuLiuList) {
        this.list = wuLiuList;
        notifyDataSetChanged();
    }

    public DeliveryInfoRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View jiedianView, lefttopView, leftbottomView;
        TextView wuliuTextView;
        TextView shijianTextView;
        LinearLayout wuliulayout;

        public ViewHolder(View itemView) {
            super(itemView);
            jiedianView = itemView.findViewById(R.id.jiedianView);
            wuliuTextView = itemView.findViewById(R.id.wuliuTextView);
            shijianTextView = itemView.findViewById(R.id.shijianTextView);
            lefttopView = itemView.findViewById(R.id.lefttopView);
            leftbottomView = itemView.findViewById(R.id.leftbottomView);
            wuliulayout=itemView.findViewById(R.id.wuliulayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.wuliuTextView.setText(list.get(position).getContext());
        holder.shijianTextView.setText(list.get(position).getTime());
        if (position == 0) {
            holder.wuliulayout.setVerticalGravity(Gravity.TOP);
            holder.jiedianView.setBackground(context.getResources().getDrawable(R.drawable.shape_wuliu_red));
            holder.wuliuTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
            holder.shijianTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
            holder.lefttopView.setVisibility(View.GONE);
            holder.leftbottomView.setVisibility(View.VISIBLE);
        } else {
            holder.jiedianView.setBackground(context.getResources().getDrawable(R.drawable.shape_wuliu_gray));
            holder.wuliuTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.shijianTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.lefttopView.setVisibility(View.VISIBLE);
            if (position == list.size() - 1) {
                holder.leftbottomView.setVisibility(View.GONE);
                holder.wuliulayout.setGravity(Gravity.BOTTOM);
            } else {
                holder.leftbottomView.setVisibility(View.VISIBLE);
                holder.wuliulayout.setVerticalGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
