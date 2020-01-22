package com.zhiyu.quanzhu.ui.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShouZhi;

public class QianBaoRecyclerAdapter extends BaseRecyclerAdapter<Integer> {
    private int viewpagerheight;
    private LinearLayout.LayoutParams ll;
    public void setViewpagerHeight(int height){
        this.viewpagerheight=height;
        ll=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,viewpagerheight);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qianbao_recycler, null));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Integer data) {
        if(viewpagerheight>0){
           if(viewHolder instanceof MyViewHolder){
               MyViewHolder myViewHolder=(MyViewHolder)viewHolder;
               myViewHolder.mViewPager.setLayoutParams(ll);
           }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ViewPager mViewPager;
        public MyViewHolder(View itemView) {
            super(itemView);
            mViewPager=itemView.findViewById(R.id.mViewPager);
        }
    }
}
