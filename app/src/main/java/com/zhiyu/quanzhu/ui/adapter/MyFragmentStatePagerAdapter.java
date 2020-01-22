package com.zhiyu.quanzhu.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;

    public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.list = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }
}
