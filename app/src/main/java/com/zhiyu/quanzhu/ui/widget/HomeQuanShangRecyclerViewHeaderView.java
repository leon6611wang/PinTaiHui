package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MallAd;
import com.zhiyu.quanzhu.ui.activity.GoodsSearchActivity;
import com.zhiyu.quanzhu.ui.activity.H5PageActivity;
import com.zhiyu.quanzhu.ui.activity.ScanActivity;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class HomeQuanShangRecyclerViewHeaderView extends LinearLayout implements View.OnClickListener {
    private LinearLayout rootLayout, toplayout;
    private Context context;
    private LinearLayout.LayoutParams params1, params2, params3, params4, paramsBanner;
    private float ratio1, ratio2, ratio3, ratio4, ratioBanner;
    private int height1, height2, height3, height4, heightBanner;
    private int screenWidth;
    private int dp_15, dp_10, dp_5;
    private CardView bannerCardView;
    private List<MallAd> list;
    private TextView searchTextView;
    private RelativeLayout saoyisaolayout;

    public void setData(List<MallAd> adList) {
        this.list = adList;
        initViews();
    }

    public HomeQuanShangRecyclerViewHeaderView(Context context) {
        super(context);
        this.context = context;
        initData();

    }

    public HomeQuanShangRecyclerViewHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    private void initData() {
        ratio1 = 0.2174f;
        ratio2 = 0.3787f;
        ratio3 = 0.5627f;
        ratio4 = 0.5627f;
        ratioBanner = 0.5217f;
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        height1 = (int) (ratio1 * screenWidth);
        height2 = (int) (ratio2 * screenWidth);
        height3 = (int) (ratio3 * screenWidth);
        height4 = (int) (ratio4 * screenWidth);
        heightBanner = (int) (ratioBanner * screenWidth);
        params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height1);
        params1.leftMargin = dp_15;
        params1.rightMargin = dp_15;
//        params1.bottomMargin = dp_10;
        params1.topMargin = dp_5;
        params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height2);
        params2.topMargin = dp_5;
        params2.bottomMargin = dp_5;
        params3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height3);
        params3.topMargin = dp_5;
        params3.bottomMargin = dp_5;
        params4 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height4);
        params4.topMargin = dp_5;
        params4.bottomMargin = dp_5;
        paramsBanner = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, heightBanner);
        paramsBanner.leftMargin = dp_15;
        paramsBanner.rightMargin = dp_15;
        paramsBanner.topMargin = dp_5;
