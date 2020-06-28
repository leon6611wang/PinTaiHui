package com.zhiyu.quanzhu.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.dao.PageDao;
import com.leon.chic.utils.CityUtils;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.ui.adapter.QuanZiSouQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈子-搜圈
 */
public class FragmentQuanZiSouQuan extends Fragment implements View.OnClickListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private PtrFrameLayout ptrFrameLayout;
    private QuanZiSouQuanRecyclerAdapter adapter;
    private TextView typeTextView, areaTextView, orderTextView;
    private LinearLayout typeLayout, areaLayout, orderLayout;
    private ImageView typeImage, areaImage, orderImage;
    private int dp_5, dp_200;
    private LinearLayout typeMenuLayout, areaMenuLayout, orderMenuLayout;
    private boolean typeMenuShow = false, areaMenuShow = false, orderMenuShow = false;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentQuanZiSouQuan> fragmentQuanZiSouQuanWeakReference;

        public MyHandler(FragmentQuanZiSouQuan fragment) {
            fragmentQuanZiSouQuanWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentQuanZiSouQuan fragment = fragmentQuanZiSouQuanWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
                case 2:
                    fragment.initAreaData();
                    break;
                case 99:
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quanzi_souquan, container, false);
        dp_5 = (int) getContext().getResources().getDimension(R.dimen.dp_5);
        dp_200 = (int) getContext().getResources().getDimension(R.dimen.dp_200);
        initViews();
//        initData();
        initPtr();
        initMenuLayout();
        initAreaMenu();
        cityList();
        initTypeViews();
        initOrderViews();
        return view;
    }

    private boolean isRequesting = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting && !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext())) && (null == list || list.size() > 0)) {
            isRequesting = true;
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    isRefresh = true;
                    page = 1;
                    searchCircle();
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isRequesting && !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext())) && (null == list || list.size() > 0)) {
            isRequesting = true;
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    isRefresh = true;
                    page = 1;
                    searchCircle();
                }
            });
        }
    }

    private void initViews() {
        typeMenuLayout = view.findViewById(R.id.typeMenuLayout);
        areaMenuLayout = view.findViewById(R.id.areaMenuLayout);
        orderMenuLayout = view.findViewById(R.id.orderMenuLayout);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new QuanZiSouQuanRecyclerAdapter(getContext());


        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_5);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (areaMenuShow) {
                    hideAreaMenu();
                }
                if (typeMenuShow) {
                    hideTypeMenu();
                }
            }
        });

        typeLayout = view.findViewById(R.id.typeLayout);
        areaLayout = view.findViewById(R.id.areaLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        typeTextView = view.findViewById(R.id.typeTextView);
        typeLayout.setOnClickListener(this);
        areaTextView = view.findViewById(R.id.areaTextView);
        areaLayout.setOnClickListener(this);
        orderTextView = view.findViewById(R.id.orderTextView);
        orderLayout.setOnClickListener(this);
        typeImage = view.findViewById(R.id.typeImage);
        areaImage = view.findViewById(R.id.areaImage);
        orderImage = view.findViewById(R.id.orderImage);
    }

    private void initData() {
        String result = PageDao.getInstance().get(CircleResult.class, BaseApplication.getInstance());
        if (!StringUtils.isNullOrEmpty(result)) {
            circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
            if (null != circleResult && null != circleResult.getData() && null != circleResult.getData().getList()) {
                list = circleResult.getData().getList();
                adapter.setList(list);
            }
        }
    }

    private void initPtr() {
        ptrFrameLayout = view.findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                isRefresh = false;
                page++;
                searchCircle();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                searchCircle();
            }
        });
