package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.dao.GoodsNormStockDao;
import com.zhiyu.quanzhu.model.result.GoodsCommentResult;
import com.zhiyu.quanzhu.model.result.GoodsNormResult;
import com.zhiyu.quanzhu.model.result.GoodsResult;
import com.zhiyu.quanzhu.model.result.GoodsStockResult;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoCommentsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoGoodsImgsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoLikeGoodsAdapter;
import com.zhiyu.quanzhu.ui.dialog.ShangPinInformationGuiGeDialog;
import com.zhiyu.quanzhu.ui.dialog.ShangPinInformationYouHuiQuanDialog;
import com.zhiyu.quanzhu.ui.widget.GoodsInfoBanner;
import com.zhiyu.quanzhu.ui.widget.MaxRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 */
public class GoodsInformationActivity extends BaseActivity implements View.OnClickListener {
    private int screenWidth;
    private FrameLayout.LayoutParams ll;
    private ScrollView mScrollView;
    private View headerLayout;
    private LinearLayout guigelayout, backLayout, youhuiquanlayout;
    private ImageView backImageView, gouwucheImageView, collectImageView;
    private LinearLayout titleLayout, commentsLayout;
    private GoodsInfoBanner goodsInfoBanner;
    private TextView titleTextView, priceTextView, tagTextView1, tagTextView2, goodsNameTextView, cityTextView, kucunTextView, xiaoliangTextView,
            maxCouponTextView, guaranteeTextView;
    private ShangPinInformationGuiGeDialog guiGeDialog;
    private ShangPinInformationYouHuiQuanDialog youHuiQuanDialog;

    private MaxRecyclerView commentsRecyclerView;
    private GoodsInfoCommentsRecyclerAdapter commentsAdapter;

    private LinearLayout goodsImgsLayout;
    private MaxRecyclerView goodsImgsRecyclerView;
    private GoodsInfoGoodsImgsRecyclerAdapter goodsImgsRecyclerAdapter;

    private MaxRecyclerView likeGoodsRecyclerView;
    private GoodsInfoLikeGoodsAdapter goodsInfoLikeGoodsAdapter;

    private MyHandler myHandler = new MyHandler(this);
    private long goods_id;

    private static class MyHandler extends Handler {
        WeakReference<GoodsInformationActivity> shangPinInformationActivityWeakReference;

        public MyHandler(GoodsInformationActivity activity) {
            shangPinInformationActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsInformationActivity activity = shangPinInformationActivityWeakReference.get();
            switch (msg.what) {
                case 0:
                    List<String> bannerList = new ArrayList<>();
                    bannerList.add(activity.goodsResult.getData().getDetail().getVideo());
                    bannerList.addAll(activity.goodsResult.getData().getDetail().getImg_list());
                    activity.goodsInfoBanner.setList(bannerList);
                    if (activity.goodsResult.getData().isHas_norms()) {
                        activity.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getMin_price()) + "-" +
                                PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getMax_price()));
                    } else {
                        activity.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getGoods_price()));
                    }
                    activity.goodsNameTextView.setText(activity.goodsResult.getData().getDetail().getGoods_name());
                    activity.titleTextView.setText(activity.goodsResult.getData().getDetail().getGoods_name());
                    activity.cityTextView.setText(activity.goodsResult.getData().getDetail().getSend_city());
                    activity.kucunTextView.setText(String.valueOf(activity.goodsResult.getData().getDetail().getGoods_stock()));
                    activity.xiaoliangTextView.setText(String.valueOf(activity.goodsResult.getData().getDetail().getSale_num()));
                    if (null != activity.goodsResult.getData().getGuarantee() && activity.goodsResult.getData().getGuarantee().size() > 0) {
                        String guarantee = "";
                        for (int i = 0; i < activity.goodsResult.getData().getGuarantee().size(); i++) {
                            guarantee += activity.goodsResult.getData().getGuarantee().get(i).getName();
                            if (i != (activity.goodsResult.getData().getGuarantee().size() - 1)) {
                                guarantee += " | ";
                            }
                        }
                        activity.guaranteeTextView.setText("#" + guarantee);
                    }
                    activity.maxCouponTextView.setText(activity.goodsResult.getData().getMax_coupon());

                    activity.goodsImgsRecyclerAdapter.setList(activity.goodsResult.getData().getDetail().getImg_json());
                    if (activity.goodsImgsRecyclerView.getRecycledViewPool() != null) {
                        activity.goodsImgsRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
                    }
