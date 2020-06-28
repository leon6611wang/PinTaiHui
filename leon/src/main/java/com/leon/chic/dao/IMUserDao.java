package com.leon.chic.dao;

import android.app.Application;

import com.leon.chic.model.IMUser;
import com.leon.chic.model.IMUserData;
import com.leon.chic.model.IMUserResult;
import com.leon.chic.utils.DaoUtils;
import com.leon.chic.utils.GsonUtils;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;

public class IMUserDao {
    private static IMUserDao dao;

    public static IMUserDao getInstance() {
        if (null == dao) {
            synchronized (IMUserDao.class) {
                dao = new IMUserDao();
            }
        }
        return dao;
    }

    public void saveIMUserList(String s, Application app) {
        clearIMUser(app);
        try {
            IMUserResult imUserResult = GsonUtils.GsonToBean(s, IMUserResult.class);
            if (null != imUserResult && null != imUserResult.getData() && null != imUserResult.getData().getUserinfo() && imUserResult.getData().getUserinfo().size() > 0) {
                for (IMUser user : imUserResult.getData().getUserinfo()) {
                    saveIMUser(user, app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveIMUserProfile(String s, Application app) {
        try {
            IMUserResult imUserResult = GsonUtils.GsonToBean(s, IMUserResult.class);
            if (null != imUserResult && null != imUserResult.getData() && null != imUserResult.getData().getUserinfo() && imUserResult.getData().getUserinfo().size() > 0) {
                for (IMUser user : imUserResult.getData().getUserinfo()) {
                    saveIMUser(user, app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearIMUser(Application app) {
        try {
            DaoUtils.getInstance(app).getDb().dropTable(IMUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveIMUser(IMUser user, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().saveBindingId(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteIMUser(int id, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().delete(IMUser.class, WhereBuilder.b("id", "=", String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IMUser selectIMUser(String id, Application app) {
        IMUser user = null;
        try {
            user = DaoUtils.getInstance(app).getDb().selector(IMUser.class).where("id", "=", id).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void getUserNameAvatar(String id, Application app, OnUserNameAvatarListener listener) {
        IMUser user = null;
        try {
            user = DaoUtils.getInstance(app).getDb().selector(IMUser.class).where("id", "=", id).findFirst();
            if (null != listener && null != user) {
                listener.onNameAvatar(user.getUsername(), user.getAvatar());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnUserNameAvatarListener {
        void onNameAvatar(String name, String avatar);
    }


    public void updateIMUser(String s, Application app) {
        try {
            IMUser u = GsonUtils.GsonToBean(s, IMUser.class);
            IMUser user = selectIMUser(String.valueOf(u.getId()), app);
            if (null != user) {
                deleteIMUser(user.getId(), app);
            }
            saveIMUser(u, app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIMUserList(Application app) {
        String json = null;
        try {
            List<IMUser> list = DaoUtils.getInstance(app).getDb().selector(IMUser.class).findAll();
            IMUserData data = new IMUserData();
            data.setUserinfo(list);
            IMUserResult result = new IMUserResult();
            result.setData(data);
            json = GsonUtils.GsonString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
