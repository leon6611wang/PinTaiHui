package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.CircleInfoUser;
import com.zhiyu.quanzhu.model.bean.FullSearchHistory;
import com.zhiyu.quanzhu.model.dao.FullSearchHistoryDao;
import com.zhiyu.quanzhu.model.result.CircleInfoUserResult;
import com.zhiyu.quanzhu.ui.adapter.CircleMemeberManageListAdapter;
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
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈子-成员管理
 */
public class CircleMemberManageActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout topLayout,backLayout,rightLayout;
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    private CircleMemeberManageListAdapter adapter;
    private int own = -1;//-1 不在群里 0圈主 1管理员 2成员
    private long circle_id;
    private String keyword;
    private EditText searchEditText;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CircleMemberManageActivity> circleMemberManageActivityWeakReference;

        public MyHandler(CircleMemberManageActivity activity) {
            circleMemberManageActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleMemberManageActivity activity = circleMemberManageActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_member_manage);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        own = getIntent().getIntExtra("own", -1);
        circle_id = 16;
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
                circleUserList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                circleUserList();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        backLayout=findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout=findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        topLayout = findViewById(R.id.topLayout);
        topLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        topLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        int height = topLayout.getHeight(); // 获取高度
                        adapter.setTopLayoutHeight(height, own);
                        System.out.println("topLayout height: " + height);
                        return true;
                    }
                });
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(CircleMemberManageActivity.this);
                    if (!StringUtils.isNullOrEmpty(search)) {
                        keyword=searchEditText.getText().toString().trim();
                        page=1;
                        isRefresh=true;
                        circleUserList();
                    }
                    return true;
                }
                return false;
            }
        });
        mListView = findViewById(R.id.mListView);
        adapter = new CircleMemeberManageListAdapter(mListView, this, (int) circle_id);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent addFrendIntent=new Intent(CircleMemberManageActivity.this,CircleMemberManageAddFrendActivity.class);
                startActivity(addFrendIntent);
                break;
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private CircleInfoUserResult circleInfoUserResult;
    private List<CircleInfoUser> list;

    private void circleUserList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_USER_LIST);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                circleInfoUserResult = GsonUtils.GsonToBean(result, CircleInfoUserResult.class);
                if (isRefresh) {
                    list = circleInfoUserResult.getData().getList();
                } else {
                    list.addAll(circleInfoUserResult.getData().getList());
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
