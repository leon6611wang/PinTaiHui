package com.zhiyu.quanzhu.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

public class GouWuCheJieSuanItemRecyclerAdapter extends RecyclerView.Adapter<GouWuCheJieSuanItemRecyclerAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView origPriceTextView,origDecimTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            origPriceTextView=itemView.findViewById(R.id.origPriceTextView);
            origDecimTextView=itemView.findViewById(R.id.origDecimTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_gouwuche_jiesuan, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.origPriceTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
//        holder.origDecimTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
