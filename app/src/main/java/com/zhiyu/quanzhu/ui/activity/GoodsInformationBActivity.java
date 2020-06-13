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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.GoodsImg;
import com.zhiyu.quanzhu.model.dao.GoodsNormStockDao;
import com.zhiyu.quanzhu.model.result.GoodsCommentResult;
import com.zhiyu.quanzhu.model.result.GoodsNormResult;
import com.zhiyu.quanzhu.model.result.GoodsResult;
import com.zhiyu.quanzhu.model.result.GoodsStockResult;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoCommentsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoLikeGoodsAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoLikeGoodsBAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoTagAdapter;
import com.zhiyu.quanzhu.ui.adapter.GoodsInformationGoodsImagesAdapter;
import com.zhiyu.quanzhu.ui.dialog.GoodsCouponsDialog;
import com.zhiyu.quanzhu.ui.dialog.GoodsNormsDialog;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.toast.SuccessToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.GoodsInfoBanner;
import com.zhiyu.quanzhu.ui.widget.ListViewForScrollView;
import com.zhiyu.quanzhu.ui.widget.MaxRecyclerView;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
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
public class GoodsInformationBActivity extends BaseActivity implements View.OnClickListener {
    private int screenWidth;
    private FrameLayout.LayoutParams ll;
    private ScrollView mScrollView;
    private View headerLayout;
    private LinearLayout guigelayout, backLayout, youhuiquanlayout, baozhanglayout, circleLayout;
    private ImageView backImageView, gouwucheImageView, collectImageView;
    private LinearLayout titleLayout, commentsLayout;
    private GoodsInfoBanner goodsInfoBanner;
    private MyRecyclerView tagRecyclerView;
    private GoodsInfoTagAdapter tagAdapter;
    private TextView titleTextView, priceTextView, goodsNameTextView, cityTextView, kucunTextView, xiaoliangTextView,
            maxCouponTextView, maxCouponDescTextView, guaranteeTextView;
    private GoodsNormsDialog normsDialog;
    private GoodsCouponsDialog youHuiQuanDialog;

    private MaxRecyclerView commentsRecyclerView;
    private GoodsInfoCommentsRecyclerAdapter commentsAdapter;

    private ListViewForScrollView mGoodsImgListView;
    private GoodsInformationGoodsImagesAdapter goodsImgsAdapter;

    private MaxRecyclerView likeGoodsRecyclerView;
    private GoodsInfoLikeGoodsBAdapter goodsInfoLikeGoodsAdapter;

    private LoadingDialog loadingDialog;
    private boolean isInfo, isLikeGoods = true, isGuiGe, isComments;

    private CircleImageView circleUserAvatarImageView;
    private TextView circleUserNameTextView, circleCreateTextView, circleNameTextView, circleDescTextView, circleCityTextView, circleIndustryTextView, chengyuanTextView, dongtaiTextView;
    private NiceImageView circleIconImageView;

    private TextView bottomShopTextView, bottomKeFuTextView, bottomShareTextView, bottomAddCartTextView, bottomBuyTextView;

    private ShareDialog shareDialog;
    private PayWayDialog payWayDialog;

    private MyHandler myHandler = new MyHandler(this);
    private long goods_id;

    private static class MyHandler extends Handler {
        WeakReference<GoodsInformationBActivity> shangPinInformationActivityWeakReference;

