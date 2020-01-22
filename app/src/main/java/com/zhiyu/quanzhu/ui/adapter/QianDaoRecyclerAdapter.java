package com.zhiyu.quanzhu.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QianDao;


public class QianDaoRecyclerAdapter extends BaseRecyclerAdapter<QianDao> {

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }




    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qiandao, null));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, QianDao data) {

    }
}
