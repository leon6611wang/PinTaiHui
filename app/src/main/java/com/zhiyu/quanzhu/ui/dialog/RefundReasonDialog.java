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

public class RefundReasonDialog extends Dialog implements View.OnClickListener{
    private LoopView mLoopView;
    private List<String> list = new ArrayList<>();
    private String refundReason;
    private TextView cancelTextView,confirmTextView;

    public RefundReasonDialog(@NonNull Context context, int themeResId,OnRefundReasonListener listener) {
        super(context, themeResId);
        this.onRefundReasonListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_refund_reason);
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
        list.add("七天无理由退货");
        list.add("拍错/不喜欢/不喜欢");
        list.add("质量问题");
        list.add("实物与店铺描述不符");
        list.add("尺寸不符合");
        list.add("做工粗糙，有瑕疵");
        list.add("款式/型号/颜色/材质与描述不符");
        list.add("卖家发错货");
        list.add("假冒伪劣");
        list.add("收到商品少件或破损");
        list.add("未收到货");
        list.add("商品降价了");
        list.add("其他");
        refundReason = list.get(0);
    }

    private void initViews() {
        cancelTextView=findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        confirmTextView=findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
        mLoopView = findViewById(R.id.mLoopView);
        mLoopView.setItems(list);
        mLoopView.setInitPosition(0);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                refundReason = list.get(index);
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
                if(null!=onRefundReasonListener){
                    onRefundReasonListener.onRefundReason(refundReason);
                }
                dismiss();
                break;
        }
    }

    private OnRefundReasonListener onRefundReasonListener;
    public interface OnRefundReasonListener{
        void onRefundReason(String reason);
    }
}
