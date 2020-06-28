package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.leon.chic.dao.CardDao;
import com.leon.chic.dao.IMUserDao;
import com.leon.chic.utils.AndroidBug54971Workaround;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.model.result.AppVersionResult;
import com.zhiyu.quanzhu.model.result.CardFrendResult;
import com.zhiyu.quanzhu.model.result.HomeBaseResult;
import com.zhiyu.quanzhu.ui.adapter.MyFragmentStatePagerAdapter;
import com.zhiyu.quanzhu.ui.dialog.AppUpdateDialog;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeQuanShang;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeQuanZi;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeRenMai;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeWoDe;
import com.zhiyu.quanzhu.ui.fragment.FragmentHomeXiaoXi;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.AppUtils;
import com.zhiyu.quanzhu.utils.AppVersionUtils;
import com.zhiyu.quanzhu.utils.BaseDataUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.IMUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private NoScrollViewPager viewPager;
    private MyFragmentStatePagerAdapter adapter;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private LinearLayout quanzilayout, renmailayout, xiaoxilayout, quanshanglayout, wodelayout;
    private ImageView quanziimageview, renmaiimageview, xiaoxiimageview, quanshangimageview, wodeimageview;
    private TextView quanzitextview, renmaitextview, xiaoxitextview, quanshangtextview, wodetextview;
    private CardView bottomBarLayout;
    private int bottomBarHeight, bottomBarWidth;
    private AppUpdateDialog appUpdateDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<HomeActivity> activityWeakReference;

        public MyHandler(HomeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            HomeActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    activity.loginSuccessOperation();
                    break;
                case 1:
                    String version = AppUtils.getInstance().getVersionName(activity);
                    boolean neglect = SharedPreferencesUtils.getInstance(activity).getNeglect();
                    if (null == version) {
                        activity.appUpdateDialog.show();
                        activity.appUpdateDialog.setData(activity.appVersionResult.getData().getAndroid().getChangelog(), activity.appVersionResult.getData().getAndroid().getVersion());
                        SharedPreferencesUtils.getInstance(activity).saveAppVersion(activity.appVersionResult.getData().getAndroid().getVersion(),
                                activity.appVersionResult.getData().getAndroid().isIsforce());
                    } else if (AppVersionUtils.getInstance().compareVersions(activity.appVersionResult.getData().getAndroid().getVersion(), version)) {
                        if (!neglect) {
                            activity.appUpdateDialog.show();
                            activity.appUpdateDialog.setData(activity.appVersionResult.getData().getAndroid().getChangelog(), activity.appVersionResult.getData().getAndroid().getVersion());
                            SharedPreferencesUtils.getInstance(activity).saveAppVersion(activity.appVersionResult.getData().getAndroid().getVersion(),
                                    activity.appVersionResult.getData().getAndroid().isIsforce());
                        }
                    } else if (version.equals(activity.appVersionResult.getData().getAndroid().getVersion()) && !neglect) {
                        activity.appUpdateDialog.show();
                        activity.appUpdateDialog.setData(activity.appVersionResult.getData().getAndroid().getChangelog(), activity.appVersionResult.getData().getAndroid().getVersion());
                    }
                    break;
                case 2:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
//        IMUserDao.getInstance().clearIMUser(BaseApplication.getInstance());
//        PageDao.getInstance().clear(BaseApplication.getInstance());
//        CardDao.getInstance().clear(BaseApplication.getInstance());
//        MessageDao.getInstance().clearSystemMessage(BaseApplication.getInstance());
//        MessageDao.getInstance().clearServiceMessage(BaseApplication.getInstance());

//        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//            @Override
//            public void run() {
//                initConversationData();
//            }
//        });

        bottomBarLayout = findViewById(R.id.bottomBarLayout);
        bottomBarLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        bottomBarLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        bottomBarWidth = bottomBarLayout.getWidth(); // 获取宽度
                        bottomBarHeight = bottomBarLayout.getHeight(); // 获取高度
                        initDatas();
                        initViews();
                        return true;
                    }
                });

