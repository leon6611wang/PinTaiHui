package com.zhiyu.quanzhu.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
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

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.Circle;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.ui.adapter.QuanZiSouQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.popupwindow.QuanZiSouQuanTypePopupWindow;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

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
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
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
        initPtr();
        initMenuLayout();
        initAreaMenu();
        initTypeViews();
        initOrderViews();
        return view;
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
                if(orderMenuShow){
                    hideOrderMenu();
                }
                if(areaMenuShow){
                    hideAreaMenu();
                }
                if(typeMenuShow){
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
        ptrFrameLayout.autoRefresh();
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
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.typeHobbyTextView:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                type = 2;
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.typeAllTextView:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                type = 0;
                changeTpeMenuText(type);
                searchCircle();
                break;
            case R.id.orderTimeTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 1;
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderMemberCountTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 2;
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderFeedCountTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 3;
                changeOrderMenuText(order_type);
                searchCircle();
                break;
            case R.id.orderAllTextView:
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                order_type = 4;
                changeOrderMenuText(order_type);
                searchCircle();
                break;
        }
    }

    private void changeTpeMenuText(int index){
        typeIndustryTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeHobbyTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        switch (index){
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
    private void changeOrderMenuText(int index){
        orderTimeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderMemberCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderFeedCountTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderAllTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        switch (index){
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
    private String cityName = SharedPreferencesUtils.getInstance(getContext()).getLocationCity();
    private CircleResult circleResult;
    private List<Circle> list;

    private void searchCircle() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_CIRCLE);
        params.addBodyParameter("type", String.valueOf(type));//1行业 2兴趣 默认0全部
        params.addBodyParameter("city_name", cityName);
        params.addBodyParameter("order_type", String.valueOf(order_type));//1按创建时间 2按圈子人数 3按圈子动态数 4综合排序
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
                if (isRefresh) {
                    list = circleResult.getData().getList();
                } else {
                    list.addAll(circleResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("搜圈： " + circleResult.getData().getList().size());
                System.out.println("搜圈： " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("搜圈： " + ex.toString());
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
        initAreaData();
        initAreaViews();
    }

    private void initAreaData() {
        areaProvinceList = AreaDao.getInstance().provinceList();
        System.out.println("province list: " + areaProvinceList.size());
        if (null != areaProvinceList && areaProvinceList.size() > 0) {
            areaProvince = areaProvinceList.get(0);
            for (AreaProvince p : areaProvinceList) {
                provinceList.add(p.getName());
            }
        }

        areaCityList = AreaDao.getInstance().cityList(areaProvinceList.get(0).getCode());
        if (null != areaCityList && areaCityList.size() > 0) {
            areaCity = areaCityList.get(0);
            for (AreaCity c : areaCityList) {
                cityList.add(c.getName());
            }
        }
    }

    private void initAreaViews() {
        provinceView = view.findViewById(R.id.provinceView);
        provinceView.setNotLoop();
        cityView = view.findViewById(R.id.cityView);
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

}
