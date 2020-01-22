package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;

import java.util.List;

public class ManageReplyRecyclerAdapter extends RecyclerView.Adapter<ManageReplyRecyclerAdapter.ViewHolder> {
    private List<String> list;
    private Context context;
    private YNDialog dialog;
    private int delete_position;
    public ManageReplyRecyclerAdapter(Context context) {
        this.context = context;
        dialog=new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                list.remove(delete_position);
                notifyDataSetChanged();
            }
        });
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView replyTextView;
        LinearLayout deleteLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            replyTextView = itemView.findViewById(R.id.replyTextView);
            deleteLayout = itemView.findViewById(R.id.deleteLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_reply, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.replyTextView.setText(list.get(position));
        holder.deleteLayout.setOnClickListener(new OnDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private class OnDeleteClick implements View.OnClickListener {
        private int position;

        public OnDeleteClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            delete_position=position;
            dialog.show();
            dialog.setTitle("确定删除此回复？");
        }
    }
}
