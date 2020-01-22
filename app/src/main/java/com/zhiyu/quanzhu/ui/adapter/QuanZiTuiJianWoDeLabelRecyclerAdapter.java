package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;

import java.util.List;

public class QuanZiTuiJianWoDeLabelRecyclerAdapter extends RecyclerView.Adapter<QuanZiTuiJianWoDeLabelRecyclerAdapter.ViewHolder> {
    private Context context;
    private boolean isEdit = false;
    private List<String> list;

    public void setList(List<String> label_list) {
        this.list = label_list;
        notifyDataSetChanged();
    }

    public void setIsEdit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }

    public QuanZiTuiJianWoDeLabelRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView labelTextView;
        ImageView closeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.labelTextView);
            closeImageView = itemView.findViewById(R.id.closeImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quanzi_tuijian_label, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.labelTextView.setText(list.get(position));
        if (position < 3) {
            holder.labelTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_quanzi_tuijian_label_bg_white));
            holder.closeImageView.setVisibility(View.GONE);
            holder.labelTextView.setClickable(true);
            holder.labelTextView.setOnClickListener(new OnGoToLabelClick(position));
        } else {
            holder.labelTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_quanzi_tuijian_label_bg_gray));
            if (isEdit) {
                holder.closeImageView.setVisibility(View.VISIBLE);
                holder.labelTextView.setClickable(false);
                holder.closeImageView.setOnClickListener(new OnDeleteClick(position));
            } else {
                holder.closeImageView.setVisibility(View.GONE);
                holder.labelTextView.setClickable(true);
                holder.labelTextView.setOnClickListener(new OnGoToLabelClick(position));
            }
        }
    }

    private class OnDeleteClick implements View.OnClickListener {
        private int position;

        public OnDeleteClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onLabelDeleteListener) {
                onLabelDeleteListener.onLabelDelete(position);
            }
        }
    }

    private OnLabelDeleteListener onLabelDeleteListener;

    public void setOnLabelDeleteListener(OnLabelDeleteListener labelDeleteListener) {
        this.onLabelDeleteListener = labelDeleteListener;
    }

    public interface OnLabelDeleteListener {
        void onLabelDelete(int position);
    }


    private class OnGoToLabelClick implements View.OnClickListener {
        private int position;

        public OnGoToLabelClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onLabelGoToListener) {
                onLabelGoToListener.onLabelGoTo(position);
            }
        }
    }

    private OnLabelGoToListener onLabelGoToListener;

    public void setOnLabelGoToListener(OnLabelGoToListener labelGoToListener) {
        this.onLabelGoToListener = labelGoToListener;
    }

    public interface OnLabelGoToListener {
        void onLabelGoTo(int position);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
