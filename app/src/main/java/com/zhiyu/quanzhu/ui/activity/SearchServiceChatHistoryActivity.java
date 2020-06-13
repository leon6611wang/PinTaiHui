package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.CustomerServiceMessage;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.model.result.CustomerServiceResult;
import com.zhiyu.quanzhu.ui.adapter.SearchServiceChatHistoryAdapter;
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
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class SearchServiceChatHistoryActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEditText;
    private LinearLayout cancelLayout;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private SearchServiceChatHistoryAdapter adapter;
    private int shop_id;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<SearchServiceChatHistoryActivity> activityWeakReference;

        public MyHandler(SearchServiceChatHistoryActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchServiceChatHistoryActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list, activity.keyword);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_service_chat_history);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        shop_id = getIntent().getIntExtra("shop_id", 0);

        initPtr();
        initViews();

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
                historyMessageList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                historyMessageList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        mEditText = findViewById(R.id.mEditText);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = mEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(SearchServiceChatHistoryActivity.this);
                    page = 1;
                    isRefresh = true;
                    historyMessageList();
                    return true;
                }
                return false;
            }
        });
        cancelLayout = findViewById(R.id.cancelLayout);
        cancelLayout.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new SearchServiceChatHistoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelLayout:
                finish();
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private CustomerServiceResult customerServiceResult;
    private String keyword = "";
    private List<CustomerServiceMessage> list = new ArrayList<>();

    private void historyMessageList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.CUSTOMER_SERVICE_MESSAGE_LIST);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("keyword", keyword);
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("历史消息: " + result);
                customerServiceResult = GsonUtils.GsonToBean(result, CustomerServiceResult.class);
                if (isRefresh) {
                    list = customerServiceResult.getData().getData().getList();
                } else {
                    list.addAll(customerServiceResult.getData().getData().getList());
                }


                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("历史消息: " + ex.toString());
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
