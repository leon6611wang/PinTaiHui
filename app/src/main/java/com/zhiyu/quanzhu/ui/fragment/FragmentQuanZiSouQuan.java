package com.zhiyu.quanzhu.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.QuanZiSouQuanRecyclerAdapter;
import com.zhiyu.quanzhu.ui.popupwindow.QuanZiSouQuanTypePopupWindow;
import com.zhiyu.quanzhu.ui.widget.MyRecyclerView;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

/**
 * 圈子-关注
 */
public class FragmentQuanZiSouQuan extends Fragment implements View.OnClickListener {
    private View view;
    private MyRecyclerView mRecyclerView;
    private QuanZiSouQuanRecyclerAdapter adapter;
    private TextView typeTextView, areaTextView, orderTextView;
    private LinearLayout typeLayout, areaLayout, orderLayout;
    private ImageView typeImage, areaImage, orderImage;
    private int dp_5, dp_200;
    private LinearLayout typeMenuLayout, areaMenuLayout, orderMenuLayout;
    private boolean typeMenuShow = false, areaMenuShow = false, orderMenuShow = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quanzi_souquan, container, false);
        dp_5 = (int) getContext().getResources().getDimension(R.dimen.dp_5);
        dp_200 = (int) getContext().getResources().getDimension(R.dimen.dp_200);
        initViews();
        initMenuLayout();
        return view;
    }

    private void initViews() {
        typeMenuLayout = view.findViewById(R.id.typeMenuLayout);
        areaMenuLayout = view.findViewById(R.id.areaMenuLayout);
        orderMenuLayout = view.findViewById(R.id.orderMenuLayout);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        adapter = new QuanZiSouQuanRecyclerAdapter(getContext());
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(dp_5);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
        mRecyclerView.setAdapter(adapter);

        typeLayout = view.findViewById(R.id.typeLayout);
        areaLayout = view.findViewById(R.id.areaLayout);
        orderLayout = view.findViewById(R.id.orderLayout);
        typeTextView = view.findViewById(R.id.typeTextView);
        typeLayout.setOnClickListener(this);
        areaTextView = view.findViewById(R.id.areaTextView);
        areaLayout.setOnClickListener(this);
        orderTextView = view.findViewById(R.id.orderTextView);
        orderLayout.setOnClickListener(this);
        typeImage = view.findViewById(R.id.typeImage);
        areaImage = view.findViewById(R.id.areaImage);
        orderImage = view.findViewById(R.id.orderImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.typeLayout:
                filterChange(1);
                menu(1);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(typeTextView);
                break;
            case R.id.areaLayout:
                filterChange(2);
                menu(2);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(areaTextView);
                break;
            case R.id.orderLayout:
                filterChange(3);
                menu(3);
//                new QuanZiSouQuanTypePopupWindow(getContext()).showAtBottom(orderTextView);
                break;
        }
    }

    private void filterChange(int position) {
        typeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        areaTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        orderTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_black));
        typeImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        areaImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        orderImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_black));
        switch (position) {
            case 1:
                typeTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                typeImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 2:
                areaTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                areaImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 3:
                orderTextView.setTextColor(getContext().getResources().getColor(R.color.text_color_yellow));
                orderImage.setImageDrawable(getContext().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
        }
    }

    private void initMenuLayout() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", 0f, -dp_200);
        animator1.setDuration(0);//播放时长
        animator1.setStartDelay(0);//延迟播放
        animator1.setRepeatCount(0);//重放次数
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", 0f, -dp_200);
        animator2.setDuration(0);//播放时长
        animator2.setStartDelay(0);//延迟播放
        animator2.setRepeatCount(0);//重放次数
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -dp_200);
        animator3.setDuration(0);//播放时长
        animator3.setStartDelay(0);//延迟播放
        animator3.setRepeatCount(0);//重放次数
        animator3.start();
    }

    private void hideTypeMenu() {
        typeMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideAreaMenu() {
        areaMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideOrderMenu() {
        orderMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -dp_200);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showTypeMenu() {
        typeMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(typeMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showAreaMenu() {
        areaMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(areaMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showOrderMenu() {
        orderMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", -dp_200, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void menu(int position) {
        switch (position) {
            case 1:
                if (areaMenuShow) {
                    hideAreaMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (typeMenuShow) {
                    hideTypeMenu();
                } else {
                    showTypeMenu();
                }
                break;
            case 2:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (areaMenuShow) {
                    hideAreaMenu();
                } else {
                    showAreaMenu();
                }
                break;
            case 3:
                if (typeMenuShow) {
                    hideTypeMenu();
                }
                if (areaMenuShow) {
                    hideAreaMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                } else {
                    showOrderMenu();
                }
                break;
        }
    }


}
