package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 商圈圈简介编辑
 */
public class ShangQuanJianJieEditDialog extends Dialog {
    private  Context mContext;
    public ShangQuanJianJieEditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shangquan_jianjie_edit);
        WindowManager.LayoutParams params =getWindow().getAttributes();
        params.width= ScreentUtils.getInstance().getScreenWidth(mContext)/5*4;
//        params.height=ScreentUtils.getInstance().getScreenWidth(mContext)/5*2;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);
    }
}