//        getToken("9527", "亚瑟");

        initDialogs();
        BaseDataUtils.getInstance().initBaseData();
        if (null == mLocationClient) {
//            System.out.println("###########初始化定位");
            initLocation();
        } else {
//            System.out.println("###########启动定位");
            //启动定位
            mLocationClient.startLocation();
        }
    }


    /**
     * 设置标签与别名
     */
    private void initJPush() {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了
         **/
        Set<String> tags = new HashSet<>();
        //这里可以设置你要推送的人，一般是用户uid 不为空在设置进去 可同时添加多个
        if (!TextUtils.isEmpty(String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)))) {
            tags.add(String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)));//设置tag
        }
        //上下文、别名【Sting行】、标签【Set型】、回调
        JPushInterface.setAliasAndTags(this, String.valueOf(SPUtils.getInstance().getUserId(BaseApplication.applicationContext)), tags,
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int code, String alias, Set<String> set) {
                        switch (code) {
                            case 0:
                                //这里可以往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//                                System.out.println("极光推送别名设置成功");
                                break;
                            case 6002:
                                //极低的可能设置失败 我设置过几百回 出现3次失败 不放心的话可以失败后继续调用上面那个方面 重连3次即可 记得return 不要进入死循环了...
//                                System.out.println("极光推送别名设置失败，60秒后重试");
                                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(60000);
                                            initJPush();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                break;
                            default:
//                                System.out.println("极光推送设置失败， errorCode = " + code);
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        requestAppVersion();
        if (checkLogin()) {
            autoLogin();
        }
    }

    private void loginSuccessOperation() {
//        System.out.println("自动登录成功，正在连接IM服务器");
        imConnect();
        initJPush();
    }

    private void initDialogs() {
        appUpdateDialog = new AppUpdateDialog(this, R.style.dialog);
    }

    private void initConversationData() {
        com.zhiyu.quanzhu.model.bean.Conversation c1 = new com.zhiyu.quanzhu.model.bean.Conversation();
        c1.setUser_id("1");
        c1.setUser_name("浪漫樱花");
        c1.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201802/03/20180203204046_yskxu.thumb.700_0.jpg");
        com.zhiyu.quanzhu.model.bean.Conversation c2 = new com.zhiyu.quanzhu.model.bean.Conversation();
        c2.setUser_id("2");
        c2.setUser_name("删删来迟");
        c2.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201810/04/20181004195003_abceb.thumb.700_0.jpg");
        com.zhiyu.quanzhu.model.bean.Conversation c98 = new com.zhiyu.quanzhu.model.bean.Conversation();
        c98.setUser_id("98");
        c98.setUser_name("宫本大头兵");
        c98.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201907/26/20190726233627_4R5ZB.thumb.700_0.jpeg");

        com.zhiyu.quanzhu.model.bean.Conversation c99 = new com.zhiyu.quanzhu.model.bean.Conversation();
        c99.setUser_id("99");
        c99.setUser_name("阿篱");
        c99.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201908/27/20190827072305_2mAeW.thumb.700_0.jpeg");

        com.zhiyu.quanzhu.model.bean.Conversation c97 = new com.zhiyu.quanzhu.model.bean.Conversation();
        c97.setUser_id("97");
        c97.setUser_name("蔡文姬");
        c97.setHeader_pic("https://c-ssl.duitang.com/uploads/item/201701/31/20170131212334_xFrhT.thumb.700_0.jpeg");
        ConversationDao.getDao(this).save(c1);
        ConversationDao.getDao(this).save(c2);
        ConversationDao.getDao(this).save(c97);
        ConversationDao.getDao(this).save(c98);
        ConversationDao.getDao(this).save(c99);
    }

    private FragmentHomeQuanZi fragmentHomeQuanZi;

    private void initDatas() {
        fragmentHomeQuanZi = new FragmentHomeQuanZi();
        Bundle bundle = new Bundle();
        bundle.putInt("bottomBarHeight", bottomBarHeight);
        fragmentHomeQuanZi.setArguments(bundle);
        fragmentArrayList.add(fragmentHomeQuanZi);
        fragmentArrayList.add(new FragmentHomeRenMai());
        fragmentArrayList.add(new FragmentHomeXiaoXi());
        fragmentArrayList.add(new FragmentHomeQuanShang());
        fragmentArrayList.add(new FragmentHomeWoDe());
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setScroll(false);
        adapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

        quanzilayout = findViewById(R.id.quanzilayout);
        renmailayout = findViewById(R.id.renmailayout);
        xiaoxilayout = findViewById(R.id.xiaoxilayout);
        quanshanglayout = findViewById(R.id.quanshanglayout);
        wodelayout = findViewById(R.id.wodelayout);
        quanzilayout.setOnClickListener(this);
        renmailayout.setOnClickListener(this);
        xiaoxilayout.setOnClickListener(this);
        quanshanglayout.setOnClickListener(this);
        wodelayout.setOnClickListener(this);
        quanziimageview = findViewById(R.id.quanziimageview);
        renmaiimageview = findViewById(R.id.renmaiimageview);
        xiaoxiimageview = findViewById(R.id.xiaoxiimageview);
        quanshangimageview = findViewById(R.id.quanshangimageview);
        wodeimageview = findViewById(R.id.wodeimageview);
        quanzitextview = findViewById(R.id.quanzitextview);
        renmaitextview = findViewById(R.id.renmaitextview);
        xiaoxitextview = findViewById(R.id.xiaoxitextview);
        quanshangtextview = findViewById(R.id.quanshangtextview);
        wodetextview = findViewById(R.id.wodetextview);
        barChange(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanzilayout:
                barChange(0);
                break;
            case R.id.renmailayout:
                if (checkLogin()) {
                    barChange(1);
                } else {
                    userLogin(1);
                }

                break;
            case R.id.xiaoxilayout:
                if (checkLogin()) {
                    barChange(2);
                } else {
                    userLogin(2);
                }

                break;
            case R.id.quanshanglayout:
                barChange(3);
                break;
            case R.id.wodelayout:
                if (checkLogin()) {
                    barChange(4);
                } else {
                    userLogin(4);
                }

                break;
        }
    }

    private void userLogin(int position) {
        Intent intent = new Intent(this, LoginGetVertifyCodeActivity.class);
        intent.putExtra("type", "from_home");
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void barChange(int position) {
        viewPager.setCurrentItem(position);
        quanzitextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        renmaitextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        xiaoxitextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        quanshangtextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        wodetextview.setTextColor(getResources().getColor(R.color.text_color_gray));
        Glide.with(this).load(R.mipmap.home_quanzi_gray).into(quanziimageview);
        Glide.with(this).load(R.mipmap.home_renmai_gray).into(renmaiimageview);
        Glide.with(this).load(R.mipmap.home_xiaoxi_gray).into(xiaoxiimageview);
        Glide.with(this).load(R.mipmap.home_quanshang_gray).into(quanshangimageview);
        Glide.with(this).load(R.mipmap.home_wode_gray).into(wodeimageview);
        switch (position) {
            case 0:
                quanzitextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                Glide.with(this).load(R.mipmap.home_quanzi_yellow).into(quanziimageview);
                break;
            case 1:
                renmaitextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                Glide.with(this).load(R.mipmap.home_renmai_yellow).into(renmaiimageview);
                break;
            case 2:
                xiaoxitextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                Glide.with(this).load(R.mipmap.home_xiaoxi_yellow).into(xiaoxiimageview);
                break;
            case 3:
                quanshangtextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                Glide.with(this).load(R.mipmap.home_quanshang_yellow).into(quanshangimageview);
                break;
            case 4:
                wodetextview.setTextColor(getResources().getColor(R.color.text_color_yellow));
                Glide.with(this).load(R.mipmap.home_wode_yellow).into(wodeimageview);
                break;
        }
    }

    private void imConnect() {
        RongIM.connect(SPUtils.getInstance().getIMToken(BaseApplication.applicationContext), new RongIMClient.ConnectCallbackEx() {
            @Override
            public void OnDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
//                System.out.println("OnDatabaseOpened: " + code.toString());
            }

            @Override
            public void onTokenIncorrect() {
                System.out.println("onTokenIncorrect: ");
            }

            @Override
            public void onSuccess(String s) {
//                System.out.println("登录融云服务器成功，当前用户id: " + s);
                RongIM.setOnReceiveMessageListener(BaseApplication.receiveMessageListener);
//                SharedPreferencesUtils.getInstance(HomeActivity.this).saveUser("1111", "一号测试", "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190518/d38fda99a9654dd2b5b60950a1cb9967.jpeg", "18768100516");
                updateUserInfo();
//                sendMsg();
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
//                System.out.println("onError: " + e.toString());
//                getToken("1139","不买魔女了");
            }
        });
    }


    public RequestParams addHeader(RequestParams params) {
        Random r = new Random();
        String Nonce = (r.nextInt(10000) + 10000) + "";
        String Timestamp = (System.currentTimeMillis() / 1000) + "";
        params.addHeader("App-Key", IMUtils.APP_KEY);
        params.addHeader("Nonce", Nonce);
        params.addHeader("Timestamp", Timestamp);
        params.addHeader("Signature",
                sha1(IMUtils.APP_SECRET + Nonce + Timestamp));
        return params;
    }

    public void getToken(final String id, final String username) {
        RequestParams params = new RequestParams(
                "https://api.cn.ronghub.com/user/getToken.json");
        addHeader(params);
        params.addBodyParameter("userId", id);
        params.addBodyParameter("name", username);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
//                System.out.println("IM onError: " + arg0.toString());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String s) {
//                System.out.println("IM token: " + s);
            }
        });
    }

    private String sha1(String data) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data.getBytes());
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0) a += 256;
                if (a < 16) buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        } catch (Exception e) {

        }
        return buf.toString();
    }


    private void sendMsg() {
        // 构建文本消息实例
        TextMessage textMessage = TextMessage.obtain("我是97号，正在向1号发送文本消息");
/**
 * 根据会话类型，发送消息。
 */
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, "1", textMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                // 消息成功存到本地数据库的回调
            }

            @Override
            public void onSuccess(Message message) {
                // 消息发送成功的回调
//                System.out.println("消息发送成功");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                // 消息发送失败的回调
            }
        });
    }

    private void updateUserInfo() {
        int user_id = SPUtils.getInstance().getUserId(BaseApplication.applicationContext);
        String user_name = SPUtils.getInstance().getUserName(BaseApplication.applicationContext);
        String user_avatar = SPUtils.getInstance().getUserAvatar(BaseApplication.applicationContext);
        UserInfo userInfo = new UserInfo(String.valueOf(user_id),
                user_name,
                StringUtils.isNullOrEmpty(user_avatar) ? null : Uri.parse(user_avatar));
        RongIM.getInstance().setCurrentUserInfo(userInfo);
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(String.valueOf(user_id),
                user_name,
                StringUtils.isNullOrEmpty(user_avatar) ? null : Uri.parse(user_avatar)));
    }

    private boolean checkLogin() {
        return SPUtils.getInstance().userIsLogin(BaseApplication.applicationContext);
    }

    private HomeBaseResult homeBaseResult;
    public static String business_url;
    public static boolean is_rz;

    private void autoLogin() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.APP_BASE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("基础信息: " + result);
                homeBaseResult = GsonUtils.GsonToBean(result, HomeBaseResult.class);
                if (200 == homeBaseResult.getCode()) {
                    business_url = homeBaseResult.getData().getBusiness_url();
                    is_rz = homeBaseResult.getData().isIs_rz();
                    System.out.println("是否认证: " + is_rz);
                    SPUtils.getInstance().saveUserInfo(BaseApplication.applicationContext, homeBaseResult.getData().getUid(), homeBaseResult.getData().getUser().getUsername(),
                            homeBaseResult.getData().getUser().getAvatar());
                    SPUtils.getInstance().saveUserToken(BaseApplication.applicationContext, homeBaseResult.getToken());
                    SPUtils.getInstance().saveIMToken(BaseApplication.applicationContext, homeBaseResult.getData().getToken());
                    SPUtils.getInstance().setSilence(BaseApplication.applicationContext,
                            homeBaseResult.getData().getUser().getGfmessagestatus(),
                            homeBaseResult.getData().getUser().getFirendsmessagestatus(),
                            homeBaseResult.getData().getUser().getCirclemessagestatus(),
                            homeBaseResult.getData().getUser().getPaymessagestatus(),
                            homeBaseResult.getData().getUser().getCouponmessagestatus(),
                            homeBaseResult.getData().getUser().getShopmessagestatus(),
                            homeBaseResult.getData().getUser().getFeedbackmessagestatus(),
                            homeBaseResult.getData().getUser().getMessagestatus());
                    SPUtils.getInstance().saveIMToken(BaseApplication.applicationContext, homeBaseResult.getData().getToken());
                    android.os.Message message = myHandler.obtainMessage(0);
                    message.sendToTarget();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("基础信息 error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private AppVersionResult appVersionResult;

    private void requestAppVersion() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.APP_VERSION);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                appVersionResult = GsonUtils.GsonToBean(result, AppVersionResult.class);
                appVersionResult.getData().getAndroid().setVersion("5.2.7");
                android.os.Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
//                System.out.println("app version: " + appVersionResult.getData().getAndroid().getVersion());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("app version error:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient)
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    String country = amapLocation.getCountry();//国家信息
                    String province = amapLocation.getProvince();//省信息
                    String city = amapLocation.getCity();//城市信息
                    String district = amapLocation.getDistrict();//城区信息
                    String street = amapLocation.getStreet();//街道信息
                    String streetNum = amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    String buildingId = amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    String floor = amapLocation.getFloor();//获取当前室内定位的楼层
                    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

//                    System.out.println("高德定位结果: " + country + " , " + province + " , " + city + " , " + district + " , " + street + " , " + address + " , " +
//                            buildingId + " , " + floor);
                    fragmentHomeQuanZi.setCityName(city);
                    SPUtils.getInstance().saveLocation(BaseApplication.applicationContext, province, city);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + amapLocation.getErrorCode() + ", errInfo:"
//                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentArrayList.get(0).isAdded())
            handleResult(fragmentArrayList.get(0), requestCode, resultCode, data);
        if (fragmentArrayList.get(1).isAdded())
            handleResult(fragmentArrayList.get(1), requestCode, resultCode, data);
        if (fragmentArrayList.get(4).isAdded())
            handleResult(fragmentArrayList.get(4), requestCode, resultCode, data);
//        for(int i=0;i<fragmentArrayList.size();i++){
//            handleResult(fragmentArrayList.get(i), requestCode, resultCode, data);
//        }
    }

    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
    }


}
