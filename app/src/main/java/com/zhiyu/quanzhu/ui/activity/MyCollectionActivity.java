package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.bean.MyCollectionTab;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.ui.adapter.MyCollectionTabBarAdapter;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyCollectionArticle;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyCollectionCard;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyCollectionGoods;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyCollectionShop;
import com.zhiyu.quanzhu.ui.fragment.FragmentMyCollectionVideo;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener, MyCollectionTabBarAdapter.OnTabChangeListener {
    private LinearLayout backLayout, editLayout, allSelectLayout;
    private TextView titleTextView, cancelTextView, deleteTextView, rightTextView;
    private FrameLayout searchCancelLayout;
    private EditText searchEditText;
    private CardView bottomLayout;
    private ImageView searchImageView, allSelectImageView;
    private RecyclerView tabBarListView;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyCollectionTabBarAdapter tabBarAdapter;
    private List<MyCollectionTab> tabList;
    private boolean isSearch, isEdit, isAllSelect;
    private FragmentMyCollectionArticle articleFragment;
    private FragmentMyCollectionVideo videoFragment;
    private FragmentMyCollectionCard cardFragment;
    private FragmentMyCollectionGoods goodsFragment;
    private FragmentMyCollectionShop shopFragment;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyCollectionActivity> activityWeakReference;

        public MyHandler(MyCollectionActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyCollectionActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (200 == activity.baseResult.getCode()) {
                        switch (activity.viewPager.getCurrentItem()) {
                            case 0:
                                activity.articleFragment.cancelCollectSuccess();
                                break;
                            case 1:
                                activity.videoFragment.cancelCollectSuccess();
                                break;
                            case 2:
                                activity.cardFragment.cancelCollectSuccess();
                                break;
                            case 3:
                                activity.goodsFragment.cancelCollectSuccess();
                                break;
                            case 4:
                                activity.shopFragment.cancelCollectSuccess();
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
        setContentView(R.layout.activity_my_collection);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDatas();
        initViews();
    }

    private void initDatas() {
        tabList = new ArrayList<>();
        tabList.add(new MyCollectionTab("文章", true));
        tabList.add(new MyCollectionTab("视频", false));
        tabList.add(new MyCollectionTab("名片", false));
        tabList.add(new MyCollectionTab("商品", false));
        tabList.add(new MyCollectionTab("店铺", false));
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的收藏");
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(MyCollectionActivity.this);
                    System.out.println("--> search: "+search);
                    articleFragment.search(search);
                    videoFragment.search(search);
                    cardFragment.search(search);
                    goodsFragment.search(search);
                    shopFragment.search(search);
                    return true;
                }
                return false;
            }
        });
        searchImageView = findViewById(R.id.searchImageView);
        cancelTextView = findViewById(R.id.cancelTextView);
        searchCancelLayout = findViewById(R.id.searchCancelLayout);
        searchCancelLayout.setOnClickListener(this);
        editLayout = findViewById(R.id.editLayout);
        editLayout.setOnClickListener(this);
        bottomLayout = findViewById(R.id.bottomLayout);
        allSelectLayout = findViewById(R.id.allSelectLayout);
        allSelectLayout.setOnClickListener(this);
        allSelectImageView = findViewById(R.id.allSelectImageView);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
        rightTextView = findViewById(R.id.rightTextView);
        tabBarListView = findViewById(R.id.tabBarListView);
        tabBarAdapter = new MyCollectionTabBarAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(5, (int) getResources().getDimension(R.dimen.dp_20), false);
        tabBarListView.setLayoutManager(layoutManager);
        tabBarListView.addItemDecoration(decoration);
        tabBarListView.setAdapter(tabBarAdapter);
        tabBarAdapter.setOnTabChangeListener(this);
        tabBarAdapter.setList(tabList);
        viewPager = findViewById(R.id.viewPager);
        articleFragment = new FragmentMyCollectionArticle();
        videoFragment = new FragmentMyCollectionVideo();
        cardFragment = new FragmentMyCollectionCard();
        goodsFragment = new FragmentMyCollectionGoods();
        shopFragment = new FragmentMyCollectionShop();
        fragmentList.add(articleFragment);
        fragmentList.add(videoFragment);
        fragmentList.add(cardFragment);
        fragmentList.add(goodsFragment);
        fragmentList.add(shopFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onTabBarChange(position);
                setSelect(-1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.searchCancelLayout:
                if (!isSearch) {
                    isSearch = true;
                    titleTextView.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.VISIBLE);
                    searchImageView.setVisibility(View.GONE);
                    cancelTextView.setVisibility(View.VISIBLE);
                } else {
                    isSearch = false;
                    titleTextView.setVisibility(View.VISIBLE);
                    searchEditText.setVisibility(View.GONE);
                    searchImageView.setVisibility(View.VISIBLE);
                    cancelTextView.setVisibility(View.GONE);
                }
                break;
            case R.id.editLayout:
                if (!isEdit) {
                    isEdit = true;
                    bottomLayout.setVisibility(View.VISIBLE);
                    rightTextView.setText("完成");
                    setSelect(viewPager.getCurrentItem());
                } else {
                    setSelect(-1);
                }
                break;
            case R.id.allSelectLayout:
                if (!isAllSelect) {
                    isAllSelect = true;
                    allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gouwuche_selected));
                } else {
                    isAllSelect = false;
                    allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gouwuche_unselect));
                }
                setAllSelect(viewPager.getCurrentItem(), isAllSelect);
                break;

            case R.id.deleteTextView:
                String ids = "";
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        ids = articleFragment.getCancelCollectIds();
                        break;
                    case 1:
                        ids = videoFragment.getCancelCollectIds();
                        break;
                    case 2:
                        ids = cardFragment.getCancelCollectIds();
                        break;
                    case 3:
                        ids = goodsFragment.getCancelCollectIds();
                        break;
                    case 4:
                        ids = shopFragment.getCancelCollectIds();
                        break;
                }
                if (!StringUtils.isNullOrEmpty(ids)) {
                    cancelCollect(ids);
                } else {
                    MessageToast.getInstance(this).show("请选择需要删除的收藏");
                }

                break;
        }
    }


    private void onTabBarChange(int position) {
        setSelect(-1);
        for (MyCollectionTab tab : tabList) {
            tab.setSelected(false);
        }
        tabList.get(position).setSelected(true);
        tabBarAdapter.setList(tabList);
    }

    @Override
    public void onTabChange(int position) {
        viewPager.setCurrentItem(position);
    }

    private void setSelect(int position) {
        articleFragment.setSelect(false);
        videoFragment.setSelect(false);
        cardFragment.setSelect(false);
        goodsFragment.setSelect(false);
        shopFragment.setSelect(false);
        switch (position) {
            case -1:
                isEdit = false;
                rightTextView.setText("编辑");
                bottomLayout.setVisibility(View.GONE);
                isAllSelect = false;
                allSelectImageView.setImageDrawable(getResources().getDrawable(R.mipmap.gouwuche_unselect));
                setAllSelect(-1, false);
                break;
            case 0:
                articleFragment.setSelect(true);
                break;
            case 1:
                videoFragment.setSelect(true);
                break;
            case 2:
                cardFragment.setSelect(true);
                break;
            case 3:
                goodsFragment.setSelect(true);
                break;
            case 4:
                shopFragment.setSelect(true);
                break;
        }
    }

    private void setAllSelect(int position, boolean isAllSelect) {
        articleFragment.setAllSelect(false);
        videoFragment.setAllSelect(false);
        cardFragment.setAllSelect(false);
        goodsFragment.setAllSelect(false);
        shopFragment.setAllSelect(false);
        switch (position) {
            case -1:

                break;
            case 0:
                articleFragment.setAllSelect(isAllSelect);
                break;
            case 1:
                videoFragment.setAllSelect(isAllSelect);
                break;
            case 2:
                cardFragment.setAllSelect(isAllSelect);
                break;
            case 3:
                goodsFragment.setAllSelect(isAllSelect);
                break;
            case 4:
                shopFragment.setAllSelect(isAllSelect);
                break;
        }
    }

    /**
     * 批量取消收藏
     *
     * @param cids
     */
    private BaseResult baseResult;

    private void cancelCollect(String cids) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CANCEL_COLLECT);
        params.addBodyParameter("cids", cids);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
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
}
