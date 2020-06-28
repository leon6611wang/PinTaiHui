package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.OrderShop;
import com.zhiyu.quanzhu.model.result.OrderResult;
import com.zhiyu.quanzhu.ui.adapter.MyOrderAllRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.MyOrderDaiFuKuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
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
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的订单-待付款
 */
public class FragmentOrderDaiFuKuan extends Fragment implements MyOrderDaiFuKuanRecyclerAdapter.OnRefreshDataListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private PtrFrameLayout ptrFrameLayout;
    private MyOrderDaiFuKuanRecyclerAdapter adapter;
    private MyHandler myHandler=new MyHandler(this);
    private LoadingDialog loadingDialog;
    private static class MyHandler extends Handler {
        WeakReference<FragmentOrderDaiFuKuan> fragmentWeakReference;
        public MyHandler(FragmentOrderDaiFuKuan fragment){
            fragmentWeakReference=new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentOrderDaiFuKuan fragment=fragmentWeakReference.get();
            switch (msg.what){
                case 99:
                    fragment.loadingDialog.dismiss();
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    MessageToast.getInstance(fragment.getActivity()).show("服务器内部错误，请稍后再试.");
                    break;
                case 1:
                    fragment.loadingDialog.dismiss();
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_order, container, false);
        initPtr();
        initViews();
        initDialog();
        return view;
    }

    private void initDialog(){
        loadingDialog=new LoadingDialog(getContext(),R.style.dialog);
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new MyOrderDaiFuKuanRecyclerAdapter(getContext());
        adapter.setOnRefreshDataListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private void initPtr() {
        ptrFrameLayout = view.findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(getContext(), ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(getContext()));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(getContext(), ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                myOrder();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                myOrder();
            }
        });
//        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isRequesting) {
            isRequesting = true;
            page = 1;
            isRefresh = true;
            myOrder();
        }
    }
    @Override
    public void onRefreshData() {
        if (!isRequesting) {
            isRequesting = true;
            page = 1;
            isRefresh = true;
            myOrder();
        }
    }
    private boolean isRequesting = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting) {
            isRequesting = true;
            page = 1;
            isRefresh = true;
            myOrder();
        }
    }

    private String searchContent="";
    public void searchOrder(String search){
        this.searchContent=search;
        page=1;
        isRefresh=true;
        myOrder();
    }
    private int page = 1;
    private boolean isRefresh = true;
    private OrderResult orderResult;
    private List<OrderShop> list;
    private void myOrder() {
        if (null != loadingDialog)
        loadingDialog.show();
        RequestParams params= MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.MY_ORDER);
        params.addBodyParameter("page",String.valueOf(page));
        params.addBodyParameter("type","0");
        params.addBodyParameter("keywords",searchContent);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("我的订单-待付款: "+result);
                orderResult= GsonUtils.GsonToBean(result,OrderResult.class);
                if(isRefresh){
                    list=orderResult.getData().getList();
                }else{
                    list.addAll(orderResult.getData().getList());
                }
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message=myHandler.obtainMessage(99);
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
}
