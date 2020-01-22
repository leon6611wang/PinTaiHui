package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;

import java.util.List;

/**
 * 行业数据持久化
 */
public class IndustryDao {
    private static IndustryDao dao;

    public static IndustryDao getInstance() {
        if (null == dao) {
            synchronized (IndustryDao.class) {
                dao = new IndustryDao();
            }
        }
        return dao;
    }

    private void clearIndustryParent() {
        try {
            BaseApplication.db.delete(IndustryParent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearIndustryChild() {
        try {
            BaseApplication.db.delete(IndustryChild.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveIndustryParent(List<IndustryParent> list) {
        if (null != list && list.size() > 0) {
            clearIndustryParent();
            for (IndustryParent parent : list) {
                try {
                    BaseApplication.db.saveBindingId(parent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveIndustryChild(List<IndustryChild> list) {
        if (null != list && list.size() > 0) {
            clearIndustryChild();
            for (IndustryChild child : list) {
                try {
                    BaseApplication.db.saveBindingId(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<IndustryParent> industryParentList() {
        List<IndustryParent> list = null;
        try {
            list = BaseApplication.db.findAll(IndustryParent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<IndustryChild> industryChildList(int pid) {
        System.out.println("pid: " + pid);
        List<IndustryChild> list = null;
        try {
            list = BaseApplication.db.selector(IndustryChild.class).where("pid", "=", String.valueOf(pid)).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public IndustryParent industryParent(String parent_name) {
        IndustryParent parent = null;
        try {
            parent = BaseApplication.db.selector(IndustryParent.class).where("name", "=", parent_name).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parent;
    }

    public IndustryChild industryChild(String child_name) {
        IndustryChild child = null;
        try {
            child = BaseApplication.db.selector(IndustryChild.class).where("name", "=", child_name).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return child;
    }

}
