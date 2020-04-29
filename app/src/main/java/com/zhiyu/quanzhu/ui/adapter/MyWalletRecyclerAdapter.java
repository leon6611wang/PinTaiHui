package com.zhiyu.quanzhu.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyWalletAll;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyWalletExpenses;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyWalletIncome;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MyWalletRecyclerAdapter extends BaseRecyclerAdapter<Integer> {
    private FragmentManager fragmentManager;
    private int viewpagerheight;
    private LinearLayout.LayoutParams ll;

    public MyWalletRecyclerAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setViewpagerHeight(int height) {
        this.viewpagerheight = height;
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewpagerheight);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_wallet_recycler, null));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Integer data) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.mViewPager.setCurrentItem(currentIndex);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        NoScrollViewPager mViewPager;
        List<Fragment> fragmentList;
        ViewPagerAdapter adapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            mViewPager = itemView.findViewById(R.id.mViewPager);
            fragmentList = new ArrayList<>();
            fragmentList.add(new FragmentMyWalletAll());
            fragmentList.add(new FragmentMyWalletIncome());
            fragmentList.add(new FragmentMyWalletExpenses());
            adapter = new ViewPagerAdapter(fragmentManager, fragmentList);
            mViewPager.setAdapter(adapter);
            mViewPager.setLayoutParams(ll);
        }
    }

    private int currentIndex = 0;

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        notifyDataSetChanged();
    }
}
