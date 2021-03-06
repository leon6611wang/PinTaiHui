package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.VIP;
import com.zhiyu.quanzhu.ui.activity.AllVIPEquityActivity;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class BuyVipRecyclerAdapter extends RecyclerView.Adapter<BuyVipRecyclerAdapter.ViewHolder> {
    private Context context;
    private ViewGroup.LayoutParams params;
    private List<VIP> list;


    public void setList(List<VIP> vipList) {
        this.list = vipList;
        notifyDataSetChanged();
    }

    public BuyVipRecyclerAdapter(Context context, int cardWidth, int cardHeight) {
        this.context = context;
        params = new ViewGroup.LayoutParams(cardWidth, cardHeight);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView vipIconImageView;
        TextView vipNametTextView, vipPriceTextView, moreEquityTextView;
        ListView listView;
        VIPEquityListAdapter adapter;
        TextView buyButtonTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setLayoutParams(params);
            vipIconImageView = itemView.findViewById(R.id.vipIconImageView);
            vipNametTextView = itemView.findViewById(R.id.vipNametTextView);
            listView = itemView.findViewById(R.id.listView);
            vipPriceTextView = itemView.findViewById(R.id.vipPriceTextView);
            adapter = new VIPEquityListAdapter();
            listView.setAdapter(adapter);
            moreEquityTextView = itemView.findViewById(R.id.moreEquityTextView);
            buyButtonTextView = itemView.findViewById(R.id.buyButtonTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_buy_vip_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getIcon()).into(holder.vipIconImageView);
        holder.vipNametTextView.setText(list.get(position).getName());
        holder.vipPriceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getPrice()));
        holder.adapter.setList(list.get(position).getEquity_list());
        switch (list.get(position).getStatus()){
            //0 不可购买 1续费 2购买
            case 0:
               holder.buyButtonTextView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.buyButtonTextView.setText("立即续费");
                break;
            case 2:
                holder.buyButtonTextView.setText("立即购买");
                break;
        }
        holder.moreEquityTextView.setOnClickListener(new OnMoreEquityClick(position));
        holder.buyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(null!=onBuyVIPListener){
                   onBuyVIPListener.onBuyVIP(position);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnMoreEquityClick implements View.OnClickListener {
        private int position;

        public OnMoreEquityClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent moreEquityIntent = new Intent(context, AllVIPEquityActivity.class);
            moreEquityIntent.putExtra("vid", list.get(position).getId());
            moreEquityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(moreEquityIntent);
        }
    }
    private OnBuyVIPListener onBuyVIPListener;
    public void setOnBuyVIPListener(OnBuyVIPListener listener){
        this.onBuyVIPListener=listener;
    }
    public interface OnBuyVIPListener{
        void onBuyVIP(int position);
    }
}
