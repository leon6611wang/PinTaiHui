package com.leon.shehuibang.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.leon.shehuibang.R;
import com.leon.shehuibang.model.bean.Comments;
import com.leon.shehuibang.ui.widget.CommentsRecyclerViewItemMenu;
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
import com.leon.shehuibang.utils.HiddenAnimUtils;
import com.leon.shehuibang.utils.ScreentUtils;

import java.util.List;

public class FragmentCommentsCommentsAdapter extends RecyclerView.Adapter<FragmentCommentsCommentsAdapter.ViewHolder> {
    private List<Comments> list;
    private Context context;
    private FrameLayout.LayoutParams params;

    public FragmentCommentsCommentsAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        int width = screenWidth - dp_10 * 2;
        params = new FrameLayout.LayoutParams(width, width);
    }

    public void setList(List<Comments> commentsList) {
        this.list = commentsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout containerLayout;
        CommentsRecyclerViewItemMenu itemMenu;
        LinearLayout menuLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
            itemMenu = itemView.findViewById(R.id.itemMenu);
            menuLayout = itemView.findViewById(R.id.menuLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_comments_comments, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        View templateView = null;
        List<String> mImageList = list.get(position).getImage_list();
        switch (list.get(position).getTemplate_code()) {
            case "1_0":
                templateView = new CommentsTemplate_1_0(context);
                ((CommentsTemplate_1_0) templateView).setImageUrl(mImageList);
                break;
            case "1_1":
                templateView = new CommentsTemplate_1_1(context);
                ((CommentsTemplate_1_1) templateView).setImageUrl(mImageList);
                break;
            case "2_0":
                templateView = new CommentsTemplate_2_0(context);
                ((CommentsTemplate_2_0) templateView).setImageUrl(mImageList);
                break;
            case "2_1":
                templateView = new CommentsTemplate_2_1(context);
                ((CommentsTemplate_2_1) templateView).setImageUrl(mImageList);
                break;
            case "3_0":
                templateView = new CommentsTemplate_3_0(context);
                ((CommentsTemplate_3_0) templateView).setImageUrl(mImageList);
                break;
            case "3_1":
                templateView = new CommentsTemplate_3_1(context);
                ((CommentsTemplate_3_1) templateView).setImageUrl(mImageList);
                break;
            case "3_2":
                templateView = new CommentsTemplate_3_2(context);
                ((CommentsTemplate_3_2) templateView).setImageUrl(mImageList);
                break;
            case "3_3":
                templateView = new CommentsTemplate_3_3(context);
                ((CommentsTemplate_3_3) templateView).setImageUrl(mImageList);
                break;
            case "3_4":
                templateView = new CommentsTemplate_3_4(context);
                ((CommentsTemplate_3_4) templateView).setImageUrl(mImageList);
                break;
            case "3_5":
                templateView = new CommentsTemplate_3_5(context);
                ((CommentsTemplate_3_5) templateView).setImageUrl(mImageList);
                break;
            case "4_0":
                templateView = new CommentsTemplate_4_0(context);
                ((CommentsTemplate_4_0) templateView).setImageUrl(mImageList);
                break;
            case "4_1":
                templateView = new CommentsTemplate_4_1(context);
                ((CommentsTemplate_4_1) templateView).setImageUrl(mImageList);
                break;
            case "5_0":
                templateView = new CommentsTemplate_5_0(context);
                ((CommentsTemplate_5_0) templateView).setImageUrl(mImageList);
                break;
            case "6_0":
                templateView = new CommentsTemplate_6_0(context);
                ((CommentsTemplate_6_0) templateView).setImageUrl(mImageList);
                break;
        }
        templateView.setLayoutParams(params);
        holder.containerLayout.addView(templateView);
//        HiddenAnimUtils.newInstance(context, holder.itemMenu).toggle();
        holder.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HiddenAnimUtils.newInstance(context, holder.itemMenu).toggle();
                holder.itemMenu.operateMenu();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
}
