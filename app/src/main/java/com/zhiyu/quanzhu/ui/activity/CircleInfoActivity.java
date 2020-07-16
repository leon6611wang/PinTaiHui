package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.qiniu.android.utils.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.Feed;
import com.zhiyu.quanzhu.model.result.AlipayOrderInfo;
import com.zhiyu.quanzhu.model.result.CircleInfoResult;
import com.zhiyu.quanzhu.model.result.CircleInfoShopResult;
import com.zhiyu.quanzhu.model.result.CircleInfoUserResult;
import com.zhiyu.quanzhu.model.result.FeedResult;
import com.zhiyu.quanzhu.model.result.OrderAddResult;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.model.result.WxpayOrderInfo;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoFeedsAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoMemberListAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoShopListAdapter;
import com.zhiyu.quanzhu.ui.dialog.CircleInfoEditDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleInfoJoinPayDialog;
import com.zhiyu.quanzhu.ui.dialog.PasswordCheckDialog;
import com.zhiyu.quanzhu.ui.dialog.PayWayDialog;
import com.zhiyu.quanzhu.ui.dialog.ReviewFullTextDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoCirclerWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoJoinedWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoUnjoinWindow;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.Indicator;
import com.zhiyu.quanzhu.ui.widget.MyListView;
import com.zhiyu.quanzhu.utils.AliPayUtils;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideImageLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.PriceParseUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ShareUtils;
import com.zhiyu.quanzhu.utils.WxPayUtils;
import com.zhiyu.quanzhu.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈子详情
 */
public class CircleInfoActivity extends BaseActivity implements AbsListView.OnScrollListener, View.OnClickListener,
        AliPayUtils.OnAlipayCallbackListener, WXEntryActivity.OnWxpayCallbackListener {
    private Banner banner;
    private List<String> imageUrl = new ArrayList<>();
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    //    private CircleInfoListAdapter adapter;
    private CircleInfoFeedsAdapter adapter;
    private View headerView;
    private View topMenuView1, topMenuView2;
    private HorizontalListView memberListView;
    private CircleInfoMemberListAdapter memberListAdapter;
    private MyListView shopListView;
    private CircleInfoShopListAdapter shopListAdapter;
    private ReviewFullTextDialog reviewFullTextDialog;
    private TextView introduceTextView, noticeTextView, titleTextView2, joinCircleTextView;
    private LinearLayout rightLayout1, rightLayout2, backLayout1, backLayout2;
    private boolean isCircler = false;//是否圈主
    private boolean isJoined = false;//是否加入
    private CircleInfoEditDialog editDialog;
    private YNDialog ynDialog;
    private ShareDialog shareDialog;
    private CircleInfoJoinPayDialog circleInfoJoinPayDialog;
    private PayWayDialog payWayDialog;
    private PasswordCheckDialog passwordCheckDialog;
    private long circle_id;
    private CircleImageView avatarImageView;
    private TextView nameTextView, cityTextView, industryTextViwe, pnumTextView, createDaysTextView, pCountTextView;
    private LinearLayout indicatorlayout;
    private Indicator indicator;
    private VideoPlayerTrackView videoPlayer;
    private MyHandler myHandler = new MyHandler(this);
    private int own = -1;

    private static class MyHandler extends Handler {
        WeakReference<CircleInfoActivity> circleInfoActivityWeakReference;

        public MyHandler(CircleInfoActivity activity) {
            circleInfoActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleInfoActivity activity = circleInfoActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == activity.circleInfoResult.getCode()) {
                        activity.imageUrl = activity.circleInfoResult.getData().getImgs();
                        activity.buttonStatusChange(activity.circleInfoResult.getData().isIs_apply());
                        activity.initBanner();
                        Glide.with(activity).load(activity.circleInfoResult.getData().getThumb())
                                .error(R.drawable.image_error)
                                .into(activity.avatarImageView);
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getName()))
                            activity.nameTextView.setText(activity.circleInfoResult.getData().getName());
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getCity_name())) {
                            activity.cityTextView.setText(activity.circleInfoResult.getData().getCity_name());
                            activity.cityTextView.setVisibility(View.VISIBLE);
                        } else {
                            activity.cityTextView.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getThree_industry())) {
                            activity.industryTextViwe.setText(activity.circleInfoResult.getData().getThree_industry());
                            activity.industryTextViwe.setVisibility(View.VISIBLE);
                        } else {
                            activity.industryTextViwe.setVisibility(View.GONE);
                        }
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getNotice()))
                            activity.noticeTextView.setText(activity.circleInfoResult.getData().getNotice());
                        if (!StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getDescirption()))
                            activity.introduceTextView.setText(activity.circleInfoResult.getData().getDescirption());
                        activity.videoPlayer.setVisibility(com.qiniu.android.utils.StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getVideo()) ? View.GONE : View.VISIBLE);
                        activity.videoPlayer.setDataSource(activity.circleInfoResult.getData().getVideo(), "");
                        Glide.with(activity).load(activity.circleInfoResult.getData().getVideo()).apply(BaseApplication.getInstance().getVideoCoverImageOption()).into(activity.videoPlayer.getCoverController().getVideoCover());
                        activity.pnumTextView.setText(activity.circleInfoResult.getData().getPnum() + "人");
                        activity.createDaysTextView.setText("已创建 " + activity.circleInfoResult.getData().getDays() + " 天");
                        activity.pCountTextView.setText(activity.circleInfoResult.getData().getPnum() + "人加入");
