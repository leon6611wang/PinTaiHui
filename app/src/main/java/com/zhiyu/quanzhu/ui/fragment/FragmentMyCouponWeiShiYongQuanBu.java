package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.MyCouponShop;
import com.zhiyu.quanzhu.model.result.MyCouponResult;
import com.zhiyu.quanzhu.ui.adapter.MyCouponShopRecyclerAdapter;
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
 * 我的卡券-未使用-全部
 */
public class FragmentMyCouponWeiShiYongQuanBu extends Fragment {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private RecyclerView mRecyclerView;
    private MyCouponShopRecyclerAdapter adapter;

    private static class MyHandler extends Handler {
        WeakReference<FragmentMyCouponWeiShiYongQuanBu> fragmentKaQuanWeiShiYongQuanBuWeakReference;

        public MyHandler(FragmentMyCouponWeiShiYongQuanBu fragmentMyCouponWeiShiYongQuanBu) {
            fragmentKaQuanWeiShiYongQuanBuWeakReference = new WeakReference<>(fragmentMyCouponWeiShiYongQuanBu);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentMyCouponWeiShiYongQuanBu fragment = fragmentKaQuanWeiShiYongQuanBuWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kaquan_weishiyong_quanbu, null);
        initViews();
        return view;
    }

    private void initViews() {
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
                myCoupons();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                myCoupons();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new MyCouponShopRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }


    private int page = 1;
    private boolean isRefresh = true;
    private int type = 1;// 1全部未使用的 2未使用且即将过期 3已过期 4已使用
    private MyCouponResult couponResult;
    private List<MyCouponShop> list;
    private void myCoupons() {
        System.out.println("isRefresh: " + isRefresh);
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_COUPON_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("type", String.valueOf(type));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                couponResult = GsonUtils.GsonToBean(result, MyCouponResult.class);
                if(isRefresh){
                    list=couponResult.getData().getList();
                }else{
                    list.addAll(couponResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("全部: " + ex.toString());
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
