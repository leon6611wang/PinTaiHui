package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.GoodsComment;

import java.util.List;

/**
 * 商品评价
 */
public class GoodsCommentData {
    private List<GoodsComment> list;
    private int allnum;
    private int imgnum;

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }

    public int getImgnum() {
        return imgnum;
    }

    public void setImgnum(int imgnum) {
        this.imgnum = imgnum;
    }

    public List<GoodsComment> getList() {
        return list;
    }

    public void setList(List<GoodsComment> list) {
        this.list = list;
    }
}
