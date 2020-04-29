package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ShopEnclosure;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class ShopVertifyGridAdapter extends BaseAdapter {
    private List<ShopEnclosure> list;
    private Context context;
    private int screenWidth, width;
    private int dp_22_5, dp_15;
    private LinearLayout.LayoutParams ll;

    public ShopVertifyGridAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_22_5 = Math.round(context.getResources().getDimension(R.dimen.dp_22_5));
        dp_15 = Math.round(context.getResources().getDimension(R.dimen.dp_15));
        width = Math.round((screenWidth - dp_15 * 2 - dp_22_5 * 2) / 3);
        ll = new LinearLayout.LayoutParams(width, width);

    }

    public void setList(List<ShopEnclosure> shopEnclosureList) {
        this.list = shopEnclosureList;
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
        FrameLayout frameLayout;
        ImageView mImageView, addImageView;
        TextView mTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_vertify, null);
            holder.frameLayout = convertView.findViewById(R.id.frameLayout);
            holder.frameLayout.setLayoutParams(ll);
            holder.mImageView = convertView.findViewById(R.id.mImageView);
            holder.addImageView = convertView.findViewById(R.id.addImageView);
            holder.mTextView = convertView.findViewById(R.id.mTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(list.get(position).getTitle());
        if (!StringUtils.isNullOrEmpty(list.get(position).getPath())) {
            holder.addImageView.setVisibility(View.GONE);
            holder.mImageView.setVisibility(View.VISIBLE);
            Glide.with(parent.getContext()).load(list.get(position).getPath()).into(holder.mImageView);
        } else {
            holder.addImageView.setVisibility(View.VISIBLE);
            holder.mImageView.setVisibility(View.GONE);
        }
        holder.frameLayout.setOnClickListener(new OnUploadClickListener(position));
        return convertView;
    }

    private class OnUploadClickListener implements View.OnClickListener {
        private int position;

        public OnUploadClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onAddImageListener) {
                onAddImageListener.onAddImage(position);
            }
        }
    }

    private OnAddImageListener onAddImageListener;

    public void setOnAddImageListener(OnAddImageListener listener) {
        this.onAddImageListener = listener;
    }

    public interface OnAddImageListener {
        void onAddImage(int position);
    }


}
