package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.result.ConversationCircleResult;
import com.zhiyu.quanzhu.ui.dialog.CircleConversationRightMenuDialog;
import com.zhiyu.quanzhu.ui.dialog.IMRightMenuDialog2;
import com.zhiyu.quanzhu.ui.widget.Indicator;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideImageLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * 聊天界面
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private View headerView;
    private LinearLayout rootLayout, privateBackLayout, privateRightLayout, groupBackLayout, groupRightLayout;
    private TextView titleTextView, noticeTextView;
    private Uri uri;
    private String targetId, title;
    private ConversationFragment fragement;
    private LinearLayout menuLayout;
    private IMRightMenuDialog2 menuDialog;
    private CircleConversationRightMenuDialog rightMenuDialog;
    private String conversation_type;
    private float ratio = 0.5333f;
    private int screenWidth, dialogHeight;
    private FrameLayout.LayoutParams fl;
    private int bannerHeight = 0;
    private Banner banner;
    private Indicator indicator;
    private List<String> imageUrl = new ArrayList<>();
    private boolean isBlackStatus;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<ConversationActivity> activityWeakReference;

        public MyHandler(ConversationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ConversationActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    if (200 == activity.conversationCircleResult.getCode()) {
                        activity.noticeTextView.setText(activity.conversationCircleResult.getData().getNotice());
                        activity.initBannerDatas();
                        activity.startBanner();
                    }

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        uri = getIntent().getData();
        conversation_type = SharedPreferencesUtils.getInstance(this).getConversationType();
        targetId = uri.getQueryParameter("targetId").toString();
        title = uri.getQueryParameter("title").toString();
//        System.out.println("ConversationActivity targetId: " + targetId);
        initHeaderViews();


        initDialogs();
    }

    private void initHeaderViews() {
        rootLayout = findViewById(R.id.rootLayout);
        if (conversation_type.equals(SharedPreferencesUtils.IM_GROUP)) {
            isBlackStatus = false;
            headerView = LayoutInflater.from(this).inflate(R.layout.header_conversation_group, null);
            headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dialogHeight = headerView.getHeight();
                }
            });
            rootLayout.addView(headerView, 0);
            groupBackLayout = headerView.findViewById(R.id.backLayout);
            groupBackLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            groupRightLayout = headerView.findViewById(R.id.rightLayout);
            groupRightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConversationActivity.this, CircleInfoActivity.class);
                    intent.putExtra("circle_id", Long.parseLong(targetId));
                    startActivity(intent);
                }
            });
            noticeTextView = headerView.findViewById(R.id.noticeTextView);
            noticeTextView.setSelected(true);
            banner = headerView.findViewById(R.id.banner);
            indicator = headerView.findViewById(R.id.indicator);
            screenWidth = ScreentUtils.getInstance().getScreenWidth(this);
            bannerHeight = (int) (ratio * screenWidth);
            fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, bannerHeight);
            banner.setLayoutParams(fl);


        } else if (conversation_type.equals(SharedPreferencesUtils.IM_PRIVATE)) {
            isBlackStatus = true;
            headerView = LayoutInflater.from(this).inflate(R.layout.header_conversation_private, null);
            rootLayout.addView(headerView, 0);
            privateBackLayout = headerView.findViewById(R.id.backLayout);
            privateBackLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            privateRightLayout = headerView.findViewById(R.id.rightLayout);
            privateRightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent privateSettingIntent = new Intent(ConversationActivity.this, ConversationPrivateSettingActivity.class);
                    privateSettingIntent.putExtra("targetId", targetId);
                    startActivityForResult(privateSettingIntent, 1003);
                }
            });
            titleTextView = headerView.findViewById(R.id.titleTextView);
            if (!TextUtils.isEmpty(title)) {
                titleTextView.setText(title);
            }
        }
        ScreentUtils.getInstance().setStatusBarLightMode(this, isBlackStatus);
        initViews();
    }

    private void initBannerDatas() {
        if (null != conversationCircleResult && null != conversationCircleResult.getData()
                && null != conversationCircleResult.getData().getImgs() &&
                conversationCircleResult.getData().getImgs().size() > 0) {
            for (String imgUrl : conversationCircleResult.getData().getImgs()) {
                imageUrl.add(imgUrl);
            }
        }
    }


    private void startBanner() {
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

    private void initDialogs() {
        menuDialog = new IMRightMenuDialog2(this, R.style.dialog);
        rightMenuDialog = new CircleConversationRightMenuDialog();
    }

    private void initViews() {
        menuLayout = findViewById(R.id.menuLayout);
        if (isBlackStatus) {
            menuLayout.setVisibility(View.GONE);
        } else {
            menuLayout.setVisibility(View.VISIBLE);
            circleInfo();
        }
        menuLayout.setOnClickListener(this);
        FragmentManager fragmentManage = getSupportFragmentManager();
        fragement = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(SharedPreferencesUtils.getInstance(this).getConversationType())
                .appendQueryParameter("targetId", targetId).build();
        fragement.setUri(uri);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLayout:
//                menuDialog.show();
//                menuDialog.setDialogHeight(dialogHeight);
                int height = ScreentUtils.getInstance().getScreenHeight(this) - dialogHeight - (int) getResources().getDimension(R.dimen.dp_10);
                Bundle bundle = new Bundle();
                bundle.putInt("height", height);

                if (null != conversationCircleResult && null != conversationCircleResult.getData() &&
                        null != conversationCircleResult.getData().getShops() &&
                        conversationCircleResult.getData().getShops().size() > 0) {
                    System.out.println("圈子的商店数量: "+conversationCircleResult.getData().getShops().size());
                    bundle.putString("shop_list", GsonUtils.GsonString(conversationCircleResult.getData().getShops()));
                }
                rightMenuDialog.setArguments(bundle);
                rightMenuDialog.show(getSupportFragmentManager(), "");
                break;
        }
    }

    private ConversationCircleResult conversationCircleResult;

    private void circleInfo() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MESSAGE_CIRCLE_INFO);
        params.addBodyParameter("circle_id", String.valueOf(targetId));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("圈子基础信息: " + result);
                conversationCircleResult = GsonUtils.GsonToBean(result, ConversationCircleResult.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1003) {
            if (null != data) {
                if (data.hasExtra("isdelete")) {
                    finish();
                }
            }
        }
    }
}
