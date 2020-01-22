package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.dialog.FangKeXiangQingDialog;

public class FangKeRecyclerAdapter  extends RecyclerView.Adapter<FangKeRecyclerAdapter.ViewHolder>{
    private FangKeXiangQingDialog fangKeXiangQingDialog;
    private Activity activity;
    public FangKeRecyclerAdapter(Activity aty) {
        this.activity=aty;
        fangKeXiangQingDialog=new FangKeXiangQingDialog(activity,R.style.dialog);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView itemCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemCardView=itemView.findViewById(R.id.itemCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fangke,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fangKeXiangQingDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
