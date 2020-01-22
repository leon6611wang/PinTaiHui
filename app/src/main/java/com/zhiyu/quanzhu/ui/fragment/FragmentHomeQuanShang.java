package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MallAdGoods;
import com.zhiyu.quanzhu.model.bean.MallAdImg;
import com.zhiyu.quanzhu.model.result.MallAdGoodsResult;
import com.zhiyu.quanzhu.model.result.MallAdResult;
import com.zhiyu.quanzhu.ui.activity.CartActivity;
import com.zhiyu.quanzhu.ui.activity.H5PageActivity;
import com.zhiyu.quanzhu.ui.activity.QuanShangFenLeiActivity;
import com.zhiyu.quanzhu.ui.adapter.HomeQuanShangRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.HomeQuanShangRecyclerViewHeaderView;
import com.zhiyu.quanzhu.ui.widget.Indicator;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideImageLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FragmentHomeQuanShang extends Fragment {

    private View view;
    private View headerView;
    private MyRecyclerView mRecyclerView;
    private ArrayList<MallAdGoods> list = new ArrayList<>();
    private HomeQuanShangRecyclerAdapter adapter;
    private View headerHeaderView;
    private HomeQuanShangRecyclerViewHeaderView homeQuanShangRecyclerViewHeaderView;
    private int totalDy;
    private Banner banner;
    private List<String> imageUrl = new ArrayList<>();
    private PtrFrameLayout ptrFrameLayout;
    private Indicator indicator;
    private MyHandler myHandler = new MyHandler(this);
    private ImageView gouwucheImageView, headerGouwucheImageView, fenleiImageView, headerFenleiImageView;

    private static class MyHandler extends Handler {
        WeakReference<FragmentHomeQuanShang> fragmentHomeQuanShangWeakReference;

        public MyHandler(FragmentHomeQuanShang fragmentHomeQuanShang) {
            fragmentHomeQuanShangWeakReference = new WeakReference<>(fragmentHomeQuanShang);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentHomeQuanShang fragment = fragmentHomeQuanShangWeakReference.get();
            switch (msg.what) {
                case 0:
                    fragment.initViews();
                    fragment.startBanner();
                    if (null != fragment.list && fragment.list.size() > 0) {
                        fragment.adapter.addDatas(fragment.list);
                    }
                    break;
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    if (null != fragment.adapter) {
                        fragment.adapter.addDatas(fragment.list);
                    }
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_quanshang, null);
        initPtr();
        requestMallHomeAd();
        requestMallHomeAdGoods();
        return view;
    }


    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        headerView = view.findViewById(R.id.headerView);
        gouwucheImageView = view.findViewById(R.id.gouwucheImageView);
        gouwucheImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gouwucheIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(gouwucheIntent);
            }
        });
        fenleiImageView = view.findViewById(R.id.fenleiImageView);
        fenleiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fenleiIntent = new Intent(getActivity(), QuanShangFenLeiActivity.class);
                startActivity(fenleiIntent);

            }
        });
        adapter = new HomeQuanShangRecyclerAdapter(getContext());
        homeQuanShangRecyclerViewHeaderView = new HomeQuanShangRecyclerViewHeaderView(getContext());
        homeQuanShangRecyclerViewHeaderView.setData(mallAdResult.getData().getList());
        headerHeaderView = homeQuanShangRecyclerViewHeaderView.findViewById(R.id.headerHeaderView);
        headerGouwucheImageView = homeQuanShangRecyclerViewHeaderView.findViewById(R.id.gouwucheImageView);
        headerGouwucheImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gouwucheIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(gouwucheIntent);
            }
        });
        headerFenleiImageView = homeQuanShangRecyclerViewHeaderView.findViewById(R.id.fenleiImageView);
        headerFenleiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fenleiIntent = new Intent(getActivity(), QuanShangFenLeiActivity.class);
                startActivity(fenleiIntent);
            }
        });
        banner = homeQuanShangRecyclerViewHeaderView.findViewById(R.id.banner);
        indicator = homeQuanShangRecyclerViewHeaderView.findViewById(R.id.indicator);
        adapter.setHeaderView(homeQuanShangRecyclerViewHeaderView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                headerLayoutChange();
            }
        });
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
                page++;
                requestMallHomeAdGoods();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    private void startBanner() {
        indicator.addParams(imageUrl.size(), 0, 0);
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrl);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String url = mallAdResult.getData().getList().get(0).getContent().getAd_imgs().get(position).getHandel_url();
                gotoAdPage(url);
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > imageUrl.size()) {
                    position = 1;
                }
                if (position == 0) {
                    position = 1;
                }
                indicator.setCurrent(position - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void headerLayoutChange() {
        if (Math.abs(totalDy) > 0) {
            headerHeaderView.setVisibility(View.INVISIBLE);
            headerView.setVisibility(View.VISIBLE);
            headerView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            headerView.setAlpha(alpha);
        } else {
            headerHeaderView.setVisibility(View.VISIBLE);
            headerView.setAlpha(0);
            headerView.setVisibility(View.INVISIBLE);
        }
    }

    private MallAdResult mallAdResult;

    /**
     * 商城广告数据
     */
    private void requestMallHomeAd() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MALL_HOME_AD);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mallAdResult = GsonUtils.GsonToBean(result, MallAdResult.class);
                for (MallAdImg img : mallAdResult.getData().getList().get(0).getContent().getAd_imgs()) {
                    imageUrl.add(img.getImg());
                }
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();
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

    private MallAdGoodsResult mallAdGoodsResult;
    private int page = 1;

    /**
     * 广告商品
     */
    private void requestMallHomeAdGoods() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MALL_HOME_AD_GOODS);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mallAdGoodsResult = GsonUtils.GsonToBean(result, MallAdGoodsResult.class);
                if (mallAdGoodsResult.getCode() == 200) {
                    list = mallAdGoodsResult.getData().getGoods_list();
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();

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

    private void gotoAdPage(String url) {
        Intent intent = new Intent(getContext(), H5PageActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

}
