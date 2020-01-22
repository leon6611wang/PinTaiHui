package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.List;

public class QuanZiTuiJianTuiJianLabelRecyclerAdapter extends RecyclerView.Adapter<QuanZiTuiJianTuiJianLabelRecyclerAdapter.ViewHolder> {
    private List<String> list;

    public void setList(List<String> label_list) {
        this.list = label_list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView labelTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.labelTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_label, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.labelTextView.setText(list.get(position));
        holder.labelTextView.setOnClickListener(new OnAddLabelListener(position));
    }

    private class OnAddLabelListener implements View.OnClickListener {
        private int position;

        public OnAddLabelListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onLabelAddListener) {
                onLabelAddListener.onLabelAdd(position);
            }
        }
    }

    private OnLabelAddListener onLabelAddListener;

    public void setOnLabelAddListener(OnLabelAddListener listener) {
        this.onLabelAddListener = listener;
    }

    public interface OnLabelAddListener {
        void onLabelAdd(int position);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
