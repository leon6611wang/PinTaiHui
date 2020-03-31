package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;


/**
 * 支付方式
 */
public class PayWayDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private LinearLayout wechatlaout,alipaylayout,balancelayout;
    private View wechatview,alipayview,balanceview;
    private TextView balancetextview;
    private int selectPayWay=1;
    private String selectPayWayStr="微信支付";
    public PayWayDialog(@NonNull Context context, int themeResId,OnPayWayListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onPayWayListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payway);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext) / 5 * 3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }


    private void initViews() {
        wechatlaout=findViewById(R.id.wechatlayout);
        wechatlaout.setOnClickListener(this);
        alipaylayout=findViewById(R.id.alipaylayout);
        alipaylayout.setOnClickListener(this);
        balancelayout=findViewById(R.id.balancelayout);
        balancelayout.setOnClickListener(this);
        wechatview=findViewById(R.id.wechatview);
        alipayview=findViewById(R.id.alipayview);
        balanceview=findViewById(R.id.balanceview);
        balancetextview=findViewById(R.id.balancetextview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wechatlayout:
                wayChange(1);
                selectPayWay=1;
                selectPayWayStr="微信支付";
                break;
            case R.id.alipaylayout:
                wayChange(2);
                selectPayWay=2;
                selectPayWayStr="支付宝支付";
                break;
            case R.id.balancelayout:
                wayChange(3);
                selectPayWay=3;
                selectPayWayStr="余额支付";
                break;
        }
    }

    private void wayChange(int position){
        wechatview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        alipayview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        balanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_bg_gray));
        switch (position){
            case 1:
                wechatview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 2:
                alipayview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
            case 3:
                balanceview.setBackground(mContext.getResources().getDrawable(R.drawable.shape_oval_solid_bg_yellow));
                break;
        }
    }
    private OnPayWayListener onPayWayListener;
    public interface OnPayWayListener{
        void onPayWay(int payWay,String payWayStr);
    }

}
