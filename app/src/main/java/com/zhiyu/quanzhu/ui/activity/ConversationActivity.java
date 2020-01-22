package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.dialog.IMRightMenuDialog;
import com.zhiyu.quanzhu.ui.widget.Indicator;
import com.zhiyu.quanzhu.utils.GlideImageLoader;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * 聊天界面
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private View headerView;
    private LinearLayout rootLayout, privateBackLayout, privateRightLayout, groupBackLayout, groupRightLayout;
    private TextView titleTextView,gonggaoTextView;
    private Uri uri;
    private String targetId, title;
    private ConversationFragment fragement;
    private LinearLayout menuLayout;
    private IMRightMenuDialog menuDialog;
    private String conversation_type;
    private float ratio = 0.5333f;
    private int screenWidth,dialogHeight;
    private FrameLayout.LayoutParams fl;
    private int bannerHeight=0;
    private Banner banner;
    private Indicator indicator;
    private List<String> imageUrl = new ArrayList<>();
    private boolean isBlackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        uri = getIntent().getData();
        conversation_type = SharedPreferencesUtils.getInstance(this).getConversationType();
        targetId = uri.getQueryParameter("targetId").toString();
        title = uri.getQueryParameter("title").toString();
        System.out.println("ConversationActivity targetId: "+targetId);
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
                    dialogHeight=headerView.getHeight();
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

                }
            });
            gonggaoTextView=headerView.findViewById(R.id.gonggaoTextView);
            gonggaoTextView.setSelected(true);
            banner = headerView.findViewById(R.id.banner);
            indicator = headerView.findViewById(R.id.indicator);
            screenWidth=ScreentUtils.getInstance().getScreenWidth(this);
            bannerHeight=(int)(ratio*screenWidth);
            fl=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,bannerHeight);
            banner.setLayoutParams(fl);

            initDatas();
            startBanner();
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
                    startActivity(privateSettingIntent);
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
        menuDialog = new IMRightMenuDialog(this, R.style.dialog);
    }

    private void initViews() {
        menuLayout = findViewById(R.id.menuLayout);
        if (isBlackStatus) {
            menuLayout.setVisibility(View.GONE);
        } else {
            menuLayout.setVisibility(View.VISIBLE);
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
                menuDialog.show();
                menuDialog.setDialogHeight(dialogHeight);
                break;
        }
    }

}
