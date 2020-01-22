package com.zhiyu.quanzhu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.model.result.StoreResult;
import com.zhiyu.quanzhu.ui.adapter.MingPianDianPuRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianDianPuShangPinIndexAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianInfoDongTaiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianInfoQuanZiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CardInfoMenuDialog;
import com.zhiyu.quanzhu.ui.dialog.MyQRCodeDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.widget.MaxRecyclerView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.ui.widget.VideoPlayer;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的名片详情
 */
public class MyCardInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView;
    private ScrollView mScrollView;
    private RecyclerView dianpuRecyclerView;
    private MingPianDianPuRecyclerAdapter mingPianDianPuRecyclerAdapter;
    private LinearLayout contentLayout;
    private CardInfoMenuDialog cardInfoMenuDialog;
    private ImageView menuImage;
    private int menuImageY;
    private ShareDialog shareDialog;
    private YNDialog ynDialog;
    private RecyclerView dongtaiRecyclerView;
    private MingPianInfoDongTaiRecyclerAdapter mingPianInfoDongTaiRecyclerAdapter;
    private MaxRecyclerView quanziRecyclerView;
    private MingPianInfoQuanZiRecyclerAdapter mingPianInfoQuanZiRecyclerAdapter;
    private MaxRecyclerView quanziIndexRecyclerView;
    private VideoPlayer mVideoPlayer;
    private ImageView mImageView;
    private TextView contentTextView;
    private LinearLayout.LayoutParams videoPlayerParams;
    private long card_id, uid;
    private RoundImageView headerImageView;
    private TextView nameTextView, positionTextView, companyTextView, mobileTextView, wxTextView, emailTextView, shareCountTextView, viewCountTextView, priseCountTextView, collectCountTextView;
    private TextView viewQrCodeTextView, shareQRCodeTextView;
    private MingPianDianPuShangPinIndexAdapter quanziIndexAdapter;
    private MyQRCodeDialog qrCodeDialog;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<MyCardInformationActivity> activityWeakReference;

        public MyHandler(MyCardInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyCardInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    Glide.with(activity).load(activity.cardResult.getData().getDetail().getCard_thumb()).into(activity.headerImageView);
                    activity.nameTextView.setText(activity.cardResult.getData().getDetail().getCard_name());
                    activity.positionTextView.setText(activity.cardResult.getData().getDetail().getOccupation());
                    activity.companyTextView.setText(activity.cardResult.getData().getDetail().getCompany());
                    activity.mobileTextView.setText(activity.cardResult.getData().getDetail().getMobile());
                    activity.wxTextView.setText(activity.cardResult.getData().getDetail().getWx());
                    activity.emailTextView.setText(activity.cardResult.getData().getDetail().getEmail());
                    activity.shareCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getShare()));
                    activity.viewCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getView()));
                    activity.priseCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getPrise()));
                    activity.collectCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getCollect()));
                    Glide.with(activity).load(activity.cardResult.getData().getDetail().getImg()).into(activity.mImageView);
                    activity.contentTextView.setText(activity.cardResult.getData().getDetail().getIntro());
                    if (null != activity.cardResult.getData().getDetail().getVideo_intro()) {
                        String url = activity.cardResult.getData().getDetail().getVideo_intro();
//                        String path = "https://flv3.bn.netease.com/2840ec043817f032b816a9b8644a77cc0175339ac7e1a9270bfa5137263205604814e273e5d023c06d4f6663d7fe2169324e57dfb26c68f16608d536a251febf24cc5be7653eb69a25937b4dabbce31a5ae7e4d8fa47413ead72e68518ac2dbd70d60cd1af8ae9cf4a31cc1dc9bde179846a0c4e38019b85.mp4";
//                        String path="https://flv3.bn.netease.com/2840ec043817f032b5d015aeb04846f55e2631a4920597fb54f2600fadc6a9ac46bf411a075106bad55b9f199d153a082713a5d02153f3595b60b499cc44d0d9a5f827df28db522cce2262e815a85146074e7583145b4cc5b2cf70bff96fc5fde0c54ba574eedc8451e61cb84373b65da951d796a83476c3.mp4";
                        activity.initVideoPlayer(url);
                    }
                    break;
                case 2:
                    activity.mingPianInfoQuanZiRecyclerAdapter.setList(activity.circleResult.getData().getList());
                    activity.quanziIndexAdapter.setIndexSize(activity.circleResult.getData().getList().size());
                    activity.quanziIndexAdapter.setCurrentIndex(0);
                    break;
                case 3:
                    activity.mingPianDianPuRecyclerAdapter.setList(activity.storeResult.getData().getList());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_information);
        card_id = getIntent().getLongExtra("card_id", 0l);
        uid = getIntent().getLongExtra("uid", 0l);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
        circleList();
        storeList();
        dongtaiList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardinfo();
    }

    private void initDialogs() {
        cardInfoMenuDialog = new CardInfoMenuDialog(this, R.style.dialog, new CardInfoMenuDialog.OnMyMingPianMenuChooseListener() {
            @Override
            public void onMyMingPianMenuChoose(int position, String desc) {
                switch (position) {
                    case 1:
                        shareDialog.show();
                        break;
                    case 2:
                        Intent editIntent = new Intent(MyCardInformationActivity.this, EditCardActivity.class);
                        editIntent.putExtra("card_id", card_id);
                        startActivity(editIntent);
                        break;
                }
            }
        });
        shareDialog = new ShareDialog(this, this, R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(MyCardInformationActivity.this, ShareInnerActivity.class));
                        overridePendingTransition(R.anim.dialog_show, R.anim.activity_silent);
                        break;
                }
            }
        });

        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                Log.i("YNDialog", "onConfirm");
            }
        });

        qrCodeDialog = new MyQRCodeDialog(this, R.style.dialog);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mVideoPlayer)
            mVideoPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mVideoPlayer)
            mVideoPlayer.release();
    }

    private void initViews() {
        mScrollView = findViewById(R.id.mScrollView);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的名片");
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);

        headerImageView = findViewById(R.id.headerImageView);
        nameTextView = findViewById(R.id.nameTextView);
        positionTextView = findViewById(R.id.positionTextView);
        companyTextView = findViewById(R.id.companyTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        wxTextView = findViewById(R.id.wxTextView);
        emailTextView = findViewById(R.id.emailTextView);
        shareCountTextView = findViewById(R.id.shareCountTextView);
        viewCountTextView = findViewById(R.id.viewCountTextView);
        priseCountTextView = findViewById(R.id.priseCountTextView);
        collectCountTextView = findViewById(R.id.collectCountTextView);
        viewQrCodeTextView = findViewById(R.id.viewQrCodeTextView);
        viewQrCodeTextView.setOnClickListener(this);
        shareQRCodeTextView = findViewById(R.id.shareQRCodeTextView);
        shareQRCodeTextView.setOnClickListener(this);

        contentLayout = findViewById(R.id.contentLayout);
        mImageView = findViewById(R.id.mImageView);
        contentTextView = findViewById(R.id.contentTextView);
        mVideoPlayer = findViewById(R.id.mVideoPlayer);

        dianpuRecyclerView = findViewById(R.id.dianpuRecyclerView);
        mingPianDianPuRecyclerAdapter = new MingPianDianPuRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        dianpuRecyclerView.setLayoutManager(linearLayoutManager1);
        dianpuRecyclerView.setAdapter(mingPianDianPuRecyclerAdapter);
        dianpuRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        menuImage = findViewById(R.id.menuImage);
        menuImage.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        menuImage.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] location = new int[2];
                        menuImage.getLocationOnScreen(location);
                        menuImageY = location[1];
                        int menuviewheight = menuImage.getHeight(); // 获取高度
                        menuImageY += menuviewheight;
                        Log.i("CardInfoMenuDialog", "menuImageY: " + menuImageY + " , : " + ScreentUtils.getInstance().px2dip(MyCardInformationActivity.this, menuImageY));
                        return true;
                    }
                });

        dongtaiRecyclerView = findViewById(R.id.dongtaiRecyclerView);
        mingPianInfoDongTaiRecyclerAdapter = new MingPianInfoDongTaiRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        dongtaiRecyclerView.setAdapter(mingPianInfoDongTaiRecyclerAdapter);
        dongtaiRecyclerView.setLayoutManager(linearLayoutManager2);
        dongtaiRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        quanziRecyclerView = findViewById(R.id.quanziRecyclerView);
        mingPianInfoQuanZiRecyclerAdapter = new MingPianInfoQuanZiRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        quanziRecyclerView.setLayoutManager(linearLayoutManager3);
        quanziRecyclerView.setAdapter(mingPianInfoQuanZiRecyclerAdapter);
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(quanziRecyclerView);
        quanziIndexRecyclerView = findViewById(R.id.quanziIndexRecyclerView);
        quanziIndexAdapter = new MingPianDianPuShangPinIndexAdapter(this);
        final LinearLayoutManager quanziindexManager = new LinearLayoutManager(this);
        quanziindexManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        quanziIndexRecyclerView.setLayoutManager(quanziindexManager);
        quanziIndexRecyclerView.setAdapter(quanziIndexAdapter);
        final MingPianDianPuShangPinIndexAdapter adapter = quanziIndexAdapter;
        quanziRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDx += dx;
