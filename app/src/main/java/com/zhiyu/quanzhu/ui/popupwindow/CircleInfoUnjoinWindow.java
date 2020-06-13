package com.zhiyu.quanzhu.ui.popupwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhiyu.quanzhu.R;

/**
 * 商圈详情-右上角菜单-未加入
 */
public class CircleInfoUnjoinWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View view;
    public CircleInfoUnjoinWindow(Context context, OnMenuSelectListener listener) {
        super(context);
        this.context = context;
        this.onMenuSelectListener = listener;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
         view = inflater.inflate(R.layout.popupwindow_circle_info_unjoin, null);
        setContentView(view);
        initWindow();
        initViews();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.35));
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }
    private LinearLayout shareLayout,complaintLayout;
    private void initViews(){
        shareLayout=view.findViewById(R.id.shareLayout);
        shareLayout.setOnClickListener(this);
        complaintLayout=view.findViewById(R.id.complaintLayout);
        complaintLayout.setOnClickListener(this);
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareLayout:
                if (null != onMenuSelectListener) {
                    onMenuSelectListener.onMenuSelect(1, "分享");
                }
                dismiss();
                break;
            case R.id.complaintLayout:
                if (null != onMenuSelectListener) {
                    onMenuSelectListener.onMenuSelect(2, "投诉");
                }
                dismiss();
                break;
        }
    }

    private OnMenuSelectListener onMenuSelectListener;

    public interface OnMenuSelectListener {
        void onMenuSelect(int index, String menu);
    }

}
