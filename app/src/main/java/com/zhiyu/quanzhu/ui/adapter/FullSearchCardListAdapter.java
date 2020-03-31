package com.zhiyu.quanzhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchCard;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class FullSearchCardListAdapter extends BaseAdapter {
    private List<FullSearchCard> list;

    public void setList(List<FullSearchCard> cardList){
        this.list=cardList;
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
        NiceImageView coverImageView;
        TextView nameTextView,occupionTextView,companyTextView,cityTextView,industryTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(null==convertView){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_card,null);
            holder.coverImageView=convertView.findViewById(R.id.coverImageView);
            holder.nameTextView=convertView.findViewById(R.id.nameTextView);
            holder.occupionTextView=convertView.findViewById(R.id.occupionTextView);
            holder.companyTextView=convertView.findViewById(R.id.companyTextView);
            holder.cityTextView=convertView.findViewById(R.id.cityTextView);
            holder.industryTextView=convertView.findViewById(R.id.industryTextView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getCard_thumb()).error(R.mipmap.img_error).into(holder.coverImageView);
        holder.nameTextView.setText(list.get(position).getCard_name());
        holder.occupionTextView.setText(list.get(position).getOccupation());
        holder.companyTextView.setText(list.get(position).getCompany());
        holder.cityTextView.setText(list.get(position).getCity_name());
        holder.industryTextView.setText(list.get(position).getIndustry());
        if(StringUtils.isNullOrEmpty(list.get(position).getCity_name())){
            holder.cityTextView.setVisibility(View.GONE);
        }else{
            holder.cityTextView.setVisibility(View.VISIBLE);
        }
        if(StringUtils.isNullOrEmpty(list.get(position).getIndustry())){
            holder.industryTextView.setVisibility(View.GONE);
        }else{
            holder.industryTextView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
