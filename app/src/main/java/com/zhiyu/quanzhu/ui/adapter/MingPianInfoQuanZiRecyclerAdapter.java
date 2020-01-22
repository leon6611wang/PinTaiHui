package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class MingPianInfoQuanZiRecyclerAdapter extends RecyclerView.Adapter<MingPianInfoQuanZiRecyclerAdapter.ViewHolder> {
    private Context context;
    private ViewGroup.LayoutParams vl;
    private List<Circle> list;

    public void setList(List<Circle> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public MingPianInfoQuanZiRecyclerAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        vl = new ViewGroup.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headerImageView;
        TextView nameTextView, dateTextView, circleNameTextView, circleDescTextView, pnumTextView, fnumTextView;
        RoundImageView circleImageView;

        private LinearLayout quanziLayout;
        private RecyclerView quanziLabelRecyclerView;
        private TypeRecyclerAdapter typeRecyclerAdapter;
        private List<String> list = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);
            headerImageView = itemView.findViewById(R.id.headerImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            circleDescTextView = itemView.findViewById(R.id.circleDescTextView);
            pnumTextView = itemView.findViewById(R.id.pnumTextView);
            fnumTextView = itemView.findViewById(R.id.fnumTextView);

//            list.add("马鞍山");
//            list.add("IT圈");
            quanziLayout = itemView.findViewById(R.id.quanziLayout);
            quanziLayout.setLayoutParams(vl);
            quanziLabelRecyclerView = itemView.findViewById(R.id.quanziLabelRecyclerView);
            typeRecyclerAdapter = new TypeRecyclerAdapter(context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            quanziLabelRecyclerView.setLayoutManager(linearLayoutManager);
            quanziLabelRecyclerView.setAdapter(typeRecyclerAdapter);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian_info_quanzi, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUseravatar()).into(holder.headerImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.dateTextView.setText("创建" + list.get(position).getDays() + "天");
        Glide.with(context).load(list.get(position).getThumb()).into(holder.circleImageView);
        holder.circleNameTextView.setText(list.get(position).getName());
        holder.circleDescTextView.setText(list.get(position).getNotice());
        holder.pnumTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.fnumTextView.setText(String.valueOf(list.get(position).getFnum()));
        if (holder.list.size() == 0) {
            if (!TextUtils.isEmpty(list.get(position).getCity_name()) && "null" != list.get(position).getCity_name())
                holder.list.add(list.get(position).getCity_name());
            if (!TextUtils.isEmpty(list.get(position).getIndustry()) && "null" != list.get(position).getIndustry())
                holder.list.add(list.get(position).getIndustry());
            holder.typeRecyclerAdapter.setData(holder.list);
        }

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
