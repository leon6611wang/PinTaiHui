package com.zhiyu.quanzhu.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.Fans;
import com.zhiyu.quanzhu.model.result.ArticleInfoResult;
import com.zhiyu.quanzhu.model.result.FansResult;
import com.zhiyu.quanzhu.ui.adapter.FansListAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.widget.MyListViewWrapLayout;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestActivity extends BaseActivity implements MyListViewWrapLayout.OnMyListViewWrapLayoutListener {
    private MyHandler myHandler = new MyHandler(this);
    private MyListViewWrapLayout myListViewWrapLayout;
    private FansListAdapter adapter;
    private ListView listView;

    private static class MyHandler extends Handler {
        WeakReference<TestActivity> activityWeakReference;

        public MyHandler(TestActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TestActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();

    }

    private void initViews() {
        myListViewWrapLayout = findViewById(R.id.myListViewWrapLayout);
        myListViewWrapLayout.setOnMyListViewWrapLayoutListener(this);
        listView = myListViewWrapLayout.getListView();
        adapter = new FansListAdapter(this);
        listView.setAdapter(adapter);
        paramsMap.put("page", String.valueOf(page));
        myListViewWrapLayout.setRequestParams(paramsMap, ConstantsUtils.BASE_URL + ConstantsUtils.MY_FANS);
    }

    private boolean isRefresh = true;
    private int page = 1;
    private FansResult fansResult;
    private List<Fans> list;
    private Map<String, String> paramsMap = new HashMap<>();

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        paramsMap.put("page", String.valueOf(page));
        myListViewWrapLayout.setRequestParams(paramsMap, ConstantsUtils.BASE_URL + ConstantsUtils.MY_FANS);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        page++;
        paramsMap.put("page", String.valueOf(page));
        myListViewWrapLayout.setRequestParams(paramsMap, ConstantsUtils.BASE_URL + ConstantsUtils.MY_FANS);
    }

    @Override
    public void onFinish() {
        finish();
    }

    @Override
    public void onResult(String result) {
        fansResult = GsonUtils.GsonToBean(result, FansResult.class);
        if (isRefresh) {
            list = fansResult.getData().getList();
        } else {
            list.addAll(fansResult.getData().getList());
        }
        adapter.setList(list);
    }
}
