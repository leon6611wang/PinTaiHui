package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RenMaiRecyclerAdapter extends BaseRecyclerAdapter<Integer> {
    private Context context;

    public RenMaiRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MyRecyclerView mRecyclerView;
        TypeRecyclerAdapter adapter;
        private List<String> list = new ArrayList<>();
        private LinearLayoutManager ms;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
            list.add("马鞍山");
            list.add("IT圈");
            list.add("生活");
            list.add("美食");
            list.add("游戏");
            list.add("汽车");
            list.add("读书");
            ms = new LinearLayoutManager(context);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter = new TypeRecyclerAdapter(context);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Integer data) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mRecyclerView.setLayoutManager(holder.ms);
            holder.mRecyclerView.setAdapter(holder.adapter);
            holder.adapter.setData(holder.list);
        }
    }


}
