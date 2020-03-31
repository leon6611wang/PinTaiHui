package com.zhiyu.quanzhu.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyu.quanzhu.R;

/**
 * 操作成功提示
 */
public class MessageToast {
    private static MessageToast successToast;
    private static Context context;
    private static Toast toast;
    private static TextView msgTextView;

    public static MessageToast getInstance(Context ctxt) {
        if (null == successToast) {
            synchronized (MessageToast.class) {
                successToast = new MessageToast();
                context = ctxt;
                toast = new Toast(context);
                initViews();
            }
        }
        return successToast;
    }

    private static void initViews() {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_message, null);
        msgTextView = view.findViewById(R.id.msgTextView);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    public void show() {
        toast.show();
    }

    public void show(String message) {
        toast.show();
        msgTextView.setText(message);
    }
}
