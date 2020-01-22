package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.RefreshFooterWrapper;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.QuanZiTuiJian;
import com.zhiyu.quanzhu.ui.adapter.HorizontalAdapter;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class HorizontalRefreshActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    private RefreshLayout refreshLayout;
    private RecyclerView horizontalListView;
    private HorizontalAdapter adapter;
    private List<QuanZiTuiJian> tuiJianList = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<HorizontalRefreshActivity> activityWeakReference;

        public MyHandler(HorizontalRefreshActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HorizontalRefreshActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.refreshLayout.finishRefresh();
                    break;
                case 2:
                    activity.refreshLayout.finishLoadMore();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontalrefresh);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
//        initDatas();
        initViews();
    }

//    private void initDatas() {
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201908/18/20190818100639_gihad.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201711/06/20171106035744_xNyTs.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201905/26/20190526153842_rwgjn.jpg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201610/22/20161022081650_yMUXz.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201503/22/20150322193457_LYiQ5.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201406/02/20140602182744_cGkZj.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201608/03/20160803145857_rcEfs.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201907/13/20190713145328_xusth.jpg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201907/17/20190717005632_wcshp.jpg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201803/02/20180302174002_H25rA.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201609/02/20160902173720_ZBYrP.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201706/04/20170604135619_sBeVQ.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201804/25/20180425210939_pnwfv.jpg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201908/02/20190802145503_kuyjx.jpg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201703/05/20170305114934_V3ZfF.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201610/20/20161020141325_SsLdu.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201412/29/20141229184401_dMefv.jpeg"));
//        tuiJianList.add(new QuanZiTuiJian("https://c-ssl.duitang.com/uploads/item/201506/01/20150601104036_nXUhE.jpeg"));
//    }

    private void initViews() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setRefreshFooter(new RefreshFooterWrapper(new ClassicsHeader(this)), -1, -2);
        refreshLayout.setRefreshHeader(new RefreshHeaderWrapper(new ClassicsHeader(this)));
        horizontalListView = findViewById(R.id.horizontalListView);
        adapter = new HorizontalAdapter(this);
        adapter.setList(tuiJianList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalListView.setAdapter(adapter);
        horizontalListView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        System.out.println("onLoadMore");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message = myHandler.obtainMessage(2);
                    message.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        System.out.println("onRefresh");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message = myHandler.obtainMessage(1);
                    message.sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
