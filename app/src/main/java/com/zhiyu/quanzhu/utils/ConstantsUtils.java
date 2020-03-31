package com.zhiyu.quanzhu.utils;

/**
 * 常量
 */
public class ConstantsUtils {
    //        public static final String BASE_URL = "http://app.pintaihui.test/";
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
    //首页-圈子-搜圈
    public static final String SEARCH_CIRCLE = "/api/v1/index/circle/search";
    //首页-圈子-推荐列表-头部导航-标签
    public static final String HOME_QUAN_ZI_TUIJIAN_TAG = "api/v1/index/list/tag";
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
    public static final String CARD_USER_LIST = "api/v1/cards/usercards";
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
    public static final String FEEDS_COMMENTS_LIST = "api/v1/circle/feeds/getcomment";
    //动态详情-二级评论列表
    public static final String REPLY_COMMENT = "api/v1/circle/feeds/replycomment";
    //动态评论
    public static final String FEED_COMMENT = "api/v1/circle/feeds/comment";
    //删除评论
    public static final String DELETE_COMMENT = "api/v1/circle/feeds/delcomment";
    //评论详情
    public static final String COMMENT_DETAIL = "/api/v1/circle/feeds/comment/detail";

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
    public static final String SEARCH_MY_CIRCLE = "api/v1/circle/feeds/searchmycircle";
    //创建圈子
    public static final String ADD_CIRCLE = "api/v1/circle/add";
    //圈子详情
    public static final String CIRCLE_DETAIL = "api/v1/circle/detail";
    //圈子基础信息
    public static final String CIRCLE_BASE = "/api/v1/circle/base";
    //圈子详情-商店列表
    public static final String CIRCLE_INFO_SHOP_LIST = "/api/v1/circle/stores";
    //全局搜索
    public static final String FULL_SEARCH = "api/v1/index/search";
    //圈子详情-动态列表
    public static final String CIRCLE_INFO_FEED_LIST = "api/v1/circle/feeds";
    //圈子的成员列表
    public static final String CIRCLE_USER_LIST = "api/v1/circle/users";
    //圈子-用户列表-操作
    public static final String CIRCLE_USER_LIST_OPERATION = "api/v1/circle/operation";
    //编辑圈子资料
    public static final String EDIT_CIRCLE_PROFILE = "api/v1/circle/update";
    //文章、视频、动态详情
    public static final String FEEDS_INFO = "/api/v1/circle/feeds/base";

    //所有导航列表
    public static final String ALL_DAO_HANG_LIST = "api/v1/index/industry/list";

    //我添加的导航列表
    public static final String MY_DAO_HANG_LIST = "api/v1/index/myindustry";
    //添加导航
    public static final String ADD_DAO_HANG = "api/v1/index/industry/add";
    //删除导航
    public static final String DELETE_DAO_HANG = "api/v1/index/industry/del";
    //发布动态
    public static final String PUBLISH_FEED = "api/v1/circle/feeds/add";

    //用户详情
    public static final String USER_HOME = "api/v1/user/home";
    //我的店铺列表
    public static final String MY_SHOP_LIST = "api/v1/circle/feeds/mystore";
    //所搜店铺商品
    public static final String SEARCH_SHOP_GOODS = "/api/v1/circle/feeds/searchgoods";
    //新增动态
    public static final String ADD_FEED = "/api/v1/circle/feeds/add";
    //更新动态
    public static final String UPDATE_FEED = "/api/v1/circle/feeds/update";
    //动态关联商品
    public static final String RELATION_GOODS = "/api/v1/circle/feeds/relation_goods";
    //我选择过商品的店铺
    public static final String SEARCH_GOODS_SHOP = "/api/v1/circle/feeds/searchgoodsstore";
    //动态详情-商品列表
    public static final String ARTICLE_INFORMATON_GOODS_LIST = "/api/v1/circle/feeds/goods";

    //我的粉丝列表
    public static final String MY_FANS = "/api/v1/user/fans";
    //我的关注列表
    public static final String MY_FOLLOW = "/api/v1/user/follow";
    //加入购物车
    public static final String ADD_CART = "api/v1/mall/cart/add";

    public static final String DELETE_CART = "api/v1/mall/cart/del";
    //商品结算
    public static final String GOODS_SETTLEMENT = "api/v1/mall/shop/settlement";
    //购物车结算
    public static final String CART_SETTLEMENT = "api/v1/mall/cart/settlement";

    //收货地址列表
    public static final String ADDRESS_LIST = "api/v1/user/address";
    //新增收货地址
    public static final String ADDRESS_ADD = "/api/v1/user/address/add";
    //删除收货地址
    public static final String ADDRESS_DELETE = "/api/v1/user/address/del";
    //收货地址详情
    public static final String ADDRESS_DETAIL = "/api/v1/user/address/detail";
    //修改收货地址
    public static final String ADDRESS_UPDATE = "/api/v1/user/address/update";

    //购物车结算下单支付
    public static final String CART_ORDER_ADD = "/api/v1/mall/cartorder/add";
    //更改购物车商品数据
    public static final String CART_GOODS_EDIT = "api/v1/mall/cart/edit";
    //用户详情
    public static final String USER_PROFILE="api/v1/user/detail";
    //更新用户信息
    public static final String UPDATE_USER_PROFILE="api/v1/user/update";
}
