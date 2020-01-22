package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 商品规格匹配规则
 */
@Table(name = "GuiGePiPei")
public class GuiGePiPei {
    @Column(isId = true, name = "id", autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "id1")
    private int id1;
    @Column(name = "id2")
    private int id2;
    @Column(name = "id3")
    private int id3;
    @Column(name = "id4")
    private int id4;
    @Column(name = "id5")
    private int id5;
    @Column(name = "id6")
    private int id6;
    @Column(name = "id7")
    private int id7;
    @Column(name = "id8")
    private int id8;
    @Column(name = "id9")
    private int id9;
    @Column(name = "id10")
    private int id10;
    @Column(name = "price")
    private float price;
    @Column(name = "stock")
    private int stock;
    @Column(name = "img")
    private String img;

    @Override
    public String toString() {
        return "id: " + id + " , id1: " + id1 + " , id2: " + id2 + " , id3: " + id3 + " , price: " + price + " , stock: " + stock + " , img: " + img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public int getId3() {
        return id3;
    }

    public void setId3(int id3) {
        this.id3 = id3;
    }

    public int getId4() {
        return id4;
    }

    public void setId4(int id4) {
        this.id4 = id4;
    }

    public int getId5() {
        return id5;
    }

    public void setId5(int id5) {
        this.id5 = id5;
    }

    public int getId6() {
        return id6;
    }

    public void setId6(int id6) {
        this.id6 = id6;
    }

    public int getId7() {
        return id7;
    }

    public void setId7(int id7) {
        this.id7 = id7;
    }

    public int getId8() {
        return id8;
    }

    public void setId8(int id8) {
        this.id8 = id8;
    }

    public int getId9() {
        return id9;
    }

    public void setId9(int id9) {
        this.id9 = id9;
    }

    public int getId10() {
        return id10;
    }

    public void setId10(int id10) {
        this.id10 = id10;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
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
