package com.zhiyu.quanzhu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.chic.utils.SPUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.result.ShareResult;
import com.zhiyu.quanzhu.ui.activity.CardInformationActivity;
import com.zhiyu.quanzhu.ui.activity.CreateCircleActivity;
import com.zhiyu.quanzhu.ui.activity.MyCirclesActivity;
import com.zhiyu.quanzhu.ui.activity.ScanActivity;
import com.zhiyu.quanzhu.ui.adapter.MyFragmentStatePagerAdapter;
import com.zhiyu.quanzhu.ui.dialog.HomeXiaoXiMenuDialog;
import com.zhiyu.quanzhu.ui.dialog.ShareDialog;
import com.zhiyu.quanzhu.ui.widget.NoScrollViewPager;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ShareUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页圈聊、消息
 */
public class FragmentHomeXiaoXi extends Fragment implements View.OnClickListener {
    private View view;
    private NoScrollViewPager mViewPager;
    private MyFragmentStatePagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TextView quanliaoTextView, xiaoxiTextView, xitongTextView;
    private LinearLayout rightLayout;
    private HomeXiaoXiMenuDialog menuDialog;
    private ShareDialog shareDialog;
    private ImageView menuImageView;
    private int menuY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_xiaoxi, container, false);
        initViews();
        initDialogs();
        shareConfig();
        return view;
    }

    private void initDialogs() {
        menuDialog = new HomeXiaoXiMenuDialog(getContext(), R.style.dialog, new HomeXiaoXiMenuDialog.OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int position, String desc) {
                switch (position) {
                    case 1:
                        Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
                        scanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(scanIntent);
                        break;
                    case 2:
                        Intent createShangQuanIntent = new Intent(getActivity(), CreateCircleActivity.class);
                        createShangQuanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(createShangQuanIntent);
                        break;
                    case 3:

                        break;
                    case 4:
                        Intent cardIntent = new Intent(getActivity(), CardInformationActivity.class);
                        cardIntent.putExtra("uid", (long) SPUtils.getInstance().getUserId(BaseApplication.applicationContext));
                        cardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(cardIntent);
                        break;
                    case 5:
                        shareDialog.show();
                        shareDialog.setShare(shareResult.getData().getShare(),SPUtils.getInstance().getUserId(getContext()));
                        shareDialog.hideInnerShare();
                        break;
                    case 6:
                        Intent shangquanIntent = new Intent(getActivity(), MyCirclesActivity.class);
                        shangquanIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(shangquanIntent);
                        break;
                }
//                System.out.println("position: "+position+" , desc: "+desc);
            }
        });
        shareDialog = new ShareDialog(getActivity(), getContext(), R.style.dialog);
    }

    private void initViews() {
        quanliaoTextView = view.findViewById(R.id.quanliaoTextView);
        quanliaoTextView.setOnClickListener(this);
        xiaoxiTextView = view.findViewById(R.id.xiaoxiTextView);
        xiaoxiTextView.setOnClickListener(this);
        xitongTextView = view.findViewById(R.id.xitongTextView);
        xitongTextView.setOnClickListener(this);
        rightLayout = view.findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        mViewPager = view.findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentXiaoXiQuanLiao());
        fragmentList.add(new FragmentXiaoXiXiaoXi());
        fragmentList.add(new FragmentXiaoXiXiTong());
        adapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(2);
        titleChange(2);
        menuImageView = view.findViewById(R.id.menuImageView);
        menuImageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        menuImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] locations = new int[2];
                        menuImageView.getLocationOnScreen(locations);
                        int menu_y = locations[1];
                        int menu_height = menuImageView.getHeight(); // 获取高度
//                        menuY=menu_y+menu_height;
                        menuY = menu_y - menu_height + 20;
//                        System.out.println("menu_height: " + menu_height+" , menu_y: "+menu_y);
                        return true;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanliaoTextView:
                titleChange(0);
                break;
            case R.id.xiaoxiTextView:
                titleChange(1);
                break;
            case R.id.xitongTextView:
                titleChange(2);
                break;
            case R.id.rightLayout:
                menuDialog.show();
                menuDialog.setY(menuY);
                break;
        }
    }

    private void titleChange(int position) {
        quanliaoTextView.setTextSize(15);
        quanliaoTextView.setAlpha(0.7f);
        xiaoxiTextView.setTextSize(15);
        xiaoxiTextView.setAlpha(0.7f);
        xitongTextView.setTextSize(15);
        xitongTextView.setAlpha(0.7f);
        switch (position) {
            case 0:
                quanliaoTextView.setTextSize(16);
                quanliaoTextView.setAlpha(1.0f);
                break;
            case 1:
                xiaoxiTextView.setTextSize(16);
                xiaoxiTextView.setAlpha(1.0f);
                break;
            case 2:
                xitongTextView.setTextSize(16);
                xitongTextView.setAlpha(1.0f);
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.setQQShareCallback(requestCode,resultCode,data);
    }

    private ShareResult shareResult;
    private void shareConfig(){
        RequestParams params= MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL+ConstantsUtils.SHARE_CONFIG);
        params.addBodyParameter("type", ShareUtils.SHARE_TYPE_APP);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                shareResult= GsonUtils.GsonToBean(result,ShareResult.class);
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
