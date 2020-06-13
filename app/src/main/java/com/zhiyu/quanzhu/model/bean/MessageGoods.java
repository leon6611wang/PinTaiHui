package com.zhiyu.quanzhu.model.bean;

import android.content.Context;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class MessageGoods {
    private int goods_id;
    private GoodsImg thumb;
    private String goods_name;
    private long goods_price;
    private int sales;
    private String goods_thumb;
    private int goods_num;

    public String getGoods_thumb() {
        return goods_thumb;
    }

    public void setGoods_thumb(String goods_thumb) {
        this.goods_thumb = goods_thumb;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    private LinearLayout.LayoutParams layoutParams;
    public LinearLayout.LayoutParams getLayoutParams(Context context){
        if(null==layoutParams){
            int screenWidth= ScreentUtils.getInstance().getScreenWidth(context);
            int dp_5=(int)context.getResources().getDimension(R.dimen.dp_5);
            int width=Math.round(0.64f*screenWidth);
            int heigh=Math.round(1.3458f*width);
            layoutParams=new LinearLayout.LayoutParams(width-dp_5*2,heigh-dp_5*2);
            layoutParams.topMargin=dp_5;
            layoutParams.leftMargin=dp_5;
            layoutParams.rightMargin=dp_5;
            layoutParams.bottomMargin=dp_5;
        }
        return layoutParams;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public GoodsImg getThumb() {
        return thumb;
    }

    public void setThumb(GoodsImg thumb) {
        this.thumb = thumb;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}
