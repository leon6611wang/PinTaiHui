package com.leon.shehuibang.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.shehuibang.R;
import com.leon.shehuibang.model.bean.CommentsTemplate;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_1_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_1_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_2_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_2_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_2;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_3;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_4;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_3_5;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_4_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_4_1;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_5_0;
import com.leon.shehuibang.ui.widget.template.CommentsTemplate_6_0;

import java.util.List;

public class CommentsTemplateAdapter extends RecyclerView.Adapter<CommentsTemplateAdapter.ViewHolder> {
    private List<CommentsTemplate> list;
    private Context context;
    private LinearLayout.LayoutParams params;

    public CommentsTemplateAdapter(Context context) {
        this.context = context;
        int dp_100 = (int) context.getResources().getDimension(R.dimen.dp_100);
        params = new LinearLayout.LayoutParams(dp_100, dp_100);
    }

    public void setList(List<CommentsTemplate> templateList) {
        this.list = templateList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView countTextView;
        FrameLayout containerLayout;
        LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            countTextView = itemView.findViewById(R.id.countTextView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments_template, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.countTextView.setText(list.get(position).getImage_count() + " 图");
        View templateView = null;
        switch (list.get(position).getTemplate_code()) {
            case "1_0":
                templateView = new CommentsTemplate_1_0(context);
                break;
            case "1_1":
                templateView = new CommentsTemplate_1_1(context);
                break;
            case "2_0":
                templateView =new CommentsTemplate_2_0(context);
                break;
            case "2_1":
                templateView =new CommentsTemplate_2_1(context);
                break;
            case "3_0":
                templateView =new CommentsTemplate_3_0(context);
                break;
            case "3_1":
                templateView = new CommentsTemplate_3_1(context);
                break;
            case "3_2":
                templateView =new CommentsTemplate_3_2(context);
                break;
            case "3_3":
                templateView = new CommentsTemplate_3_3(context);
                break;
            case "3_4":
                templateView =new CommentsTemplate_3_4(context);
                break;
            case "3_5":
                templateView = new CommentsTemplate_3_5(context);
                break;
            case "4_0":
                templateView = new CommentsTemplate_4_0(context);
                break;
            case "4_1":
                templateView = new CommentsTemplate_4_1(context);
                break;
            case "5_0":
                templateView = new CommentsTemplate_5_0(context);
                break;
            case "6_0":
                templateView = new CommentsTemplate_6_0(context);
                break;
        }
        templateView.setLayoutParams(params);
        holder.containerLayout.addView(templateView);
        holder.containerLayout.setLayoutParams(params);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onTemplateAdapterClickListener) {
                    onTemplateAdapterClickListener.onTemplateeAdapterClick(list.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    private OnTemplateAdapterClickListener onTemplateAdapterClickListener;

    public void setOnTemplateAdapterClickListener(OnTemplateAdapterClickListener listener) {
        this.onTemplateAdapterClickListener = listener;
    }

    public interface OnTemplateAdapterClickListener {
        void onTemplateeAdapterClick(CommentsTemplate template);
    }
}
