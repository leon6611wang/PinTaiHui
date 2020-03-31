package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.MallAdGoods;
import com.zhiyu.quanzhu.model.result.MallAdGoodsResult;
import com.zhiyu.quanzhu.model.result.ShopResult;
import com.zhiyu.quanzhu.ui.adapter.HomeQuanShangRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.GoodsCouponsDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
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

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class ShopInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private EditText searchEditText;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private HomeQuanShangRecyclerAdapter adapter;
    private View headerView;
    private MyHandler myHandler = new MyHandler(this);
    private FrameLayout headerBgLayout;
    private LinearLayout ttLayout, tbLayout;
    private float hw_ratio = 0.5627f;
    private float totalDy;
    private int ttheight, headerHeight;
    private LinearLayout.LayoutParams ll;
    private String keyword;
    private String shop_id;
    private ArrayList<MallAdGoods> list = new ArrayList<>();
    private TextView zongheOrderTextView, xiaoliangOrderTextView, jiageOrderTextView, newOrderTextView, zongheOrderTextView2, xiaoliangOrderTextView2, jiageOrderTextView2, newOrderTextView2;
    private CircleImageView shopIconImageView;
    private TextView shopNameTextView, followTextView, followBtnTextView;
    private LinearLayout starLayout, followBtnLayout;
    private ImageView followBtnImageView;
    private LinearLayout gouwucheLayout, shareLayout;
    private GoodsCouponsDialog youHuiQuanDialog;
    private TextView getCouponTextView;
    private static class MyHandler extends Handler {
        WeakReference<ShopInformationActivity> activityWeakReference;

        public MyHandler(ShopInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShopInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    if (null != activity.adapter) {
                        activity.adapter.addDatas(activity.list);
                    }
                    break;
                case 2:
                    activity.initStar(activity.shopResult.getData().getMark());
                    Glide.with(activity).load(activity.shopResult.getData().getShop_icon())
                            //异常时候显示的图片
                            .error(R.mipmap.img_h)
                            //加载成功前显示的图片
                            .placeholder(R.mipmap.img_h)
                            //url为空的时候,显示的图片
                            .fallback(R.mipmap.img_h)
                            .into(activity.shopIconImageView);
                    activity.shopNameTextView.setText(activity.shopResult.getData().getShop_name());
                    activity.followTextView.setText(activity.shopResult.getData().getFollow_num() + "关注");
                    if (activity.shopResult.getData().isIs_follow()) {
                        activity.followBtnLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_solid_bg_white));
                        activity.followBtnImageView.setVisibility(View.GONE);
                        activity.followBtnTextView.setText("已关注");
                        activity.followBtnTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    } else {
                        activity.followBtnLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_bg_white));
                        activity.followBtnImageView.setVisibility(View.VISIBLE);
                        activity.followBtnTextView.setText("关注");
                        activity.followBtnTextView.setTextColor(activity.getResources().getColor(R.color.white));
                    }
                    break;
                case 3:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.baseResult.getCode() == 200) {
                        activity.followBtnLayout.setBackground(activity.getResources().getDrawable(R.drawable.shape_oval_solid_bg_white));
                        activity.followBtnImageView.setVisibility(View.GONE);
                        activity.followBtnTextView.setText("已关注");
                        activity.followBtnTextView.setTextColor(activity.getResources().getColor(R.color.text_color_yellow));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_information);
        shop_id = getIntent().getStringExtra("shop_id");
        shop_id = "1";
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        int screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        headerHeight = Math.round(screenWidth * hw_ratio);
        ll = new LinearLayout.LayoutParams(screenWidth, headerHeight);
        initPtr();
        initViews();
        initDialogs();
        shopInfo();
        searchGoods();
        shopGoodsType();

    }

    private void initDialogs(){
        youHuiQuanDialog=new GoodsCouponsDialog(this,R.style.dialog);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        gouwucheLayout = findViewById(R.id.gouwucheLayout);
        gouwucheLayout.setOnClickListener(this);
        shareLayout = findViewById(R.id.shareLayout);
        shareLayout.setOnClickListener(this);
        ttLayout = findViewById(R.id.ttLayout);
        ttLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                ttheight = ttLayout.getMeasuredHeight();
                return true;
            }
        });
        tbLayout = findViewById(R.id.tbLayout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new HomeQuanShangRecyclerAdapter(this);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_shop_information, null);
        headerBgLayout = headerView.findViewById(R.id.headerBgLayout);
        headerBgLayout.setLayoutParams(ll);
        shopIconImageView = headerView.findViewById(R.id.shopIconImageView);
        shopNameTextView = headerView.findViewById(R.id.shopNameTextView);
        followTextView = headerView.findViewById(R.id.followTextView);
        followBtnTextView = headerView.findViewById(R.id.followBtnTextView);
        starLayout = headerView.findViewById(R.id.starLayout);
        followBtnLayout = headerView.findViewById(R.id.followBtnLayout);
        followBtnLayout.setOnClickListener(this);
        followBtnImageView = headerView.findViewById(R.id.followBtnImageView);
        getCouponTextView=headerView.findViewById(R.id.getCouponTextView);
        getCouponTextView.setOnClickListener(this);
        adapter.setHeaderView(headerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftKeyboardUtil.hideSoftKeyboard(ShopInformationActivity.this);
                    keyword = searchEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyword)) {
                        searchGoods();
                    }
                    return true;
                }

                return false;
            }
        });

        zongheOrderTextView = findViewById(R.id.zongheOrderTextView);
        zongheOrderTextView.setOnClickListener(this);
        xiaoliangOrderTextView = findViewById(R.id.xiaoliangOrderTextView);
        xiaoliangOrderTextView.setOnClickListener(this);
        jiageOrderTextView = findViewById(R.id.jiageOrderTextView);
        jiageOrderTextView.setOnClickListener(this);
        newOrderTextView = findViewById(R.id.newOrderTextView);
        newOrderTextView.setOnClickListener(this);
        zongheOrderTextView2 = headerView.findViewById(R.id.zongheOrderTextView2);
        zongheOrderTextView2.setOnClickListener(this);
        xiaoliangOrderTextView2 = headerView.findViewById(R.id.xiaoliangOrderTextView2);
        xiaoliangOrderTextView2.setOnClickListener(this);
        jiageOrderTextView2 = headerView.findViewById(R.id.jiageOrderTextView2);
        jiageOrderTextView2.setOnClickListener(this);
        newOrderTextView2 = headerView.findViewById(R.id.newOrderTextView2);
        newOrderTextView2.setOnClickListener(this);
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

    private void initStar(int starCount) {
        starLayout.removeAllViews();
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int dp = (int) getResources().getDimension(R.dimen.dp_1);
        ll.leftMargin = dp;
        ll.topMargin = dp;
        ll.rightMargin = dp;
        ll.bottomMargin = dp;
        for (int i = 0; i < starCount; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.star_white));
            imageView.setLayoutParams(ll);
            starLayout.addView(imageView);
        }
    }

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                searchGoods();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.zongheOrderTextView:
                changeOrder(1);
                break;
            case R.id.xiaoliangOrderTextView:
                changeOrder(2);
                break;
            case R.id.jiageOrderTextView:
                changeOrder(3);
                break;
            case R.id.zongheOrderTextView2:
                changeOrder(1);
                break;
            case R.id.xiaoliangOrderTextView2:
                changeOrder(2);
                break;
            case R.id.jiageOrderTextView2:
                changeOrder(3);
                break;
            case R.id.newOrderTextView:
                changeOrder(4);
                break;
            case R.id.newOrderTextView2:
                changeOrder(4);
                break;
            case R.id.followBtnLayout:
                followShop();
                break;
            case R.id.gouwucheLayout:
                Intent gouwucheIntent = new Intent(ShopInformationActivity.this, CartActivity.class);
                startActivity(gouwucheIntent);
                break;
            case R.id.sharelayout:

                break;
            case R.id.getCouponTextView:
                youHuiQuanDialog.show();
                break;
        }
    }

    private String sort = "";
    private String sort_type = "desc";
    private boolean isXiaoliangAsc = true, isJiageAsc = true;

    private void changeOrder(int position) {
        resetTopLayout();
        adapter.clearDatas();
        zongheOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        xiaoliangOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        jiageOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        zongheOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_black));
        xiaoliangOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_black));
        jiageOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_black));
        newOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        newOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_black));
        switch (position) {
            case 1:
                sort = "zh_score";
                zongheOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zongheOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_yellow));
                isXiaoliangAsc = true;
                isJiageAsc = true;
                Drawable rightDrawable0 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable0.setBounds(0, 0, rightDrawable0.getMinimumWidth(), rightDrawable0.getMinimumHeight());
                xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable0, null);
                jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable0, null);
                xiaoliangOrderTextView2.setCompoundDrawables(null, null, rightDrawable0, null);
                jiageOrderTextView2.setCompoundDrawables(null, null, rightDrawable0, null);
                page = 1;
                searchGoods();
                break;
            case 2:
                sort = "sale_num";
                isJiageAsc = true;
                Drawable rightDrawable1 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable1.setBounds(0, 0, rightDrawable1.getMinimumWidth(), rightDrawable1.getMinimumHeight());
                jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable1, null);
                jiageOrderTextView2.setCompoundDrawables(null, null, rightDrawable1, null);
                xiaoliangOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                xiaoliangOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_yellow));
                if (!isXiaoliangAsc) {
                    isXiaoliangAsc = true;
                    sort_type = "desc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_up_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                    xiaoliangOrderTextView2.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    isXiaoliangAsc = false;
                    sort_type = "asc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_down_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                    xiaoliangOrderTextView2.setCompoundDrawables(null, null, rightDrawable, null);
                }
                page = 1;
                searchGoods();
                break;
            case 3:
                sort = "goods_price";
                isXiaoliangAsc = true;
                Drawable rightDrawable2 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable2.setBounds(0, 0, rightDrawable2.getMinimumWidth(), rightDrawable2.getMinimumHeight());
                xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable2, null);
                jiageOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                xiaoliangOrderTextView2.setCompoundDrawables(null, null, rightDrawable2, null);
                jiageOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_yellow));
                if (!isJiageAsc) {
                    isJiageAsc = true;
                    sort_type = "desc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_up_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                    jiageOrderTextView2.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    isJiageAsc = false;
                    sort_type = "asc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_down_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                    jiageOrderTextView2.setCompoundDrawables(null, null, rightDrawable, null);
                }
                page = 1;
                searchGoods();
                break;
            case 4:
                sort = "new_score";
                newOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                newOrderTextView2.setTextColor(getResources().getColor(R.color.text_color_yellow));
                isXiaoliangAsc = true;
                isJiageAsc = true;
                Drawable rightDrawable3 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable3.setBounds(0, 0, rightDrawable3.getMinimumWidth(), rightDrawable3.getMinimumHeight());
                xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable3, null);
                jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable3, null);
                xiaoliangOrderTextView2.setCompoundDrawables(null, null, rightDrawable3, null);
                jiageOrderTextView2.setCompoundDrawables(null, null, rightDrawable3, null);
                page = 1;
                searchGoods();
                break;
        }
    }

    private void headerLayoutChange() {
        if (Math.abs(totalDy) > 0) {
            ttLayout.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            ttLayout.setAlpha(alpha);
        } else {
            ttLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            ttLayout.setAlpha(1);
        }
        if (Math.abs(totalDy) >= (headerHeight - ttheight)) {
            tbLayout.setVisibility(View.VISIBLE);
        } else {
            tbLayout.setVisibility(View.GONE);
        }
    }

    private void resetTopLayout() {
        totalDy = 0f;
        ttLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ttLayout.setAlpha(1);
        tbLayout.setVisibility(View.GONE);
    }

    private int page = 1;
    private MallAdGoodsResult mallAdGoodsResult;

    private void searchGoods() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_SEARCH);
        params.addBodyParameter("keywords", keyword);
        params.addBodyParameter("page", String.valueOf(page));
