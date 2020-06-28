package com.leon.chic.dao;

import android.app.Application;
import android.util.Log;

import com.leon.chic.model.PageData;
import com.leon.chic.utils.DaoUtils;

import org.xutils.db.sqlite.WhereBuilder;


public class PageDao {
    private final String TAG = "PageDao";
    private static PageDao dao;

    public static PageDao getInstance() {
        if (null == dao) {
            synchronized (PageDao.class) {
                dao = new PageDao();
            }
        }
        return dao;
    }

    public void save(Class cls, String s2, Application app) {
        PageData data = select(cls.getName(), app);
        if (null != data) {
            delete(cls.getName(), app);
        }
        PageData pageData = new PageData();
        pageData.setPageName(cls.getName());
        pageData.setPageData(s2);
        try {
            DaoUtils.getInstance(app).getDb().saveBindingId(pageData);
            Log.i(TAG, cls.getName() + "保存完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PageData select(String pageName, Application app) {
        PageData pageData = null;
        try {
            pageData = DaoUtils.getInstance(app).getDb().selector(PageData.class).where("page_name", "=", pageName).findFirst();
            if (null != pageData) {
                Log.i(TAG, pageName + ": " + pageData.getPageData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageData;
    }

    public void clear(Application app) {
        try {
            DaoUtils.getInstance(app).getDb().dropTable(PageData.class);
            Log.i(TAG, "页面缓存已清空.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(String pageName, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().delete(PageData.class, WhereBuilder.b("page_name", "=", pageName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(Class cls, Application app) {
        String result = null;
        try {
            PageData pageData = select(cls.getName(), app);
            if (null != pageData) {
                result = pageData.getPageData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
