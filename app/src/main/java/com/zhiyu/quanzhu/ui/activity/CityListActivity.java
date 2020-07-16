package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.leon.chic.dao.CityDao;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.leon.chic.model.City;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.ui.adapter.CityListAdapter;
import com.zhiyu.quanzhu.ui.adapter.CityListGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.ZiMuListAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CityListActivity extends BaseActivity implements View.OnClickListener, CityListAdapter.OnSelectCityListener, CityListGridAdapter.OnGridSelectedCityListener {
    private ListView cityListView;
    private CityListAdapter cityListAdapter;
    private List<Map<String, List<City>>> list = null;
    private ListView ziMuListView;
    private List<String> ziMuList = new ArrayList<>();
    private ZiMuListAdapter ziMuListAdapter;
    private View headerView;
    private LinearLayout backLayout;
    private EditText searchEditText;
    private MyGridView hotCityGridView;
    private CityListGridAdapter hotCityGridViewAdapter;
    private List<City> hotCityList = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);
    private TextView locationCityTextView, refreshLocationTextView;
    private String selectedCity;

    private static class MyHandler extends Handler {
        WeakReference<CityListActivity> weakReference;

        public MyHandler(CityListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CityListActivity activity = weakReference.get();
            switch (msg.what) {
                case 1:
                    activity.cityListAdapter.setData(activity.list);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initData();
        initViews();

        list = CityDao.getInstance().getCities(BaseApplication.getInstance());
        if (null == list || list.size() == 0) {
            cityList();
        } else {
            cityListAdapter.setData(list);
        }
    }

    private void initData() {
        ziMuList.add("a");
        ziMuList.add("b");
        ziMuList.add("c");
        ziMuList.add("d");
        ziMuList.add("e");
        ziMuList.add("f");
        ziMuList.add("g");
        ziMuList.add("h");
        ziMuList.add("i");
        ziMuList.add("j");
        ziMuList.add("k");
        ziMuList.add("l");
        ziMuList.add("m");
        ziMuList.add("n");
        ziMuList.add("o");
        ziMuList.add("p");
        ziMuList.add("q");
        ziMuList.add("r");
        ziMuList.add("s");
        ziMuList.add("t");
        ziMuList.add("u");
        ziMuList.add("v");
        ziMuList.add("w");
        ziMuList.add("x");
        ziMuList.add("y");
        ziMuList.add("z");


        City h1 = new City();
        h1.setName("全国");
        City h4 = new City();
        h4.setName("北京");
        City h5 = new City();
        h5.setName("上海");
        City h6 = new City();
        h6.setName("广州");
        City h7 = new City();
        h7.setName("深圳");
        City h8 = new City();
        h8.setName("杭州");
        City h9 = new City();
        h9.setName("南京");
        City h10 = new City();
        h10.setName("苏州");
        City h11 = new City();
        h11.setName("重庆");
        City h12 = new City();
        h12.setName("天津");
        City h13 = new City();
        h13.setName("武汉");
        City h14 = new City();
        h14.setName("西安");
        hotCityList.add(h1);
        hotCityList.add(h4);
        hotCityList.add(h5);
        hotCityList.add(h6);
        hotCityList.add(h7);
        hotCityList.add(h8);
        hotCityList.add(h9);
        hotCityList.add(h10);
        hotCityList.add(h11);
        hotCityList.add(h12);
        hotCityList.add(h13);
        hotCityList.add(h14);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(CityListActivity.this);
                    if (!StringUtils.isNullOrEmpty(search)) {
                        list = CityDao.getInstance().getCities(BaseApplication.getInstance(), search);
                    } else {
                        list = CityDao.getInstance().getCities(BaseApplication.getInstance());
                    }
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                    return true;
                }
                return false;
            }
        });
        headerView = LayoutInflater.from(this).inflate(R.layout.header_city_recyclerview, null);
        locationCityTextView = headerView.findViewById(R.id.locationCityTextView);
        locationCityTextView.setOnClickListener(this);
        selectedCity = SPUtils.getInstance().getLocationCity(this);
        locationCityTextView.setText(selectedCity);
        refreshLocationTextView = headerView.findViewById(R.id.refreshLocationTextView);
        refreshLocationTextView.setOnClickListener(this);
        cityListView = findViewById(R.id.cityListView);
        ziMuListView = findViewById(R.id.ziMuListView);
        ziMuListAdapter = new ZiMuListAdapter();
        ziMuListAdapter.setData(ziMuList);
        ziMuListView.setAdapter(ziMuListAdapter);
        cityListAdapter = new CityListAdapter();
        cityListAdapter.setOnSelectCityListener(this);
        cityListView.setAdapter(cityListAdapter);
        cityListView.addHeaderView(headerView);