//                    activity.addGoodsImages();
                    break;
                case 1://商品规格数据
                    activity.guiGeDialog.setGuiGeList(activity.goodsNormResult.getData().getList());
                    break;
                case 2://精选评论
                    activity.commentsAdapter.setList(activity.goodsCommentResult.getData().getList());
                    break;
                case 3://猜你喜欢
                    activity.goodsInfoLikeGoodsAdapter.setList(activity.likeGoodsResult.getData().getGoods_list());
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_information);
        goods_id = getIntent().getLongExtra("goods_id", 0l);
        goods_id = 1l;
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initDatas();
        initViews();
        initDialogs();

        goodsInfo();
        goodsInfoComments();
        goodsInfoGoodsNorms();
        goodsInfoGoodsStock();
    }

    private void initDialogs() {
        guiGeDialog = new ShangPinInformationGuiGeDialog(this, R.style.dialog);
        youHuiQuanDialog = new ShangPinInformationYouHuiQuanDialog(this, R.style.dialog);
    }

    private void initDatas() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        ll = new FrameLayout.LayoutParams(screenWidth, screenWidth);
    }

    private int totalDy;

    private void initViews() {
        mScrollView = findViewById(R.id.mScrollView);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                totalDy = scrollY;
                headerChange();
            }
        });
        goodsInfoBanner = findViewById(R.id.goodsInfoBanner);
        headerLayout = findViewById(R.id.headerLayout);
        guigelayout = findViewById(R.id.guigelayout);
        youhuiquanlayout = findViewById(R.id.youhuiquanlayout);
        backLayout = findViewById(R.id.backLayout);
        youhuiquanlayout.setOnClickListener(this);
        guigelayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        backImageView = findViewById(R.id.backImageView);
        gouwucheImageView = findViewById(R.id.gouwucheImageView);
        gouwucheImageView.setOnClickListener(this);
        collectImageView = findViewById(R.id.collectImageView);
        collectImageView.setOnClickListener(this);
        titleLayout = findViewById(R.id.titleLayout);
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        tagTextView1 = findViewById(R.id.tagTextView1);
        tagTextView2 = findViewById(R.id.tagTextView2);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        cityTextView = findViewById(R.id.cityTextView);
        kucunTextView = findViewById(R.id.kucunTextView);
        xiaoliangTextView = findViewById(R.id.xiaoliangTextView);
        maxCouponTextView = findViewById(R.id.maxCouponTextView);
        guaranteeTextView = findViewById(R.id.guaranteeTextView);
        //精选评论
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsAdapter = new GoodsInfoCommentsRecyclerAdapter(this);
        LinearLayoutManager commentsLayoutManager = new LinearLayoutManager(this);
        commentsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(commentsLayoutManager);

        goodsImgsLayout = findViewById(R.id.goodsImgsLayout);
        goodsImgsRecyclerView = findViewById(R.id.goodsImgsRecyclerView);
        goodsImgsRecyclerAdapter = new GoodsInfoGoodsImgsRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goodsImgsRecyclerView.setLayoutManager(linearLayoutManager);
        goodsImgsRecyclerView.setAdapter(goodsImgsRecyclerAdapter);
        goodsImgsRecyclerView.setNestedScrollingEnabled(false);//禁止滑动


        likeGoodsRecyclerView = findViewById(R.id.likeGoodsRecyclerView);
        goodsInfoLikeGoodsAdapter = new GoodsInfoLikeGoodsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        likeGoodsRecyclerView.setLayoutManager(gridLayoutManager);
        likeGoodsRecyclerView.setAdapter(goodsInfoLikeGoodsAdapter);
        likeGoodsRecyclerView.setNestedScrollingEnabled(false);//禁止滑动

        commentsLayout = findViewById(R.id.commentsLayout);
        commentsLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guigelayout:
                guiGeDialog.show();
                guiGeDialog.setGoods(goodsResult.getData().getDetail());
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.youhuiquanlayout:
                youHuiQuanDialog.show();
                youHuiQuanDialog.setList(goodsResult.getData().getCoupon());
                break;
            case R.id.commentsLayout:
                Intent commentsIntent = new Intent(this, GoodsInfoAllCommentsActivity.class);
                commentsIntent.putExtra("goods_id", goods_id);
                startActivity(commentsIntent);
                break;
            case R.id.gouwucheImageView:
                Intent gouwucheIntent=new Intent(GoodsInformationActivity.this,CartActivity.class);
                startActivity(gouwucheIntent);
                break;
            case R.id.collectImageView:

                break;
        }
    }

    private boolean statusColorWhite = true;

    private void headerChange() {
        if (Math.abs(totalDy) > 30) {
            headerLayout.setBackgroundColor(getResources().getColor(R.color.white));
            backImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_back_black));
            gouwucheImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_gouwuche_black));
            collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_black));
            titleLayout.setVisibility(View.VISIBLE);
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            headerLayout.setAlpha(alpha);
            if (statusColorWhite) {
                ScreentUtils.getInstance().setStatusBarLightMode(this, true);
                statusColorWhite = false;
            }
        } else {
            if (!statusColorWhite) {
                ScreentUtils.getInstance().setStatusBarLightMode(this, false);
                statusColorWhite = true;
            }
            backImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_back));
            gouwucheImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_gouwuche));
            collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_white));
            titleLayout.setVisibility(View.INVISIBLE);
            headerLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            headerLayout.setAlpha(1);
        }
    }

    private void addGoodsImages() {
        if (null != goodsResult.getData().getDetail().getImg_json() && goodsResult.getData().getDetail().getImg_json().size() > 0) {
            goodsResult.getData().getDetail().getImg_json().add(0, "https://c-ssl.duitang.com/uploads/item/201711/06/20171106035744_xNyTs.jpeg");
            goodsResult.getData().getDetail().getImg_json().add(0, "https://c-ssl.duitang.com/uploads/item/201507/30/20150730100628_BXhFx.jpeg");
            goodsResult.getData().getDetail().getImg_json().add(0, "https://c-ssl.duitang.com/uploads/item/201608/03/20160803145649_fQXrE.jpeg");
            goodsResult.getData().getDetail().getImg_json().add(0, "https://c-ssl.duitang.com/uploads/item/201907/14/20190714111310_eltte.jpg");
            for (String imageUrl : goodsResult.getData().getDetail().getImg_json()) {
                ImageView imageView = new ImageView(this);
                Glide.with(this).load(imageUrl).into(imageView);
                goodsImgsLayout.addView(imageView);
            }
        }
    }

    private GoodsResult goodsResult;

    /**
     * 商品详情
     */
    private void goodsInfo() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("spm", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品详情 " + result);
                goodsResult = GsonUtils.GsonToBean(result, GoodsResult.class);
                goodsInfoLikeGoods(goodsResult.getData().getDetail().getShop_id());
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

    private GoodsResult likeGoodsResult;

    /**
     * 猜你喜欢
     */
    private void goodsInfoLikeGoods(long shop_id) {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_LIKE_GOODS);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                likeGoodsResult = GsonUtils.GsonToBean(result, GoodsResult.class);
                System.out.println("c猜你喜欢" + likeGoodsResult.getData().getGoods_list().size());
                Message message = myHandler.obtainMessage(3);
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

    private GoodsCommentResult goodsCommentResult;

    /**
     * 精选评价
     */
    private void goodsInfoComments() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_COMMENTS);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                goodsCommentResult = GsonUtils.GsonToBean(result, GoodsCommentResult.class);
                Message message = myHandler.obtainMessage(2);
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

    private GoodsNormResult goodsNormResult;

    /**
     * 商品规格
     */
    private void goodsInfoGoodsNorms() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_NORMS);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                goodsNormResult = GsonUtils.GsonToBean(result, GoodsNormResult.class);
                List<GoodsNorm> goodsNormList = new ArrayList<>();
                goodsNormList.add(goodsNormResult.getData().getList().get(0).getList().get(1));
                goodsNormList.add(goodsNormResult.getData().getList().get(1).getList().get(1));
//                goodsNormList.add(goodsNormResult.getData().getList().get(2));
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


    private GoodsStockResult goodsStockResult;

    /**
     * 商品规格对应库存
     */
    private void goodsInfoGoodsStock() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_STOCK);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                goodsStockResult = GsonUtils.GsonToBean(result, GoodsStockResult.class);
                GoodsNormStockDao.getInstance().saveStockList(goodsStockResult.getData().getList());
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
