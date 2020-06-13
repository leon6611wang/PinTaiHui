package com.zhiyu.quanzhu.model.data;

import android.content.Context;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.model.bean.Goods;
import com.zhiyu.quanzhu.model.bean.GoodsCircle;
import com.zhiyu.quanzhu.model.bean.GoodsCoupon;
import com.zhiyu.quanzhu.model.bean.GoodsGuarantee;

import java.util.List;

public class GoodsData {
    private Goods detail;
    private boolean has_norms;
    private List<GoodsGuarantee> guarantee;
    private List<GoodsCoupon> coupon;
    private String max_coupon;
    private String max_coupon_desc;
    private GoodsCircle circle;
    private List<Goods> goods_list;

    public String getMax_coupon_desc() {
        return max_coupon_desc;
    }

    public void setMax_coupon_desc(String max_coupon_desc) {
        this.max_coupon_desc = max_coupon_desc;
    }

    public List<Goods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Goods> goods_list) {
        this.goods_list = goods_list;
    }

    public Goods getDetail() {
        return detail;
    }

    public void setDetail(Goods detail) {
        this.detail = detail;
    }

    public boolean isHas_norms() {
        return has_norms;
    }

    public void setHas_norms(boolean has_norms) {
        this.has_norms = has_norms;
    }

    public List<GoodsGuarantee> getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(List<GoodsGuarantee> guarantee) {
        this.guarantee = guarantee;
    }

    public List<GoodsCoupon> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<GoodsCoupon> coupon) {
        this.coupon = coupon;
    }

    public String getMax_coupon() {
        return max_coupon;
    }

    public void setMax_coupon(String max_coupon) {
        this.max_coupon = max_coupon;
    }

    public GoodsCircle getCircle() {
        return circle;
    }

    public void setCircle(GoodsCircle circle) {
        this.circle = circle;
    }
}