        public MyHandler(GoodsInformationBActivity activity) {
            shangPinInformationActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsInformationBActivity activity = shangPinInformationActivityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (null != activity.goodsResult && activity.goodsResult.getCode() == 200 && null != activity.goodsResult.getData() &&
                            null != activity.goodsResult.getData().getDetail()) {
                        activity.goodsInfoLikeGoods(activity.goodsResult.getData().getDetail().getShop_id());
                        List<GoodsImg> bannerList = new ArrayList<>();
                        if (!StringUtils.isNullOrEmpty(activity.goodsResult.getData().getDetail().getVideo())) {
                            GoodsImg goodsVideo = new GoodsImg();
                            goodsVideo.setUrl(activity.goodsResult.getData().getDetail().getVideo());
                            goodsVideo.setWidth(activity.goodsResult.getData().getDetail().getVideo_width());
                            goodsVideo.setHeight(activity.goodsResult.getData().getDetail().getVideo_height());
                            goodsVideo.setVideo(true);
                            bannerList.add(goodsVideo);
                        }
                        bannerList.addAll(activity.goodsResult.getData().getDetail().getImg_list());
                        activity.goodsInfoBanner.setList(bannerList);
                        if (activity.goodsResult.getData().isHas_norms()) {
                            activity.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getMin_price()) + "-" +
                                    PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getMax_price()));
                        } else {
                            activity.priceTextView.setText(PriceParseUtils.getInstance().parsePrice(activity.goodsResult.getData().getDetail().getGoods_price()));
                        }
                        if (null != activity.goodsResult.getData().getCoupon() && activity.goodsResult.getData().getCoupon().size() > 0) {
                            activity.youhuiquanlayout.setVisibility(View.VISIBLE);
                        } else {
                            activity.youhuiquanlayout.setVisibility(View.GONE);
                        }
                        if (null != activity.goodsResult.getData().getGuarantee() && activity.goodsResult.getData().getGuarantee().size() > 0) {
                            activity.baozhanglayout.setVisibility(View.VISIBLE);
                            String baozhang = "";
                            for (int i = 0; i < activity.goodsResult.getData().getGuarantee().size(); i++) {
                                baozhang += activity.goodsResult.getData().getGuarantee().get(i).getName();
                                if (i < activity.goodsResult.getData().getGuarantee().size() - 1) {
                                    baozhang += " | ";
                                }
                            }
                            activity.guaranteeTextView.setText(baozhang);
                        } else {
                            activity.baozhanglayout.setVisibility(View.GONE);
                        }
                        if (activity.goodsResult.getData().isHas_norms()) {
                            activity.guigelayout.setVisibility(View.VISIBLE);
                        } else {
                            activity.guigelayout.setVisibility(View.GONE);
                        }
                        activity.goodsNameTextView.setText(activity.goodsResult.getData().getDetail().getGoods_name());
                        activity.titleTextView.setText(activity.goodsResult.getData().getDetail().getGoods_name());
                        activity.tagAdapter.setList(activity.goodsResult.getData().getDetail().getTags());
                        if (activity.goodsResult.getData().getDetail().isIs_collect()) {
                            if (!activity.statusColorWhite) {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_yellow_yellow));
                            } else {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_yellow));
                            }
                        } else {
                            if (!activity.statusColorWhite) {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_black));
                            } else {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_white));
                            }
                        }
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
                        if (!StringUtils.isNullOrEmpty(activity.goodsResult.getData().getMax_coupon())) {
                            activity.maxCouponTextView.setText(activity.goodsResult.getData().getMax_coupon());
                            activity.maxCouponTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.maxCouponTextView.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isNullOrEmpty(activity.goodsResult.getData().getMax_coupon_desc())) {
                            activity.maxCouponDescTextView.setText(activity.goodsResult.getData().getMax_coupon_desc());
                            activity.maxCouponDescTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.maxCouponDescTextView.setVisibility(View.GONE);
                        }

                        activity.goodsImgsAdapter.setList(activity.goodsResult.getData().getDetail().getImg_json());

                        Glide.with(activity).load(activity.goodsResult.getData().getCircle().getUser_avatar()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.circleUserAvatarImageView);
                        activity.circleUserNameTextView.setText(activity.goodsResult.getData().getCircle().getUser_name());
                        activity.circleCreateTextView.setText(String.valueOf(activity.goodsResult.getData().getCircle().getDays()));
                        Glide.with(activity).load(activity.goodsResult.getData().getCircle().getThumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                                .fallback(R.drawable.image_error).into(activity.circleIconImageView);
                        activity.circleNameTextView.setText(activity.goodsResult.getData().getCircle().getName());
                        activity.circleDescTextView.setText(activity.goodsResult.getData().getCircle().getDescirption());
                        activity.circleCityTextView.setText(activity.goodsResult.getData().getCircle().getCity_name());
                        activity.circleIndustryTextView.setText(activity.goodsResult.getData().getCircle().getIndustry());
                        activity.chengyuanTextView.setText(String.valueOf(activity.goodsResult.getData().getCircle().getPnum()));
                        activity.dongtaiTextView.setText(String.valueOf(activity.goodsResult.getData().getCircle().getFnum()));

                        activity.scrollTop();
                    } else {
                        Toast.makeText(activity, "暂无数据", Toast.LENGTH_LONG).show();
                    }
                    if (activity.isInfo && activity.isComments && activity.isGuiGe && activity.isLikeGoods) {
                        activity.loadingDialog.dismiss();
                    }
                    break;
                case 1://商品规格数据
                    if (null != activity.goodsNormResult && activity.goodsNormResult.getCode() == 200 &&
                            null != activity.goodsNormResult.getData() && null != activity.goodsNormResult.getData().getList() &&
                            activity.goodsNormResult.getData().getList().size() > 0) {
                        activity.normsDialog.setGuiGeList(activity.goodsNormResult.getData().getList());
                        activity.scrollTop();
                    }
                    if (activity.isInfo && activity.isComments && activity.isGuiGe && activity.isLikeGoods) {
                        activity.loadingDialog.dismiss();
                    }
                    break;
                case 2://精选评论
                    if (null != activity.goodsCommentResult && activity.goodsCommentResult.getCode() == 200 &&
                            null != activity.goodsCommentResult.getData() && null != activity.goodsCommentResult.getData().getList()
                            && activity.goodsCommentResult.getData().getList().size() > 0) {
                        activity.commentsAdapter.setList(activity.goodsCommentResult.getData().getList());
                        activity.scrollTop();
                    }
                    if (activity.isInfo && activity.isComments && activity.isGuiGe && activity.isLikeGoods) {
                        activity.loadingDialog.dismiss();
                    }
                    break;
                case 3://猜你喜欢
                    if (null != activity.likeGoodsResult && activity.likeGoodsResult.getCode() == 200 &&
                            null != activity.likeGoodsResult.getData() && null != activity.likeGoodsResult.getData().getGoods_list()
                            && activity.likeGoodsResult.getData().getGoods_list().size() > 0) {
                        activity.goodsInfoLikeGoodsAdapter.setList(activity.likeGoodsResult.getData().getGoods_list());
                        activity.scrollTop();
                    }
                    if (activity.isInfo && activity.isComments && activity.isGuiGe && activity.isLikeGoods) {
                        activity.loadingDialog.dismiss();
                    }
                    break;
                case 4:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.goodsResult.getData().getDetail().setIs_collect(!activity.goodsResult.getData().getDetail().isIs_collect());
                        if (activity.goodsResult.getData().getDetail().isIs_collect()) {
                            if (!activity.statusColorWhite) {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_yellow_yellow));
                            } else {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_yellow));
                            }
                        } else {
                            if (!activity.statusColorWhite) {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_black));
                            } else {
                                activity.collectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.goods_info_collect_white));
                            }
                        }
                    }
                    break;
                case 5:
                    SuccessToast.getInstance(activity).show("加入成功");
                    break;
                case 6:
                    FailureToast.getInstance(activity).show();
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试.");
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_information);
        goods_id = getIntent().getIntExtra("goods_id", 0);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initDatas();
        initViews();
        initDialogs();

        loadingDialog.show();
        goodsInformation();
        goodsInfoComments();
        goodsInfoGoodsNorms();
        goodsInfoGoodsStock();
    }


    private void initDialogs() {
        normsDialog = new GoodsNormsDialog(this, R.style.dialog);
        youHuiQuanDialog = new GoodsCouponsDialog(this, R.style.dialog);
        loadingDialog = new LoadingDialog(this, R.style.dialog);
        shareDialog = new ShareDialog(this, this, R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {

            }
        });
        payWayDialog = new PayWayDialog(this, R.style.dialog, new PayWayDialog.OnPayWayListener() {
            @Override
            public void onPayWay(int payWay, String payWayStr) {

            }
        });
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
        baozhanglayout = findViewById(R.id.baozhanglayout);
        backLayout = findViewById(R.id.backLayout);
        youhuiquanlayout.setOnClickListener(this);
        guigelayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        backImageView = findViewById(R.id.backImageView);
        gouwucheImageView = findViewById(R.id.gouwucheImageView);
        gouwucheImageView.setOnClickListener(this);
        collectImageView = findViewById(R.id.collectImageView);
        collectImageView.setOnClickListener(this);
        tagRecyclerView = findViewById(R.id.tagRecyclerView);
        LinearLayoutManager tagLayoutManager = new LinearLayoutManager(this);
        tagLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tagRecyclerView.setLayoutManager(tagLayoutManager);
        tagAdapter = new GoodsInfoTagAdapter();
        tagRecyclerView.setAdapter(tagAdapter);
        titleLayout = findViewById(R.id.titleLayout);
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        goodsNameTextView = findViewById(R.id.goodsNameTextView);
        cityTextView = findViewById(R.id.cityTextView);
        kucunTextView = findViewById(R.id.kucunTextView);
        xiaoliangTextView = findViewById(R.id.xiaoliangTextView);
        maxCouponTextView = findViewById(R.id.maxCouponTextView);
        maxCouponDescTextView = findViewById(R.id.maxCouponDescTextView);
        guaranteeTextView = findViewById(R.id.guaranteeTextView);
        //精选评论
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setFocusable(false);
        commentsAdapter = new GoodsInfoCommentsRecyclerAdapter(this);
        LinearLayoutManager commentsLayoutManager = new LinearLayoutManager(this);
        commentsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(commentsLayoutManager);

        circleLayout = findViewById(R.id.circleLayout);
        circleLayout.setOnClickListener(this);
        circleUserAvatarImageView = findViewById(R.id.circleUserAvatarImageView);
        circleUserNameTextView = findViewById(R.id.circleUserNameTextView);
        circleCreateTextView = findViewById(R.id.circleCreateTextView);
        circleNameTextView = findViewById(R.id.circleNameTextView);
        circleDescTextView = findViewById(R.id.circleDescTextView);
        circleCityTextView = findViewById(R.id.circleCityTextView);
        circleIndustryTextView = findViewById(R.id.circleIndustryTextView);
        circleIconImageView = findViewById(R.id.circleIconImageView);
        chengyuanTextView = findViewById(R.id.chengyuanTextView);
        dongtaiTextView = findViewById(R.id.dongtaiTextView);

        mGoodsImgListView = findViewById(R.id.mGoodsImgListView);
        mGoodsImgListView.setFocusable(false);
        goodsImgsAdapter = new GoodsInformationGoodsImagesAdapter(this);
        mGoodsImgListView.setAdapter(goodsImgsAdapter);


        likeGoodsRecyclerView = findViewById(R.id.likeGoodsRecyclerView);
        likeGoodsRecyclerView.setFocusable(false);
        goodsInfoLikeGoodsAdapter = new GoodsInfoLikeGoodsBAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(2, (int) getResources().getDimension(R.dimen.dp_15), true);
        likeGoodsRecyclerView.setLayoutManager(gridLayoutManager);
        likeGoodsRecyclerView.addItemDecoration(decoration);
        likeGoodsRecyclerView.setAdapter(goodsInfoLikeGoodsAdapter);
        likeGoodsRecyclerView.setNestedScrollingEnabled(false);//禁止滑动

        commentsLayout = findViewById(R.id.commentsLayout);
        commentsLayout.setOnClickListener(this);

        bottomShopTextView = findViewById(R.id.bottomShopTextView);
        bottomShopTextView.setOnClickListener(this);
        bottomKeFuTextView = findViewById(R.id.bottomKeFuTextView);
        bottomKeFuTextView.setOnClickListener(this);
        bottomShareTextView = findViewById(R.id.bottomShareTextView);
        bottomShareTextView.setOnClickListener(this);
        bottomAddCartTextView = findViewById(R.id.bottomAddCartTextView);
        bottomAddCartTextView.setOnClickListener(this);
        bottomBuyTextView = findViewById(R.id.bottomBuyTextView);
        bottomBuyTextView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guigelayout:
                normsDialog.show();
                normsDialog.setGoods(goodsResult.getData().getDetail(), goodsResult.getData().isHas_norms());
                normsDialog.setType(0);
                break;
            case R.id.backLayout:
                finish();
                break;
            case R.id.youhuiquanlayout:
                if (null != goodsResult.getData().getCoupon() && goodsResult.getData().getCoupon().size() > 0) {
                    youHuiQuanDialog.show();
                    youHuiQuanDialog.setShopId((int) goodsResult.getData().getDetail().getShop_id());
//                    youHuiQuanDialog.setList(goodsResult.getData().getCoupon());
                } else {
                    MessageToast.getInstance(this).show("暂无优惠券");
                }
                break;
            case R.id.commentsLayout:
                Intent commentsIntent = new Intent(this, GoodsInfoAllCommentsActivity.class);
                commentsIntent.putExtra("goods_id", goods_id);
                startActivity(commentsIntent);
                break;
            case R.id.gouwucheImageView:
                Intent gouwucheIntent = new Intent(GoodsInformationBActivity.this, CartActivity.class);
                startActivity(gouwucheIntent);
                break;
            case R.id.collectImageView:
                if (null != goodsResult && null != goodsResult.getData() && null != goodsResult.getData().getDetail()) {
                    collectGoods();
                }
                break;
            case R.id.bottomShopTextView:
                Intent shopIntent = new Intent(this, ShopInformationActivity.class);
                shopIntent.putExtra("shop_id", String.valueOf(goodsResult.getData().getDetail().getShop_id()));
                startActivity(shopIntent);
                break;
            case R.id.bottomKeFuTextView:
                Intent goodsIntent = new Intent(this, CustomerServiceActivity.class);
                goodsIntent.putExtra("shop_id", (int) goodsResult.getData().getDetail().getShop_id());
                startActivity(goodsIntent);
                break;
            case R.id.bottomShareTextView:
                shareDialog.show();
                break;
            case R.id.bottomAddCartTextView:
                normsDialog.show();
                normsDialog.setGoods(goodsResult.getData().getDetail(), goodsResult.getData().isHas_norms());
                normsDialog.setType(1);
                break;
            case R.id.bottomBuyTextView:
                normsDialog.show();
                normsDialog.setGoods(goodsResult.getData().getDetail(), goodsResult.getData().isHas_norms());
                normsDialog.setType(2);
                break;
            case R.id.circleLayout:
                if (null != goodsResult && null != goodsResult.getData().getCircle()) {
                    Intent intent = new Intent(this, CircleInfoActivity.class);
                    intent.putExtra("circle_id", goodsResult.getData().getCircle().getId());
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean statusColorWhite = true;

    private void headerChange() {
        if (Math.abs(totalDy) > 30) {
            headerLayout.setBackgroundColor(getResources().getColor(R.color.white));
            backImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_back_black));
            gouwucheImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_gouwuche_black));
            if (goodsResult.getData().getDetail().isIs_collect()) {
                collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_yellow_yellow));
            } else {
                collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_black));
            }

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
            if (goodsResult.getData().getDetail().isIs_collect()) {
                collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_yellow));
            } else {
                collectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.goods_info_collect_white));
            }

            titleLayout.setVisibility(View.INVISIBLE);
            headerLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            headerLayout.setAlpha(1);
        }
    }

    private void scrollTop() {
        mScrollView.scrollTo(0, 0);
    }

    private GoodsResult goodsResult;

    /**
     * 商品详情
     */
    private void goodsInformation() {
        isInfo = false;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        params.addBodyParameter("spm", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isInfo = true;
                System.out.println("商品详情 " + result);
                goodsResult = GsonUtils.GsonToBean(result, GoodsResult.class);
                Message message = myHandler.obtainMessage(0);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isInfo = true;
                Message message = myHandler.obtainMessage(0);
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

    private GoodsResult likeGoodsResult;

    /**
     * 猜你喜欢
     */
    private void goodsInfoLikeGoods(long shop_id) {
        isLikeGoods = false;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_LIKE_GOODS);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isLikeGoods = true;
                System.out.println("猜你喜欢" + result);
                likeGoodsResult = GsonUtils.GsonToBean(result, GoodsResult.class);
                System.out.println("c猜你喜欢" + likeGoodsResult.getData().getGoods_list().size());
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isLikeGoods = true;
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("猜你喜欢" + ex.toString());
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
        isComments = false;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_COMMENTS);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isComments = true;
                System.out.println("精选评价:" + result);
                goodsCommentResult = GsonUtils.GsonToBean(result, GoodsCommentResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                isComments = true;
                Message message = myHandler.obtainMessage(2);
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

    private GoodsNormResult goodsNormResult;

    /**
     * 商品规格
     */
    private void goodsInfoGoodsNorms() {
        isGuiGe = false;
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_NORMS);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isGuiGe = true;
                System.out.println("商品规格:" + result);
                goodsNormResult = GsonUtils.GsonToBean(result, GoodsNormResult.class);
//                if (null != goodsNormResult && null != goodsNormResult.getData() && null != goodsNormResult.getData().getList() && goodsNormResult.getData().getList().size() > 0) {
//                    List<GoodsNorm> goodsNormList = new ArrayList<>();
//                    goodsNormList.add(goodsNormResult.getData().getList().get(0).getList().get(1));
//                    goodsNormList.add(goodsNormResult.getData().getList().get(1).getList().get(1));
//                    goodsNormList.add(goodsNormResult.getData().getList().get(2));
//                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("norms: " + ex.toString());
                isGuiGe = true;
                Message message = myHandler.obtainMessage(1);
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


    private GoodsStockResult goodsStockResult;

    /**
     * 商品规格对应库存
     */
    private void goodsInfoGoodsStock() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_INFO_GOODS_STOCK);
        params.addBodyParameter("goods_id", String.valueOf(goods_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("商品规格对应库存:" + result);
                goodsStockResult = GsonUtils.GsonToBean(result, GoodsStockResult.class);
                GoodsNormStockDao.getInstance().saveStockList(goodsStockResult.getData().getList());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("商品规格对应库存:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private BaseResult baseResult;

    private void collectGoods() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(goods_id));
        params.addBodyParameter("module_type", "goods");
        params.addBodyParameter("type", goodsResult.getData().getDetail().isIs_collect() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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


    //加入购物车
    private void addCart() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.ADD_CART);
        params.addBodyParameter("goods_id", String.valueOf(goodsResult.getData().getDetail().getId()));
        params.addBodyParameter("goods_num", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    Message message = myHandler.obtainMessage(5);
                    message.sendToTarget();
                } else {
                    Message message = myHandler.obtainMessage(6);
                    message.sendToTarget();
                }
                System.out.println(baseResult.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }
}
