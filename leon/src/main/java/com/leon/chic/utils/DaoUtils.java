package com.leon.chic.utils;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

public class DaoUtils {
    private static DbManager db = null;
    public static DaoUtils utils;

    public static DaoUtils getInstance(Application application) {
        if (null == utils) {
            synchronized (DaoUtils.class) {
                utils = new DaoUtils();
                x.Ext.init(application);
                x.Ext.setDebug(true);
                initXUtils();
            }
        }
        return utils;
    }

    private static void initXUtils() {
        if (null == db) {
            DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                    //设置数据库名，默认xutils.db
                    .setDbName("leon.db")
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

    public DbManager getDb(){
        return db;
    }
}
