package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.leon.chic.model.City;
import com.zhiyu.quanzhu.ui.widget.MyGridView;

import java.util.List;
import java.util.Map;

public class CityListAdapter extends BaseAdapter implements CityListGridAdapter.OnGridSelectedCityListener {
    private List<Map<String, List<City>>> list;

    public void setData(List<Map<String, List<City>>> list_map) {
        this.list = list_map;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private TextView ziMuTextView;
        private MyGridView cityListGridView;
        private CityListGridAdapter cityListGridAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_listview, null);
            holder.ziMuTextView = convertView.findViewById(R.id.ziMuTextView);
            holder.cityListGridView = convertView.findViewById(R.id.cityListGridView);
            holder.cityListGridAdapter = new CityListGridAdapter();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String ziMu = null;
        List<City> cityList = null;
        for (String key : list.get(position).keySet()) {
            ziMu = key;
            cityList = list.get(position).get(key);
        }
        holder.ziMuTextView.setText(ziMu);
        holder.cityListGridAdapter.setData(cityList);
        holder.cityListGridView.setAdapter(holder.cityListGridAdapter);
        holder.cityListGridAdapter.setOnGridSelectedCityListener(this);
        return convertView;
    }

    @Override
    public void onGridSelectdCity(String city, int childPosition) {
        for (int i = 0; i < list.size(); i++) {
            for (String key : list.get(i).keySet()) {
                for (int j = 0; j < list.get(i).get(key).size(); j++) {
                    list.get(i).get(key).get(j).setSelected(false);
                    if (list.get(i).get(key).get(j).getName().equals(city)) {
                        list.get(i).get(key).get(j).setSelected(true);
                    }
                }
            }
        }
        notifyDataSetChanged();
        if (null != onSelectCityListener) {
            onSelectCityListener.onSelectCity(city);
        }
    }

    private OnSelectCityListener onSelectCityListener;

    public void setOnSelectCityListener(OnSelectCityListener listener) {
        this.onSelectCityListener = listener;
    }

    public interface OnSelectCityListener {
        void onSelectCity(String city);
    }
}
