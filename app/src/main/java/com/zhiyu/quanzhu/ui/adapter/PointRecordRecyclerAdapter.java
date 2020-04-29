package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.PointObtainRecord;

import java.util.List;

public class PointRecordRecyclerAdapter extends RecyclerView.Adapter<PointRecordRecyclerAdapter.ViewHolder> {
    private List<PointObtainRecord> list;

    public void setList(List<PointObtainRecord> recordList) {
        this.list = recordList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, timeTextView, pointTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            pointTextView = itemView.findViewById(R.id.pointTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point_record, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getTitle());
        holder.timeTextView.setText(list.get(position).getCreate_time());
        holder.pointTextView.setText("+" + list.get(position).getCredits());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
