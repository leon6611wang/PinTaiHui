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
public class CardInfoMenuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private int menuImageY;
    private LinearLayout sharelayout, editlayout;
    private View topMarginView;

    public CardInfoMenuDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CardInfoMenuDialog(@NonNull Context context, int themeResId, OnMyMingPianMenuChooseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.onMyMingPianMenuChooseListener = listener;
    }

    public void setMenuImageY(int y) {
        this.menuImageY = y;
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, menuImageY);
        topMarginView.setLayoutParams(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_info_menu);
        initViews();
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = ScreentUtils.getInstance().getScreenWidth(mContext);
//        params.height = ScreentUtils.getInstance().getScreenHeight(mContext);
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void initViews() {
        sharelayout = findViewById(R.id.sharelayout);
        sharelayout.setOnClickListener(this);
        editlayout = findViewById(R.id.editlayout);
        editlayout.setOnClickListener(this);
        topMarginView = findViewById(R.id.topMarginView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharelayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(1, "分享");
                    dismiss();
                }
                break;
            case R.id.editlayout:
                if (null != onMyMingPianMenuChooseListener) {
                    onMyMingPianMenuChooseListener.onMyMingPianMenuChoose(2, "编辑");
                    dismiss();
                }
                break;
        }
    }

    private OnMyMingPianMenuChooseListener onMyMingPianMenuChooseListener;

    public interface OnMyMingPianMenuChooseListener {
        void onMyMingPianMenuChoose(int position, String desc);
    }

}
