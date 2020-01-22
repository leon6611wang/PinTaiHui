package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class ProvinceCityDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private LoopView provinceView, cityView;
    private List<String> provinceList = new ArrayList<>();
    private List<AreaProvince> areaProvinceList;
    private List<AreaCity> areaCityList;
    private List<String> cityList = new ArrayList<>();
    private String province, city;
    private TextView cancelTextView, confirmTextView;
    private AreaProvince areaProvince;
    private AreaCity areaCity;

    public ProvinceCityDialog(@NonNull Context context, int themeResId, OnCityChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCityChooseListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_province_city);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        areaProvinceList = AreaDao.getInstance().provinceList();
        if (null != areaProvinceList && areaProvinceList.size() > 0) {
            areaProvince=areaProvinceList.get(0);
            for (AreaProvince p : areaProvinceList) {
                provinceList.add(p.getName());
            }
        }

        areaCityList = AreaDao.getInstance().cityList(areaProvinceList.get(0).getCode());
        if (null != areaCityList && areaCityList.size() > 0) {
            areaCity=areaCityList.get(0);
            for (AreaCity c : areaCityList) {
                cityList.add(c.getName());
            }
        }
    }

    private void initViews() {
        provinceView = findViewById(R.id.provinceView);
        provinceView.setNotLoop();
        cityView = findViewById(R.id.cityView);
        cityView.setNotLoop();
        provinceView.setItems(provinceList);
        provinceView.setInitPosition(0);
        provinceView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(provinceList.get(index))) {
                    province = provinceList.get(index);
                    areaProvince = AreaDao.getInstance().getAreaProvince(province);
                    if (null != areaCityList) {
                        areaCityList.clear();
                    }
                    if (null != cityList) {
                        cityList.clear();
                    }
                    areaCityList = AreaDao.getInstance().cityList(areaProvinceList.get(index).getCode());
                    if (null != areaCityList && areaCityList.size() > 0) {
                        for (AreaCity c : areaCityList) {
                            cityList.add(c.getName());
                        }
                    }
                    cityView.setItems(cityList);
                }
            }
        });
        province = provinceList.get(provinceView.getSelectedItem());
        cityView.setItems(cityList);
        cityView.setInitPosition(0);
        cityView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(cityList.get(index))) {
                    city = cityList.get(index);
                    areaCity = AreaDao.getInstance().getAreaCity(city);
                }
            }
        });
        city = cityList.get(cityView.getSelectedItem());
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if (null != onCityChooseListener) {
                    onCityChooseListener.onCityChoose(areaProvince, areaCity);
                    dismiss();
                }
                break;
        }
    }

    private OnCityChooseListener onCityChooseListener;

    public interface OnCityChooseListener {
        void onCityChoose(AreaProvince province, AreaCity city);
    }
}
