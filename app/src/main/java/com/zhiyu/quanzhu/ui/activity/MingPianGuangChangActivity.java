package com.zhiyu.quanzhu.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.CityUtils;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.Card;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryHobby;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.model.result.IndustryHobbyResult;
import com.zhiyu.quanzhu.ui.adapter.MingPianRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 名片广场
 */
public class MingPianGuangChangActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private RecyclerView mRecyclerView;
    private MingPianRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private EditText searchEditText;
    private int chengshiMenuHeight, zhiyeMenuHeight, orderMenuHeight;
    private boolean chengshiMenuShow, zhiyeMenuShow, orderMenuShow;
    private LinearLayout chengshiMenuLayout, zhiyeMenuLayout, orderMenuLayout;
    private LinearLayout chengshiLayout, zhiyeLayout, orderLayout;
    private TextView chengshiTextView, zhiyeTextView, orderTextView;
    private ImageView chengshiImage, zhiyeImage, orderImage;
    private TextView cityConfirmTextView, industryConfirmTextView, sortConfirmTextView;

    private LoopView provinceView, cityView, industryParentView, industryChildView, sortView;

    private static class MyHandler extends Handler {
        WeakReference<MingPianGuangChangActivity> mingPianGuangChangActivityWeakReference;

        public MyHandler(MingPianGuangChangActivity activity) {
            mingPianGuangChangActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MingPianGuangChangActivity activity = mingPianGuangChangActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    activity.initCityData();
                    break;
                case 3:
                    activity.initIndustryData();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mingpianguangchang);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        chengshiMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        zhiyeMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        orderMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        initViews();
        initMenuLayout();
        initMenu();
        cityList();
        industryHobbyList();
    }

    private String keyword = "";

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("名片广场");

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(MingPianGuangChangActivity.this);
                    page = 1;
                    isRefresh = true;
                    searchCard();
                    return true;
                }
                return false;
            }
        });

        chengshiMenuLayout = findViewById(R.id.chengshiMenuLayout);
        zhiyeMenuLayout = findViewById(R.id.zhiyeMenuLayout);
        orderMenuLayout = findViewById(R.id.orderMenuLayout);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        chengshiLayout.setOnClickListener(this);
        zhiyeLayout = findViewById(R.id.zhiyeLayout);
        zhiyeLayout.setOnClickListener(this);
        orderLayout = findViewById(R.id.orderLayout);
        orderLayout.setOnClickListener(this);
        chengshiTextView = findViewById(R.id.chengshiTextView);
        zhiyeTextView = findViewById(R.id.zhiyeTextView);
        orderTextView = findViewById(R.id.orderTextView);
        chengshiImage = findViewById(R.id.chengshiImage);
        zhiyeImage = findViewById(R.id.zhiyeImage);
        orderImage = findViewById(R.id.orderImage);
        cityConfirmTextView = findViewById(R.id.cityConfirmTextView);
        cityConfirmTextView.setOnClickListener(this);
        industryConfirmTextView = findViewById(R.id.industryConfirmTextView);
        industryConfirmTextView.setOnClickListener(this);
        sortConfirmTextView = findViewById(R.id.sortConfirmTextView);
        sortConfirmTextView.setOnClickListener(this);

        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                searchCard();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                searchCard();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MingPianRecyclerAdapter(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    private List<String> provinceList = new ArrayList<>();
    private List<AreaProvince> areaProvinceList;
    private List<AreaCity> areaCityList;
    private List<String> cityList = new ArrayList<>();
    private int cityIndex, industryIndex, sortIndex;

    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private List<IndustryHobby> industryParentList;
    private List<IndustryHobby> industryChildList;
    private List<String> sortList = new ArrayList<>();

    private void initMenu() {
        initSortData();
        provinceView = findViewById(R.id.provinceView);
        cityView = findViewById(R.id.cityView);
        provinceView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(provinceList.get(index))) {
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
                    }
                    cityView.setItems(cityList);
                }
            }
        });

        cityView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                cityIndex = index;
            }
        });
        industryParentView = findViewById(R.id.industryParentView);
        industryChildView = findViewById(R.id.industryChildView);
        industryParentView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(parentList.get(index))) {
                    if (null != industryChildList) {
                        industryChildList.clear();
                    }
                    if (null != childList) {
                        childList.clear();
                    }
                    industryChildList = industryParentList.get(index).getChild();
                    if (null != industryChildList && industryChildList.size() > 0) {
                        for (IndustryHobby child : industryChildList) {
                            childList.add(child.getName());
                        }
                    }
                    industryChildView.setItems(childList);
                    industryChildView.setInitPosition(0);
                }
            }
        });

        industryChildView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                industryIndex = index;
            }
        });
        sortView = findViewById(R.id.sortView);
        sortView.setItems(sortList);
        sortView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                sortIndex = index;
            }
        });

    }

    private void getCitys(int code) {
        String json = CityUtils.getInstance().getCitys(BaseApplication.getInstance(), code);
        if (!StringUtils.isNullOrEmpty(json)) {
            areaCityList = GsonUtils.getObjectList(json, AreaCity.class);
        }
    }

    private void initCityData() {
        areaProvinceList = areaResult.getData().getCitys();
        for (AreaProvince province : areaProvinceList) {
            provinceList.add(province.getName());
        }
        provinceView.setItems(provinceList);
        provinceView.setInitPosition(0);
        areaCityList = areaProvinceList.get(0).getChild();
        for (AreaCity city : areaCityList) {
            cityList.add(city.getName());
        }
        cityView.setItems(cityList);
        cityView.setInitPosition(0);
    }

    private void initIndustryData() {
        if (null != industryParentList && industryParentList.size() > 0)
            for (IndustryHobby parent : industryParentList) {
                parentList.add(parent.getName());
            }
        industryParentView.setItems(parentList);
        industryParentView.setInitPosition(0);
        industryChildList = industryParentList.get(0).getChild();
        if (null != industryChildList && industryChildList.size() > 0) {
            for (IndustryHobby child : industryChildList) {
                childList.add(child.getName());
            }
        }
        industryChildView.setItems(childList);
        industryChildView.setInitPosition(0);
    }

    private void initSortData() {
        sortList.add("入驻时间");
        sortList.add("点赞数");
        sortList.add("浏览数");
        sortList.add("收藏数");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.chengshiLayout:
                barChange(1);
                menu(1);
                break;
            case R.id.zhiyeLayout:
                barChange(2);
                menu(2);
                break;
            case R.id.orderLayout:
                barChange(3);
                menu(3);
                break;
            case R.id.cityConfirmTextView:
                menu(1);
                city_id = areaCityList.get(cityIndex).getId();
                chengshiTextView.setText(cityList.get(cityIndex));
                chengshiTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                page = 1;
                isRefresh = true;
                searchCard();
                break;
            case R.id.industryConfirmTextView:
                if (null == childList || childList.size() == 0) {
                    MessageToast.getInstance(MingPianGuangChangActivity.this).show("当前行业无二级分类，请选择其他行业");
                } else {
                    menu(2);
                    industry = childList.get(industryIndex);
                    zhiyeTextView.setText(childList.get(industryIndex));
                    zhiyeTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                    page = 1;
                    isRefresh = true;
                    searchCard();
                }

                break;
            case R.id.sortConfirmTextView:
                menu(3);
                sort = sortList.get(sortIndex);
                orderTextView.setText(sortList.get(sortIndex));
                orderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                page = 1;
                isRefresh = true;
                searchCard();
                break;
        }
    }

    private void barChange(int position) {
//        chengshiTextView.setTextColor(getResources().getColor(R.color.text_color_black));
//        zhiyeTextView.setTextColor(getResources().getColor(R.color.text_color_black));
//        orderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        chengshiImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        zhiyeImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        orderImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        switch (position) {
            case 1:
//                chengshiTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                chengshiImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 2:
//                zhiyeTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhiyeImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 3:
//                orderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                orderImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
        }
    }

    private void initMenuLayout() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", 0f, -chengshiMenuHeight);
        animator1.setDuration(0);//播放时长
        animator1.setStartDelay(0);//延迟播放
        animator1.setRepeatCount(0);//重放次数
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", 0f, -zhiyeMenuHeight);
        animator2.setDuration(0);//播放时长
        animator2.setStartDelay(0);//延迟播放
        animator2.setRepeatCount(0);//重放次数
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -orderMenuHeight);
        animator3.setDuration(0);//播放时长
        animator3.setStartDelay(0);//延迟播放
        animator3.setRepeatCount(0);//重放次数
        animator3.start();
    }

    private void showChengshiMenu() {
        chengshiMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", -chengshiMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showZhiyeMenu() {
        zhiyeMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", -zhiyeMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showOrderMenu() {
        orderMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", -orderMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideChengshiMenu() {
        chengshiMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", 0f, -chengshiMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideOrderMenu() {
        orderMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -orderMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideZhiyeMenu() {
        zhiyeMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", 0f, -zhiyeMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void menu(int position) {
        switch (position) {
            case 1:
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                } else {
                    showChengshiMenu();
                }
                break;
            case 2:
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
                } else {
                    showZhiyeMenu();
                }
                break;
            case 3:
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                }
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
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
    private CardResult cardResult;
    private List<Card> list;
    private int city_id;
    private String industry, sort;

    private void searchCard() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_CARD);
        if (city_id > 0)
            params.addBodyParameter("city", String.valueOf(city_id));
        if (!StringUtils.isNullOrEmpty(industry))
            params.addBodyParameter("industry", industry);
        if (!StringUtils.isNullOrEmpty(sort))
            params.addBodyParameter("sort", sort);
        params.addBodyParameter("keywords", keyword);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                if (isRefresh) {
                    list = cardResult.getData().getCard_list();
                } else {
                    list.addAll(cardResult.getData().getCard_list());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("名片广场: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private AreaResult areaResult;

    /**
     * 地区列表
     */
    private void cityList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CITYS);
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

    private IndustryHobbyResult industryHobbyResult;

    private void industryHobbyList() {
        final RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOBBY_LIST);
        params.addBodyParameter("type", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                industryHobbyResult = GsonUtils.GsonToBean(result, IndustryHobbyResult.class);
                industryParentList = industryHobbyResult.getData().getList().get(0).getChild();
                int index = -1;
                if (null != industryParentList && industryParentList.size() > 0) {
                    for (int i = 0; i < industryParentList.size(); i++) {
                        if (industryParentList.get(i).getName().equals("自定义")) {
                            if (null == industryParentList.get(i).getChild() || industryParentList.get(i).getChild().size() == 0) {
                                index = i;
                            }
                        }
                    }
                }
                if (index > -1) {
                    industryParentList.remove(index);
                }

                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("hobby list error: " + ex.toString());
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
