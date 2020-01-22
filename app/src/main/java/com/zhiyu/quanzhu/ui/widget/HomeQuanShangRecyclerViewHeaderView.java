package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MallAd;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.List;

public class HomeQuanShangRecyclerViewHeaderView extends LinearLayout {
    private LinearLayout rootLayout, toplayout;
    private Context context;
    private LinearLayout.LayoutParams params1, params2, params3, params4, paramsBanner;
    private float ratio1, ratio2, ratio3, ratio4, ratioBanner;
    private int height1, height2, height3, height4, heightBanner;
    private int screenWidth;
    private int dp_15, dp_10, dp_5;
    private CardView bannerCardView;
    private List<MallAd> list;

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
        for (MallAd ad : list) {
            if (ad.getType().equals("1")) {
                View view1 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_1, null);
                view1.setLayoutParams(params1);
                ImageView adImageView = view1.findViewById(R.id.adImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(0).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(adImageView);
                toplayout.addView(view1);
            }
            if (ad.getType().equals("2")) {
                View view2 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_2, null);
                view2.setLayoutParams(params2);
                ImageView leftImageView = view2.findViewById(R.id.leftImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(0).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(leftImageView);
                ImageView rightImageViwe = view2.findViewById(R.id.rightImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(1).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(rightImageViwe);
                rootLayout.addView(view2);
            }
            if (ad.getType().equals("3")) {
                View view3 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_3, null);
                view3.setLayoutParams(params3);
                ImageView leftImageView = view3.findViewById(R.id.leftImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(0).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(leftImageView);
                ImageView rightTopImageView = view3.findViewById(R.id.rightTopImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(1).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(rightTopImageView);
                ImageView rightBottomImageView = view3.findViewById(R.id.rightBottomImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(2).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(rightBottomImageView);
                rootLayout.addView(view3);
            }
            if (ad.getType().equals("4")) {
                View view4 = LayoutInflater.from(context).inflate(R.layout.header_home_quanshang_recyclerview_4, null);
                view4.setLayoutParams(params4);
                ImageView topLeftImageView = view4.findViewById(R.id.topLeftImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(0).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(topLeftImageView);
                ImageView topRightImageView = view4.findViewById(R.id.topRightImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(1).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(topRightImageView);
                ImageView bottomLeftImageView = view4.findViewById(R.id.bottomLeftImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(2).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(bottomLeftImageView);
                ImageView bottomRightImageView = view4.findViewById(R.id.bottomRightImageView);
                Glide.with(context).load(ad.getContent().getAd_imgs().get(3).getImg())
                        //异常时候显示的图片
                        .error(R.mipmap.img_h)
                        //加载成功前显示的图片
                        .placeholder(R.mipmap.img_h)
                        //url为空的时候,显示的图片
                        .fallback(R.mipmap.img_h)
                        .into(bottomRightImageView);
                rootLayout.addView(view4);
            }
        }
        this.addView(rootLayout);
    }

}
