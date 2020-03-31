package com.zhiyu.quanzhu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsFenLei;
import com.zhiyu.quanzhu.ui.activity.GoodsSearchActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class QuanShangFenLeiRecyclerViewRightItemRecyclerAdapter extends RecyclerView.Adapter<QuanShangFenLeiRecyclerViewRightItemRecyclerAdapter.ViewHolder> {
    private float ratio1 = 0.6987f;
    private int width, dp_40;
    private Context context;
    private LinearLayout.LayoutParams ll;
    private List<GoodsFenLei> list;

    public void setData(List<GoodsFenLei> fenLeiList) {
        this.list = fenLeiList;
        notifyDataSetChanged();
    }

    public QuanShangFenLeiRecyclerViewRightItemRecyclerAdapter(Context context) {
        this.context = context;
        dp_40 = (int) context.getResources().getDimension(R.dimen.dp_40);
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        int rightWidth = Math.round(screenWidth * ratio1);
        width = Math.round((rightWidth - dp_40) / 3);
        ll = new LinearLayout.LayoutParams(width, width);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemImageLayout;
        ImageView fenleiImageView;
        TextView fenleiNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImageLayout = itemView.findViewById(R.id.itemImageLayout);
            itemImageLayout.setLayoutParams(ll);
            fenleiImageView = itemView.findViewById(R.id.fenleiImageView);
            fenleiNameTextView = itemView.findViewById(R.id.fenleiNameTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_quanshang_fenlei_right, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemImageLayout.setOnClickListener(new OnFenLeiClickListener(list.get(position).getName()));
        holder.fenleiNameTextView.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getImg())
                //异常时候显示的图片
                .error(R.mipmap.img_error)
                //加载成功前显示的图片
                .placeholder(R.mipmap.img_loading)
                //url为空的时候,显示的图片
                .fallback(R.mipmap.img_error)
                .into(holder.fenleiImageView);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    public class OnFenLeiClickListener implements View.OnClickListener {
        private String fenlei;

        public OnFenLeiClickListener(String fenlei) {
            this.fenlei = fenlei;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, GoodsSearchActivity.class);
            intent.putExtra("keyword", fenlei);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
