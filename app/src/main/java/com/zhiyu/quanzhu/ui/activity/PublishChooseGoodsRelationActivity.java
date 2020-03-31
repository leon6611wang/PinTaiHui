package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FullSearchGoods;
import com.zhiyu.quanzhu.model.result.FullSearchGoodsResult;
import com.zhiyu.quanzhu.ui.adapter.PublishChooseGoodsRelationAdapter;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 发布-选择商品-关联商品
 */
public class PublishChooseGoodsRelationActivity extends BaseActivity implements View.OnClickListener {
    private GridView goodsGridView;
    private PublishChooseGoodsRelationAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private LinearLayout backLayout, rightLayout;
    private EditText searchEditText;
    private NiceImageView shopIconImageView;
    private TextView shopNameTextView;
    private int shop_id;
    private int feeds_id;
    private String shop_icon, shop_name;
    private String keywords;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<PublishChooseGoodsRelationActivity> activityWeakReference;

        public MyHandler(PublishChooseGoodsRelationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PublishChooseGoodsRelationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (200 == activity.baseResult.getCode()) {
                        activity.finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_choose_goods_relation);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        shop_id = getIntent().getIntExtra("shop_id", 0);
        feeds_id = getIntent().getIntExtra("feeds_id", 0);
        shop_icon = getIntent().getStringExtra("shop_icon");
        shop_name = getIntent().getStringExtra("shop_name");
        initPtr();
        initViews();
        searchGoods();
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
                isRefresh = false;
                searchGoods();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                searchGoods();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        goodsGridView = findViewById(R.id.goodsGridView);
        adapter = new PublishChooseGoodsRelationAdapter(this);
        goodsGridView.setAdapter(adapter);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(PublishChooseGoodsRelationActivity.this);
                    keywords = search;
                    page = 1;
                    searchGoods();
                    return true;
                }
                return false;
            }
        });
        shopIconImageView = findViewById(R.id.shopIconImageView);
        shopNameTextView = findViewById(R.id.shopNameTextView);
        Glide.with(this).load(shop_icon).into(shopIconImageView);
        shopNameTextView.setText(shop_name);
    }

    private String goods_ids = "";
    private Set<Integer> idSet = new HashSet<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:

                finish();
                break;
            case R.id.rightLayout:
                List<FullSearchGoods> goodsList = adapter.getList();
                List<Integer> goodsIdList = new ArrayList<>();
                if (null != goodsList && goodsList.size() > 0) {
                    for (FullSearchGoods goods : goodsList) {
                        if (goods.isIs_relation()) {
                            goodsIdList.add(goods.getId());
                            idSet.add(goods.getId());
                        }

                    }
                }
                if (goodsIdList.size() > 0) {
                    for (int i = 0; i < goodsIdList.size(); i++) {
                        goods_ids += goodsIdList.get(i);
                        if (i < goodsIdList.size() - 1) {
                            goods_ids += ",";
                        }
                    }
                }

                relationGoods();
                System.out.println(goods_ids);
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private FullSearchGoodsResult goodsResult;
    private List<FullSearchGoods> list;

    private void searchGoods() {
        System.out.println("shop_id: " + shop_id + " , feeds_id: " + feeds_id);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_SHOP_GOODS);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                goodsResult = GsonUtils.GsonToBean(result, FullSearchGoodsResult.class);
                if (isRefresh) {
                    list = goodsResult.getData().getGoods();
                } else {
                    list.addAll(goodsResult.getData().getGoods());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("搜索店铺商品: " + result);
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

    private void relationGoods() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.RELATION_GOODS);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("goods_ids", goods_ids);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("relation goods: " + result);
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

    @Override
    public void finish() {
        if (null != onRelationGoodsListener) {
            onRelationGoodsListener.onRelationGoods(idSet);
        }
        super.finish();
    }

    public static void setOnRelationGoodsListener(OnRelationGoodsListener listener) {
        onRelationGoodsListener = listener;
    }

    private static OnRelationGoodsListener onRelationGoodsListener;

    public interface OnRelationGoodsListener {
        void onRelationGoods(Set<Integer> idSet);
    }
}
