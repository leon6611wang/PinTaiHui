package com.leon.chic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.floor;

public class TimeUtils {
    private static TimeUtils utils;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-HH-dd HH:mm:ss");

    public static TimeUtils getInstance() {
        if (null == utils) {
            synchronized (TimeUtils.class) {
                utils = new TimeUtils();
            }
        }
        return utils;
    }

    public String getFormatTime(long time){
        return sdf.format(time);
    }

    public String getDisTime(long time) {
        String disTime = null;
        if (time > 0) {
            long dis = getSecondTimestamp() - time;
            if (dis < 60) {
                disTime = dis + "秒前";
            } else {
                if (dis < 3600) {
                    return (int)floor(dis / 60) + "分钟前";
                } else {
                    if (dis < 86400) {
                        return (int)floor(dis / 3600) + "小时前";
                    } else {
                        if (dis < 259200) {//3天内
                            return (int)floor(dis / 86400) + "天前";
                        } else {
                            return sdf.format(time);
                        }
                    }
                }
            }
        }

        return disTime;
    }


    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    private  int getSecondTimestamp() {
        String timestamp = String.valueOf(new Date().getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

}
