package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.RefreshFooterWrapper;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianDaoHang;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianDaoHangResult;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianQuanZiResult;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianResult;
import com.zhiyu.quanzhu.ui.adapter.CircleTuiJianAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianTitleRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.QuanZiTuiJianLabelDialog;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.CenterLayoutManager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;
import com.zhiyu.quanzhu.utils.VideoCacheUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子-关注
 */
public class FragmentQuanZiTuiJian extends Fragment implements QuanZiTuiJianTitleRecyclerAdapter.OnTitleClickListener, View.OnClickListener, OnRefreshLoadMoreListener,
        QuanZiTuiJianAdapter.OnChangeXingQuListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private RefreshLayout refreshLayout;
    private QuanZiTuiJianAdapter adapter;
    private CircleTuiJianAdapter circleTuiJianAdapter;
    private MyRecyclerView titleRecyclerView;
    private QuanZiTuiJianTitleRecyclerAdapter titleRecyclerAdapter;
    private List<QuanZiTuiJianDaoHang> daoHangList;
    private CenterLayoutManager centerLayoutManager;
    private ImageView addLabelImageView;
    private QuanZiTuiJianLabelDialog quanZiTuiJianLabelDialog;
    private List<QuanZiTuiJian> tuiJianList = new ArrayList<>();
    private QuanZiTuiJianResult quanziTuijianResult;
    private int contentHeight;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentQuanZiTuiJian> fragmentQuanZiTuiJianWeakReference;

        public MyHandler(FragmentQuanZiTuiJian fragment) {
            fragmentQuanZiTuiJianWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentQuanZiTuiJian fragment = fragmentQuanZiTuiJianWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.refreshLayout.finishRefresh();
//                    fragment.adapter.setData(fragment.tuiJianList);
                    fragment.circleTuiJianAdapter.setList(fragment.tuiJianList);
                    break;
                case 2:
                    fragment.refreshLayout.finishLoadMore();
                    fragment.circleTuiJianAdapter.setList(fragment.tuiJianList);
                    break;
                case 3:
                    fragment.refreshLayout.finishRefresh();
                    fragment.refreshLayout.finishLoadMore();
                    break;
                case 4://感兴趣的圈子推荐
                    int position = (Integer) msg.obj;
                    fragment.tuiJianList.get(position).setQuanzi(fragment.quanZiTuiJianQuanZiResult.getData().getCirclelist());
                    fragment.circleTuiJianAdapter.notifyItemChanged(position);
                    break;
                case 5:
                    Bundle b = (Bundle) msg.obj;
                    int po = b.getInt("position");
                    String pa = b.getString("path");
                    fragment.tuiJianList.get(po).getContent().setVideo_url(pa);
                    fragment.circleTuiJianAdapter.notifyItemChanged(po);
//                    System.out.println("position: " + po + " , path: " + pa);
                    break;
                case 6:
                    fragment.daoHangList.get(0).setChoose(true);
                    fragment.titleRecyclerAdapter.setData(fragment.daoHangList);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quanzi_tuijian, container, false);
        contentHeight = getArguments().getInt("contentHeight");
        int dp_60 = (int) getContext().getResources().getDimension(R.dimen.dp_60);
        card_height = contentHeight - dp_60;
        float width = (9f / 16f) * card_height;
        card_width = Math.round(width);
        try {
            cityName=SharedPreferencesUtils.getInstance(getContext()).getLocationCity();
        }catch (Exception e){
            e.printStackTrace();
        }
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                requestDaoHangList();
                tuijianFeedList();
            }
        });

        initRefreshLayout();
        initViews();
        initDialogs();
        return view;
    }

    private void initDialogs() {
        quanZiTuiJianLabelDialog = new QuanZiTuiJianLabelDialog(getActivity(), R.style.dialog, new QuanZiTuiJianLabelDialog.OnDaoHangCallbackListener() {
            @Override
            public void onDaoHangCallback(List<QuanZiTuiJianDaoHang> list) {
                daoHangList=list;
                titleRecyclerAdapter.setData(daoHangList);
            }
        });
    }

    private void initRefreshLayout() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setRefreshFooter(new RefreshFooterWrapper(new ClassicsFooter(getContext())));
        refreshLayout.setRefreshHeader(new RefreshHeaderWrapper(new ClassicsHeader(getContext())));
    }


    private int card_width, card_height;
    private int currentPosition = -1;


    private void initViews() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemViewCacheSize(10);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
