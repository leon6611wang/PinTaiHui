package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class FangKeXiangQingDialog extends Dialog {
    private Context mContext;

    public FangKeXiangQingDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public FangKeXiangQingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fangke_xiangqing);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
        params.height=ScreentUtils.getInstance().getScreenHeight(mContext)/5*3;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.dialogBottomShow);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);
    }


}
