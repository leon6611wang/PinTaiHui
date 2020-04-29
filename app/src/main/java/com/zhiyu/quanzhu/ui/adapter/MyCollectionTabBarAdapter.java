package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCollectionTab;

import java.util.List;

public class MyCollectionTabBarAdapter extends RecyclerView.Adapter<MyCollectionTabBarAdapter.ViewHolder> {
    private List<MyCollectionTab> list;
    private Context context;

    public MyCollectionTabBarAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<MyCollectionTab> tabList) {
        this.list = tabList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection_tabbar, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tabTextView.setText(list.get(position).getTitle());
        if (list.get(position).isSelected()) {
            holder.tabLineView.setVisibility(View.VISIBLE);
            holder.tabTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
        } else {
            holder.tabLineView.setVisibility(View.INVISIBLE);
            holder.tabTextView.setTextColor(context.getResources().getColor(R.color.text_color_black));
        }
        holder.itemLayout.setOnClickListener(new OnItemClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tabTextView;
        View tabLineView;
        LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tabTextView = itemView.findViewById(R.id.tabTextView);
            tabLineView = itemView.findViewById(R.id.tabLineView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    private class OnItemClick implements View.OnClickListener {
        private int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (MyCollectionTab tab : list) {
                tab.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
            if(null!=onTabChangeListener){
                onTabChangeListener.onTabChange(position);
            }
        }
    }
    private OnTabChangeListener onTabChangeListener;
    public void setOnTabChangeListener(OnTabChangeListener listener){
        this.onTabChangeListener=listener;
    }
    public interface OnTabChangeListener{
        void onTabChange(int position);
    }

}
