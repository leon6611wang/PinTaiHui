package com.zhiyu.quanzhu.ui.adapter.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;

import java.util.List;

/**
 * Created by Administrator on 2018/12/24.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private List<CardBean> mCardBeenList;
    private CardItemCallback mCardItemCallback;
    private Context context;
    public CardAdapter(Context mContext,List<CardBean> cardBeenList) {
        mCardBeenList = cardBeenList;
        context=mContext;
    }

    public void setCardItemCallback(CardItemCallback cardItemCallback) {
        this.mCardItemCallback = cardItemCallback;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("zs", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_recyclerview, null);
        CardHolder holder = new CardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Log.i("zs", "onBindViewHolder");
        Glide.with(context).load(mCardBeenList.get(position).getPic()).into(holder.imageView);
        holder.tv_name.setText(mCardBeenList.get(position).getName());
        holder.tv_year.setText(mCardBeenList.get(position).getBallYear());
        holder.tv_team.setText(mCardBeenList.get(position).getTeam());
    }

    @Override
    public int getItemCount() {
        return mCardBeenList.size();
    }

    class CardHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tv_name, tv_year, tv_team;

        public CardHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_team = itemView.findViewById(R.id.tv_team);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCardItemCallback != null) {
                        mCardItemCallback.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface CardItemCallback {
        void onItemClick(int position);

    }
}
