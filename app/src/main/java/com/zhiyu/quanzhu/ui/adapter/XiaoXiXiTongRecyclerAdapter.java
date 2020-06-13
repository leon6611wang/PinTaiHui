package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.utils.MessageTypeUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.SystemMessage;
import com.zhiyu.quanzhu.model.bean.XiTongXiaoXi;
import com.zhiyu.quanzhu.ui.activity.CustomerServiceActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanZhuAssistantActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiTouSuFanKuiActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiGuanZhuDianPuActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiKaQuanTongZhiActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanYouShenHeActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanZiShenHeActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiZhiFuTongZhiActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class XiaoXiXiTongRecyclerAdapter extends RecyclerView.Adapter<XiaoXiXiTongRecyclerAdapter.ViewHolder> {
    private List<XiTongXiaoXi> list;
    private Context context;

    public XiaoXiXiTongRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<XiTongXiaoXi> xiaoXiList) {
        this.list = xiaoXiList;
        Collections.sort(list, new Comparator<XiTongXiaoXi>() {
            @Override
            public int compare(XiTongXiaoXi o1, XiTongXiaoXi o2) {
                int sort = (int) (o2.getMessage_time() - o1.getMessage_time());
//                System.out.println("--> sort: "+sort);
                return sort;
            }
        });
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iconImageView;
        TextView nameTextView, labelTextView, timeTextView, msgTextView, msgCountTextView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            labelTextView = itemView.findViewById(R.id.labelTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            msgTextView = itemView.findViewById(R.id.msgTextView);
            msgCountTextView = itemView.findViewById(R.id.msgCountTextView);
            mCardView = itemView.findViewById(R.id.mCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitong_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getMessage_type() > 0) {
            holder.iconImageView.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));
        } else {
            Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                    .fallback(R.drawable.image_error).into(holder.iconImageView);
        }

        holder.nameTextView.setText(list.get(position).getName());
        if (!StringUtils.isNullOrEmpty(list.get(position).getTime()))
            holder.timeTextView.setText(list.get(position).getTime());
        if (!StringUtils.isNullOrEmpty(list.get(position).getMsg()))
            holder.msgTextView.setText(list.get(position).getMsg());
        if (list.get(position).getMsgCount() > 0) {
            holder.msgCountTextView.setVisibility(View.VISIBLE);
            if (list.get(position).getMsgCount() < 10) {
                holder.msgCountTextView.setTextSize(9);
                holder.msgCountTextView.setText(String.valueOf(list.get(position).getMsgCount()));
            } else if (list.get(position).getMsgCount() > 10 && list.get(position).getMsgCount() < 100) {
                holder.msgCountTextView.setTextSize(9);
                holder.msgCountTextView.setText(String.valueOf(list.get(position).getMsgCount()));
            } else if (list.get(position).getMsgCount() > 99) {
                holder.msgCountTextView.setTextSize(6);
                holder.msgCountTextView.setText("99+");
            }
        } else {
            holder.msgCountTextView.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).isGuanFang()) {
            holder.labelTextView.setVisibility(View.VISIBLE);
        } else {
            holder.labelTextView.setVisibility(View.GONE);
        }
        holder.mCardView.setOnClickListener(new OnItemClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    class OnItemClick implements View.OnClickListener {
        private int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (list.get(position).getMessage_type()) {
                case 0:
                    Intent serviceIntent = new Intent(context, CustomerServiceActivity.class);
                    serviceIntent.putExtra("shop_id", list.get(position).getShop_id());
                    serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(serviceIntent);
                    break;
                case MessageTypeUtils.XIAO_MI_SHU:
                    Intent assistantIntent = new Intent(context, XiTongXiaoXiQuanZhuAssistantActivity.class);
                    assistantIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(assistantIntent);
                    break;
                case MessageTypeUtils.QUAN_YOU_QING_QIU:
                    Intent quanyouIntent = new Intent(context, XiTongXiaoXiQuanYouShenHeActivity.class);
                    quanyouIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(quanyouIntent);
                    break;
                case MessageTypeUtils.QUAN_ZI_SHEN_HE:
                    Intent quanziIntent = new Intent(context, XiTongXiaoXiQuanZiShenHeActivity.class);
                    quanziIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(quanziIntent);
                    break;
                case MessageTypeUtils.FU_KUAN_TONG_ZHI:
                    Intent tuikuanIntent = new Intent(context, XiTongXiaoXiZhiFuTongZhiActivity.class);
                    tuikuanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(tuikuanIntent);
                    break;
                case MessageTypeUtils.KA_QUAN_TONG_ZHI:
                    Intent kaquanIntent = new Intent(context, XiTongXiaoXiKaQuanTongZhiActivity.class);
                    kaquanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(kaquanIntent);
                    break;
                case MessageTypeUtils.GUAN_ZHU_DIAN_PU:
                    Intent dianpuIntent = new Intent(context, XiTongXiaoXiGuanZhuDianPuActivity.class);
                    dianpuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dianpuIntent);
                    break;
                case MessageTypeUtils.TOU_SU_FAN_KUI:
                    Intent touSuIntent = new Intent(context, XiTongXiaoXiTouSuFanKuiActivity.class);
                    touSuIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(touSuIntent);
                    break;
            }
        }
    }
}
