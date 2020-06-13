package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.TouSuFanKui;
import com.zhiyu.quanzhu.ui.activity.ShenSuActivity;

import java.util.List;

public class XiTongXiaoXiTouSuFanKuiAdapter extends RecyclerView.Adapter<XiTongXiaoXiTouSuFanKuiAdapter.ViewHolder> {
    private List<TouSuFanKui> list;
    private Context context;

    public XiTongXiaoXiTouSuFanKuiAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<TouSuFanKui> touSuFanKuiList) {
        this.list = touSuFanKuiList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, titleTextView, notificationTimeTextView, targetTextView,
                reasonTextView, remarkTextView, chuliTextView;
        CardView mCardView;
        LinearLayout shensuLayout, duixianglayout, yuanyinlayout, beizhulayout, shensuyuanyinlayout;
        TextView duixiangtextview, yuanyintextview, beizhutextview, shensuyuanyintextview, suReasonTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            duixianglayout = itemView.findViewById(R.id.duixianglayout);
            yuanyinlayout = itemView.findViewById(R.id.yuanyinlayout);
            beizhulayout = itemView.findViewById(R.id.beizhulayout);
            shensuyuanyinlayout = itemView.findViewById(R.id.shensuyuanyinlayout);
            duixiangtextview = itemView.findViewById(R.id.duixiangtextview);
            yuanyintextview = itemView.findViewById(R.id.yuanyintextview);
            beizhutextview = itemView.findViewById(R.id.beizhutextview);
            shensuyuanyintextview = itemView.findViewById(R.id.shensuyuanyintextview);
            suReasonTextView = itemView.findViewById(R.id.suReasonTextView);
            mCardView = itemView.findViewById(R.id.mCardView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            targetTextView = itemView.findViewById(R.id.targetTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            remarkTextView = itemView.findViewById(R.id.remarkTextView);
            chuliTextView = itemView.findViewById(R.id.chuliTextView);
            notificationTimeTextView = itemView.findViewById(R.id.notificationTimeTextView);
            shensuLayout = itemView.findViewById(R.id.shensuLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_tou_su_fan_kui, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.titleTextView.setText(list.get(position).getAction_desc());
        holder.notificationTimeTextView.setText(list.get(position).getTime());
        holder.targetTextView.setText(list.get(position).getTarget());
        holder.reasonTextView.setText(list.get(position).getReason());
        holder.remarkTextView.setText(list.get(position).getRemark());
        holder.chuliTextView.setText("投诉处理：" + list.get(position).getResult_msg());
        holder.duixianglayout.setVisibility(View.GONE);
        holder.yuanyinlayout.setVisibility(View.GONE);
        holder.beizhulayout.setVisibility(View.GONE);
        holder.shensuyuanyinlayout.setVisibility(View.GONE);
        holder.shensuLayout.setVisibility(View.GONE);
        switch (list.get(position).getAction()) {
            case 1://1.投诉未通过通知
                holder.duixianglayout.setVisibility(View.VISIBLE);
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.duixiangtextview.setText("投诉对象：");
                holder.yuanyintextview.setText("投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                break;
            case 2://2.投诉受理通知
                holder.duixianglayout.setVisibility(View.VISIBLE);
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.duixiangtextview.setText("投诉对象：");
                holder.yuanyintextview.setText("投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                break;
            case 3://3.账号/店铺/圈子/动态被投诉
                holder.shensuLayout.setVisibility(View.VISIBLE);
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.yuanyintextview.setText("被投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                holder.shensuLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShenSuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",list.get(position).getMsg_id());
                        context.startActivity(intent);
                    }
                });
                break;
            case 4://4.发起申诉
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.shensuyuanyinlayout.setVisibility(View.VISIBLE);
                holder.yuanyintextview.setText("被投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                holder.shensuyuanyintextview.setText("申诉原因：");
                break;
            case 5://5.申诉通过
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.shensuyuanyinlayout.setVisibility(View.VISIBLE);
                holder.yuanyintextview.setText("被投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                holder.shensuyuanyintextview.setText("申诉原因：");
                break;
            case 6://6.申诉未通过
                holder.yuanyinlayout.setVisibility(View.VISIBLE);
                holder.beizhulayout.setVisibility(View.VISIBLE);
                holder.shensuyuanyinlayout.setVisibility(View.VISIBLE);
                holder.yuanyintextview.setText("被投诉原因：");
                holder.beizhutextview.setText("原因备注：");
                holder.shensuyuanyintextview.setText("申诉原因：");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
