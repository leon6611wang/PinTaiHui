package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ZhiFuTongZhi;
import com.zhiyu.quanzhu.utils.PriceParseUtils;

import java.util.List;

public class XiTongXiaoXiZhiFuTongZhiAdapter extends RecyclerView.Adapter<XiTongXiaoXiZhiFuTongZhiAdapter.ViewHolder>{
    private List<ZhiFuTongZhi> list;
    public void setList(List<ZhiFuTongZhi> zhiFuTongZhiList){
        this.list=zhiFuTongZhiList;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeTextView,payTimeTextView,priceTextView,
                nameTextView,goodsTextView,payWayTextView,
                notificationTimeTextView,orderTextView,titleTextView,
                moneyTitleTextView,tuikuanTextView,
                applyTimeTextView,fuwufeiTextView,tixianfangshiTextView,
                tixiandanhaoTextView,yujidaozhangTextView;
        CardView mCardView;
        LinearLayout shoukuanfanglayout,goodslayout,paywaylayout,
                paytimelayout,orderlayout,tuikuanlayout,shenqingshijianlayout,
                fuwufeilayout,tixianfangshilayout,tixiandanhaolayout,
                yujidaozhanglayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mCardView=itemView.findViewById(R.id.mCardView);
            timeTextView=itemView.findViewById(R.id.timeTextView);
            payTimeTextView=itemView.findViewById(R.id.payTimeTextView);
            priceTextView=itemView.findViewById(R.id.priceTextView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            goodsTextView=itemView.findViewById(R.id.goodsTextView);
            payWayTextView=itemView.findViewById(R.id.payWayTextView);
            notificationTimeTextView=itemView.findViewById(R.id.notificationTimeTextView);
            orderTextView=itemView.findViewById(R.id.orderTextView);
            titleTextView=itemView.findViewById(R.id.titleTextView);
            moneyTitleTextView=itemView.findViewById(R.id.moneyTitleTextView);
            tuikuanTextView=itemView.findViewById(R.id.tuikuanTextView);
            shoukuanfanglayout=itemView.findViewById(R.id.shoukuanfanglayout);
            goodslayout=itemView.findViewById(R.id.goodslayout);
            paywaylayout=itemView.findViewById(R.id.paywaylayout);
            paytimelayout=itemView.findViewById(R.id.paytimelayout);
            orderlayout=itemView.findViewById(R.id.orderlayout);
            tuikuanlayout=itemView.findViewById(R.id.tuikuanlayout);
            shenqingshijianlayout=itemView.findViewById(R.id.shenqingshijianlayout);
            applyTimeTextView= itemView.findViewById(R.id.applyTimeTextView);
            fuwufeilayout=itemView.findViewById(R.id.fuwufeilayout);
            fuwufeiTextView=itemView.findViewById(R.id.fuwufeiTextView);
            tixianfangshilayout=itemView.findViewById(R.id.tixianfangshilayout);
            tixianfangshiTextView=itemView.findViewById(R.id.tixianfangshiTextView);
            tixiandanhaolayout=itemView.findViewById(R.id.tixiandanhaolayout);
            tixiandanhaoTextView=itemView.findViewById(R.id.tixiandanhaoTextView);
            yujidaozhanglayout=itemView.findViewById(R.id.yujidaozhanglayout);
            yujidaozhangTextView=itemView.findViewById(R.id.yujidaozhangTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xitongxiaoxi_zhi_fu_tong_zhi,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeTextView.setText(list.get(position).getAdd_time());
        holder.titleTextView.setText(list.get(position).getAction_desc());
        holder.notificationTimeTextView.setText(list.get(position).getTime());
        holder.moneyTitleTextView.setText(list.get(position).getMoney_title());
        holder.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(list.get(position).getMoney()));
        holder.nameTextView.setText(list.get(position).getAccount());
        holder.goodsTextView.setText(list.get(position).getGoods());
        holder.payWayTextView.setText(list.get(position).getType());
        holder.payTimeTextView.setText(list.get(position).getPay_time());
        holder.orderTextView.setText(list.get(position).getOrder_sn());
        holder.tuikuanTextView.setText(list.get(position).getResult_msg());
        holder.applyTimeTextView.setText(list.get(position).getPay_time());
        holder.yujidaozhangTextView.setText(list.get(position).getResult_msg());
        holder.tixianfangshiTextView.setText(list.get(position).getType());
        holder.tixiandanhaoTextView.setText(list.get(position).getCash_time());
        holder.fuwufeiTextView.setText("¥"+PriceParseUtils.getInstance().parsePrice(list.get(position).getServermoney()));
        switch (list.get(position).getAction()){
            case 1:
                holder.shoukuanfanglayout.setVisibility(View.VISIBLE);
                holder.goodslayout.setVisibility(View.VISIBLE);
                holder.paywaylayout.setVisibility(View.VISIBLE);
                holder.paytimelayout.setVisibility(View.VISIBLE);
                holder.orderlayout.setVisibility(View.VISIBLE);
                holder.tuikuanlayout.setVisibility(View.GONE);
                holder.shenqingshijianlayout.setVisibility(View.GONE);
                holder.fuwufeilayout.setVisibility(View.GONE);
                holder.tixianfangshilayout.setVisibility(View.GONE);
                holder.tixiandanhaolayout.setVisibility(View.GONE);
                holder.yujidaozhanglayout.setVisibility(View.GONE);
                break;
            case 2:
                holder.shoukuanfanglayout.setVisibility(View.VISIBLE);
                holder.goodslayout.setVisibility(View.VISIBLE);
                holder.paywaylayout.setVisibility(View.VISIBLE);
                holder.paytimelayout.setVisibility(View.VISIBLE);
                holder.orderlayout.setVisibility(View.VISIBLE);
                holder.tuikuanlayout.setVisibility(View.VISIBLE);
                holder.shenqingshijianlayout.setVisibility(View.GONE);
                holder.fuwufeilayout.setVisibility(View.GONE);
                holder.tixianfangshilayout.setVisibility(View.GONE);
                holder.tixiandanhaolayout.setVisibility(View.GONE);
                holder.yujidaozhanglayout.setVisibility(View.GONE);
                break;
            case 3:
                holder.shoukuanfanglayout.setVisibility(View.GONE);
                holder.goodslayout.setVisibility(View.GONE);
                holder.paywaylayout.setVisibility(View.GONE);
                holder.paytimelayout.setVisibility(View.GONE);
                holder.tuikuanlayout.setVisibility(View.GONE);
                holder.shenqingshijianlayout.setVisibility(View.VISIBLE);
                holder.fuwufeilayout.setVisibility(View.VISIBLE);
                holder.tixianfangshilayout.setVisibility(View.VISIBLE);
                holder.tixiandanhaolayout.setVisibility(View.VISIBLE);
                holder.orderlayout.setVisibility(View.VISIBLE);
                holder.yujidaozhanglayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
