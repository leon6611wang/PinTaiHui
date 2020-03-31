package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 查看全文
 * 圈子公告
 * 圈子介绍
 */
public class ReviewFullTextDialog extends Dialog implements View.OnClickListener {
    private TextView titleTextView, contentTextView, confirmTextView;

    public ReviewFullTextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setData(String title,String content){
        titleTextView.setText(title);
        contentTextView.setText(content);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review_full_text);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext()) / 5 * 4;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }


    private void initViews() {
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        confirmTextView = findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                dismiss();
                break;
        }
    }
}
