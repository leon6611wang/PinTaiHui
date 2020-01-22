package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsFenLei;
import com.zhiyu.quanzhu.model.bean.ShangPinFenLei2;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;

import java.util.List;

public class QuanShangFenLeiRecyclerViewRightAdapter extends RecyclerView.Adapter<QuanShangFenLeiRecyclerViewRightAdapter.ViewHolder> {
    private Context context;
    private List<GoodsFenLei> list;
    private int dp_10;

    public QuanShangFenLeiRecyclerViewRightAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
    }

    public void setData(List<GoodsFenLei> shangPinFenLei2List) {
        this.list = shangPinFenLei2List;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        QuanShangFenLeiRecyclerViewRightItemRecyclerAdapter adapter;
        GridLayoutManager gridLayoutManager;
        GridSpacingItemDecoration gridSpacingItemDecoration;
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            adapter = new QuanShangFenLeiRecyclerViewRightItemRecyclerAdapter(context);
            gridLayoutManager = new GridLayoutManager(context, 3);
            gridSpacingItemDecoration = new GridSpacingItemDecoration(3, dp_10, true);
            mRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanshang_fenlei_right, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mRecyclerView.setLayoutManager(holder.gridLayoutManager);
        holder.nameTextView.setText(list.get(position).getName());
        holder.adapter.setData(list.get(position).getChild());
//        if (!list.get(position).isDecoration()) {
//            list.get(position).setDecoration(true);
//            holder.mRecyclerView.addItemDecoration(holder.gridSpacingItemDecoration);
//        }
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }
}