//                    activity.shopListAdapter.setList(activity.circleInfoResult.getData().getStores());
                        activity.titleTextView2.setText(activity.circleInfoResult.getData().getName());
                        if (!activity.circleInfoResult.getData().isIs_join()) {
                            activity.joinCircleTextView.setVisibility(View.VISIBLE);
                            if (activity.circleInfoResult.getData().getIs_price() == 1) {
                                activity.joinCircleTextView.setText("付费加入 " + (activity.circleInfoResult.getData().getPrice() / 100) + "元/年");
                            } else {
                                activity.joinCircleTextView.setText("加入圈子");
                            }
                        } else {
                            activity.joinCircleTextView.setVisibility(View.GONE);
                        }
                    } else {
                        MessageToast.getInstance(activity).show(activity.circleInfoResult.getMsg());
                    }
                    break;
                case 2:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.feedList);
                    break;
                case 3:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.circleBase();
                    }
                    break;
                case 4:
                    if (null != activity.userResult && null != activity.userResult.getData() && null != activity.userResult.getData().getList())
                        activity.memberListAdapter.setList(activity.userResult.getData().getList());
                    break;
                case 5:
                    if (null != activity.shopResult && null != activity.shopResult.getData() && null != activity.shopResult.getData().getStores())
                        activity.shopListAdapter.setList(activity.shopResult.getData().getStores());
                    break;
                case 6:
                    MessageToast.getInstance(activity).show(activity.orderAddResult.getMsg());
                    break;
                case 10:
                    activity.circleInfoJoinPayDialog.show();
                    activity.circleInfoJoinPayDialog.setPrice(PriceParseUtils.getInstance().parsePrice(activity.circleInfoResult.getData().getPrice()));
                    break;
                case 11://微信支付订单详情回调
                    if (200 == activity.wxpayOrderInfo.getCode()) {
                        WxPayUtils.getInstance().wxPay(activity, activity.wxpayOrderInfo.getData());
                    } else {
                        MessageToast.getInstance(activity).show(activity.wxpayOrderInfo.getMsg());
                    }
                    break;
                case 12://支付宝支付订单详情回调
                    if (200 == activity.alipayOrderInfo.getCode()) {
                        AliPayUtils.getInstance(activity).aliPay(activity, activity.alipayOrderInfo.getData());
                    } else {
                        MessageToast.getInstance(activity).show(activity.alipayOrderInfo.getMsg());
                    }
                    break;
                case 13:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    if (activity.baseResult.getCode() == 200) {
                        activity.circleBase();
                    }
                    break;
                case 14:
                    MessageToast.getInstance(activity).show(activity.orderAddResult.getMsg());
                    break;
                case 99:
                    MessageToast.getInstance(activity).show("服务器内部错误，请稍后再试");
                    break;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_info);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
