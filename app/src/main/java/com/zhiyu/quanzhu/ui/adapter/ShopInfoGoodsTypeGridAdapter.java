package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopInfoGoodsType;
import com.zhiyu.quanzhu.ui.activity.GoodsSearchActivity;

import java.util.List;

public class ShopInfoGoodsTypeGridAdapter extends BaseAdapter {
    private List<ShopInfoGoodsType> list;
    private Context context;

    public ShopInfoGoodsTypeGridAdapter(Context context) {
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
        TextView goodsTypeTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_info_goods_type_gridview, null);
            holder.goodsTypeTextView = convertView.findViewById(R.id.goodsTypeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodsTypeTextView.setText(list.get(position).getName());
        if (list.get(position).isSelected()) {
            holder.goodsTypeTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
            holder.goodsTypeTextView.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
        } else {
            holder.goodsTypeTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
            holder.goodsTypeTextView.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        }
        holder.goodsTypeTextView.setOnClickListener(new OnGoodsSearchClick(position));
        return convertView;
    }

    private class OnGoodsSearchClick implements View.OnClickListener {
        private int position;

        public OnGoodsSearchClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onGoodsTypeSelectListener) {
                onGoodsTypeSelectListener.onGoodsTypeSelectListener(list.get(position));
            }
            list.get(position).setSelected(true);
            notifyDataSetChanged();
//
//            Intent goodsSearchIntent = new Intent(context, GoodsSearchActivity.class);
//            goodsSearchIntent.putExtra("keyword", list.get(position).getName());
//            goodsSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(goodsSearchIntent);
        }
    }

    private static OnGoodsTypeSelectListener onGoodsTypeSelectListener;

    public static void setOnGoodsTypeSelectListener(OnGoodsTypeSelectListener listener) {
        onGoodsTypeSelectListener = listener;
    }

    public interface OnGoodsTypeSelectListener {
        void onGoodsTypeSelectListener(ShopInfoGoodsType goodsType);
    }
}
