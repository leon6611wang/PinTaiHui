package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.CartShop;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.CartAvailableFragment;
import com.zhiyu.quanzhu.ui.fragment.CartInvalidFragment;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
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

import io.rong.imlib.MessageTag;
import io.vov.vitamio.utils.Log;

/**
 * 购物车
 */
public class CartActivity extends BaseActivity implements View.OnClickListener, CartAvailableFragment.OnAvailableGoodsSelectListener {
    private LinearLayout backLayout, rightLayout, availableLayout, invalidLayout;
    private TextView invalidTextView, availabelTextView, rightTextView;
    private View availableLineView, invalidLineView;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private LinearLayout quanxuanLayout, buyLayout, manageLayout;
    private TextView zhengshuTextView, xiaoshuTextView, jiesuanTextView, deleteTextView;
    private ImageView quanxuanImageView;
    private CartAvailableFragment availableFragment;
    private CartInvalidFragment invalidFragment;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CartActivity> activityWeakReference;

        public MyHandler(CartActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CartActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        switch (activity.mViewPager.getCurrentItem()) {
                            case 0:
                                activity.availableFragment.refreshCart();
                                break;
                            case 1:
                                activity.invalidFragment.refreshCart();
                                break;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        availableLayout = findViewById(R.id.availableLayout);
        availableLayout.setOnClickListener(this);
        invalidLayout = findViewById(R.id.invalidLayout);
        invalidLayout.setOnClickListener(this);
        invalidTextView = findViewById(R.id.invalidTextView);
        availabelTextView = findViewById(R.id.availabelTextView);
        availableLineView = findViewById(R.id.availableLineView);
        invalidLineView = findViewById(R.id.invalidLineView);
        quanxuanLayout = findViewById(R.id.quanxuanLayout);
        quanxuanLayout.setOnClickListener(this);
        manageLayout = findViewById(R.id.manageLayout);
        buyLayout = findViewById(R.id.buyLayout);
        quanxuanImageView = findViewById(R.id.quanxuanImageView);
        zhengshuTextView = findViewById(R.id.zhengshuTextView);
        xiaoshuTextView = findViewById(R.id.xiaoshuTextView);
        jiesuanTextView = findViewById(R.id.jiesuanTextView);
        jiesuanTextView.setOnClickListener(this);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);

        mViewPager = findViewById(R.id.mViewPager);
        availableFragment = new CartAvailableFragment();
        availableFragment.setOnAvailableGoodsSelectListener(this);
        invalidFragment = new CartInvalidFragment();
        fragmentList.add(availableFragment);
        fragmentList.add(invalidFragment);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                barChange(position);
                showBottomOperationLayout(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private boolean isManage;
    private boolean isAllSelected = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                isManage = !isManage;
                rightTextView.setText(isManage ? "完成" : "管理");
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        showBottomOperationLayout(isManage ? 1 : 0);
                        availableFragment.setManage(isManage);
                        break;
                    case 1:
                        showBottomOperationLayout(1);
                        break;
                }
                if(!isManage){
                    availableFragment.setUnSelected();
                }
                break;
            case R.id.availableLayout:
                barChange(0);
                showBottomOperationLayout(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.invalidLayout:
                barChange(1);
                showBottomOperationLayout(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.quanxuanLayout:
                isAllSelected = !isAllSelected;
                if (isAllSelected) {
                    quanxuanImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gouwuche_selected));
                } else {
                    quanxuanImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gouwuche_unselect));
                }
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        availableFragment.allSelect(isAllSelected);
                        break;
                    case 1:
                        invalidFragment.allSelect(isAllSelected);
                        break;
                }
                calculateCartPrice();
                break;
            case R.id.jiesuanTextView:
                settlement();
                break;
            case R.id.deleteTextView:
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        List<CartShop> shopList0 = availableFragment.getList();
                        if (null != shopList0 && shopList0.size() > 0) {
                            String ids = "";
                            for (int i = 0; i < shopList0.size(); i++) {
                                for (int j = 0; j < shopList0.get(i).getList().size(); j++) {
                                    if (shopList0.get(i).getList().get(j).isSelected())
                                        ids += shopList0.get(i).getList().get(j).getId() + ",";
                                }
                            }
                            if (!StringUtils.isNullOrEmpty(ids)) {
                                deleteCart(ids);
                            } else {
                                MessageToast.getInstance(this).show("请选择待删除的商品");
                            }
                        }
                        break;
                    case 1:
                        List<CartShop> shopList1 = invalidFragment.getList();
                        if (null != shopList1 && shopList1.size() > 0) {
                            String ids = "";
                            for (int i = 0; i < shopList1.size(); i++) {
                                for (int j = 0; j < shopList1.get(i).getList().size(); j++) {
                                    if (shopList1.get(i).getList().get(j).isSelected())
                                        ids += shopList1.get(i).getList().get(j).getId() + ",";
                                }
                            }
                            if (!StringUtils.isNullOrEmpty(ids)) {
                                deleteCart(ids);
                            } else {
                                MessageToast.getInstance(this).show("请选择待删除的商品");
                            }
                        }
                        break;
                }
                break;
        }
    }


    @Override
    public void onAvailableGoodsSelect() {
        calculateCartPrice();
    }

    private void calculateCartPrice() {
        List<Long> idsList = new ArrayList<>();
        String ids;
        long totalPrice = 0;
        List<CartShop> shopList = availableFragment.getList();
        for (CartShop shop : shopList) {
            for (CartGoods goods : shop.getList()) {
                if (goods.isSelected()) {
                    idsList.add(goods.getId());
                    System.out.println("price: " + goods.getPrice() + " , num: " + goods.getCurrentNum());
                    long price = goods.getPrice() * goods.getCurrentNum();
                    totalPrice += price;
                }
            }
        }
        zhengshuTextView.setText(PriceParseUtils.getInstance().getZhengShu(totalPrice));
        xiaoshuTextView.setText(PriceParseUtils.getInstance().getXiaoShu(totalPrice));
        System.out.println("idsList: " + idsList.toString());
    }

    private void barChange(int position) {
        availabelTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        availableLineView.setBackgroundColor(getResources().getColor(R.color.white));
        invalidTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        invalidLineView.setBackgroundColor(getResources().getColor(R.color.white));
        switch (position) {
            case 0:
                availabelTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                availableLineView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
                break;
            case 1:
                invalidTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                invalidLineView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
                break;
        }
    }

    private void showBottomOperationLayout(int index) {
        switch (index) {
            case 0:
                buyLayout.setVisibility(View.VISIBLE);
                manageLayout.setVisibility(View.GONE);
                break;
            case 1:
                buyLayout.setVisibility(View.GONE);
                manageLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private BaseResult baseResult;

    //删除购物车商品
    private void deleteCart(String ids) {
        System.out.println("删除购物车商品 ids: " + ids);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELETE_CART);
        params.addBodyParameter("ids", ids);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("delete cart: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("delete cart: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 结算
     */
    private void settlement() {
        List<Long> idsList = new ArrayList<>();
        String ids;
        List<CartShop> shopList = availableFragment.getList();
        for (CartShop shop : shopList) {
            for (CartGoods goods : shop.getList()) {
                if (goods.isSelected()) {
                    idsList.add(goods.getId());
                }
            }
        }
        System.out.println("idsList: " + idsList.toString());
        ids = GsonUtils.GsonString(idsList);
        if (idsList.size() > 0) {
            Intent jiesuanIntent = new Intent(this, OrderConfirmActivity.class);
            jiesuanIntent.putExtra("ids", ids);
            startActivity(jiesuanIntent);
        } else {
            MessageToast.getInstance(this).show("请选择待结算商品");
        }
    }


}
