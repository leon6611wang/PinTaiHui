package com.zhiyu.quanzhu.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;

public class QuanZiTuiJianAdapter2 extends HeaderFooterRecyclerAdapter<String> {
    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_image_text, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, int RealPosition, String data) {
        if (holder instanceof QuanZiTuiJianAdapter2.MyHolder) {
            QuanZiTuiJianAdapter2.MyHolder myHolder = (QuanZiTuiJianAdapter2.MyHolder) holder;
        }

    }


    class MyHolder extends Holder {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
