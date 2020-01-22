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

/**
 * 圈聊商品item
 */
public class ConversationGroupGoodsRecyclerAdapter extends RecyclerView.Adapter<ConversationGroupGoodsRecyclerAdapter.ViewHolder> {
    private Context context;
    private LinearLayout.LayoutParams ll;
    private int width, dp_20, dp_5, cardview_width, cardview_height;

    public ConversationGroupGoodsRecyclerAdapter(Context context) {
        this.context = context;
        dp_20 = (int) context.getResources().getDimension(R.dimen.dp_30);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
    }

    public void setWidth(int w) {
        this.width = w;
        System.out.println("conversation group goodsLayout width: " + width + " , dp_20: " + dp_20);
        cardview_width = (width - (int) (dp_20 * 3)) / 2;
//        cardview_width=(int)(0.4714f*width);
        cardview_height = (int) (1.4545 * cardview_width);
        ll = new LinearLayout.LayoutParams(cardview_width, cardview_height);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_group_goods, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (null != ll){
            if((position+1)%2!=0){
                ll.rightMargin=dp_5/2;
            }else{
                ll.leftMargin=dp_5/2;
            }
            ll.bottomMargin=dp_5;
            holder.mCardView.setLayoutParams(ll);
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