//        System.out.println("circleinfo circle_id: "+circle_id);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        AliPayUtils.getInstance(this).setOnAlipayCallbackListener(this);
        WXEntryActivity.setOnWxpayCallbackListener(this);
        initPtr();
        initViews();
        shopList();
        feedList();
        shareConfig();
    }

    @Override
    public void onAlipayCallBack() {
        circleBase();
    }

    @Override
    public void onWxpayCallback() {
        circleBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleBase();
        userList();
        if (null != adapter)
            adapter.setVideoStop(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != adapter)
            adapter.setVideoStop(true);
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
                feedList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
    }

    private void initViews() {
        rightLayout1 = findViewById(R.id.rightLayout1);
        rightLayout1.setOnClickListener(this);
        rightLayout2 = findViewById(R.id.rightLayout2);
        rightLayout2.setOnClickListener(this);
        backLayout1 = findViewById(R.id.backLayout1);
        backLayout2 = findViewById(R.id.backLayout2);
        titleTextView2 = findViewById(R.id.titleTextView2);
        joinCircleTextView = findViewById(R.id.joinCircleTextView);
        joinCircleTextView.setOnClickListener(this);
        backLayout1.setOnClickListener(this);
        backLayout2.setOnClickListener(this);
        mListView = findViewById(R.id.mListView);
        mListView.setOnScrollListener(this);
//        adapter = new CircleInfoListAdapter(this, this);
        adapter = new CircleInfoFeedsAdapter(this, this);
        mListView.setAdapter(adapter);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_circle_info, null);
        avatarImageView = headerView.findViewById(R.id.avatarImageView);
        nameTextView = headerView.findViewById(R.id.nameTextView);
        cityTextView = headerView.findViewById(R.id.cityTextView);
        industryTextViwe = headerView.findViewById(R.id.industryTextViwe);
        videoPlayer = headerView.findViewById(R.id.videoPlayer);
        pnumTextView = headerView.findViewById(R.id.pnumTextView);
        createDaysTextView = headerView.findViewById(R.id.createDaysTextView);
        pCountTextView = headerView.findViewById(R.id.pCountTextView);
        pnumTextView.setOnClickListener(this);
        memberListView = headerView.findViewById(R.id.memberListView);
        memberListAdapter = new CircleInfoMemberListAdapter();
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (circleInfoResult.getData().isIs_join()) {
                    Intent memberManageIntent = new Intent(CircleInfoActivity.this, CircleMemberManageActivity.class);
                    memberManageIntent.putExtra("circle_id", circle_id);
                    memberManageIntent.putExtra("own", own);
                    startActivity(memberManageIntent);
                } else {
                    MessageToast.getInstance(CircleInfoActivity.this).show("只有圈子成员才能查看，请先加入此圈。");
                }

            }
        });
        shopListView = headerView.findViewById(R.id.shopListView);
        shopListAdapter = new CircleInfoShopListAdapter(this);
        noticeTextView = headerView.findViewById(R.id.noticeTextView);
        introduceTextView = headerView.findViewById(R.id.introduceTextView);
        noticeTextView.setOnClickListener(this);
        introduceTextView.setOnClickListener(this);
        shopListView.setAdapter(shopListAdapter);
        memberListView.setAdapter(memberListAdapter);
        topMenuView1 = findViewById(R.id.topMenuView1);
        topMenuView2 = findViewById(R.id.topMenuView2);
        mListView.addHeaderView(headerView);
        banner = headerView.findViewById(R.id.banner);
        indicator = headerView.findViewById(R.id.indicator);
