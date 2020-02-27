package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
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
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.dao.ConversationDao;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.AppVersionResult;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.model.result.HobbyDaoResult;
import com.zhiyu.quanzhu.model.result.IndustryResult;
import com.zhiyu.quanzhu.model.result.UserResult;
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
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.IMUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;

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
                case 1:
                    String version = AppUtils.getInstance().getVersionName(activity);
                    System.out.println("versionName: " + version);
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
                case 2://基础信息
                    if (activity.userResult.getData().getUser().getPwdstatus() == 0) {
                        //设置密码

                    } else {
                        if (activity.userResult.getData().getUser().getMobilestatus() == 0) {
                            //绑定手机

                        } else {
                            if (activity.userResult.getData().getUser().getInfostatus() == 0) {
                                //完善资料

                            } else {
                                if (activity.userResult.getData().getUser().getIndustrystatus() == 0) {
                                    //偏好选择

                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Vitamio.isInitialized(getApplication());
//        SharedPreferencesUtils.getInstance(this).clearUser();
        // 检测Vitamio是否解压解码包
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        initConversationData();
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
        imConnect();
        initDialogs();
        cityList();
        industryList();
        hobbyList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mLocationClient) {
            initLocation();
        }
        //启动定位
        mLocationClient.startLocation();
        requestAppVersion();
        if (checkLogin()) {
            requestBase();
        }
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

    private void initDatas() {
        FragmentHomeQuanZi fragmentHomeQuanZi = new FragmentHomeQuanZi();
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
                if (checkLogin())
                    barChange(1);
                break;
            case R.id.xiaoxilayout:
                if (checkLogin())
                    barChange(2);
                break;
            case R.id.quanshanglayout:
                barChange(3);
                break;
            case R.id.wodelayout:
                if (checkLogin())
                    barChange(4);
                break;
        }
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
        RongIM.connect(IMUtils.TOKEN_9527, new RongIMClient.ConnectCallbackEx() {
            @Override
            public void OnDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                System.out.println("OnDatabaseOpened: " + code.toString());
            }

            @Override
            public void onTokenIncorrect() {
                System.out.println("onTokenIncorrect: ");
            }

            @Override
            public void onSuccess(String s) {
                System.out.println("登录融云服务器成功，当前用户id: " + s);
                RongIM.setOnReceiveMessageListener(BaseApplication.receiveMessageListener);
                SharedPreferencesUtils.getInstance(HomeActivity.this).saveUser("97", "一号测试", "http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190518/d38fda99a9654dd2b5b60950a1cb9967.jpeg", "18768100516");
                updateUserInfo();
//                sendMsg();
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                System.out.println("onError: " + e.toString());
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
                System.out.println("onError: " + arg0.toString());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String s) {
                System.out.println("token: " + s);
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
        UserInfo userInfo = new UserInfo(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId(),
                ConversationDao.getDao(this).selectById(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId()).getUser_name(),
                Uri.parse(ConversationDao.getDao(this).selectById(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId()).getHeader_pic()));
        RongIM.getInstance().setCurrentUserInfo(userInfo);
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId(), ConversationDao.getDao(this).selectById(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId()).getUser_name(),
                Uri.parse(ConversationDao.getDao(this).selectById(SharedPreferencesUtils.getInstance(HomeActivity.this).getUserId()).getHeader_pic())));
    }

    private boolean checkLogin() {
        String user_token = SharedPreferencesUtils.getInstance(this).getUserToken();
        if (TextUtils.isEmpty(user_token)) {
            Intent loginIntent = new Intent(this, LoginGetVertifyCodeActivity.class);
            startActivity(loginIntent);
        }
        System.out.println("是否登录: " + (!TextUtils.isEmpty(user_token)));
        return !TextUtils.isEmpty(user_token);
    }

    private UserResult userResult;

    private void requestBase() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.APP_BASE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("基础信息: " +result);
                userResult = GsonUtils.GsonToBean(result, UserResult.class);
                android.os.Message message = myHandler.obtainMessage(2);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("基础信息 error: " + ex.toString());
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
                System.out.println("app version: " + appVersionResult.getData().getAndroid().getVersion());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("app version error:" + ex.toString());
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

                    System.out.println("高德定位结果: " + country + " , " + province + " , " + city + " , " + district + " , " + street + " , " + address + " , " +
                            buildingId + " , " + floor);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private AreaResult areaResult;

    /**
     * 地区列表
     */
    private void cityList() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CITYS);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("area: " + result);
                areaResult = GsonUtils.GsonToBean(result, AreaResult.class);
                AreaDao.getInstance().saveAreaProvince(areaResult.getData().getCitys());
                for (final AreaProvince p : areaResult.getData().getCitys()) {
                    ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                        @Override
                        public void run() {
                            AreaDao.getInstance().saveAreaCity(p.getChild());
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private IndustryResult industryResult;

    /**
     * 行业列表
     */
    private void industryList() {
        final RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("industry: " + result);
                industryResult = GsonUtils.GsonToBean(result, IndustryResult.class);
                if (null != industryResult) {
                    IndustryDao.getInstance().saveIndustryParent(industryResult.getData().getList().get(0).getChild());
                    for (final IndustryParent parent : industryResult.getData().getList().get(0).getChild()) {
                        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                            @Override
                            public void run() {
                                IndustryDao.getInstance().saveIndustryChild(parent.getChild());
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private HobbyDaoResult hobbyResult;

    private void hobbyList() {
        final RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("hobby: " + result);
                hobbyResult = GsonUtils.GsonToBean(result, HobbyDaoResult.class);
                HobbyDao.getInstance().saveHobbyParentList(hobbyResult.getData().getList().get(0).getChild());

                for (HobbyDaoParent parent : hobbyResult.getData().getList().get(0).getChild()) {
                    HobbyDao.getInstance().saveHobbyChildList(parent.getChild());
                    System.out.println(parent.getChild().size());
//                    for (HobbyDaoChild child : parent.getChild()) {
//                        System.out.println("parent: " + child.getSub_name() + " , child: " + child.getName());
//                    }
                }

                HobbyDao.getInstance().hobbyParentList();

//                industryResult = GsonUtils.GsonToBean(result, IndustryResult.class);
//                if (null != industryResult) {
//                    IndustryDao.getInstance().saveIndustryParent(industryResult.getData().getList().get(0).getChild());
//                    for (final IndustryParent parent : industryResult.getData().getList().get(0).getChild()) {
//                        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                IndustryDao.getInstance().saveIndustryChild(parent.getChild());
//                            }
//                        });
//                    }
//                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
