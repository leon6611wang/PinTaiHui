package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.activity.GeRenXinXiActivity;
import com.zhiyu.quanzhu.ui.activity.MyOrderActivity;
import com.zhiyu.quanzhu.ui.activity.QianBaoActivity;
import com.zhiyu.quanzhu.ui.activity.QianDaoActivity;
import com.zhiyu.quanzhu.ui.activity.SystemSettingActivity;
import com.zhiyu.quanzhu.ui.activity.WoDeHuiYuanYongHuActivity;
import com.zhiyu.quanzhu.ui.activity.WoDeKaQuanActivity;
import com.zhiyu.quanzhu.ui.activity.ZuJiActivity;
import com.zhiyu.quanzhu.ui.dialog.WuLiuDialog;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;

public class FragmentHomeWoDe extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout fangkelayout, pinglunlayout, dianzanlayout, liulanjilulayout, shoucanglayout,
            daifukuanlayout, daifahuolayout, daishouhuolayout, daipingjialayout, tuihuanhuolayout,
            qianbaolayout, kaquanlayout, huiyuanlayout, qiandaolayout, dianpulayout, shangwulayout, kefulayout, gongnenglayout,
            fabulayout, fensilayout, guanzhulayout, huozanlayout, wuliulayout;
    private View fangkeYuanDian, pinglunYuanDian;
    private TextView quanbudingdanTextView, daifukuanTextView, daifahuoTextView, daishouhuoTextView, daipingjiaTextView, tuihuanhuoTextView,
            userNameTextView, pingxinzhiTextView, pingjifenTextView;
    private CircleImageView headerImageView;
    private ImageView gouwucheImageView, shezhiImageView;
    private WuLiuDialog wuLiuDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_wode, container, false);
        initViews();
        initDialogs();
        return view;
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
                Intent gerenxinxiIntent=new Intent(getActivity(), GeRenXinXiActivity.class);
                startActivity(gerenxinxiIntent);
                break;
            case R.id.gouwucheImageView:

                break;
            case R.id.shezhiImageView:
                Intent settingIntent = new Intent(getActivity(), SystemSettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.fabulayout:

                break;
            case R.id.fensilayout:

                break;
            case R.id.guanzhulayout:

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
                Intent qianbaoIntent=new Intent(getActivity(), QianBaoActivity.class);
                startActivity(qianbaoIntent);
                break;
            case R.id.kaquanlayout:
                Intent kaquanIntent=new Intent(getActivity(), WoDeKaQuanActivity.class);
                startActivity(kaquanIntent);
                break;
            case R.id.huiyuanlayout:
                Intent huiyuanIntent=new Intent(getActivity(), WoDeHuiYuanYongHuActivity.class);
                startActivity(huiyuanIntent);
                break;
            case R.id.qiandaolayout:
                Intent qiandaoIntent=new Intent(getActivity(), QianDaoActivity.class);
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
}
