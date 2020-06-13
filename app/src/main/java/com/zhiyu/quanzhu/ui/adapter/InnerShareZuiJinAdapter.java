package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.IMCircle;
import com.zhiyu.quanzhu.model.bean.ZuiJinUser;
import com.zhiyu.quanzhu.ui.listener.ShareInnerSelectListener;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class InnerShareZuiJinAdapter extends RecyclerView.Adapter<InnerShareZuiJinAdapter.ViewHolder> {
    private Context context;
    private List<ZuiJinUser> list;

    public void setList(List<ZuiJinUser> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIdList() {
        List<Integer> idsList = new ArrayList<>();
        if (null != list && list.size() > 0)
            for (ZuiJinUser user : list) {
                if (user.isSelect()) {
                    idsList.add(user.getId());
                }
            }
        return idsList;
    }

    public InnerShareZuiJinAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView selectImageView;
        CircleImageView iconImageView;
        TextView nameTextView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            selectImageView = itemView.findViewById(R.id.selectImageView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inner_share, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).isSelect()) {
            holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.choose_goods_selected));
        } else {
            holder.selectImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.draft_unselect));
        }
        Glide.with(context).load(list.get(position).getAvatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(holder.iconImageView);
        holder.nameTextView.setText(list.get(position).getUsername());
        holder.layout.setOnClickListener(new OnSelectClick(position));
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
            notifyItemChanged(position);
            if (null != shareInnerSelectListener) {
                shareInnerSelectListener.onSelect(list.get(position).getAvatar(), list.get(position).getUsername(), list.get(position).isSelect(), false);
            }
        }
    }

    private static ShareInnerSelectListener shareInnerSelectListener;

    public static void setShareInnerSelectListener(ShareInnerSelectListener listener) {
        shareInnerSelectListener = listener;
    }
}
