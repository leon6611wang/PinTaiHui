package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.model.result.MemberCenterResult;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.ui.fragment.FragmentPointGoods;
import com.zhiyu.quanzhu.ui.fragment.FragmentPointRecord;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员中心
 */
public class MemberCenterActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout, rightLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TextView jifenduihaoliTextView, jifenjiluTextView, checkInTextView;
    private CircleImageView avatarImageView;
    private TextView nameTextView,pointTextView,growUpTextView,growLevelTextView;
    private MyHandler myHandler=new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<MemberCenterActivity> activityWeakReference;
        public MyHandler(MemberCenterActivity activity){
            activityWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MemberCenterActivity activity=activityWeakReference.get();
            switch (msg.what){
                case 1:
                    if(activity.memberCenterResult.getCode()==200){
                        Glide.with(activity).load(activity.memberCenterResult.getData().getAvatar()).error(R.drawable.image_error).into(activity.avatarImageView);
                        activity.nameTextView.setText(activity.memberCenterResult.getData().getUsername());
                        activity.pointTextView.setText(activity.memberCenterResult.getData().getCredits()+"积分");
                        activity.growUpTextView.setText(activity.memberCenterResult.getData().getGrow_up());
                        activity.growLevelTextView.setText(activity.memberCenterResult.getData().getGrow_level());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        memberCenter();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        rightLayout = findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        checkInTextView = findViewById(R.id.checkInTextView);
        checkInTextView.setOnClickListener(this);
        avatarImageView=findViewById(R.id.avatarImageView);
        nameTextView=findViewById(R.id.nameTextView);
        pointTextView=findViewById(R.id.pointTextView);
        growUpTextView=findViewById(R.id.growUpTextView);
        growLevelTextView=findViewById(R.id.growLevelTextView);


        mViewPager = findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentPointGoods());
        fragmentList.add(new FragmentPointRecord());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        jifenduihaoliTextView = findViewById(R.id.jifenduihaoliTextView);
        jifenduihaoliTextView.setOnClickListener(this);
        jifenjiluTextView = findViewById(R.id.jifenjiluTextView);
        jifenjiluTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent jiluIntent = new Intent(this, PointExchangeRecordActivity.class);
                startActivity(jiluIntent);
                break;
            case R.id.jifenduihaoliTextView:
                barChange(0);
                break;
            case R.id.jifenjiluTextView:
                barChange(1);
                break;
            case R.id.checkInTextView:
                Intent checkInIntent = new Intent(this, CheckInActivity.class);
                startActivity(checkInIntent);
                break;
        }
    }

    private void barChange(int position) {
        jifenduihaoliTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_gray_bg));
        jifenjiluTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_gray_bg));
        jifenduihaoliTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        jifenjiluTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        switch (position) {
            case 0:
                jifenduihaoliTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_grown_bg));
                jifenduihaoliTextView.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                jifenjiluTextView.setBackground(getResources().getDrawable(R.drawable.shape_huiyuan_yonghu_bar_grown_bg));
                jifenjiluTextView.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    private MemberCenterResult memberCenterResult;
    private void memberCenter() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MEMBER_CENTER);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("会员中心: " + result);
                memberCenterResult= GsonUtils.GsonToBean(result,MemberCenterResult.class);
                Message message=myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("会员中心: "+ ex.toString());
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
