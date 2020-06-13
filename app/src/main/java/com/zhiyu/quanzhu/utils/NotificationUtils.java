package com.zhiyu.quanzhu.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.leon.chic.utils.MessageTypeUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.ui.activity.CustomerServiceActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanZhuAssistantActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiTouSuFanKuiActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiGuanZhuDianPuActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiKaQuanTongZhiActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanYouShenHeActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiQuanZiShenHeActivity;
import com.zhiyu.quanzhu.ui.activity.XiTongXiaoXiZhiFuTongZhiActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {
    private static NotificationUtils utils;

    public static NotificationUtils getInstance() {
        if (null == utils) {
            synchronized (NotificationUtils.class) {
                utils = new NotificationUtils();
            }
        }
        return utils;
    }


    public void showServiceMessage(int shop_id, String userName, String message) {
        NotificationManager notifcationManage = (NotificationManager) BaseApplication.applicationContext.getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(BaseApplication.applicationContext, CustomerServiceActivity.class);
        intent.putExtra("shop_id", shop_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        }
        NotificationCompat.Builder build = new NotificationCompat.Builder(BaseApplication.applicationContext, "default");
        build.setContentTitle(userName);//设置通知栏标题
        build.setContentText(message); //设置通知栏显示内容
        //build.setNumber(3); //设置通知集合的数量
        build.setTicker("新消息通知"); //通知首次出现在通知栏，带上升动画效果的
        build.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        build.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        build.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消
        build.setOngoing(true);//true，设置他为一个正在进行的通知,通常是用来表示一个后台任务,以某种方式正在等待,如一个文件下载,同步操作
        build.setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果
        build.setSmallIcon(R.mipmap.logo);//设置通知小ICON
        build.setLargeIcon(BitmapFactory.decodeResource(BaseApplication.applicationContext.getResources(), R.mipmap.logo));
        build.setContentIntent(pendingIntent); //设置通知栏点击意图
        Notification mNotification = build.build();
        notifcationManage.notify(id, mNotification);
    }


    public void showSystemMessage(int type, String title, String message) {
        NotificationManager notifcationManage = (NotificationManager) BaseApplication.applicationContext.getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        Intent intent = null;
        switch (type) {
            case MessageTypeUtils.XIAO_MI_SHU:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiQuanZhuAssistantActivity.class);
                break;
            case MessageTypeUtils.QUAN_YOU_QING_QIU:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiQuanYouShenHeActivity.class);
                break;
            case MessageTypeUtils.QUAN_ZI_SHEN_HE:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiQuanZiShenHeActivity.class);
                break;
            case MessageTypeUtils.FU_KUAN_TONG_ZHI:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiZhiFuTongZhiActivity.class);
                break;
            case MessageTypeUtils.KA_QUAN_TONG_ZHI:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiKaQuanTongZhiActivity.class);
                break;
            case MessageTypeUtils.GUAN_ZHU_DIAN_PU:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiGuanZhuDianPuActivity.class);
                break;
            case MessageTypeUtils.TOU_SU_FAN_KUI:
                intent = new Intent(BaseApplication.applicationContext, XiTongXiaoXiTouSuFanKuiActivity.class);
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        }
        NotificationCompat.Builder build = new NotificationCompat.Builder(BaseApplication.applicationContext, "default");
        build.setContentTitle(title);//设置通知栏标题
        build.setContentText(message); //设置通知栏显示内容
        //build.setNumber(3); //设置通知集合的数量
        build.setTicker("新消息通知"); //通知首次出现在通知栏，带上升动画效果的
        build.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
        build.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        build.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消
        build.setOngoing(true);//true，设置他为一个正在进行的通知,通常是用来表示一个后台任务,以某种方式正在等待,如一个文件下载,同步操作
        build.setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果
        build.setSmallIcon(R.mipmap.logo);//设置通知小ICON
        build.setLargeIcon(BitmapFactory.decodeResource(BaseApplication.applicationContext.getResources(), R.mipmap.logo));
        build.setContentIntent(pendingIntent); //设置通知栏点击意图
        Notification mNotification = build.build();
        notifcationManage.notify(id, mNotification);
    }
}
