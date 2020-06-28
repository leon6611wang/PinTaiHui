package com.leon.chic.utils;

import android.util.Log;

public class LogUtils {
    private static LogUtils utils;
    private boolean showLog = true;
    private String TAG = "leon";

    public static LogUtils getInstance() {
        if (null == utils) {
            synchronized (LogUtils.class) {
                utils = new LogUtils();
            }
        }
        return utils;
    }

    public void setShowLog(boolean isShow) {
        this.showLog = isShow;
    }

    public void show(String tag, String log) {
        if (showLog) {
            if (null != tag && !"".equals(tag)) {
                TAG = tag;
            }
            Log.i(TAG, log);
        }
    }

    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

}
