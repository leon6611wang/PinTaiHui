package com.leon.chic.utils;

public class H5Utils {
    private static H5Utils utils;
    public static H5Utils getInstance(){
        if(null==utils){
            if(null==utils){
                synchronized (H5Utils.class){
                    utils=new H5Utils();
                }
            }
        }
        return utils;
    }
    private final String BASE = "https://m.pintaihui.cn/";
    private final String GOODS_INFO = "goods.html";//商品详情
    private final String CARD_INFO = "cards.html";//名片详情
    private final String FEED_INFO = "feeds.html";//动态详情
    private final String CIRCLE_INFO = "circle.html";//圈子详情
    private final String COUPON_INFO = "coupon.html";//优惠券详情
    private final String SHANG_JIA_XIE_YI = "serviceagreement";//商家协议
    private final String YONG_HU_XIE_YI = "serviceagreement";//用户协议
    private final String YONG_HU_XIE_YI_VIP = "serviceagreement";//VIP用户协议
    private final String YIN_SI_ZHENG_CE = "privacypolicy";//隐私政策

    public String goodsInfo(){
        return BASE+GOODS_INFO;
    }
    public String cardInfo(){
        return BASE+CARD_INFO;
    }
    public String feedInfo(){
        return BASE+FEED_INFO;
    }
    public String circleInfo(){
        return BASE+CIRCLE_INFO;
    }
    public String couponInfo(){
        return BASE+COUPON_INFO;
    }
    public String shangJiaXieYi(){
        return BASE+SHANG_JIA_XIE_YI;
    }
    public String yongHuXieYi(){
        return BASE+YONG_HU_XIE_YI;
    }
    public String yongHuXieYiVIP(){
        return BASE+YONG_HU_XIE_YI_VIP;
    }
    public String yinSiZhengCe(){
        return BASE+YIN_SI_ZHENG_CE;
    }
}
