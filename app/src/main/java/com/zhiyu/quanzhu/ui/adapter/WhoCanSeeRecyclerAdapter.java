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
import com.zhiyu.quanzhu.model.bean.WhoCanSee;

import java.util.List;

public class WhoCanSeeRecyclerAdapter extends RecyclerView.Adapter<WhoCanSeeRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<WhoCanSee> list;

    public WhoCanSeeRecyclerAdapter(Context context) {
        this.context = context;
    }

    public WhoCanSee getWhoCanSee() {
        WhoCanSee see = null;
        if (null != list && list.size() > 0) {
            for (WhoCanSee whoCanSee : list) {
                if (whoCanSee.isSelected()) {
                    see = whoCanSee;
                }
            }
        }
        return see;
    }

    public void setList(List<WhoCanSee> whoCanSeeList) {
        this.list = whoCanSeeList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemRootLayout;
        ImageView selectedImageView;
        TextView titleTextView, descTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRootLayout = itemView.findViewById(R.id.itemRootLayout);
            selectedImageView = itemView.findViewById(R.id.selectedImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_who_can_see, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_unselect));
        }
        holder.titleTextView.setText(list.get(position).getTitle());
        holder.descTextView.setText(list.get(position).getDesc());
        holder.itemRootLayout.setOnClickListener(new OnSelectedClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnSelectedClick implements View.OnClickListener {
        private int position;

        public OnSelectedClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (WhoCanSee see : list) {
                see.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
        }
    }
}
