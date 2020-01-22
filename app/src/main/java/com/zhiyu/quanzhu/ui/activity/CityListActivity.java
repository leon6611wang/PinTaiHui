package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.City;
import com.zhiyu.quanzhu.model.bean.Province;
import com.zhiyu.quanzhu.ui.adapter.CityListAdapter;
import com.zhiyu.quanzhu.ui.adapter.CityListGridAdapter;
import com.zhiyu.quanzhu.ui.adapter.ZiMuListAdapter;
import com.zhiyu.quanzhu.ui.widget.MyGridView;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityListActivity extends BaseActivity {
    private ListView cityListView;
    private CityListAdapter cityListAdapter;
    private List<Map<String, List<City>>> list = new ArrayList<>();
    private ListView ziMuListView;
    private List<String> ziMuList = new ArrayList<>();
    private ZiMuListAdapter ziMuListAdapter;
    private View headerView;
    private LinearLayout backLayout;
    private TextView titleTextView;
    private MyGridView hotCityGridView;
    private CityListGridAdapter hotCityGridViewAdapter;
    private List<City> hotCityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initData();
        initViews();
        readJson();
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

        List<City> cityList = new ArrayList<>();
        City c1 = new City();
        c1.setName("安庆");
        c1.setZiMu("a");
        cityList.add(c1);
        City c2 = new City();
        c2.setZiMu("a");
        c2.setName("安阳");
        cityList.add(c2);
        City c13 = new City();
        c13.setZiMu("a");
        c13.setName("鞍山");
        cityList.add(c13);
        City c14 = new City();
        c14.setZiMu("a");
        c14.setName("澳门");
        cityList.add(c14);
        List<City> cityList2 = new ArrayList<>();
        City c3 = new City();
        c3.setName("蚌埠");
        c3.setZiMu("b");
        cityList2.add(c3);
        City c4 = new City();
        c4.setZiMu("b");
        c4.setName("北京");
        cityList2.add(c4);

        City c23 = new City();
        c23.setZiMu("b");
        c23.setName("本溪");
        cityList2.add(c23);

        List<City> cityList3 = new ArrayList<>();
        City c31 = new City();
        c31.setName("重庆");
        c31.setZiMu("c");
        cityList3.add(c31);

        City c32 = new City();
        c32.setZiMu("c");
        c32.setName("长春");
        cityList3.add(c32);

        City c33 = new City();
        c33.setZiMu("c");
        c33.setName("沧州");
        cityList3.add(c33);

        City c34 = new City();
        c34.setZiMu("c");
        c34.setName("赤峰");
        cityList3.add(c34);

        List<City> cityList4 = new ArrayList<>();
        City c41 = new City();
        c41.setName("丹东");
        c41.setZiMu("d");
        c41.setSelected(true);
        cityList4.add(c41);

        City c42 = new City();
        c42.setZiMu("d");
        c42.setName("东莞");
        cityList4.add(c42);

        City c43 = new City();
        c43.setZiMu("d");
        c43.setName("敦化");
        cityList4.add(c43);

        City c44 = new City();
        c44.setZiMu("d");
        c44.setName("当阳");
        cityList4.add(c44);

        List<City> cityList5 = new ArrayList<>();
        City c51 = new City();
        c51.setName("丹东");
        c51.setZiMu("e");
        cityList5.add(c51);

        List<City> cityList6 = new ArrayList<>();
        City c61 = new City();
        c61.setName("丹东");
        c61.setZiMu("f");
        cityList6.add(c61);

        List<City> cityList7 = new ArrayList<>();
        City c71 = new City();
        c71.setName("丹东");
        c71.setZiMu("g");
        cityList7.add(c71);

        List<City> cityList8 = new ArrayList<>();
        City c81 = new City();
        c81.setName("丹东");
        c81.setZiMu("h");
        cityList8.add(c81);

        List<City> cityList9 = new ArrayList<>();
        City c91 = new City();
        c91.setName("丹东");
        c91.setZiMu("i");
        cityList9.add(c91);


        Map<String, List<City>> map = new HashMap<>();
        map.put("A", cityList);
        Map<String, List<City>> map2 = new HashMap<>();
        map2.put("B", cityList2);
        Map<String, List<City>> map3 = new HashMap<>();
        map3.put("C", cityList3);
        Map<String, List<City>> map4 = new HashMap<>();
        map4.put("D", cityList4);
        Map<String, List<City>> map5 = new HashMap<>();
        map5.put("E", cityList5);
        Map<String, List<City>> map6 = new HashMap<>();
        map6.put("F", cityList6);
        Map<String, List<City>> map7 = new HashMap<>();
        map7.put("G", cityList7);
        Map<String, List<City>> map8 = new HashMap<>();
        map8.put("H", cityList8);
        Map<String, List<City>> map9 = new HashMap<>();
        map9.put("I", cityList9);
        list.add(map);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        list.add(map7);
        list.add(map8);
        list.add(map9);

        City h1 = new City();
        h1.setName("全国");
        City h4 = new City();
        h4.setName("上海");
        City h5 = new City();
        h5.setName("天津");
        City h6 = new City();
        h6.setName("杭州");
        City h7 = new City();
        h7.setName("深圳");
        City h8 = new City();
        h8.setName("北京");
        City h9 = new City();
        h9.setName("广州");
        City h10 = new City();
        h10.setName("成都");
        City h11 = new City();
        h11.setName("南京");
        hotCityList.add(h1);
        hotCityList.add(h4);
        hotCityList.add(h5);
        hotCityList.add(h6);
        hotCityList.add(h7);
        hotCityList.add(h8);
        hotCityList.add(h9);
        hotCityList.add(h10);
        hotCityList.add(h11);

    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("选择城市");
        headerView = LayoutInflater.from(this).inflate(R.layout.header_city_recyclerview, null);
        cityListView = findViewById(R.id.cityListView);
        ziMuListView = findViewById(R.id.ziMuListView);
        ziMuListAdapter = new ZiMuListAdapter();
        ziMuListAdapter.setData(ziMuList);
        ziMuListView.setAdapter(ziMuListAdapter);
        cityListAdapter = new CityListAdapter();
        cityListView.setAdapter(cityListAdapter);
        cityListAdapter.setData(list);
        cityListView.addHeaderView(headerView);
        cityListView.smoothScrollToPosition(4);

        hotCityGridView = headerView.findViewById(R.id.hotCityGridView);
        hotCityGridViewAdapter = new CityListGridAdapter();
        hotCityGridViewAdapter.setData(hotCityList);
        hotCityGridView.setAdapter(hotCityGridViewAdapter);

        ziMuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityListView.smoothScrollToPosition(position);
            }
        });
    }

    private void readJson() {
        try {
            InputStream is = CityListActivity.this.getClass().getClassLoader().
                    getResourceAsStream("assets/" + "city.json");
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();
            String str=stringBuilder.toString();
            List<Province> provinceList= GsonUtils.jsonToList(str,Province.class);
            Log.i("parseJson","province size: "+(null==provinceList?0:provinceList.size()));
            for(Province province:provinceList){
                if(null!=province.getCity()&&province.getCity().size()>0){
                    for(City city:province.getCity()){
                        if(city.getName().equals("市辖区")){
                            Log.i("parseJson",province.toString());
                        }else if(!city.getName().equals("省直辖县级行政区划")){
                            Log.i("parseJson",city.toString());
                        }
                    }
                }else{
                    Log.i("parseJson",province.toString());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
