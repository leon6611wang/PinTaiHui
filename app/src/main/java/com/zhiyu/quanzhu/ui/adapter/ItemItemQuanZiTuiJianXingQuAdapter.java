package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianQuanZi;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;

import java.util.List;

public class ItemItemQuanZiTuiJianXingQuAdapter extends RecyclerView.Adapter<ItemItemQuanZiTuiJianXingQuAdapter.ViewHolder> {
    private List<QuanZiTuiJianQuanZi> list;
    private Context context;
    private float ratioWidth=0.2923f;
    private float ratioHeight=0.7895f;
    private int imageWidth,imageHeight;
    private LinearLayout.LayoutParams ll;
    public ItemItemQuanZiTuiJianXingQuAdapter(Context context,int cardWidth) {
        this.context = context;
        imageWidth=Math.round(ratioWidth*cardWidth);
        imageHeight=Math.round(ratioHeight*imageWidth);
        ll=new LinearLayout.LayoutParams(imageWidth,imageHeight);
    }

    public void setList(List<QuanZiTuiJianQuanZi> quanZiList) {
        this.list = quanZiList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headerImageView;
        TextView nicknameTextView;
        TextView createtimeTextView;
        RoundImageView quanziImageView;
        TextView quanzinameTextView, quanzidescTextView, citylabelTextView, hangyelabelTextView, chengyuanTextView, dongtaiTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            headerImageView = itemView.findViewById(R.id.headerImageView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
            createtimeTextView = itemView.findViewById(R.id.createtimeTextView);
            quanziImageView = itemView.findViewById(R.id.quanziImageView);
            quanzinameTextView = itemView.findViewById(R.id.quanzinameTextView);
            quanzidescTextView = itemView.findViewById(R.id.quanzidescTextView);
            citylabelTextView = itemView.findViewById(R.id.citylabelTextView);
            hangyelabelTextView = itemView.findViewById(R.id.hangyelabelTextView);
            chengyuanTextView = itemView.findViewById(R.id.chengyuanTextView);
            dongtaiTextView = itemView.findViewById(R.id.dongtaiTextView);
            quanziImageView.setLayoutParams(ll);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_quanzi_tuijian_xingqu, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.headerImageView);
        Glide.with(context).load(list.get(position).getThumb()).into(holder.quanziImageView);
        holder.nicknameTextView.setText(list.get(position).getUsername());
        holder.quanzinameTextView.setText(list.get(position).getName());
        holder.quanzidescTextView.setText(list.get(position).getDescirption());
        holder.citylabelTextView.setText(list.get(position).getCity_name());
        holder.hangyelabelTextView.setText(list.get(position).getThree_industry());
        holder.chengyuanTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.dongtaiTextView.setText(String.valueOf(list.get(position).getFnum()));
        holder.createtimeTextView.setText("创建" + list.get(position).getCreated_at() + "天");

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
