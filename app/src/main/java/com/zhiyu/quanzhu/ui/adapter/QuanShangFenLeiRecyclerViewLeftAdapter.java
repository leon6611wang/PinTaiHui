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
import com.zhiyu.quanzhu.model.bean.GoodsFenLei;
import com.zhiyu.quanzhu.model.bean.ShangPinFenLei1;

import java.util.List;

public class QuanShangFenLeiRecyclerViewLeftAdapter extends RecyclerView.Adapter<QuanShangFenLeiRecyclerViewLeftAdapter.ViewHolder> {
    private List<GoodsFenLei> list;
    private Context context;

    public QuanShangFenLeiRecyclerViewLeftAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GoodsFenLei> fenLei1List) {
        this.list = fenLei1List;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        LinearLayout rootlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rootlayout = itemView.findViewById(R.id.rootlayout);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanshang_fenlei_left, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTextView.setText(list.get(position).getName());
        holder.rootlayout.setOnClickListener(new OnRootLayoutClick(position));
        if (list.get(position).isSelected()) {
            holder.rootlayout.setBackgroundColor(context.getResources().getColor(R.color.color_f8));
            holder.nameTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
            holder.nameTextView.getPaint().setFakeBoldText(true);
        } else {
            holder.rootlayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.nameTextView.setTextColor(context.getResources().getColor(R.color.text_color_black));
            holder.nameTextView.getPaint().setFakeBoldText(false);
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    class OnRootLayoutClick implements View.OnClickListener {
        private int position;

        public OnRootLayoutClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (GoodsFenLei fenlei : list) {
                fenlei.setSelected(false);
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
            if(null!=onLeftClickListener){
                onLeftClickListener.onLeftClick(position);
            }
        }
    }
    private OnLeftClickListener onLeftClickListener;
    public void setOnLeftClickListener(OnLeftClickListener listener){
        this.onLeftClickListener=listener;
    }
    public interface OnLeftClickListener{
        void onLeftClick(int position);
    }
}