//        paramsBanner.bottomMargin = dp_5;
    }

    private void initViews() {
        rootLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview, null);
        bannerCardView = rootLayout.findViewById(R.id.bannerCardView);
        bannerCardView.setLayoutParams(paramsBanner);
        toplayout = rootLayout.findViewById(R.id.toplayout);
        searchTextView = rootLayout.findViewById(R.id.searchTextView);
        searchTextView.setOnClickListener(this);
        saoyisaolayout = rootLayout.findViewById(R.id.saoyisaolayout);
        saoyisaolayout.setOnClickListener(this);

        for (MallAd ad : list) {
            if (ad.getType().equals("1")) {
                View view1 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_1, null);
                view1.setLayoutParams(params1);
                ImageView adImageView = view1.findViewById(R.id.adImageView);
                String url = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    url = ad.getContent().getAd_imgs().get(0).getHandle_url();
                }
                final String url0 = url;
                adImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url0);
                    }
                });
                String imgUrl = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    imgUrl = ad.getContent().getAd_imgs().get(0).getImg();
                }
                Glide.with(context).load(imgUrl)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(adImageView);
                toplayout.addView(view1);

            }
            if (ad.getType().equals("2")) {
                View view2 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_2, null);
                view2.setLayoutParams(params2);
                ImageView leftImageView = view2.findViewById(R.id.leftImageView);
                String pageUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    pageUrl0 = ad.getContent().getAd_imgs().get(0).getHandle_url();
                }
                final String url0 = pageUrl0;
                leftImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url0);
                    }
                });
                String imgUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    imgUrl0 = ad.getContent().getAd_imgs().get(0).getImg();
                }
                Glide.with(context).load(imgUrl0)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(leftImageView);
                ImageView rightImageViwe = view2.findViewById(R.id.rightImageView);

                String pageUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1) {
                    pageUrl1 = ad.getContent().getAd_imgs().get(1).getHandle_url();
                }
                final String url1 = pageUrl1;
                rightImageViwe.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url1);
                    }
                });
                String imageUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1) {
                    imageUrl1 = ad.getContent().getAd_imgs().get(1).getImg();
                }
                Glide.with(context).load(imageUrl1)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(rightImageViwe);

                rootLayout.addView(view2);
            }
            if (ad.getType().equals("3")) {
                View view3 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_3, null);
                view3.setLayoutParams(params3);
                ImageView leftImageView = view3.findViewById(R.id.leftImageView);
                String pageUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    pageUrl0 = ad.getContent().getAd_imgs().get(0).getHandle_url();
                }
                final String url0 = pageUrl0;
                leftImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url0);
                    }
                });
                String imageUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    imageUrl0 = ad.getContent().getAd_imgs().get(0).getImg();
                }
                Glide.with(context).load(imageUrl0)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(leftImageView);
                ImageView rightTopImageView = view3.findViewById(R.id.rightTopImageView);
                String pageUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1) {
                    pageUrl1 = ad.getContent().getAd_imgs().get(1).getHandle_url();
                }
                final String url1 = pageUrl1;
                rightTopImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url1);
                    }
                });
                String imageUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1)
                    imageUrl1 = ad.getContent().getAd_imgs().get(1).getImg();
                Glide.with(context).load(imageUrl1)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(rightTopImageView);
                ImageView rightBottomImageView = view3.findViewById(R.id.rightBottomImageView);
                String pageUrl2 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 2) {
                    pageUrl2 = ad.getContent().getAd_imgs().get(2).getHandle_url();
                }
                final String url2 = pageUrl2;
                rightBottomImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url2);
                    }
                });
                String imageUrl2 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 2)
                    imageUrl2 = ad.getContent().getAd_imgs().get(2).getImg();
                Glide.with(context).load(imageUrl2)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(rightBottomImageView);
                rootLayout.addView(view3);
            }
            if (ad.getType().equals("4")) {
                View view4 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_4, null);
                view4.setLayoutParams(params4);
                ImageView topLeftImageView = view4.findViewById(R.id.topLeftImageView);
                String pageUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0) {
                    pageUrl0 = ad.getContent().getAd_imgs().get(0).getHandle_url();
                }
                final String url0 = pageUrl0;
                topLeftImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url0);
                    }
                });
                String imageUrl0 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 0)
                    imageUrl0 = ad.getContent().getAd_imgs().get(0).getImg();
                Glide.with(context).load(imageUrl0)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(topLeftImageView);
                ImageView topRightImageView = view4.findViewById(R.id.topRightImageView);
                String pageUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1) {
                    pageUrl1 = ad.getContent().getAd_imgs().get(1).getHandle_url();
                }
                final String url1 = pageUrl1;
                topRightImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url1);
                    }
                });
                String imgeUrl1 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 1)
                    imgeUrl1 = ad.getContent().getAd_imgs().get(1).getImg();
                Glide.with(context).load(imgeUrl1)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(topRightImageView);
                ImageView bottomLeftImageView = view4.findViewById(R.id.bottomLeftImageView);
                String pageUrl2 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 2) {
                    pageUrl2 = ad.getContent().getAd_imgs().get(2).getHandle_url();
                }
                final String url2 = pageUrl2;
                bottomLeftImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoAdPage(url2);
                    }
                });
                String imageUrl2 = "";
                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 2)
                    imageUrl2 = ad.getContent().getAd_imgs().get(2).getImg();
                Glide.with(context).load(imageUrl2)
                        //异常时候显示的图片
                        .error(R.drawable.image_error)
                        //加载成功前显示的图片
                        .placeholder(R.drawable.image_error)
                        //url为空的时候,显示的图片
                        .fallback(R.drawable.image_error)
                        .into(bottomLeftImageView);
                ImageView bottomRightImageView = view4.findViewById(R.id.bottomRightImageView);

                if (null != ad.getContent().getAd_imgs() && ad.getContent().getAd_imgs().size() > 3) {
                    final String url3 = ad.getContent().getAd_imgs().get(3).getHandle_url();
                    bottomRightImageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoAdPage(url3);
                        }
                    });
                    Glide.with(context).load(ad.getContent().getAd_imgs().get(3).getImg())
                            //异常时候显示的图片
                            .error(R.drawable.image_error)
                            //加载成功前显示的图片
                            .placeholder(R.drawable.image_error)
                            //url为空的时候,显示的图片
                            .fallback(R.drawable.image_error)
                            .into(bottomRightImageView);
                }
                rootLayout.addView(view4);
            }
        }
        this.addView(rootLayout);
    }


    private void gotoAdPage(String url) {
        Intent intent = new Intent(getContext(), H5PageActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchTextView:
                Intent searchIntent = new Intent(context, GoodsSearchActivity.class);
                searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(searchIntent);
                break;
            case R.id.saoyisaolayout:
                Intent scanIntent = new Intent(context, ScanActivity.class);
                scanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(scanIntent);
                break;
        }
    }
}
