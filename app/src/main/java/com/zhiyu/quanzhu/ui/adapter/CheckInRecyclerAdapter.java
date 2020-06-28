package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.CheckIn;
import com.zhiyu.quanzhu.ui.activity.CompleteUserProfileActivity;
import com.zhiyu.quanzhu.ui.activity.CreateCircleActivity;
import com.zhiyu.quanzhu.ui.activity.HomeActivity;
import com.zhiyu.quanzhu.ui.activity.MyProfileActivity;
import com.zhiyu.quanzhu.ui.activity.PublishArticleActivity;
import com.zhiyu.quanzhu.ui.activity.PublishFeedActivity;
import com.zhiyu.quanzhu.ui.activity.PublishVideoActivity;
import com.zhiyu.quanzhu.ui.activity.UserVertifyActivity;

import java.util.List;


public class CheckInRecyclerAdapter extends BaseRecyclerAdapter<CheckIn> {
    private Context context;

    public CheckInRecyclerAdapter(Context context) {
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descTextView, checkInTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            checkInTextView = itemView.findViewById(R.id.checkInTextView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_in, null));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, CheckIn data) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.titleTextView.setText(data.getRulename());
        holder.descTextView.setText("奖励" + data.getCredits() + "积分");
        if (data.isIs_finish()) {
            holder.checkInTextView.setText("已完成");
        } else {
            holder.checkInTextView.setText(data.getRenwu_name());
        }
        holder.checkInTextView.setOnClickListener(new OnCheckInClick(data));
    }

    private class OnCheckInClick implements View.OnClickListener {
        private CheckIn checkIn;

        public OnCheckInClick(CheckIn ci) {
            this.checkIn = ci;
        }

        @Override
        public void onClick(View v) {
            if (!checkIn.isIs_finish()) {
                if (checkIn.getAction().equals("Create_feeds_video")) {//发布视频
                    Intent publishVideoIntent = new Intent(context, PublishVideoActivity.class);
                    publishVideoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(publishVideoIntent);
                } else if (checkIn.getAction().equals("Create_feeds_article")) {//发布文章
                    Intent publishArticleIntent = new Intent(context, PublishArticleActivity.class);
                    publishArticleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(publishArticleIntent);
                } else if (checkIn.getAction().equals("Invite_register")) {//邀请注册成功
                    if(null!=onCheckInShareListener){
                        onCheckInShareListener.onCheckInShare(1,"邀请注册");
                    }
                } else if (checkIn.getAction().equals("Create_feeds")) {//发布动态
                    Intent publishFeedIntent = new Intent(context, PublishFeedActivity.class);
                    publishFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(publishFeedIntent);
                } else if (checkIn.getAction().equals("User_update")) {//完善资料
                    Intent editProfileIntent = new Intent(context, MyProfileActivity.class);
                    editProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(editProfileIntent);
                } else if (checkIn.getAction().equals("Day_comment")) {//每日首次评论
                    Intent homeIntent = new Intent(context, HomeActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(homeIntent);
                } else if (checkIn.getAction().equals("Create_circle")) {//首次创建商圈
                    Intent createCircleIntent = new Intent(context, CreateCircleActivity.class);
                    createCircleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(createCircleIntent);
                } else if (checkIn.getAction().equals("User_share")) {//分享
                    if(null!=onCheckInShareListener){
                        onCheckInShareListener.onCheckInShare(2,"分享");
                    }
                } else if (checkIn.getAction().equals("User_renzheng")) {//实名认证
                    Intent vertifyIntent = new Intent(context, UserVertifyActivity.class);
                    vertifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(vertifyIntent);
                }
            }
        }
    }
    private OnCheckInShareListener onCheckInShareListener;
    public void setOnCheckInShareListener(OnCheckInShareListener listener){
        this.onCheckInShareListener=listener;
    }
    public interface OnCheckInShareListener{
        void onCheckInShare(int type,String type_desc);
    }
}
