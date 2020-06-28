package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.result.ShopProfileResult;
import com.zhiyu.quanzhu.ui.adapter.ShopProfileRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 聊天-店铺资料
 */
public class ShopProfileActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLaout;
    private RecyclerView mRecyclerView;
    private ShopProfileRecyclerAdapter adapter;
    private View headerView;
    private int totalDy;
    private LinearLayout headerLayout;
    private CircleImageView shopIconImageView;
    private TextView shopNameTextView, daysTextView, followTextView, goodsCountTextView,
            followCountTextView, saleNumTextView, goodAppraiseTextView,
            goodsMarkTextView, serviceMarkTextView, deliveryMarkTextView,
            enterShopTextView, customerServiceTextView;
    private LinearLayout followLayout;
    private ImageView followImageView;
    private ProgressBar goodsMarkProgressBar, serviceMarkProgressBar, deliveryMarkProgressBar;
    private int shop_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShopProfileActivity> activityWeakReference;

        public MyHandler(ShopProfileActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShopProfileActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Glide.with(activity).load(activity.shopProfileResult.getData().getShop_icon()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(activity.shopIconImageView);
                    activity.shopNameTextView.setText(activity.shopProfileResult.getData().getShop_name());
                    activity.daysTextView.setText("入驻：" + activity.shopProfileResult.getData().getDays() + "天");
                    if (activity.shopProfileResult.getData().isIs_follow()) {
                        activity.followLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_solid_bg_ededed_gray));
                        activity.followImageView.setVisibility(View.GONE);
                        activity.followTextView.setText("已关注");
                        activity.followTextView.setTextColor(activity.getResources().getColor(R.color.text_color_grey));
                    } else {
                        activity.followLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_bg_yellow));
                        activity.followImageView.setVisibility(View.VISIBLE);
                        activity.followTextView.setText("关注");
                        activity.followTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }
                    activity.goodsCountTextView.setText("商品数量 " + activity.shopProfileResult.getData().getGoods_num());
                    activity.followCountTextView.setText("圈粉数 " + activity.shopProfileResult.getData().getFollow_num());
                    activity.saleNumTextView.setText("销量 " + activity.shopProfileResult.getData().getSale_num());
                    activity.goodAppraiseTextView.setText((int)activity.shopProfileResult.getData().getMark() + "%");
                    activity.goodsMarkTextView.setText(String.valueOf(activity.shopProfileResult.getData().getGoods_mark()));
                    activity.goodsMarkProgressBar.setProgress((int) activity.shopProfileResult.getData().getGoods_mark() * 10);
                    activity.serviceMarkProgressBar.setProgress((int) activity.shopProfileResult.getData().getService_mark() * 10);
                    activity.serviceMarkTextView.setText(String.valueOf(activity.shopProfileResult.getData().getService_mark()));
                    activity.deliveryMarkProgressBar.setProgress((int) activity.shopProfileResult.getData().getKd_mark() * 10);
                    activity.deliveryMarkTextView.setText(String.valueOf(activity.shopProfileResult.getData().getKd_mark()));
                    activity.adapter.clearDatas();
                    activity.adapter.addDatas(activity.shopProfileResult.getData().getLicense());
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        activity.shopProfile();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        shop_id = getIntent().getIntExtra("shop_id", 0);
        initViews();
        shopProfile();
    }

    private void initViews() {
        headerLayout = findViewById(R.id.headerLayout);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_shop_profile_recyclerview, null);
        shopIconImageView = headerView.findViewById(R.id.shopIconImageView);
        shopNameTextView = headerView.findViewById(R.id.shopNameTextView);
        daysTextView = headerView.findViewById(R.id.daysTextView);
        followLayout = headerView.findViewById(R.id.followLayout);
        followLayout.setOnClickListener(this);
        followImageView = headerView.findViewById(R.id.followImageView);
        followTextView = headerView.findViewById(R.id.followTextView);
        goodsCountTextView = headerView.findViewById(R.id.goodsCountTextView);
        followCountTextView = headerView.findViewById(R.id.followCountTextView);
        saleNumTextView = headerView.findViewById(R.id.saleNumTextView);
        goodAppraiseTextView = headerView.findViewById(R.id.goodAppraiseTextView);
        goodsMarkProgressBar = headerView.findViewById(R.id.goodsMarkProgressBar);
        goodsMarkTextView = headerView.findViewById(R.id.goodsMarkTextView);
        serviceMarkProgressBar = headerView.findViewById(R.id.serviceMarkProgressBar);
        serviceMarkTextView = headerView.findViewById(R.id.serviceMarkTextView);
        deliveryMarkProgressBar = headerView.findViewById(R.id.deliveryMarkProgressBar);
        deliveryMarkTextView = headerView.findViewById(R.id.deliveryMarkTextView);
        backLaout = findViewById(R.id.backLayout);
        backLaout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ShopProfileRecyclerAdapter(this);
        adapter.setHeaderView(headerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
        enterShopTextView = findViewById(R.id.enterShopTextView);
        enterShopTextView.setOnClickListener(this);
        customerServiceTextView = findViewById(R.id.customerServiceTextView);
        customerServiceTextView.setOnClickListener(this);

    }

    private void headerLayoutChange() {
        if (Math.abs(totalDy) > 0) {
            float alpha = (float) Math.abs(totalDy) / (float) 100;
            if (alpha > 1.0f) {
                alpha = 1.0f;
            }
            headerLayout.getBackground().mutate().setAlpha((int) (alpha * 255));
        } else if (totalDy == 0) {
            headerLayout.getBackground().mutate().setAlpha(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.enterShopTextView:
                Intent shopInfoIntent = new Intent(this, ShopInformationActivity.class);
                shopInfoIntent.putExtra("shop_id", String.valueOf(shop_id));
                startActivity(shopInfoIntent);
                break;
            case R.id.customerServiceTextView:
                Intent customerServiceIntent = new Intent(this, CustomerServiceActivity.class);
                customerServiceIntent.putExtra("shop_id", shop_id);
                startActivity(customerServiceIntent);
                break;
            case R.id.followLayout:
                follow();
                break;
        }
    }

    private ShopProfileResult shopProfileResult;

    private void shopProfile() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_PROFILE);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("shopProfile: " + result);
                shopProfileResult = GsonUtils.GsonToBean(result, ShopProfileResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("shopProfile: " + ex.toString());
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

    private void follow() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(shop_id));
        params.addBodyParameter("module_type", "store");
        params.addBodyParameter("type", shopProfileResult.getData().isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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
}
