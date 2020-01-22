package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

public class MyOrderAllRecyclerAdapter extends RecyclerView.Adapter<MyOrderAllRecyclerAdapter.ViewHolder> {
    private Context context;

    public MyOrderAllRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MyRecyclerView shangpinRecyclerView;
        MyOrderShangPinRecyclerAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            shangpinRecyclerView = itemView.findViewById(R.id.shangpinRecyclerView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_daifukuan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.adapter = new MyOrderShangPinRecyclerAdapter();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.shangpinRecyclerView.setLayoutManager(linearLayoutManager);
        holder.shangpinRecyclerView.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
