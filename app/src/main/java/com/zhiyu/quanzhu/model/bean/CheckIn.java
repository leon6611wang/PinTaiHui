package com.zhiyu.quanzhu.model.bean;

/**
 * 签到
 */
public class CheckIn {
    private int id;
    private String rulename;
    private String action;
    private int credits;
    private String renwu_name;
    private boolean is_finish;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getRenwu_name() {
        return renwu_name;
    }

    public void setRenwu_name(String renwu_name) {
        this.renwu_name = renwu_name;
    }

    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }
}
