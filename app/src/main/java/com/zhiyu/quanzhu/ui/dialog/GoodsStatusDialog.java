package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodsStatusDialog extends Dialog implements View.OnClickListener{
    private LoopView mLoopView;
    private List<String> list = new ArrayList<>();
    private String goodsStatus;
    private TextView cancelTextView,confirmTextView;

    public GoodsStatusDialog(@NonNull Context context, int themeResId,OnGoodsStatusListener listener) {
        super(context, themeResId);
        this.onGoodsStatusListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goods_status);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext());
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 5 * 2;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initData();
        initViews();
    }

    private void initData() {
        list.add("已收到货物");
        list.add("未收到货物");
        goodsStatus = list.get(0);
    }

    private void initViews() {
        cancelTextView=findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView=findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mLoopView = findViewById(R.id.mLoopView);
        mLoopView.setItems(list);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                goodsStatus = list.get(index);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.confirmTextView:
                if(null!=onGoodsStatusListener){
                    onGoodsStatusListener.onGoodsSatus(mLoopView.getSelectedItem()+1,goodsStatus);
                }
                dismiss();
                break;
        }
    }
    private OnGoodsStatusListener onGoodsStatusListener;
    public interface OnGoodsStatusListener {
        void onGoodsSatus(int statusIndex,String status);
    }

}
