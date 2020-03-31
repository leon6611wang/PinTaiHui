package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.HomeCache;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.Date;

/**
 * 首页数据缓存持久化操作类
 */
public class HomeCacheDao {
    private static HomeCacheDao dao;

    public static HomeCacheDao getDao() {
        if (null == dao) {
            synchronized (HomeCacheDao.class) {
                dao = new HomeCacheDao();
            }
        }
        return dao;
    }

    public void saveHomeCache(String content, String pageName) {
        try {
            deleteHomeCache(pageName);
            BaseApplication.db.saveBindingId(new HomeCache(pageName, content, new Date().getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String queryHomeCache(String pageName) {
        String content = null;
        try {
            BaseApplication.db.selector(HomeCache.class).where("page_name", "=", pageName).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private void deleteHomeCache(String pageName) {
        try {
            BaseApplication.db.delete(HomeCache.class, WhereBuilder.b("page_name", "=", pageName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
