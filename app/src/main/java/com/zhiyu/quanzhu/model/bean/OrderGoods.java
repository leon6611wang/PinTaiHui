package com.zhiyu.quanzhu.model.bean;

import java.util.ArrayList;
import java.util.List;

public class OrderGoods {
    private int id;
    private int goods_id;
    private String goods_name;
    private long goods_price;
    private String goods_img;
    private int goods_num;
    private String goods_normas_name;
    private int refund;// 1.仅退款  2.退货退款,3换货
    private int score;



    private ArrayList<String> commentImageList;
    private List<String> urlList;
    private String commentContent;

    public List<String> getUrlList() {
        if(null==urlList){
            urlList=new ArrayList<>();
        }
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public ArrayList<String> getCommentImageList() {
        if (null == commentImageList) {
            commentImageList = new ArrayList<>();
        }
        if (null != commentImageList && commentImageList.size() == 0) {
            commentImageList.add("add");
        }
        return commentImageList;
    }

    public void setCommentImageList(ArrayList<String> commentImageList) {
        this.commentImageList = commentImageList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_normas_name() {
        return goods_normas_name;
    }

    public void setGoods_normas_name(String goods_normas_name) {
        this.goods_normas_name = goods_normas_name;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }
}
