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

import com.leon.chic.dao.PageDao;
import com.leon.chic.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.RefreshFooterWrapper;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJianDaoHang;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianDaoHangResult;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianQuanZiResult;
import com.zhiyu.quanzhu.model.result.QuanZiTuiJianResult;
import com.zhiyu.quanzhu.ui.adapter.CircleTuiJianAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiTuiJianTitleRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.LoadingDialog;
import com.zhiyu.quanzhu.ui.dialog.QuanZiTuiJianLabelDialog;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.CenterLayoutManager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
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
        CircleTuiJianAdapter.OnChangeXingQuListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private RefreshLayout refreshLayout;
    private CircleTuiJianAdapter circleTuiJianAdapter;
    private MyRecyclerView titleRecyclerView;
    private QuanZiTuiJianTitleRecyclerAdapter titleRecyclerAdapter;
    private List<QuanZiTuiJianDaoHang> daoHangList;
    private CenterLayoutManager centerLayoutManager;
    private ImageView addLabelImageView;
    private QuanZiTuiJianLabelDialog quanZiTuiJianLabelDialog;
    private List<QuanZiTuiJian> tuiJianList = new ArrayList<>();
    private QuanZiTuiJianResult quanziTuijianResult;
    private LoadingDialog loadingDialog;
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
                    fragment.isRequesting=false;
                    if (fragment.loadingDialog.isShowing()) {
                        fragment.loadingDialog.dismiss();
                    }
                    fragment.refreshLayout.finishRefresh();
                    fragment.circleTuiJianAdapter.setList(fragment.tuiJianList);
                    break;
                case 2:
                    fragment.isRequesting=false;
                    if (fragment.loadingDialog.isShowing()) {
                        fragment.loadingDialog.dismiss();
                    }
                    fragment.refreshLayout.finishLoadMore();
                    fragment.circleTuiJianAdapter.setList(fragment.tuiJianList);
                    break;
                case 3:
                    fragment.isRequesting=false;
                    if (fragment.loadingDialog.isShowing()) {
                        fragment.loadingDialog.dismiss();
                    }
                    fragment.refreshLayout.finishRefresh();
                    fragment.refreshLayout.finishLoadMore();
                    break;
                case 4://感兴趣的圈子推荐
                    if (fragment.loadingDialog.isShowing()) {
                        fragment.loadingDialog.dismiss();
                    }
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
        int sw = ScreentUtils.getInstance().getScreenWidth(getContext());
        int sh = ScreentUtils.getInstance().getScreenHeight(getContext());
        float ratio = (float) sw / (float) sh;
        float ratio2 = (float) 9 / (float) 16;
        System.out.println("ratio: " + ratio + " , ratio2: " + ratio2);
        float width = ratio * card_height;
        card_width = Math.round(width);
        try {
            cityName = SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRefreshLayout();
        initViews();
//        initData();
        initDialogs();

        return view;
    }

    private boolean isRequesting = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting && (null == tuiJianList || tuiJianList.size() == 0)) {
            isRequesting = true;
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    requestDaoHangList();
                    tuiJianList();
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isRequesting && (null == tuiJianList || tuiJianList.size() == 0)) {
            isRequesting = true;
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    requestDaoHangList();
                    tuiJianList();
                }
            });
        }
        if (null != circleTuiJianAdapter)
            if (isVisibleToUser) {
                circleTuiJianAdapter.setVideoStop(false);
            } else {
                circleTuiJianAdapter.setVideoStop(true);
            }
    }

    private void initDialogs() {
        quanZiTuiJianLabelDialog = new QuanZiTuiJianLabelDialog(getActivity(), R.style.dialog, new QuanZiTuiJianLabelDialog.OnDaoHangCallbackListener() {
            @Override
            public void onDaoHangCallback(List<QuanZiTuiJianDaoHang> list) {
                daoHangList = list;
                titleRecyclerAdapter.setData(daoHangList);
            }
        });
        loadingDialog = new LoadingDialog(getContext(), R.style.dialog);
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
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemViewCacheSize(10);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
        circleTuiJianAdapter = new CircleTuiJianAdapter(getContext(), card_width, card_height);
        circleTuiJianAdapter.setOnChangeXingQuListener(this);
        mRecyclerView.setAdapter(circleTuiJianAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {//停止滑动
                    if (disX == 0) {
                        currentPosition = 0;
                    } else {
                        try {
                            currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(1).getLayoutParams()).getViewAdapterPosition();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    circleTuiJianAdapter.notifyItemChanged(currentPosition);

//                    tuiJianList.get(currentPosition).setPlay(true);
//                    System.out.println("currentPosition: " + currentPosition);

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

    private int requetType;

    @Override
    public void onTitleClick(int position) {
        loadingDialog.show();
        tuiJianList.clear();
        circleTuiJianAdapter.setList(tuiJianList);
        disX = 0;
        currentX = 0;
        mScale = 0.8f;
        isInitScale = true;
        requetType = position;
        page = 1;
        isRefresh = true;

        for (QuanZiTuiJianDaoHang tuiJianTitle : daoHangList) {
            tuiJianTitle.setChoose(false);
        }
        daoHangList.get(position).setChoose(true);
        titleRecyclerAdapter.setData(daoHangList);
        centerLayoutManager.smoothScrollToPosition(titleRecyclerView, new RecyclerView.State(), position);
        switch (requetType) {
            case 0:
                tuiJianList();
                break;
            case 1:
                videoList();
                break;
            case 2:
                feedList();
                break;
            case 3:
                cityList();
                break;
            default:
                tagFeedList(daoHangList.get(position).getName());
                break;
        }
    }

    @Override
    public void onChangeXingQu(int position) {
        requestShangQuanTuiJian(position);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        page++;
        switch (requetType) {
            case 0:
                tuiJianList();
                break;
            case 1:
                videoList();
                break;
            case 2:
                feedList();
                break;
            case 3:
                cityList();
                break;
            default:
                tagFeedList(daoHangList.get(requetType).getName());
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        page = 1;
        switch (requetType) {
            case 0:
                tuiJianList();
                break;
            case 1:
                videoList();
                break;
            case 2:
                feedList();
                break;
            case 3:
                cityList();
                break;
            default:
                tagFeedList(daoHangList.get(requetType).getName());
                break;
        }

    }

    private int disX, currentX;
    private float mScale = 0.8f;
    private boolean isInitScale = true;

    private void scaleRecyclerViewItem() {
        if (null != tuiJianList && tuiJianList.size() > 0) {
            View currentView = null, leftView = null, rightView = null;
            int currentPosition = 0;
            try {
                currentPosition = ((RecyclerView.LayoutParams) mRecyclerView.getChildAt(1).getLayoutParams()).getViewAdapterPosition();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int count = tuiJianList.size();
            currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
            if (null != currentView)
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

    private String cityName = "";

    public void setCity(String city) {
        cityName = city;
        page = 1;
        isRefresh = true;
        tuiJianList();
        requestDaoHangList();
    }

    private int page = 1;
    private boolean isRefresh = true;

    private void tuiJianList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_LIST);
        params.addBodyParameter("city_name", cityName);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
//                System.out.println("推荐-推荐: " + res);
                quanziTuijianResult = GsonUtils.GsonToBean(res, QuanZiTuiJianResult.class);
                if (isRefresh) {
                    PageDao.getInstance().save(QuanZiTuiJianResult.class, res, BaseApplication.getInstance());
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
//                System.out.println("推荐-推荐: " + ex.toString());
                Message message = myHandler.obtainMessage(3);
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

    private QuanZiTuiJianQuanZiResult quanZiTuiJianQuanZiResult;

    private void requestShangQuanTuiJian(final int position) {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHANG_QUAN_TUI_JIAN_LIST);
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("感兴趣的圈子: " + result);
                quanZiTuiJianQuanZiResult = GsonUtils.GsonToBean(result, QuanZiTuiJianQuanZiResult.class);
                Message message = myHandler.obtainMessage(4);
                message.obj = position;
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("感兴趣的圈子: " + ex.toString());
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
//                System.out.println("推荐-导航: " + result);
                daoHangResult = GsonUtils.GsonToBean(result, QuanZiTuiJianDaoHangResult.class);
                daoHangList = daoHangResult.getData().getList();
                Message message = myHandler.obtainMessage(6);
                message.sendToTarget();
                PageDao.getInstance().save(QuanZiTuiJianDaoHangResult.class, result, BaseApplication.getInstance());
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


    private void videoList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_SHIPIN);
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("推荐-视频: " + result);
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
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("推荐-视频: " + ex.toString());
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
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("推荐-动态: " + result);
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
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("推荐-动态: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void cityList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_TUIJIAN_DAOHANG_DIQU);
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("推荐-城市" + result);
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
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
                System.out.println("推荐-城市: " + ex.toString());
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
        params.addBodyParameter("city_name", SPUtils.getInstance().getLocationCity(BaseApplication.applicationContext));
        params.addBodyParameter("tag_name", tag_name);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("推荐-标签: " + result);
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
                System.out.println("推荐-标签: " + ex.toString());
                Message message = myHandler.obtainMessage(3);
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