//        params.addBodyParameter("sort",sort);
//        params.addBodyParameter("sort_type",sort_type);
        params.addBodyParameter("shop_id", shop_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                result = " {\"code\":200,\"msg\":\"ok\",\"data\":{\"goods_list\":[{\"id\":1,\"goods_name\":\"\\u54c8\\u54c8\",\"img\":\"http:\\/\\/q1ecexot0.bkt.clouddn.com\\/89f7fd7ac19548f80029469fc67efab2.jpg\",\"goods_price\":\"1\",\"goods_stock\":\"1\",\"sale_num\":\"0\"},{\"id\":2,\"goods_name\":\"\\u563f\\u563f\",\"img\":\"http:\\/\\/q1ecexot0.bkt.clouddn.com\\/89f7fd7ac19548f80029469fc67efab2.jpg\",\"goods_price\":\"1\",\"goods_stock\":\"1\",\"sale_num\":\"0\"},{\"id\":3,\"goods_name\":\"\\u54c8\\u55bd\",\"img\":\"http:\\/\\/q1ecexot0.bkt.clouddn.com\\/89f7fd7ac19548f80029469fc67efab2.jpg\",\"goods_price\":\"1\",\"goods_stock\":\"1\",\"sale_num\":\"0\"},{\"id\":26,\"goods_name\":\"\\u6d4b\\u8bd5\\u5546\\u54c1\",\"img\":\"http:\\/\\/q1ecexot0.bkt.clouddn.com\\/89f7fd7ac19548f80029469fc67efab2.jpg\",\"goods_price\":\"0\",\"goods_stock\":\"0\",\"sale_num\":\"0\"}]}}\n";
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

    private ShopResult shopResult;

    private void shopInfo() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHOP_INFO);
        params.addBodyParameter("shop_id", shop_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shopResult = GsonUtils.GsonToBean(result, ShopResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("shopinfo: " + shopResult.getData());
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


    private BaseResult baseResult;

    private void followShop() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", shop_id);
        params.addBodyParameter("module_type", "store");
        params.addBodyParameter("type", (null == shopResult || null == shopResult.getData() || !shopResult.getData().isIs_follow()) ? "0" : "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                shopInfo();
                System.out.println("follow shop: " + result);
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

    private void shopGoodsType(){
        RequestParams params=new RequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.SHOP_GOODS_TYPE);
        params.addBodyParameter("shop_id",shop_id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("shopGoodsType: "+result);
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
