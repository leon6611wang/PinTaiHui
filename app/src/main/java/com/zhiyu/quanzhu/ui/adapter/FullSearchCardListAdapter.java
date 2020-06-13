package com.zhiyu.quanzhu.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchCard;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;

import java.util.List;

public class FullSearchCardListAdapter extends BaseAdapter {
    private List<FullSearchCard> list;

    public void setList(List<FullSearchCard> cardList) {
        this.list = cardList;
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
        NiceImageView coverImageView;
        TextView nameTextView, occupionTextView, companyTextView, cityTextView, industryTextView;
        CardView mCardView;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_full_search_card, null);
            holder.coverImageView = convertView.findViewById(R.id.coverImageView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.occupionTextView = convertView.findViewById(R.id.occupionTextView);
            holder.companyTextView = convertView.findViewById(R.id.companyTextView);
            holder.cityTextView = convertView.findViewById(R.id.cityTextView);
            holder.industryTextView = convertView.findViewById(R.id.industryTextView);
            holder.mCardView=convertView.findViewById(R.id.mCardView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(list.get(position).getCard_thumb()).error(R.drawable.image_error).into(holder.coverImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getCard_name()))
            holder.nameTextView.setText(list.get(position).getCard_name());
        if (!StringUtils.isNullOrEmpty(list.get(position).getOccupation()))
            holder.occupionTextView.setText(list.get(position).getOccupation());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCompany()))
            holder.companyTextView.setText(list.get(position).getCompany());
        holder.cityTextView.setText(list.get(position).getCity_name());
        holder.industryTextView.setText(list.get(position).getIndustry());
        if (StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.GONE);
        } else {
            holder.cityTextView.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.GONE);
        } else {
            holder.industryTextView.setVisibility(View.VISIBLE);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(parent.getContext(), CardInformationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("card_id",(long)list.get(position).getId());
//                intent.putExtra("uid",(long)list.get(position).getUid());
                parent.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
