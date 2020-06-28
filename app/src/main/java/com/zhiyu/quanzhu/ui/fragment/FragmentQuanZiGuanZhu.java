package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leon.chic.dao.PageDao;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.model.result.QuanZiGuanZhuResult;
import com.zhiyu.quanzhu.model.result.QuanZiGuanZhuUserResult;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.adapter.CircleGuanZhuAdapter;
import com.zhiyu.quanzhu.ui.adapter.QuanZiGuanZhuHeaderRecyclerAdapter;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈子-关注
 */
public class FragmentQuanZiGuanZhu extends Fragment {
    private View view;
    private CircleGuanZhuAdapter circleGuanZhuAdapter;
    private MyRecyclerView recyclerView;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView headerRecyclerView;
    private QuanZiGuanZhuHeaderRecyclerAdapter headerAdapter;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentQuanZiGuanZhu> guanZhuWeakReference;

        public MyHandler(FragmentQuanZiGuanZhu guanZhu) {
            guanZhuWeakReference = new WeakReference<>(guanZhu);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentQuanZiGuanZhu fragment = guanZhuWeakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.isRequesting = false;
                    fragment.ptrFrameLayout.refreshComplete();
//                    fragment.adapter.setData(fragment.list);
                    fragment.circleGuanZhuAdapter.setList(fragment.feedList);
                    break;
                case 2:
                    fragment.isRequesting=false;
                    if (null != fragment.userResult && null != fragment.userResult.getData() &&
                            null != fragment.userResult.getData().getList() && fragment.userResult.getData().getList().size() > 0)
                        fragment.headerAdapter.setList(fragment.userResult.getData().getList());
                    break;
                case 3:
                    if(200==fragment.shareResult.getCode()){
                        fragment.circleGuanZhuAdapter.setShare(fragment.shareResult.getData().getShare());
                    }
                    break;
                case 99:
                    fragment.isRequesting=false;
                    fragment.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quanzi_guanzhu, container, false);
        initPtr();
        initViews();
        shareConfig();
//        initData();


        return view;
    }

    private boolean isRequesting = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRequesting && !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext()))
                && (null == feedList || feedList.size() == 0)) {
            isRequesting = true;
            isRefresh = true;
            page = 1;
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    requestMyFollows();
                    requestGuanZhuDongTaiList();
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isRequesting && !StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserToken(getContext()))
                    && (null == feedList || feedList.size() == 0)) {
                isRequesting = true;
                isRefresh = true;
                page = 1;
                ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                    @Override
                    public void run() {
                        requestMyFollows();
                        requestGuanZhuDongTaiList();
                    }
                });
            }
        }
        if (null != circleGuanZhuAdapter) {
            if (isVisibleToUser) {
                circleGuanZhuAdapter.setVideoStop(false);
            } else {
                circleGuanZhuAdapter.setVideoStop(true);
            }
        }
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
                requestGuanZhuDongTaiList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRefresh = true;
                page = 1;
                requestGuanZhuDongTaiList();
                requestMyFollows();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }


    private void initViews() {
        headerRecyclerView = view.findViewById(R.id.headerRecyclerView);
        headerAdapter = new QuanZiGuanZhuHeaderRecyclerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headerRecyclerView.setAdapter(headerAdapter);
        headerRecyclerView.setLayoutManager(linearLayoutManager);

        recyclerView = view.findViewById(R.id.recyclerView);
        circleGuanZhuAdapter = new CircleGuanZhuAdapter(getActivity(), getContext());
        LinearLayoutManager ms = new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.VERTICAL);
//        SpaceItemDecoration decoration = new SpaceItemDecoration((int) getContext().getResources().getDimension(R.dimen.dp_15));
//        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(ms);
        recyclerView.setAdapter(circleGuanZhuAdapter);

    }

    private void initData() {
        String result = PageDao.getInstance().get(QuanZiGuanZhuResult.class, BaseApplication.getInstance());
        if (!StringUtils.isNullOrEmpty(result)) {
            System.out.println("initData " + result);
            feedResult = GsonUtils.GsonToBean(result, FeedResult.class);
            feedList = feedResult.getData().getMycircles();
            circleGuanZhuAdapter.setList(feedList);
        }
        String result2 = PageDao.getInstance().get(QuanZiGuanZhuUserResult.class, BaseApplication.getInstance());
        if (!StringUtils.isNullOrEmpty(result2)) {
            userResult = GsonUtils.GsonToBean(result, QuanZiGuanZhuUserResult.class);
            if (null != userResult && null != userResult.getData() && null != userResult.getData().getList()) {
                headerAdapter.setList(userResult.getData().getList());
            }
        }
    }

    private int page = 1;
    private boolean isRefresh = true;
    private QuanZiGuanZhuResult guanZhuResult;
    private FeedResult feedResult;
    private List<Feed> feedList;

    private void requestGuanZhuDongTaiList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.HOME_QUANZI_GUANZHU_LIST);
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("动态-关注: " + result);
//                LogUtils.i("guanzhu", result);
                PageDao.getInstance().save(QuanZiGuanZhuResult.class, result, BaseApplication.getInstance());
                feedResult = GsonUtils.GsonToBean(result, FeedResult.class);
                if (isRefresh) {
                    feedList = feedResult.getData().getMycircles();
                } else {
                    feedList.addAll(feedResult.getData().getMycircles());
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("动态-关注" + ex.toString());
                Message message = myHandler.obtainMessage(99);
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

    private QuanZiGuanZhuUserResult userResult;

    private void requestMyFollows() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_FOLLOW);
        params.addBodyParameter("type", "user");
        params.addBodyParameter("page", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PageDao.getInstance().save(QuanZiGuanZhuUserResult.class, result, BaseApplication.getInstance());
                userResult = GsonUtils.GsonToBean(result, QuanZiGuanZhuUserResult.class);
//                System.out.println("我的关注列表:" + result);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("我关注的人列表: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        circleGuanZhuAdapter.setShareResultCode(requestCode, resultCode, data);
    }

    private ShareResult shareResult;
    private void shareConfig(){
        RequestParams params=MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_FEED);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("share: "+result);
                shareResult=GsonUtils.GsonToBean(result,ShareResult.class);
                Message message=myHandler.obtainMessage(3);
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
}
