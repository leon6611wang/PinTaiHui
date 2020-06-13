package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.LinkShop;
import com.zhiyu.quanzhu.model.result.LinkShopResult;
import com.zhiyu.quanzhu.ui.activity.PublishChooseGoodsRelationActivity;
import com.zhiyu.quanzhu.ui.adapter.PublishChooseGoodsSearchShopAdapter;
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
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FragmentPublishChooseGoodsSelected extends Fragment {
    private PtrFrameLayout ptrFrameLayout;
    private ListView shopListView;
    private PublishChooseGoodsSearchShopAdapter adapter;
    private View view;
    private int feeds_id;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentPublishChooseGoodsSelected> fragmentPublishChooseGoodsSelectedWeakReference;

        public MyHandler(FragmentPublishChooseGoodsSelected fragment) {
            fragmentPublishChooseGoodsSelectedWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentPublishChooseGoodsSelected fragment = fragmentPublishChooseGoodsSelectedWeakReference.get();
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
        view = inflater.inflate(R.layout.fragment_publish_choose_goods_selected, null);
        feeds_id = (Integer) getArguments().get("feeds_id");
        initPtr();
        initViews();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            page = 1;
            isRefresh = true;
            searchGoodsShop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        isRefresh = true;
        searchGoodsShop();
    }

    private void initViews() {
        shopListView = view.findViewById(R.id.shopListView);
        adapter = new PublishChooseGoodsSearchShopAdapter();
        shopListView.setAdapter(adapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PublishChooseGoodsRelationActivity.class);
                intent.putExtra("shop_id", list.get(position).getId());
                intent.putExtra("shop_icon", list.get(position).getIcon());
                intent.putExtra("shop_name", list.get(position).getName());
                intent.putExtra("feeds_id", feeds_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
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
                searchGoodsShop();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                searchGoodsShop();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }
    private int selectGoodsCount;
    public int getSelectedGoodsCount(){
        selectGoodsCount=0;
        if(null!=list&&list.size()>0){
            for(LinkShop shop:list){
                selectGoodsCount+=shop.getCount();
            }
        }
        return selectGoodsCount;
    }

    private int page = 1;
    private boolean isRefresh = true;
    private LinkShopResult shopResult;
    private List<LinkShop> list;

    private void searchGoodsShop() {
        System.out.println("feeds_id: " + feeds_id);
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SEARCH_GOODS_SHOP);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("已选: " + result);
                shopResult = GsonUtils.GsonToBean(result, LinkShopResult.class);
                if (isRefresh) {
                    list = shopResult.getData().getList();
                } else {
                    list.addAll(shopResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("我搜索过的商店: " + ex.toString());
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
