package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.MyWalletRecyclerAdapter;
import com.zhiyu.quanzhu.ui.dialog.CalendarDialog;
import com.zhiyu.quanzhu.ui.widget.MyControllScrollRecyclerView;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    private MyControllScrollRecyclerView mRecyclerView;
    private MyWalletRecyclerAdapter adapter;
    private View headerView;
    private View mHeaderView, mMenuView, headerlayout, menulayout;
    private LinearLayoutManager linearLayoutManager;
    private MyHandler myHandler = new MyHandler(this);
    private int viewpagerheight = 0, screenheight, headerviewheight, menuviewheight, totalDy;
    private int[] menulocation = new int[2];
    private int menulayoutY = 0;
    private LinearLayout quanbuView, shouruView, zhichuView, quanbuLayout, shouruLayout, zhichuLayout;
    private TextView quanbuTextView, shouruTextView, zhichuTextView, quanbuTextLayout, shouruTextLayout, zhichuTextLayout;
    private View quanbuLineView, shouruLineView, zhichuLineView, quanbuLineLayout, shouruLineLayout, zhichuLineLayout;
    private TextView startDateTextView, endDateTextView;
    private CalendarDialog startDateDialog, endDateDialog;
    private LinearLayout backView, rightView, backLayout, rightLayout;
    private TextView tixianTextView;

    private static class MyHandler extends Handler {
        WeakReference<MyWalletActivity> qianBaoActivityWeakReference;

        public MyHandler(MyWalletActivity activity) {
            qianBaoActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyWalletActivity activity = qianBaoActivityWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        screenheight = ScreentUtils.getInstance().getScreenHeight(this);
        ScreentUtils.getInstance().setStatusBarLightMode(this, false);
        initViews();
        initDialogs();
    }

    private void initDialogs() {
        startDateDialog = new CalendarDialog(this, R.style.dialog,false, new CalendarDialog.OnCalendarListener() {
            @Override
            public void onCalendar(int year, int month, int day) {
                startDateTextView.setText(month + "月" + day + "日 " + year);
            }
        });
        endDateDialog = new CalendarDialog(this, R.style.dialog,false, new CalendarDialog.OnCalendarListener() {
            @Override
            public void onCalendar(int year, int month, int day) {
                endDateTextView.setText(month + "月" + day + "日 " + year);
            }
        });
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.controllScroll(true);
        adapter = new MyWalletRecyclerAdapter(getSupportFragmentManager());
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        adapter.addDatas(list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        headerView = LayoutInflater.from(this).inflate(R.layout.header_qianbao_recyclerview, null);
        tixianTextView = headerView.findViewById(R.id.tixianTextView);
        tixianTextView.setOnClickListener(this);
        backView = findViewById(R.id.backLayout);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightView = findViewById(R.id.rightLayout);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bankcardIntent = new Intent(MyWalletActivity.this, BankCardListActivity.class);
                startActivity(bankcardIntent);
            }
        });
        backLayout = headerView.findViewById(R.id.backLayout);
        rightLayout = headerView.findViewById(R.id.rightLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bankcardIntent = new Intent(MyWalletActivity.this, BankCardListActivity.class);
                startActivity(bankcardIntent);
            }
        });
        headerlayout = headerView.findViewById(R.id.headerlayout);
        menulayout = headerView.findViewById(R.id.menulayout);
        mHeaderView = findViewById(R.id.mHeaderView);
        mMenuView = findViewById(R.id.mMenuView);
        startDateTextView = headerView.findViewById(R.id.startDateTextView);
        startDateTextView.setOnClickListener(this);
        endDateTextView = headerView.findViewById(R.id.endDateTextView);
        endDateTextView.setOnClickListener(this);
        quanbuView = findViewById(R.id.quanbuView);
        quanbuView.setOnClickListener(this);
        shouruView = findViewById(R.id.shouruView);
        shouruView.setOnClickListener(this);
        zhichuView = findViewById(R.id.zhichuView);
        zhichuView.setOnClickListener(this);
        quanbuLayout = headerView.findViewById(R.id.quanbuLayout);
        quanbuLayout.setOnClickListener(this);
        shouruLayout = headerView.findViewById(R.id.shouruLayout);
        shouruLayout.setOnClickListener(this);
        zhichuLayout = headerView.findViewById(R.id.zhichuLayout);
        zhichuLayout.setOnClickListener(this);
        quanbuTextView = findViewById(R.id.quanbuTextView);
        quanbuTextLayout = headerView.findViewById(R.id.quanbuTextLayout);
        shouruTextView = findViewById(R.id.shouruTextView);
        shouruTextLayout = headerView.findViewById(R.id.shouruTextLayout);
        zhichuTextView = findViewById(R.id.zhichuTextView);
        zhichuTextLayout = headerView.findViewById(R.id.zhichuTextLayout);
        quanbuLineView = findViewById(R.id.quanbuLineView);
        quanbuLineLayout = headerView.findViewById(R.id.quanbuLineLayout);
        shouruLineView = findViewById(R.id.shouruLineView);
        shouruLineLayout = headerView.findViewById(R.id.shouruLineLayout);
        zhichuLineView = findViewById(R.id.zhichuLineView);
        zhichuLineLayout = headerView.findViewById(R.id.zhichuLineLayout);
        mHeaderView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mHeaderView.getViewTreeObserver().removeOnPreDrawListener(this);
                        headerviewheight = mHeaderView.getHeight(); // 获取高度
                        if (headerviewheight > 0 && menuviewheight > 0) {
                            viewpagerheight = screenheight - headerviewheight - menuviewheight;
                            adapter.setViewpagerHeight(viewpagerheight);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter.setHeaderView(headerView);
                            mRecyclerView.setAdapter(adapter);
                        }

