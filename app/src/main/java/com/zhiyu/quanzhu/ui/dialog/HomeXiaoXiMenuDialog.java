package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class HomeXiaoXiMenuDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int screenWidth, dp_5;
    private float ratio = 0.3733f, ratio2 = 1.7714f;
    private int dialogWidth, dialogHeight;
    private LinearLayout.LayoutParams ll;
    private LinearLayout contentLayout;
    private LinearLayout saoyisaolayout, chuangquanlayout, tianjialayout, mingpianlayout, yaoqinglayout, shangquanlayout;

    public HomeXiaoXiMenuDialog(@NonNull Context context, int themeResId, OnMenuSelectedListener listener) {
        super(context, themeResId);
        this.context = context;
        this.onMenuSelectedListener = listener;
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dialogWidth = (int) (ratio * screenWidth);
        dialogHeight = (int) (dialogWidth * ratio2);
        dp_5 = (int) context.getResources().getDimension(R.dimen.dp_5);
        ll = new LinearLayout.LayoutParams(dialogWidth, dialogHeight);
        ll.rightMargin = dp_5;


    }

    public void setY(int y) {
        ll.topMargin = y;
        contentLayout.setLayoutParams(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_home_xiaoxi_menu);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        contentLayout = findViewById(R.id.contentLayout);
        saoyisaolayout = findViewById(R.id.saoyisaolayout);
        saoyisaolayout.setOnClickListener(this);
        chuangquanlayout = findViewById(R.id.chuangquanlayout);
        chuangquanlayout.setOnClickListener(this);
        tianjialayout = findViewById(R.id.tianjialayout);
        tianjialayout.setOnClickListener(this);
        mingpianlayout = findViewById(R.id.mingpianlayout);
        mingpianlayout.setOnClickListener(this);
        yaoqinglayout = findViewById(R.id.yaoqinglayout);
        yaoqinglayout.setOnClickListener(this);
        shangquanlayout = findViewById(R.id.shangquanlayout);
        shangquanlayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoyisaolayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(1, "扫一扫");
                }
                dismiss();
                break;
            case R.id.chuangquanlayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(2, "快速创圈");
                }
                dismiss();
                break;
            case R.id.tianjialayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(3, "添加圈友");
                }
                dismiss();
                break;
            case R.id.mingpianlayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(4, "我的名片");
                }
                dismiss();
                break;
            case R.id.yaoqinglayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(5, "邀请圈友");
                }
                dismiss();
                break;
            case R.id.shangquanlayout:
                if (null != onMenuSelectedListener) {
                    onMenuSelectedListener.onMenuSelected(6, "我的商圈");
                }
                dismiss();
                break;
        }
    }

    private OnMenuSelectedListener onMenuSelectedListener;

    public interface OnMenuSelectedListener {
        void onMenuSelected(int position, String desc);
    }
}
