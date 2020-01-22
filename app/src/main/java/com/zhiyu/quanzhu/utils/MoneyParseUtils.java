package com.zhiyu.quanzhu.utils;

import java.text.DecimalFormat;

/**
 * 金额转化工具类
 */
public class MoneyParseUtils {

    private static MoneyParseUtils utils;

    public static MoneyParseUtils getInstance() {
        if (null == utils) {
            synchronized (MoneyParseUtils.class) {
                utils = new MoneyParseUtils();
            }
        }
        return utils;
    }


    public String decimal2Point(float price) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        String pri = decimalFormat.format(price);
        return pri;
    }

    public String[] decimal2PointString(float price) {
        String str = decimal2Point(price);
        String[] s = str.split(".");
        return s;
    }


}
