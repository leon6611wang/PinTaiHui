package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ComplaintReason;

import java.util.List;

public class ComplaintReasonRecyclerAdapter extends RecyclerView.Adapter<ComplaintReasonRecyclerAdapter.ViewHolder> {
    private List<ComplaintReason> list;
    private Context context;

    public ComplaintReasonRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ComplaintReason> complaintReasonList) {
        this.list = complaintReasonList;
        notifyDataSetChanged();
    }

    public List<ComplaintReason> getList(){
        return list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView selectedImageView;
        TextView reasonTextView;
        LinearLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            selectedImageView = itemView.findViewById(R.id.selectedImageView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            itemLayout=itemView.findViewById(R.id.itemLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint_reason, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.reasonTextView.setText(list.get(position).getReason());
        holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(list.get(position).isSelected() ? R.mipmap.gouwuche_selected : R.mipmap.gouwuche_unselect));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ComplaintReason reason:list){
                    reason.setSelected(false);
                }
                list.get(position).setSelected(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

}
