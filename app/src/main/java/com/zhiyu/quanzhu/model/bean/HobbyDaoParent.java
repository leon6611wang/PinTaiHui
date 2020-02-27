package com.zhiyu.quanzhu.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * 偏好
 */
@Table(name = "hobby_parent")
public class HobbyDaoParent {
    @Column(name = "hobby_parent_id", isId = true,autoGen = true)
    private int hobby_parent_id;
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

    private List<HobbyDaoChild> child;

    public int getHobby_parent_id() {
        return hobby_parent_id;
    }

    public void setHobby_parent_id(int hobby_parent_id) {
        this.hobby_parent_id = hobby_parent_id;
    }

    public List<HobbyDaoChild> getChild() {
        return child;
    }

    public void setChild(List<HobbyDaoChild> child) {
        this.child = child;
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