//        adapter = new QuanZiTuiJianAdapter(getActivity());
//        adapter.setOnChangeXingQuListener(this);
//        mRecyclerView.setAdapter(adapter);
//        adapter.setWidthHeight(card_width, card_height);
        circleTuiJianAdapter = new CircleTuiJianAdapter(getContext(), card_width, card_height);
        mRecyclerView.setAdapter(circleTuiJianAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {//停止滑动
                    if (disX == 0) {
                        currentPosition = 0;
                    } else {
                        currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(1).getLayoutParams()).getViewAdapterPosition();
                    }
//                    tuiJianList.get(currentPosition).setPlay(true);
//                    System.out.println("currentPosition: " + currentPosition);
                    circleTuiJianAdapter.notifyItemChanged(currentPosition);
                } else {//滑动
                    if (currentPosition > -1) {
                        tuiJianList.get(currentPosition).setPlay(false);
                        circleTuiJianAdapter.notifyItemChanged(currentPosition);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                disX -= dx;
                scaleRecyclerViewItem();
            }
        });


        titleRecyclerView = view.findViewById(R.id.titleRecyclerView);
        titleRecyclerAdapter = new QuanZiTuiJianTitleRecyclerAdapter(getContext());
        centerLayoutManager = new CenterLayoutManager(getContext());
        centerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        titleRecyclerView.setLayoutManager(centerLayoutManager);
        titleRecyclerView.setAdapter(titleRecyclerAdapter);
        titleRecyclerAdapter.setOnTitleClickListener(this);
        addLabelImageView = view.findViewById(R.id.addLabelImageView);
        addLabelImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addLabelImageView:
                quanZiTuiJianLabelDialog.show();
                quanZiTuiJianLabelDialog.setCityName(cityName);
                break;
        }
    }

    @Override
    public void onTitleClick(int position) {
        for (QuanZiTuiJianDaoHang tuiJianTitle : daoHangList) {
            tuiJianTitle.setChoose(false);
        }
        daoHangList.get(position).setChoose(true);
        titleRecyclerAdapter.setData(daoHangList);
        centerLayoutManager.smoothScrollToPosition(titleRecyclerView, new RecyclerView.State(), position);
//        tuiJianList.clear();
//        circleTuiJianAdapter.notifyDataSetChanged();
        switch (daoHangList.get(position).getType()) {

            case 1:
                tuijianFeedList();
                break;
            case 2:
                videoFeedList();
                break;
            case 3:
                feedList();
                break;
            case 4:
                districtFeedList();
                break;
            default:
                tagFeedList(daoHangList.get(position).getName());
                break;
        }
//        titleRecyclerView.scrollToPosition(position);
//        Log.i("onTitleClick", "position: " + position);
    }

    @Override
    public void onChangeXingQu(int position) {
        requestShangQuanTuiJian(position);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        page++;
        tuijianFeedList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        page = 1;
        tuijianFeedList();

    }

    private int disX, currentX;
    private float mScale = 0.8f;
    private boolean isInitScale = true;

    private void scaleRecyclerViewItem() {
        if (null != tuiJianList && tuiJianList.size() > 0) {
            View currentView = null, leftView = null, rightView = null;
            int currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(1).getLayoutParams()).getViewAdapterPosition();
            int count = tuiJianList.size();
            currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            currentView.setScaleY(1.0f);
            if (currentPosition > 0 && currentPosition < count) {
                leftView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition - 1);
                rightView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition + 1);
            }

            if (isInitScale) {
                if (null != leftView) {
                    leftView.setScaleY(mScale);
                }
                if (null != rightView) {
                    rightView.setScaleY(mScale);
                }
                isInitScale = false;
            }
            currentX = (int) ((currentPosition) * (card_width + getContext().getResources().getDimension(R.dimen.dp_10)));
            float offset = (Math.abs(disX) - currentX);
            float layout_width = (card_width + getContext().getResources().getDimension(R.dimen.dp_10));
//        layout_width=layout_width/2;
//        System.out.println("offset: " + offset + " , layout_width: " + layout_width);
            float percent = (float) Math.max(Math.abs(offset) * 1.0 / layout_width, 0.0001);
//        System.out.println("percent: " + percent);
            if (leftView != null) {
                float left_scale = (1 - mScale) * percent + mScale;
                if (left_scale > 1.0f) {
                    left_scale = 1.0f;
                }
//            System.out.println("left_scale: " + left_scale);
                if (left_scale > mScale * 1.1f)
                    leftView.setScaleY(left_scale);
            }
            if (currentView != null) {
                float current_scale = (mScale - 1) * percent + 1;
                current_scale = current_scale * 1.1f;//后面为校正系数
                if (current_scale > 1.0f) {
                    current_scale = 1.0f;
                }
//            System.out.println("current_scale: " + current_scale);
                currentView.setScaleY(current_scale);
            }
            if (rightView != null) {
                float right_scale = (1 - mScale) * percent + mScale;
                if (right_scale > 1.0f) {
                    right_scale = 1.0f;
                }
//            System.out.println("right_scale: " + right_scale);
                if (right_scale > mScale * 1.1f)
                    rightView.setScaleY(right_scale);
            }
        }
    }

    private String cityName="";
    public void setCity(String city){
        cityName=city;
        page=1;
        isRefresh=true;
        tuijianFeedList();
        requestDaoHangList();
    }

    private int page = 1;
    private boolean isRefresh = true;

    private void tuijianFeedList() {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_LIST);
        params.addBodyParameter("city_name", cityName);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                System.out.println("圈子-推荐列表: " + res);
                quanziTuijianResult = GsonUtils.GsonToBean(res, QuanZiTuiJianResult.class);
                if (isRefresh) {
                    tuiJianList.clear();
                    tuiJianList = quanziTuijianResult.getData().getList();
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                } else {
                    tuiJianList.addAll(quanziTuijianResult.getData().getList());
                    Message message = myHandler.obtainMessage(2);
                    message.sendToTarget();
                }
