package com.zhiyu.quanzhu.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.GoodsImg;
import com.zhiyu.quanzhu.ui.adapter.GoodsInfoBannerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GoodsInfoBanner extends FrameLayout {
    private View view;
    private Context context;
    private int screenWidth;
    private FrameLayout.LayoutParams layoutParams;
    private MaxRecyclerView mRecyclerView;
    private GoodsInfoBannerAdapter adapter;
    private List<GoodsImg> list = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);
    private TextView indicatorTextView;
    private int currentPosition = 0;

    private static class MyHandler extends Handler {
        WeakReference<GoodsInfoBanner> bannerWeakReference;

        public MyHandler(GoodsInfoBanner banner) {
            bannerWeakReference = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            GoodsInfoBanner banner = bannerWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    public GoodsInfoBanner(Context ctxt, @Nullable AttributeSet attrs) {
        super(ctxt, attrs);
        this.context = ctxt;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        layoutParams = new FrameLayout.LayoutParams(screenWidth, screenWidth);
        view = LayoutInflater.from(context).inflate(R.layout.widget_goods_info_banner, null);
        view.setLayoutParams(layoutParams);
        this.addView(view);
        initViews();
    }


    public void setList(List<GoodsImg> urlList) {
        this.list = urlList;
        adapter.setList(list);
        indicatorTextView.setText((currentPosition + 1) + "/" + list.size());
    }

    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        indicatorTextView = view.findViewById(R.id.indicatorTextView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
        adapter = new GoodsInfoBannerAdapter(context);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {//停止滑动
                    currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                    indicatorTextView.setText((currentPosition + 1) + "/" + list.size());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


}
