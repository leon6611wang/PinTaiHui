package com.zhiyu.quanzhu.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.zhiyu.quanzhu.model.bean.AppMarket;

import java.util.ArrayList;
import java.util.List;

public class AppUpdateUtils {
    private static AppUpdateUtils utils;
    private List<AppMarket> marketList = new ArrayList<>();

    public static AppUpdateUtils getInstance() {
        if (null == utils) {
            synchronized (AppUpdateUtils.class) {
                utils = new AppUpdateUtils();
            }
        }
        return utils;
    }

    public void updateApp(Context context, String url) {
        if (marketList.size() > 0) {
            marketList.clear();
        }
        marketList.add(new AppMarket("Google Play", "com.android.vending"));
        marketList.add(new AppMarket("应用宝", "com.tencent.android.qqdownloader"));
        marketList.add(new AppMarket("360手机助手", "com.qihoo.appstore"));
        marketList.add(new AppMarket("百度手机助", "com.baidu.appsearch"));
        marketList.add(new AppMarket("小米应用商店", "com.xiaomi.market"));
        marketList.add(new AppMarket("豌豆荚", "com.wandoujia.phoenix2"));
        marketList.add(new AppMarket("华为应用市场", "com.huawei.appmarket"));
        marketList.add(new AppMarket("OPPO应用商店", "com.oppo.market"));
        marketList.add(new AppMarket("VIVO应用商店", "com.bbk.appstore"));
        marketList.add(new AppMarket("联想应用商店", "com.lenovo.leos.appstore"));
        marketList.add(new AppMarket("魅族应用市场", "com.meizu.mstore"));
        marketList.add(new AppMarket("三星应用商店", "com.sec.android.app.samsungapps"));
        marketList.add(new AppMarket("中兴应用商店", "zte.com.market"));
        marketList.add(new AppMarket("酷派应用商店", "com.yulong.android.coolmart"));
        boolean isExit = false;
        for (AppMarket market : marketList) {
            isExit = isAvilible(context, market.getPackageName());
            if (isExit) {
                System.out.println(market);
                launchAppDetail(context, "com.zhiyu.quanzhu", market.getPackageName());
            }
        }
        if (!isExit) {
            downloadApk(context, url);
        }
    }

    public void appraiseApp(Context context) {
        if (marketList.size() > 0) {
            marketList.clear();
        }
        marketList.add(new AppMarket("Google Play", "com.android.vending"));
        marketList.add(new AppMarket("应用宝", "com.tencent.android.qqdownloader"));
        marketList.add(new AppMarket("360手机助手", "com.qihoo.appstore"));
        marketList.add(new AppMarket("百度手机助", "com.baidu.appsearch"));
        marketList.add(new AppMarket("小米应用商店", "com.xiaomi.market"));
        marketList.add(new AppMarket("豌豆荚", "com.wandoujia.phoenix2"));
        marketList.add(new AppMarket("华为应用市场", "com.huawei.appmarket"));
        marketList.add(new AppMarket("OPPO应用商店", "com.oppo.market"));
        marketList.add(new AppMarket("VIVO应用商店", "com.bbk.appstore"));
        marketList.add(new AppMarket("联想应用商店", "com.lenovo.leos.appstore"));
        marketList.add(new AppMarket("魅族应用市场", "com.meizu.mstore"));
        marketList.add(new AppMarket("三星应用商店", "com.sec.android.app.samsungapps"));
        marketList.add(new AppMarket("中兴应用商店", "zte.com.market"));
        marketList.add(new AppMarket("酷派应用商店", "com.yulong.android.coolmart"));
        boolean isExit = false;
        for (AppMarket market : marketList) {
            isExit = isAvilible(context, market.getPackageName());
            if (isExit) {
                System.out.println(market);
                launchAppDetail(context, "com.zhiyu.quanzhu", market.getPackageName());
            }
        }
    }

    /**
     * 判断应用市场是否存在的方法
     *
     * @param context
     * @param packageName 主流应用商店对应的包名
     *                    com.android.vending    -----Google Play
     *                    com.tencent.android.qqdownloader     -----应用宝
     *                    com.qihoo.appstore    -----360手机助手
     *                    com.baidu.appsearch    -----百度手机助
     *                    com.xiaomi.market    -----小米应用商店
     *                    com.wandoujia.phoenix2    -----豌豆荚
     *                    com.huawei.appmarket    -----华为应用市场
     *                    com.taobao.appcenter    -----淘宝手机助手
     *                    com.hiapk.marketpho    -----安卓市场
     *                    cn.goapk.market        -----安智市场
     */
    private boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> pName = new ArrayList<String>();
        // 从pinfo中将包名字取出
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pf = pinfo.get(i).packageName;
                pName.add(pf);
            }
        }
        // 判断pName中是否有目标程序的包名，有true，没有false
        return pName.contains(packageName);
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面
     */
    private void launchAppDetail(Context mContext, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                return;
            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadApk(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
