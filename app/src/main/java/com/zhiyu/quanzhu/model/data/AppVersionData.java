package com.zhiyu.quanzhu.model.data;

import com.zhiyu.quanzhu.model.bean.AppVersion;

public class AppVersionData {
    private AppVersion android;
    private AppVersion ios;

    public AppVersion getAndroid() {
        return android;
    }

    public void setAndroid(AppVersion android) {
        this.android = android;
    }

    public AppVersion getIos() {
        return ios;
    }

    public void setIos(AppVersion ios) {
        this.ios = ios;
    }
}
