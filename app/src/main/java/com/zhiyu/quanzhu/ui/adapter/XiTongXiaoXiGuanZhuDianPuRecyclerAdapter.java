package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GuanZhuDianPu;
import com.zhiyu.quanzhu.ui.activity.H5PageActivity;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class XiTongXiaoXiGuanZhuDianPuRecyclerAdapter extends RecyclerView.Adapter<XiTongXiaoXiGuanZhuDianPuRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<GuanZhuDianPu> list;

    public XiTongXiaoXiGuanZhuDianPuRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<GuanZhuDianPu> guanZhuDianPuList) {
        this.list = guanZhuDianPuList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView iconImageView;
        TextView nameTextView, actTitleTextView, actContentTextView,
                timeTextView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            actTitleTextView = itemView.findViewById(R.id.actTitleTextView);
            actContentTextView = itemView.findViewById(R.id.actContentTextView);
            mCardView = itemView.findViewById(R.id.mCardView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_guanzhudianpu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getShop_thumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.nameTextView.setText(list.get(position).getShop_name());
        holder.actTitleTextView.setText(list.get(position).getAct_title());
//        holder.actContentTextView.setText(list.get(position).getAct_content());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, H5PageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("url", list.get(position).getAct_url());
//                context.startActivity(intent);

                Intent intent = new Intent(context, ShopInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("shop_id", String.valueOf(list.get(position).getShop_id()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
