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
import com.zhiyu.quanzhu.model.bean.Card;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.ui.adapter.MyCollectionArticleAdapter;
import com.zhiyu.quanzhu.ui.adapter.MyCollectionCardRecyclerAdapter;
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
 * 我的收藏-文章
 */
public class FragmentMyCollectionCard extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private MyCollectionCardRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentMyCollectionCard> fragmentWeakReference;

        public MyHandler(FragmentMyCollectionCard fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentMyCollectionCard fragment = fragmentWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.cardList);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_collection_card, null);
        initPtr();
        initViews();
        return view;
    }

    public void setSelect(boolean select){
        adapter.setSelectModel(select);
    }
    public void setAllSelect(boolean isAllSelect){
        adapter.setAllSelect(isAllSelect);
    }
    public String getCancelCollectIds(){
        return adapter.getCancelCollectIds();
    }

    public void cancelCollectSuccess(){
        adapter.cancelCollectSuccess();
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
                isRefresh = false;
                page++;
                myCollection();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                myCollection();
            }
        });
        ptrFrameLayout.autoRefresh();
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MyCollectionCardRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void search(String keyWord) {
        this.keywords = keyWord;
        page = 1;
        isRefresh = true;
        myCollection();
    }

    private String type = "cards";//类型 article文章 cards名片 video视频 goods商品 store店铺
    private int page = 1;
    private boolean isRefresh = true;
    private CardResult cardResult;
    private List<Card> cardList;
    private String keywords="";
    private void myCollection() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_COLLECTION_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("type", type);
        params.addBodyParameter("keywords",keywords);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                if (isRefresh) {
                    cardList = cardResult.getData().getList();
                } else {
                    cardList.addAll(cardResult.getData().getList());
                }
                System.out.println("card: "+(null==cardList?0:cardList.size()));
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("card: "+ex.toString());
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
