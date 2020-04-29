package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.AppUpdateUtils;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class AppraiseUsDialog extends Dialog implements View.OnClickListener{
    private TextView goodTextView,adviseTextView,refuseTextView;
    public AppraiseUsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_appraise_us);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = Math.round(ScreentUtils.getInstance().getScreenWidth(getContext())* 0.773f);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews(){
        goodTextView=findViewById(R.id.goodTextView);
        goodTextView.setOnClickListener(this);
        adviseTextView=findViewById(R.id.adviseTextView);
        adviseTextView.setOnClickListener(this);
        refuseTextView=findViewById(R.id.refuseTextView);
        refuseTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goodTextView:
                AppUpdateUtils.getInstance().appraiseApp(getContext());
                dismiss();
                break;
            case R.id.adviseTextView:
                AppUpdateUtils.getInstance().appraiseApp(getContext());
                dismiss();
                break;
            case R.id.refuseTextView:
                dismiss();
                break;
        }
    }
}
