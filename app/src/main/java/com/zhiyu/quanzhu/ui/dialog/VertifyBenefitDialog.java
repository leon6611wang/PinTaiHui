package com.zhiyu.quanzhu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.utils.ScreentUtils;

/**
 * 认证好处
 */
public class VertifyBenefitDialog extends Dialog {
    private WebView webView;
    private TextView confirmTextView;
    public VertifyBenefitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setUrl(String url) {
        webView.loadUrl(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vertify_benefit);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ScreentUtils.getInstance().getScreenWidth(getContext()) / 5 * 4;
        params.height = ScreentUtils.getInstance().getScreenHeight(getContext()) / 2;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.shape_wait_dialog_bg);

        initViews();
    }

    private void initViews() {
        webView = findViewById(R.id.webView);
        confirmTextView=findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
