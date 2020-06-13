package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.model.bean.InterestCircle;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class InterestQuanZiRecyclerAdapter extends RecyclerView.Adapter<InterestQuanZiRecyclerAdapter.ViewHolder> {
    private List<InterestCircle> list;
    private Context context;

    public InterestQuanZiRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<InterestCircle> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public List<InterestCircle> getList() {
        return list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTextView, daysTextView, circleNameTextView, circleDescTextView,
                cityTextView, industryTextView, chengyuanTextView, dongtaiTextView;
        NiceImageView iconImageView;
        ImageView selectImageView;
        CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            daysTextView = itemView.findViewById(R.id.daysTextView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            circleDescTextView = itemView.findViewById(R.id.circleDescTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            industryTextView = itemView.findViewById(R.id.industryTextView);
            chengyuanTextView = itemView.findViewById(R.id.chengyuanTextView);
            dongtaiTextView = itemView.findViewById(R.id.dongtaiTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            selectImageView = itemView.findViewById(R.id.selectImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest_quanzi, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUser_avatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.avatarImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getUser_avatar()))
            holder.nameTextView.setText(list.get(position).getUser_name());
        holder.daysTextView.setText(String.valueOf(list.get(position).getDays()));
        Glide.with(context).load(list.get(position).getThumb().getFile()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        if (!StringUtils.isNullOrEmpty(list.get(position).getName()))
            holder.circleNameTextView.setText(list.get(position).getName());
        if (!StringUtils.isNullOrEmpty(list.get(position).getDescirption()))
            holder.circleDescTextView.setText(list.get(position).getDescirption());
        if (!StringUtils.isNullOrEmpty(list.get(position).getCity_name())) {
            holder.cityTextView.setVisibility(View.VISIBLE);
            holder.cityTextView.setText(list.get(position).getCity_name());
        } else {
            holder.cityTextView.setVisibility(View.GONE);
        }
        if (!StringUtils.isNullOrEmpty(list.get(position).getIndustry())) {
            holder.industryTextView.setVisibility(View.VISIBLE);
            holder.industryTextView.setText(list.get(position).getIndustry());
        } else {
            holder.industryTextView.setVisibility(View.GONE);
        }
        if (list.get(position).isSelect()) {
            holder.selectImageView.setVisibility(View.VISIBLE);
            holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.color_e9e9e9));
        } else {
            holder.selectImageView.setVisibility(View.GONE);
            holder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.chengyuanTextView.setText(String.valueOf(list.get(position).getPnum()));
        holder.dongtaiTextView.setText(String.valueOf(list.get(position).getFnum()));
        holder.mCardView.setOnClickListener(new OnSelectClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnSelectClick implements View.OnClickListener {
        private int position;

        public OnSelectClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            list.get(position).setSelect(!list.get(position).isSelect());
            notifyDataSetChanged();
        }
    }


}
