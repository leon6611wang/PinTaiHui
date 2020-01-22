package com.zhiyu.quanzhu.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.MingPianRecyclerAdapter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 名片广场
 */
public class MingPianGuangChangActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout;
    private TextView titleTextView;
    private RecyclerView mRecyclerView;
    private MingPianRecyclerAdapter adapter;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private int chengshiMenuHeight, zhiyeMenuHeight, orderMenuHeight;
    private boolean chengshiMenuShow, zhiyeMenuShow, orderMenuShow;
    private LinearLayout chengshiMenuLayout, zhiyeMenuLayout, orderMenuLayout;
    private LinearLayout chengshiLayout, zhiyeLayout, orderLayout;
    private TextView chengshiTextView, zhiyeTextView, orderTextView;
    private ImageView chengshiImage, zhiyeImage, orderImage;

    private static class MyHandler extends Handler {
        WeakReference<MingPianGuangChangActivity> mingPianGuangChangActivityWeakReference;

        public MyHandler(MingPianGuangChangActivity activity) {
            mingPianGuangChangActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MingPianGuangChangActivity activity = mingPianGuangChangActivityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mingpianguangchang);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        chengshiMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        zhiyeMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        orderMenuHeight = (int) getResources().getDimension(R.dimen.dp_200);
        initViews();
        initMenuLayout();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("名片广场");

        chengshiMenuLayout = findViewById(R.id.chengshiMenuLayout);
        zhiyeMenuLayout = findViewById(R.id.zhiyeMenuLayout);
        orderMenuLayout = findViewById(R.id.orderMenuLayout);
        chengshiLayout = findViewById(R.id.chengshiLayout);
        chengshiLayout.setOnClickListener(this);
        zhiyeLayout = findViewById(R.id.zhiyeLayout);
        zhiyeLayout.setOnClickListener(this);
        orderLayout = findViewById(R.id.orderLayout);
        orderLayout.setOnClickListener(this);
        chengshiTextView = findViewById(R.id.chengshiTextView);
        zhiyeTextView = findViewById(R.id.zhiyeTextView);
        orderTextView = findViewById(R.id.orderTextView);
        chengshiImage = findViewById(R.id.chengshiImage);
        zhiyeImage = findViewById(R.id.zhiyeImage);
        orderImage = findViewById(R.id.orderImage);

        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message message = myHandler.obtainMessage(1);
                            message.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new MingPianRecyclerAdapter(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.chengshiLayout:
                barChange(1);
                menu(1);
                break;
            case R.id.zhiyeLayout:
                barChange(2);
                menu(2);
                break;
            case R.id.orderLayout:
                barChange(3);
                menu(3);
                break;
        }
    }

    private void barChange(int position) {
        chengshiTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        zhiyeTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        orderTextView.setTextColor(getResources().getColor(R.color.text_color_black));
        chengshiImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        zhiyeImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        orderImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_black));
        switch (position) {
            case 1:
                chengshiTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                chengshiImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 2:
                zhiyeTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                zhiyeImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
            case 3:
                orderTextView.setTextColor(getResources().getColor(R.color.text_color_yellow));
                orderImage.setImageDrawable(getResources().getDrawable(R.mipmap.sanjiao_xia_yellow));
                break;
        }
    }

    private void initMenuLayout() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", 0f, -chengshiMenuHeight);
        animator1.setDuration(0);//播放时长
        animator1.setStartDelay(0);//延迟播放
        animator1.setRepeatCount(0);//重放次数
        animator1.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", 0f, -zhiyeMenuHeight);
        animator2.setDuration(0);//播放时长
        animator2.setStartDelay(0);//延迟播放
        animator2.setRepeatCount(0);//重放次数
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -orderMenuHeight);
        animator3.setDuration(0);//播放时长
        animator3.setStartDelay(0);//延迟播放
        animator3.setRepeatCount(0);//重放次数
        animator3.start();
    }

    private void showChengshiMenu() {
        chengshiMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", -chengshiMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showZhiyeMenu() {
        zhiyeMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", -zhiyeMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void showOrderMenu() {
        orderMenuShow = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", -orderMenuHeight, 0f);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideChengshiMenu() {
        chengshiMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(chengshiMenuLayout, "translationY", 0f, -chengshiMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideOrderMenu() {
        orderMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(orderMenuLayout, "translationY", 0f, -orderMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void hideZhiyeMenu() {
        zhiyeMenuShow = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(zhiyeMenuLayout, "translationY", 0f, -zhiyeMenuHeight);
        animator.setDuration(100);//播放时长
        animator.setStartDelay(0);//延迟播放
        animator.setRepeatCount(0);//重放次数
        animator.start();
    }

    private void menu(int position) {
        switch (position) {
            case 1:
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                } else {
                    showChengshiMenu();
                }
                break;
            case 2:
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                }
                if (orderMenuShow) {
                    hideOrderMenu();
                }
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
                } else {
                    showZhiyeMenu();
                }
                break;
            case 3:
                if (chengshiMenuShow) {
                    hideChengshiMenu();
                }
                if (zhiyeMenuShow) {
                    hideZhiyeMenu();
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
