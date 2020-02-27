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

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

/**
 * 圈子选择-列表数据适配器
 */
public class CircleSelectRecyclerAdapter extends RecyclerView.Adapter<CircleSelectRecyclerAdapter.ViewHolder> {
    private List<MyCircle> list;
    private Context context;

    public void setList(List<MyCircle> myCircleList) {
        this.list = myCircleList;
        notifyDataSetChanged();
    }

    public CircleSelectRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout circleSelectRootLayout;
        ImageView selectedImageView;
        NiceImageView circleImageView;
        TextView circleNameTextView, identifyTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            circleSelectRootLayout = itemView.findViewById(R.id.circleSelectRootLayout);
            selectedImageView = itemView.findViewById(R.id.selectedImageView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            identifyTextView = itemView.findViewById(R.id.identifyTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_select_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelected()) {
            holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_selected));
        } else {
            holder.selectedImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_unselect));
        }
        Glide.with(context).load(list.get(position).getThumb()).into(holder.circleImageView);
        holder.circleNameTextView.setText(list.get(position).getName());
        holder.identifyTextView.setText(list.get(position).getRoleName());
        holder.circleSelectRootLayout.setOnClickListener(new OnSelectCircleClick(position));
    }

    public MyCircle getSelectedCircle(){
        MyCircle circle=null;
        if(null!=list&&list.size()>0){
            for(MyCircle myCircle:list){
                if(myCircle.isSelected()){
                    circle=myCircle;
                }
            }
        }
        return  circle;
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnSelectCircleClick implements View.OnClickListener{
        private int position;

        public OnSelectCircleClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for(MyCircle circle:list){
                circle.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
        }
    }

}
