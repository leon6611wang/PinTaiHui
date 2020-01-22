package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.ui.adapter.ConversationGroupGoodsRecyclerAdapter;
import com.zhiyu.quanzhu.utils.GridSpacingItemDecoration;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.lang.ref.WeakReference;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 圈聊-右侧商品菜单
 */
public class IMRightMenuDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private int dialogHeight, screenHeight, dp_10, contentLayoutWidth, dp_5;
    private LinearLayout contentLayout;
    private WindowManager.LayoutParams params;
    private RecyclerView mRecyclerView;
    private ConversationGroupGoodsRecyclerAdapter adapter;
    private boolean isGetHeight = false;
    private PtrFrameLayout ptrFrameLayout;
    private MyHandler myHandler = new MyHandler(this);
    private LinearLayout closeLayout,shopLayout,shoppingCarLayout,serviceLayout;

    private static class MyHandler extends Handler {
        WeakReference<IMRightMenuDialog> imRightMenuDialogWeakReference;

        public MyHandler(IMRightMenuDialog dialog) {
            imRightMenuDialogWeakReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            IMRightMenuDialog dialog = imRightMenuDialogWeakReference.get();
            switch (msg.what) {
                case 1:
                    dialog.ptrFrameLayout.refreshComplete();
                    break;
            }
        }
    }

    public IMRightMenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        screenHeight = ScreentUtils.getInstance().getScreenHeight(context);
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
    }

    public void setDialogHeight(int height) {
        this.dialogHeight = height;
        params.height = screenHeight - dialogHeight - dp_10;
        getWindow().setAttributes(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_im_right_menu);
        params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(context);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogRightShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
        initPtr();
    }

    private void initViews() {
        contentLayout = findViewById(R.id.contentLayout);
        contentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isGetHeight) {
                    isGetHeight = true;
                    contentLayoutWidth = contentLayout.getHeight();
                    adapter.setWidth(contentLayoutWidth);
                }
            }
        });
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new ConversationGroupGoodsRecyclerAdapter(context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        closeLayout=findViewById(R.id.closeLayout);
        closeLayout.setOnClickListener(this);
        shopLayout=findViewById(R.id.shopLayout);
        shopLayout.setOnClickListener(this);
        shoppingCarLayout=findViewById(R.id.shoppingCarLayout);
        shoppingCarLayout.setOnClickListener(this);
        serviceLayout=findViewById(R.id.serviceLayout);
        serviceLayout.setOnClickListener(this);
    }

    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(context));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(context, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(context));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(context, ptrFrameLayout));
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeLayout:
                dismiss();
                break;
            case R.id.shopLayout:
                System.out.println("店铺首页");
                break;
            case R.id.shoppingCarLayout:
                System.out.println("购物车");
                break;
            case R.id.serviceLayout:
                System.out.println("客服小二");
                break;
        }
    }
}
