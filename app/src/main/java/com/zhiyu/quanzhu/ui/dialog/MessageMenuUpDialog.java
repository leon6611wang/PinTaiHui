package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class MessageMenuUpDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private WindowManager.LayoutParams params;
    private LinearLayout menuLayout, rootLayout;
    private TextView topTextView, deleteTextView;
    private LinearLayout.LayoutParams ll;
    private int screenWidth, dp_120;

    public MessageMenuUpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        dp_120 = (int) context.getResources().getDimension(R.dimen.dp_120);
        screenWidth = ScreentUtils.getInstance().getScreenWidth(context);
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setLocation(int x, int y) {
        if (x < dp_120) {
            x = dp_120;
        }
        ll.rightMargin = screenWidth - x;
        ll.topMargin = y;
        menuLayout.setLayoutParams(ll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_messagelist_menu_up);
        params = getWindow().getAttributes();
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        menuLayout = findViewById(R.id.menuLayout);
        menuLayout.setOnClickListener(this);
        rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(this);
        topTextView = findViewById(R.id.topTextView);
        topTextView.setOnClickListener(this);
        deleteTextView = findViewById(R.id.deleteTextView);
        deleteTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rootLayout:
                dismiss();
                break;
            case R.id.topTextView:
                System.out.println("置顶");
                dismiss();
                break;
            case R.id.deleteTextView:
                System.out.println("删除");
                dismiss();
                break;
        }
    }
}
