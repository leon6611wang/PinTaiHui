package com.zhiyu.quanzhu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.ViewPagerAdapter;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.MyRequestParams;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class FragmentLiShi extends Fragment implements View.OnClickListener {
    private View view;
    private ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();


    private LinearLayout shangquanlayout, wenzhanglayout, shangpinlayout;
    private TextView shangquantextview, wenzhangtextview, shangpintextview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_lishi, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        shangquanlayout = view.findViewById(R.id.shangquanlayout);
        shangquanlayout.setOnClickListener(this);
        wenzhanglayout = view.findViewById(R.id.wenzhanglayout);
        wenzhanglayout.setOnClickListener(this);
        shangpinlayout = view.findViewById(R.id.shangpinlayout);
        shangpinlayout.setOnClickListener(this);
        shangquantextview = view.findViewById(R.id.shangquantextview);
        wenzhangtextview = view.findViewById(R.id.wenzhangtextview);
        shangpintextview = view.findViewById(R.id.shangpintextview);

        mViewPager = view.findViewById(R.id.mViewPager);
        fragmentList.add(new FragmentShangQuan());
        fragmentList.add(new FragmentWenZhang());
        fragmentList.add(new FragmentShangPin());
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shangquanlayout:
                barChange(0);
                break;
            case R.id.wenzhanglayout:
                barChange(1);
                break;
            case R.id.shangpinlayout:
                barChange(2);
                break;
        }
    }

    private void barChange(int position) {
        shangquantextview.setTextColor(getContext().getResources().getColor(R.color.text_color_gray));
        wenzhangtextview.setTextColor(getContext().getResources().getColor(R.color.text_color_gray));
        shangpintextview.setTextColor(getContext().getResources().getColor(R.color.text_color_gray));
        shangquantextview.setBackground(null);
        wenzhangtextview.setBackground(null);
        shangpintextview.setBackground(null);
        switch (position) {
            case 0:
                shangquantextview.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                shangquantextview.setBackground(getContext().getResources().getDrawable(R.drawable.shape_type_bg));
                break;
            case 1:
                wenzhangtextview.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                wenzhangtextview.setBackground(getContext().getResources().getDrawable(R.drawable.shape_type_bg));
                break;
            case 2:
                shangpintextview.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                shangpintextview.setBackground(getContext().getResources().getDrawable(R.drawable.shape_type_bg));
                break;
        }
        mViewPager.setCurrentItem(position);
    }

    private void myHistory() {
        RequestParams params = MyRequestParams.getInstance(getContext()).getRequestParams(ConstantsUtils.BASE_URL + ConstantsUtils.MY_HISTORY_LIST);


    }
}
