package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.LinkShop;
import com.zhiyu.quanzhu.model.result.LinkShopResult;
import com.zhiyu.quanzhu.ui.activity.PublishChooseGoodsRelationActivity;
import com.zhiyu.quanzhu.ui.adapter.PublishChooseGoodsSearchMyShopAdapter;
import com.zhiyu.quanzhu.ui.adapter.PublishChooseGoodsSearchShopAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class FragmentPublishChooseGoodsSearch extends Fragment {
    private View view;
    private ListView myShopListView, shopListView;
    private PublishChooseGoodsSearchMyShopAdapter myShopAdapter;
    private PublishChooseGoodsSearchShopAdapter shopAdapter;
    private PtrFrameLayout ptrFrameLayout;
    private EditText searchEditText;
    private int feeds_id;
    private int selectGoodsCount;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentPublishChooseGoodsSearch> searchWeakReference;

        public MyHandler(FragmentPublishChooseGoodsSearch fragment) {
            searchWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentPublishChooseGoodsSearch fragment = searchWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (fragment.isSearch) {
                        fragment.ptrFrameLayout.refreshComplete();
                        fragment.shopAdapter.setList(fragment.shopList);
                    } else {
                        fragment.myShopAdapter.setList(fragment.myShopList);
                    }
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_publish_choose_goods_search, null);
        feeds_id = (Integer) getArguments().get("feeds_id");
        initPtr();
        initViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shopList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            shopList();
        }
    }

    private void initViews() {
        myShopListView = view.findViewById(R.id.myShopListView);
        shopListView = view.findViewById(R.id.shopListView);
        myShopAdapter = new PublishChooseGoodsSearchMyShopAdapter();
        shopAdapter = new PublishChooseGoodsSearchShopAdapter();
        myShopListView.setAdapter(myShopAdapter);
        shopListView.setAdapter(shopAdapter);
        myShopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PublishChooseGoodsRelationActivity.class);
                intent.putExtra("shop_id", myShopList.get(position).getId());
                intent.putExtra("shop_icon", myShopList.get(position).getIcon());
                intent.putExtra("shop_name", myShopList.get(position).getName());
                intent.putExtra("feeds_id", feeds_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PublishChooseGoodsRelationActivity.class);
                intent.putExtra("shop_id", shopList.get(position).getId());
                intent.putExtra("shop_icon", shopList.get(position).getIcon());
                intent.putExtra("shop_name", shopList.get(position).getName());
                intent.putExtra("feeds_id", feeds_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = searchEditText.getText().toString().trim();
                    SoftKeyboardUtil.hideSoftKeyboard(getActivity());
                    if (!StringUtils.isNullOrEmpty(search)) {
                        keywords = search;
                        page = 1;
                        isSearch = true;
                        shopList();
                    }
                    return true;
                }
                return false;
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
                shopList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    public int getSelectedGoodsCount() {
        selectGoodsCount=0;
        System.out.println("shoplist " + (null == shopList ? 0 : shopList.size()));
        System.out.println("myShopList " + (null == myShopList ? 0 : myShopList.size()));
        if (null != shopList && shopList.size() > 0) {
            for (LinkShop shop : shopList) {
                selectGoodsCount += shop.getCount();
            }
        }
        if (null != myShopList && myShopList.size() > 0) {
            for (LinkShop shop : myShopList) {
                selectGoodsCount += shop.getCount();
            }
        }
        return selectGoodsCount;
    }

    private int page = 1;
    private String keywords;
    private LinkShopResult shopResult;
    private List<LinkShop> shopList = new ArrayList<>(), myShopList;
    private boolean isSearch = false;

    private void shopList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_SHOP_LIST);
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("feeds_id", String.valueOf(feeds_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("搜索: " + result);
                shopResult = GsonUtils.GsonToBean(result, LinkShopResult.class);
                if (isSearch) {
                    if (page == 1) {
                        shopList = shopResult.getData().getList();
                    } else {
                        shopList.addAll(shopResult.getData().getList());
                    }
                } else {
                    myShopList = shopResult.getData().getList();
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
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
