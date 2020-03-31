package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CircleInfoFeedImg;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class CircleInfoFeedImagesGridAdapter extends BaseAdapter {
    private List<CircleInfoFeedImg> list;
    private Context context;
    private int screenWidth,dp_15,dp_5,width;
    private RelativeLayout.LayoutParams params;
    public CircleInfoFeedImagesGridAdapter(Context context) {
        this.context=context;
        list=new ArrayList<>();
        screenWidth= ScreentUtils.getInstance().getScreenWidth(context);
        dp_5=(int)context.getResources().getDimension(R.dimen.dp_5);
        dp_15=(int)context.getResources().getDimension(R.dimen.dp_15);
        width=Math.round((screenWidth-dp_15*2-dp_5*2)/3);
        params=new RelativeLayout.LayoutParams(width,width);
//        params.bottomMargin=dp_5;
//        list.add("https://c-ssl.duitang.com/uploads/item/201510/18/20151018225255_3mRkF.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201510/18/20151018225531_2WtYL.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201604/05/20160405134540_XcEuv.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201510/15/20151015195532_tEXn2.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201608/18/20160818174129_5WAVL.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201607/12/20160712124842_PQEi4.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201801/29/20180129215053_5jjSC.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/201212/20/20121220164114_zde3P.jpeg");
//        list.add("https://c-ssl.duitang.com/uploads/item/202002/27/20200227115822_cdqpq.jpg");
    }

    public void setList(List<CircleInfoFeedImg> imageList) {
        this.list = imageList;
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
        NiceImageView feedImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_images_gridview, null);
            holder.feedImageView = convertView.findViewById(R.id.feedImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.feedImageView.setLayoutParams(params);
        Glide.with(parent.getContext()).load(list.get(position).getThumb_file()).into(holder.feedImageView);
        holder.feedImageView.setOnClickListener(new OnLargeImageClick(position));
        return convertView;
    }

    private class OnLargeImageClick implements View.OnClickListener{
        private int position;

        public OnLargeImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent largeImageIntent=new Intent(context, LargeImageActivity.class);
            largeImageIntent.putExtra("imgUrl",list.get(position).getFile());
            largeImageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(largeImageIntent);
        }
    }
}
