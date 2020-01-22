package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.fragment.FragmentShangQuanShangPin;

import java.util.ArrayList;
import java.util.List;

public class ShangQuanShangPinListAdapter extends RecyclerView.Adapter<ShangQuanShangPinListAdapter.ViewHolder> {
    private FragmentManager fragmentManager;
    private List<String> list = new ArrayList<>();
    private Context context;
    public void initData() {
        list.add("http://img.duoziwang.com/2016/11/25/05162874655.jpg");
        list.add("http://img.duoziwang.com/2016/11/27/190923198669.jpg");
        list.add("http://img.duoziwang.com/2016/11/25/00424044710.jpg");
        list.add("http://img.duoziwang.com/uploads/c160417/1460V0A5320-15533.jpg");
        list.add("http://img.duoziwang.com/2019/02/04232036663888.jpg");
        list.add("http://img.duoziwang.com/2019/02/04232036663762.jpg");
        list.add("http://img.duoziwang.com/2019/02/04232036663589.jpg");
        list.add("http://img.duoziwang.com/2018/18/06132214114261.jpg");
        list.add("http://img.duoziwang.com/2018/18/06132214117188.jpg");
        list.add("http://img.duoziwang.com/2018/18/06132214109383.jpg");
        list.add("http://img.duoziwang.com/2018/18/06191020109005.jpg");
        list.add("http://img.duoziwang.com/2018/18/06132214102781.jpg");
        list.add("http://img.duoziwang.com/2018/18/06032028724738.jpg");
        list.add("http://img.duoziwang.com/2018/18/06032028724654.jpg");
        list.add("http://img.duoziwang.com/2018/18/06032028724063.jpg");
    }

    public void setFragmentManager(Context ctxt,FragmentManager fm) {
        this.context=ctxt;
        this.fragmentManager = fm;
        initData();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shangquan_shangpin_list,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.headerImageView);
        holder.shangpinViewPager.setId(position + getItemCount());
        holder. fragmentList=new ArrayList<>();
        holder.f1=new FragmentShangQuanShangPin();
        holder.f2=new FragmentShangQuanShangPin();
        holder.f3=new FragmentShangQuanShangPin();
        holder. fragmentList.add(holder.f1);
        holder.fragmentList.add(holder.f2);
        holder.fragmentList.add(holder.f3);
        holder.adapter=new ViewPagerAdapter(fragmentManager,holder.fragmentList);
        holder.shangpinViewPager.setAdapter(holder.adapter);
    }

    @Override
    public int getItemCount() {
        return null==list?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentPage = 0;
        private ImageView headerImageView;
        private  ViewPager shangpinViewPager;
        private ViewPagerAdapter adapter;
        private  List<Fragment> fragmentList;
        private  FragmentShangQuanShangPin f1;
        private  FragmentShangQuanShangPin f2;
        private  FragmentShangQuanShangPin f3;
        public ViewHolder(View itemView) {
            super(itemView);
            shangpinViewPager = itemView.findViewById(R.id.shangpinViewPager);
            headerImageView=itemView.findViewById(R.id.headerImageView);
        }
    }


}
