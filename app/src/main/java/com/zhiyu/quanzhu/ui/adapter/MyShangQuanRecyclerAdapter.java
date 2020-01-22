package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class MyShangQuanRecyclerAdapter extends RecyclerView.Adapter<MyShangQuanRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_15, screenWidth, layoutHeight, layoutWidth;
    private LinearLayout.LayoutParams ll;
    private float ratio = 0.5768f;

    public MyShangQuanRecyclerAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        layoutWidth = screenWidth - dp_15 * 2;
        layoutHeight = (int) (layoutWidth * ratio);
        ll = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
       CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_shangquan, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCardView.setLayoutParams(ll);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
