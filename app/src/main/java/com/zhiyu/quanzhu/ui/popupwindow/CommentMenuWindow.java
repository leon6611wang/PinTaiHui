package com.zhiyu.quanzhu.ui.popupwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 评论菜单
 */
public class CommentMenuWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View view;
    private int screenWidth, dp_45;
    private TextView copyTextView, deleteTextView, complaintTextView;
    private View deleteLine;

    public CommentMenuWindow(Context context) {
        super(context);
        this.context = context;
//        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        dp_45 = (int) context.getResources().getDimension(R.dimen.dp_40);
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup_comment_menu, null);
        setContentView(view);
        initWindow();
        initViews();
    }

    private void initViews() {
        copyTextView = view.findViewById(R.id.copyTextView);
        copyTextView.setOnClickListener(this);
        deleteTextView = view.findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
        complaintTextView = view.findViewById(R.id.complaintTextView);
        complaintTextView.setOnClickListener(this);
        deleteLine = view.findViewById(R.id.deleteLine);
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

    public void showMeu(View view, int y, boolean isDelete, OnMenuClickListener listener) {
        deleteLine.setVisibility(isDelete ? View.VISIBLE : View.GONE);
        deleteTextView.setVisibility(isDelete ? View.VISIBLE : View.GONE);
        showAtLocation(view, Gravity.TOP, 0, y - dp_45);//有偏差
        this.onMenuClickListener = listener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //copyTextView, deleteTextView, complaintTextView
            case R.id.copyTextView:
                if (null != onMenuClickListener) {
                    onMenuClickListener.onMenuClick(1);
                }
                dismiss();
                break;
            case R.id.deleteTextView:
                if (null != onMenuClickListener) {
                    onMenuClickListener.onMenuClick(2);
                }
                dismiss();
                break;
            case R.id.complaintTextView:
                if (null != onMenuClickListener) {
                    onMenuClickListener.onMenuClick(3);
                }
                dismiss();
                break;
        }
    }

    private OnMenuClickListener onMenuClickListener;

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }

}
