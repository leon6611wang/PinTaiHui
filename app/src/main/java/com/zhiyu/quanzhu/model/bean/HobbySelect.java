package com.zhiyu.quanzhu.model.bean;

/**
 * 偏好选择-上传
 */
public class HobbySelect {
    private int id;
    private String name;
    private int pid;
    private String pname;
    private int ppid;
    private String ppname;
    private int pptype;


    public HobbySelect(int id, String name, int pid, String pname, int ppid, String ppname,int pptype) {
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.pname = pname;
        this.ppid = ppid;
        this.ppname = ppname;
        this.pptype=pptype;
    }

    public int getPpid() {
        return ppid;
    }

    public void setPpid(int ppid) {
        this.ppid = ppid;
    }

    public String getPpname() {
        return ppname;
    }

    public void setPpname(String ppname) {
        this.ppname = ppname;
    }

    public int getPptype() {
        return pptype;
    }

    public void setPptype(int pptype) {
        this.pptype = pptype;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
