package com.zhiyu.quanzhu.utils;

import java.text.SimpleDateFormat;

public class DateUtils {
    private static DateUtils utils;
    public static DateUtils getInstance(){
        if(null==utils){
            synchronized (DateUtils.class){
                utils=new DateUtils();
            }
        }
        return utils;
    }

    public String parseDate(long time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String date=sdf.format(time);
        return date;
    }
}
