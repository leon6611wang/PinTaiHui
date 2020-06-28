package com.leon.chic.utils;

import android.app.Application;
import android.content.Context;

import com.leon.chic.model.Province;

public class CityUtils {

    private static CityUtils utils;

    public static CityUtils getInstance() {
        if (null == utils) {
            synchronized (CityUtils.class) {
                utils = new CityUtils();
            }
        }
        return utils;
    }

    public void saveCitys(Application app, String province_name, int province_code, String citys) {
        Province province = new Province();
        province.setCitys(citys);
        province.setCode(province_code);
        province.setName(province_name);
        try {
            DaoUtils.getInstance(app).getDb().saveBindingId(province);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCitys(Application app, int province_code) {
        String json = null;
        try {
            Province province = DaoUtils.getInstance(app).getDb().selector(Province.class).where("code", "=", province_code).findFirst();
            if (null != province) {
                json = province.getCitys();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
