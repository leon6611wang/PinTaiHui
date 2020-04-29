package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.PageData;

/**
 * 页面数据持久化工具类
 */
public class PageDataDao {
    private static PageDataDao dao;

    public static PageDataDao getDao() {
        if (null == dao) {
            synchronized (PageDataDao.class) {
                dao = new PageDataDao();
            }
        }
        return dao;
    }

    public void savePageData(PageData pageData){
        try {
            BaseApplication.db.saveBindingId(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PageData getPageData(String pageName,String dataName){
        PageData pageData=null;
        try {
            pageData=BaseApplication.db.selector(PageData.class).where("page_name","=",pageName).findFirst();
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageData;
    }
}
