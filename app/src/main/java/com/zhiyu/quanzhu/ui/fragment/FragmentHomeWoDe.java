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
import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.MyCircle;
import com.zhiyu.quanzhu.model.bean.OrderDelivery;
import com.zhiyu.quanzhu.model.result.OrderDeliveryResult;
import com.zhiyu.quanzhu.model.result.UserResult;
import com.zhiyu.quanzhu.ui.activity.BuyVIPActivity;
import com.zhiyu.quanzhu.ui.activity.CartActivity;
import com.zhiyu.quanzhu.ui.activity.ContactCustomerServiceActivity;
import com.zhiyu.quanzhu.ui.activity.CreateCircleActivity;
import com.zhiyu.quanzhu.ui.activity.CreateShopActivity;
import com.zhiyu.quanzhu.ui.activity.MyCollectionActivity;
import com.zhiyu.quanzhu.ui.activity.MyProfileActivity;
import com.zhiyu.quanzhu.ui.activity.MyFansActivity;
import com.zhiyu.quanzhu.ui.activity.MyFollowActivity;
import com.zhiyu.quanzhu.ui.activity.MyOrderActivity;
import com.zhiyu.quanzhu.ui.activity.MyPublishListActivity;
import com.zhiyu.quanzhu.ui.activity.MyPurseActivity;
import com.zhiyu.quanzhu.ui.activity.CheckInActivity;
import com.zhiyu.quanzhu.ui.activity.SystemSettingActivity;
import com.zhiyu.quanzhu.ui.activity.MemberCenterActivity;
import com.zhiyu.quanzhu.ui.activity.MyCouponActivity;
import com.zhiyu.quanzhu.ui.activity.MyFootprintActivity;
import com.zhiyu.quanzhu.ui.activity.UserVertifyActivity;
import com.zhiyu.quanzhu.ui.dialog.CircleSelectDialog;
import com.zhiyu.quanzhu.ui.dialog.DeliveryInfoDialog;
import com.zhiyu.quanzhu.ui.dialog.RegTokenDialog;
import com.zhiyu.quanzhu.ui.dialog.YNDialog;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.CircleImageView;
import com.zhiyu.quanzhu.ui.widget.NiceImageView;
import com.zhiyu.quanzhu.ui.widget.VerticalMarqueeLayout;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ThreadPoolUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FragmentHomeWoDe extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout fangkelayout, pinglunlayout, dianzanlayout, liulanjilulayout, shoucanglayout,
            daifukuanlayout, daifahuolayout, daishouhuolayout, daipingjialayout, tuihuanhuolayout,
            qianbaolayout, kaquanlayout, huiyuanlayout, qiandaolayout, dianpulayout, shangwulayout, kefulayout, gongnenglayout,
            fabulayout, fensilayout, guanzhulayout, huozanlayout, wuliulayout, buyVipButton;
    private View fangkeYuanDian, pinglunYuanDian;
    private TextView quanbudingdanTextView, daifukuanTextView, daifahuoTextView, daishouhuoTextView, daipingjiaTextView, tuihuanhuoTextView,
            userNameTextView, pingxinzhiTextView, pingjifenTextView;
    private TextView feedCountTextView, fansCountTextView, followCountTextView, priseCountTextView, deliveryTimeTextView, regTokenTextView;
    private CircleImageView headerImageView;
    private ImageView gouwucheImageView, shezhiImageView;
    private VerticalMarqueeLayout deliveryLayout;
    private DeliveryInfoDialog deliveryInfoDialog;
    private CircleSelectDialog circleSelectDialog;
    private YNDialog ynDialog;
    private RegTokenDialog regTokenDialog;
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
                    if (null != fragment.userResult && null != fragment.userResult.getData() && null != fragment.userResult.getData().getUser()) {
                        if (null != fragment.getContext())
                            Glide.with(fragment.getContext()).load(fragment.userResult.getData().getUser().getAvatar()).error(R.mipmap.no_avatar).into(fragment.headerImageView);
                        fragment.userNameTextView.setText(fragment.userResult.getData().getUser().getUsername());
//                        fragment.pingxinzhiTextView.setText("苹信值 " + String.valueOf(fragment.userResult.getData().getUser().getScore()));
                        fragment.pingjifenTextView.setText("圈积分 " + String.valueOf(fragment.userResult.getData().getUser().getCredit()));
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
                    }

                    break;
                case 2:
                    if (null != fragment.deliveryResult && fragment.deliveryResult.getCode() == 200 &&
                            null != fragment.deliveryResult.getData() && null != fragment.deliveryResult.getData().getList() &&
                            fragment.deliveryResult.getData().getList().size() > 0)
                        fragment.initDelivery();
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
            ThreadPoolUtils.getInstance().init().execute(new Runnable() {
                @Override
                public void run() {
                    userHome();
                    deliveryList();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                userHome();
            }
        });
    }

    private void initDialogs() {
        deliveryInfoDialog = new DeliveryInfoDialog(getActivity(), R.style.dialog);

        circleSelectDialog = new CircleSelectDialog(getContext(), R.style.dialog, 1, new CircleSelectDialog.OnCircleSeletedListener() {
            @Override
            public void onCircleSelected(MyCircle circle) {
                if (null != circle) {
                    Intent createShopIntent = new Intent(getActivity(), CreateShopActivity.class);
                    createShopIntent.putExtra("circle_id", circle.getId());
                    startActivity(createShopIntent);
                }

            }
        });
        regTokenDialog = new RegTokenDialog(getContext(), R.style.dialog);
        ynDialog = new YNDialog(getContext(), R.style.dialog, new YNDialog.OnYNListener() {
            @Override
            public void onConfirm() {
                if (!userResult.getData().getUser().isIs_rz()) {
                    Intent intent=new Intent(getContext(), UserVertifyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                } else if (!userResult.getData().getUser().isHas_circle()) {
                    Intent intent=new Intent(getContext(), CreateCircleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });
    }

    private void initViews() {
        headerImageView = view.findViewById(R.id.headerImageView);
        headerImageView.setOnClickListener(this);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        pingxinzhiTextView = view.findViewById(R.id.pingxinzhiTextView);
        pingjifenTextView = view.findViewById(R.id.pingjifenTextView);
        regTokenTextView = view.findViewById(R.id.regTokenTextView);
        regTokenTextView.setOnClickListener(this);
        gouwucheImageView = view.findViewById(R.id.gouwucheImageView);
        feedCountTextView = view.findViewById(R.id.feedCountTextView);
        fansCountTextView = view.findViewById(R.id.fansCountTextView);
        followCountTextView = view.findViewById(R.id.followCountTextView);
        priseCountTextView = view.findViewById(R.id.priseCountTextView);
        buyVipButton = view.findViewById(R.id.buyVipButton);
        buyVipButton.setOnClickListener(this);

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
        deliveryLayout = view.findViewById(R.id.deliveryLayout);
        deliveryTimeTextView = view.findViewById(R.id.deliveryTimeTextView);
    }

    private void initDelivery() {
        int count = deliveryList.size();
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            views.add(inflateView(inflater, deliveryLayout, i));
        }
        if (deliveryLayout.getChildCount() > 0) {
            deliveryLayout.removeAllViews();
        }
        deliveryLayout.setViewList(views);
        deliveryLayout.setOnDeliveryChangeListener(new VerticalMarqueeLayout.OnDeliveryChangeListener() {
            @Override
            public void onDeliveryChange(int index) {
                deliveryTimeTextView.setText(deliveryList.get(index).getNewDelivery().getTime());
            }
        });
    }

    private View inflateView(LayoutInflater inflater, VerticalMarqueeLayout marqueeRoot, final int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(getContext());
        }
        View view = inflater.inflate(R.layout.item_delivery_vertical_banner, marqueeRoot, false);
        NiceImageView orderImageView = view.findViewById(R.id.orderImageView);
        TextView statusDescTextView = view.findViewById(R.id.statusDescTextView);
        TextView deliveryContextTextView = view.findViewById(R.id.deliveryContextTextView);
        LinearLayout deliveryRootLayut = view.findViewById(R.id.deliveryRootLayut);
        Glide.with(getContext()).load(deliveryList.get(position).getThumb()).error(R.drawable.image_error).placeholder(R.drawable.image_error)
                .fallback(R.drawable.image_error).into(orderImageView);
        statusDescTextView.setText(deliveryList.get(position).getStatus_desc());
        deliveryContextTextView.setText(deliveryList.get(position).getNewDelivery().getContext());
        deliveryRootLayut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryInfoDialog.show();
                deliveryInfoDialog.setOrderId(deliveryList.get(position).getOid());
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerImageView:
                Intent gerenxinxiIntent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(gerenxinxiIntent);
                break;
            case R.id.gouwucheImageView:
                Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(cartIntent);
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
                Intent followIntent = new Intent(getContext(), MyFollowActivity.class);
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
                Intent zujiIntent0 = new Intent(getActivity(), MyFootprintActivity.class);
                zujiIntent0.putExtra("position", 0);
                startActivity(zujiIntent0);
                break;
            case R.id.pinglunlayout:
                Intent zujiIntent1 = new Intent(getActivity(), MyFootprintActivity.class);
                zujiIntent1.putExtra("position", 1);
                startActivity(zujiIntent1);
                break;
            case R.id.dianzanlayout:
                Intent zujiIntent2 = new Intent(getActivity(), MyFootprintActivity.class);
                zujiIntent2.putExtra("position", 2);
                startActivity(zujiIntent2);
                break;
            case R.id.liulanjilulayout:
                Intent zujiIntent3 = new Intent(getActivity(), MyFootprintActivity.class);
                zujiIntent3.putExtra("position", 3);
                startActivity(zujiIntent3);
                break;
            case R.id.shoucanglayout:
                Intent myCollectionIntent = new Intent(getActivity(), MyCollectionActivity.class);
                startActivity(myCollectionIntent);
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
                Intent qianbaoIntent = new Intent(getActivity(), MyPurseActivity.class);
                startActivity(qianbaoIntent);
                break;
            case R.id.kaquanlayout:
                Intent kaquanIntent = new Intent(getActivity(), MyCouponActivity.class);
                startActivity(kaquanIntent);
                break;
            case R.id.huiyuanlayout:
                Intent huiyuanIntent = new Intent(getActivity(), MemberCenterActivity.class);
                startActivity(huiyuanIntent);
                break;
            case R.id.qiandaolayout:
                Intent qiandaoIntent = new Intent(getActivity(), CheckInActivity.class);
                startActivity(qiandaoIntent);
                break;
            case R.id.dianpulayout:
                if (userResult.getData().getUser().isIs_rz()) {
                    if (userResult.getData().getUser().isHas_circle()) {
                        circleSelectDialog.show();
                    } else {
                        ynDialog.show();
                        ynDialog.setTitle("您还未创建圈子，是否立即创建圈子？");
                    }

                } else {
                    ynDialog.show();
                    ynDialog.setTitle("您还未通过认证，是否立即实名认证？");
                }
                break;
            case R.id.shangwulayout:

                break;
            case R.id.kefulayout:
                Intent customerServiceIntent = new Intent(getActivity(), ContactCustomerServiceActivity.class);
                startActivity(customerServiceIntent);
                break;
            case R.id.gongnenglayout:

                break;
            case R.id.buyVipButton:
                Intent vipIntent = new Intent(getActivity(), BuyVIPActivity.class);
                startActivity(vipIntent);
                break;
            case R.id.regTokenTextView:
                regTokenDialog.show();
                regTokenDialog.setRegTokenData(userResult.getData().getUser().getRegtoken(), userResult.getData().getUser().getSharetxt(), null);
                break;
        }
    }


    private UserResult userResult;

    /**
     * 首页个人中心
     */
    private void userHome() {
        final String t1 = SPUtils.getInstance().getUserToken(BaseApplication.applicationContext);
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.USER_HOME);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("userHome: " + result);
                userResult = GsonUtils.GsonToBean(result, UserResult.class);
                String t2 = userResult.getToken();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("--------> userHome: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private OrderDeliveryResult deliveryResult;
    private List<OrderDelivery> deliveryList;

    /**
     * 物流数据
     */
    private void deliveryList() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.DELIVERY_LIST);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("物流: " + result);
                deliveryResult = GsonUtils.GsonToBean(result, OrderDeliveryResult.class);
                deliveryList = deliveryResult.getData().getList();
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
                System.out.println("物流: " + deliveryResult.getData().getList().size());
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
