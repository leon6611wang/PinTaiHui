package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Visitor;

import java.util.List;

public class MyVisitorDialogRecyclerAdapter extends RecyclerView.Adapter<MyVisitorDialogRecyclerAdapter.ViewHolder>{
    private List<Visitor> list;
    public void setList(List<Visitor> visitorList){
        this.list=visitorList;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,timeTextView,contentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
            contentTextView=itemView.findViewById(R.id.contentTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_visitor_dialog_recyclerview,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.timeTextView.setText(list.get(position).getDate());
        holder.contentTextView.setText(Html.fromHtml(list.get(position).getType()));
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
