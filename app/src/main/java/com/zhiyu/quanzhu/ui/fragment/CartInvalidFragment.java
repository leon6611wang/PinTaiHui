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
import com.zhiyu.quanzhu.model.bean.CartGoods;
import com.zhiyu.quanzhu.model.bean.CartShop;
import com.zhiyu.quanzhu.model.result.CartResult;
import com.zhiyu.quanzhu.ui.adapter.CartInvalidShopRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.FailureToast;
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
import java.util.ListIterator;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 购物车-无效的
 */
public class CartInvalidFragment extends Fragment {
    private View view;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView mRecyclerView;
    private CartInvalidShopRecyclerAdapter adapter;
    private YNDialog qingkongDialog, deleteDialog;
    //    private ImageView quanxuanImageView;
//    private TextView deleteQuanXuanTextView, zhengshuTextView, xiaoshuTextView, jiesuanTextView;
//    private LinearLayout quanxuanLayout;
    private List<CartShop> list;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CartInvalidFragment> fragmentWeakReference;

        public MyHandler(CartInvalidFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CartInvalidFragment fragment = fragmentWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setData(fragment.list);
                    break;
                case 2:
                    fragment.ptrFrameLayout.refreshComplete();
                    FailureToast.getInstance(fragment.getContext()).show();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cart_invalid, null);

        initDialogs();
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cartList();
    }

    private void initViews() {
        initPtr();
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new CartInvalidShopRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusableInTouchMode(false);
        mRecyclerView.requestFocus();

    }


    public void allSelect(boolean isAllSelected) {
        if (!isAllSelected) {
            if (null != list && list.size() > 0)
                for (CartShop gouWuCheItem : list) {
                    gouWuCheItem.setSelected(false);
                    for (CartGoods gouWuCheItemItem : gouWuCheItem.getList()) {
                        gouWuCheItemItem.setSelected(false, false);
                    }
                }
        } else {
            if (null != list && list.size() > 0)
                for (CartShop gouWuCheItem : list) {
                    gouWuCheItem.setSelected(true);
                    for (CartGoods gouWuCheItemItem : gouWuCheItem.getList()) {
                        gouWuCheItemItem.setSelected(true, false);
                    }
                }
        }
        if (null != list && list.size() > 0)
            adapter.setData(list);
    }

    public List<CartShop> getList() {
        return adapter.getList();
    }

    public void refreshCart() {
        isRefresh = true;
        page = 1;
        cartList();
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
                cartList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                cartList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void initDialogs() {
        qingkongDialog = new YNDialog(getActivity(), R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {

            }
        });
        deleteDialog = new YNDialog(getActivity(), R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                List<CartShop> list = adapter.getList();
                ListIterator<CartShop> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    CartShop gouWuCheItem = iterator.next();
                    if (gouWuCheItem.isSelected()) {
                        iterator.remove();
                    } else {
                        ListIterator<CartGoods> itemIterator = gouWuCheItem.getList().listIterator();
                        while (itemIterator.hasNext()) {
                            CartGoods gouWuCheItemItem = itemIterator.next();
                            if (gouWuCheItemItem.isSelected()) {
                                itemIterator.remove();
                            }
                        }
                    }
                }
                adapter.setData(list);
            }
        });
    }

    private int page = 1;
    private int type = 2;//1.有效；2.失效
    private boolean isRefresh = true;
    private CartResult cartResult;

    private void cartList() {
        RequestParams params = MyRequestParams.getInstance(getActivity()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CART_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("type", String.valueOf(type));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("购物车-失效: " + result);
                cartResult = GsonUtils.GsonToBean(result, CartResult.class);
                if (null != cartResult.getData()) {
                    if (isRefresh) {
                        list = cartResult.getData();
                    } else {
                        list.addAll(cartResult.getData());
                    }
                } else if (null == cartResult.getData()) {
                    if (null != list)
                        list.clear();
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(2);
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
