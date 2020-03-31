package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.HobbyDaoChild;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;

import java.util.List;

public class HobbyDao {
    private static HobbyDao dao;

    public static HobbyDao getInstance() {
        if (null == dao) {
            synchronized (HobbyDao.class) {
                dao = new HobbyDao();
            }
        }
        return dao;
    }

    public void saveHobbyParentList(List<HobbyDaoParent> list) {
        if (null != list && list.size() > 0) {
            for (HobbyDaoParent hobbyParent : list) {
                try {
                    BaseApplication.db.saveBindingId(hobbyParent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveHobbyChildList(List<HobbyDaoChild> list) {
        if (null != list && list.size() > 0) {
            for (HobbyDaoChild hobbyDaoChild : list) {
                try {
                    BaseApplication.db.saveBindingId(hobbyDaoChild);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<HobbyDaoParent> hobbyParentList() {
        List<HobbyDaoParent> list = null;
        try {
            list = BaseApplication.db.findAll(HobbyDaoParent.class);
            if (null != list && list.size() > 0) {
                for (HobbyDaoParent parent : list) {
                    System.out.println("hobbyParent: " + parent.getName()+" ----> ");
                    hobbyChildList(parent.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HobbyDaoChild> hobbyChildList(int pid) {
        List<HobbyDaoChild> list = null;
        try {
            list = BaseApplication.db.selector(HobbyDaoChild.class).where("pid", "=", String.valueOf(pid)).findAll();
            if (null != list && list.size() > 0) {
                for (HobbyDaoChild child : list) {
                    System.out.println("hobbyChild: " + child.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HobbyDaoChild> hobbyChildList(String parent_name) {
        List<HobbyDaoChild> list = null;
        try {
            list = BaseApplication.db.selector(HobbyDaoChild.class).where("sub_name", "=", parent_name).findAll();
            if (null != list && list.size() > 0) {
                for (HobbyDaoChild child : list) {
                    System.out.println("hobbyChild: " + child.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HobbyDaoParent getHobbyParent(String name){
        HobbyDaoParent parent=null;
        try {
            parent=BaseApplication.db.selector(HobbyDaoParent.class).where("name","=",name).findFirst();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  parent;
    }

    public HobbyDaoChild getHobbyChild(String name){
        HobbyDaoChild child=null;
        try {
            child=BaseApplication.db.selector(HobbyDaoChild.class).where("name","=",name).findFirst();
        }catch (Exception e){
            e.printStackTrace();
        }
        return child;
    }

    private void clearHobbyParent(){
        try {
            BaseApplication.db.delete(HobbyDaoParent.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearHobbyChild(){
        try {
            BaseApplication.db.delete(HobbyDaoChild.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clearHobby(){
        clearHobbyParent();
        clearHobbyChild();
    }

}
