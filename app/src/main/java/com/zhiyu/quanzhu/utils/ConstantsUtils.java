package com.zhiyu.quanzhu.utils;

/**
 * 常量
 */
public class ConstantsUtils {
    //    public static final String BASE_URL = "http://app.pintaihui.test/";
    public static final String BASE_URL = "http://192.168.1.200:8090/";
    //首页-圈子-关注列表
    public static final String HOME_QUANZI_GUANZHU_LIST = "api/v1/index/followcircles";
    //首页-圈子-推荐列表
    public static final String HOME_QUANZI_TUIJIAN_LIST = "api/v1/index/list/tj";
    //首页-圈子-推荐列表-感兴趣的商圈推荐
    public static final String SHANG_QUAN_TUI_JIAN_LIST = "api/v1/index/circle/tj";
    //首页-圈子-推荐列表-头部导航列表
    public static final String HOME_QUANZI_TUIJIAN_DAOHANG_LIST = "api/v1/index/myindustry";
    //首页-圈子-推荐列表-头部导航-视频
    public static final String HOME_QUANZI_TUIJIAN_DAOHANG_SHIPIN = "api/v1/index/list/film";
    //首页-圈子-推荐列表-头部导航-动态
    public static final String HOME_QUANZI_TUIJIAN_DAOHANG_DONGTAI = "api/v1/index/list/feeds";
    //首页-圈子-推荐列表-头部导航-地区
    public static final String HOME_QUANZI_TUIJIAN_DAOHANG_DIQU = "api/v1/index/list/district";
    //获取验证码
    public static final String GET_VERTIFY_CODE = "api/v1/sendcode";
    //验证码登录/注册/校验
    public static final String VERIFY_LOGIN = "api/v1/verifylogin";
    //密码登录
    public static final String LOGIN_BY_PWD = "api/v1/login";
    //基础信息
    public static final String APP_BASE = "api/v1/common/base";
    //app升级提醒
    public static final String APP_VERSION = "api/v1/common/appversion";
    //兴趣行业
    public static final String HOBBY_LIST = "api/v1/common/gethobby";
    //添加自定义兴趣
    public static final String ADD_HOBBY = "api/v1/common/sethobby";
    //删除自定义兴趣
    public static final String DELETE_HOBBY = "api/v1/common/delhobby";
    //微信登录
    public static final String WX_LOGIN = "api/v1/wxlogin";
    //QQ登录
    public static final String QQ_LOGIN = "api/v1/qqlogin";


    //商城首页广告
    public static final String MALL_HOME_AD = "api/v1/mall/home";
    //商城首页广告商品
    public static final String MALL_HOME_AD_GOODS = "api/v1/mall/ad/goodslist";
    //商城商品分类
    public static final String MALL_GOODS_FENLEI = "api/v1/mall/category";
    //商城商品子类
    public static final String MALL_GOODS_CHILD_FENLEI = "api/v1/mall/childcategory";
    //商品搜索
    public static final String GOODS_SEARCH = "api/v1/mall/searchgoods";
    //店铺详情
    public static final String SHOP_INFO = "api/v1/mall/shop/detail";
    //关注/取消关注
    public static final String FOLLOW = "api/v1/common/follow";
    //商铺商品分类
    public static final String SHOP_GOODS_TYPE = "api/v1/mall/shop/category";
    //商品详情
    public static final String GOODS_INFO = "api/v1/mall/goods/detail";
    //商品详情-猜你喜欢
    public static final String GOODS_INFO_LIKE_GOODS = "api/v1/mall/goods/likegoods";
    //商品详情-精选评价
    public static final String GOODS_INFO_COMMENTS = "api/v1/mall/goods/jxcomments";
    //商品详情-所有评价
    public static final String GOODS_INFO_ALL_COMMENTS = "api/v1/mall/goods/comments";
    //商品详情-商品规格
    public static final String GOODS_INFO_GOODS_NORMS = "api/v1/mall/goods/goodsnorms";
    //商品详情-规格对应库存
    public static final String GOODS_INFO_GOODS_STOCK = "api/v1/mall/goods/stock";
    //领取优惠券
    public static final String GET_COUPON = "api/v1/marketing/getcoupons";


    //购物车列表
    public static final String CART_LIST = "api/v1/mall/cart/list";


    //人脉首页
    public static final String CARD_INDEX = "api/v1/cards/index";
    //我的好友名片
    public static final String CARD_USER_CARDS = "api/v1/cards/usercards";
    //创建名片
    public static final String CARD_ADD = "api/v1/cards/add";
    //编辑名片
    public static final String CARD_UPDATE = "api/v1/cards/update";
    //删除名片
    public static final String CARD_DELETE = "api/v1/cards/del";
    //名片详情
    public static final String CARD_INFO = "api/v1/cards/detail";
    //交换名片
    public static final String CARD_EXCHANGE = "api/v1/cards/change";


    //省市数据
    public static final String CITYS = "api/v1/common/citys";


    //用户圈子列表
    //status状态-1审核中 0正常 1删除 2审核失败 3后台禁用
    public static final String CIRCLE_LIST = "api/v1/user/circle/list";
    //用户店铺列表
    public static final String STORE_LIST = "api/v1/user/store/list";


    //用户的动态列表
    public static final String USER_DONG_TAI_LIST = "api/v1/user/publish/list";


    //获取七牛token
    public static final String QI_NIU_TOKEN = "api/v1/common/qiniutoken";
    //我关注的人列表
    public static final String USER_FOLLOW = "api/v1/user/follow";

    //动态详情
    public static final String DONGTAI_INFORMATION = "api/v1/circle/feeds/detail";
    //动态详情-一级评论列表
    public static final String DONGTAI_COMMENTS_LIST = "api/v1/circle/feeds/getcomment";
    //动态详情-二级评论列表
    public static final String DONGTAI_COMMENTS_CHILD_LIST = "api/v1/circle/feeds/replycomment";
    //动态评论
    public static final String DONGTAI_COMMENT = "api/v1/circle/feeds/comment";
    //删除评论
    public static final String DELETE_COMMENT = "api/v1/circle/feeds/delcomment";

    //点赞-通用
    public static final String PRISE = "api/v1/common/prise";
    //收藏-通用
    public static final String COLLECT = "api/v1/common/collect";
    //举报
    public static final String REPORT = "api/v1/common/report";
    //搜索标签/热门标签
    public static final String SEARCH_TAGS = "api/v1/common/searchtags";
    //标签精准搜索
    public static final String SEARCH_TAG = "api/v1/common/searchtag";
    //生成标签
    public static final String ADD_TAG = "api/v1/common/addtags";
    //搜索与我相关的圈子
    public static final String SEARCH_MY_CIRCLE="api/v1/circle/searchmycircle";
    //创建圈子
    public static final String ADD_CIRCLE="api/v1/circle/add";
    //圈子详情
    public static final String CIRCLE_DETAIL="api/v1/circle/detail";

}
