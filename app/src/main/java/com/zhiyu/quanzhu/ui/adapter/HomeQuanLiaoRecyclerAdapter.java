package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.MessageListActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class HomeQuanLiaoRecyclerAdapter extends RecyclerView.Adapter<HomeQuanLiaoRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_15, screenWidth, layoutHeight, layoutWidth;
    private LinearLayout.LayoutParams ll;
    private float ratio = 0.5768f;

    public HomeQuanLiaoRecyclerAdapter(Context context) {
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_xiaoxi_quanliao, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCardView.setLayoutParams(ll);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, MessageListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                SharedPreferencesUtils.getInstance(context).setConversationType(Conversation.ConversationType.GROUP.getName().toLowerCase());
                try {
                    RongIM.getInstance().startConversation(context, Conversation.ConversationType.GROUP, "1", "群聊");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,"无法进入圈聊会话界面",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
