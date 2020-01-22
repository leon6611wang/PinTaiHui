package com.zhiyu.quanzhu.model.bean;

/**
 * 圈子推荐-图片
 */
public class QuanZiTuiJianImg {
    private String file;
    private int id;
    private int width;
    private int height;
    private String thumb_file;

    public QuanZiTuiJianImg(String file) {
        this.file = file;
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

    public String getThumb_file() {
        return thumb_file;
    }

    public void setThumb_file(String thumb_file) {
        this.thumb_file = thumb_file;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
