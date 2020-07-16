package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.leon.chic.dao.CityDao;
import com.leon.chic.dao.MessageDao;
import com.leon.chic.utils.SPUtils;
import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.AreaProvince;
import com.zhiyu.quanzhu.model.bean.HobbyDaoParent;
import com.zhiyu.quanzhu.model.bean.IndustryParent;
import com.zhiyu.quanzhu.model.dao.AreaDao;
import com.zhiyu.quanzhu.model.dao.HobbyDao;
import com.zhiyu.quanzhu.model.dao.IndustryDao;
import com.zhiyu.quanzhu.model.result.AreaResult;
import com.zhiyu.quanzhu.model.result.HobbyDaoResult;
import com.zhiyu.quanzhu.model.result.IndustryResult;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends BaseActivity {
    private boolean guideShow, isLogin;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<InitActivity> activityWeakReference;

        public MyHandler(InitActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            InitActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.pageChange();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
//        SPUtils.getInstance().clearUserSP(BaseApplication.applicationContext);
//        SPUtils.getInstance().saveUserInfoStatus(BaseApplication.applicationContext, true, true, true, true);

//        MessageDao.getInstance().clearSystemMessage(BaseApplication.getInstance());
//        MessageDao.getInstance().clearServiceMessage(BaseApplication.getInstance());
//        CityDao.getInstance().clearCity(BaseApplication.getInstance());

        guideShow = SPUtils.getInstance().guideShow(BaseApplication.applicationContext);
//        guideShow = false;
        isLogin = SPUtils.getInstance().userIsLogin(BaseApplication.applicationContext);
        timer.schedule(task, 1000);
//        Intent intent = new Intent(this, CompleteUserProfileActivity.class);
//        startActivity(intent);

    }

    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = myHandler.obtainMessage(1);
            message.sendToTarget();
        }
    };


    private void pageChange() {
        if (!guideShow) {
            Intent guideIntent = new Intent(InitActivity.this, GuideActivity.class);
            startActivity(guideIntent);
        } else {
            if (!isLogin) {
                System.out.println("未登录");
                Intent loginIntent = new Intent(InitActivity.this, LoginGetVertifyCodeActivity.class);
                startActivity(loginIntent);
            } else {
                System.out.println("已登录");
                if (SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext) &&
                        SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext) &&
                        SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext) &&
                        SPUtils.getInstance().getUserHasPwd(BaseApplication.applicationContext)) {
                    System.out.println("所有初始信息已补全");
                    Intent homeIntent = new Intent(this, HomeActivity.class);
                    startActivity(homeIntent);
                } else {
                    if (StringUtils.isNullOrEmpty(SPUtils.getInstance().getUserPhoneNum(BaseApplication.applicationContext)) &&
                            !SPUtils.getInstance().getUserBindPhone(BaseApplication.applicationContext)) {
                        System.out.println("未绑定手机");
                        Intent intent = new Intent(this, BondPhoneNumberActivity.class);
                        startActivity(intent);
                    } else if (!SPUtils.getInstance().getUserHasPwd(BaseApplication.applicationContext)) {
                        System.out.println("未设置密码");
                        Intent intent = new Intent(this, SetLoginPwdActivity.class);
                        startActivity(intent);
                    } else if (!SPUtils.getInstance().getUserFillProfile(BaseApplication.applicationContext)) {
                        System.out.println("未完善信息");
                        Intent intent = new Intent(this, CompleteUserProfileActivity.class);
                        startActivity(intent);
                    } else if (!SPUtils.getInstance().getUserChooseInterest(BaseApplication.applicationContext)) {
                        System.out.println("未选偏好");
                        Intent intent = new Intent(this, HobbySelectActivity.class);
                        startActivity(intent);
                    }else{
                        Intent homeIntent = new Intent(this, HomeActivity.class);
                        startActivity(homeIntent);
                    }
                }
            }
        }
        finish();
    }


}
