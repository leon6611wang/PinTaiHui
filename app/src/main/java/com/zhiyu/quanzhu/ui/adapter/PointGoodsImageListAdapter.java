package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsImg;

import java.util.List;

public class PointGoodsImageListAdapter extends BaseAdapter {
    private List<GoodsImg> list;
    private Context context;

    public void setList(List<GoodsImg> imgList) {
        this.list = imgList;
        notifyDataSetChanged();
    }

    public PointGoodsImageListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return null==list?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView goodsimgImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goodsinfo_goodsimgs, null);
            holder.goodsimgImageView=convertView.findViewById(R.id.goodsimgImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.goodsimgImageView.setLayoutParams(list.get(position).getImageParams(context));
        Glide.with(context).load(list.get(position).getUrl()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.goodsimgImageView);
        return convertView;
    }
}
