package com.zhiyu.quanzhu.model.dao;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

/**
 * 全局搜索-本地历史记录
 */
public class FullSearchHistoryDao {
    private static FullSearchHistoryDao dao;

    public static FullSearchHistoryDao getDao() {
        if (null == dao) {
            synchronized (FullSearchHistoryDao.class) {
                dao = new FullSearchHistoryDao();
            }
        }
        return dao;
    }

    public void saveFullSearchHistory(FullSearchHistory history) {
        try {
            deleteFullSearchHistory(history);
            BaseApplication.db.saveBindingId(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFullSearchHistory(FullSearchHistory history) {
        try {
            BaseApplication.db.delete(FullSearchHistory.class, WhereBuilder.b("history", "=", history.getHistory()).and("user_id", "=", SPUtils.getInstance().getUserId(BaseApplication.applicationContext)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean queryFullSearchHistory(FullSearchHistory history) {
        boolean isHas = false;
        try {
            BaseApplication.db.selector(FullSearchHistory.class).where("history", "=", history.getHistory()).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isHas;
    }

    public List<FullSearchHistory> fullSearchHistoryList() {
        List<FullSearchHistory> list = null;
        try {
            list = BaseApplication.db.selector(FullSearchHistory.class).where("user_id", "=", SPUtils.getInstance().getUserId(BaseApplication.applicationContext)).orderBy("time", true).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void clearFullSearchHistory() {
        try {
            BaseApplication.db.delete(FullSearchHistory.class, WhereBuilder.b("user_id", "=", SPUtils.getInstance().getUserId(BaseApplication.applicationContext)));
//            BaseApplication.db.dropTable(FullSearchHistory.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