//        initDatas();
//        initBanner();
        initDialogs();
    }

    private String apply_content;
    private int balancePayType;

    private void initDialogs() {
        reviewFullTextDialog = new ReviewFullTextDialog(this, R.style.dialog);
        editDialog = new CircleInfoEditDialog(this, R.style.dialog, new CircleInfoEditDialog.OnEditListener() {
            @Override
            public void onEdit(int editType, String content) {
                apply_content = content;
                if (editType == 1 || editType == 2) {
                    updateCircleProfile(editType, content);
                } else if (editType == 3) {
                    joinCircle();
                }
            }
        });
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                outCircle();
            }
        });
        shareDialog = new ShareDialog(this, this, R.style.dialog);

        circleInfoJoinPayDialog = new CircleInfoJoinPayDialog(this, R.style.dialog, new CircleInfoJoinPayDialog.OnPayListener() {
            @Override
            public void onPay() {
                payWayDialog.show();
                payWayDialog.setMoney(circleInfoResult.getData().getPrice());

            }
        });
        payWayDialog = new PayWayDialog(this, R.style.dialog, new PayWayDialog.OnPayWayListener() {
            @Override
            public void onPayWay(int payWay, String payWayStr) {
                switch (payWay) {
                    case 1://微信支付
                        wxpayRequest();
                        break;
                    case 2://支付宝支付
                        alipayRequest();
                        break;
                    case 3://微信余额支付
                        passwordCheckDialog.show();
                        passwordCheckDialog.setPasswordType(2);
                        balancePayType = 1;
                        break;
                    case 4://支付宝余额支付
                        passwordCheckDialog.show();
                        passwordCheckDialog.setPasswordType(2);
                        balancePayType = 2;
                        break;
                }
            }
        });
        passwordCheckDialog = new PasswordCheckDialog(this, R.style.dialog, new PasswordCheckDialog.OnPayPwdListener() {
            @Override
            public void onPayPwd(String password) {
                if (payWayDialog.isShowing()) {
                    payWayDialog.dismiss();
                }
                balancePay(password);
            }
        });
    }


    private void initBanner() {
        indicator.removeAllViews();
        indicator.addParams(imageUrl.size(), 0, 0);
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrl);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > imageUrl.size()) {
                    position = 1;
                }
                if (position == 0) {
                    position = 1;
                }
                indicator.setCurrent(position - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mCurrentfirstVisibleItem = firstVisibleItem;
        View firstView = view.getChildAt(0);
        if (null != firstView) {
            ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
            if (null == itemRecord) {
                itemRecord = new ItemRecod();
            }
            itemRecord.height = firstView.getHeight();
            itemRecord.top = firstView.getTop();
            recordSp.append(firstVisibleItem, itemRecord);
            int h = getScrollY();//滚动距离
            //在此进行你需要的操作//TODO
            menuChange(h);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joinCircleTextView:
                editDialog.show();
                editDialog.setEditType(3);
                break;
            case R.id.pnumTextView:
                if (circleInfoResult.getData().isIs_join()) {
                    Intent memberManageIntent = new Intent(CircleInfoActivity.this, CircleMemberManageActivity.class);
                    memberManageIntent.putExtra("circle_id", circle_id);
                    memberManageIntent.putExtra("own", own);
                    startActivity(memberManageIntent);
                } else {
                    MessageToast.getInstance(this).show("只有圈子成员才可以查看哦~");
                }
                break;
            case R.id.noticeTextView:
                reviewFullTextDialog.show();
                reviewFullTextDialog.setData("圈公告", circleInfoResult.getData().getNotice());
                break;
            case R.id.introduceTextView:
                reviewFullTextDialog.show();
                reviewFullTextDialog.setData("圈子介绍", circleInfoResult.getData().getDescirption());
                break;
            case R.id.rightLayout1:
                if (isCircler) {
                    new CircleInfoCirclerWindow(this, new CircleInfoCirclerWindow.OnMenuSelectListener() {
                        @Override
                        public void onMenuSelect(int index, String menu) {
                            switch (index) {
                                case 1:
                                    shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                    shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                    shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                    shareDialog.show();
                                    shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                    break;
                                case 2:

                                    break;
                                case 3:
                                    editDialog.show();
                                    editDialog.setEditType(1);
                                    break;
                                case 4:
                                    editDialog.show();
                                    editDialog.setEditType(2);
                                    break;
                                case 5:
                                    Intent settingIntent = new Intent(CircleInfoActivity.this, CircleSettingActivity.class);
                                    settingIntent.putExtra("circle_id", circle_id);
                                    startActivityForResult(settingIntent, 10071);
                                    break;
                            }
                        }
                    }).showAtBottom(rightLayout1);
                } else {
                    if (isJoined) {
                        new CircleInfoJoinedWindow(this, new CircleInfoJoinedWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {
                                switch (index) {
                                    case 1:
                                        shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                        shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                        shareDialog.show();
                                        shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                        break;
                                    case 2:

                                        break;
                                    case 3:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
                                        complaintIntent.putExtra("report_id", (int) circle_id);
                                        complaintIntent.putExtra("module_type", "quanzi");
                                        startActivity(complaintIntent);
                                        break;
                                    case 4:
                                        ynDialog.show();
                                        ynDialog.setTitle("确定退出圈子？");
                                        break;
                                }
                            }
                        }).showAtBottom(rightLayout1);
                    } else {
                        new CircleInfoUnjoinWindow(this, new CircleInfoUnjoinWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {
                                switch (index) {
                                    case 1:
                                        shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                        shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                        shareDialog.show();
                                        shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                        break;
                                    case 2:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
                                        complaintIntent.putExtra("report_id", (int) circle_id);
                                        complaintIntent.putExtra("module_type", "quanzi");
                                        startActivity(complaintIntent);
                                        break;
                                }
                            }
                        }).showAtBottom(rightLayout1);
                    }
                }
                break;
            case R.id.rightLayout2:
                if (isCircler) {
                    new CircleInfoCirclerWindow(this, new CircleInfoCirclerWindow.OnMenuSelectListener() {
                        @Override
                        public void onMenuSelect(int index, String menu) {
                            switch (index) {
                                case 1:
                                    shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                    shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                    shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                    shareDialog.show();
                                    shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                    break;
                                case 2:

                                    break;
                                case 3:
                                    editDialog.show();
                                    editDialog.setEditType(1);
                                    break;
                                case 4:
                                    editDialog.show();
                                    editDialog.setEditType(2);
                                    break;
                                case 5:
                                    Intent settingIntent = new Intent(CircleInfoActivity.this, UpdateCircleProfileActivity.class);
                                    settingIntent.putExtra("circle_id", circle_id);
                                    startActivity(settingIntent);
                                    break;
                            }
                        }
                    }).showAtBottom(rightLayout1);
                } else {
                    if (isJoined) {
                        new CircleInfoJoinedWindow(this, new CircleInfoJoinedWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {
                                switch (index) {
                                    case 1:
                                        shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                        shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                        shareDialog.show();
                                        shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                        break;
                                    case 2:

                                        break;
                                    case 3:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
                                        complaintIntent.putExtra("report_id", (int) circle_id);
                                        complaintIntent.putExtra("module_type", "quanzi");
                                        startActivity(complaintIntent);
                                        break;
                                    case 4:
                                        ynDialog.show();
                                        ynDialog.setTitle("确定退出圈子？");
                                        break;
                                }
                            }
                        }).showAtBottom(rightLayout1);
                    } else {
                        new CircleInfoUnjoinWindow(this, new CircleInfoUnjoinWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {
                                switch (index) {
                                    case 1:
                                        shareResult.getData().getShare().setContent(circleInfoResult.getData().getName());
                                        shareResult.getData().getShare().setImage_url(circleInfoResult.getData().getThumb());
                                        shareResult.getData().getShare().setType_desc(ShareUtils.SHARE_TYPE_CIRCLE);
                                        shareDialog.show();
                                        shareDialog.setShare(shareResult.getData().getShare(), (int) circle_id);
                                        break;
                                    case 2:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
                                        complaintIntent.putExtra("report_id", (int) circle_id);
                                        complaintIntent.putExtra("module_type", "quanzi");
                                        startActivity(complaintIntent);
                                        break;
                                }
                            }
                        }).showAtBottom(rightLayout1);
                    }
                }
                break;
            case R.id.backLayout2:
                finish();
                break;
            case R.id.backLayout1:
                finish();
                break;
        }
    }

    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;

    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (null != itemRecod)
                height += itemRecod.height;
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    private void menuChange(int disY) {
        if (Math.abs(disY) > 0) {
            topMenuView1.setVisibility(View.GONE);
            topMenuView2.setVisibility(View.VISIBLE);
            ScreentUtils.getInstance().setStatusBarLightMode(CircleInfoActivity.this, true);
            float alpha = (float) Math.abs(disY) / (float) 300;
            topMenuView2.setAlpha(alpha);
        } else {
            topMenuView1.setVisibility(View.VISIBLE);
            topMenuView2.setVisibility(View.GONE);
            ScreentUtils.getInstance().setStatusBarLightMode(CircleInfoActivity.this, false);
            topMenuView2.setAlpha(0);
        }
    }

    private void buttonStatusChange(boolean is_apply) {
        if (!is_apply) {
            joinCircleTextView.setClickable(true);
            joinCircleTextView.setBackground(getResources().getDrawable(R.mipmap.button_yellow_bg));
        } else {
            joinCircleTextView.setClickable(false);
            joinCircleTextView.setBackground(getResources().getDrawable(R.drawable.shape_oval_solid_bg_gray));
        }

    }

    private CircleInfoResult circleInfoResult;

    /**
     * 圈子详情-基础信息
     */
    private void circleBase() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_BASE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("circle info : " + result);
                circleInfoResult = GsonUtils.GsonToBean(result, CircleInfoResult.class);
                isCircler = circleInfoResult.getData().isIs_own();
                isJoined = circleInfoResult.getData().isIs_join();
                own = circleInfoResult.getData().getOwn();
