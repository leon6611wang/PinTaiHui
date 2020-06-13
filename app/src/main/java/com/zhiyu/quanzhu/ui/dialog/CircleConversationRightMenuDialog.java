package com.zhiyu.quanzhu.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.ConversationCircleShop;
import com.zhiyu.quanzhu.ui.fragment.ConversationGroupGoodsFragment;
import com.zhiyu.quanzhu.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CircleConversationRightMenuDialog extends DialogFragment implements View.OnClickListener {
    private View view;
    private int height;
    private List<ConversationCircleShop> shop_list;
    private List<Fragment> fragmentList = new ArrayList<>();
    private LinearLayout closeLayout;
    private int contentLayoutWidth;
    private boolean isGetHeight;

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            height = bundle.getInt("height");
            String shops = bundle.getString("shop_list");
            if (!StringUtils.isNullOrEmpty(shops)) {
                shop_list = GsonUtils.getObjectList(shops, ConversationCircleShop.class);
            }
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogRightShow;
        view = inflater.inflate(R.layout.dialog_im_right_menu, null);
        initViews();
        return view;
    }

    private void initViews() {
        ViewPager viewPager = view.findViewById(R.id.mViewPager);
        initFragmentList();
        AcoesMuscularesAdapter ama = new AcoesMuscularesAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(ama);

        closeLayout = view.findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeLayout:
                dismiss();
                break;
        }
    }

    private void initFragmentList() {
        if (null != shop_list && shop_list.size() > 0) {
            for (ConversationCircleShop shop : shop_list) {
                ConversationGroupGoodsFragment fragment = new ConversationGroupGoodsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("shop", GsonUtils.GsonString(shop));
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }

        }
    }

    class AcoesMuscularesAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public AcoesMuscularesAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