//        cityListView.smoothScrollToPosition(1);

        hotCityGridView = headerView.findViewById(R.id.hotCityGridView);
        hotCityGridViewAdapter = new CityListGridAdapter();
        hotCityGridViewAdapter.setData(hotCityList);
        hotCityGridView.setAdapter(hotCityGridViewAdapter);
        hotCityGridViewAdapter.setOnGridSelectedCityListener(this);

        ziMuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String letter = ziMuList.get(position);
                System.out.println("letter: " + letter);
                int index = -1;
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
//                        for (Map.Entry<String, List<City>> entry : list.get(i).entrySet()) {
//                            System.out.println("key:" + entry.getKey());
//                        }
                        if (list.get(i).containsKey(letter.toUpperCase())) {
                            index = i;
                            break;
                        }
                    }
                }
                System.out.println("index: " + index);
                if (index > -1) {
                    cityListView.smoothScrollToPosition(position + 1);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.locationCityTextView:
                selectedCity = locationCityTextView.getText().toString().trim();
                locationCityTextView.setBackground(getResources().getDrawable(R.drawable.shape_city_selected));
                locationCityTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                for (int i = 0; i < hotCityList.size(); i++) {
                    hotCityList.get(i).setSelected(false);
                }
                hotCityGridViewAdapter.setData(hotCityList);
                for (int i = 0; i < list.size(); i++) {
                    for (String key : list.get(i).keySet()) {
                        for (int j = 0; j < list.get(i).get(key).size(); j++) {
                            list.get(i).get(key).get(j).setSelected(false);
                        }
                    }
                }
                cityListAdapter.setData(list);
                if (null != onCityListSelected) {
                    onCityListSelected.onCityListSelectd(selectedCity);
                }
                finish();
                break;
            case R.id.refreshLocationTextView:
                MessageToast.getInstance(this).show("正在定位...");
                startLocation();
                break;
        }
    }

    @Override
    public void onSelectCity(String city) {
        selectedCity = city;
        for (int i = 0; i < hotCityList.size(); i++) {
            hotCityList.get(i).setSelected(false);
        }
        hotCityGridViewAdapter.setData(hotCityList);
        if (null != onCityListSelected) {
            onCityListSelected.onCityListSelectd(selectedCity);
        }
        finish();
    }

    @Override
    public void onGridSelectdCity(String city, int childPosition) {
        selectedCity = city;
        for (int i = 0; i < hotCityList.size(); i++) {
            hotCityList.get(i).setSelected(false);
        }
        hotCityList.get(childPosition).setSelected(true);
        hotCityGridViewAdapter.setData(hotCityList);
        for (int i = 0; i < list.size(); i++) {
            for (String key : list.get(i).keySet()) {
                for (int j = 0; j < list.get(i).get(key).size(); j++) {
                    list.get(i).get(key).get(j).setSelected(false);
                }
            }
        }
        cityListAdapter.setData(list);
        if (null != onCityListSelected) {
            onCityListSelected.onCityListSelectd(selectedCity);
        }
        finish();
    }

    private void startLocation() {
        if (null == mLocationClient) {
//            System.out.println("###########初始化定位");
            initLocation();
        } else {
//            System.out.println("###########启动定位");
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    //以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    String country = amapLocation.getCountry();//国家信息
                    String province = amapLocation.getProvince();//省信息
                    String city = amapLocation.getCity();//城市信息
                    String district = amapLocation.getDistrict();//城区信息
                    String street = amapLocation.getStreet();//街道信息
                    String streetNum = amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    String buildingId = amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    String floor = amapLocation.getFloor();//获取当前室内定位的楼层
                    amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

//                    System.out.println("高德定位结果: " + country + " , " + province + " , " + city + " , " + district + " , " + street + " , " + address + " , " +
//                            buildingId + " , " + floor);
//                    fragmentHomeQuanZi.setCityName(city);
                    MessageToast.getInstance(CityListActivity.this).show("定位成功");
                    locationCityTextView.setText(city);
                    selectedCity = city;
                    SPUtils.getInstance().saveLocation(BaseApplication.applicationContext, province, city);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    MessageToast.getInstance(CityListActivity.this).show("定位失败");
//                    Log.e("AmapError", "location Error, ErrCode:"
//                            + amapLocation.getErrorCode() + ", errInfo:"
//                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    private AreaResult areaResult;

    /**
     * 地区列表
     */
    private void cityList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CITYS);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                areaResult = GsonUtils.GsonToBean(result, AreaResult.class);
                CityDao.getInstance().saveCities(GsonUtils.GsonString(areaResult.getData().getCitys()), BaseApplication.getInstance(), new CityDao.OnCityLocationListener() {
                    @Override
                    public void onCityLocation() {
                        list = CityDao.getInstance().getCities(BaseApplication.getInstance());
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    }
                });
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


    private static OnCityListSelected onCityListSelected;

    public static void setOnCityListSelected(OnCityListSelected listSelected) {
        onCityListSelected = listSelected;
    }

    public interface OnCityListSelected {
        void onCityListSelectd(String city);
    }
}
