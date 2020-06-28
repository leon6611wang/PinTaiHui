package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ConversationCircleGoods;
import com.zhiyu.quanzhu.model.bean.ConversationCircleShop;
import com.zhiyu.quanzhu.model.result.ConversationCircleGoodsResult;
import com.zhiyu.quanzhu.ui.activity.ShopInformationActivity;
import com.zhiyu.quanzhu.ui.adapter.ConversationGroupGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
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

public class ConversationGroupGoodsFragment extends Fragment {
    private View view;
    private int circle_id, shop_id;
    private ConversationCircleShop shop;
    private int cardWidth;
    private RecyclerView mRecyclerView;
    private ConversationGroupGoodsRecyclerAdapter adapter;
    private NiceImageView iconImageView;
    private TextView nameTextView, cityTextView, industryTextView, markTextView,enterShopTextView;

    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ConversationGroupGoodsFragment> fragmentWeakReference;

        public MyHandler(ConversationGroupGoodsFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            ConversationGroupGoodsFragment fragment = fragmentWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.ptrFrameLayout.refreshComplete();
                    fragment.adapter.setList(fragment.list);
                    break;
                case 99:
                    MessageToast.getInstance(fragment.getContext()).show("服务器内部错误，请稍后再试.");
                    fragment.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (null != bundle) {
            cardWidth = bundle.getInt("cardWidth");
            String shop_s = bundle.getString("shop");
            if (!StringUtils.isNullOrEmpty(shop_s)) {
                shop = GsonUtils.GsonToBean(shop_s, ConversationCircleShop.class);
                shop_id = shop.getId();
                circle_id = shop.getQuanzi_id();
            }

        }
        view = inflater.inflate(R.layout.fragment_conversation_group_goods, null);
        initPtr();
        initviews();
        goodsList();
        return view;
    }


    private void initviews() {
        iconImageView = view.findViewById(R.id.iconImageView);
        Glide.with(getContext()).load(shop.getLogo()).error(R.drawable.image_error) .placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(iconImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        nameTextView.setText(shop.getName());
        cityTextView = view.findViewById(R.id.cityTextView);
        if (!StringUtils.isNullOrEmpty(shop.getCity_name())) {
            cityTextView.setVisibility(View.VISIBLE);
            cityTextView.setText(shop.getCity_name());
        } else {
            cityTextView.setVisibility(View.GONE);
        }
        industryTextView = view.findViewById(R.id.industryTextView);
        if (!StringUtils.isNullOrEmpty(shop.getShop_type_name())) {
            industryTextView.setVisibility(View.VISIBLE);
            industryTextView.setText(shop.getShop_type_name());
        } else {
            industryTextView.setVisibility(View.GONE);
        }
        markTextView = view.findViewById(R.id.markTextView);
        markTextView.setText(String.valueOf(shop.getMark()));
        enterShopTextView=view.findViewById(R.id.enterShopTextView);
        enterShopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShopInformationActivity.class);
                intent.putExtra("shop_id",String.valueOf(shop.getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new ConversationGroupGoodsRecyclerAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        GridSpacingItemDecoration decoration=new GridSpacingItemDecoration(2,(int)getContext().getResources().getDimension(R.dimen.dp_5),false);
//        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
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
                goodsList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                goodsList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private int page = 1;
    private boolean isRefresh = true;
    private ConversationCircleGoodsResult conversationCircleGoodsResult;
    private List<ConversationCircleGoods> list;

    private void goodsList() {
        System.out.println("shop_id: "+shop_id+" , circle_id: "+circle_id);
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MESSAGE_CIRCLE_GOODS_LIST);
        params.addBodyParameter("shop_id", String.valueOf(shop.getId()));
        params.addBodyParameter("circle_id", String.valueOf(shop.getQuanzi_id()));
        params.addBodyParameter("page",String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("goodsList: " + result);
                conversationCircleGoodsResult = GsonUtils.GsonToBean(result, ConversationCircleGoodsResult.class);
                if (isRefresh) {
                    list = conversationCircleGoodsResult.getData().getList();
                } else {
                    list.addAll(conversationCircleGoodsResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("goodsList: " + ex.toString());
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
