package com.leon.shehuibang.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingmouren.layoutmanagergroup.echelon.EchelonLayoutManager;
import com.dingmouren.layoutmanagergroup.picker.PickerLayoutManager;
import com.dingmouren.layoutmanagergroup.skidright.SkidRightLayoutManager;
import com.dingmouren.layoutmanagergroup.slide.SlideLayoutManager;
import com.leon.shehuibang.R;
import com.leon.shehuibang.model.MyResult;
import com.leon.shehuibang.model.bean.Comments;
import com.leon.shehuibang.ui.adapter.FragmentCommentsCommentsAdapter;
import com.leon.shehuibang.utils.ConstantsUtils;
import com.leon.shehuibang.utils.GsonUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FragmentHomeCommentsComments extends Fragment {
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycleView;
    private FragmentCommentsCommentsAdapter adapter;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentHomeCommentsComments> weakReference;

        public MyHandler(FragmentHomeCommentsComments fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentHomeCommentsComments fragment = weakReference.get();
            switch (msg.what) {
                case 1:
                    fragment.swipeRefreshLayout.setRefreshing(false);
                    fragment.adapter.setList(fragment.list);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_comments_comments, null);
        initViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        comments_list();
    }

    public void scrollToTop() {
//        recycleView.scrollToPosition(0);
        recycleView.smoothScrollToPosition(0);
    }

    private void initViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_color_blue, R.color.text_color_yellow, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里获取数据的逻辑
                time = 0;
                comments_list();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        recycleView = view.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        EchelonLayoutManager echelonLayoutManager = new EchelonLayoutManager(getContext());
        SkidRightLayoutManager skidRightLayoutManager = new SkidRightLayoutManager(2f, 1.2f);
        adapter = new FragmentCommentsCommentsAdapter(getContext());
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        //加载更多功能的代码
                        System.out.println("加载更多");
                        comments_list();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0) {
                    //大于0表示正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });
    }

    private MyResult myResult;
    private List<Comments> list = new ArrayList<>();
    private long time = 0;
    private boolean has_more = true;

    private void comments_list() {
        if (!has_more) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        RequestParams params = new RequestParams(ConstantsUtils.HOST + "comments/comments_list");
        params.addHeader("Content-Type", "application/json; charset=UTF-8");
        params.addParameter("user_id", 1);
        params.addParameter("time", time);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态列表: " + result);
                myResult = GsonUtils.GsonToBean2(result, MyResult.class);
                if (null != myResult && null != myResult.getComments_list() && myResult.getComments_list().size() > 0) {
                    list.addAll(myResult.getComments_list());
                    time = myResult.getComments_list().get(myResult.getComments_list().size() - 1).getCreate_time();
                    has_more = true;
                } else {
                    has_more = false;
                }
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("动态列表: " + ex.toString());
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
