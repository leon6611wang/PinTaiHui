package com.zhiyu.quanzhu.model.bean;

public class Version {
    private VersionApp android;
    private VersionApp ios;

    public VersionApp getAndroid() {
        return android;
    }

    public void setAndroid(VersionApp android) {
        this.android = android;
    }

    public VersionApp getIos() {
        return ios;
    }

    public void setIos(VersionApp ios) {
        this.ios = ios;
    }
}
