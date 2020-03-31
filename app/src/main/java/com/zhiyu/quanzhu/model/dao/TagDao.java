package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.Tag;

import org.xutils.db.sqlite.WhereBuilder;

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

    public void saveTags(List<Tag> list) {
        clearTags();
        if (null != list && list.size() > 0) {
            for (Tag tag : list) {
                try {
                    Tag t = selectTagById(tag.getId());
                    if (null == t)
                        BaseApplication.db.saveBindingId(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveTag(Tag tag) {
        try {
            Tag t = selectTagById(tag.getId());
            if (null == t)
                BaseApplication.db.saveBindingId(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTag(Tag tag) {
        try {
            BaseApplication.db.delete(Tag.class, WhereBuilder.b("id", "=", tag.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tag selectTagById(int id) {
        Tag tag = null;
        try {
            tag = BaseApplication.db.selector(Tag.class).where(WhereBuilder.b("id", "=", id)).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tag;
    }

    public Tag selectTagByName(String name) {
        Tag tag = null;
        try {
            tag = BaseApplication.db.selector(Tag.class).where(WhereBuilder.b("name", "=", name)).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tag;
    }


    public List<Tag> tagList() {
        List<Tag> list = null;
        try {
            list = BaseApplication.db.findAll(Tag.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void clearTags() {
        try {
            BaseApplication.db.delete(Tag.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
