package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.leon.chic.utils.CityUtils;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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
    private int provinceIndex, cityIndex;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ProvinceCityDialog> dialogWeakReference;

        public MyHandler(ProvinceCityDialog dialog) {
            dialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            ProvinceCityDialog dialog = dialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.initData();
                    break;
            }
        }
    }

    public ProvinceCityDialog(@NonNull Context context, int themeResId, OnCityChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCityChooseListener = listener;
        province = SPUtils.getInstance().getLocationProvince(BaseApplication.applicationContext);
        city = SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext);
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
        initViews();
        cityList();
    }

    private void initData() {
        areaProvinceList = areaResult.getData().getCitys();
        if (null != areaProvinceList && areaProvinceList.size() > 0) {
            areaProvince = areaProvinceList.get(0);
            for (int i = 0; i < areaProvinceList.size(); i++) {
                provinceList.add(areaProvinceList.get(i).getName());
                if (!StringUtils.isNullOrEmpty(province)) {
                    if (areaProvinceList.get(i).getName().equals(province) ||
                            areaProvinceList.get(i).getName().contains(province) ||
                            province.contains(areaProvinceList.get(i).getName())) {
                        areaProvince = areaProvinceList.get(i);
                        provinceIndex = i;
                    }
                }
            }
        }
        areaCityList = areaProvinceList.get(provinceIndex).getChild();
        if (null != areaCityList && areaCityList.size() > 0) {
            areaCity = areaCityList.get(0);
            for (int i = 0; i < areaCityList.size(); i++) {
                cityList.add(areaCityList.get(i).getName());
                if (!StringUtils.isNullOrEmpty(city)) {
                    if (city.equals(areaCityList.get(i).getName()) ||
                            city.contains(areaCityList.get(i).getName()) ||
                            areaCityList.get(i).getName().contains(city)
                            ) {
                        cityIndex = i;
                        areaCity = areaCityList.get(i);
                    }
                }
            }
        }

        provinceView.setItems(provinceList);
        provinceView.setInitPosition(provinceIndex);
        cityView.setItems(cityList);
        cityView.setInitPosition(cityIndex);
    }

    private void getCitys(int code) {
        String json = CityUtils.getInstance().getCitys(BaseApplication.getInstance(), code);
        if (!StringUtils.isNullOrEmpty(json)) {
            areaCityList = GsonUtils.getObjectList(json, AreaCity.class);
        }
    }

    private void initViews() {
        provinceView = findViewById(R.id.provinceView);
        provinceView.setNotLoop();
        cityView = findViewById(R.id.cityView);
        cityView.setNotLoop();

        provinceView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(provinceList.get(index))) {
                    province = provinceList.get(index);
                    areaProvince = areaProvinceList.get(index);
                    if (null != areaCityList) {
                        areaCityList.clear();
                    }
                    if (null != cityList) {
                        cityList.clear();
                    }
                    getCitys(areaProvinceList.get(index).getCode());
                    if (null != areaCityList && areaCityList.size() > 0) {
                        for (AreaCity c : areaCityList) {
                            cityList.add(c.getName());
                        }
                        cityView.setItems(cityList);
                        cityView.setCurrentPosition(cityIndex);
                        if (cityIndex > cityList.size() - 1) {
                            cityIndex = 0;
                        }
                        city = cityList.get(cityIndex);
                        areaCity = areaCityList.get(cityIndex);
                    } else {
                        city = null;
                        areaCity = null;
                    }


                }
            }
        });

        cityView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                cityIndex = index;
                if (!TextUtils.isEmpty(cityList.get(index))) {
                    city = cityList.get(index);
                    areaCity =areaCityList.get(index);
                }
            }
        });
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

    private AreaResult areaResult;

    /**
     * 地区列表
     */
    private void cityList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CITYS);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("city list: " + result);
                areaResult = GsonUtils.GsonToBean(result, AreaResult.class);
                if (null != areaResult && null != areaResult.getData() && null != areaResult.getData().getCitys() && areaResult.getData().getCitys().size() > 0) {
                    for (AreaProvince province : areaResult.getData().getCitys()) {
                        CityUtils.getInstance().saveCitys(BaseApplication.getInstance(), province.getName(), province.getCode(), GsonUtils.GsonString(province.getChild()));
                    }
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("city list error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
