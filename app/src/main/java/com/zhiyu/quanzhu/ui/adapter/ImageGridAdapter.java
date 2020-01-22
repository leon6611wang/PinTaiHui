package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiGuanZhuImg;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {
    private Context context;
    private List<QuanZiGuanZhuImg> list=new ArrayList<>();
    private LinearLayout.LayoutParams ll;
    private int screenWidth,dp_15,dp_5,image_width;

    public ImageGridAdapter(Context context) {
        this.context = context;
        screenWidth= ScreentUtils.getInstance().getScreenWidth(context);
        dp_15=(int)context.getResources().getDimension(R.dimen.dp_15);
        dp_5=(int)context.getResources().getDimension(R.dimen.dp_5);
        image_width=(screenWidth-dp_15*2-dp_5*2)/3;
        ll=new LinearLayout.LayoutParams(image_width,image_width);
        ll.rightMargin=dp_5;
        ll.bottomMargin=dp_5;
//        list.add("https://c-ssl.duitang.com/uploads/item/201909/17/20190917110520_8FNyj.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201909/23/20190923184304_3krMj.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201907/18/20190718170010_sGEhK.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201909/23/20190923184307_yw5MX.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201812/13/20181213084735_yfplr.thumb.700_0.jpg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201910/26/20191026215816_EGRic.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201805/21/20180521143944_ezdGt.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201909/30/20190930181403_3ZeLe.thumb.700_0.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201910/29/20191029053629_tNAfy.thumb.700_0.jpeg");
    }

    public void setData(List<QuanZiGuanZhuImg> img_list){
        this.list=img_list;
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
        ImageView mImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview,null);
            holder.mImageView=convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.mImageView.setLayoutParams(ll);
        Glide.with(context).load(list.get(position).getThumb_file()).into(holder.mImageView);
        return convertView;
    }
}
