package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.chic.utils.SPUtils;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.CardResult;
import com.zhiyu.quanzhu.model.result.CircleResult;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.model.result.StoreResult;
import com.zhiyu.quanzhu.ui.adapter.CardInfoFeedsAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianDianPuRecyclerAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianDianPuShangPinIndexAdapter;
import com.zhiyu.quanzhu.ui.adapter.MingPianInfoQuanZiRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CardInfoMenuDialog;
import com.zhiyu.quanzhu.ui.dialog.MyQRCodeDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.MaxRecyclerView;
import com.zhiyu.quanzhu.ui.widget.ResizableImageView;
import com.zhiyu.quanzhu.ui.widget.RoundImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.CopyUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 我的名片详情
 */
public class CardInformationActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private TextView titleTextView;
    private PtrFrameLayout ptrFrameLayout;
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
    private CardInfoFeedsAdapter mingPianInfoDongTaiRecyclerAdapter;
    private MaxRecyclerView quanziRecyclerView;
    private MingPianInfoQuanZiRecyclerAdapter mingPianInfoQuanZiRecyclerAdapter;
    private MaxRecyclerView quanziIndexRecyclerView;
    private ResizableImageView mImageView;
    private TextView contentTextView;
    private LinearLayout priseLayout;
    private ImageView priseImageView;
    private TextView priseTextView;
    private LinearLayout.LayoutParams videoPlayerParams;
    private VideoPlayerTrackView videoplayer;
    private long card_id, uid;
    private RoundImageView headerImageView;
    private TextView nameTextView, positionTextView, companyTextView, mobileTextView, wxTextView, emailTextView,
            copyWxTextView, copyEmailTextView,
            shareCountTextView, viewCountTextView, priseCountTextView, collectCountTextView;
    private TextView cardLeftTextView, cardRightTextView;
    private TextView jieshaoTextView, dianpuTextView, dongtaiTextView;
    private LinearLayout cardLeftLayout, cardRightLayout;
    private ImageView cardLeftImageView, cardRightImageView;
    private MingPianDianPuShangPinIndexAdapter quanziIndexAdapter;
    private MyQRCodeDialog qrCodeDialog;
    private boolean isMyCard;
    private int priseCount,totalPriseCount;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<CardInformationActivity> activityWeakReference;

        public MyHandler(CardInformationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CardInformationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.cardButtonLayout();
                    activity.is_prise = activity.cardResult.getData().getDetail().isIs_prise();
                    activity.priseCount = activity.cardResult.getData().getDetail().getPrise();
                    Glide.with(activity).load(activity.cardResult.getData().getDetail().getCard_thumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(activity.headerImageView);
                    activity.nameTextView.setText(activity.cardResult.getData().getDetail().getCard_name());
                    activity.titleTextView.setText(activity.isMyCard ? "我的名片" : activity.cardResult.getData().getDetail().getCard_name() + "的名片");
                    activity.positionTextView.setText(activity.cardResult.getData().getDetail().getOccupation());
                    activity.companyTextView.setText(activity.cardResult.getData().getDetail().getCompany());
                    activity.totalPriseCount=activity.cardResult.getData().getDetail().getPrise();
                    if (activity.isMyCard) {
                        activity.priseLayout.setVisibility(View.INVISIBLE);
                    } else {
                        activity.priseLayout.setVisibility(View.VISIBLE);
                        activity.priseStatus(activity.is_prise);
                    }
                    activity.priseCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getPrise()));
                    activity.mobileTextView.setText(activity.cardResult.getData().getDetail().getMobile());
                    activity.wxTextView.setText(activity.cardResult.getData().getDetail().getWx());
                    activity.emailTextView.setText(activity.cardResult.getData().getDetail().getEmail());
                    activity.shareCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getShare()));
                    activity.viewCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getView()));
                    activity.collectCountTextView.setText(String.valueOf(activity.cardResult.getData().getDetail().getCollect()));
                    Glide.with(activity).load(activity.cardResult.getData().getDetail().getImg()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                            .fallback(R.drawable.image_error).into(activity.mImageView);
                    if (!StringUtils.isNullOrEmpty(activity.cardResult.getData().getDetail().getIntro()))
                        activity.contentTextView.setText(activity.cardResult.getData().getDetail().getIntro());
                    if (!StringUtils.isNullOrEmpty(activity.cardResult.getData().getDetail().getVideo_intro())) {
                        String url = activity.cardResult.getData().getDetail().getVideo_intro();
                        activity.videoplayer.setVisibility(View.VISIBLE);
                        activity.videoplayer.setDataSource(url, null);
                        Glide.with(activity).load(url).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(activity.videoplayer.getCoverController().getVideoCover());
                    } else {
                        activity.videoplayer.setVisibility(View.GONE);
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
                case 4:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.mingPianInfoDongTaiRecyclerAdapter.setList(activity.feedList);
                    break;
                case 5:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 15://点赞
                    activity.is_prise = !activity.is_prise;
                    if (activity.is_prise) {
                        activity.priseCount++;
                        activity.totalPriseCount++;
                    } else {
                        activity.priseCount--;
                        activity.totalPriseCount--;
                    }
                    activity.priseStatus(activity.is_prise);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_information);
        card_id = getIntent().getLongExtra("card_id", 0l);
        uid = getIntent().getLongExtra("uid", 0l);
//        System.out.println("uid: " + uid);
        isMyCard = (uid == SPUtils.getInstance().getUserId(BaseApplication.applicationContext));
        if (uid == 0) {
            isMyCard = false;
        }
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initPtr();
        initViews();
        initDialogs();
        shareConfig();
        circleList();
        storeList();
        dongtaiList();
    }

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                dongtaiList();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardinfo();
        mingPianInfoDongTaiRecyclerAdapter.stopVideo(true);
    }

    private void initDialogs() {
        cardInfoMenuDialog = new CardInfoMenuDialog(this, R.style.dialog, new CardInfoMenuDialog.OnMyMingPianMenuChooseListener() {
            @Override
            public void onMyMingPianMenuChoose(int position, String desc) {
                switch (position) {
                    case 1:
                        shareResult.getData().getShare().setImage_url(cardResult.getData().getDetail().getCard_thumb());
                        shareResult.getData().getShare().setContent(cardResult.getData().getDetail().getCard_name());
                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CARD);
                        shareResult.getData().getShare().setUid((int) uid);
                        shareDialog.show();
                        shareDialog.setShare(shareResult.getData().getShare(),(int)cardResult.getData().getDetail().getUid());
                        break;
                    case 2:
                        Intent editIntent = new Intent(CardInformationActivity.this, EditCardActivity.class);
                        editIntent.putExtra("card_id", card_id);
                        startActivity(editIntent);
                        break;
                    case 3:
                        collect();
                        break;
                    case 4:
                        Intent complaintIntent = new Intent(CardInformationActivity.this, ComplaintActivity.class);
                        complaintIntent.putExtra("report_id", (int) card_id);
                        complaintIntent.putExtra("module_type", "cards");
                        startActivity(complaintIntent);
                        break;
                    case 5:
                        follow();
                        break;
                }
            }
        });
        shareDialog = new ShareDialog(this, this, R.style.dialog, new ShareDialog.OnShareListener() {
            @Override
            public void onShare(int position, String desc) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(CardInformationActivity.this, ShareInnerActivity.class));
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
        boolean isplaying = videoplayer.isPlaying();
        if (isplaying) {
            videoplayer.pause();
        }
        mingPianInfoDongTaiRecyclerAdapter.stopVideo(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        jieshaoTextView = findViewById(R.id.jieshaoTextView);
        jieshaoTextView.setText(isMyCard ? "我的介绍" : "TA的介绍");
        dianpuTextView = findViewById(R.id.dianpuTextView);
        dianpuTextView.setText(isMyCard ? "我的店铺" : "TA的店铺");
        dongtaiTextView = findViewById(R.id.dongtaiTextView);
        dongtaiTextView.setText(isMyCard ? "我的动态" : "TA的动态");
        mScrollView = findViewById(R.id.mScrollView);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(isMyCard ? "我的名片" : "名片详情");
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);

        priseLayout = findViewById(R.id.priseLayout);
        priseLayout.setOnClickListener(this);
        priseImageView = findViewById(R.id.priseImageView);
        priseTextView = findViewById(R.id.priseTextView);
        headerImageView = findViewById(R.id.headerImageView);
        nameTextView = findViewById(R.id.nameTextView);
        positionTextView = findViewById(R.id.positionTextView);
        companyTextView = findViewById(R.id.companyTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        mobileTextView.setOnClickListener(this);
        wxTextView = findViewById(R.id.wxTextView);
        emailTextView = findViewById(R.id.emailTextView);
        copyWxTextView = findViewById(R.id.copyWxTextView);
        copyWxTextView.setOnClickListener(this);
        copyEmailTextView = findViewById(R.id.copyEmailTextView);
        copyEmailTextView.setOnClickListener(this);

        shareCountTextView = findViewById(R.id.shareCountTextView);
        viewCountTextView = findViewById(R.id.viewCountTextView);
        priseCountTextView = findViewById(R.id.priseCountTextView);
        collectCountTextView = findViewById(R.id.collectCountTextView);

        cardLeftLayout = findViewById(R.id.cardLeftLayout);
        cardLeftLayout.setOnClickListener(this);
        cardLeftImageView = findViewById(R.id.cardLeftImageView);
        cardLeftTextView = findViewById(R.id.cardLeftTextView);

        cardRightLayout = findViewById(R.id.cardRightLayout);
        cardRightLayout.setOnClickListener(this);
        cardRightImageView = findViewById(R.id.cardRightImageView);
        cardRightTextView = findViewById(R.id.cardRightTextView);

        contentLayout = findViewById(R.id.contentLayout);
        mImageView = findViewById(R.id.mImageView);
        contentTextView = findViewById(R.id.contentTextView);
        videoplayer = findViewById(R.id.videoplayer);


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
//                        Log.i("CardInfoMenuDialog", "menuImageY: " + menuImageY + " , : " + ScreentUtils.getInstance().px2dip(CardInformationActivity.this, menuImageY));
                        return true;
                    }
                });

        dongtaiRecyclerView = findViewById(R.id.dongtaiRecyclerView);
        mingPianInfoDongTaiRecyclerAdapter = new CardInfoFeedsAdapter(this, this);
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
//                System.out.println("totalDx: "+totalDx+" , screenWidth: "+ScreentUtils.getInstance().getScreenWidth(CardInformationActivity.this));
                int page = totalDx / ScreentUtils.getInstance().getScreenWidth(CardInformationActivity.this);
                quanziIndexAdapter.setCurrentIndex(page);
            }
        });
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
                cardInfoMenuDialog.setInitData(isMyCard, cardResult.getData().getDetail().isIs_collect(), cardResult.getData().getDetail().isIs_follow());
                cardInfoMenuDialog.setMenuImageY(ScreentUtils.getInstance().px2dip(CardInformationActivity.this, menuImageY) + 30);
                break;
            case R.id.cardLeftLayout:
                cardButtonClick(1);
                break;
            case R.id.cardRightLayout:
                cardButtonClick(2);
                break;
            case R.id.copyWxTextView:
                if (!StringUtils.isNullOrEmpty(cardResult.getData().getDetail().getWx())) {
                    if (isMyCard) {
                        CopyUtils.getInstance().copy(CardInformationActivity.this, cardResult.getData().getDetail().getWx());
                        MessageToast.getInstance(CardInformationActivity.this).show("微信号已成功复制到剪贴板");
                    } else {
                        if (cardResult.getData().getDetail().isIs_friends()) {
                            CopyUtils.getInstance().copy(CardInformationActivity.this, cardResult.getData().getDetail().getWx());
                            MessageToast.getInstance(CardInformationActivity.this).show("微信号已成功复制到剪贴板");
                        } else {
                            MessageToast.getInstance(CardInformationActivity.this).show("成为名片圈友才能查看、复制");
                        }
                    }

                } else {
                    MessageToast.getInstance(CardInformationActivity.this).show("请等待名片主人完善微信信息");
                }
                break;
            case R.id.copyEmailTextView:
                if (!StringUtils.isNullOrEmpty(cardResult.getData().getDetail().getEmail())) {
                    if (isMyCard) {
                        CopyUtils.getInstance().copy(CardInformationActivity.this, cardResult.getData().getDetail().getEmail());
                        MessageToast.getInstance(CardInformationActivity.this).show("邮箱地址已成功复制到剪贴板");
                    } else {
                        if (cardResult.getData().getDetail().isIs_friends()) {
                            CopyUtils.getInstance().copy(CardInformationActivity.this, cardResult.getData().getDetail().getEmail());
                            MessageToast.getInstance(CardInformationActivity.this).show("邮箱地址已成功复制到剪贴板");
                        } else {
                            MessageToast.getInstance(CardInformationActivity.this).show("成为名片圈友才能查看、复制");
                        }
                    }
                } else {
                    MessageToast.getInstance(CardInformationActivity.this).show("请等待名片主人完善邮箱信息");
                }
                break;
            case R.id.mobileTextView:
                if (!StringUtils.isNullOrEmpty(cardResult.getData().getDetail().getMobile())) {
                    if (isMyCard) {
                        MessageToast.getInstance(CardInformationActivity.this).show("请等待名片主人完善电话信息");
                    } else {
                        if (cardResult.getData().getDetail().isIs_friends()) {
                            callService();
                        } else {
                            MessageToast.getInstance(CardInformationActivity.this).show("成为名片圈友才能查看、拨打电话");
                        }
                    }
                } else {
                    MessageToast.getInstance(CardInformationActivity.this).show("请等待名片主人完善电话信息");
                }
                break;
            case R.id.priseLayout:
                priseCard();
                break;
        }
    }

    private void cardButtonLayout() {
        if (isMyCard) {
            cardLeftImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_ma));
            cardLeftTextView.setText("我的名片");
            cardRightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_fenxiang));
            cardRightTextView.setText("分享此名片");
        } else {
            if (cardResult.getData().getDetail().isIs_friends()) {
                cardLeftImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_fenxiang_yellow));
                cardRightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_chat_white));
                cardLeftTextView.setText("分享此名片");
                cardRightTextView.setText("马上去聊天");
            } else {
                cardLeftImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_fenxiang_yellow));
                cardRightImageView.setImageDrawable(getResources().getDrawable(R.mipmap.mingpian_jiaohuan_white));
                cardLeftTextView.setText("分享此名片");
                cardRightTextView.setText("交换名片");
            }
        }
    }

    private void cardButtonClick(int position) {
        switch (position) {
            case 1:
                if (isMyCard) {
                    qrCodeDialog.show();
                    qrCodeDialog.setCard(cardResult.getData().getDetail());
                } else {
                    if (cardResult.getData().getDetail().isIs_friends()) {
                        shareResult.getData().getShare().setImage_url(cardResult.getData().getDetail().getCard_thumb());
                        shareResult.getData().getShare().setContent(cardResult.getData().getDetail().getCard_name());
                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CARD);
                        shareResult.getData().getShare().setUid((int) uid);
                        shareDialog.show();
                        shareDialog.setShare(shareResult.getData().getShare(),(int)cardResult.getData().getDetail().getUid());
                    } else {
                        shareResult.getData().getShare().setImage_url(cardResult.getData().getDetail().getCard_thumb());
                        shareResult.getData().getShare().setContent(cardResult.getData().getDetail().getCard_name());
                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CARD);
                        shareResult.getData().getShare().setUid((int) uid);
                        shareDialog.show();
                        shareDialog.setShare(shareResult.getData().getShare(),(int)cardResult.getData().getDetail().getUid());
                    }
                }
                break;
            case 2:
                if (isMyCard) {
                    shareResult.getData().getShare().setImage_url(cardResult.getData().getDetail().getCard_thumb());
                    shareResult.getData().getShare().setContent(cardResult.getData().getDetail().getCard_name());
                    shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CARD);
                    shareResult.getData().getShare().setUid((int) uid);
                    shareDialog.show();
                    shareDialog.setShare(shareResult.getData().getShare(),(int)cardResult.getData().getDetail().getUid());
                } else {
                    if (cardResult.getData().getDetail().isIs_friends()) {
                        chat();
                    } else {
                        exchange();
                    }
                }
                break;
        }
    }

    private void priseStatus(boolean isPrise) {
        priseImageView.setImageDrawable(getResources().getDrawable(isPrise ? R.mipmap.card_prise_yellow : R.mipmap.card_prise_gray));
        priseTextView.setTextColor(getResources().getColor(isPrise ? R.color.text_color_yellow : R.color.text_color_gray));
        priseTextView.setText(String.valueOf(priseCount));
        priseCountTextView.setText(String.valueOf(totalPriseCount));
    }

    private void chat() {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        SharedPreferencesUtils.getInstance(this).setConversationType(Conversation.ConversationType.PRIVATE.getName().toLowerCase());
        try {
            RongIM.getInstance().startPrivateChat(this, String.valueOf(cardResult.getData().getDetail().getUid()),
                    TextUtils.isEmpty(cardResult.getData().getDetail().getCard_name()) ? "聊天界面" : cardResult.getData().getDetail().getCard_name());

//            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, list.get(position).getUserId(), list.get(position).getUser_name());
        } catch (Exception e) {
            e.printStackTrace();
            MessageToast.getInstance(this).show("无法进入会话界面");
        }
    }

    /**
     * 进入拨号界面
     */
    private void callService() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + cardResult.getData().getDetail().getMobile());
        intent.setData(data);
        startActivity(intent);
    }


    private CardResult cardResult;

    private void cardinfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO);
        params.addBodyParameter("card_id", String.valueOf(card_id));
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("名片详情: " + result);
                cardResult = GsonUtils.GsonToBean(result, CardResult.class);
                is_prise = cardResult.getData().getDetail().isIs_prise();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("名片详情: " + ex.toString());
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
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO_CIRCLE_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("圈子列表: " + result);
                circleResult = GsonUtils.GsonToBean(result, CircleResult.class);
                Message message = myHandler.obtainMessage(2);
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


    private StoreResult storeResult;

    private void storeList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO_SHOP_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("storeList: " + result);
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

    private FeedResult feedResult;
    private List<Feed> feedList;
    private int page = 1;

    private void dongtaiList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_INFO_FEED_LIST);
        params.addBodyParameter("uid", String.valueOf(uid));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("动态列表 " + result);
                feedResult = GsonUtils.GsonToBean(result, FeedResult.class);
                if (null == feedList || feedList.size() == 0) {
                    feedList = feedResult.getData().getList();
                } else {
                    feedList.addAll(feedResult.getData().getList());
                }
                System.out.println("feedList: " + feedList.size());
                Message message = myHandler.obtainMessage(4);
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

    private BaseResult baseResult;
    private boolean is_prise;

    private void priseCard() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.PRISE);
        params.addBodyParameter("prise_id", String.valueOf(card_id));
        params.addBodyParameter("module_type", "cards");
        params.addBodyParameter("type", is_prise ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("点赞名片： " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (200 == baseResult.getCode()) {
                    Message message = myHandler.obtainMessage(15);
                    message.sendToTarget();
                } else {
                    Message message = myHandler.obtainMessage(5);
                    message.sendToTarget();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("点赞名片: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void collect() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.COLLECT);
        params.addBodyParameter("collect_id", String.valueOf(card_id));
        params.addBodyParameter("module_type", "cards");
        params.addBodyParameter("type", cardResult.getData().getDetail().isIs_collect() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("收藏: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
                if (200 == baseResult.getCode()) {
                    cardinfo();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("收藏: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void follow() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.FOLLOW);
        params.addBodyParameter("follow_id", String.valueOf(cardResult.getData().getDetail().getUid()));
        params.addBodyParameter("module_type", "user");
        params.addBodyParameter("type", cardResult.getData().getDetail().isIs_follow() ? "1" : "0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("关注: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
                if (200 == baseResult.getCode()) {
                    cardinfo();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("收藏: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void exchange() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CARD_EXCHANGE);
        params.addBodyParameter("tuid", String.valueOf(cardResult.getData().getDetail().getId()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("交换: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
                if (200 == baseResult.getCode()) {
                    cardinfo();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("收藏: " + ex.toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
    }

    private ShareResult shareResult;
    private void shareConfig(){
        RequestParams params=MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_CARD);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult=GsonUtils.GsonToBean(result,ShareResult.class);
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