//                cachedVideo();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message1 = myHandler.obtainMessage(1);
                message1.sendToTarget();
                Message message2 = myHandler.obtainMessage(2);
                message2.sendToTarget();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private QuanZiTuiJianQuanZiResult quanZiTuiJianQuanZiResult;

    private void requestShangQuanTuiJian(final int position) {
        RequestParams params = new RequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHANG_QUAN_TUI_JIAN_LIST);
        params.addBodyParameter("city_name", "马鞍山");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                quanZiTuiJianQuanZiResult = GsonUtils.GsonToBean(result, QuanZiTuiJianQuanZiResult.class);
                Message message = myHandler.obtainMessage(4);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private QuanZiTuiJianDaoHangResult daoHangResult;

    private void requestDaoHangList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_LIST);
        params.addBodyParameter("city_name", cityName);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                daoHangResult = GsonUtils.GsonToBean(result, QuanZiTuiJianDaoHangResult.class);
                daoHangList = daoHangResult.getData().getList();
                Message message = myHandler.obtainMessage(6);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void cachedVideo() {
        VideoCacheUtils.getInstance().cacheListVideo(getContext(), tuiJianList, new VideoCacheUtils.OnCacheListVideoListener() {
            @Override
            public void onCacheListVideo(int position, String path) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("path", path);
                Message message = myHandler.obtainMessage(5);
                message.obj = bundle;
                message.sendToTarget();
//                System.out.println("position: " + position + " , path: " + path);
            }
        });
    }


    private void videoFeedList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_SHIPIN);
        params.addBodyParameter("city_name", SharedPreferencesUtils.getInstance(getContext()).getLocationCity());
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("视频: " + result);
                quanziTuijianResult = GsonUtils.GsonToBean(result, QuanZiTuiJianResult.class);
                if (null != quanziTuijianResult && null != quanziTuijianResult.getData() && null != quanziTuijianResult.getData().getList())
                    if (isRefresh) {
                        tuiJianList.clear();
                        tuiJianList = quanziTuijianResult.getData().getList();
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    } else {
                        tuiJianList.addAll(quanziTuijianResult.getData().getList());
                        Message message = myHandler.obtainMessage(2);
                        message.sendToTarget();
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void feedList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_DONGTAI);
        params.addBodyParameter("city_name", SharedPreferencesUtils.getInstance(getContext()).getLocationCity());
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态: " + result);
                quanziTuijianResult = GsonUtils.GsonToBean(result, QuanZiTuiJianResult.class);
                if (null != quanziTuijianResult && null != quanziTuijianResult.getData() && null != quanziTuijianResult.getData().getList())
                    if (isRefresh) {
                        tuiJianList.clear();
                        tuiJianList = quanziTuijianResult.getData().getList();
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    } else {
                        tuiJianList.addAll(quanziTuijianResult.getData().getList());
                        Message message = myHandler.obtainMessage(2);
                        message.sendToTarget();
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void districtFeedList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_DIQU);
        params.addBodyParameter("city_name", SharedPreferencesUtils.getInstance(getContext()).getLocationCity());
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("城市: " + result);
                quanziTuijianResult = GsonUtils.GsonToBean(result, QuanZiTuiJianResult.class);
                if (null != quanziTuijianResult && null != quanziTuijianResult.getData() && null != quanziTuijianResult.getData().getList())
                    if (isRefresh) {
                        tuiJianList.clear();
                        tuiJianList = quanziTuijianResult.getData().getList();
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    } else {
                        tuiJianList.addAll(quanziTuijianResult.getData().getList());
                        Message message = myHandler.obtainMessage(2);
                        message.sendToTarget();
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void tagFeedList(String tag_name) {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUAN_ZI_TUIJIAN_TAG);
        params.addBodyParameter("city_name", SharedPreferencesUtils.getInstance(getContext()).getLocationCity());
        params.addBodyParameter("tag_name", tag_name);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                quanziTuijianResult = GsonUtils.GsonToBean(result, QuanZiTuiJianResult.class);
                if (null != quanziTuijianResult && null != quanziTuijianResult.getData() && null != quanziTuijianResult.getData().getList())
                    if (isRefresh) {
                        tuiJianList.clear();
                        tuiJianList = quanziTuijianResult.getData().getList();
                        Message message = myHandler.obtainMessage(1);
                        message.sendToTarget();
                    } else {
                        tuiJianList.addAll(quanziTuijianResult.getData().getList());
                        Message message = myHandler.obtainMessage(2);
                        message.sendToTarget();
                    }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