//                        Log.i("ViewHeight", "mHeaderView height: " + headerviewheight);
                        return true;
                    }
                });
        mMenuView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                        menuviewheight = mMenuView.getHeight(); // 获取高度
                        if (headerviewheight > 0 && menuviewheight > 0) {
                            viewpagerheight = screenheight - headerviewheight - menuviewheight;
                            adapter.setViewpagerHeight(viewpagerheight);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter.setHeaderView(headerView);
                            mRecyclerView.setAdapter(adapter);
                        }

//                        Log.i("ViewHeight", " ------ screenheight: " + screenheight);
//                        Log.i("ViewHeight", " ------ viewpagerheight: " + viewpagerheight);
//                        Log.i("ViewHeight", "mMenuView height: " + menuviewheight);
                        return true;
                    }
                });


        headerlayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        headerlayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        int[] headerlocation = new int[2];
                        headerlayout.getLocationOnScreen(headerlocation);
//                        Log.i("ViewHeight", "headerlocation y: " + headerlocation[1]);
                        int height = headerlayout.getHeight(); // 获取高度
//                        Log.i("ViewHeight", "headerlayout height: " + height);
                        return true;
                    }
                });
        menulayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        menulayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        menulayout.getLocationInWindow(menulocation);
                        menulayoutY = menulocation[1];
//                        menulayoutY=menulayout.getTop();
                        Log.i("ViewHeight", "menulayout y: " + menulayoutY);
                        int height = menulayout.getHeight(); // 获取高度
//                        Log.i("ViewHeight", "menulayout height: " + height);
                        return true;
                    }
                });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                barChange();
            }
        });

    }

    private void barChange() {
        if (Math.abs(totalDy) > 0) {
            headerlayout.setVisibility(View.INVISIBLE);
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderView.setBackgroundColor(getResources().getColor(R.color.text_color_yellow));
            float alpha = (float) Math.abs(totalDy) / (float) 300;
            mHeaderView.setAlpha(alpha);
        } else {
            headerlayout.setVisibility(View.VISIBLE);
            mHeaderView.setVisibility(View.INVISIBLE);
            mHeaderView.setAlpha(0);
        }

        Log.i("ViewHeight", "menulayoutY: " + (menulayoutY - headerviewheight) + " , totalDy: " + totalDy);
//        int y = ScreentUtils.getInstance().px2dip(this, Math.abs(totalDy));
//        Log.i("ViewHeight", "***********************y: " + y);
        if (menulayoutY > 0) {
            if (Math.abs(totalDy) >= (menulayoutY - headerviewheight)) {
                mMenuView.setVisibility(View.VISIBLE);
                Log.i("ViewHeight", "显示菜单");
            } else {
                mMenuView.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quanbuView:
                menuChange(0);
                adapter.setCurrentIndex(0);
                break;
            case R.id.quanbuLayout:
                menuChange(0);
                adapter.setCurrentIndex(0);
                break;
            case R.id.shouruView:
                menuChange(1);
                adapter.setCurrentIndex(1);
                break;
            case R.id.shouruLayout:
                menuChange(1);
                adapter.setCurrentIndex(1);
                break;
            case R.id.zhichuView:
                menuChange(2);
                adapter.setCurrentIndex(2);
                break;
            case R.id.zhichuLayout:
                menuChange(2);
                adapter.setCurrentIndex(2);
                break;
            case R.id.startDateTextView:
                startDateDialog.show();
                break;
            case R.id.endDateTextView:
                endDateDialog.show();
                break;
            case R.id.tixianTextView:
                Intent tixianIntent = new Intent(this, TiXianActivity.class);
                startActivity(tixianIntent);
                break;
        }
    }

    private void menuChange(int position) {
        quanbuTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        quanbuTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        shouruTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        shouruTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        zhichuTextView.setTextColor(getResources().getColor(R.color.text_color_gray));
        zhichuTextLayout.setTextColor(getResources().getColor(R.color.text_color_gray));
        quanbuLineView.setVisibility(View.INVISIBLE);
        quanbuLineLayout.setVisibility(View.INVISIBLE);
        shouruLineView.setVisibility(View.INVISIBLE);
        shouruLineLayout.setVisibility(View.INVISIBLE);
        zhichuLineView.setVisibility(View.INVISIBLE);
        zhichuLineLayout.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                quanbuTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanbuTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                quanbuLineView.setVisibility(View.VISIBLE);
                quanbuLineLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                shouruTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shouruTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                shouruLineView.setVisibility(View.VISIBLE);
                shouruLineLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                zhichuTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhichuTextLayout.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhichuLineView.setVisibility(View.VISIBLE);
                zhichuLineLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

}
