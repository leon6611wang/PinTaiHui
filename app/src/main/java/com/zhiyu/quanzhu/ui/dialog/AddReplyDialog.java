package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

public class AddReplyDialog extends Dialog implements View.OnClickListener {
    private float ratio = 0.3627f;
    private int editHeight, dp_10, dp_20;
    private LinearLayout.LayoutParams ll;
    private EditText addEditText;
    private TextView cancelTextView, saveTextView;

    public AddReplyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        int screenwidth = ScreentUtils.getInstance().getScreenWidth(context);
        editHeight = (int) (ratio * screenwidth);
        System.out.println("editHeight: "+editHeight);
        dp_10 = (int) context.getResources().getDimension(R.dimen.dp_10);
        dp_20 = (int) context.getResources().getDimension(R.dimen.dp_20);
        ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, editHeight);
        ll.leftMargin = dp_10;
        ll.rightMargin = dp_10;
        ll.bottomMargin = dp_20;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_reply);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initViews();
    }

    private void initViews() {
        addEditText = findViewById(R.id.addEditText);
        addEditText.setLayoutParams(ll);
        cancelTextView = findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        saveTextView = findViewById(R.id.saveTextView);
        saveTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTextView:
                dismiss();
                break;
            case R.id.saveTextView:

                break;
        }
    }
}
