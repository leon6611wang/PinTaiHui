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
import com.zhiyu.quanzhu.model.bean.FeedImage;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.PublishFeedActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class FullSearchFeedImagesGridAdapter extends BaseAdapter {
    private List<FeedImage> list;
    private Context context;
    private int screenWidth,dp_15,dp_5,width;
    private RelativeLayout.LayoutParams params;
    private boolean editFeed;
    private int feeds_id;
    public void setEditFeed(boolean edit,int id){
        this.editFeed=edit;
        this.feeds_id=id;
    }

    public FullSearchFeedImagesGridAdapter(Context context) {
        this.context=context;
        list=new ArrayList<>();
        screenWidth= ScreentUtils.getInstance().getScreenWidth(context);
        dp_5=(int)context.getResources().getDimension(R.dimen.dp_5);
        dp_15=(int)context.getResources().getDimension(R.dimen.dp_15);
        width=Math.round((screenWidth-dp_15*2-dp_5*2)/3);
        params=new RelativeLayout.LayoutParams(width,width);
//        params.bottomMargin=dp_5;
    }

    public void setList(List<FeedImage> imageList) {
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
        holder.feedImageView.setOnClickListener(new OnLargeImageClick(list.get(position).getFile()));
        return convertView;
    }

    private class OnLargeImageClick implements View.OnClickListener {
        private String imageUrl;

        public OnLargeImageClick(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public void onClick(View v) {
            if(!editFeed){
                Intent largeIntent = new Intent(context, LargeImageActivity.class);
                largeIntent.putExtra("imgUrl", imageUrl);
                largeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(largeIntent);
            }else{
                Intent videoInfoIntent = new Intent(context, PublishFeedActivity.class);
                videoInfoIntent.putExtra("feeds_id", feeds_id);
                videoInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(videoInfoIntent);
            }

        }
    }
}
