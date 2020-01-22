package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.dialog.LargeImageDialog;

import java.util.List;

public class DongTaiGridViewAdapter extends BaseAdapter {
    private List<String> list;

    private LinearLayout.LayoutParams gl;
    private LargeImageDialog dialog;

    public DongTaiGridViewAdapter(Context context){
        dialog=new LargeImageDialog(context,R.style.dialog);
    }

    public void setLayoutParams( LinearLayout.LayoutParams params){
        this.gl=params;
    }

    public void setData(List<String> imglist){
        this.list=imglist;
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
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imageview,null);
            holder.imageView=convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.imageView.setLayoutParams(gl);
        Glide.with(parent.getContext()).load(list.get(position)).into(holder.imageView);
        holder.imageView.setOnClickListener(new OnImageClick(list.get(position)));
        return convertView;
    }

    private class OnImageClick implements View.OnClickListener{
        String image_url;
        public OnImageClick(String url) {
            this.image_url=url;
        }

        @Override
        public void onClick(View v) {
            dialog.show();
            dialog.setImageUrl(image_url);
        }
    }
}
