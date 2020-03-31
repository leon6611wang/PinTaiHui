package com.zhiyu.quanzhu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.FeedCommentParent;
import com.zhiyu.quanzhu.model.result.FeedCommentResult;
import com.zhiyu.quanzhu.ui.adapter.ArticleInfoCommentListParentAdapter;
import com.zhiyu.quanzhu.ui.widget.MyCommentLayout;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.VideoCoverUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 我的视频播放详情
 */
public class MyVideoPlayerInfoActivity extends BaseActivity implements MyCommentLayout.OnMyCommentLayoutDistanceYListener {
    private MyHandler myHandler = new MyHandler(this);
    private VideoPlayerTrackView videoPlayer;
    private int screenWidth, screenHeight, videoWidth, videoHeight, dp_200;
    private FrameLayout.LayoutParams videoLayoutParams, commentLayoutParams;
    private LinearLayout.LayoutParams videoParams;
    private ListView listView;
    private ArticleInfoCommentListParentAdapter adapter;
    private LinearLayout videoLayout;
    private MyCommentLayout commentLayout;
    private boolean isScrollTop = false;
    private String videoUrl1 = "https://flv3.bn.netease.com/25ca1257cc7f5e6c57831ea39f452709e2e18c62caf7486aac01cc06c73af3430af3609ff2ae2e718ae5214b9bb06ad630a31061628ae7dd1a8b22a6ad271f04b65f3ebe5e307600bd8f5e3f6471ddf3c57df6a9569165d0268eb5b0f8d8607951ecfbf3d7eb583f116b943ccb93a9cd9cfe1eebd1b9df9e.mp4";
    private String videoUrl2 = "https://flv3.bn.netease.com/25ca1257cc7f5e6cf9ab441d9f771f708f16cd95c4671a906aff2b16ca86959f63eb25fd379944f8b9b42fcbc18fc1f095e5901920e2a330e181e73edce736defb86ad1938cb2d20cbfd1d84033488fa373cc05fec0c976b508636abce396c4bdd72b10e29640374e0add787e76706257888ee05ad1273dd.mp4";

    private static class MyHandler extends Handler {
        WeakReference<MyVideoPlayerInfoActivity> activityWeakReference;

        public MyHandler(MyVideoPlayerInfoActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyVideoPlayerInfoActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.adapter.setList(activity.list);
                    break;
                case 2:
                    activity.initLayoutParams();
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvideoplayer_info);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        screenHeight = ScreentUtils.getInstance().getScreenHeight(this);
        dp_200 = (int) getResources().getDimension(R.dimen.dp_200);

        initViews();
        commentList();
    }

    private void initViews() {
        videoLayout = findViewById(R.id.videoLayout);
        videoPlayer = findViewById(R.id.videoPlayer);
        commentLayout = findViewById(R.id.commentLayout);
        commentLayout.setOnMyCommentLayoutDistanceYListener(this);
        videoPlayer.setDataSource(videoUrl1, null);
        VideoCoverUtils.getInstance().setVideoCover(this, videoUrl1, videoPlayer);
        VideoCoverUtils.getInstance().getVideoWidthAndHeightAndVideoTimes(videoUrl1, new VideoCoverUtils.OnVideoWidthAndHeightListener() {
            @Override
            public void onVideoWidthAndHeight(float width, float height, float time) {
                videoWidth = (int) width;
                videoHeight = (int) height;
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }
        });

        listView = findViewById(R.id.listView);
        adapter = new ArticleInfoCommentListParentAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断顶部底部
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isScrollTop = true;
                    } else {
                        isScrollTop = false;
                    }
                    System.out.println("listView isScrollTop: " + isScrollTop);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
    }


    private void initLayoutParams() {
        videoLayoutParams = new FrameLayout.LayoutParams(screenWidth, screenHeight / 3);
        videoLayoutParams.gravity = Gravity.TOP;
        videoLayout.setLayoutParams(videoLayoutParams);
        commentLayoutParams = new FrameLayout.LayoutParams(screenWidth, screenHeight / 3 * 2);
        commentLayoutParams.gravity = Gravity.BOTTOM;
        commentLayout.setLayoutParams(commentLayoutParams);

        int width, height;
        if (videoWidth >= videoHeight) {//宽视频，视频宽度=屏幕宽度，计算对应视频高度
            height = Math.round(screenWidth * videoHeight / videoWidth);
            videoParams = new LinearLayout.LayoutParams(screenWidth, height);
        } else {//窄视频，视频高度=屏幕高度，计算对应视频宽度
            width = Math.round(screenHeight * videoWidth / videoHeight);
            videoParams = new LinearLayout.LayoutParams(width, screenHeight);
        }
        videoParams.gravity = Gravity.CENTER;
        videoPlayer.setLayoutParams(videoParams);
    }

    @Override
    public void onMyCommentLayoutDistanceY(float distanceY) {
        System.out.println("distanceY: " + distanceY);
        changeCommentLayoutParams(Math.round(distanceY));
//        if(isScrollTop){
//            changeCommentLayoutParams(Math.round(distanceY));
//        }
    }

    //评论布局
    private int commentLayoutParamsHeight = Math.round(screenHeight / 3 * 2);
    private int videoLayoutParamsHeight = Math.round(screenHeight / 3);
    private int videoParamsWidth;
    private int videoParamsHeight;
    private void changeCommentLayoutParams(int disY) {
        videoLayoutParamsHeight += disY;
        if (videoLayoutParamsHeight < Math.round(screenHeight / 3)) {
            videoLayoutParamsHeight = Math.round(screenHeight / 3);
        } else if (videoLayoutParamsHeight > screenHeight) {
            videoLayoutParamsHeight = screenHeight;
        }
        videoLayoutParams = new FrameLayout.LayoutParams(screenWidth, videoLayoutParamsHeight);
        videoLayoutParams.gravity = Gravity.TOP;
        videoLayout.setLayoutParams(videoLayoutParams);

        int width, height;
        if (videoWidth >= videoHeight) {//宽视频，视频宽度=屏幕宽度，计算对应视频高度
            height = Math.round(screenWidth * videoHeight / videoWidth);
            videoParams = new LinearLayout.LayoutParams(screenWidth, height);
        } else {//窄视频，视频高度=屏幕高度，计算对应视频宽度
            width = Math.round(screenHeight * videoWidth / videoHeight);
            videoParams = new LinearLayout.LayoutParams(width, screenHeight);
        }
        videoParams.gravity = Gravity.CENTER;
        videoPlayer.setLayoutParams(videoParams);

        commentLayoutParamsHeight -= disY;
        if (commentLayoutParamsHeight > Math.round(screenHeight / 3 * 2)) {
            commentLayoutParamsHeight = Math.round(screenHeight / 3 * 2);
        } else if (commentLayoutParamsHeight < 0) {
            commentLayoutParamsHeight = 0;
        }
        commentLayoutParams = new FrameLayout.LayoutParams(screenWidth, commentLayoutParamsHeight);
        commentLayoutParams.gravity = Gravity.BOTTOM;
        commentLayout.setLayoutParams(commentLayoutParams);
    }

    private FeedCommentResult commentResult;
    private List<FeedCommentParent> list;
    private int page = 1;
    private boolean isRefresh = true;

    private void commentList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FEEDS_COMMENTS_LIST);
        params.addBodyParameter("feeds_id", String.valueOf(25));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                commentResult = GsonUtils.GsonToBean(result, FeedCommentResult.class);
                if (isRefresh) {
                    list = commentResult.getData().getList();
                } else {
                    list.addAll(commentResult.getData().getList());
                }
                Message message = myHandler.obtainMessage(1);
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
