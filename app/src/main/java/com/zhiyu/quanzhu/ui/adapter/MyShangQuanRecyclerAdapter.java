package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.ui.activity.CreateCircleActivity;
import com.zhiyu.quanzhu.ui.activity.ShangQuanInformationActivity;
import com.zhiyu.quanzhu.ui.dialog.DeleteImageDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class MyShangQuanRecyclerAdapter extends RecyclerView.Adapter<MyShangQuanRecyclerAdapter.ViewHolder> {
    private Context context;
    private int dp_15, screenWidth, layoutHeight, layoutWidth;
    private LinearLayout.LayoutParams ll;
    private float ratio = 0.5768f;
    private List<Circle> list;
    private DeleteImageDialog deleteDialog;
    private YNDialog ynDialog;

    public void setList(List<Circle> circleList) {
        this.list = circleList;
        notifyDataSetChanged();
    }

    public MyShangQuanRecyclerAdapter(Context context) {
        this.context = context;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        layoutWidth = screenWidth - dp_15 * 2;
        layoutHeight = (int) (layoutWidth * ratio);
        ll = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
        initDialogs();
    }

    private void initDialogs() {
        deleteDialog = new DeleteImageDialog(context, R.style.dialog, new DeleteImageDialog.OnDeleteImageClickListener() {
            @Override
            public void onDeleteImage() {
                ynDialog.show();
            }
        });
        ynDialog = new YNDialog(context, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                list.remove(deletePosition);
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView thumbImageView, shenheImageView;
        CircleImageView avatarImageView;
        TextView userNameTextView, circleNameTextView, pnumTextView;
        FrameLayout shenheLayout;
        LinearLayout passLayout;
        TextView shenheTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.mCardView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            circleNameTextView = itemView.findViewById(R.id.circleNameTextView);
            pnumTextView = itemView.findViewById(R.id.pnumTextView);
            shenheLayout = itemView.findViewById(R.id.shenheLayout);
            passLayout = itemView.findViewById(R.id.passLayout);
            shenheImageView = itemView.findViewById(R.id.shenheImageView);
            shenheTextView = itemView.findViewById(R.id.shenheTextView);
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
        Glide.with(context).load(list.get(position).getThumb())
                .error(R.mipmap.shop_information_bg)
                .into(holder.thumbImageView);
        holder.passLayout.setVisibility(list.get(position).getStatus() == 1 ? View.VISIBLE : View.GONE);
        holder.shenheLayout.setVisibility(list.get(position).getStatus() != 1 ? View.VISIBLE : View.GONE);
        if (list.get(position).getStatus() == 1) {
            Glide.with(context).load(list.get(position).getUseravatar())
                    .error(R.mipmap.no_avatar)
                    .into(holder.avatarImageView);
            holder.userNameTextView.setText(list.get(position).getUsername());
            holder.circleNameTextView.setText(list.get(position).getName());
            holder.pnumTextView.setText(String.valueOf(list.get(position).getPnum()));
            holder.mCardView.setOnClickListener(new OnCircleInfoClickListener(position));
        } else {
            //-2审核失败 -1审核中 1上架中  2后台下架 3后台禁用 4圈主解散
            switch (list.get(position).getStatus()) {
                case -2:
                    holder.shenheImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.circle_bohui));
                    holder.shenheTextView.setText("圈子申请已被驳回，请点击重新申请");
                    holder.mCardView.setOnClickListener(new OnEditCircleClickListener(position));
                    break;
                case -1:
                    holder.shenheImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.circle_shenhezhong));
                    holder.shenheTextView.setText("圈子审核中，可点击查看详情");
                    holder.mCardView.setOnClickListener(new OnEditCircleClickListener(position));
                    break;
                case 2:
                    holder.shenheImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.circle_xiajia));
                    holder.shenheTextView.setText("圈子已被下架，请根据系统通知调整");
                    holder.mCardView.setOnClickListener(new OnEditCircleClickListener(position));
                    break;
                case 3:
                    holder.shenheImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.circle_jinyong));
                    holder.shenheTextView.setText("圈子已被禁用，可向平台申诉解封");
                    holder.mCardView.setOnClickListener(new OnEditCircleClickListener(position));
                    break;
                case 4:
                    holder.shenheImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.circle_jiesan));
                    holder.shenheTextView.setText("圈子已被圈主解散，可长按删除");
                    holder.mCardView.setOnLongClickListener(new OnDeleteCircleLongClickListener(position));
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    /**
     * 编辑圈子信息
     */
    private class OnEditCircleClickListener implements View.OnClickListener {
        private int position;

        public OnEditCircleClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent circlrIntent = new Intent(context, CreateCircleActivity.class);
            circlrIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            circlrIntent.putExtra("circle_id", list.get(position).getId());
            context.startActivity(circlrIntent);
        }
    }

    private int deletePosition;

    /**
     * 删除圈子
     */
    private class OnDeleteCircleLongClickListener implements View.OnLongClickListener {
        private int position;

        public OnDeleteCircleLongClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            deletePosition = position;
            deleteDialog.show();
            return true;
        }
    }

    public class OnCircleInfoClickListener implements View.OnClickListener{
        private int position;

        public OnCircleInfoClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent circleInfoIntent=new Intent(context, ShangQuanInformationActivity.class);
            circleInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            circleInfoIntent.putExtra("circle_id",list.get(position).getId());
            context.startActivity(circleInfoIntent);
        }
    }
}
