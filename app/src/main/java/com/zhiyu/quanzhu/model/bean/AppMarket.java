package com.zhiyu.quanzhu.model.bean;

public class AppMarket {
    private String marketName;
    private String packageName;


    @Override
    public String toString() {
        return marketName+" , "+packageName;
    }

    public AppMarket(String marketName, String packageName) {
        this.marketName = marketName;
        this.packageName = packageName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
