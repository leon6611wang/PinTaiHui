package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaCity;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.activity.CityListActivity;
import com.zhiyu.quanzhu.ui.activity.CreateCircleActivity;
import com.zhiyu.quanzhu.ui.activity.FullSearchActivity;
import com.zhiyu.quanzhu.ui.activity.MyCirclesActivity;
import com.zhiyu.quanzhu.ui.activity.ScanActivity;
import com.zhiyu.quanzhu.ui.adapter.CircleGuanZhuAdapter;
import com.zhiyu.quanzhu.ui.adapter.MyFragmentStatePagerAdapter;
import com.zhiyu.quanzhu.ui.dialog.HomeCircleMenuDialog;
import com.zhiyu.quanzhu.ui.dialog.ProvinceCityDialog;
import com.zhiyu.quanzhu.ui.dialog.PublishDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 首页-圈子
 */
public class FragmentHomeQuanZi extends Fragment implements View.OnClickListener, CircleGuanZhuAdapter.OnMoreCircleClickListener, CityListActivity.OnCityListSelected {
    private View view;
    private NoScrollViewPager viewPager;
    private MyFragmentStatePagerAdapter adapter;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private TextView guanzhutextview, tuijiantextview, souquantextview;
    private ImageView publishMenuImageView, searchImageView;
    private FrameLayout topBarLayout;
    private int topBarWidth, topBarHeight;
    private int bottomBarHeight;
    private int contentHeight;
    private HomeCircleMenuDialog menuDialog;
    private PublishDialog publishDialog;
    private ShareDialog shareDialog;
    private LinearLayout cityLayout;
    private TextView cityTextView;
    private ProvinceCityDialog cityDialog;
    private int menuY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_quanzi, container, false);
        CityListActivity.setOnCityListSelected(this);
        shareConfig();
        Bundle bundle = getArguments();
        bottomBarHeight = bundle.getInt("bottomBarHeight");
//        System.out.println("FragmentHomeQuanZi bottomBarHeight: " + bottomBarHeight);
        topBarLayout = view.findViewById(R.id.topBarLayout);
        topBarLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        topBarLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        topBarWidth = topBarLayout.getWidth(); // 获取宽度
                        topBarHeight = topBarLayout.getHeight(); // 获取高度
                        initDatas();
                        initViews();
                        initDialogs();
