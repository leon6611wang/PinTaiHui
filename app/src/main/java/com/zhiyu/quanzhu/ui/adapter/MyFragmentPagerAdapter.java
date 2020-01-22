package com.zhiyu.quanzhu.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tab_title_list;//存放标签页标题
    private ArrayList<Fragment> fragment_list;//存放ViewPager下的Fragment

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<String> tab_title_list, ArrayList<Fragment> fragment_list) {
        super(fm);
        this.tab_title_list = tab_title_list;
        this.fragment_list = fragment_list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title_list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
