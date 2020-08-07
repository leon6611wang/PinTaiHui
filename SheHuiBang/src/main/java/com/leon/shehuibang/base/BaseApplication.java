package com.leon.shehuibang.base;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

public class BaseApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        x.Ext.init(this);                   //xUtils 初始化
    }
}