//                        System.out.println("topBarWidth: " + topBarWidth + " , topBarHeight: " + topBarHeight);
                        return true;
                    }
                });

        return view;
    }

    private void initDialogs() {
        shareDialog = new ShareDialog(getActivity(), getContext(), R.style.dialog);
        publishDialog = new PublishDialog(getContext(), R.style.dialog);
        cityDialog = new ProvinceCityDialog(getContext(), R.style.dialog, new ProvinceCityDialog.OnCityChooseListener() {
            @Override
            public void onCityChoose(AreaProvince province, AreaCity city) {
                cityTextView.setText(city.getName());
                fragmentQuanZiTuiJian.setCity(city.getName());
            }
        });
        menuDialog = new HomeCircleMenuDialog(getContext(), R.style.dialog, new HomeCircleMenuDialog.OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int position, String desc) {
                switch (position) {
                    case 0:
                        publishDialog.show();
                        break;
                    case 1:
                        Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
                        scanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(scanIntent);
                        break;
                    case 2:
                        Intent createShangQuanIntent = new Intent(getActivity(), CreateCircleActivity.class);
                        createShangQuanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(createShangQuanIntent);
                        break;
                    case 3:

                        break;
                    case 4:
                        Intent cardIntent = new Intent(getActivity(), CardInformationActivity.class);
                        cardIntent.putExtra("uid", (long) SPUtils.getInstance().getUserId(BaseApplication.applicationContext));
                        cardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(cardIntent);
                        break;
                    case 5:
                        shareDialog.show();
                        shareDialog.setShare(shareResult.getData().getShare(), SPUtils.getInstance().getUserId(getContext()));
                        shareDialog.hideInnerShare();
                        break;
                    case 6:
                        Intent shangquanIntent = new Intent(getActivity(), MyCirclesActivity.class);
                        shangquanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(shangquanIntent);
                        break;
                }
            }
        });
    }

    @Override
    public void onCityListSelectd(String city) {
        if (null != cityTextView)
            cityTextView.setText(city);
        if (null != fragmentQuanZiTuiJian)
            fragmentQuanZiTuiJian.setCity(city);
    }

    private FragmentQuanZiTuiJian fragmentQuanZiTuiJian;

    private void initDatas() {
        fragmentQuanZiTuiJian = new FragmentQuanZiTuiJian();
        Bundle bundle = new Bundle();
        int screenHeight = ScreentUtils.getInstance().getScreenHeight(getContext());
        contentHeight = screenHeight - topBarHeight - bottomBarHeight;
        bundle.putInt("contentHeight", contentHeight);
        fragmentQuanZiTuiJian.setArguments(bundle);
//        if (null == fragmentArrayList && fragmentArrayList.size() == 0) {
        fragmentArrayList.add(new FragmentQuanZiGuanZhu());
        fragmentArrayList.add(fragmentQuanZiTuiJian);
        fragmentArrayList.add(new FragmentQuanZiSouQuan());
//        }
    }

    public void setCityName(String cityName) {
        cityTextView.setText(cityName);
    }

    private void initViews() {
        cityLayout = view.findViewById(R.id.cityLayout);
        cityLayout.setOnClickListener(this);
        cityTextView = view.findViewById(R.id.cityTextView);
        cityTextView.setText(SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        guanzhutextview = view.findViewById(R.id.guanzhutextview);
        tuijiantextview = view.findViewById(R.id.tuijiantextview);
        souquantextview = view.findViewById(R.id.souquantextview);
        publishMenuImageView = view.findViewById(R.id.publishMenuImageView);
        publishMenuImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        publishMenuImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] locations = new int[2];
                        publishMenuImageView.getLocationOnScreen(locations);
                        int menu_y = locations[1];
                        int menu_height = publishMenuImageView.getHeight(); // 获取高度
//                        menuY=menu_y+menu_height;
                        menuY = menu_y - menu_height + 20;
//                        System.out.println("menu_height: " + menu_height+" , menu_y: "+menu_y);
                        return true;
                    }
                });
        searchImageView = view.findViewById(R.id.searchImageView);
        searchImageView.setOnClickListener(this);
        guanzhutextview.setOnClickListener(this);
        tuijiantextview.setOnClickListener(this);
        souquantextview.setOnClickListener(this);
        publishMenuImageView.setOnClickListener(this);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setScroll(false);
        adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fragmentArrayList);
        CircleGuanZhuAdapter.setOnMoreCircleClickListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        barChange(1);
    }

    @Override
    public void onMoreCircle() {
        viewPager.setCurrentItem(2);
        barChange(2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guanzhutextview:
                barChange(0);
                break;
            case R.id.tuijiantextview:
                barChange(1);
                break;
            case R.id.souquantextview:
                barChange(2);
                break;
            case R.id.publishMenuImageView:
                menuDialog.show();
                menuDialog.setY(menuY);
                break;
            case R.id.searchImageView:
                Intent fullSearchIntent = new Intent(getActivity(), FullSearchActivity.class);
                fullSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(fullSearchIntent);
                break;
            case R.id.cityLayout:
//                cityDialog.show();
                Intent cityIntent = new Intent(getActivity(), CityListActivity.class);
                cityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(cityIntent);
                break;
        }
    }

    private void barChange(int position) {
        viewPager.setCurrentItem(position);
        guanzhutextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tuijiantextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        souquantextview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        guanzhutextview.setTextSize(14);
        tuijiantextview.setTextSize(14);
        souquantextview.setTextSize(14);

        switch (position) {
            case 0:
                guanzhutextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                guanzhutextview.setTextSize(16);
                break;
            case 1:
                tuijiantextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tuijiantextview.setTextSize(16);
                break;
            case 2:
                souquantextview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                souquantextview.setTextSize(16);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }

    private ShareResult shareResult;

    private void shareConfig() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_APP);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult = GsonUtils.GsonToBean(result, ShareResult.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
