package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhiyu.quanzhu.R;

/**
 * 名片详情-菜单
 */
public class ContactCustomerServiceDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private int menuImageY;
    private LinearLayout servicelayout;
    private View topMarginView;

    public ContactCustomerServiceDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ContactCustomerServiceDialog(@NonNull Context context, int themeResId, OnCustomerServiceMenuChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onCustomerServiceListener = listener;
    }


    public void setMenuImageY(int y) {
        this.menuImageY = y;
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, menuImageY);
        topMarginView.setLayoutParams(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact_customer_service);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void initViews() {
        servicelayout = findViewById(R.id.servicelayout);
        servicelayout.setOnClickListener(this);
        topMarginView = findViewById(R.id.topMarginView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.servicelayout:
                if (null != onCustomerServiceListener) {
                    onCustomerServiceListener.onCustomerService();
                    dismiss();
                }
                break;
        }
    }

    private OnCustomerServiceMenuChooseListener onCustomerServiceListener;

    public interface OnCustomerServiceMenuChooseListener {
        void onCustomerService();
    }

}
