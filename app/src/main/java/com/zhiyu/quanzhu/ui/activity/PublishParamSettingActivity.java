package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.bean.IndustryChild;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.IndustryDialog;
import com.zhiyu.quanzhu.ui.widget.SwitchButton;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 发布文章/视频参数设置
 */
public class PublishParamSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTextView, circleTitleTextView, circleTextView, industryTextView, hobbyTextView, goodsTextView;
    private LinearLayout atCircleRootLayout, topCircleFeedRootLayout, onlyPublishCircleRootLayout, circleLayout, industryLayout, hobbyLayout, goodsLayout, backLayout;
    private SwitchButton personalFeedSwitchButton, circleFeedSwitchButton, circleSwitchButton;
    private CircleSelectDialog circleSelectDialog;
    private IndustryDialog industryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_param_setting);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
        initDialogs();
    }

    private void initDialogs(){
        circleSelectDialog=new CircleSelectDialog(this, R.style.dialog, new CircleSelectDialog.OnCircleSeletedListener() {
            @Override
            public void onCircleSelected(MyCircle circle) {

            }
        });
        industryDialog=new IndustryDialog(this, R.style.dialog, new IndustryDialog.OnHangYeChooseListener() {
            @Override
            public void onHangYeChoose(IndustryParent parent, IndustryChild child) {

            }
        });
    }

    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("发布设置");
        atCircleRootLayout = findViewById(R.id.atCircleRootLayout);
        topCircleFeedRootLayout = findViewById(R.id.topCircleFeedRootLayout);
        onlyPublishCircleRootLayout = findViewById(R.id.onlyPublishCircleRootLayout);
        circleLayout = findViewById(R.id.circleLayout);
        circleLayout.setOnClickListener(this);
        industryLayout = findViewById(R.id.industryLayout);
        industryLayout.setOnClickListener(this);
        hobbyLayout = findViewById(R.id.hobbyLayout);
        hobbyLayout.setOnClickListener(this);
        goodsLayout = findViewById(R.id.goodsLayout);
        goodsLayout.setOnClickListener(this);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        circleTitleTextView = findViewById(R.id.circleTitleTextView);
        circleTitleTextView.setText("@圈子");
        circleTextView = findViewById(R.id.circleTextView);
        industryTextView = findViewById(R.id.industryTextView);
        hobbyTextView = findViewById(R.id.hobbyTextView);
        goodsTextView = findViewById(R.id.goodsTextView);
        personalFeedSwitchButton = findViewById(R.id.personalFeedSwitchButton);
        circleFeedSwitchButton = findViewById(R.id.circleFeedSwitchButton);
        circleSwitchButton = findViewById(R.id.circleSwitchButton);
        personalFeedSwitchButton.setOnSwitchButtonListener(new SwitchButton.OnSwitchButtonListener() {
            @Override
            public void onOpen(boolean isOpen) {
                System.out.println("isOpen: " + isOpen);
            }
        });
        personalFeedSwitchButton.open();
        showCircleParams(true);
    }

    private void showCircleParams(boolean isShow) {
        if (isShow) {
            atCircleRootLayout.setVisibility(View.VISIBLE);
            topCircleFeedRootLayout.setVisibility(View.VISIBLE);
            onlyPublishCircleRootLayout.setVisibility(View.VISIBLE);
        } else {
            atCircleRootLayout.setVisibility(View.GONE);
            topCircleFeedRootLayout.setVisibility(View.GONE);
            onlyPublishCircleRootLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.circleLayout:
                circleSelectDialog.show();
                break;
            case R.id.industryLayout:
                industryDialog.show();
                break;
            case R.id.hobbyLayout:
                industryDialog.show();
                break;
            case R.id.goodsLayout:
                Intent goodsIntent = new Intent(PublishParamSettingActivity.this, PublishChooseGoodsActivity.class);
                startActivity(goodsIntent);
                break;
        }
    }
}
