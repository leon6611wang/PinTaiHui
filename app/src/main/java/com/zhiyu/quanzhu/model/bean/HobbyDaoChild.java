package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * 偏好
 */
@Table(name = "hobby_child")
public class HobbyDaoChild {
    @Column(name = "hobby_child_id", isId = true,autoGen = true)
    private int hobby_child_id;
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "sub_name")
    private String sub_name;
    @Column(name = "pid")
    private int pid;
    @Column(name = "type")
    private int type;
    @Column(name = "level")
    private int level;

    public int getHobby_child_id() {
        return hobby_child_id;
    }

    public void setHobby_child_id(int hobby_child_id) {
        this.hobby_child_id = hobby_child_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
