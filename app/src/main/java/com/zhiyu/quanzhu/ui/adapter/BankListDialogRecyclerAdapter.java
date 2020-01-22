package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Bank;

import java.util.List;

public class BankListDialogRecyclerAdapter extends RecyclerView.Adapter<BankListDialogRecyclerAdapter.ViewHolder> {
    private List<Bank> list;
    private Context context;

    public BankListDialogRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Bank> bankList) {
        this.list = bankList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView banknametextview;
        View selectview;

        public ViewHolder(View itemView) {
            super(itemView);
            banknametextview = itemView.findViewById(R.id.banknametextview);
            selectview = itemView.findViewById(R.id.selectview);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banklist_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.banknametextview.setText(list.get(position).getBankName());
        holder.itemView.setOnClickListener(new OnBankClick(position));
        if (list.get(position).isSelected()) {
            holder.selectview.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
        } else {
            holder.selectview.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        }
    }

    private class OnBankClick implements View.OnClickListener {
        private int position;

        public OnBankClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (Bank bank : list) {
                bank.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
            if (null != onBankChooseListener) {
                onBankChooseListener.onBankChoose(list.get(position));
            }
        }
    }

    private OnBankChooseListener onBankChooseListener;

    public void setOnBankChooseListener(OnBankChooseListener listener) {
        this.onBankChooseListener = listener;
    }

    public interface OnBankChooseListener {
        void onBankChoose(Bank bank);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
