package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Store;
import com.zhiyu.quanzhu.ui.widget.MaxRecyclerView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MingPianDianPuRecyclerAdapter extends RecyclerView.Adapter<MingPianDianPuRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_10, dp_165;
    private List<Store> list;

    public void setList(List<Store> storeList) {
        this.list = storeList;
        notifyDataSetChanged();
    }

    public MingPianDianPuRecyclerAdapter(Context context) {
        this.context = context;
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_5);
        dp_165 = (int) context.getResources().getDimension(R.dimen.dp_165);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaxRecyclerView dianpuLabelRecyclerView;
        TypeRecyclerAdapter adapter;
        MaxRecyclerView dianpushangpinRecyclerView;
        MingPianDianPuShangPinRecyclerAdapter dianPuShangPinRecyclerAdapter;
        SpaceItemDecoration spaceItemDecoration;
        LinearLayoutManager dianpushangpinManager, dianpushangpinindexManager;
        MaxRecyclerView dianpushangpinIndexRecyclerView;
        MingPianDianPuShangPinIndexAdapter dianPuShangPinIndexAdapter;
        private List<String> list = new ArrayList<>();
        private LinearLayoutManager ms;
        private RoundImageView iconImageView;
        private TextView nameTextView, sourceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            dianpuLabelRecyclerView = itemView.findViewById(R.id.dianpuLabelRecyclerView);
            dianpushangpinRecyclerView = itemView.findViewById(R.id.dianpushangpinRecyclerView);
            LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
            mLinearSnapHelper.attachToRecyclerView(dianpushangpinRecyclerView);
//            list.add("马鞍山");
//            list.add("IT圈");
            ms = new LinearLayoutManager(context);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            adapter = new TypeRecyclerAdapter(context);
            dianPuShangPinRecyclerAdapter = new MingPianDianPuShangPinRecyclerAdapter(context);
            dianpushangpinManager = new LinearLayoutManager(context);
            spaceItemDecoration = new SpaceItemDecoration(dp_10);
            dianpushangpinManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            dianpushangpinIndexRecyclerView = itemView.findViewById(R.id.dianpushangpinIndexRecyclerView);
            dianPuShangPinIndexAdapter = new MingPianDianPuShangPinIndexAdapter(context);
            dianpushangpinindexManager = new LinearLayoutManager(context);
            dianpushangpinindexManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            dianpuLabelRecyclerView.setLayoutManager(ms);
            dianpuLabelRecyclerView.setAdapter(adapter);
            dianpushangpinRecyclerView.setLayoutManager(dianpushangpinManager);
            dianpushangpinRecyclerView.setAdapter(dianPuShangPinRecyclerAdapter);
            dianpushangpinIndexRecyclerView.setLayoutManager(dianpushangpinindexManager);
            dianpushangpinIndexRecyclerView.setAdapter(dianPuShangPinIndexAdapter);
            dianPuShangPinIndexAdapter.setCurrentIndex(0);
            final MingPianDianPuShangPinIndexAdapter adapter = dianPuShangPinIndexAdapter;
            dianpushangpinRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    totalDx += dx;
                    int page = (totalDx / dp_165) / 2;
                    adapter.setCurrentIndex(page);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian_dianpu, parent, false));
    }

    private int totalDx;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.list.size() == 0) {
            holder.list.add(list.get(position).getCity_name());
            holder.list.add(list.get(position).getShop_type_name());
            holder.adapter.setData(holder.list);
        }
        if (holder.dianPuShangPinIndexAdapter.getItemCount() == 0) {
            holder.dianPuShangPinIndexAdapter.setIndexSize(list.get(position).getGoods_list().size()/2);
        }
        Glide.with(context).load(list.get(position).getIcon()).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getName());
        holder.sourceTextView.setText(list.get(position).getMark());
        holder.dianPuShangPinRecyclerAdapter.setList(list.get(position).getGoods_list());
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
