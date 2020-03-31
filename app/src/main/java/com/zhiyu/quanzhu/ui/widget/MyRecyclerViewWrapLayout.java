package com.zhiyu.quanzhu.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class MyRecyclerViewWrapLayout extends FrameLayout implements View.OnClickListener {
    private View listView;
    private View noNetView;
    private View noDataView;
    private View loadingView;
    private View errorView;
    private PtrFrameLayout ptrLayout;
    private RecyclerView mRecyclerView;
    private Context context;
    private TextView refreshTextView, backTextView;
    private LoadingDialog loadingDialog;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean hasData;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyRecyclerViewWrapLayout> layoutWeakReference;

        public MyHandler(MyRecyclerViewWrapLayout layout) {
            layoutWeakReference = new WeakReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            MyRecyclerViewWrapLayout layout = layoutWeakReference.get();
            switch (msg.what) {
                case 10001:
                    layout.ptrLayout.refreshComplete();
                    if (layout.baseResult.getCode() == 200) {
                        layout.hasData = true;
                        layout.showListView();
                    } else {
                        layout.showNoData();
                    }
                    if (null != layout.onMyListViewWrapLayoutListener) {
                        layout.onMyListViewWrapLayoutListener.onResult((String) msg.obj);
                    }
                    break;
                case 10002:
                    layout.ptrLayout.refreshComplete();
                    layout.showErrorView();
                    break;
            }
        }
    }

    public MyRecyclerViewWrapLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initNetStatusListener();
        initDialog();
        initViews();

    }

    /**
     * 监听网络状态
     */
    private void initNetStatusListener() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        context.registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void initDialog() {
        loadingDialog = new LoadingDialog(context, R.style.dialog);
    }

    private void initViews() {
        listView = LayoutInflater.from(context).inflate(R.layout.widget_wrap_layout_recyclerview, null);
        initPtr();
        mRecyclerView = listView.findViewById(R.id.mRecyclerView);
        this.addView(listView);
        listView.setVisibility(GONE);
        noNetView = LayoutInflater.from(context).inflate(R.layout.widget_wrap_layout_no_net, null);
        refreshTextView = noNetView.findViewById(R.id.refreshTextView);
        this.addView(noNetView);
        noNetView.setVisibility(GONE);
        noDataView = LayoutInflater.from(context).inflate(R.layout.widget_wrap_layout_no_data, null);
        backTextView = noDataView.findViewById(R.id.backTextView);
        this.addView(noDataView);
        noDataView.setVisibility(GONE);
        loadingView = LayoutInflater.from(context).inflate(R.layout.widget_wrap_layout_loading, null);
        this.addView(loadingView);
        loadingView.setVisibility(VISIBLE);
        errorView = LayoutInflater.from(context).inflate(R.layout.widget_wrap_layout_error, null);
        this.addView(errorView);
        errorView.setVisibility(GONE);
    }

    private void initPtr() {
        ptrLayout = listView.findViewById(R.id.ptrLayout);
        ptrLayout.setHeaderView(new MyPtrRefresherHeader(context));
        ptrLayout.addPtrUIHandler(new MyPtrHandlerHeader(context, ptrLayout));
        ptrLayout.setFooterView(new MyPtrRefresherFooter(context));
        ptrLayout.addPtrUIHandler(new MyPtrHandlerFooter(context, ptrLayout));
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (null != onMyListViewWrapLayoutListener) {
                    onMyListViewWrapLayoutListener.onLoadMore();
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (null != onMyListViewWrapLayoutListener) {
                    onMyListViewWrapLayoutListener.onRefresh();
                }
            }
        });
        ptrLayout.autoRefresh();
        ptrLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshTextView:
                if (null != onMyListViewWrapLayoutListener) {
                    onMyListViewWrapLayoutListener.onRefresh();
                }
                break;
            case R.id.backTextView:
                if (null != onMyListViewWrapLayoutListener) {
                    onMyListViewWrapLayoutListener.onFinish();
                }
                break;
        }
    }

    private OnMyListViewWrapLayoutListener onMyListViewWrapLayoutListener;

    public void setOnMyListViewWrapLayoutListener(OnMyListViewWrapLayoutListener listener) {
        this.onMyListViewWrapLayoutListener = listener;
    }

    public interface OnMyListViewWrapLayoutListener {
        void onRefresh();

        void onLoadMore();

        void onFinish();

        void onResult(String result);
    }

    private void showNoNet() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        listView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        noNetView.setVisibility(VISIBLE);
    }

    private void showLoading() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        listView.setVisibility(GONE);
        noNetView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        if (!StringUtils.isNullOrEmpty(url)) {
            requestNetData();
        }
    }

    private void showNoData() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        listView.setVisibility(GONE);
        noNetView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        noDataView.setVisibility(VISIBLE);
    }

    private void showListView() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        noNetView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
    }

    private void showErrorView() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        noNetView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        listView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private Map<String, String> map;
    private String url;

    public void setRequestParams(Map<String, String> params, String requestUrl) {
        this.map = params;
        this.url = requestUrl;
        if (hasData) {
            requestNetData();
        }
    }

    private BaseResult baseResult;

    private void requestNetData() {
        RequestParams params = MyRequestParams.getInstance(context).getRequestParams(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                params.addBodyParameter(mapKey, mapValue);
            }
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(10001);
                message.obj = result;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(10002);
                message.sendToTarget();
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
     * 监听网络状态
     */
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                if (!hasData) {
                    showLoading();
                } else {
                    showListView();
                }
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
//                        Log.i("myNetWork", "正在使用2G/3G/4G网络");
                        break;
                    case TYPE_WIFI:
//                        Log.i("myNetWork", "正在使用wifi上网");
                        break;
                    default:
                        break;
                }
            } else {

                if (!hasData) {
                    showNoNet();
                } else {
                    showListView();
                }

//                Log.i("myNetWork", "当前无网络连接");
            }
        }
    }
}
