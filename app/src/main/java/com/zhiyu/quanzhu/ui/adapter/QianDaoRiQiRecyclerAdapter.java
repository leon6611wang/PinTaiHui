package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.ArrayList;
import java.util.List;

public class QianDaoRiQiRecyclerAdapter extends RecyclerView.Adapter<QianDaoRiQiRecyclerAdapter.ViewHolder> {
    private int sum = 7;
    private int days=0;
    private Context context;
    private List<Integer> list = new ArrayList<>();

    public QianDaoRiQiRecyclerAdapter(Context ctxt) {
        this.context=ctxt;
        for (int i = 1; i < sum + 1; i++) {
            list.add(i);
        }
    }

    public void setDays(int days) {
        this.days=days;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, dayTextView2;
        View leftView,rightView;
        public ViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            dayTextView2 = itemView.findViewById(R.id.dayTextView2);
            leftView=itemView.findViewById(R.id.leftView);
            rightView=itemView.findViewById(R.id.rightView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qiandao_riqi, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0){
            holder.leftView.setVisibility(View.INVISIBLE);
            holder.rightView.setVisibility(View.VISIBLE);
        }else if(position==6){
            holder.leftView.setVisibility(View.VISIBLE);
            holder.rightView.setVisibility(View.INVISIBLE);
        }else{
            holder.leftView.setVisibility(View.VISIBLE);
            holder.rightView.setVisibility(View.VISIBLE);
        }
        if(position<days){
            holder.leftView.setBackground(context.getResources().getDrawable(R.color.text_color_yellow));
            holder.rightView.setBackground(context.getResources().getDrawable(R.color.text_color_yellow));
            holder.dayTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
            holder.dayTextView2.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
        }else{
            holder.leftView.setBackground(context.getResources().getDrawable(R.color.text_color_gray));
            holder.rightView.setBackground(context.getResources().getDrawable(R.color.text_color_gray));
            holder.dayTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_solid_bg_gray));
            holder.dayTextView2.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        }
        holder.dayTextView.setText(String.valueOf(list.get(position)));
        holder.dayTextView2.setText("第" + int2chineseNum(list.get(position)) + "天");
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    private String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while (src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }
}