//                System.out.println("totalDx: "+totalDx+" , screenWidth: "+ScreentUtils.getInstance().getScreenWidth(MyCardInformationActivity.this));
                int page = totalDx / ScreentUtils.getInstance().getScreenWidth(MyCardInformationActivity.this);
                quanziIndexAdapter.setCurrentIndex(page);
            }
        });
    }

    private void initVideoPlayer(String url) {
        if (null != mVideoPlayer) {
            contentLayout.removeView(mVideoPlayer);
            mVideoPlayer = null;
        }
        if (null != videoPlayerParams) {
            videoPlayerParams = null;
        }

        int video_width, video_height;
        int screenWidth, dp_15;
        float ratio = 1.778f;
        dp_15 = (int) getResources().getDimension(R.dimen.dp_15);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
        video_width = screenWidth - dp_15 * 2;
        video_height = Math.round(video_width / ratio);
        videoPlayerParams = new LinearLayout.LayoutParams(video_width, video_height);
        mVideoPlayer = new VideoPlayer(this, video_width, video_height);
        mVideoPlayer.setLayoutParams(videoPlayerParams);
        contentLayout.addView(mVideoPlayer);
        mVideoPlayer.setVideoUrl(url);
    }

    private int totalDx;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                cardInfoMenuDialog.show();
                cardInfoMenuDialog.setMenuImageY(ScreentUtils.getInstance().px2dip(MyCardInformationActivity.this, menuImageY) + 30);
                break;
            case R.id.viewQrCodeTextView:
                qrCodeDialog.show();
                qrCodeDialog.setCard(cardResult.getData().getDetail());
                break;
            case R.id.shareQRCodeTextView:

                break;
        }
    }


    private CardResult cardResult;

    private void cardinfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO);
        params.addBodyParameter("card_id", String.valueOf(card_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("名片详情: " + result);
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
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

    private CircleResult circleResult;

    private void circleList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
//                System.out.println("圈子列表: " + result);
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


    private StoreResult storeResult;

    private void storeList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.STORE_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("storeList: " + result);
                storeResult = GsonUtils.GsonToBean(result, StoreResult.class);
//                System.out.println("storeList: " + storeResult.getData().getList().size());
                Message message = myHandler.obtainMessage(3);
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

    private void dongtaiList() {
        System.out.println("uid: " + uid);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_DONG_TAI_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态列表 " + result);
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
