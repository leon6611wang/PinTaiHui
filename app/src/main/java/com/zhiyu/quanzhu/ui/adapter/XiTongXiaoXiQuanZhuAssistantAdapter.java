package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZhuAssistant;
import com.zhiyu.quanzhu.ui.activity.H5PageActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class XiTongXiaoXiQuanZhuAssistantAdapter extends RecyclerView.Adapter<XiTongXiaoXiQuanZhuAssistantAdapter.ViewHolder> {
    private List<QuanZhuAssistant> list;

    public void setList(List<QuanZhuAssistant> assistantList) {
        this.list = assistantList;
        notifyDataSetChanged();
    }

    private Context context;

    public XiTongXiaoXiQuanZhuAssistantAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, contentTextView;
        NiceImageView mImageView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            mImageView = itemView.findViewById(R.id.mImageView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            mCardView = itemView.findViewById(R.id.mCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_xitongxiaoxi_quan_zhu_assistant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCardView.setOnClickListener(new OnCardViewClick(position));
        Glide.with(context).load(list.get(position).getContent().getImg()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.mImageView);
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.contentTextView.setText(list.get(position).getContent().getContent());

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnCardViewClick implements View.OnClickListener {
        private int position;

        public OnCardViewClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(!StringUtils.isNullOrEmpty(list.get(position).getContent().getHandler_url())){
                Intent intent = new Intent(context, H5PageActivity.class);
                intent.putExtra("url", list.get(position).getContent().getHandler_url());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
