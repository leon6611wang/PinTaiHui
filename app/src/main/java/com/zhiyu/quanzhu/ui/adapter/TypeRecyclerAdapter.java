package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.List;

public class TypeRecyclerAdapter extends RecyclerView.Adapter<TypeRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<String> list;

    public TypeRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.typeTextView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
