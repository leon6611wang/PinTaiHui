package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.PointExchangeRecord;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class PointExchangeRecordRecyclerAdapter extends RecyclerView.Adapter<PointExchangeRecordRecyclerAdapter.ViewHolder> {
    private List<PointExchangeRecord> list;
    private Context context;

    public PointExchangeRecordRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PointExchangeRecord> recordList) {
        this.list = recordList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView,nameTextView,pointTextView;
        RoundImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
            iconImageView=itemView.findViewById(R.id.iconImageView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            pointTextView=itemView.findViewById(R.id.pointTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_exchange_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeTextView.setText(list.get(position).getAddtime());
        Glide.with(context).load(list.get(position).getThumb()).error(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.pointTextView.setText("-"+list.get(position).getNeed_inegral()+"积分");
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
