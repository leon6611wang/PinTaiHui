package com.zhiyu.quanzhu.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.DongTai;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class HomeTuiJianDongTaiRecyclerViewAdapter extends BaseRecyclerAdapter<DongTai>{
    private int screenWidth,dp_15,dp_10,dp_5,row_image_height;
    private LinearLayout.LayoutParams grid_params;
    private LinearLayout.LayoutParams image_params;
    public HomeTuiJianDongTaiRecyclerViewAdapter(Activity context) {
        this.context = context;
        screenWidth= ScreentUtils.getInstance().getScreenWidth(context);
        dp_5=(int)context.getResources().getDimension(R.dimen.dp_5);
        dp_10=(int)context.getResources().getDimension(R.dimen.dp_10);
        dp_15=(int)context.getResources().getDimension(R.dimen.dp_15);

        image_params =new LinearLayout.LayoutParams((screenWidth-dp_15*2-dp_10*3)/3,(screenWidth-dp_15*2-dp_10*3)/3);
        row_image_height=screenWidth-dp_15*2-dp_10*3+dp_5+dp_5;

        image_params.leftMargin=dp_5;
        image_params.rightMargin=dp_5;
        image_params.bottomMargin=dp_5;
        image_params.topMargin=dp_5;
    }

    private Activity context;



    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jinriredian, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, int position, DongTai data) {
        if(holder instanceof MyHolder) {
            MyHolder myHolder=(MyHolder)holder;
            Glide.with(context)
                    .load(data.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .into(myHolder.image);

            int image_rows=1;
            if(data.getImageList().size()>=1&&data.getImageList().size()<=3){
                image_rows=1;
            }else if(data.getImageList().size()>3&&data.getImageList().size()<=6){
                image_rows=2;
            }else if(data.getImageList().size()>6&&data.getImageList().size()<=9){
                image_rows=3;
            }
            grid_params =new LinearLayout.LayoutParams(screenWidth-dp_15*2,row_image_height*image_rows);
            grid_params.leftMargin=dp_15;
            grid_params.rightMargin=dp_15;

            myHolder.adapter.setData(data.getImageList());
            myHolder.adapter.setLayoutParams(image_params);
//            myHolder.dongtaiGridView.setLayoutParams(grid_params);
            myHolder.dongtaiGridView.setAdapter(myHolder.adapter);
        }
    }

    class MyHolder extends BaseRecyclerAdapter.Holder {
        ImageView image;
        MyGridView dongtaiGridView;
        DongTaiGridViewAdapter adapter;
        public MyHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            dongtaiGridView=itemView.findViewById(R.id.dongtaiGridView);
            adapter=new DongTaiGridViewAdapter(context);
        }
    }


}
