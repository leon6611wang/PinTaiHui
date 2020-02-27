package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;

import java.util.List;

public class RichTextMenuRecyclerAdapter extends RecyclerView.Adapter<RichTextMenuRecyclerAdapter.ViewHolder> {
    private List<Integer> list;
    private Context context;
    private int dp_18_5, dp_25, dp_20;
    private LinearLayout.LayoutParams ll, rightLL, leftLL;

    public RichTextMenuRecyclerAdapter(Context context) {
        this.context = context;
        dp_18_5 = (int) context.getResources().getDimension(R.dimen.dp_18_5);
        dp_20 = (int) context.getResources().getDimension(R.dimen.dp_20);
        dp_25 = (int) context.getResources().getDimension(R.dimen.dp_25);
        ll = new LinearLayout.LayoutParams(dp_25, dp_20);
        ll.rightMargin = dp_25;
        ll.topMargin = dp_18_5;
        ll.bottomMargin = dp_18_5;
        ll.gravity = Gravity.CENTER;
        leftLL = new LinearLayout.LayoutParams(dp_25, dp_20);
        leftLL.topMargin = dp_18_5;
        leftLL.bottomMargin = dp_18_5;
        leftLL.leftMargin = dp_20;
        leftLL.gravity = Gravity.CENTER;
        rightLL = new LinearLayout.LayoutParams(dp_25, dp_20);
        rightLL.topMargin = dp_18_5;
        rightLL.bottomMargin = dp_18_5;
        leftLL.rightMargin = dp_20;
        rightLL.gravity = Gravity.CENTER;
    }

    public void setList(List<Integer> iconList) {
        this.list = iconList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView menuImageView;
        View leftView, rightView;

        public ViewHolder(View itemView) {
            super(itemView);
            menuImageView = itemView.findViewById(R.id.menuImageView);
            leftView = itemView.findViewById(R.id.leftView);
            rightView = itemView.findViewById(R.id.rightView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rich_text_menu_recyclervieww, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.menuImageView.setImageDrawable(context.getResources().getDrawable(list.get(position)));
        if (position == 0) {
            holder.rightView.setVisibility(View.GONE);
        } else if (position == list.size() - 1) {
           holder.leftView.setVisibility(View.GONE);
        }else{
            holder.rightView.setVisibility(View.GONE);
        }
        holder.menuImageView.setOnClickListener(new OnMenuClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnMenuClick implements View.OnClickListener{
        private int position;

        public OnMenuClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(null!=onRichTextMenuClickListener){
                onRichTextMenuClickListener.onRichTextMenuClick(position);
            }
        }
    }
    public void setOnRichTextMenuClickListener(OnRichTextMenuClickListener listener){
        this.onRichTextMenuClickListener=listener;
    }
    private OnRichTextMenuClickListener onRichTextMenuClickListener;
    public interface OnRichTextMenuClickListener{
        void onRichTextMenuClick(int position);
    }
}
