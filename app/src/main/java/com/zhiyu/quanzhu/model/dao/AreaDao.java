package com.zhiyu.quanzhu.model.dao;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;

import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * 地区持久化
 */
public class AreaDao {
    private static AreaDao dao;

    public static AreaDao getInstance() {
        if (null == dao) {
            synchronized (AreaDao.class) {
                dao = new AreaDao();
            }
        }
        return dao;
    }

    public void saveAreaProvince(List<AreaProvince> list) {
        if (null != list && list.size() > 0) {
            clearAreaProvince();
            for (AreaProvince p : list) {
                try {
                    BaseApplication.db.saveBindingId(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveAreaCity(List<AreaCity> list) {
        if (null != list && list.size() > 0) {
            clearAreaCity();
            for (AreaCity c : list) {
                try {
                    BaseApplication.db.saveBindingId(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<AreaProvince> provinceList() {
        List<AreaProvince> list = null;
        try {
            list = BaseApplication.db.findAll(AreaProvince.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AreaCity> cityList(int province_id) {
        List<AreaCity> list = null;
        try {
            list = BaseApplication.db.selector(AreaCity.class).where("pcode", "=", String.valueOf(province_id)).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void clearAreaProvince() {
        try {
            BaseApplication.db.delete(AreaProvince.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAreaCity() {
        try {
            BaseApplication.db.delete(AreaCity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AreaProvince getAreaProvince(String province_name) {
        AreaProvince province = null;
        try {
            province = BaseApplication.db.selector(AreaProvince.class).where("name", "=", province_name).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return province;
    }

    public AreaCity getAreaCity(String city_name) {
        AreaCity city = null;
        try {
            city = BaseApplication.db.selector(AreaCity.class).where("name", "=", city_name).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

}
