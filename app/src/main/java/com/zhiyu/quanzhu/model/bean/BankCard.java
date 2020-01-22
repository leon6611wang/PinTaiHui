package com.zhiyu.quanzhu.model.bean;

public class BankCard {
    private String bankName;
    private String bankNo;
    private String bankImage;

    public BankCard(String bankName, String bankNo, String bankImage) {
        this.bankName = bankName;
        this.bankNo = bankNo;
        this.bankImage = bankImage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankImage() {
        return bankImage;
    }

    public void setBankImage(String bankImage) {
        this.bankImage = bankImage;
    }
}
