package com.zhiyu.quanzhu.model.bean;

public class Bank {
    private int bankId;
    private String bankName;
    private boolean isSelected=false;

    public Bank(String bankName, boolean isSelected) {
        this.bankName = bankName;
        this.isSelected = isSelected;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
