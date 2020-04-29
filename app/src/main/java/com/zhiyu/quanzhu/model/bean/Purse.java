package com.zhiyu.quanzhu.model.bean;

/**
 * 钱包
 */
public class Purse {
    private long money;//总收入
    private long frozen_money;//冻结收入
    private long in_money;//收入
    private long out_money;//支出
    private long ali_money;//支付宝提现余额
    private long wechat_money;//微信提现余额
    private String desc;//冻结说明
    private boolean is_pwd;//是否设置支付密码
    private boolean is_ali;
    private boolean is_wechar;

    public boolean isIs_ali() {
        return is_ali;
    }

    public void setIs_ali(boolean is_ali) {
        this.is_ali = is_ali;
    }

    public boolean isIs_wechar() {
        return is_wechar;
    }

    public void setIs_wechar(boolean is_wechar) {
        this.is_wechar = is_wechar;
    }

    public boolean isIs_pwd() {
        return is_pwd;
    }

    public void setIs_pwd(boolean is_pwd) {
        this.is_pwd = is_pwd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getAli_money() {
        return ali_money;
    }

    public void setAli_money(long ali_money) {
        this.ali_money = ali_money;
    }

    public long getWechat_money() {
        return wechat_money;
    }

    public void setWechat_money(long wechat_money) {
        this.wechat_money = wechat_money;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getFrozen_money() {
        return frozen_money;
    }

    public void setFrozen_money(long frozen_money) {
        this.frozen_money = frozen_money;
    }

    public long getIn_money() {
        return in_money;
    }

    public void setIn_money(long in_money) {
        this.in_money = in_money;
    }

    public long getOut_money() {
        return out_money;
    }

    public void setOut_money(long out_money) {
        this.out_money = out_money;
    }
}
