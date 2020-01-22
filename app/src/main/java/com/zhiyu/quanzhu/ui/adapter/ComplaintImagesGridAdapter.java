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

public class ComplaintImagesGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> list = new ArrayList<>();
    private LinearLayout.LayoutParams ll;
    private int screenWidth, dp_15, dp_5, image_width;

    public ComplaintImagesGridAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        image_width = (screenWidth - dp_15 * 2 - dp_5 * 2) / 3;
        ll = new LinearLayout.LayoutParams(image_width, image_width);
        ll.rightMargin = dp_5;
        ll.bottomMargin = dp_5;
    }

    public void setData(ArrayList<String> img_list) {
        this.list = img_list;
        for(String s:list){
            System.out.println(s);
        }
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
        ImageView mImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint_imageview, null);
            holder.mImageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mImageView.setLayoutParams(ll);
        if (list.get(position).equals("add")) {
            Glide.with(context).load(R.mipmap.mingpian_add_img).into(holder.mImageView);
            holder.mImageView.setOnClickListener(new OnSelectImagesListener());
        } else {
            Glide.with(context).load(list.get(position)).into(holder.mImageView);
            holder.mImageView.setClickable(false);
        }
        return convertView;
    }


    public class OnSelectImagesListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null != onAddImagesListener) {
                onAddImagesListener.onAddImages();
            }
        }
    }

    private OnAddImagesListener onAddImagesListener;

    public void setOnAddImagesListener(OnAddImagesListener listener) {
        this.onAddImagesListener = listener;
    }

    public interface OnAddImagesListener {
        void onAddImages();
    }
}
