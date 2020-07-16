package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.LargeImageList2Activity;
import com.zhiyu.quanzhu.ui.activity.LargeImageListActivity;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情-精选评论-图片
 */
public class ItemGoodsInfoCommentsRecyclerAdapter extends RecyclerView.Adapter<ItemGoodsInfoCommentsRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private ViewGroup.LayoutParams ll;

    public void setList(ArrayList<String> imageList) {
        this.list = imageList;
        notifyDataSetChanged();
    }

    public ItemGoodsInfoCommentsRecyclerAdapter(Context context) {
        this.context = context;
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int dp_84 = (int) context.getResources().getDimension(R.dimen.dp_84);
        int width = Math.round((screenWidth - dp_84) / 3);
        ll = new ViewGroup.LayoutParams(width, width);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        NiceImageView commentImageView;
        LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            commentImageView = itemView.findViewById(R.id.commentImageView);
            rootLayout.setLayoutParams(ll);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_goodsinfo_comment_recycler, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.commentImageView);
        holder.commentImageView.setOnClickListener(new OnImageClickListener(position));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }


    private class OnImageClickListener implements View.OnClickListener {
        private int position;

        public OnImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, LargeImageList2Activity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("imgList", list);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