//                System.out.println(" --- > own: " + own);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("circle info error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private CircleInfoUserResult userResult;

    /**
     * 用户列表
     */
    private void userList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_USER_LIST);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("userList : " + result);
                userResult = GsonUtils.GsonToBean(result, CircleInfoUserResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("circle info error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private CircleInfoShopResult shopResult;

    /**
     * 商店列表
     */
    private void shopList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_INFO_SHOP_LIST);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("shopList : " + result);
                shopResult = GsonUtils.GsonToBean(result, CircleInfoShopResult.class);
                System.out.println("shopList : " + shopResult.getData().getStores().size());
                Message message = myHandler.obtainMessage(5);
                message.sendToTarget();
//                circleInfoResult = GsonUtils.GsonToBean(result, CircleInfoResult.class);
//                own = circleInfoResult.getData().getOwn();
//                System.out.println(" --- > own: " + own);
//                Message message = myHandler.obtainMessage(1);
//                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("circle info error: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private FeedResult circleInfoFeedResult;
    private int page = 1;
    private List<Feed> feedList = new ArrayList<>();

    /**
     * 圈子-动态列表
     */
    private void feedList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_INFO_FEED_LIST);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("page", String.valueOf(page));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("圈子-动态列表: " + result);
                circleInfoFeedResult = GsonUtils.GsonToBean(result, FeedResult.class);
                feedList.addAll(circleInfoFeedResult.getData().getFeeds());
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

    private BaseResult baseResult;

    private void updateCircleProfile(int type, String content) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.EDIT_CIRCLE_PROFILE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        switch (type) {
            case 1:
                params.addBodyParameter("notice", content);
                break;
            case 2:
                params.addBodyParameter("descirption", content);
                break;
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

    private OrderAddResult orderAddResult;

    private void joinCircle() {
//        System.out.println("circle_id: " + circle_id + " , content: " + apply_content);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.JOIN_CIRCLE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        params.addBodyParameter("apply_content", apply_content);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("加入圈子: " + result);
                orderAddResult = GsonUtils.GsonToBean(result, OrderAddResult.class);
                if (orderAddResult.getCode() == 200) {
                    Message message = myHandler.obtainMessage(6);
                    message.sendToTarget();
                    circleBase();
                    userList();
                } else if (orderAddResult.getCode() == 1006) {
                    Message message = myHandler.obtainMessage(10);
                    message.sendToTarget();
                } else {
                    Message message = myHandler.obtainMessage(14);
                    message.sendToTarget();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("加入圈子: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void outCircle() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.OUT_CIRCLE);
        params.addBodyParameter("circle_id", String.valueOf(circle_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("退出圈子: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                if (baseResult.getCode() == 200) {
                    circleBase();
                    userList();
                }
                Message message = myHandler.obtainMessage(6);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("退出圈子: " + ex.toString());
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
        if (null != data && requestCode == 10071) {
            finish();
        }
        shareDialog.setQQShareCallback(requestCode, resultCode, data);
        adapter.setShareResultCode(requestCode, resultCode, data);
    }

    private AlipayOrderInfo alipayOrderInfo;

    private void alipayRequest() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_ALIPAY);
        params.addBodyParameter("oid", String.valueOf(orderAddResult.getData().getOid()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("alipay: " + result);
                alipayOrderInfo = GsonUtils.GsonToBean(result, AlipayOrderInfo.class);
                Message message = myHandler.obtainMessage(12);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message message = myHandler.obtainMessage(99);
                message.sendToTarget();
                System.out.println("alipay: " + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private WxpayOrderInfo wxpayOrderInfo;

    private void wxpayRequest() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_WXPAY);
        params.addBodyParameter("oid", String.valueOf(orderAddResult.getData().getOid()));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("wxpay: " + result);
                wxpayOrderInfo = GsonUtils.GsonToBean(result, WxpayOrderInfo.class);
                Message message = myHandler.obtainMessage(11);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("wxpay: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void balancePay(String pwd) {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.CIRCLE_BALANCE_PAY);
        params.addBodyParameter("oid", String.valueOf(orderAddResult.getData().getOid()));
        params.addBodyParameter("password", pwd);
        params.addBodyParameter("type", balancePayType == 1 ? "wechat" : "ali");//ali,wechat
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("余额支付: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(13);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("余额支付: " + ex.toString());
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

    private ShareResult shareResult;

    private void shareConfig() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_CIRCLE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult = GsonUtils.GsonToBean(result, ShareResult.class);
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