//        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.typeLayout:
                filterChange(1);
                menu(1);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(typeTextView);
                break;
            case R.id.areaLayout:
                filterChange(2);
                menu(2);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(areaTextView);
                break;
            case R.id.orderLayout:
                filterChange(3);
                menu(3);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(orderTextView);
                break;
            case R.id.typeIndustryTextView:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                type = 1;
                typeTextView.setText("行业");
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.typeHobbyTextView:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                type = 2;
                typeTextView.setText("兴趣");
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.typeAllTextView:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                type = 0;
                typeTextView.setText("全部");
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.orderTimeTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 1;
                orderTextView.setText("时间");
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderMemberCountTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 2;
                orderTextView.setText("人数");
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderFeedCountTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 3;
                orderTextView.setText("动态");
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderAllTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 4;
                orderTextView.setText("综合");
                changeOrderMenuText(order_type);
                searchCircle();
                break;
        }
    }

    private void changeTpeMenuText(int index) {
        typeIndustryTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeHobbyTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        switch (index) {
            case 1:
                typeIndustryTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
            case 2:
                typeHobbyTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
            case 0:
                typeAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
        }
    }

    private void changeOrderMenuText(int index) {
        orderTimeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderMemberCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderFeedCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        switch (index) {
            case 1:
                orderTimeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
            case 2:
                orderMemberCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
            case 3:
                orderFeedCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
            case 4:
                orderAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                break;
        }
    }

    private void filterChange(int position) {
        typeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        areaTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        areaImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        orderImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        switch (position) {
            case 1:
                typeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                typeImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 2:
                areaTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                areaImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 3:
                orderTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                orderImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
        }
    }

    private void initMenuLayout() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", 0f, -dp_200);
        animator1.setDuration(0);//播放时长
        animator1.setStartDelay(0);//延迟播放
        animator1.setRepeatCount(0);//重放次数
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", 0f, -dp_200);
        animator2.setDuration(0);//播放时长
        animator2.setStartDelay(0);//延迟播放
        animator2.setRepeatCount(0);//重放次数
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -dp_200);
        animator3.setDuration(0);//播放时长
        animator3.setStartDelay(0);//延迟播放
        animator3.setRepeatCount(0);//重放次数
        animator3.start();
    }

    private void hideTypeMenu() {
        typeMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideAreaMenu() {
        areaMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideOrderMenu() {
        orderMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showTypeMenu() {
        typeMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showAreaMenu() {
        areaMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showOrderMenu() {
        orderMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void menu(int position) {
        switch (position) {
            case 1:
                if (areaMenuShow) {
                    hideAreaMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (typeMenuShow) {
                    hideTypeMenu();
                } else {
                    showTypeMenu();
                }
                break;
            case 2:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (areaMenuShow) {
                    hideAreaMenu();
                } else {
                    showAreaMenu();
                }
                break;
            case 3:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                if (areaMenuShow) {
                    hideAreaMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                } else {
                    showOrderMenu();
                }
                break;
        }
    }


    private int page = 1;
    private boolean isRefresh = true;
    private int type = 0;
    private int order_type = 4;
    private String cityName = SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext);
    private CircleResult circleResult;
    private List<Circle> list;

    private void searchCircle() {
//        System.out.println("搜圈-cityName" + cityName+" , type: "+type+" , order_type: "+order_type);
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_CIRCLE);
        params.addBodyParameter("type", String.valueOf(type));//1行业 2兴趣 默认0全部
        params.addBodyParameter("city_name", cityName);
        params.addBodyParameter("order_type", String.valueOf(order_type));//1按创建时间 2按圈子人数 3按圈子动态数 4综合排序
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("搜圈: " + result);
                circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
                if (isRefresh) {
                    list = circleResult.getData().getList();
                } else {
                    list.addAll(circleResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                PageDao.getInstance().save(CircleResult.class, result, BaseApplication.getInstance());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("搜圈: " + ex.toString());
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private LoopView provinceView, cityView;
    private List<String> provinceList = new ArrayList<>();
    private List<AreaProvince> areaProvinceList;
    private List<AreaCity> areaCityList;
    private List<String> cityList = new ArrayList<>();
    private String province, city;
    private AreaProvince areaProvince;
    private AreaCity areaCity;

    private void initAreaMenu() {
        initAreaViews();
    }

    private int provinceIndex, cityIndex;

    private void initAreaData() {
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

    private void initAreaViews() {
        provinceView = view.findViewById(R.id.provinceView);
        provinceView.setNotLoop();
        cityView = view.findViewById(R.id.cityView);
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
                    areaCity = areaCityList.get(index);
                    areaTextView.setText(city);
                    if (areaMenuShow) {
                        hideAreaMenu();
                    }
                    cityName = city;
                    searchCircle();
                }
            }
        });
    }

    private TextView typeIndustryTextView, typeHobbyTextView, typeAllTextView;

    private void initTypeViews() {
        typeIndustryTextView = view.findViewById(R.id.typeIndustryTextView);
        typeIndustryTextView.setOnClickListener(this);
        typeHobbyTextView = view.findViewById(R.id.typeHobbyTextView);
        typeHobbyTextView.setOnClickListener(this);
        typeAllTextView = view.findViewById(R.id.typeAllTextView);
        typeAllTextView.setOnClickListener(this);
    }

    private TextView orderTimeTextView, orderMemberCountTextView, orderFeedCountTextView, orderAllTextView;

    private void initOrderViews() {
        orderTimeTextView = view.findViewById(R.id.orderTimeTextView);
        orderTimeTextView.setOnClickListener(this);
        orderMemberCountTextView = view.findViewById(R.id.orderMemberCountTextView);
        orderMemberCountTextView.setOnClickListener(this);
        orderFeedCountTextView = view.findViewById(R.id.orderFeedCountTextView);
        orderFeedCountTextView.setOnClickListener(this);
        orderAllTextView = view.findViewById(R.id.orderAllTextView);
        orderAllTextView.setOnClickListener(this);
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
                Message message = myHandler.obtainMessage(2);
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
