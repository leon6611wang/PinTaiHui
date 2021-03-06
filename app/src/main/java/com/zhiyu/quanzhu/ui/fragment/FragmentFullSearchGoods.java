package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.FullSearchGoods;
import com.zhiyu.quanzhu.model.result.FullSearchGoodsResult;
import com.zhiyu.quanzhu.model.result.GoodsResult;
import com.zhiyu.quanzhu.ui.adapter.FullSearchGoodsGridAdapter;
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
 * 全局搜索-动态
 */
public class FragmentFullSearchGoods extends Fragment {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private GridView mListView;
    private FullSearchGoodsGridAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentFullSearchGoods> fullSearchGoodsWeakReference;

        public MyHandler(FragmentFullSearchGoods fragment) {
            fullSearchGoodsWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentFullSearchGoods fragment = fullSearchGoodsWeakReference.get();
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
        view = inflater.inflate(R.layout.fragment_full_search_goods, null);
        initViews();
        return view;
    }

    private String searchContext;
    private boolean isRequested=false;
    public void setSearchContext(String search){
        this.searchContext=search;
        isRequested=false;
        if (isUserVisible) {
            search();
        }
    }
    private boolean isUserVisible = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isUserVisible = isVisibleToUser;
        if(isVisibleToUser&&!isRequested){
            search();
        }
    }

    private void initViews() {
        initPtr();
        mListView=view.findViewById(R.id.mListView);
        adapter=new FullSearchGoodsGridAdapter(getContext());
        mListView.setAdapter(adapter);

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
               isRefresh=false;
               search();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
               page=1;
               isRefresh=true;
               search();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }


    //0全部 1圈子 2名片 3动态 4商品 5店铺
    private final String TYPE="4";
    private int page=1;
    private boolean isRefresh=true;
    private FullSearchGoodsResult goodsResult;
    private List<FullSearchGoods> list;
    private void search() {
        if(!StringUtils.isNullOrEmpty(searchContext)){
        isRequested=true;
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FULL_SEARCH);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("type", TYPE);
        params.addBodyParameter("keywords", searchContext);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("goods : "+result);
                goodsResult= GsonUtils.GsonToBean(result,FullSearchGoodsResult.class);
                if(isRefresh){
                    list=goodsResult.getData().getGoods_list();
                }else{
                    list.addAll(goodsResult.getData().getGoods_list());
                }
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("full search goods: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });}
    }
}
