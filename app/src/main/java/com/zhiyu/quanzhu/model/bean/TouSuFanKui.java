package com.zhiyu.quanzhu.model.bean;

public class TouSuFanKui {
    private String id;
    private int msg_id;
    private int action;
    private String action_desc;
    private String target;
    private String reason;
    private String remark;
    private String result_msg;
    private String add_time;
    private String time;

    public String getId() {
        return id;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getAction_desc() {
        switch (action){
            case 1:
                this.action_desc="投诉未通过通知";
                break;
            case 2:
                this.action_desc="投诉受理通知";
                break;
            case 3:
                this.action_desc="账号/店铺/圈子/动态被投诉";
                break;
            case 4:
                this.action_desc="发起申诉";
                break;
            case 5:
                this.action_desc="申诉通过";
                break;
            case 6:
                this.action_desc="申诉未通过";
                break;
        }
        return action_desc;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
