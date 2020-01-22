package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.Tag;

import java.util.List;

public class TagDao {
    private static TagDao dao;

    public static TagDao getInstance() {
        if (null == dao) {
            synchronized (TagDao.class) {
                dao = new TagDao();
            }
        }
        return dao;
    }

    public void saveTags(List<Tag> list){
        if(null!=list&& list.size()>0){
            for(Tag tag:list){
                try {
                    BaseApplication.db.saveBindingId(tag);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Tag> tagList(){
        List<Tag> list=null;
        try {
            list=BaseApplication.db.findAll(Tag.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public void clearTags(){
        try {
            BaseApplication.db.delete(Tag.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
