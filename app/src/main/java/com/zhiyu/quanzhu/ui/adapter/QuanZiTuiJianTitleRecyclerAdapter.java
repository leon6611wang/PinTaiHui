package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianDaoHang;

import java.util.List;

public class QuanZiTuiJianTitleRecyclerAdapter extends RecyclerView.Adapter<QuanZiTuiJianTitleRecyclerAdapter.ViewHolder> {
    private Context context;

    private List<QuanZiTuiJianDaoHang> list;

    public QuanZiTuiJianTitleRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<QuanZiTuiJianDaoHang> titleList) {
        this.list = titleList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        View titleLine;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            titleLine = itemView.findViewById(R.id.titleLine);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_title, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getName());
        holder.titleTextView.setOnClickListener(new OnTitleClick(position));
        if (list.get(position).isChoose()) {
            holder.titleTextView.setTextSize(16);
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
            holder.titleLine.setVisibility(View.VISIBLE);
        } else {
            holder.titleTextView.setTextSize(14);
            holder.titleTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            holder.titleLine.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    class OnTitleClick implements View.OnClickListener {
        int position;

        public OnTitleClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onTitleClickListener) {
                onTitleClickListener.onTitleClick(position);
            }
        }
    }

    private OnTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        if (null != listener) {
            this.onTitleClickListener = listener;
        }
    }

    public interface OnTitleClickListener {
        void onTitleClick(int position);
    }
}
