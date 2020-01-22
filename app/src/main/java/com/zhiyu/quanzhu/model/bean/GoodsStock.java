package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 商品库存
 */
@Table(name = "goods_stock")
public class GoodsStock {
    @Column(name = "goods_stock_id", isId = true, autoGen = true)
    private long goods_stock_id;
    @Column(name = "id1")
    private long id1;
    @Column(name = "id2")
    private long id2;
    @Column(name = "id3")
    private long id3;
    @Column(name = "id4")
    private long id4;
    @Column(name = "id5")
    private long id5;
    @Column(name = "id6")
    private long id6;
    @Column(name = "id7")
    private long id7;
    @Column(name = "id8")
    private long id8;
    @Column(name = "id9")
    private long id9;
    @Column(name = "id10")
    private long id10;
    @Column(name = "price")
    private long price;//分
    @Column(name = "stock")
    private int stock;
    @Column(name = "img")
    private String img;

    @Override
    public String toString() {
        return "id1: " + id1 + " , id2: " + id2 + " , id3: " + id3 + " , id4: " + id4 + " , id5: " + id5 + " , id6: " + id6 + " , id7: " + id7 + " , id8: " + id8 + " , id9: " + id9 + " , id10: " + id10;
    }

    public long getId1() {
        return id1;
    }

    public void setId1(long id1) {
        this.id1 = id1;
    }

    public long getId2() {
        return id2;
    }

    public void setId2(long id2) {
        this.id2 = id2;
    }

    public long getId3() {
        return id3;
    }

    public void setId3(long id3) {
        this.id3 = id3;
    }

    public long getId4() {
        return id4;
    }

    public void setId4(long id4) {
        this.id4 = id4;
    }

    public long getId5() {
        return id5;
    }

    public void setId5(long id5) {
        this.id5 = id5;
    }

    public long getId6() {
        return id6;
    }

    public void setId6(long id6) {
        this.id6 = id6;
    }

    public long getId7() {
        return id7;
    }

    public void setId7(long id7) {
        this.id7 = id7;
    }

    public long getId8() {
        return id8;
    }

    public void setId8(long id8) {
        this.id8 = id8;
    }

    public long getId9() {
        return id9;
    }

    public void setId9(long id9) {
        this.id9 = id9;
    }

    public long getId10() {
        return id10;
    }

    public void setId10(long id10) {
        this.id10 = id10;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
