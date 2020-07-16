package com.zhiyu.quanzhu.utils;

public class PriceParseUtils {
    private static PriceParseUtils utils;

    public static PriceParseUtils getInstance() {
        if (null == utils) {
            synchronized (PriceParseUtils.class) {
                utils = new PriceParseUtils();
            }
        }
        return utils;
    }

    public String parsePrice(long price) {
        long zhengshu = price / 100;
        long xiaoshu = price % 100;
        String priceStr = "";
        String zhengshuStr = "", xiaoshuStr = "";
        zhengshuStr = String.valueOf(zhengshu);
        if (xiaoshu == 0) {
            xiaoshuStr = ".00";
        } else if (xiaoshu > 0 && xiaoshu < 10) {
            xiaoshuStr = ".0" + xiaoshu;
        } else if (xiaoshu >= 10) {
            xiaoshuStr = "." + xiaoshu;
        }
        priceStr = zhengshuStr + xiaoshuStr;
        return priceStr;
    }


    public String getZhengShu(long price) {
        long zhengshu = price / 100;
        long xiaoshu = price % 100;
        String priceStr = null;
        String zhengshuStr = null, xiaoshuStr = null;
        zhengshuStr = String.valueOf(zhengshu);
        if (xiaoshu == 0) {
            xiaoshuStr = ".00";
        } else if (xiaoshu > 0 && xiaoshu < 10) {
            xiaoshuStr = ".0" + xiaoshu;
        } else if (xiaoshu >= 10) {
            xiaoshuStr = "." + xiaoshu;
        }
        priceStr = zhengshuStr + xiaoshuStr;
        return zhengshuStr;
    }

    public String getXiaoShu(long price) {
        long zhengshu = price / 100;
        long xiaoshu = price % 100;
        String priceStr = null;
        String zhengshuStr = null, xiaoshuStr = null;
        zhengshuStr = String.valueOf(zhengshu);
        if (xiaoshu == 0) {
            xiaoshuStr = ".00";
        } else if (xiaoshu > 0 && xiaoshu < 10) {
            xiaoshuStr = ".0" + xiaoshu;
        } else if (xiaoshu >= 10) {
            xiaoshuStr = "." + xiaoshu;
        }
        priceStr = zhengshuStr + xiaoshuStr;
        return xiaoshuStr;
    }

}
