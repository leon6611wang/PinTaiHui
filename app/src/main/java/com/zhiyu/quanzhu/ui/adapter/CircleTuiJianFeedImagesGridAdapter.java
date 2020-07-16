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
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianImg;
import com.zhiyu.quanzhu.ui.activity.LargeImageActivity;
import com.zhiyu.quanzhu.ui.activity.LargeImageList2Activity;
import com.zhiyu.quanzhu.ui.activity.LargeImageListActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class CircleTuiJianFeedImagesGridAdapter extends BaseAdapter {
    private List<QuanZiTuiJianImg> list;
    private Context context;
    private int screenWidth, dp_15, dp_5, width;
    private RelativeLayout.LayoutParams params;

    public CircleTuiJianFeedImagesGridAdapter(Context context, int screen_width) {
        this.context = context;
        list = new ArrayList<>();
        screenWidth = screen_width;
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        width = Math.round((screenWidth - dp_15 * 2 - dp_5 * 2) / 3);
        params = new RelativeLayout.LayoutParams(width, width);
//        params.bottomMargin=dp_5;
    }

    public void setList(List<QuanZiTuiJianImg> imageList) {
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

    private class OnLargeImageClick implements View.OnClickListener {
        private int position;

        public OnLargeImageClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ArrayList<String> imgList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                imgList.add(list.get(i).getFile());
            }
            Intent largeIntent = new Intent(context, LargeImageList2Activity.class);
            largeIntent.putExtra("imgList", imgList);
            largeIntent.putExtra("position", position);
            largeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(largeIntent);
        }
    }
}
