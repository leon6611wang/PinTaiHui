package com.zhiyu.quanzhu.model.bean;

import java.io.Serializable;

/**
 * 文章文字-图片内容
 */
public class ArticleContent implements Serializable{
    private int id;//主键id
    private int index;//顺序下标
    private String content;//文字内容或图片url
    private int type;//1.文字；2.图片
    private int width, height;


    @Override
    public String toString() {
        return "index: " + index + " , content: " + content + " , type: " + (type == 1 ? "文字" : "图片") + " , width: " + width + " , height: " + height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
