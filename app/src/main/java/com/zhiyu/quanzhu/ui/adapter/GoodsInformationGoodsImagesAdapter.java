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
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

/**
 * 商品详情-商品内容图片展示适配器
 */
public class GoodsInformationGoodsImagesAdapter extends BaseAdapter {
    private List<GoodsImg> list;
    private Context context;

    public void setList(List<GoodsImg> imgList) {
        this.list = imgList;
        notifyDataSetChanged();
    }

    public GoodsInformationGoodsImagesAdapter(Context context) {
        this.context = context;
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
        ImageView goodsimgImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodsinfo_goodsimgs, null);
            holder.goodsimgImageView = convertView.findViewById(R.id.goodsimgImageView);
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
