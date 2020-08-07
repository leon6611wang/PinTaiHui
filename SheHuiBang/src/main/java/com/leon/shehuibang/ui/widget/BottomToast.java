package com.leon.shehuibang.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.shehuibang.R;
import com.leon.shehuibang.utils.ScreentUtils;


/**
 * 操作成功提示
 */
public class BottomToast {
    private static BottomToast successToast;
    private static Context context;
    private static Toast toast;
    private static TextView msgTextView;
    private static LinearLayout toastLayout;

    public static BottomToast getInstance(Context ctxt) {
        if (null == successToast) {
            synchronized (BottomToast.class) {
                successToast = new BottomToast();
                context = ctxt;
                toast = new Toast(context);
                initViews();
            }
        }
        return successToast;
    }

    private static void initViews() {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_bottom, null);
        toastLayout = view.findViewById(R.id.toastLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreentUtils.getInstance().getScreenWidth(context),
                (int) context.getResources().getDimension(R.dimen.dp_50));
        toastLayout.setLayoutParams(layoutParams);
        msgTextView = view.findViewById(R.id.msgTextView);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
    }

    public void show() {
        toast.show();
    }

    public void show(String message) {
        toast.show();
        msgTextView.setText(message);
    }
}
