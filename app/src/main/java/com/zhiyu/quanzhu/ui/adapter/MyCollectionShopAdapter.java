package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.bean.FullSearchShop;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyCollectionShopAdapter extends BaseAdapter {
    private List<FullSearchShop> list;
    private Context context;
    private boolean isSelectModel;

    public void setAllSelect(boolean isAllSelected) {
        for (FullSearchShop feed : list) {
            feed.setSelected(isAllSelected);
        }
        notifyDataSetChanged();
    }

    public void setSelectModel(boolean isSelected) {
        this.isSelectModel = isSelected;
        notifyDataSetChanged();
    }

    public String getCancelCollectIds() {
        String ids = "";
        for (FullSearchShop feed : list) {
            if (feed.isSelected()) {
                ids += feed.getId() + ",";
            }
        }
        return ids;
    }

    public void cancelCollectSuccess() {
        List<Integer> positionList = new ArrayList<>();
        for (int i = list.size()-1; i >0; i--) {
            if (list.get(i).isSelected()) {
                positionList.add(i);
            }
        }
        for (Integer position : positionList) {
            list.remove((int)position);
        }
        notifyDataSetChanged();
    }

    public MyCollectionShopAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<FullSearchShop> shopList) {
        this.list = shopList;
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
        HorizontalListView goodsListView;
        MyCollectionShopGoodsAdapter adapter;
        NiceImageView coverImageView;
        TextView nameTextView, cityTextView, industryTextView, markTextView;
        LinearLayout enterShopLayout;
        RelativeLayout selectLayout;
        ImageView selectImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection_shop, null);
            holder.goodsListView = convertView.findViewById(R.id.goodsListView);
            holder.adapter = new MyCollectionShopGoodsAdapter(parent.getContext());
            holder.goodsListView.setAdapter(holder.adapter);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.enterShopLayout = convertView.findViewById(R.id.enterShopLayout);
            holder.cityTextView = convertView.findViewById(R.id.cityTextView);
            holder.industryTextView = convertView.findViewById(R.id.industryTextView);
            holder.markTextView = convertView.findViewById(R.id.markTextView);
            holder.selectLayout = convertView.findViewById(R.id.selectLayout);
            holder.selectImageView = convertView.findViewById(R.id.selectImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getIcon()).into(holder.coverImageView);
        holder.nameTextView.setText(list.get(position).getName());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.VISIBLE);
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getShop_type_name())) {
            holder.industryTextView.setVisibility(View.VISIBLE);
        } else {
            holder.industryTextView.setVisibility(View.GONE);
        }
        holder.cityTextView.setText(list.get(position).getCity_name());
        holder.industryTextView.setText(list.get(position).getShop_type_name());
        holder.markTextView.setText(String.valueOf(list.get(position).getMark()));
        holder.enterShopLayout.setOnClickListener(new OnShopInfoClick(position));
        if (null != list.get(position).getGoods() && list.get(position).getGoods().size() > 0) {
            holder.goodsListView.setVisibility(View.VISIBLE);
        } else {
            holder.goodsListView.setVisibility(View.GONE);
        }
        holder.adapter.setList(list.get(position).getGoods());
        if (isSelectModel) {
            holder.selectLayout.setVisibility(View.VISIBLE);
            if (list.get(position).isSelected()) {
                holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_selected));
            } else {
                holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.gouwuche_unselect));
            }
        } else {
            holder.selectLayout.setVisibility(View.GONE);
        }
        holder.selectLayout.setOnClickListener(new OnSelectClick(position));
        return convertView;
    }

    private class OnShopInfoClick implements View.OnClickListener {
        private int position;

        public OnShopInfoClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent shopInfoIntent = new Intent(context, ShopInformationActivity.class);
            shopInfoIntent.putExtra("shop_id", String.valueOf(list.get(position).getId()));
            shopInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(shopInfoIntent);
        }
    }

    private class OnSelectClick implements View.OnClickListener {
        private int position;

        public OnSelectClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelected(!list.get(position).isSelected());
            notifyDataSetChanged();
        }
    }

}
