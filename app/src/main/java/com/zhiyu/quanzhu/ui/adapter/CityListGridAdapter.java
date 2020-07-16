package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.leon.chic.model.City;

import java.util.List;

public class CityListGridAdapter extends BaseAdapter {
    private List<City> list;

    public void setData(List<City> l) {
        this.list = l;
        notifyDataSetChanged();
    }

    public void setCitySelected(int position) {

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
        private TextView cityTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_grid, null);
            holder.cityTextView = convertView.findViewById(R.id.cityTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cityTextView.setText(list.get(position).getName());
        if (list.get(position).isSelected()) {
            holder.cityTextView.setBackground(parent.getResources().getDrawable(R.drawable.shape_city_selected));
            holder.cityTextView.setTextColor(parent.getResources().getColor(R.color.text_color_yellow));
        } else {
            holder.cityTextView.setBackground(parent.getResources().getDrawable(R.drawable.shape_city));
            holder.cityTextView.setTextColor(parent.getResources().getColor(R.color.text_color_gray));
        }
        holder.cityTextView.setOnClickListener(new OnCitySelectListener(position));
        return convertView;
    }

    private class OnCitySelectListener implements View.OnClickListener {
        private int position;

        public OnCitySelectListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onGridSelectedCityListener) {
                onGridSelectedCityListener.onGridSelectdCity(list.get(position).getName(), position);
            }
        }
    }

    private OnGridSelectedCityListener onGridSelectedCityListener;

    public void setOnGridSelectedCityListener(OnGridSelectedCityListener listener) {
        this.onGridSelectedCityListener = listener;
    }

    public interface OnGridSelectedCityListener {
        void onGridSelectdCity(String city, int childPosition);
    }
}
