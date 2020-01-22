package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.ui.adapter.BankCardRecyclerAdapter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SpaceItemDecoration;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class BankCardListActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout backLayout,rightLayout;
    private TextView titleTextView,rightTextView;
    private SwipeMenuRecyclerView mRecyclerView;
    private BankCardRecyclerAdapter adapter;
    private MyHandler myHandler = new MyHandler(this);
    private int bankcardmenuwidth,dp_15;
    private static class MyHandler extends Handler {
        WeakReference<BankCardListActivity> bankCardListActivityWeakReference;

        public MyHandler(BankCardListActivity activity) {
            bankCardListActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BankCardListActivity activity = bankCardListActivityWeakReference.get();
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankcardmenuwidth=(int) getResources().getDimension(R.dimen.bankcard_menu_width);
        dp_15=(int)getResources().getDimension(R.dimen.dp_15);
        setContentView(R.layout.activity_bankcard_list);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        initViews();
    }

    private void initViews() {
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("我的银行卡");
        rightLayout=findViewById(R.id.rightLayout);
        rightLayout.setOnClickListener(this);
        rightTextView=findViewById(R.id.rightTextView);
        rightTextView.setText("新增");

        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new BankCardRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        SpaceItemDecoration spaceItemDecoration=new SpaceItemDecoration(dp_15);
        mRecyclerView.addItemDecoration(spaceItemDecoration);
        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(BankCardListActivity.this)
                        .setBackgroundColor(getResources().getColor(R.color.text_color_yellow))
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(17)
                        .setWidth(bankcardmenuwidth)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                rightMenu.addMenuItem(deleteItem);
            }
        };

        // 设置监听器。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                Log.i("SwipeMenu", "adapterPosition: " + adapterPosition + " , menuPosition: " + menuPosition);
            }
        };

        // 菜单点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.rightLayout:
                Intent addBankCardIntent=new Intent(this,AddBankCardActivity.class);
                startActivity(addBankCardIntent);
                break;
        }
    }
}
