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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.myvideoplaerlibrary.view.VideoPlayerTrackView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CircleInfoFeed;
import com.zhiyu.quanzhu.model.result.CircleInfoFeedResult;
import com.zhiyu.quanzhu.model.result.CircleInfoResult;
import com.zhiyu.quanzhu.model.result.CircleInfoUserResult;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoListAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoMemberListAdapter;
import com.zhiyu.quanzhu.ui.adapter.CircleInfoShopListAdapter;
import com.zhiyu.quanzhu.ui.dialog.CircleInfoEditDialog;
import com.zhiyu.quanzhu.ui.dialog.CircleInfoJoinPayDialog;
import com.zhiyu.quanzhu.ui.dialog.ReviewFullTextDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoCirclerWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoJoinedWindow;
import com.zhiyu.quanzhu.ui.popupwindow.CircleInfoUnjoinWindow;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.HorizontalListView;
import com.zhiyu.quanzhu.ui.widget.MyListView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideImageLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

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
public class CircleInfoActivity extends BaseActivity implements AbsListView.OnScrollListener, View.OnClickListener {
    private Banner banner;
    private List<String> imageUrl = new ArrayList<>();
    private PtrFrameLayout ptrFrameLayout;
    private ListView mListView;
    private CircleInfoListAdapter adapter;
    private View headerView;
    private View topMenuView1, topMenuView2;
    private HorizontalListView memberListView;
    private CircleInfoMemberListAdapter memberListAdapter;
    private MyListView shopListView;
    private CircleInfoShopListAdapter shopListAdapter;
    private ReviewFullTextDialog reviewFullTextDialog;
    private TextView introduceTextView, noticeTextView, titleTextView2, joinCircleTextView;
    private LinearLayout rightLayout1, rightLayout2, backLayout1, backLayout2;
    private boolean isCircler = true;//是否圈主
    private boolean isJoined = true;//是否加入
    private CircleInfoEditDialog editDialog;
    private YNDialog ynDialog;
    private ShareDialog shareDialog;
    private CircleInfoJoinPayDialog circleInfoJoinPayDialog;
    private long circle_id;
    private CircleImageView avatarImageView;
    private TextView nameTextView, cityTextView, industryTextViwe, pnumTextView, createDaysTextView, pCountTextView;
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
                    activity.imageUrl = activity.circleInfoResult.getData().getImgs();
                    activity.initBanner();
                    Glide.with(activity).load(activity.circleInfoResult.getData().getAvatar())
                            .error(R.mipmap.no_avatar)
                            .into(activity.avatarImageView);
                    activity.nameTextView.setText(activity.circleInfoResult.getData().getName());
                    activity.cityTextView.setText(activity.circleInfoResult.getData().getProvince() + "/" + activity.circleInfoResult.getData().getCity_name());
                    activity.industryTextViwe.setText(activity.circleInfoResult.getData().getTwo_industry() + "/" + activity.circleInfoResult.getData().getThree_industry());
                    activity.noticeTextView.setText(activity.circleInfoResult.getData().getNotice());
                    activity.introduceTextView.setText(activity.circleInfoResult.getData().getDescirption());
                    activity.videoPlayer.setVisibility(com.qiniu.android.utils.StringUtils.isNullOrEmpty(activity.circleInfoResult.getData().getVideo()) ? View.GONE : View.VISIBLE);
                    activity.videoPlayer.setDataSource(activity.circleInfoResult.getData().getVideo(), "");
                    activity.pnumTextView.setText(activity.circleInfoResult.getData().getPnum() + "人");
                    activity.createDaysTextView.setText("已创建 " + activity.circleInfoResult.getData().getDays() + " 天");
                    activity.pCountTextView.setText(activity.circleInfoResult.getData().getPnum() + "人加入");
//                    activity.shopListAdapter.setList(activity.circleInfoResult.getData().getStores());
                    activity.titleTextView2.setText(activity.circleInfoResult.getData().getName());
                    if (!activity.circleInfoResult.getData().isIs_join()) {
                        activity.joinCircleTextView.setVisibility(View.VISIBLE);
                    } else {
                        activity.joinCircleTextView.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.feedList);
                    break;
                case 3:
                    Toast.makeText(activity, activity.baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                    if (activity.baseResult.getCode() == 200) {
                        activity.circleBase();
                    }
                    break;
                case 4:
                    activity.memberListAdapter.setList(activity.userResult.getData().getList());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_info);
        circle_id = getIntent().getLongExtra("circle_id", 0l);
        circle_id = 16;
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initPtr();
        initViews();
        circleBase();
        userList();
        shopList();
        feedList();
    }

    private void initDatas() {
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201711/23/20171123005542_4ZvXh.jpeg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201901/25/20190125133246_reiqi.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201805/31/20180531100518_yrtgi.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201808/19/20180819144607_bhmtu.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201708/10/20170810190105_hx23Z.jpeg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201906/16/20190616121851_rbhei.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201904/30/20190430205649_vbbtf.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201901/05/20190105152941_qjlkm.jpg");
        imageUrl.add("https://c-ssl.duitang.com/uploads/item/201206/09/20120609152914_F2RAR.jpeg");
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
        adapter = new CircleInfoListAdapter(this, this);
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
                Intent memberManageIntent = new Intent(CircleInfoActivity.this, CircleMemberManageActivity.class);
                memberManageIntent.putExtra("circle_id", circle_id);
                memberManageIntent.putExtra("own", own);
                startActivity(memberManageIntent);
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
//        initDatas();
//        initBanner();
        initDialogs();
    }

    private void initDialogs() {
        reviewFullTextDialog = new ReviewFullTextDialog(this, R.style.dialog);
        editDialog = new CircleInfoEditDialog(this, R.style.dialog, new CircleInfoEditDialog.OnEditListener() {
            @Override
            public void onEdit(int editType, String content) {
                if (editType == 1 || editType == 2) {
                    updateCircleProfile(editType, content);
                } else if (editType == 3) {
                    if (circleInfoResult.getData().getIs_price() == 1) {
                        circleInfoJoinPayDialog.show();
                        circleInfoJoinPayDialog.setPrice(String.valueOf(circleInfoResult.getData().getPrice()));
                    }
                }

            }
        });
        ynDialog = new YNDialog(this, R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {

            }
        });
        shareDialog = new ShareDialog(this, this, R.style.dialog);

        circleInfoJoinPayDialog = new CircleInfoJoinPayDialog(this, R.style.dialog, new CircleInfoJoinPayDialog.OnPayListener() {
            @Override
            public void onPay() {

            }
        });
    }


    private void initBanner() {
//        indicator.addParams(imageUrl.size(), 0, 0);
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
//                indicator.setCurrent(position - 1);
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
                Intent memberManageIntent = new Intent(CircleInfoActivity.this, CircleMemberManageActivity.class);
                memberManageIntent.putExtra("circle_id", circle_id);
                memberManageIntent.putExtra("own", own);
                startActivity(memberManageIntent);
                break;
            case R.id.noticeTextView:
                reviewFullTextDialog.show();
                reviewFullTextDialog.setData("圈公告", "黄飞鸿不好听没硬胶囊剂热回复，后退货太弱鸡推蒙混过关官方后，台他好核桃仁突然投入突然柔荑花投入和人热人员而已二人太热塔尔，轻微的无无任务区人防千万人我确认我。黄飞鸿不好听没硬胶囊剂热回复，后退货太弱鸡推蒙混过关官方后，台他好核桃仁突然投入突然柔荑花投入和人热人员而已二人太热塔尔，轻微的无无任务区人防千万人我确认我。黄飞鸿不好听没硬胶囊剂热回复，后退货太弱鸡推蒙混过关官方后，台他好核桃仁突然投入突然柔荑花投入和人热人员而已二人太热塔尔，轻微的无无任务区人防千万人我确认我。");
                break;
            case R.id.introduceTextView:
                reviewFullTextDialog.show();
                reviewFullTextDialog.setData("圈子介绍", "圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理，并且具有唯一性，并且圈主在APP内会 有很多变现形式，可能您在放松时间在商圈内聊聊天，就能 轻松赚钱。 圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理。圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理，并且具有唯一性，并且圈主在APP内会 有很多变现形式，可能您在放松时间在商圈内聊聊天，就能 轻松赚钱。 圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理。圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理，并且具有唯一性，并且圈主在APP内会 有很多变现形式，可能您在放松时间在商圈内聊聊天，就能 轻松赚钱。 圈主是基于APP上的一个重要组成部分，通过圈主可以吸引 粉丝入圈，圈内举办线上线下活动，商圈建立后圈主可以自 行建群并实行管理。");
                break;
            case R.id.rightLayout1:
                if (isCircler) {
                    new CircleInfoCirclerWindow(this, new CircleInfoCirclerWindow.OnMenuSelectListener() {
                        @Override
                        public void onMenuSelect(int index, String menu) {
                            switch (index) {
                                case 1:
                                    shareDialog.show();
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
                                    startActivity(settingIntent);
                                    break;
                            }
                            System.out.println(menu);
                        }
                    }).showAtBottom(rightLayout1);
                } else {
                    if (isJoined) {
                        new CircleInfoJoinedWindow(this, new CircleInfoJoinedWindow.OnMenuSelectListener() {
                            @Override
                            public void onMenuSelect(int index, String menu) {
                                switch (index) {
                                    case 1:
                                        shareDialog.show();
                                        break;
                                    case 2:

                                        break;
                                    case 3:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
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
                        new CircleInfoUnjoinWindow(this).showAtBottom(rightLayout1);
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
                                    shareDialog.show();
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
                                        shareDialog.show();
                                        break;
                                    case 2:

                                        break;
                                    case 3:
                                        Intent complaintIntent = new Intent(CircleInfoActivity.this, ComplaintActivity.class);
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
                        new CircleInfoUnjoinWindow(this).showAtBottom(rightLayout1);
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
                own = circleInfoResult.getData().getOwn();
                System.out.println(" --- > own: " + own);
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

    private CircleInfoFeedResult circleInfoFeedResult;
    private int page = 1;
    private List<CircleInfoFeed> feedList = new ArrayList<>();

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
                circleInfoFeedResult = GsonUtils.GsonToBean(result, CircleInfoFeedResult.class);
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
                params.addBodyParameter("describetion", content);
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
