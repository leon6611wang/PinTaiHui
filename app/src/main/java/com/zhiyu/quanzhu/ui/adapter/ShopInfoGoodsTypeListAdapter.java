package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopInfoGoodsType;
import com.zhiyu.quanzhu.ui.widget.MyGridView;

import java.util.List;

public class ShopInfoGoodsTypeListAdapter extends BaseAdapter {
    private List<ShopInfoGoodsType> list;
    private Context context;

    public ShopInfoGoodsTypeListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ShopInfoGoodsType> goodsTypeList) {
        this.list = goodsTypeList;
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
        TextView typeNameTextView;
        MyGridView mGridView;
        ShopInfoGoodsTypeGridAdapter adapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_info_goods_type_listview, null);
            holder.typeNameTextView = convertView.findViewById(R.id.typeNameTextView);
            holder.mGridView = convertView.findViewById(R.id.mGridView);
            holder.adapter = new ShopInfoGoodsTypeGridAdapter(context);
            holder.mGridView.setAdapter(holder.adapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeNameTextView.setText(list.get(position).getName());
        holder.adapter.setList(list.get(position).getChild());
        return convertView;
    }
}
