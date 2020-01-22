package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.AddEditShouHuoDiZhiActivity;

public class ShouHuoDiZhiRecyclerAdapter extends RecyclerView.Adapter<ShouHuoDiZhiRecyclerAdapter.ViewHolder> {
    private Context context;

    public ShouHuoDiZhiRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phonenumTextView, locationTextView;
        ImageView editImageView, deleteImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phonenumTextView = itemView.findViewById(R.id.phonenumTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            editImageView = itemView.findViewById(R.id.editImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shouhuodizhi, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.editImageView.setOnClickListener(new EditOnClick(position));
        holder.deleteImageView.setOnClickListener(new DeleteOnClick(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class EditOnClick implements View.OnClickListener {
        private int position;

        public EditOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent editIntent = new Intent(context, AddEditShouHuoDiZhiActivity.class);
            editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(editIntent);
        }
    }

    class DeleteOnClick implements View.OnClickListener {
        private int position;

        public DeleteOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (null != onDiZhiDeleteListener) {
                onDiZhiDeleteListener.onDiZhiDelete(position);
            }
        }
    }

    private OnDiZhiDeleteListener onDiZhiDeleteListener;

    public void setOnDiZhiDeleteListener(OnDiZhiDeleteListener listener) {
        this.onDiZhiDeleteListener = listener;
    }

    public interface OnDiZhiDeleteListener {
        void onDiZhiDelete(int position);
    }
}
