package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.zhiyu.quanzhu.R;

public class MessageMenuDownDialog extends Dialog {

    public MessageMenuDownDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_messagelist_menu_down);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
