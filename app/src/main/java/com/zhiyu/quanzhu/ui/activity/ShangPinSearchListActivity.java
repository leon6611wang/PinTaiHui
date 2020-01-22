package com.zhiyu.quanzhu.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.MallAdGoods;
import com.zhiyu.quanzhu.model.result.MallAdGoodsResult;
import com.zhiyu.quanzhu.ui.adapter.HomeQuanShangRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.WaitDialog;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商品搜索列表
 */
public class ShangPinSearchListActivity extends BaseActivity implements View.OnClickListener {
    private HomeQuanShangRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private String keyword = null;
    private LinearLayout backLayout;
    private EditText searchEditText;
    private TextView zongheOrderTextView, xiaoliangOrderTextView, jiageOrderTextView;
    private ArrayList<MallAdGoods> list = new ArrayList<>();
    private LoadingDialog waitDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ShangPinSearchListActivity> activityWeakReference;

        public MyHandler(ShangPinSearchListActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShangPinSearchListActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    if (null != activity.adapter) {
                        activity.adapter.addDatas(activity.list);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpin_searchlist);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        keyword = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(keyword)) {
            searchGoods();
        }

        initPtr();
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        waitDialog=new LoadingDialog(this,R.style.dialog);
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
                page = 1;
                searchGoods();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new HomeQuanShangRecyclerAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftKeyboardUtil.hideSoftKeyboard(ShangPinSearchListActivity.this);
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

        }
    }

    private String sort = "";
    private String sort_type = "desc";
    private boolean isXiaoliangAsc = true, isJiageAsc = true;

    private void changeOrder(int position) {
        adapter.clearDatas();
        zongheOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        xiaoliangOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        jiageOrderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        switch (position) {
            case 1:
                waitDialog.show();
                sort = "zh_score";
                zongheOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                isXiaoliangAsc = true;
                isJiageAsc = true;
                Drawable rightDrawable0 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable0.setBounds(0, 0, rightDrawable0.getMinimumWidth(), rightDrawable0.getMinimumHeight());
                xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable0, null);
                jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable0, null);
                page = 1;
                searchGoods();
                break;
            case 2:
                sort = "sale_num";
                isJiageAsc = true;
                Drawable rightDrawable1 = getResources().getDrawable(R.mipmap.order_down_gray);
                rightDrawable1.setBounds(0, 0, rightDrawable1.getMinimumWidth(), rightDrawable1.getMinimumHeight());
                jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable1, null);
                xiaoliangOrderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                if (!isXiaoliangAsc) {
                    isXiaoliangAsc = true;
                    sort_type = "desc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_up_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    isXiaoliangAsc = false;
                    sort_type = "asc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_down_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    xiaoliangOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
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
                if (!isJiageAsc) {
                    isJiageAsc = true;
                    sort_type = "desc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_up_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                } else {
                    isJiageAsc = false;
                    sort_type = "asc";
                    Drawable rightDrawable = getResources().getDrawable(R.mipmap.order_down_yellow);
                    rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                    jiageOrderTextView.setCompoundDrawables(null, null, rightDrawable, null);
                }
                page = 1;
                searchGoods();
                break;
        }
    }

    private MallAdGoodsResult mallAdGoodsResult;
    private int page = 1;

    private void searchGoods() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.GOODS_SEARCH);
        params.addBodyParameter("keywords", keyword);
        params.addBodyParameter("page",String.valueOf(page));
        params.addBodyParameter("sort",sort);
        params.addBodyParameter("sort_type",sort_type);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
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


}
