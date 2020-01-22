package com.zhiyu.quanzhu.model.dao;

import android.util.Log;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.SearchHistory;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

public class SearchHistoryDao {
    private static SearchHistoryDao dao;

    public static SearchHistoryDao getInstance() {
        if (null == dao) {
            synchronized (SearchHistory.class) {
                dao = new SearchHistoryDao();
            }
        }
        return dao;
    }

    public void add(SearchHistory history) {
        try {
            SearchHistory searchHistory=BaseApplication.db.selector(SearchHistory.class).where(WhereBuilder.b("name","=",history.getName())).findFirst();
            if(null!=searchHistory){
                Log.i("tagSelect","已有记录，不做dao");
            }else{
                Log.i("tagSelect","没有记录，dao");
                BaseApplication.db.saveBindingId(history);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            BaseApplication.db.delete(SearchHistory.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SearchHistory> queryAll() {
        List<SearchHistory> list = null;
        try {
            list = BaseApplication.db.selector(SearchHistory.class).orderBy("time", true).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
