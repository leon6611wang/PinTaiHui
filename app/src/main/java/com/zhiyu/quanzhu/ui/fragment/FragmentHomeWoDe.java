package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.result.UserResult;
import com.zhiyu.quanzhu.ui.activity.MyProfileActivity;
import com.zhiyu.quanzhu.ui.activity.MyFansActivity;
import com.zhiyu.quanzhu.ui.activity.MyFollowActivity;
import com.zhiyu.quanzhu.ui.activity.MyOrderActivity;
import com.zhiyu.quanzhu.ui.activity.MyPublishListActivity;
import com.zhiyu.quanzhu.ui.activity.QianBaoActivity;
import com.zhiyu.quanzhu.ui.activity.QianDaoActivity;
import com.zhiyu.quanzhu.ui.activity.SystemSettingActivity;
import com.zhiyu.quanzhu.ui.activity.WoDeHuiYuanYongHuActivity;
import com.zhiyu.quanzhu.ui.activity.WoDeKaQuanActivity;
import com.zhiyu.quanzhu.ui.activity.ZuJiActivity;
import com.zhiyu.quanzhu.ui.dialog.WuLiuDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class FragmentHomeWoDe extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout fangkelayout, pinglunlayout, dianzanlayout, liulanjilulayout, shoucanglayout,
            daifukuanlayout, daifahuolayout, daishouhuolayout, daipingjialayout, tuihuanhuolayout,
            qianbaolayout, kaquanlayout, huiyuanlayout, qiandaolayout, dianpulayout, shangwulayout, kefulayout, gongnenglayout,
            fabulayout, fensilayout, guanzhulayout, huozanlayout, wuliulayout;
    private View fangkeYuanDian, pinglunYuanDian;
    private TextView quanbudingdanTextView, daifukuanTextView, daifahuoTextView, daishouhuoTextView, daipingjiaTextView, tuihuanhuoTextView,
            userNameTextView, pingxinzhiTextView, pingjifenTextView;
    private TextView feedCountTextView, fansCountTextView, followCountTextView, priseCountTextView;
    private CircleImageView headerImageView;
    private ImageView gouwucheImageView, shezhiImageView;
    private WuLiuDialog wuLiuDialog;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<FragmentHomeWoDe> fragmentHomeWoDeWeakReference;

        public MyHandler(FragmentHomeWoDe fragmentHomeWoDe) {
            fragmentHomeWoDeWeakReference = new WeakReference<>(fragmentHomeWoDe);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentHomeWoDe fragment = fragmentHomeWoDeWeakReference.get();
            switch (msg.what) {
                case 1:
                    Glide.with(fragment.getContext()).load(fragment.userResult.getData().getUser().getAvatar()).error(R.mipmap.no_avatar).into(fragment.headerImageView);
                    fragment.userNameTextView.setText(fragment.userResult.getData().getUser().getUsername());
                    fragment.pingxinzhiTextView.setText("苹信值 " + String.valueOf(fragment.userResult.getData().getUser().getScore()));
                    fragment.pingjifenTextView.setText("苹积分 " + String.valueOf(fragment.userResult.getData().getUser().getCredit()));
                    fragment.feedCountTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getFeeds_count()));
                    fragment.fansCountTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getFriends_count()));
                    fragment.followCountTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getFollow_count()));
                    fragment.priseCountTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getPrise_count()));
                    if (fragment.userResult.getData().getUser().getOrder_pay() > 0) {
                        fragment.daifukuanTextView.setVisibility(View.VISIBLE);
                        fragment.daifukuanTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getOrder_pay()));
                    } else {
                        fragment.daifukuanTextView.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().getOrder_send() > 0) {
                        fragment.daifahuoTextView.setVisibility(View.VISIBLE);
                        fragment.daifahuoTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getOrder_send()));
                    } else {
                        fragment.daifahuoTextView.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().getOrder_revice() > 0) {
                        fragment.daishouhuoTextView.setVisibility(View.VISIBLE);
                        fragment.daishouhuoTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getOrder_revice()));
                    } else {
                        fragment.daishouhuoTextView.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().getOrder_comment() > 0) {
                        fragment.daipingjiaTextView.setVisibility(View.VISIBLE);
                        fragment.daipingjiaTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getOrder_comment()));
                    } else {
                        fragment.daipingjiaTextView.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().getOrder_back() > 0) {
                        fragment.tuihuanhuoTextView.setVisibility(View.VISIBLE);
                        fragment.tuihuanhuoTextView.setText(String.valueOf(fragment.userResult.getData().getUser().getOrder_back()));
                    } else {
                        fragment.tuihuanhuoTextView.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().isComment_status()) {
                        fragment.pinglunYuanDian.setVisibility(View.VISIBLE);
                    } else {
                        fragment.pinglunYuanDian.setVisibility(View.GONE);
                    }
                    if (fragment.userResult.getData().getUser().isCards_status()) {
                        fragment.fangkeYuanDian.setVisibility(View.VISIBLE);
                    } else {
                        fragment.fangkeYuanDian.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_wode, container, false);
        initViews();
        initDialogs();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            userHome();
        }
    }

    private void initDialogs() {
        wuLiuDialog = new WuLiuDialog(getActivity(), R.style.dialog, new WuLiuDialog.OnChoosePhotoListener() {
            @Override
            public void xiangce() {

            }

            @Override
            public void paizhao() {

            }
        });
    }

    private void initViews() {
        headerImageView = view.findViewById(R.id.headerImageView);
        headerImageView.setOnClickListener(this);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        pingxinzhiTextView = view.findViewById(R.id.pingxinzhiTextView);
        pingjifenTextView = view.findViewById(R.id.pingjifenTextView);
        gouwucheImageView = view.findViewById(R.id.gouwucheImageView);
        feedCountTextView = view.findViewById(R.id.feedCountTextView);
        fansCountTextView = view.findViewById(R.id.fansCountTextView);
        followCountTextView = view.findViewById(R.id.followCountTextView);
        priseCountTextView = view.findViewById(R.id.priseCountTextView);

        gouwucheImageView.setOnClickListener(this);
        shezhiImageView = view.findViewById(R.id.shezhiImageView);
        shezhiImageView.setOnClickListener(this);
        fabulayout = view.findViewById(R.id.fabulayout);
        fabulayout.setOnClickListener(this);
        fensilayout = view.findViewById(R.id.fensilayout);
        fensilayout.setOnClickListener(this);
        guanzhulayout = view.findViewById(R.id.guanzhulayout);
        guanzhulayout.setOnClickListener(this);
        huozanlayout = view.findViewById(R.id.huozanlayout);
        huozanlayout.setOnClickListener(this);
        fangkelayout = view.findViewById(R.id.fangkelayout);
        fangkelayout.setOnClickListener(this);
        pinglunlayout = view.findViewById(R.id.pinglunlayout);
        pinglunlayout.setOnClickListener(this);
        dianzanlayout = view.findViewById(R.id.dianzanlayout);
        dianzanlayout.setOnClickListener(this);
        liulanjilulayout = view.findViewById(R.id.liulanjilulayout);
        liulanjilulayout.setOnClickListener(this);
        shoucanglayout = view.findViewById(R.id.shoucanglayout);
        shoucanglayout.setOnClickListener(this);
        fangkeYuanDian = view.findViewById(R.id.fangkeYuanDian);
        pinglunYuanDian = view.findViewById(R.id.pinglunYuanDian);
        quanbudingdanTextView = view.findViewById(R.id.quanbudingdanTextView);
        quanbudingdanTextView.setOnClickListener(this);
        daifukuanlayout = view.findViewById(R.id.daifukuanlayout);
        daifukuanlayout.setOnClickListener(this);
        daifahuolayout = view.findViewById(R.id.daifahuolayout);
        daifahuolayout.setOnClickListener(this);
        daishouhuolayout = view.findViewById(R.id.daishouhuolayout);
        daishouhuolayout.setOnClickListener(this);
        daipingjialayout = view.findViewById(R.id.daipingjialayout);
        daipingjialayout.setOnClickListener(this);
        tuihuanhuolayout = view.findViewById(R.id.tuihuanhuolayout);
        tuihuanhuolayout.setOnClickListener(this);
        daifukuanTextView = view.findViewById(R.id.daifukuanTextView);
        daifahuoTextView = view.findViewById(R.id.daifahuoTextView);
        daishouhuoTextView = view.findViewById(R.id.daishouhuoTextView);
        daipingjiaTextView = view.findViewById(R.id.daipingjiaTextView);
        tuihuanhuoTextView = view.findViewById(R.id.tuihuanhuoTextView);
        qianbaolayout = view.findViewById(R.id.qianbaolayout);
        qianbaolayout.setOnClickListener(this);
        kaquanlayout = view.findViewById(R.id.kaquanlayout);
        kaquanlayout.setOnClickListener(this);
        huiyuanlayout = view.findViewById(R.id.huiyuanlayout);
        huiyuanlayout.setOnClickListener(this);
        qiandaolayout = view.findViewById(R.id.qiandaolayout);
        qiandaolayout.setOnClickListener(this);
        dianpulayout = view.findViewById(R.id.dianpulayout);
        dianpulayout.setOnClickListener(this);
        shangwulayout = view.findViewById(R.id.shangwulayout);
        shangwulayout.setOnClickListener(this);
        kefulayout = view.findViewById(R.id.kefulayout);
        kefulayout.setOnClickListener(this);
        gongnenglayout = view.findViewById(R.id.gongnenglayout);
        gongnenglayout.setOnClickListener(this);
        wuliulayout = view.findViewById(R.id.wuliulayout);
        wuliulayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerImageView:
                Intent gerenxinxiIntent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(gerenxinxiIntent);
                break;
            case R.id.gouwucheImageView:

                break;
            case R.id.shezhiImageView:
                Intent settingIntent = new Intent(getActivity(), SystemSettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.fabulayout:
                Intent publishIntent = new Intent(getContext(), MyPublishListActivity.class);
                publishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(publishIntent);
                break;
            case R.id.fensilayout:
                Intent fensiIntent = new Intent(getContext(), MyFansActivity.class);
                fensiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(fensiIntent);
                break;
            case R.id.guanzhulayout:
                Intent followIntent=new Intent(getContext(), MyFollowActivity.class);
                followIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(followIntent);
                break;
            case R.id.huozanlayout:

                break;
            case R.id.quanbudingdanTextView:
                Intent orderIntent0 = new Intent(getActivity(), MyOrderActivity.class);
                orderIntent0.putExtra("position", 0);
                startActivity(orderIntent0);
                break;
            case R.id.fangkelayout:
                Intent zujiIntent0 = new Intent(getActivity(), ZuJiActivity.class);
                zujiIntent0.putExtra("position", 0);
                startActivity(zujiIntent0);
                break;
            case R.id.pinglunlayout:
                Intent zujiIntent1 = new Intent(getActivity(), ZuJiActivity.class);
                zujiIntent1.putExtra("position", 1);
                startActivity(zujiIntent1);
                break;
            case R.id.dianzanlayout:
                Intent zujiIntent2 = new Intent(getActivity(), ZuJiActivity.class);
                zujiIntent2.putExtra("position", 2);
                startActivity(zujiIntent2);
                break;
            case R.id.liulanjilulayout:
                Intent zujiIntent3 = new Intent(getActivity(), ZuJiActivity.class);
                zujiIntent3.putExtra("position", 3);
                startActivity(zujiIntent3);
                break;
            case R.id.shoucanglayout:

                break;
            case R.id.daifukuanlayout:
                Intent orderIntent1 = new Intent(getActivity(), MyOrderActivity.class);
                orderIntent1.putExtra("position", 1);
                startActivity(orderIntent1);
                break;
            case R.id.daifahuolayout:
                Intent orderIntent2 = new Intent(getActivity(), MyOrderActivity.class);
                orderIntent2.putExtra("position", 2);
                startActivity(orderIntent2);
                break;
            case R.id.daishouhuolayout:
                Intent orderIntent3 = new Intent(getActivity(), MyOrderActivity.class);
                orderIntent3.putExtra("position", 3);
                startActivity(orderIntent3);
                break;
            case R.id.daipingjialayout:
                Intent orderIntent4 = new Intent(getActivity(), MyOrderActivity.class);
                orderIntent4.putExtra("position", 4);
                startActivity(orderIntent4);
                break;
            case R.id.tuihuanhuolayout:

                break;
            case R.id.qianbaolayout:
                Intent qianbaoIntent = new Intent(getActivity(), QianBaoActivity.class);
                startActivity(qianbaoIntent);
                break;
            case R.id.kaquanlayout:
                Intent kaquanIntent = new Intent(getActivity(), WoDeKaQuanActivity.class);
                startActivity(kaquanIntent);
                break;
            case R.id.huiyuanlayout:
                Intent huiyuanIntent = new Intent(getActivity(), WoDeHuiYuanYongHuActivity.class);
                startActivity(huiyuanIntent);
                break;
            case R.id.qiandaolayout:
                Intent qiandaoIntent = new Intent(getActivity(), QianDaoActivity.class);
                startActivity(qiandaoIntent);
                break;
            case R.id.dianpulayout:

                break;
            case R.id.shangwulayout:

                break;
            case R.id.kefulayout:

                break;
            case R.id.gongnenglayout:

                break;
            case R.id.wuliulayout:
                wuLiuDialog.show();
                break;
        }
    }


    private UserResult userResult;

    /**
     * 首页个人中心
     */
    private void userHome() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_HOME);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("userHome: "+result);
                userResult = GsonUtils.GsonToBean(result, UserResult.class);
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
                System.out.println("--------> userHome: " + userResult.getData().getUser().getUsername());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("--------> userHome: " + ex.toString());
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
