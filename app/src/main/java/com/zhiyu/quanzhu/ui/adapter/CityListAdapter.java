package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.City;
import com.zhiyu.quanzhu.ui.widget.MyGridView;

import java.util.List;
import java.util.Map;

public class CityListAdapter extends BaseAdapter {
    private List<Map<String,List<City>>> list;

    public void setData(List<Map<String,List<City>>> list_map){
        this.list=list_map;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return null==list?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        private TextView ziMuTextView;
        private MyGridView cityListGridView;
        private  CityListGridAdapter cityListGridAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_listview,null);
            holder.ziMuTextView=convertView.findViewById(R.id.ziMuTextView);
            holder.cityListGridView=convertView.findViewById(R.id.cityListGridView);
            holder.cityListGridAdapter=new CityListGridAdapter();
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        String ziMu=null;
        List<City> cityList=null;
        for(String key : list.get(position).keySet()){
            ziMu=key;
            cityList = list.get(position).get(key);
        }
        holder.ziMuTextView.setText(ziMu);
        holder.cityListGridAdapter.setData(cityList);
        holder.cityListGridView.setAdapter(holder.cityListGridAdapter);

        return convertView;
    }
}
