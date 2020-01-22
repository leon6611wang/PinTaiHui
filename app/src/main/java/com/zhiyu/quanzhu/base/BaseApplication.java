package com.zhiyu.quanzhu.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.orm.SugarContext;
import com.zhiyu.quanzhu.ui.widget.rongfrend.FrendMessage;
import com.zhiyu.quanzhu.ui.widget.rongmingpian.MingPianMessage;
import com.zhiyu.quanzhu.ui.widget.rongmingpian.MingPianMessageItemProvider;
import com.zhiyu.quanzhu.ui.widget.rongorder.OrderMessage;
import com.zhiyu.quanzhu.ui.widget.rongorder.OrderMessageItemProvider;
import com.zhiyu.quanzhu.ui.widget.rongplugins.MyExtensionModule;
import com.zhiyu.quanzhu.ui.widget.rongshare.ShareMessage;
import com.zhiyu.quanzhu.ui.widget.rongshare.ShareMessageItemProvider;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;


public class BaseApplication extends Application implements BaseActivity.OnExternalStorageListener {
    public static DbManager db = null;
    public static Context applicationContext;
    private MediaPlayer mediaPlayer;
    private static BaseApplication app;

    public static BaseApplication getInstance() {
        if (null == app) {
            synchronized (BaseApplication.class) {
                app = new BaseApplication();
            }
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        x.Ext.init(this);                   //xUtils 初始化
        x.Ext.setDebug(true);               //xUtils日志输出
        BaseActivity.setOnExternalStorageListener(this);

        if (getApplicationInfo().packageName
                .equals(getCurProcessName(getApplicationContext()))
                || "io.rong.push"
                .equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            setInputProvider();
            //名片消息
            RongIM.registerMessageType(MingPianMessage.class);
            RongIM.registerMessageTemplate(new MingPianMessageItemProvider(this));
            //订单消息
            RongIM.registerMessageType(OrderMessage.class);
            RongIM.registerMessageTemplate(new OrderMessageItemProvider(this));

            //分享消息
            RongIM.registerMessageType(ShareMessage.class);
            RongIM.registerMessageTemplate(new ShareMessageItemProvider(this));
            //名片好友消息
            RongIM.registerMessageType(FrendMessage.class);
        }
        imHeaderListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }


    public static void initXUtils() {
        if (null == db) {
            DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                    //设置数据库名，默认xutils.db
                    .setDbName("quanzhu.db")
                    // 不设置dbDir时, 默认存储在app的私有目录.
//                    .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                    .setDbVersion(1)//数据库版本
                    //设置是否允许事务，默认true
                    //.setAllowTransaction(true)
                    //设置表创建的监听
                    .setTableCreateListener(new DbManager.TableCreateListener() {

                        @Override
                        public void onTableCreated(DbManager db, TableEntity<?> table) {
//                            System.out.println("table name: " + table.getName());
                        }
                    })

                    //设置数据库更新的监听
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    })
                    //设置数据库打开的监听
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            //开启数据库支持多线程操作，提升性能
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    });
            db = x.getDb(daoConfig);
        }
    }

    @Override
    public void onExternalStorage() {
        if (null == db) {
//            System.out.println("application initXUtils");
            SugarContext.init(this);
            initXUtils();
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private void imHeaderListener() {
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                //跳转个人主页
                System.out.println("1头像点击 " + userInfo.getName());
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                System.out.println("onMessageClick");
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
    }

    private void setInputProvider() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }

    public static RongIMClient.OnReceiveMessageListener receiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            System.out.println("---- rong message type: " + message.getObjectName());
            return true;
        }
    };


    public MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

}