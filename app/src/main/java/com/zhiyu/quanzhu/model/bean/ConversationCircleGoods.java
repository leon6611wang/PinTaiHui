package com.zhiyu.quanzhu.model.bean;


import android.content.Context;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class ConversationCircleGoods {
    private int goods_id;
    private String goods_name;
    private long goods_price;
    private int sale_num;
    private GoodsImg img;
    private LinearLayout.LayoutParams layoutParams;

    public LinearLayout.LayoutParams getLayoutParams(Context context, int position) {
        if (null == layoutParams) {
            int cardview_width, cardview_height, dp_25, dp_1, dp_2_5, dp_20;
            dp_25 = (int) context.getResources().getDimension(R.dimen.dp_25);
            dp_20 = (int) context.getResources().getDimension(R.dimen.dp_20);
            dp_2_5 = (int) context.getResources().getDimension(R.dimen.dp_2_5);
            dp_1 = (int) context.getResources().getDimension(R.dimen.dp_1);
            cardview_width = Math.round((ScreentUtils.getInstance().getScreenWidth(context) - dp_25 - dp_20) / 2);
            cardview_height = (int) (1.4545 * cardview_width);
            layoutParams = new LinearLayout.LayoutParams(cardview_width -dp_1*2, cardview_height );
            layoutParams.topMargin = dp_2_5;
            layoutParams.bottomMargin = dp_2_5;
            if (position % 2 == 1) {
                layoutParams.leftMargin = dp_2_5;
                layoutParams.rightMargin = dp_1;
            } else {
                layoutParams.leftMargin = dp_1;
                layoutParams.rightMargin = dp_2_5;
            }
        }
        return layoutParams;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
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

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public GoodsImg getImg() {
        return img;
    }

    public void setImg(GoodsImg img) {
        this.img = img;
    }
}
