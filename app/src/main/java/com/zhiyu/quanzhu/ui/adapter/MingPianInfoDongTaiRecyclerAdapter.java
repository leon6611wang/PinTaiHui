package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.util.ArrayList;

public class MingPianInfoDongTaiRecyclerAdapter extends RecyclerView.Adapter<MingPianInfoDongTaiRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_6;
    private ArrayList<String> list = new ArrayList<>();

    public MingPianInfoDongTaiRecyclerAdapter(Context context) {
        this.context = context;
        dp_6 = (int) context.getResources().getDimension(R.dimen.dp_6);
        list.add("https://c-ssl.duitang.com/uploads/item/201808/08/20180808191035_xAutH.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201406/07/20140607111821_jYTT8.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201803/25/20180325155250_iqich.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201208/26/20120826234112_3ZXBG.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201704/20/20170420091511_VjR3y.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201808/10/20180810163151_hswik.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201503/22/20150322211819_KsHQd.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201209/08/20120908085415_HJAGk.thumb.700_0.jpeg");
        list.add("https://c-ssl.duitang.com/uploads/item/201203/18/20120318111605_EWkce.thumb.700_0.jpeg");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView dongtaiImagesRecyclerView, dongtaiLabelRecyclerView;
        MingPianDongTaiImageRecyclerAdapter imageAdapter;
        MingPianDongTaiLabelRecyclerAdapter labelAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            dongtaiImagesRecyclerView = itemView.findViewById(R.id.dongtaiImagesRecyclerView);
            imageAdapter = new MingPianDongTaiImageRecyclerAdapter(context);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            gridLayoutManager.setInitialPrefetchItemCount(9);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(3, dp_6, false);
            dongtaiImagesRecyclerView.setAdapter(imageAdapter);
            dongtaiImagesRecyclerView.addItemDecoration(gridSpacingItemDecoration);
            dongtaiImagesRecyclerView.setLayoutManager(gridLayoutManager);
            dongtaiImagesRecyclerView.setItemViewCacheSize(200);
            dongtaiImagesRecyclerView.setHasFixedSize(true);
            dongtaiImagesRecyclerView.setNestedScrollingEnabled(false);

            dongtaiLabelRecyclerView = itemView.findViewById(R.id.dongtaiLabelRecyclerView);
            labelAdapter = new MingPianDongTaiLabelRecyclerAdapter();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            SpaceItemDecoration decoration = new SpaceItemDecoration((int) context.getResources().getDimension(R.dimen.dp_10));
            dongtaiLabelRecyclerView.setAdapter(labelAdapter);
            dongtaiLabelRecyclerView.setLayoutManager(linearLayoutManager);
//            dongtaiLabelRecyclerView.addItemDecoration(decoration);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mingpian_info_dongtai, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageAdapter.setList(list);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
