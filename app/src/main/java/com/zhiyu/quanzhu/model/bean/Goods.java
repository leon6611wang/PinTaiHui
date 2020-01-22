package com.zhiyu.quanzhu.model.bean;

import java.util.ArrayList;
import java.util.List;

public class Goods {
    private long id;
    private String goods_name;
    private List<String> img_list;
    private GoodsImg img;
    private ArrayList<String> img_json;
    private long goods_price;
    private int goods_stock;
    private int sale_num;
    private long shop_id;
    private String video;
    private String video_thumb;
    private String send_city;
    private List<GoodsTag> tags;
    private long min_price;
    private long max_price;


    public long getMin_price() {
        return min_price;
    }

    public void setMin_price(long min_price) {
        this.min_price = min_price;
    }

    public long getMax_price() {
        return max_price;
    }

    public void setMax_price(long max_price) {
        this.max_price = max_price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    public GoodsImg getImg() {
        return img;
    }

    public void setImg(GoodsImg img) {
        this.img = img;
    }

    public ArrayList<String> getImg_json() {
        return img_json;
    }

    public void setImg_json(ArrayList<String> img_json) {
        this.img_json = img_json;
    }

    public long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(long goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(int goods_stock) {
        this.goods_stock = goods_stock;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public long getShop_id() {
        return shop_id;
    }

    public void setShop_id(long shop_id) {
        this.shop_id = shop_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(String video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getSend_city() {
        return send_city;
    }

    public void setSend_city(String send_city) {
        this.send_city = send_city;
    }

    public List<GoodsTag> getTags() {
        return tags;
    }

    public void setTags(List<GoodsTag> tags) {
        this.tags = tags;
    }
}
