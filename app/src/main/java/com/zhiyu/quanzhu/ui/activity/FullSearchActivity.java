package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.leon.myvideoplaerlibrary.manager.VideoPlayerManager;
import com.leon.myvideoplaerlibrary.manager.VideoWindowManager;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.FullSearchTab;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.ui.adapter.FullSearchHistoryListAdapter;
import com.zhiyu.quanzhu.ui.adapter.FullSearchTabListAdapter;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchAll;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchArticle;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchCard;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchCircle;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchFeed;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchGoods;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchShop;
import com.zhiyu.quanzhu.ui.fragment.FragmentFullSearchVideo;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView2;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyBoardListener;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局搜索
 */
public class FullSearchActivity extends BaseActivity implements View.OnClickListener, FullSearchTabListAdapter.OnSelectTagListener, FullSearchHistoryListAdapter.OnDeleteFullSearchHistory, FragmentFullSearchAll.OnSelectPageListener {
    private LinearLayout backLayout, contentLayout, historyLayout;
    private ListView searchHistoryListView;
    private FullSearchHistoryListAdapter fullSearchHistoryListAdapter;
    private TextView clearHistoryTextView;
    private HorizontalListView2 tabTitleListView;
    private EditText searchEditText;
    private FullSearchTabListAdapter tabListAdapter;
    private List<FullSearchTab> tabList;
    private List<Fragment> fragmentList;
    private ViewPagerAdapter viewPagerAdapter;
    private NoScrollViewPager mViewPager;
    private SoftKeyBoardListener softKeyBoardListener;
    private FragmentFullSearchAll fragmentFullSearchAll;
    private FragmentFullSearchCircle fragmentFullSearchCircle;
    private FragmentFullSearchCard fragmentFullSearchCard;
    private FragmentFullSearchArticle fragmentFullSearchArticle;
    private FragmentFullSearchFeed fragmentFullSearchFeed;
    private FragmentFullSearchVideo fragmentFullSearchVideo;
    private FragmentFullSearchGoods fragmentFullSearchGoods;
    private FragmentFullSearchShop fragmentFullSearchShop;
    private List<FullSearchHistory> historyList;
    private int screenWidth, tabWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_search);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initDatas();
        initViews();
        setSoftKeyBoardListener();
    }

    private void initDatas() {
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        tabWidth = Math.round(screenWidth / 6);
        tabList = new ArrayList<>();
        tabList.add(new FullSearchTab("全部", true));
        tabList.add(new FullSearchTab("圈子", false));
        tabList.add(new FullSearchTab("名片", false));
        tabList.add(new FullSearchTab("文章", false));
        tabList.add(new FullSearchTab("动态", false));
        tabList.add(new FullSearchTab("视频", false));
        tabList.add(new FullSearchTab("商品", false));
        tabList.add(new FullSearchTab("店铺", false));
        fragmentList = new ArrayList<>();
        fragmentFullSearchAll = new FragmentFullSearchAll();
        fragmentFullSearchAll.setOnSelectPageListener(this);
        fragmentFullSearchCircle = new FragmentFullSearchCircle();
        fragmentFullSearchCard = new FragmentFullSearchCard();
        fragmentFullSearchArticle = new FragmentFullSearchArticle();
        fragmentFullSearchFeed = new FragmentFullSearchFeed();
        fragmentFullSearchVideo = new FragmentFullSearchVideo();
        fragmentFullSearchGoods = new FragmentFullSearchGoods();
        fragmentFullSearchShop = new FragmentFullSearchShop();
        fragmentList.add(fragmentFullSearchAll);
        fragmentList.add(fragmentFullSearchCircle);
        fragmentList.add(fragmentFullSearchCard);
        fragmentList.add(fragmentFullSearchArticle);
        fragmentList.add(fragmentFullSearchFeed);
        fragmentList.add(fragmentFullSearchVideo);
        fragmentList.add(fragmentFullSearchGoods);
        fragmentList.add(fragmentFullSearchShop);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
    }


    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        contentLayout = findViewById(R.id.contentLayout);
        searchHistoryListView = findViewById(R.id.searchHistoryListView);
        historyLayout = findViewById(R.id.historyLayout);
        clearHistoryTextView = findViewById(R.id.clearHistoryTextView);
        clearHistoryTextView.setOnClickListener(this);
        fullSearchHistoryListAdapter = new FullSearchHistoryListAdapter();
        fullSearchHistoryListAdapter.setOnDeleteFullSearchHistory(this);
        searchHistoryListView.setAdapter(fullSearchHistoryListAdapter);
        searchHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search = historyList.get(position).getHistory();
                searchEditText.setText(search);
                SoftKeyboardUtil.hideSoftKeyboard(FullSearchActivity.this);
                if (!StringUtils.isNullOrEmpty(search)) {
                    fragmentFullSearchAll.setSearchContext(search);
                    fragmentFullSearchCircle.setSearchContext(search);
                    fragmentFullSearchCard.setSearchContext(search);
                    fragmentFullSearchArticle.setSearchContext(search);
                    fragmentFullSearchFeed.setSearchContext(search);
                    fragmentFullSearchVideo.setSearchContext(search);
                    fragmentFullSearchGoods.setSearchContext(search);
                    fragmentFullSearchShop.setSearchContext(search);
                    FullSearchHistory history = new FullSearchHistory();
                    history.setHistory(search);
                    history.setTime(new Date().getTime());
                    FullSearchHistoryDao.getDao().saveFullSearchHistory(history);
                }
            }
        });
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(FullSearchActivity.this);
                    if (!StringUtils.isNullOrEmpty(search)) {
                        fragmentFullSearchAll.setSearchContext(search);
                        fragmentFullSearchCircle.setSearchContext(search);
                        fragmentFullSearchCard.setSearchContext(search);
                        fragmentFullSearchArticle.setSearchContext(search);
                        fragmentFullSearchFeed.setSearchContext(search);
                        fragmentFullSearchVideo.setSearchContext(search);
                        fragmentFullSearchGoods.setSearchContext(search);
                        fragmentFullSearchShop.setSearchContext(search);
                        FullSearchHistory history = new FullSearchHistory();
                        history.setHistory(search);
                        history.setTime(new Date().getTime());
                        history.setUser_id(SPUtils.getInstance().getUserId(BaseApplication.applicationContext));
                        FullSearchHistoryDao.getDao().saveFullSearchHistory(history);
                    }

                    return true;
                }
                return false;
            }
        });
        tabTitleListView = findViewById(R.id.tabTitleListView);
        tabListAdapter = new FullSearchTabListAdapter(this);
        tabListAdapter.setOnSelectTagListener(this);
        tabTitleListView.setAdapter(tabListAdapter);
        tabListAdapter.setList(tabList);
        mViewPager = findViewById(R.id.mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (FullSearchTab tab : tabList) {
                    tab.setSelected(false);
                }
                tabList.get(position).setSelected(true);
                tabListAdapter.setList(tabList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onSelectTag(int position) {
        tabTitleListView.setSelection(position);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.clearHistoryTextView:
                FullSearchHistoryDao.getDao().clearFullSearchHistory();
                fullSearchHistoryListAdapter.setList(null);
                break;
        }
    }


    /**
     * 添加软键盘的监听
     */
    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(this);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                contentLayout.setVisibility(View.GONE);
                historyLayout.setVisibility(View.VISIBLE);
                historyList = FullSearchHistoryDao.getDao().fullSearchHistoryList();
                fullSearchHistoryListAdapter.setList(historyList);
            }

            @Override
            public void keyBoardHide(int height) {
                contentLayout.setVisibility(View.VISIBLE);
                historyLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefreshFullSearchHistory() {
        historyList = FullSearchHistoryDao.getDao().fullSearchHistoryList();
        fullSearchHistoryListAdapter.setList(historyList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.getInstance().onPause();
    }

    @Override
    public void onBackPressed() {
        //尝试弹射返回
        if (VideoPlayerManager.getInstance().isBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().onDestroy();
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy();
    }

    @Override
    public void onSelectPage(int position) {
        tabTitleListView.setSelection(position);
        mViewPager.setCurrentItem(position);
    }
}
